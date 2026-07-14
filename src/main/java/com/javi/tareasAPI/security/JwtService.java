package com.javi.tareasAPI.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
            "MiClaveSuperSecretaParaJWTDebeTenerAlMenos32Caracteres";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generarToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

    }

}