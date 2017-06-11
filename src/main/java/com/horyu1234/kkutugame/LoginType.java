package com.horyu1234.kkutugame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by horyu on 2017-03-27.
 */
public class LoginType {
    private String name;
    private String oauthAuthUrl;
    private String oauthTokenUrl;
    private String clientId;
    private String redirectUrl;
    private String scope;
    private String secretKey;

    public void setOauthAuthUrl(String oauthAuthUrl) {
        this.oauthAuthUrl = oauthAuthUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOauthTokenUrl() {
        return oauthTokenUrl;
    }

    public void setOauthTokenUrl(String oauthTokenUrl) {
        this.oauthTokenUrl = oauthTokenUrl;
    }

    public String getLoginURL() {
        return oauthAuthUrl
                .replace("@client_id@", encodeURL(clientId))
                .replace("@redirect_url@", encodeURL(redirectUrl))
                .replace("@scope@", scope == null ? "" : encodeURL(scope));
    }

    private String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("URL Encode 중 오류가 발생하였습니다.", e);

            return url;
        }
    }
}
