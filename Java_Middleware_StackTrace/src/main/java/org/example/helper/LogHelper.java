package org.example.helper;


import jakarta.servlet.http.HttpServletRequest;
import org.example.entity.ExceptionLog;
import org.example.entity.RequestLog;

import java.time.LocalDateTime;

public class LogHelper {

    private static final String USER_NOT_KNOWN = "USER_NOT_KNOWN";

    /**
     * Authorization: Bearer <token>
     * Attempts to pull userId from token, otherwise returns “USER_NOT_KNOWN”.
     * In this example we kept the actual validation code minimal.
     */
    public static String parseUserIdFromJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return USER_NOT_KNOWN;
        }
        String jwtToken = authorizationHeader.substring(7).trim();

        try {
            // Here you can parse the token with the jjwt library and read the userId claim.
            // For example:
            // Claims claims = Jwts.parserBuilder()
            // .setSigningKey(signingKey)
            // .build()
            // .parseClaimsJws(jwtToken)
            // .getBody();
            // String userId = claims.get(“userId”, String.class);
            // return userId != null ? userId : USER_NOT_KNOWN;


            return "FAKE_USER_ID"; // Basic e.g
        } catch (Exception e) {
            return USER_NOT_KNOWN;
        }
    }

    public static ExceptionLog createExceptionLogWithCustomException(
            HttpServletRequest request,
            CustomException exception,
            ServerType serverType,
            int requestId) {

        String userId = parseUserIdFromJwtToken(request);
        String ipAddress = request.getRemoteAddr() != null ? request.getRemoteAddr() : "UNKNOWN_IP";

        ExceptionLog log = new ExceptionLog();
        log.setIpAddress(ipAddress);
        log.setServerType(serverType);
        log.setUserId(userId);
        log.setException(exception.getMessage());
        log.setStackTrace(getStackTraceAsString(exception));
        log.setErrorCode(exception.getErrorCodes());
        log.setLogDate(LocalDateTime.now().plusHours(3));
        log.setRequestId(requestId);

        return log;
    }

    public static ExceptionLog createExceptionLogWithException(
            HttpServletRequest request,
            Exception exception,
            ServerType serverType,
            int requestId) {

        String userId = parseUserIdFromJwtToken(request);
        String ipAddress = request.getRemoteAddr() != null ? request.getRemoteAddr() : "UNKNOWN_IP";

        ExceptionLog log = new ExceptionLog();
        log.setIpAddress(ipAddress);
        log.setServerType(serverType);
        log.setUserId(userId);
        log.setException(exception.getMessage());
        log.setStackTrace(getStackTraceAsString(exception));
        log.setErrorCode(418);
        log.setLogDate(LocalDateTime.now().plusHours(3));
        log.setRequestId(requestId);

        return log;
    }

    public static RequestLog createRequestLog(
            HttpServletRequest request,
            ServerType serverType) {

        String userId = parseUserIdFromJwtToken(request);
        String ipAddress = request.getRemoteAddr() != null ? request.getRemoteAddr() : "UNKNOWN_IP";
        String url = request.getRequestURI() != null ? request.getRequestURI() : "UNKNOWN_URL";

        RequestLog log = new RequestLog();
        log.setIpAddress(ipAddress);
        log.setLogDate(LocalDateTime.now().plusHours(3));
        log.setResponseCode(0);
        log.setServerType(serverType);
        log.setUrl(url);
        log.setUserId(userId);

        return log;
    }

    private static String getStackTraceAsString(Throwable t) {
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement elem : t.getStackTrace()) {
            sb.append(elem.toString()).append("\n");
        }
        return sb.toString();
    }
}
