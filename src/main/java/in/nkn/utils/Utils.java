/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import dev.nknone.enums.Activity;
import dev.nknone.pojo.session.Token;
import helper.ActivityHelper;
import helper.GenericHelper;
import helper.SecurityViewHelper;
import in.nkn.exceptions.CookieNotFoundException;
import java.util.logging.Level;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import org.apache.log4j.Logger;

/**
 *
 * @author schauhan
 */
public class Utils {

    Logger log = Logger.getLogger(Utils.class);

    public Token getInitialAttr(HttpServletRequest request, HttpHeaders header, String serviceName, String realm, String browserId) {

        log.debug("realm is " + realm);
        String ip = "";
        try {
            ip = header.getRequestHeader("X-FORWARDED-FOR").get(0);

            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ex) {
            ip = request.getRemoteAddr();

        }
        log.debug("IP is " + ip);

        String ua = header.getRequestHeader("user-agent").get(0);
        log.debug("ua " + ua);

        try {
            serviceName = new in.nkn.utils.Cookies().getCookie("serviceName", request);
        } catch (CookieNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        log.debug("service " + serviceName);

//        String browserId = new in.nkn.utils.Cookies().getCookie("browserId", request);
//        log.debug("browser ID is " + browserId);

        String sessionId = null;
        try {
            sessionId = new in.nkn.utils.Cookies().getCookie("sessionId", request);
        } catch (CookieNotFoundException ex) {

            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.debug("session ID is " + sessionId);

        Token t = new in.nkn.utils.POJOMaker().makeToken(null, ip, ua, serviceName, browserId, sessionId, realm);

        return t;
    }

    public Token getSessionAttr(HttpServletRequest request, HttpHeaders header, String serviceName, String realm, String browserId) {

        log.debug("realm is " + realm);
        String ip = "";
        try {
            ip = header.getRequestHeader("X-FORWARDED-FOR").get(0);

            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception ex) {
            ip = request.getRemoteAddr();

        }
        log.debug("IP is " + ip);
        String ua = header.getRequestHeader("user-agent").get(0);
        log.debug("ua " + ua);

        log.debug("service " + serviceName);

//        String browserId = null;
//        try {
//            browserId = new in.nkn.utils.Cookies().getCookie("browserId", request);
//        } catch (CookieNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        log.debug("browser ID is " + browserId);

        String sessionId = null;

        try {
            sessionId = new in.nkn.utils.Cookies().getCookie("sessionId", request);
        } catch (CookieNotFoundException ex) {

            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.debug("session ID is " + sessionId);

        String tokenId = null;
        try {
            tokenId = new in.nkn.utils.Cookies().getCookie("tokenId", request);
        } catch (CookieNotFoundException ex) {

            java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.debug("token ID is " + tokenId);


        Token t = new in.nkn.utils.POJOMaker().makeToken(tokenId, ip, ua, serviceName, browserId, sessionId, realm);
        String userName = t.getUserName();


        return t;
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String script = "\n"
                + "function deleteCookies() {\n"
                + "    var cookies = document.cookie.split(';');console.log(cookies);\n"
                + "\n"
                + "    for (var i = 0; i < cookies.length; i++) {\n"
                + "        var cookie = cookies[i];\n"
                + "        \n"
                + "        var eqPos = cookie.indexOf(\"=\");\n"
                + "        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;\n"
                + "        document.cookie = name + \"=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;domain=.staging.nkn.in;\";\n"
                + "        \n"
                + "    }\n"
                + "}";
        try {
            System.out.println(script);
            engine.eval(script);
//            engine.eval("var x = 10;");
//	    engine.eval("var y = 20;");
//	    engine.eval("var z = x + y;");
//	    engine.eval("print (z);");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }
}
