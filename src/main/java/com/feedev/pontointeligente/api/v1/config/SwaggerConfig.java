package com.feedev.pontointeligente.api.v1.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.feedev.pontointeligente.api.v1.security.utils.JwtTokenUtil;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("dev")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Bean
	public Docket api() {

		String swaggerToken;
		try {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername("admin@feedev.com");
			swaggerToken = this.jwtTokenUtil.obterToken(userDetails);
		} catch (Exception e) {
			swaggerToken = "";
		}
		
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.feedev.pontointeligente.api.v1.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo())
				.globalOperationParameters(Collections.singletonList(new ParameterBuilder().name("Authorization")
						.modelRef(new ModelRef("string")).parameterType("header").required(true).hidden(true)
						.defaultValue("Bearer " + swaggerToken).build()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Ponto Inteligente API", "Documentação da API de acesso aos endpoints do Ponto Inteligente.",
				"1.0.0", "", new Contact(null, null, null), "", "", Collections.emptyList());
	}

}
