/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import dev.nknone.pojo.session.Token;

/**
 *
 * @author mkumar1
 */
public class URLGenerator {

    public String getTokenQueryParams(Token token) {
        String tokenId = token.getTokenId();
        String browserId = token.getBrowserId();
        String userAgent = token.getUserAgent();
        String serviceName = token.getServiceName();
        String ip = token.getIp();
        tokenId = URLParamEncoder.encode(tokenId);
        browserId = URLParamEncoder.encode(browserId);
        userAgent = URLParamEncoder.encode(userAgent);
        serviceName = URLParamEncoder.encode(serviceName);
        ip = URLParamEncoder.encode(ip);
        String param = "tokenId=" + tokenId + "&browserID=" + browserId + "&ip=" + ip + "&ua=" + userAgent + "&service=" + serviceName;
        return param;
    }

    public String getQueryParams(Token token) {
        String param = "";
        try {
            String tokenId = token.getTokenId();
            String browserId = token.getBrowserId();
            String userAgent = token.getUserAgent();
            String serviceName = token.getServiceName();
            String ip = token.getIp();
            tokenId = URLParamEncoder.encode(tokenId);
            browserId = URLParamEncoder.encode(browserId);
            userAgent = URLParamEncoder.encode(userAgent);
            serviceName = URLParamEncoder.encode(serviceName);
            ip = URLParamEncoder.encode(ip);
            param = "tokenID=" + tokenId + "&browserID=" + browserId + "&ip=" + ip + "&ua=" + userAgent + "&service=" + serviceName;
        } catch (Exception e) {
            e.printStackTrace();
            param = null;
        }
        return param;
    }
}
