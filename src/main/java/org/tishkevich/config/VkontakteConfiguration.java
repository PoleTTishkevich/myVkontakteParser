package org.tishkevich.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VkontakteConfiguration {

	@Value("${vkontakte.client.id}")
	private String clientId;
	
	@Value("${vkontakte.util.host}")
	private String host;
	
	@Value("${vkontakte.util.port}")
	private String port;
	
	@Value("${vkontakte.util.user}")
	private String user;
	
	@Value("${vkontakte.util.pass}")
	private String pass;
	

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Value("${vkontakte.client.secret}")
	private String clientSecret;

	@Value("${vkontakte.redirect.uri}")
	private String redirectUri;

	@Value("${vkontakte.url}")
	private String url;

	@Value("${vkontakte.response.type}")
	private String responseType;

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public String getUrl() {
		return url;
	}

	public String getResponseType() {
		return responseType;
	}

}
