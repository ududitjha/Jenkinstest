/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import dev.nknone.pojo.session.Token;
import in.nkn.exceptions.CookieNotFoundException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Administrator
 */
public class TokenMaker {

    public Token tokenGenerator(HttpServletRequest request, String tokenId) {
        try {
            String browserId = null;
            String sessionId = null;
            try {
                browserId = new in.nkn.utils.Cookies().getCookie("browserId", request);
            } catch (CookieNotFoundException ex) {
                java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sessionId = new in.nkn.utils.Cookies().getCookie("sessionId", request);
            } catch (CookieNotFoundException ex) {
                java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
            String service = new in.nkn.utils.Cookies().getCookie("serviceName", request);
            String ip = "";
            try {
                ip = request.getHeader("X-FORWARDED-FOR");
                if (ip == null) {
                    ip = request.getRemoteAddr();
                }
            } catch (Exception ex) {
                ip = request.getRemoteAddr();
            }
            String ua = request.getHeader("user-agent");
            //String tokenid = request.getParameter("tokenId");

            System.out.println("tokenId: " + tokenId);
            Token token = new Token();
            token.setBrowserId(browserId);
            token.setTokenId(tokenId);
            token.setIp(ip);
            token.setServiceName(service);
            token.setUserAgent(ua);
            token.setRealm("users");
            token.setSessionId(sessionId);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}
