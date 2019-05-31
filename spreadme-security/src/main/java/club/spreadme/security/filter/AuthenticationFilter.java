package club.spreadme.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import club.spreadme.learn.auth.CredentialsToken;

public interface AuthenticationFilter {

	default void filter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!requiresAuthentication(request)) {
			filterChain.doFilter(request, response);
		}

		if (executeAuth(request, response)) {
			filterChain.doFilter(request, response);
		}
	}

	default boolean executeAuth(HttpServletRequest request, HttpServletResponse response) {
		CredentialsToken<?> token = createToken(request, response);
		return doAuth(request, response, token);
	}

	boolean requiresAuthentication(HttpServletRequest request);

	CredentialsToken<?> createToken(HttpServletRequest request, HttpServletResponse response);

	boolean doAuth(HttpServletRequest request, HttpServletResponse response, CredentialsToken<?> token);
}
