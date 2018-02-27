/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

//import in.nkn.pojos.Generic.Authenticator;
import dev.nknone.pojo.session.Token;
import helper.GenericHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class OpenAM {

    private static String openAmHost;
    private static String openAmPort;
    Logger log = Logger.getLogger(OpenAM.class);
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        Properties prop = new Properties();
        try {
            prop.load(OpenAM.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            openAmHost = prop.getProperty("openAmHost");
            System.out.println("Getting the openAmHost:" + openAmHost);
            openAmPort = prop.getProperty("openAmPort");
            System.out.println("Getting the openAmPort:" + openAmPort);
        } catch (IOException ex) {
            System.out.println("Occurs an Exception is:" + ex);
            ex.printStackTrace();
        }
    }

    public String getTokenId(String userId, String password) {
        log.debug("getTokenId() method invoked ");
        log.debug("Getting the Token id for user id:" + userId + " and password:" + password + " from OpenAM");
        userId = (new URLParamEncoder()).encode(userId);
        log.debug("Encoded user id:" + userId);
        password = (new URLParamEncoder()).encode(password);
        log.debug("Encoded password:" + userId);
        //log.debug("Getting the Encoded  user id:"+userId+" and password:"+password);
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/authenticate?username=" + userId + "&password=" + password + "&uri=realm=users";
        ////System.out.println(url);
        log.debug("url for authentication of the user:" + userId + " is:" + url);
        String tokenId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        HttpGet httpGet = new HttpGet(url);

        try {
            log.debug("Inside try block of getTokenId() method");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                tokenId = line;
            }

            if (tokenId.substring(0, 8).equals("token.id")) {
                tokenId = tokenId.substring(9);
                log.debug("getting the Token ID : " + tokenId + " for user id : " + userId);
            } else {
                tokenId = "Authentication Failed";
                log.debug("getting the Token ID : " + tokenId + " for user id : " + userId);
            }
            rd.close();
            log.debug("getTokenId() method is successfully executed and return the toekn id : " + tokenId);
        } catch (ConnectTimeoutException e) {
            log.fatal("Connection Timed Out");
            e.printStackTrace();
        } catch (Exception e) {
            log.fatal("An exception occured in getTokeId() method of OpenAM class and Exception:" + e);
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
            log.debug("closed the socket connection");
        }

        //log.debug("getTokenId() method of OpenAM class is successfully executed and return the toekn id:"+tokenId);
        //System.out.println(tokenId);
        return tokenId;
    }

    public String getUserId(String tokenId) {
        log.debug("getUserId() method invoked");
        log.debug("Getting the user id by OpenAM for Token Id:" + tokenId);
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/attributes?subjectid=" + tokenId;
        log.debug("url is:" + url);
        String userId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new org.apache.http.client.methods.HttpGet(url);
        System.out.println(url);

        try {
            log.debug("Inside try block of getUserId() method");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                if (line.contains("value=uid=")) {
                    //To split the string (line) by "," character and get the sequence of tokens which store on StringTokenizer object. 
                    StringTokenizer st2 = new StringTokenizer(line, ",");
                    userId = st2.nextElement().toString().substring(32);
                    log.debug("Getting the user id:" + userId + " for Token ID:" + tokenId);
                }
            }
            rd.close();
            log.debug("getUserId() method of OpenAM class is successfully executed and return the user id:" + userId);
        } catch (ConnectTimeoutException e) {
            log.fatal("Connection Timed Out");
        } catch (Exception e) {
            log.fatal("An exception occured in getUserId() method of OpenAM class and Exception:" + e);
            e.printStackTrace();
        } finally {

            httpClient.getConnectionManager().shutdown();
            log.debug("closed the socket connection");
            //log.debug("finally block is successfully executed");
        }

        //log.debug("getUserId() method of OpenAM class is successfully executed and return the user id:"+userId);
        return userId;
    }

    public String getUserAttributes(String tokenId, String param) {
        log.debug("getUserAttributes() method invoked ");
        log.debug("Getting attributes of an user for token id:" + tokenId + " and Param:" + param);
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/attributes/abc?subjectid=" + tokenId;
        log.debug("url is:" + url);
        String userId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new org.apache.http.client.methods.HttpGet(url);
        String line = "";
        JSONObject user = null;
        String result = "null";
        try {
            log.debug("Inside try block of getUserAttributes() method");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                user = new JSONObject().fromObject(line).getJSONObject("User");
                result = user.getString(param);
                //log.debug("Get attributes of a user:"+result+"for Token ID:"+tokenId+" and Param:"+param);
                log.debug("Getting the string:" + result + "from User object for Param:" + param);
                log.debug("getUserAttributes() method of OpenAM class is successfully executed and return the result:" + result);
                break;
            }
            rd.close();
            //log.debug("try block of getUserAttributes() method is successfully executed");
        } catch (ConnectTimeoutException ex) {
            log.fatal("Connection Timed Out");
        } catch (Exception e) {
            log.fatal("An exception occured in getUserAttributes() method of OpenAM class and Exception:" + e);
            e.printStackTrace();
        } finally {
            log.debug("closed the socket connection");
            httpClient.getConnectionManager().shutdown();
            //log.debug("finally block is successfully executed");
        }

        //log.debug("getUserAttributes() method of OpenAM class is successfully executed and return the result:"+result);
        return result;
    }

    public boolean isTokenValidValidation(Token token) {

        String openAmHost = null;
        String openAmPort = null;
        String realm = null;
        Properties prop = new Properties();
        try {
            prop.load(OpenAM.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            openAmHost = prop.getProperty("openAmHost");
            openAmPort = prop.getProperty("openAmPort");
            realm = prop.getProperty("accountsRealm");

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

        String queryParam = new URLGenerator().getQueryParams(token);
        if (queryParam == null) {
            return false;
        }

        realm = URLParamEncoder.encode(realm);

        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/isTokenValid?" + queryParam + "&realm=" + realm;
        log.debug("Checking is token valid with ReST URL " + url);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
        String res = null;
        boolean isValidToken = false;

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            res = sb.toString();
            rd.close();
            if (res.equals("boolean=true")) {
                isValidToken = true;
            }

        } catch (ConnectTimeoutException e) {
            log.fatal("OpenAM Connection Timed Out " + e.getLocalizedMessage());
        } catch (IOException ex) {
            log.fatal("OpenAM can not respond " + ex.getLocalizedMessage());
            ex.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        log.debug("Returning " + isValidToken + " from isTokenValid method");
        return isValidToken;
    }

    public void logout(String tokenId) {
        log.debug("called the logout() method of OpenAM class");
        log.debug("To Logout of the user according Token Id:" + tokenId + " by OpenAM");
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/logout?subjectid=" + tokenId;
        log.debug("Getting the url:" + url);
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
        String res = null;
        try {
            log.debug("Inside try block of logout() method");
            HttpResponse response = httpClient.execute(httpGet);
            log.debug("try block of logout() method is successfully executed");
            log.debug("logout() method of OpenAM class is successfully executed");
        } catch (ConnectTimeoutException ex) {
            log.fatal("Connection Timed Out");

        } catch (IOException ex) {
            log.fatal("An Error occured in logout() method of OpenAM class and Exception:" + ex);
            ex.printStackTrace();
        } finally {
            log.debug("closed the socket connection");
            httpClient.getConnectionManager().shutdown();
            //log.debug("finally block is successfully executed");
        }
        //log.debug("logout() method of OpenAM class is successfully executed");
    }

    public String isValidCredentials(String userName, String password, String realm, String userAgent, String ip, String serviceName, String browserID) {

        String openAmHost = null;
        String openAmPort = null;

        Properties prop = new Properties();
        String returnValue = "Invalid";
        try {
            prop.load(OpenAM.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            openAmHost = prop.getProperty("openAmHost");
            openAmPort = prop.getProperty("openAmPort");

        } catch (IOException ex) {

            ex.printStackTrace();

        }
        realm = URLParamEncoder.encode(realm);
        userAgent = URLParamEncoder.encode(userAgent);
        ip = URLParamEncoder.encode(ip);
        serviceName = URLParamEncoder.encode(serviceName);
        if (browserID == null) {
            browserID = "null";
        }
        browserID = URLParamEncoder.encode(browserID);

        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/authenticate?realm=" + realm
                + "&ip=" + ip + "&ua=" + userAgent + "&service=" + serviceName + "&browserID=" + browserID;
        log.debug("Validating Credentials with ReST URL " + url);
        // boolean userAuthentic = false;
        String tokenId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

//        userName = URLParamEncoder.encode(userName);
//        password = URLParamEncoder.encode(password);
        urlParameters.add(new BasicNameValuePair("username", userName));
        urlParameters.add(new BasicNameValuePair("password", password));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = httpClient.execute(post);
            System.out.println("response " + response);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                tokenId = line;
                returnValue = line;
                System.out.println(line);
            }
            rd.close();
        } catch (ConnectTimeoutException ex) {
            log.fatal("OpenAM Connection  Timed Out " + ex.getLocalizedMessage());
        } catch (Exception e) {
            log.fatal("OpenAM can not respond " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
//        if (tokenId.substring(0, 8).equals("token.id")) {
//            
//            userAuthentic = true;
//        }
        return returnValue;
    }

    public Token getToken(String tokenId, String browserId, String ip, String ua, String service) {
        log.debug("getToken() method invoked");
        //To create the Token object for this token id
        Token token = new Token();
        String jsonString = getUserName(tokenId, browserId, ip, ua, service);
        System.out.println("JSON String is " + jsonString);
        NKNUserPOJO user = new JSONParser().getNKNUserPOJO(jsonString);
        String userFullName = user.getFirstName();//+ " " + user.getLastName();
        String profileCreated = String.valueOf(user.getIsProfiled());
        String userName = user.getUseridofnknuser();
        boolean isProfiled = profileCreated.equals("1") ? true : false;
        //Now to set the token id on Token object
        token.setTokenId(tokenId);
        log.debug("Token ID : " + tokenId + " to Token Object is set.");
        //At first to get the user id by getUserId()method for this token id and then to set this user id on Token object
        token.setUserName(userName);
        log.debug("User id: " + userName + " to Token object is set.");
        //At first to check the validation of this token id by isTokenValid() method which return the boolean value and now to set this boolean value on Token object 
        token.setIsValid(true);

        log.debug("Token Validity: true to Token object is set.");
        log.debug("getToken() method of OpenAM class is successfully executed and return the Token Object");
        return token;
    }

    public String getTokenId(String userId, String password, String realm) {
        log.debug("called the getTokenId() method of OpenAM class");
        log.debug("To Getting the Token Id of the visitor by OpenAM according to the userId,password and realm of the visitor");
        userId = (new URLParamEncoder()).encode(userId);
        password = (new URLParamEncoder()).encode(password);
        log.debug("Encoded the user id:" + userId + " and password:" + password);
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/authenticate?username=" + userId + "&password=" + password + "&uri=realm=" + realm;
        ////System.out.println(url);
        log.debug("Getting the url:" + url);
        String tokenId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);

        try {
            log.debug("Inside try block of getTokenId() method");
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                tokenId = line;
            }

            if (tokenId.substring(0, 8).equals("token.id")) {
                tokenId = tokenId.substring(9);
                log.debug("Getting the Token Id:" + tokenId + " of the visitor for userId:" + userId + ", password:" + password + " and realm:" + realm);
                log.debug("getTokenId() method of OpenAM class is successfully executed and return the Token id:" + tokenId);
            } else {
                log.debug("Authentication Failed for the userId,password and realm of the visitor");
                tokenId = "Authentication Failed";
            }
            //log.debug("Get the Token ID:"+tokenId+" of the visitor");
            rd.close();
        } catch (ConnectTimeoutException ex) {
            log.fatal("Connection Timed Out");

        } catch (Exception e) {
            log.fatal("An exception occured in getTokeId() method of OpenAM class and Exception:" + e);
            e.printStackTrace();
        } finally {
            log.debug("closed the socket connection");
            httpClient.getConnectionManager().shutdown();
        }
        //System.out.println(tokenId);
        return tokenId;
    }

    public String getUserNameDeatil(String tokenId, String browserId, String ip, String ua, String service) {
        String openAmHost = null;
        String openAmPort = null;
        String realm = null;
        Properties prop = new Properties();
        try {
            prop.load(OpenAM.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            openAmHost = prop.getProperty("openAmHost");
            openAmPort = prop.getProperty("openAmPort");
            realm = prop.getProperty("accountsRealm");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tokenId = URLParamEncoder.encode(tokenId);

        browserId = browserId == null ? "null" : browserId;
        browserId = URLParamEncoder.encode(browserId);
        realm = URLParamEncoder.encode(realm);
        ip = URLParamEncoder.encode(ip);
        ua = URLParamEncoder.encode(ua);
        service = URLParamEncoder.encode(service);
        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/attributes?tokenID=" + tokenId
                + "&browserID=" + browserId + "&ip=" + ip + "&ua=" + ua + "&service=" + service + "&realm=" + realm;
        System.out.println("URL is " + url);

        String userName = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new org.apache.http.client.methods.HttpGet(url);

        try {

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(entity.getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            userName = sb.toString();
            rd.close();
        } catch (ConnectTimeoutException ex) {
            log.fatal("Connection Timed Out");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return userName;
    }

    public String getUserName(String tokenId, String browserId, String ip, String ua, String service) {

        log.debug("getUserName Method called with following details");

        String uri = "users";

        try {
            // tokenId = URLParamEncoder.encode(tokenId);
            // browserId = browserId == null ? "null" : browserId;
            // browserId = URLParamEncoder.encode(browserId);
            // ua = URLParamEncoder.encode(ua);
            // service = URLParamEncoder.encode(service);
            // ip = URLParamEncoder.encode(ip);
            // uri = URLParamEncoder.encode(uri);

            log.debug("Attributes Method called with following details");
            log.debug("tokenID: " + tokenId);
            log.debug("realm: " + uri);
            log.debug("browserID: " + browserId);
            log.debug("IP: " + ip);
            log.debug("UA: " + ua);
            log.debug("service: " + service);
            if (uri.equals("users") || uri.equals("admin")) {
                log.debug("Getting attributes for " + uri + " realm");
                return new GenericHelper().Attributes(tokenId, uri, browserId, ip, ua, service, sdfDate);
            } else {
                log.debug("InValid Realm");
                return "Failure";
            }
        } catch (Exception e) {
            log.error("Exception caught with details as " + e.getMessage());
            e.printStackTrace();
            return "Failure";
        }

    }

    public boolean logout(String tokenId, String ip, String ua, String service, String browserId) {

        String openAmHost = null;
        String openAmPort = null;
        String realm = null;
        Properties prop = new Properties();
        try {
            prop.load(OpenAM.class.getClassLoader().getResourceAsStream("/properties/LDAP.properties"));
            openAmHost = prop.getProperty("openAmHost");
            openAmPort = prop.getProperty("openAmPort");
            realm = prop.getProperty("accountsRealm");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        tokenId = URLParamEncoder.encode(tokenId);
        ip = URLParamEncoder.encode(ip);
        ua = URLParamEncoder.encode(ua);
        realm = URLParamEncoder.encode(realm);
        System.out.println("Service is " + service);
        service = URLParamEncoder.encode(service);
        browserId = URLParamEncoder.encode(browserId);

        String url = "http://" + openAmHost + ":" + openAmPort + "/openam/identity/logout?tokenID=" + tokenId
                + "&browserID=" + browserId + "&ip=" + ip + "&ua=" + ua + "&service=" + service + "&realm=" + realm;

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpGet httpGet = new HttpGet(url);
        String res = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            return true;

        } catch (ConnectTimeoutException ex) {
            log.fatal("Connection Timed Out");
            return false;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }

    public boolean isTokenValid(Token token) {

        log.debug("isTokenValid Called");
        String res = null;
        boolean tokenValid = false;

        try {

            String tokenId = token.getTokenId();
            String browserId = token.getBrowserId();
            String ua = token.getUserAgent();
            String serviceName = token.getServiceName();
            String ip = token.getIp();
            String uri = token.getRealm();
            // tokenId = URLParamEncoder.encode(tokenId);
            // browserId = URLParamEncoder.encode(browserId);
            // ua = URLParamEncoder.encode(ua);
            // serviceName = URLParamEncoder.encode(serviceName);
            // ip = URLParamEncoder.encode(ip);
            //// uri = URLParamEncoder.encode(uri);



            log.debug("tokenID: " + tokenId);
            log.debug("realm: " + uri);
            log.debug("browserID: " + browserId);
            log.debug("IP: " + ip);
            log.debug("UA: " + ua);
            log.debug("service: " + serviceName);
            if (uri.equals("users") || uri.equals("admin") || uri.equals("subadmin")) {

                log.debug("Calling validate token for GenericHelper");

                res = new GenericHelper().validateToken(tokenId, uri, browserId, ip, ua, serviceName, sdfDate);
                System.out.println("res: " + res);
                log.debug(" Token validation " + res);

                if (res.equals("boolean=true")) {
                    tokenValid = true;
                } else {
                    tokenValid = false;
                }
            } else {
                log.debug("InValid Realm");
                tokenValid = false;
            }

            return tokenValid;
        } catch (Exception e) {
            log.error("Exception caught with message " + e.getMessage());
            e.printStackTrace();
            return tokenValid;

        }


    }

    public boolean isTokenValidWithoutService(Token token) {

        log.debug("isTokenValid Called");
        String res = null;
        boolean tokenValid = false;

        try {

            String tokenId = token.getTokenId();
            String browserId = token.getBrowserId();
            String ua = token.getUserAgent();
            String serviceName = token.getServiceName();
            String ip = token.getIp();
            String uri = token.getRealm();
            // tokenId = URLParamEncoder.encode(tokenId);
            // browserId = URLParamEncoder.encode(browserId);
            // ua = URLParamEncoder.encode(ua);
            // serviceName = URLParamEncoder.encode(serviceName);
            // ip = URLParamEncoder.encode(ip);
            //// uri = URLParamEncoder.encode(uri);



            log.debug("tokenID: " + tokenId);
            log.debug("realm: " + uri);
            log.debug("browserID: " + browserId);
            log.debug("IP: " + ip);
            log.debug("UA: " + ua);
            log.debug("service: " + serviceName);
            if (uri.equals("users") || uri.equals("admin") || uri.equals("subadmin")) {

                log.debug("Calling validate token for GenericHelper");

                //res = new GenericHelper().validateTokenWithoutService(tokenId, uri, browserId, ip, ua, sdfDate);
                res = new GenericHelper().validateTokenWithoutService(tokenId,uri, browserId, ip, ua, sdfDate);
                System.out.println("res: " + res);
                log.debug(" Token validation " + res);

                if (res.equals("boolean=true")) {
                    tokenValid = true;
                } else {
                    tokenValid = false;
                }
            } else {
                log.debug("InValid Realm");
                tokenValid = false;
            }

            return tokenValid;
        } catch (Exception e) {
            log.error("Exception caught with message " + e.getMessage());
            e.printStackTrace();
            return tokenValid;

        }


    }
}
