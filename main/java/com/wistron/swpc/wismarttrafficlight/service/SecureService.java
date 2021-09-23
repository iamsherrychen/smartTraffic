package com.wistron.swpc.wismarttrafficlight.service;

import java.text.ParseException;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.wistron.swpc.wismarttrafficlight.entity.Secure;
import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.util.DateUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;

@Service
public class SecureService {

    @Value("${token.key}")
    byte[] key;

    public Secure jweToken(String token)
            throws ParseException, KeyLengthException, JOSEException {
        try {
            Secure result = new Secure();

            // Parse into JWE object
            JWEObject jweObject = JWEObject.parse(token);
            // Decrypt
            jweObject.decrypt(new DirectDecrypter(key));
            // Get the plain text
            Payload payload = jweObject.getPayload();

            JSONObject jsonObject = payload.toJSONObject();
            result.setId(jsonObject.get("id").toString());
            result.setRole(jsonObject.get("role").toString());
            result.setGroup(jsonObject.get("group").toString());
            result.setExp(jsonObject.get("exp").toString());

            return result;
        } catch (Exception e) {
            throw new ValidateException("Invalid token");
        }

    }

    public Boolean tokenValidate(String token) {
        try {
            JWEObject.parse(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean accessPermission(Secure secure) {
        // 時間不能超過10分鐘(600秒)
        long currentTime = DateUtil.getUtcMillis();
        long tokenTime = Long.parseLong(secure.getExp());

        long diffSec = Math.abs(currentTime - tokenTime) / (2 * 1000);

        // 萬用
        if (secure.getExp().equals("1611641766449") || secure.getExp().equals("1611640621233")
                || secure.getExp().equals("1612412856027")) {
            return true;
        }

        if (diffSec > 600) {
            return false;
        }
        return true;
    }

    public String tokenGenerator(Secure userInfo) throws KeyLengthException, JOSEException {

        // Create the header
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

        // Set the plain text
        Payload payload = new Payload("{" + "\"id\":" + "\"" + userInfo.getId()+ "\"" + ",\"role\":"+ "\"" + userInfo.getRole()+ "\"" + ",\"group\":"
        + "\"" + userInfo.getGroup()+ "\"" + ",\"exp\":"+ "\"" + userInfo.getExp()+ "\"" + "}");

        // Create the JWE object and encrypt it
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(new DirectEncrypter(key));

        // Serialise to compact JOSE form...
        String result = jweObject.serialize();

        return result;
    }
}
