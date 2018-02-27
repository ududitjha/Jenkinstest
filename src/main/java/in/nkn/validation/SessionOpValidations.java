/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.validation;

/**
 *
 * @author mkumar1
 */
public class SessionOpValidations extends ValidationFramework {

    public String validateSessionAttributes(String ipAddress, String tokenId, String browserId, String userAgent, String service, String sessionId) {
        String msg1 = validateIP(ipAddress);
        String msg2 = validateToken(tokenId);
        String msg3 = validateBrowserId(browserId);
        String msg4 = validateUserAgent(userAgent);
        String msg5 = validateService(sessionId);
        return finalMessage(msg1, msg2, msg3, msg4, msg5);
    }

    public String validateInitialSessionAttributes(String ipAddress, String tokenId, String browserId, String userAgent, String service, String sessionId) {
        String msg1 = validateIP(ipAddress);
        String msg2 = validateToken(tokenId);
        String msg3 = validateBrowserId(browserId);
        String msg4 = validateUserAgent(userAgent);
        String msg5 = validateService(sessionId);
        return finalMessage(msg1, msg2, msg3, msg4, msg5);
    }

    public String validateInitialSession(String ipAddress, String tokenId, String browserId, String userAgent, String service, String sessionId) {
        String msg1 = validateIP(ipAddress);
        String msg2 = validateBrowserId(browserId);
        String msg3 = validateUserAgent(userAgent);
        String msg4 = validateService(sessionId);
        return finalMessage(msg1, msg2, msg3, msg4);
    }

    private String validateIP(String ipAddress) {
        String msg1 = isIPAddress(ipAddress);
        String msg2 = lengthGTE(ipAddress, 7, "IP Address");
        String msg3 = lengthLTE(ipAddress, 15, "IP Address");
        return finalMessage(msg1, msg2, msg3);
    }

    private String validateToken(String token) {
        String msg1 = isAlphanumericOnly(token, "Token Id");
        String msg2 = LengthEquaility(token, 64, "Token Id");
        return finalMessage(msg1, msg2);
    }

    private String validateBrowserId(String browserId) {
        String msg1 = isAlphanumericWithHyphenOnly(browserId, "Browser Id");
        String msg2 = LengthEquaility(browserId, 36, "Browser Id");
        return finalMessage(msg1, msg2);
    }

    private String validateSessionId(String sessionId) {
        String msg1 = isAlphanumericWithHyphenOnly(sessionId, "Session Id");
        String msg2 = LengthEquaility(sessionId, 36, "Session Id");
        return finalMessage(msg1, msg2);
    }

    private String validateUserAgent(String userAgent) {

        String msg1 = lengthGTE(userAgent, 1, "User Agent");
        String msg2 = lengthLTE(userAgent, 200, "User Agent");
        return finalMessage(msg1, msg2);
    }

    private String validateService(String service) {

        String msg1 = lengthGTE(service, 1, "Service");
        String msg2 = lengthLTE(service, 20, "Service");
        String msg3 = isAlphabets(service, "Service");
        return finalMessage(msg1, msg2, msg3);
    }
}
