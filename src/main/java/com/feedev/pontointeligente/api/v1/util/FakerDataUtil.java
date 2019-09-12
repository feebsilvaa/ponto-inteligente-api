package com.feedev.pontointeligente.api.v1.util;

import java.util.Locale;

import com.github.javafaker.Faker;

public class FakerDataUtil extends Faker {

	public FakerDataUtil() {
		super(new Locale("pt-BR"));
	}
	
}
