package com.feedev.pontointeligente.api.v1.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private Long jwtExpiration;
	
	/**
	 * Obtem o username contido no token JWT
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		String username;
		
		try {
			Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			log.error("Erro ao tentar obter o username do token: {}", e.getMessage());
			username = null;
		}
		
		return username;
	}
	
	/**
	 * Retorna a data de expiração de um token
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			log.error("Erro ao tentar obter a data de expiração do token: {}", e.getMessage());
			expiration = null;
		}
		
		return expiration;
	}

	/**
	 * Cria um novo token (refresh)
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = gerarToken(claims);
		} catch (Exception e) {
			log.error("Erro ao tentar criar um novo token: {}", e.getMessage());
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	/**
	 * Verifica se um token jwt é válido
	 * @param token
	 * @return
	 */
	public boolean tokenValido(String token) {
		return !tokenExpirado(token);
	}
	
	/**
	 * Retorna um novo tokem JWT com base nos dados do usuário
	 * @param userDetails
	 * @return
	 */
	public String obterToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());
		
		return gerarToken(claims);
	}
	
	/**
	 * Realiza o parse do token JWT para extrair as informações
	 * contidas no corpo dele
	 * @param token
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		
		try {
			claims = Jwts.parser()
					.setSigningKey(jwtSecret)
					.parseClaimsJws(token)
					.getBody();		
		} catch (ExpiredJwtException e) {
			log.error("Token expirado: {}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("Erro ao tentar extrair as informações do token: {}", e.getMessage());
			claims = null;
		}
		
		return claims;
	}
	
	/**
	 * Retorna a data de expiracao com base da na data autal
	 * @return Date
	 */
	private Date gerarDataExpiracao() {
		return new Date(System.currentTimeMillis() + jwtExpiration * 1000);
	}
	
	/**
	 * Verifica se um token jwt está expirado
	 * @param token
	 * @return
	 */
	private boolean tokenExpirado(String token) {
		Date dataExpiracao = this.getExpirationDateFromToken(token);
		if (dataExpiracao == null) {
			return false;
		}
		return dataExpiracao.before(new Date());
	}
	
	/**
	 * Gera um novo token JWT contendo os dados fornecidos
	 * @param claims
	 * @return
	 */
	private String gerarToken(Map<String, Object> claims) {
		return Jwts.builder()
					.setClaims(claims)
					.setExpiration(this.gerarDataExpiracao())
					.signWith(SignatureAlgorithm.HS512, jwtSecret)
					.compact();
	}
	
}
