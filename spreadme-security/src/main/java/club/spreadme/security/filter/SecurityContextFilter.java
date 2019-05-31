package club.spreadme.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import club.spreadme.learn.auth.AuthenticatedToken;

public interface SecurityContextFilter {

	SessionManager getSessionManager();

	default void filter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		AuthenticatedToken<?> token = getSessionManager().load(request);

		if (token != null) {
			SecurityContextHolder.getContext().setAuthenticationToken(token);
		}

		try {
			filterChain.doFilter(request, response);
		}
		finally {
			SecurityContextHolder.clearContext();
		}
	}
}
