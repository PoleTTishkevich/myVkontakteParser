package org.tishkevich.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VkontakteConfiguration {

	@Value("${vkontakte.client.id}")
	private String clientId;

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
