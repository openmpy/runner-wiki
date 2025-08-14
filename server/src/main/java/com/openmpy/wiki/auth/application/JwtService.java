package com.openmpy.wiki.auth.application;

import com.openmpy.wiki.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    public static final String ACCESS_TOKEN = "access-token";

    private final JwtProperties jwtProperties;

    public String createToken(final Map<String, Object> claims) {
        final Claims jwtClaims = Jwts.claims();
        jwtClaims.putAll(claims);

        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + jwtProperties.expiration() * 1000);

        return Jwts.builder()
                .setClaims(jwtClaims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey())
                .compact();
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token);
            return true;
        } catch (final ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
        } catch (final UnsupportedJwtException e) {
            log.warn("Unsupported token: {}", e.getMessage());
        } catch (final MalformedJwtException e) {
            log.warn("Malformed token: {}", e.getMessage());
        } catch (final SecurityException e) {
            log.warn("Invalid signature: {}", e.getMessage());
        } catch (final IllegalArgumentException e) {
            log.warn("Token claims empty: {}", e.getMessage());
        }
        return false;
    }

    public String getRole(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    private String getSigningKey() {
        return jwtProperties.secretKey();
    }
}
