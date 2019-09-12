//package com.feedev.pontointeligente.api.v1.controller;
//
//
//import io.restassured.RestAssured;
//import io.restassured.matcher.RestAssuredMatchers.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.CoreMatchers.notNullValue;
//
//import org.hamcrest.Matchers.*;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//public class PJControllerTest {
//
//	@Test
//	public void testSalvarUmPj() {
//		RestAssured.post("/v1/pj")
//			.then()
//			.body("id", notNullValue());
//	}
//	
//}
