package org.tishkevich.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tishkevich.config.VkontakteConfiguration;
import org.tishkevich.entities.VkontakteAccount;
import org.tishkevich.utils.VkontakteUtils;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private VkontakteConfiguration vkConf;
		
	@GetMapping
	public String index(ModelMap model) {
		model.put("vk_url", vkConf.getUrl());
		model.put("client_id", vkConf.getClientId());
		model.put("redirect_uri", vkConf.getRedirectUri());
		model.put("response_type", vkConf.getResponseType());
		return "home/index";
	}

	@GetMapping(value = "myapp")
	public String code(@RequestParam("code") String code, HttpSession session) {
		VkontakteAccount vkAccount=VkontakteUtils.setUserInfoByCode(vkConf, code);
		session.setAttribute("account", vkAccount);
		return "redirect:/confirm";
	}
	
	@GetMapping(value = "confirm")
	public String confirm() {
		return "home/confirm";
	}
}
