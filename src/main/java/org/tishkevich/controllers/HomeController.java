package org.tishkevich.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tishkevich.config.VkontakteConfiguration;
import org.tishkevich.entities.AppRole;
import org.tishkevich.entities.UserRole;
import org.tishkevich.entities.VkontakteAccount;
import org.tishkevich.repositories.RoleRepo;
import org.tishkevich.repositories.UserRepo;
import org.tishkevich.utils.EmailUtility;
import org.tishkevich.utils.EncrytedPasswordUtils;
import org.tishkevich.utils.VkontakteUtils;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private VkontakteConfiguration vkConf;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@GetMapping
	public String index(HttpSession session) {
		System.out.println("Index controller started!");
		session.setAttribute("conf", vkConf);
		session.setAttribute("vk_url", vkConf.getUrl());
		session.setAttribute("client_id", vkConf.getClientId());
		session.setAttribute("redirect_uri", vkConf.getRedirectUri());
		session.setAttribute("response_type", vkConf.getResponseType());
		return "index";
	}
	
	@GetMapping(value = "myapp")
	public String code(@RequestParam("code") String code, HttpSession session) {
		System.out.println("Myapp controller started!");
		VkontakteAccount vkAccount = VkontakteUtils.setUserInfoByCode(vkConf, code);
		VkontakteAccount vkAccount2 = userRepo.findByUsername(vkAccount.getUsername());
		String pass = vkAccount.getPassword();
		if (vkAccount2 == null) {
			userRepo.save(vkAccount);
			UserRole ur = new UserRole();
			ur.setAppRole(new AppRole(1L, "User"));
			ur.setUser(vkAccount);
			roleRepo.save(ur);

		} else {
			Optional<VkontakteAccount> vkAcc = userRepo.findById(vkAccount2.getId());
			vkAccount2 = vkAcc.get();
			vkAccount2.setPassword(EncrytedPasswordUtils.encrytePassword(pass));
			userRepo.save(vkAccount2);
		}
		session.setAttribute("account", vkAccount);
		session.setAttribute("code", pass);
		return "redirect:/confirm";
	}

	@GetMapping(value = "confirm")
	public String confirm() {
		System.out.println("Confirm controller started!");
		return "confirm";
	}

	@PostMapping(value = "confirm")
	public String postConfirm(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session) {
		System.out.println("Confirm controller started!");
		VkontakteAccount vkAccount = userRepo.findByUsername(username);
		if (vkAccount == null||vkAccount.isActive()==false) {
			return "redirect:/login?error=true";
		}
		if (EncrytedPasswordUtils.compare(password, vkAccount.getPassword())) {
			session.setAttribute("account", vkAccount);
			session.setAttribute("code", password);
		}
		return "confirm";
	}

	@GetMapping(value = "userinfo")
	public String user(Principal principal, ModelMap model) {
		System.out.println("User controller started!");
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		model.addAttribute("user", loginedUser);
		return "userinfo";
	}

	@GetMapping(value = "adminpage")
	public String admin(Principal principal, ModelMap model) {
		System.out.println("Admin controller started!");
		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		for (GrantedAuthority auth : loginedUser.getAuthorities()) {
			System.out.println(auth.getAuthority());
		}
		model.addAttribute("user", loginedUser);
		return "adminpage";
	}

	@PostMapping(value = "registrate")
	public String registration(@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("fname") String fname, @RequestParam("lname") String lname,
			@RequestParam("email") String email, @RequestParam("city") String city, ModelMap model) {
		System.out.println("Registration controller started!");
		VkontakteAccount vkAccount = userRepo.findByUsername(username);
		if (vkAccount != null) {
			model.addAttribute("error", "Логин уже занят");
			return "registration";
		}
		vkAccount = new VkontakteAccount();
		vkAccount.setActive(false);
		vkAccount.setCity(city);
		vkAccount.setFirstName(fname);
		vkAccount.setLastName(lname);
		vkAccount.setPassword(EncrytedPasswordUtils.encrytePassword(password));
		vkAccount.setUsername(username);
		String generatedValue = UUID.randomUUID().toString().replaceAll("-", "");
		vkAccount.setKeycode(generatedValue);
		userRepo.save(vkAccount);
		UserRole ur = new UserRole();
		ur.setAppRole(new AppRole(1L, "User"));
		ur.setUser(vkAccount);
		roleRepo.save(ur);
		String recipient = email;
		String msg = "This is your confirmation code: <a href=\"http://localhost:8080/myapp/registration?login="+username+"&key="+generatedValue+"\">activate</a>";
		String subject = "Confirmation from MySite";
		String host = vkConf.getHost();
		String port = vkConf.getPort();
		String fromEmail = vkConf.getUser();
		String pass = vkConf.getPass();
	     try {
			EmailUtility.sendEmailWithAttachment(host, port, fromEmail, pass,
					 recipient, subject, msg, null);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		return "success";
	}
	
	@GetMapping(value = "myapp/registration")
	public String regCode(@RequestParam("login") String login, @RequestParam("key") String key, HttpSession session) {
		System.out.println("Myapp/reg controller started!");
		VkontakteAccount vkAccount = userRepo.findByUsername(login);
		if (vkAccount.getKeycode().equals(key)) {
			vkAccount.setActive(true);
			userRepo.save(vkAccount);
			} 
		session.setAttribute("reg", true);
		return "redirect:/login";
	}

	@GetMapping(value = "403")
	public String getError() {
		System.out.println("Error controller started!");
		return "403";
	}
}
