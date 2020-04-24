package com.feedev.pontointeligente.api.v1.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {
	
	private static final String R_CODE_OK = "OK";

	private T data;
	private LocalDateTime execDate;
	private String responseCode;
	private List<String> errors;
	
	public ApiResponse() {
		this.execDate = LocalDateTime.now();
		this.responseCode = ApiResponse.R_CODE_OK;
	}
	
	public ApiResponse(T data, String responseCode, List<String> errors) {
		this.execDate = null;
		this.responseCode = "";
		this.data = data;
		this.execDate = LocalDateTime.now();
		this.responseCode = responseCode == null ? ApiResponse.R_CODE_OK : responseCode;
		this.errors = errors;
	}

	public List<String> getErrors() {
		if (this.errors == null)
			this.errors = new ArrayList<String>();
		return errors;
	}
	
	public T getData() {
		return data;
	}
	
	public LocalDateTime getExecDate() {
		return execDate;
	}
	
	public String getResponseCode() {
		return responseCode;
	}

}
