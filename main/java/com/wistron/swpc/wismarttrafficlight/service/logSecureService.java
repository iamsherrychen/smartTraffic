package com.wistron.swpc.wismarttrafficlight.service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class logSecureService {
    public static String vaildLog(String logs) {
        List<String> list = new ArrayList<String>();
        list.add("%0a");
        list.add("%0A");
        list.add("%0d");
        list.add("%0D");
        list.add("\r");
        list.add("\n");
        String normalize = Normalizer.normalize(logs, Normalizer.Form.NFKC);
        for (String str : list) {
            normalize = normalize.replace(str, "");
        }
        return normalize;
    }
}
