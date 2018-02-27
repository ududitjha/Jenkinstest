/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.core;

import dev.nknone.pojo.session.Token;
import helper.GenericHelper;
import in.nkn.exceptions.CookieNotFoundException;
import in.nkn.utils.AESEncryption;
import in.nkn.utils.CheckServicesHelper;
import in.nkn.utils.Cookies;
import in.nkn.utils.JSONParser;
import in.nkn.utils.NKNUserPOJO;
import in.nkn.utils.OpenAM;
import in.nkn.utils.POJOMaker;
import in.nkn.utils.ServicesXML;
import in.nkn.utils.TokenMaker;
import in.nkn.utils.Utils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author schauhan
 */
public class CheckServices extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Logger log = Logger.getLogger(CheckServices.class);
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //*******Get Params From Session Manager**********
        //String param1 = request.getParameter("service");
        //System.out.println("param1: " + param1);
        //***********************************************
        response.setContentType("text/html;charset=UTF-8");

        /*
         * Check service
         * IF Valid
         *      Check cookies browser and token.  
         *          If found 
         *              validate
         *                  If Valid
         *                      Check Profile
         *                          If Profiled
         *                               Services Home Page
         *                          Else
         *                              Profile Creation Page
         *                  Else 
         *                      Present Login Page
         *  Else
         *      Present Invalid Service Page
         */
        //String url = "http://nknone.staging.nkn.in:8080/NKNOne";
        String url = "http://accounts.staging.nkn.in:8080/Accounts";
        log.debug("checkservice method called");
        String service = "";
        String tokenId = "";


        try {
            service = request.getParameter("service");
            log.debug("service: " + service);
        } catch (Exception e) {
            service = "Accounts";
//            try {
//                service = new Cookies().getCookie("serviceName", request);
//            } catch (CookieNotFoundException ex) {
//                service = "Accounts";
//            }

        }
        System.out.println("Request URL: " + new Utils().getFullURL(request));

        log.debug("Service is " + service);
        //ServicesXML servicesXML = new CheckServicesHelper().checkServiceByXML(service, request);
        ServicesXML servicesXML = new CheckServicesHelper().checkServiceByEJB(service);


        if (servicesXML != null) {
            new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
            String browserId;
            NKNUserPOJO user = new NKNUserPOJO();

            log.debug("Getting TokenID from Browser ... ");
            try {
                tokenId = new Cookies().getCookie("tokenId", request);
                //tokenId = request.getParameter("tokenId");
            } catch (CookieNotFoundException | NullPointerException ex) {
                tokenId = null;
                ex.printStackTrace();
            }
            log.debug("tokenId: " + tokenId);
            log.debug("Getting BrowserID from Browser ... ");

            try {

                browserId = new Cookies().getCookie("browserId", request);
            } catch (Exception e) {
                browserId = null;
            }
            log.debug("browserId: " + browserId);
            String ip = request.getRemoteAddr();
            String ua = request.getHeader("user-agent");

            log.debug("IP, User-Agent and Service are " + ip + ", " + ua + " and " + service + " respectively.");

            Token token = new POJOMaker().makeToken(tokenId, ip, ua, service, browserId);
            token.setRealm("users");

            if (!(tokenId == null && browserId == null)) {


                ///  isTokenValid Rest Api changes to tokenValidation Method.............

                boolean isTokenValid = new OpenAM().isTokenValid(token);

                log.debug("isTokenValid: " + isTokenValid);
                //boolean isTokenValid = new OpenAM().tokenValidation(token);
                if (!isTokenValid) {
                    new Cookies().setCookie("tokenId", null, servicesXML.getDomain(), 0, response);
                    new Cookies().setCookie("browserId", null, servicesXML.getDomain(), 0, response);
                    log.debug("service: " + service + " Domain Name: " + servicesXML.getDomain());
                    //new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
                    response.sendRedirect(url + "/NKN/index.html?service=" + service);
                    return;
                }


                try {
                    log.debug("Getting User Name from token..");
                    String checkClientTokenId = "";
                    if (!"Accounts".equals(service)) {
                        checkClientTokenId = new GenericHelper().checkClientExistsOrNot(tokenId, browserId, ip, "users", ua, service, sdfDate);
                    } else {
                        checkClientTokenId = "Failure";
                    }
                    if ("Failure".equals(checkClientTokenId)) {
                        //i.e client Token not exists
                        String userJSONString = new OpenAM().getUserName(tokenId, browserId, ip, ua, service);
                        System.out.println("User JSON String is " + userJSONString);

                        user = new JSONParser().getNKNUserPOJO(userJSONString);

                        String profileCreated = String.valueOf(user.getIsProfiled());
                        log.debug("User Name from token " + user.getUseridofnknuser());
                        if (profileCreated.equals("1")) {
                            log.debug("Profile found for user ...Redirecting to Service URL..");
                            if (service.equals("Accounts")) {

                                response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/home.html");
                            } else {
                                try {
                                    JSONObject userDetails = new JSONParser().getUserDetailsJSON(user.getUseridofnknuser());
                                    //
                                    //generate new token id for another client
                                    //userDetails.put("tokenId", tokenId);
                                    userDetails.put("tokenId", tokenId);
                                    userDetails.put("ip", ip);
                                    userDetails.put("browserId", browserId);
                                    userDetails.put("ua", ua);
                                    userDetails.put("service", service);
                                    //for each client
                                    String newTokenId = DigestUtils.sha256Hex(userDetails.get("userName").toString() + System.currentTimeMillis());
                                    userDetails.put("localTokenId", newTokenId);

                                    System.out.println("Encrypting " + userDetails.toString() + " from checkservices");
                                    String encryptedText = AESEncryption.encryptIntoHex(userDetails.toString(), servicesXML.getApi_key());

                                    String serviceURL = servicesXML.getUrl();
                                    //url = serviceURL + "/KeyCheck?userJSON=" + encryptedText;
                                    url = serviceURL + "?string=" + encryptedText + "&status=false";
                                    System.out.println("Redirecting to URL after profile found " + url);
                                    log.debug("userJSON is :" + encryptedText);

                                    //new HTTPHelper().makeHTTPGetRequest(url);

//                                Token tokenPojo = new TokenMaker().tokenGenerator(request, tokenId);
//                                tokenPojo.setUserName(userDetails.get("userName").toString());
//                                tokenPojo.setServiceName(service);
//                                tokenPojo.setNewTokenId(newTokenId);
                                    String updateResult = new GenericHelper().updateIntoCouchbase(service + "TokenId", newTokenId, tokenId, browserId, ip, "users", ua, service, sdfDate);
                                    //Document inserted into Couchbase
                                    log.info("Document inserted into couchbase");
                                    if (!"FAILURE".equals(updateResult)) {
                                        log.debug("Making Call to servlet " + url);
                                        response.sendRedirect(url);
                                    } else {
                                        response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html?service=" + service);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            log.info("User " + user + " logged in to " + service + " .");

                        } else {
                            log.debug("Profile not found for user ...Redirecting to Profile Page..");
                            new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
                            response.sendRedirect(url + "/NKN/ProfilePage.html");
                            log.info("User " + user + " redirected to profile creation.");
                        }
                    } else {
                        log.debug("rdirecting to password page");
                        //response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/PasswordPage.html");
                        //new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
                        response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/PasswordPage.html?action=update&service=" + service);
                    }
                } catch (NullPointerException e) {
                    log.debug("OpenAM returened null value on getting username from token.\n Or Cookie Invalid/Not Found");

                    //new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);

                    log.info("Service cookie set with value Accounts, presenting GenericLogin page");

                    response.sendRedirect(url + "/NKN/index.html?service=" + service);
                    e.printStackTrace();
                }


            } else {

                System.out.println("Service is " + service + " service domain is " + servicesXML.getDomain());

                ServicesXML servicesXMLAcc = new CheckServicesHelper().checkServiceByEJB(service);
                //new Cookies().setCookie("serviceName", service, servicesXMLAcc.getDomain(), 5 * 60 * 60, response);
                log.info("TokenId or Browser id is null");
                log.info("url: " + url);
                response.sendRedirect(url + "/NKN/index.html?service=" + service);
                //response.sendRedirect("NKN/index.html");

            }

        } else {
            log.info("Invalid Service.");
            //new Cookies().setCookie("serviceName", "Accounts", servicesXML.getDomain(), 5 * 60 * 60, response);
            response.sendRedirect(url + "/NKN/index.html?service=" + service);
        }

    }
// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
