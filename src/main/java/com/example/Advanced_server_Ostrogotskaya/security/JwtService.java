package com.example.Advanced_server_Ostrogotskaya.security;

import com.example.Advanced_server_Ostrogotskaya.entities.UserEntity;
import com.example.Advanced_server_Ostrogotskaya.repositories.AuthRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import static com.example.Advanced_server_Ostrogotskaya.constants.Constants.BEARER;

@Service
@RequiredArgsConstructor
public class JwtService extends Throwable {

    @Value("${secret.key}")
    private String secret_key;

    private final AuthRepository authRepository;

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateToken(UUID id) {
        return BEARER + Jwts.builder().setSubject(id.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token) {
        Optional<UserEntity> user = authRepository.findById(UUID.fromString(getUsername(token)));
        return (user.isPresent()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
