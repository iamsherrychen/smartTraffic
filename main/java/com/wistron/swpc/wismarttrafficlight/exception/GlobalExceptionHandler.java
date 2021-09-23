package com.wistron.swpc.wismarttrafficlight.exception;

import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public TrafficResponse httpRequestMethodNotSupportedException(HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setStatus(405);
        return TrafficResponse.error();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public TrafficResponse resourceAccessException(HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setStatus(400);
        return TrafficResponse.error();
    }

    @ExceptionHandler(ValidateException.class)
    public TrafficResponse validateException(ValidateException ex, HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setStatus(400);
        return TrafficResponse.error().setResult(ex.getMsg());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public TrafficResponse httpMediaTypeNotSupportedException(HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        // 415 如果Content-Type发送了你不能使用
        response.setStatus(415);
        return TrafficResponse.error();
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public TrafficResponse httpMediaTypeNotAcceptableException(HttpServletResponse response) {
        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        // 406 如果Accept发送了标头，则不能使用
        response.setStatus(406);
        return TrafficResponse.error();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception ex) {
        logger.error("[traffic] global exception", ex);
        return ResponseEntity.status(500).header("Cache-Control", "no-cache, no-store, must-revalidate").header("Pragma", "no-cache").header("Expires", "0").header("X-Content-Type-Options", "nosniff").header("X-Frame-Options", "DENY").header("X-XSS-Protection", "1; mode=block").build();
    }

}
