/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.core;

import dev.nknone.pojo.session.Token;
import in.nkn.exceptions.CookieNotFoundException;
import in.nkn.utils.CheckServicesHelper;
import in.nkn.utils.Cookies;
import in.nkn.utils.HTTPHelper;
import in.nkn.utils.OpenAM;
import in.nkn.utils.ServicesXML;
import in.nkn.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author psharma1
 */
public class ValidateClientToken extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String browserId = null;

            try {
                browserId = new in.nkn.utils.Cookies().getCookie("browserId", request);
            } catch (CookieNotFoundException ex) {

                java.util.logging.Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }

            String service = request.getParameter("service");

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

            String tokenid = request.getParameter("tokenId");
            String sessionId = request.getParameter("sessionId");
            System.out.println("tokenId: " + tokenid);
            Token token = new Token();
            token.setTokenId(tokenid);
            token.setIp(ip);
            token.setServiceName(service);
            token.setUserAgent(ua);
            token.setRealm("users");
            boolean isTokenValid = new OpenAM().isTokenValid(token);
            System.out.println("isTokenValid: " + isTokenValid);
            //isTokenValid = true;
            ServicesXML servicesXML = new CheckServicesHelper().checkServiceByEJB(service);

            String serviceURL = servicesXML.getUrl();

            // ServicesXML servicesXMLAcc = new CheckServicesHelper().checkServiceByXML("Accounts", request);
            //new Cookies().setCookie("tokenId", tokenid, servicesXMLAcc.getDomain(), 5 * 60 * 60, response);
            //new Cookies().setCookie("tokenId", tokenid, ".staging.nkn.in", 5 * 60 * 60, response);

            System.out.println("Redirecting " + serviceURL + "/index.jsp?status=" + isTokenValid);



            String url = serviceURL + "/index.jsp?status=" + isTokenValid + "&sessionId=" + sessionId;
            //response.sendRedirect(serviceURL + "/SetCookie?status=" + isTokenValid);

            String responseText = new HTTPHelper().makeHTTPGetRequest(url);
            out.println(responseText);
            //response.sendRedirect(url);


        } finally {
            out.close();
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

    public static void main(String[] args) {
        String url = "http://clientapp.client.com:8080/ClientApplication/SetCookie?status=true";
        new HTTPHelper().makeHTTPGetRequest(url);
    }
}
