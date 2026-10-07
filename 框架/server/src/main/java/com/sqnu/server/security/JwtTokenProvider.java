package com.sqnu.server.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT令牌提供器
 * <p>
 * 负责JWT令牌的生成、解析和验证。密钥和过期时间从应用配置文件中读取。
 * </p>
 */
@Component
public class JwtTokenProvider {

    /** HMAC签名密钥 */
    private final SecretKey secretKey;

    /** 令牌过期时间（毫秒） */
    private final long expiration;

    /**
     * 构造JWT令牌提供器
     *
     * @param secret     JWT签名密钥（从配置中读取 {@code jwt.secret}）
     * @param expiration 令牌过期时间，单位毫秒（从配置中读取 {@code jwt.expiration}）
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    /**
     * 生成JWT令牌
     * <p>
     * 令牌包含用户名作为主题，并设置签发时间和过期时间。
     * 使用HMAC-SHA算法进行签名。
     * </p>
     *
     * @param username 用户名
     * @return JWT令牌字符串
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 从JWT令牌中解析用户名
     *
     * @param token JWT令牌字符串
     * @return 令牌中包含的用户名
     * @throws JwtException 令牌解析失败时抛出
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 验证JWT令牌是否有效
     * <p>
     * 通过尝试解析令牌来验证其签名和有效期。
     * </p>
     *
     * @param token JWT令牌字符串
     * @return {@code true} 令牌有效，{@code false} 令牌无效或已过期
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
