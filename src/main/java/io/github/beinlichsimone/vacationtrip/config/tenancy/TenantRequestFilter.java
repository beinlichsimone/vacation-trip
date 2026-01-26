package io.github.beinlichsimone.vacationtrip.config.tenancy;

import io.github.beinlichsimone.vacationtrip.config.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Lê o tenant do header e popula o TenantContext para a requisição.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(TenantRequestFilter.class);

	@Value("${vacation-trip.tenant.header:X-Tenant-ID}")
	private String tenantHeader;

	@Value("${vacation-trip.tenant.default:public}")
	private String defaultTenant;

	private final TokenService tokenService;

	public TenantRequestFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String tenantFromHeader = request.getHeader(tenantHeader);
		String tenantFromJwt = extractTenantFromJwt(request);
		String effectiveTenant = StringUtils.hasText(tenantFromHeader)
				? tenantFromHeader
				: (StringUtils.hasText(tenantFromJwt) ? tenantFromJwt : defaultTenant);

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Resolving tenant: header='{}' jwt='{}' -> effective='{}' path='{}'",
						tenantFromHeader, tenantFromJwt, effectiveTenant, request.getRequestURI());
			}
			TenantContext.setCurrentTenant(effectiveTenant);
			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear();
		}
	}

	private String extractTenantFromJwt(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
			return null;
		}
		String token = auth.substring(7);
		try {
			return tokenService.getStringClaim(token, "tenant");
		} catch (Exception ignored) {
			return null;
		}
	}
}
