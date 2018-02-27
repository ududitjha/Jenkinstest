/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author user
 */
public class RedirectUrl {

    public String getRedirectionURL(String serviceName) {
        Properties prop = new Properties();
        String url, protocol = null;
        try {
            prop.load(RedirectUrl.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            url = prop.getProperty(serviceName + "URL");
            protocol = prop.getProperty("protocol");
        } catch (IOException ex) {
            url = "http://storage.nkn.in";
            ex.printStackTrace();
        }
        return protocol + "://" + url;

    }
}
