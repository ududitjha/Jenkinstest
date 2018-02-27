/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import dev.nknone.pojo.session.Token;
import in.nkn.exceptions.CookieNotFoundException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.log4j.Logger;

/**
 *
 * @author schauhan
 */
public class GenericUtils {

    Logger log = Logger.getLogger(GenericUtils.class);

    public Token getInitialAttr(HttpServletRequest request, HttpHeaders header, String serviceName, String realm) throws CookieNotFoundException {

        log.debug("realm is " + realm);
        String ip = request.getRemoteAddr();
        log.debug("IP is " + ip);
        String ua = header.getRequestHeader("user-agent").get(0);
        log.debug("ua " + ua);

        log.debug("service " + serviceName);

        String browserId = null;
        try {
            browserId = new in.nkn.utils.Cookies().getCookie("browserId", request);
        } catch (CookieNotFoundException ex) {
            java.util.logging.Logger.getLogger(GenericUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.debug("browser ID is " + browserId);

        String sessionId = new in.nkn.utils.Cookies().getCookie("sessionId", request);
        log.debug("session ID is " + sessionId);

        Token t = new in.nkn.utils.POJOMaker().makeToken(null, ip, ua, serviceName, browserId, sessionId, realm);
        return t;
    }

    public String removeDeviceSettings(String ss) {
        
         log.debug("removeDeviceSettings method called ");
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(ss);
        JSONObject innerJSON = jsonObject.getJSONObject("User");
        JSONObject jo = (JSONObject) JSONSerializer.toJSON(innerJSON);

        JSONArray ja = jo.getJSONArray("settings");

        if (ja.contains("twoStepUsingEither")) {
            ja.discard("twoStepUsingEither");
        }
        if (ja.contains("twoStepOnEmail")) {
            ja.discard("twoStepOnEmail");
        }
        if (ja.contains("twoStepOnPhone")) {
            ja.discard("twoStepOnPhone");
        }
        jsonObject.getJSONObject("User").put("settings", ja);
        log.debug("Final settings are  " + ja.toString());
        return jsonObject.toString();
    }
}
