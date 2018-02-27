/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.core;

import dev.nknone.pojo.session.Token;
import helper.GenericHelper;
import in.nkn.utils.CheckServicesHelper;
import in.nkn.utils.Cookies;
import in.nkn.utils.ServicesXML;
import in.nkn.utils.TokenMaker;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class ClientManagement extends HttpServlet {

    Logger log = Logger.getLogger(CheckServices.class);
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //JSONObject result = new JSONObject();
        try {
            log.info("ClientManagement called");

            //String clientTokenId = request.getParameter("clientTokenId");
            String service = request.getParameter("service");
            //log.debug("clientTokenId: " + clientTokenId);
            log.debug("serviceName: " + service);
            ServicesXML servicesXML = new CheckServicesHelper().checkServiceByEJB(service);
            if (servicesXML != null) {
                //SecretKey sk = AESEncryption.getSecretKeyFromString(servicesXML.getApi_key());
                String parentTokenId = new Cookies().getCookie("tokenId", request);
                log.debug("parentTokenId: " + parentTokenId);
                //make Token
                Token token = new TokenMaker().tokenGenerator(request, parentTokenId);
                log.debug("token " + token.toString());
                if (token != null) {
//                    String clientTokenStatus = new GenericHelper().validateSpecificClient(clientTokenId, parentTokenId, token.getBrowserId(),
//                            token.getIp(), "users", token.getUserAgent(), service, sdfDate);
//                    log.debug("clientTokenStatus " + clientTokenStatus);
//                    String updateResult = new GenericHelper().deleteIntoCouchbase(service + "TokenId", parentTokenId, token.getBrowserId(),
//                            token.getIp(), "users", token.getUserAgent(), service, sdfDate);

                    String updateResult = new GenericHelper().updateIntoCouchbase(service + "TokenId","timeout",parentTokenId, token.getBrowserId(),
                            token.getIp(), "users", token.getUserAgent(), service, sdfDate);

                    // if ("SUCCESS".equals(updateResult)) {
                    // String updateResult = new GenericHelper().deleteIntoCouchbase(service + "TokenId", parentTokenId, token.getBrowserId(),
//                                token.getIp(), "users", token.getUserAgent(), service, sdfDate);
                    //String updateResult = new GenericHelper().updateIntoCouchbase(service, "TimeOut", parentTokenId, token.getBrowserId(), token.getIp(), "users", token.getUserAgent(), service, sdfDate);

                    if (!"FAILURE".equals(updateResult)) {

                        new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
                        response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/PasswordPage.html?action=update&service=" + service);
                    } else {
                        //response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html");
                        response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html?service=" + service);
                    }
                    //new HTTPHelper().makeHTTPGetRequest("accounts.staging.nkn.in:8080/Accounts/NKN/PasswordPage.html");
//                    } else {
//                        new Cookies().setCookie("serviceName", service, servicesXML.getDomain(), 5 * 60 * 60, response);
//                        response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html");
//                    }
                } else {
                    response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html?service=" + service);
                    // response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html");
                }
                System.out.println("Decrypting with key " + servicesXML.getApi_key());
            } else {
                response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html?service=" + service);
                //response.sendRedirect("http://accounts.staging.nkn.in:8080/Accounts/NKN/index.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
