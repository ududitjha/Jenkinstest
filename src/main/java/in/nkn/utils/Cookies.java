/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import in.nkn.exceptions.CookieNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author mkumar1
 */
public class Cookies {

    Logger log = Logger.getLogger(Cookies.class);

    public String getCookie(String cookieName, HttpServletRequest request) throws CookieNotFoundException {

        String cookieValue = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie ck : cookies) {

                if (ck.getName().equals(cookieName)) {
                    cookieValue = ck.getValue();
                    System.out.println("cookie Domain is " + ck.getDomain());
                    break;
                }
            }
        }
        return cookieValue;
    }

    public void setCookie(String cookieName, String cookieValue, String cookieDomain, int cookieAgeHours, HttpServletResponse response) {
        Cookie c = new Cookie(cookieName, cookieValue);
        c.setDomain(cookieDomain);
        c.setPath("/");
        c.setMaxAge(cookieAgeHours * 60 * 60);
        response.addCookie(c);
        System.out.println("Set Cookie  " + cookieName + " value " + cookieValue + " domain " + cookieDomain);
    }

    public boolean saveSessionAndBrowserCookie(String service, HttpServletRequest request, HttpServletResponse response, String browserID, String sessionId) {
        try {
            //ServicesXML services = new CheckServicesHelper().checkServiceByXML(service, request);
            ServicesXML services = new CheckServicesHelper().checkServiceByEJB(service);
            log.debug("Cookie domain is " + services.getDomain());
            setCookie("browserId", browserID, services.getDomain(), 5, response);
            setCookie("sessionId", sessionId, services.getDomain(), 5, response);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Cookie eraseCookie(String strCookieName, String strPath) {
        Cookie cookie = new Cookie(strCookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath(strPath);
        return cookie;
    }

    public static void main(String[] args) {
    }
}
