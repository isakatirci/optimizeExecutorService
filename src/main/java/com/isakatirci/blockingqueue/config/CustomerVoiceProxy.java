package com.isakatirci.blockingqueue.config;


import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CustomerVoiceProxy implements Serializable {
    protected static final long serialVersionUID = -4408922810710099968L;

    protected String proxyHost;

    protected int proxyPort;

    protected String userName;
    protected String password;
    protected String nonProxyHost;
    protected Pattern nonProxyHostPattern;
    protected boolean tried = false;

    public CustomerVoiceProxy() {
    }

    public boolean isNonProxy(String url) {
/*        url = getBaseUrl(url);
        if (!StringUtils.isBlank(url) && this.nonProxyHostPattern != null) {
            if (ResourceLoader.getInstance().isProxy()) {
                Matcher matcher = this.nonProxyHostPattern.matcher(url);
                return matcher.find();
            } else {
                return !ResourceLoader.getInstance().isProxy();
            }
        } else {
            return false;
        }*/
        return false;
    }

    private static String getBaseUrl(String url) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isBlank(url)) {
            return builder.toString();
        } else {
            URL aURL = null;

            try {
                aURL = new URL(url);
            } catch (MalformedURLException var4) {
                return builder.toString();
            }

            builder.append(aURL.getProtocol()).append("://").append(aURL.getAuthority());
            return builder.toString();
        }
    }

    public String toString() {
        return String.format("ProxyHost:%s Port: %s%nUserName: %s Password: %s%nNon-ProxyHost: %s", this.proxyHost, this.proxyPort, this.userName, StringUtils.isBlank(this.password) ? "" : this.password.charAt(0) + StringUtils.repeat("*", this.password.length() - 1), this.nonProxyHost);
    }

    public String stringify() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(this.userName)) {
            builder.append(this.userName).append(":").append(StringUtils.isBlank(this.password) ? "" : this.password.charAt(0) + StringUtils.repeat("*", this.password.length() - 1)).append("@");
        }

        builder.append(this.proxyHost).append(":").append(this.proxyPort);
        return builder.toString();
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getNonProxyHost() {
        return this.nonProxyHost;
    }

    public void setNonProxyHost(String nonProxyHost) {
        this.nonProxyHost = nonProxyHost;
        if (StringUtils.isNotBlank(nonProxyHost)) {
            this.nonProxyHostPattern = Pattern.compile(nonProxyHost);
        }

    }

    public boolean hasTried() {
        return this.tried;
    }

    public void setTried(boolean tried) {
        this.tried = tried;
    }
}
