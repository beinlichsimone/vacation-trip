package io.github.beinlichsimone.vacationtrip.config.tenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

	private final DataSource dataSource;

	@Value("${vacation-trip.tenant.default:public}")
	private String defaultTenant;

	@Value("${vacation-trip.tenant.create-schema-if-missing:false}")
	private boolean createSchemaIfMissing;

	public SchemaMultiTenantConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			connection.setSchema(tenantIdentifier);
			// garante a sequence padrão do Hibernate neste schema (necessária para GenerationType.AUTO)
			try (Statement st = connection.createStatement()) {
				st.execute("CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1");
			} catch (SQLException ignored) {
			}
		} catch (SQLException e) {
			if (createSchemaIfMissing) {
				try (Statement st = connection.createStatement()) {
					st.execute("CREATE SCHEMA IF NOT EXISTS \"" + tenantIdentifier + "\"");
					connection.setSchema(tenantIdentifier);
					// após criar o schema, garante a sequence padrão
					try (Statement st2 = connection.createStatement()) {
						st2.execute("CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1");
					} catch (SQLException ignored) {
					}
					return connection;
				} catch (SQLException ignored) {
					// cairá para o fallback do defaultTenant
				}
			}
			try {
				connection.setSchema(defaultTenant);
			} catch (SQLException ignored) {
			}
			throw e;
		}
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		try {
			connection.setSchema(defaultTenant);
		} finally {
			connection.close();
		}
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return null;
	}
}


