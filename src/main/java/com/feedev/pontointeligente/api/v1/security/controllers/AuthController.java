package com.feedev.pontointeligente.api.v1.security.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedev.pontointeligente.api.v1.response.ApiResponse;
import com.feedev.pontointeligente.api.v1.security.dto.JwtAuthenticationDto;
import com.feedev.pontointeligente.api.v1.security.dto.TokenDto;
import com.feedev.pontointeligente.api.v1.security.services.JwtUserDetailsService;
import com.feedev.pontointeligente.api.v1.security.utils.JwtTokenUtil;

@RestController
@RequestMapping(path = "auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private static final String TOKEN_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	/**
	 * Gera e retorna um novo token JWT
	 * 
	 * @param authenticationDto
	 * @param result
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<TokenDto>> gerarTokenJwt(
			@Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result) {
		ApiResponse<TokenDto> apiResponse = new ApiResponse<TokenDto>();

		log.info("Gerando token para o email: {}", authenticationDto);
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationDto.getEmail(), authenticationDto.getSenha()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			log.error("Usuário e/ou senha inválido(s) {}", e.getMessage());
			apiResponse.getErrors().add("Usuário e/ou senha inválido(s).");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
		}

		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationDto.getEmail());
		String token = jwtTokenUtil.obterToken(userDetails);
		log.info("Token gerado: {}", token);
		log.info("Data de expiração do token: {}", jwtTokenUtil.getExpirationDateFromToken(token));
		apiResponse.setData(new TokenDto(token));

		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	/**
	 * Gera um novo token com uma nova data de expiração.
	 * 
	 * @param request
	 * @return ResponseEntity<Response<TokenDto>>
	 */
	@PostMapping(value = "/refresh")
	public ResponseEntity<ApiResponse<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
		log.info("Gerando refresh token JWT.");
		ApiResponse<TokenDto> response = new ApiResponse<TokenDto>();
		Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

		if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
			token = Optional.of(token.get().substring(7));
		}

		if (!token.isPresent()) {
			response.getErrors().add("Token não informado.");
		} else if (!jwtTokenUtil.tokenValido(token.get())) {
			response.getErrors().add("Token inválido ou expirado.");
		}

		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}

		String refreshedToken = jwtTokenUtil.refreshToken(token.get());
		response.setData(new TokenDto(refreshedToken));
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
