package com.feedev.pontointeligente.api.v1.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.feedev.pontointeligente.api.v1.security.JwtAuthenticationEntryPoint;
import com.feedev.pontointeligente.api.v1.security.filters.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
		return new JwtAuthenticationTokenFilter();
	}
    
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        http
//        .authorizeRequests().anyRequest().authenticated()
////        .authorizeRequests().anyRequest().permitAll()
//          .and().httpBasic()
//          .and().cors()
//          .and().csrf().disable();
        
        httpSecurity
        	.csrf().disable()
        	.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        		.and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        		.and()
    		.authorizeRequests()
    		.antMatchers(
    				"/health", 
    				"/auth/**", 
    				"/v1/pf", // habilitando endpoint para cadastrar pf
    				"/v1/pj", // habilitando endpoint para cadastrar pj
    				"/v2/api-docs",
    				"/swagger-resources/**", 
    				"/configuration/security", 
    				"/swagger-ui.html", 
    				"/webjars/**")
    		.permitAll().anyRequest().authenticated();
    	httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		httpSecurity.headers().cacheControl();
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
	   return super.authenticationManager();
	}
	
}
