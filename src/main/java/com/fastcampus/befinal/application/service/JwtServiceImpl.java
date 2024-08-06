package com.fastcampus.befinal.application.service;

import com.fastcampus.befinal.common.contant.JwtConstant;
import com.fastcampus.befinal.common.response.error.exception.BusinessException;
import com.fastcampus.befinal.domain.info.TokenInfo;
import com.fastcampus.befinal.domain.info.UserInfo;
import com.fastcampus.befinal.domain.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.fastcampus.befinal.common.response.error.info.JwtErrorCode.*;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserDetailsService userDetailsService;

    private Key key;

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Value("${spring.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public TokenInfo createTokenInfo(UserInfo user) {
        return TokenInfo.of(createAccessToken(user), createRefreshToken());
    }

    private String createAccessToken(UserInfo user) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenValidityInSeconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(tokenValidity.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private String createRefreshToken() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(refreshTokenValidityInSeconds);

        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(tokenValidity.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    @Override
    public String resolveAuthorizationHeader(HttpServletRequest request) {
        String jwt = getJwt(request);

        validateJwt(jwt);

        return jwt;
    }

    private String getJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConstant.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstant.JWT_PREFIX)) {
            return bearerToken.substring(JwtConstant.JWT_PREFIX.length()).trim();
        }
        return null;
    }

    private boolean validateJwt(String Jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(Jwt);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new BusinessException(NOT_VALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ILLEGAL_JWT);
        }
    }

    @Override
    public void setAuthentication(String jwt) {
        Authentication authentication = getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthentication(String jwt) {
        String userId = parseUserId(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private String parseUserId(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .get("userId", String.class);
    }
}
