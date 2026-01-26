package io.github.beinlichsimone.vacationtrip.config.tenancy;

/**
 * Armazena o identificador do tenant por requisição (ThreadLocal).
 */
public final class TenantContext {

	private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

	private TenantContext() {
	}

	public static void setCurrentTenant(String tenantIdentifier) {
		CURRENT_TENANT.set(tenantIdentifier);
	}

	public static String getCurrentTenant() {
		return CURRENT_TENANT.get();
	}

	public static void clear() {
		CURRENT_TENANT.remove();
	}
}


