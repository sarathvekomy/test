package com.vekomy.vbooks.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationProcessingFilterEntryPoint;

/**
 * @author Satish
 * 
 */
@SuppressWarnings("deprecation")
public class CustomAuthenticationEntryPoint extends
		AuthenticationProcessingFilterEntryPoint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * LoginUrlAuthenticationEntryPoint
	 * #commence(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.AuthenticationException)
	 */

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exp)
			throws IOException, ServletException {
		super.commence(request, response, exp);
	}

}
