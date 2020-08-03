package org.singhlee.admin.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @program: admin-backend
 * @description:
 * @author: singhlee
 * @date: 2020-06-16 15:53
 **/
@Component
@Slf4j
public class JwtTokenUtil {
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";
    private static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";
    private static final String CLAIM_KEY_ACCOUNT_NON_LOCKED = "non_locked";
    private static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "non_expired";
    private static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 过期时间（秒）
     */
    @Value("${jwt.access_token.expiration}")
    private int accessTokenExpiration;
    /**
     * 刷新时间（秒）
     */
    @Value("${jwt.refresh_token.expiration}")
    private int refreshTokenExpiration;
    /**
     * 时间（小时）
     */
    @Value("${jwt.validTime}")
    private int validTime;

    private RedisUtil redisUtil;


    /**
     * 生成token expirationSeconds 过期时间（秒）
     *
     * @param subject （主体信息）
     * @param claims  自定义身份信息
     * @return
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + (accessTokenExpiration * 1000)))
                .signWith(SIGNATURE_ALGORITHM, secret)
                .compact();
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token,获得subject中的信息
     *
     * @param token
     * @return
     */
    public String parseToken(String token) {
        String subject = null;
        try {
            /*Claims claims = Jwts.parser()
//                    .setSigningKey(secret) // 不使用公钥私钥
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token).getBody();*/
            subject = getTokenBody(token).getSubject();
        } catch (Exception e) {
        }
        return subject;
    }

    /**
     * 获取token自定义属性
     *
     * @param token
     * @return
     */
    public Map<String, Object> getClaims(String token) {
        Map<String, Object> claims = null;
        try {
            claims = getTokenBody(token);
        } catch (Exception e) {

        }
        return claims;
    }

    /**
     * 判断此token是否在黑名单中
     *
     * @param token
     * @return
     */
    public Boolean isBlackList(String token) {
        return redisUtil.hasKey("blacklist", token);
    }

    /**
     * 将token加入到redis黑名单中
     *
     * @param token
     */
    public void addBlackList(String token) {
        redisUtil.hset("blacklist", token, "true");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getTokenValidTimeByToken(String token) {
        return redisUtil.hget(token, "tokenValidTime");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getUsernameByToken(String token) {
        return redisUtil.hget(token, "username");
    }

    /**
     * 查询token下的刷新时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getIpByToken(String token) {
        return redisUtil.hget(token, "ip");
    }

    /**
     * 查询token下的过期时间
     *
     * @param token 查询的key
     * @return HV
     */
    public Object getExpirationTimeByToken(String token) {
        return redisUtil.hget(token, "expirationTime");
    }

    public void setTokenRefresh(String token, String username, String ip) {
        //刷新时间
        Integer expire = validTime * 24 * 60 * 60 * 1000;
        redisUtil.hset(token, "tokenValidTime", DateUtil.getAddDayTime(validTime), expire);
        redisUtil.hset(token, "expirationTime", DateUtil.getAddDaySecond(accessTokenExpiration), expire);
        redisUtil.hset(token, "username", username, expire);
        redisUtil.hset(token, "ip", ip, expire);
    }

    // 是否已过期
    public boolean isExpiration(String expirationTime) {
        /*return getTokenBody(token).getExpiration().before(new Date());*/

        //通过redis中的失效时间进行判断
        String currentTime = DateUtil.getTime();
        if (DateUtil.compareDate(currentTime, expirationTime)) {
            //当前时间比过期时间小，失效
            return true;
        } else {
            return false;
        }
    }


    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
