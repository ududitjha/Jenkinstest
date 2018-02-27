/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.core;

import dev.nknone.pojo.session.Token;
import in.nkn.exceptions.CookieNotFoundException;
import in.nkn.utils.Cookies;
import in.nkn.utils.OpenAM;
import in.nkn.utils.POJOMaker;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author schauhan
 */
public class OutputServlet extends HttpServlet {

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
    Logger log = Logger.getLogger(OutputServlet.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //BasicConfigurator.configure();
        try {

            String tokenId = "";
            String browserId = "";

            log.debug("Getting TokenID from Browser ... ");
            try {
                tokenId = new Cookies().getCookie("tokenId", request);
            } catch (CookieNotFoundException e) {
                tokenId = null;
                e.printStackTrace();
            }

            log.debug("Getting BrowserID from Browser ... ");
            try {
                browserId = new Cookies().getCookie("browserId", request);
            } catch (CookieNotFoundException e) {
                browserId = null;
                e.printStackTrace();
            }

            String ip = request.getRemoteAddr();
            String ua = request.getHeader("user-agent");
            String service = "Accounts";//request.getParameter("service");

            Token token = new POJOMaker().makeToken(tokenId, ip, ua, service, browserId);

            if (tokenId != null && browserId != null) {
                String pageName = request.getParameter("pagename");
                boolean cookieValid = new OpenAM().isTokenValid(token);

                if (cookieValid) {
//                    String directory = getServletContext().getRealPath("") + File.separator + "WebTexts";
//                    String webPage = GenericHelper.buildWebPage(directory, pageName, tokenId);
//                    out.println(webPage);
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    response.setDateHeader("Expires", 0);
                    response.sendRedirect(pageName + ".html");
                } else {
//                    String directory = getServletContext().getRealPath("") + File.separator + "WebTexts";
//                    String genericLogin = GenericHelper.buildWebPage(directory, "GenericLogin", "");
//                    out.println(genericLogin);
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    response.setDateHeader("Expires", 0);
                    response.sendRedirect("GenericLogin.html");
                }
            } else {
//                    String directory = getServletContext().getRealPath("") + File.separator + "WebTexts";
//                    String genericLogin = GenericHelper.buildWebPage(directory, "GenericLogin", "");
//                    out.println(genericLogin);
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0);
                response.sendRedirect("GenericLogin.html");
            }

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
}
