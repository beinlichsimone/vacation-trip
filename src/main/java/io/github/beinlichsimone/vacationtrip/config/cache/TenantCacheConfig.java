package io.github.beinlichsimone.vacationtrip.config.cache;

import io.github.beinlichsimone.vacationtrip.config.tenancy.TenantContext;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Objects;

@Configuration
public class TenantCacheConfig {

    /**
     * KeyGenerator global que prefixa o tenant atual em todas as chaves de cache.
     * Aplica-se a quaisquer @Cacheable/@CachePut que não definirem 'key' explicitamente.
     */
    @Bean("keyGenerator")
    public KeyGenerator tenantAwareKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                String tenant = Objects.requireNonNullElse(TenantContext.getCurrentTenant(), "public");
                Object simple = SimpleKeyGenerator.generateKey(params);
                return tenant + ":" + simple;
            }
        };
    }
}




