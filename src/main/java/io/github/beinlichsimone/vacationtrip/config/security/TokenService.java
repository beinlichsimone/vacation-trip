package io.github.beinlichsimone.vacationtrip.config.security;

import io.github.beinlichsimone.vacationtrip.model.User;
import io.github.beinlichsimone.vacationtrip.config.tenancy.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${vacation-trip.jwt.expiration}")
    private String expiration;

    @Value("${vacation-trip.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        User logado = (User) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration) );

        String currentTenant = TenantContext.getCurrentTenant();

        return Jwts.builder().setIssuer("API Vacation Trip")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .claim("tenant", currentTenant) // propaga o schema/tenant atual no token
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getStringClaim(String token, String claimName) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        Object value = claims.get(claimName);
        return value != null ? String.valueOf(value) : null;
    }
}
