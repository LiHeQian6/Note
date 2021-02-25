package com.zhifou.note.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : li
 * @Date: 2021-02-22 10:46
 */
@Component
public class JwtUtils implements Serializable {
    private static final long serialVersionUID = -4324967L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLES = "roles";

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * @param: user
     * @param: token
     * @return org.springframework.security.core.Authentication
     * @description 根据token获取授权对象
     * @author li
     * @Date 2021/2/24 20:59
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String[] roles = getRolesFromToken(token).split(",");
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (roles.length >0) {
            List<SimpleGrantedAuthority> list = new ArrayList<>();
            for (String role : roles) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
                list.add(simpleGrantedAuthority);
            }
            authorities = list;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token, "", authorities);
        return authentication;
    }

    /**
     * Description: 解析token，从token中获取信息
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    /**
     * Description:获取用户名
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            final Claims claims = getClaimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * Description:从token中获取
     *
     * @param token
     * @author li
     * @date 2019/10/25
     */
    public String getRolesFromToken(String token) {
        String roles;
        try {
            final Claims claims = getClaimsFromToken(token);
            roles = (String) claims.get(CLAIM_KEY_ROLES);
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }

    /**
     * Description:获取token创建时间
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * Description: 获取token过期时间
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * Description:token生成过期时间
     *
     * @param
     * @author li
     * @date 2021/2/24
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Description:token是否过期
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Description:token创建时间与密码最后修改时间比较，小于返回true，大于返回false
     *
     * @param created
     * @param lastPasswordReset
     * @author li
     * @date 2019/10/24
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * Description: 创建token
     *
     * @param userDetails
     * @author li
     * @date 2021/2/24
     */
    public String generateToken(UserDetails userDetails) {
        String roles = "";
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String temp = authority.getAuthority() + ",";
            roles += temp;
        }
        roles = roles.substring(0, roles.length() - 1);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ROLES, roles);
        return generateToken(claims);
    }

    /**
     * Description:使用Rs256签名
     *
     * @param claims
     * @author li
     * @date 2021/2/24
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Description:是否刷新token,
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        long l = (System.currentTimeMillis() - created.getTime()) / (60 * 60 * 24 * 1000);
        return l >= 1;
    }

    /**
     * Description:刷新token
     *
     * @param token
     * @author li
     * @date 2021/2/24
     */
    public String refreshToken(String token) {
        String refreshToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshToken = generateToken(claims);
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }

    /**
     * Description:验证token,验证成功返回刷新后的token,验证失败返回null
     *
     * @param token
     * @author li
     * @date 2019/10/24
     */
    public String validateToken(String token) {
        if (redisTemplate.opsForValue().get(RedisKeyUtil.getTokenKey(getUserNameFromToken(token)))!=null && !isTokenExpired(token)) {
            //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
            if (canTokenBeRefreshed(token)){
                String refreshToken = refreshToken(token);
                redisTemplate.boundValueOps(RedisKeyUtil.getTokenKey(getUserNameFromToken(refreshToken))).set(refreshToken,
                        this.expiration, TimeUnit.SECONDS);
                return refreshToken;
            }
            return token;
        }
        return null;
    }
}