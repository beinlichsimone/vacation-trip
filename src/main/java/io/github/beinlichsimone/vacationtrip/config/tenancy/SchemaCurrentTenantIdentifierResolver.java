package io.github.beinlichsimone.vacationtrip.config.tenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SchemaCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	private static final Logger logger = LoggerFactory.getLogger(SchemaCurrentTenantIdentifierResolver.class);

	@Value("${vacation-trip.tenant.default:public}")
	private String defaultTenant;

	@Override
	public String resolveCurrentTenantIdentifier() {
		String current = TenantContext.getCurrentTenant();
		String resolved = StringUtils.hasText(current) ? current : defaultTenant;
		if (logger.isDebugEnabled()) {
			logger.debug("Current tenant resolved to '{}'", resolved);
		}
		return resolved;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}


