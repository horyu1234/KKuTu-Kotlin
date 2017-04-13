package com.horyu1234.kkutugame;

/**
 * Created by horyu on 2017-03-27.
 */
public class LoginType {
    private String name;
    private String oauthUrl;
    private String clientId;
    private String redirectUrl;
    private String scope;
    private String secretKey;

    public void setOauthUrl(String oauthUrl) {
        this.oauthUrl = oauthUrl;
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

    public String getLoginURL() {
        return oauthUrl
                .replace("@client_id@", clientId)
                .replace("@redirect_url@", redirectUrl)
                .replace("@scope@", scope == null ? "" : scope);
    }
}
