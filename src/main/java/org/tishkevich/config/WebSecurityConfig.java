package org.tishkevich.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.tishkevich.service.UserDetailsServiceImpl;
import org.tishkevich.utils.EncrytedPasswordUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/index", "/confirm", "/myapp", "/myapp/registration", "/registrate", "/registration", "/success").permitAll();
		http.authorizeRequests().antMatchers("/userinfo","/hello").access("hasAnyRole(\"ROLE_USER\", \"ROLE_ADMIN\")");
		http.authorizeRequests().antMatchers("/adminpage").access("hasRole(\"ROLE_ADMIN\")");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
				http.authorizeRequests().and().formLogin().loginPage("/login").defaultSuccessUrl("/hello").failureUrl("/login?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password").and().logout().permitAll();
		
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(EncrytedPasswordUtils.getPasswordEncoder());

	}

}