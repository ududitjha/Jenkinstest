/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import dev.nknone.pojo.session.Device;
import dev.nknone.pojo.session.Session;
import dev.nknone.pojo.session.Token;
import java.util.Date;

/**
 *
 * @author mkumar1
 */
public class POJOMaker {

    public Token makeToken(String tokenId, String ip, String userAgent, String serviceName, String browserID, String sessionID, String realm) {
        Token token = new Token();
        token.setBrowserId(browserID);
        token.setIp(ip);
        token.setServiceName(serviceName);
        token.setTokenId(tokenId);
        token.setUserAgent(userAgent);
        token.setSessionId(sessionID);
        token.setRealm(realm);
        return token;
    }
    
    public Token makeToken(String tokenId, String ip, String userAgent, String serviceName, String browserID) {
        Token token = new Token();
        token.setBrowserId(browserID);
        token.setIp(ip);
        token.setServiceName(serviceName);
        token.setTokenId(tokenId);
        token.setUserAgent(userAgent);
        
        return token;
    }

    public Token makeToken(String ip, String userAgent, String serviceName, String browserID, String sessionID, String realm) {
        Token token = new Token();
        token.setBrowserId(browserID);
        token.setIp(ip);
        token.setServiceName(serviceName);
        token.setUserAgent(userAgent);
        token.setSessionId(sessionID);
        token.setRealm(realm);
        return token;
    }

    public Device makeDevice(String browserID, String userName, String ip) {

        Device devicePojo = new Device();
        devicePojo.setDeviceId(browserID);
        devicePojo.setCurrentIP(ip);
        devicePojo.setCurrentUserName(userName);
        return devicePojo;
    }
      public Session makeSession(Token token,String sessionStatus) {

       
        //token.setSessionId(sessionId);
        Session sessionPojo = new Session();
//        sessionPojo.setId(token.getSessionId());
        sessionPojo.setSessionStatus(sessionStatus);
     
        return sessionPojo;
    }
}
