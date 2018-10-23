package org.tishkevich.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.tishkevich.config.VkontakteConfiguration;
import org.tishkevich.entities.VkontakteAccount;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class VkontakteUtils {

	public static VkontakteAccount setUserInfoByCode(VkontakteConfiguration vkConf, String code) {
		Map<String, String> arguments = new HashMap<>();
		arguments.put("client_id", vkConf.getClientId());
		arguments.put("client_secret", vkConf.getClientSecret());
		arguments.put("code", code);
		arguments.put("redirect_uri", vkConf.getRedirectUri());
		String urlAddress = "https://oauth.vk.com/access_token";
		String result = getResponse(urlAddress, arguments);
		String token = result.toString().split("\"")[3];
		String uids = result.toString().split("\"")[8].substring(1).split("}")[0];
		urlAddress = "https://api.vk.com/method/users.get";
		arguments = new HashMap<>();
		arguments.put("user_ids", uids);
		arguments.put("fields", "photo_50,city,verified");
		arguments.put("access_token", token);
		arguments.put("v", "5.8.5");
		result = getResponse(urlAddress, arguments);
		String[] res = result.split("[\\\\{\\}:\"\\[\\],]");
		List<String> resList = new ArrayList<>();
		for (int i = 0; i < res.length; i++) {
			if (res[i].length() > 1) {
				resList.add(res[i]);
			}
		}
		VkontakteAccount newAcc = new VkontakteAccount();
		newAcc.setUsername(resList.get(resList.indexOf("id") + 1));
		newAcc.setFirstName(resList.get(resList.indexOf("first_name") + 1));
		newAcc.setLastName(resList.get(resList.indexOf("last_name") + 1));
		newAcc.setCity(resList.get(resList.indexOf("title") + 1));
		newAcc.setActive(true);
		newAcc.setPassword(code);
		int start = resList.indexOf("https") + 1;
		int finish = resList.indexOf("verified");
		StringBuilder photo = new StringBuilder("https:/");
		for (int i = start; i < finish; i++) {
			photo.append(resList.get(i));
		}
		newAcc.setPhoto(photo.toString());
		System.out.println(newAcc.getPhoto());
		return newAcc;
	}

	private static String getResponse(String urlAddress, Map<String, String> arguments) {
		URL url = null;
		final StringBuilder sb = new StringBuilder();
		try {
			url = new URL(urlAddress);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		HttpURLConnection http = (HttpURLConnection) con;
		try {
			http.setRequestMethod("GET");
		} catch (ProtocolException e1) {
			e1.printStackTrace();
		}
		http.setDoOutput(true);

		StringJoiner sj = new StringJoiner("&");
		for (Map.Entry<String, String> entry : arguments.entrySet())
			try {
				sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
		int length = out.length;
		http.setFixedLengthStreamingMode(length);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		try {
			http.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (OutputStream os = http.getOutputStream()) {
			os.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (InputStream is = http.getInputStream()) { // проверка полученного ответа
			final int bufferSize = 1024;
			final char[] buffer = new char[bufferSize];

			Reader in = new InputStreamReader(is, "UTF-8");
			for (;;) {
				int rsz = in.read(buffer, 0, buffer.length);
				if (rsz < 0)
					break;
				sb.append(buffer, 0, rsz);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		http.disconnect();
		return sb.toString();
	}
}
