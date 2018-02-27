/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

/**
 *
 * @author psharma1
 */
public class HTTPHelper {

    Logger log = Logger.getLogger(HTTPHelper.class);

    public String makeHTTPGetRequest(String url) {


        ////System.out.println(url);
        log.debug("url for for servlet callback is " + url);
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
            System.out.println("Response from servlet is "
                    + tokenId);

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
        return tokenId;

    }

    public void makeHTTPPostRequest(String url) throws IOException {


        ////System.out.println(url);
        log.debug("url for for servlet callback is " + url);
        String tokenId = null;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = httpClient.execute(httpPost);

//        try {
//            log.debug("Inside try block of getTokenId() method");
//            HttpResponse response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            BufferedReader rd = new BufferedReader(
//                    new InputStreamReader(entity.getContent()));
//
//            String line;
//            while ((line = rd.readLine()) != null) {
//                tokenId = line;
//            }
//            System.out.println("Response from servlet is "
//                    + tokenId);
//
//        } catch (ConnectTimeoutException e) {
//            log.fatal("Connection Timed Out");
//            e.printStackTrace();
//        } catch (Exception e) {
//            log.fatal("An exception occured in getTokeId() method of OpenAM class and Exception:" + e);
//            e.printStackTrace();
//        } finally {
//            httpClient.getConnectionManager().shutdown();
//            log.debug("closed the socket connection");
//        }

    }
}
