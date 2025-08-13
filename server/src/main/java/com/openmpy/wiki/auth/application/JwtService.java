package com.openmpy.wiki.auth.application;

import com.openmpy.wiki.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {

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
}
