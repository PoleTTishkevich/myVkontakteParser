package org.tishkevich.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncrytedPasswordUtils {
	private final static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

	
	public static String encrytePassword(String password) {
	    return passwordEncoder.encode(password);
	}
	
	public static BCryptPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	
	public static boolean compare(String fpass, String lpass) {
		return passwordEncoder.matches(fpass, lpass);
	}

}
