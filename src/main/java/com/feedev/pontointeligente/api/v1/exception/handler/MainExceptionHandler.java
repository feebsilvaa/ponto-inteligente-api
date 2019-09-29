package com.feedev.pontointeligente.api.v1.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.feedev.pontointeligente.api.v1.response.ApiResponse;


@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		BindingResult result = ex.getBindingResult();
		List<String> errors = new ArrayList<String>();
		for (FieldError fieldError: result.getFieldErrors()) {
			errors.add(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
		}
		apiResponse.setErrors(errors);
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	private ResponseEntity<?> handleEmptyResultDataAccessException(
			EmptyResultDataAccessException ex,
			WebRequest request
			) {
		
		ApiResponse<?> apiResponse = new ApiResponse<>();
		String msg = "Recurso não encontrado.";
		if (ex.getMessage().contains("entities.Lancamento")) {
			msg = "Lançamento não encontrado.";
		}
		
		apiResponse.getErrors().add(msg);
		
		return handleExceptionInternal(ex, apiResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	private ResponseEntity<?> handleAccessDeniedException(
			AccessDeniedException ex,
			WebRequest request
			) {
		
		ApiResponse<?> apiResponse = new ApiResponse<>();
		String msg = "Acesso negado! Você não tem permissão para realizar essa operação.";
		
		apiResponse.getErrors().add(msg);
		
		return handleExceptionInternal(ex, apiResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
	
}
