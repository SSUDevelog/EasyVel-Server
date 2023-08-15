package com.easyvel.server.jwt;

import com.easyvel.server.config.security.SecurityConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    @Value("${security.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private Long secretExp;

    @PostConstruct
    private void init() {
        LOGGER.info("[*JwtTokenProvider] init()");
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        LOGGER.info("[JwtTokenProvider*] init()");
    }

    public String createToken(String uid, List<String> roles) {
        LOGGER.info("[*JwtTokenProvider] createToken()");
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put(SecurityConfiguration.ROLES, roles);

        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + secretExp))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        LOGGER.info("[JwtTokenProvider*] createToken()");
        return token;
    }

    public String getUsername(String token) {
        LOGGER.info("[*JwtTokenProvider] getUsername()");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        LOGGER.info("[JwtTokenProvider*] getUsername(); info : {}", info);
        return info;
    }

    public Authentication getAuthentication(String token) {
        LOGGER.info("[*JwtTokenProvider] getAuthentication()");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        LOGGER.info("[JwtTokenProvider*] getAuthentication(); UserDetails UserName : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        LOGGER.info("[*JwtTokenProvider*] resolveToken()");
        return request.getHeader(SecurityConfiguration.TOKEN_HEADER);
    }

    public boolean validateToken(String token) {
        LOGGER.info("[*JwtTokenProvider*] validateToken()");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.info("[*JwtTokenProvider*] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
