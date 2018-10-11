package org.tishkevich.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VkGroup {
	private String id;
	private int followersCount;

	public String getId() {
		return id;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public VkGroup(String str) {
		Document doc,doc2;
		try {
			doc = Jsoup.connect(str).get();
			
			for (Element el : doc.getElementsByAttributeValueStarting("href", "/search?c[section]=people")) {
				String[] tmpNum = el.attr("href").split("=");
				id = "-" + tmpNum[tmpNum.length - 1];
				// find followers count
				String[] tmpArr = el.text().split(" ");
				String result = "";
				for (int i = 1; i < tmpArr.length; i++) {
					result += tmpArr[i];
				}
				followersCount = Integer.parseInt(result);
				System.out.println(followersCount);
				
				Map<String, String> arguments = new HashMap<>();
				arguments.put("act", "box");
				arguments.put("al", "1");
				arguments.put("al_ad", "0");
				arguments.put("offset", "120");
				arguments.put("oid", id);
				arguments.put("tab", "members");
				
				Map<String, String> cookMap = new HashMap<>();
				arguments.put("remixdt", "0");
				arguments.put("remixflash", "29.0.0");
				arguments.put("remixgp", "b5bce433c19aa777214fe2610287a378");
				arguments.put("remixlang", "0");
				arguments.put("remixrefkey", "5e48122333b91ca2b8");
				arguments.put("remixscreen_depth", "24");
				arguments.put("remixseenads", "1");
				arguments.put("remixsid", "7dc3737c4cc690eef9ad43d4d0c0fcc7ebff195bad09b1362025a");
				arguments.put("remixstid", "803990467_8fe62f15b61927ffd5");
				arguments.put("tmr_detect", "1|1527168682445");
				
				doc2=Jsoup.connect("https://vk.com/al_page.php").data(arguments).cookies(cookMap).post();//add real cookies
				System.out.println(doc2);
			}

			// System.out.println(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		VkGroup vk = new VkGroup("https://vk.com/itcookies");
		System.out.println(vk.getFollowersCount() + " " + vk.getId());
	}

}
