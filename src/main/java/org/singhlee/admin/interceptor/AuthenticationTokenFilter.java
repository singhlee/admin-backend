package org.singhlee.admin.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.singhlee.admin.authentication.detail.CustomUserDetailsService;
import org.singhlee.admin.common.enums.ReturnCode;
import org.singhlee.admin.common.utils.*;
import org.singhlee.admin.common.utils.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: admin-backend
 * @description: token 校验
 * @author: singhlee
 * @create: 2020-06-16 13:45
 **/
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private CustomUserDetailsService userDetailsService;

    private String tokenHeaderName;

    private String tokenHead = "Bearer ";

    private JwtTokenUtil jwtTokenUtil;

    private RedisUtil redisUtil;


    public AuthenticationTokenFilter(JwtTokenUtil jwtTokenUtil, String tokenHeader, CustomUserDetailsService userDetailsService, RedisUtil redisUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeaderName = tokenHeader;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeaderName);
        //获取请求的ip地址
        String currentIp = AddressUtil.getIpAddress(request);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.parseToken(authToken);
            String ip = CollectionUtil.getMapValue(jwtTokenUtil.getClaims(authToken), "ip");
            //进入黑名单验证
            if (jwtTokenUtil.isBlackList(authToken)) {
                log.info("用户：{}的token：{}在黑名单之中，拒绝访问", username, authToken);
                response.getWriter().write(JSON.toJSONString(Result.failed(null, ReturnCode.TOKEN_IS_BLACKLIST.getCode(), ReturnCode.TOKEN_IS_BLACKLIST.getValue())));
                return;
            }

            //判断token是否过期
            /*
             * 过期的话，从redis中读取有效时间（比如七天登录有效），再refreshToken（根据以后业务加入，现在直接refresh）
             * 同时，已过期的token加入黑名单
             * 判断redis是否有保存
             */
            if (redisUtil.hasKey(authToken)) {
                String expirationTime = redisUtil.hget(authToken, "expirationTime").toString();
                if (jwtTokenUtil.isExpiration(expirationTime)) {
                    //获得redis中用户的token刷新时效
                    String tokenValidTime = (String) jwtTokenUtil.getTokenValidTimeByToken(authToken);
                    String currentTime = DateUtil.getTime();
                    //这个token已作废，加入黑名单
                    log.info("{}已作废，加入黑名单", authToken);
                    redisUtil.hset("blacklist", authToken, DateUtil.getTime());

                    if (DateUtil.compareDate(currentTime, tokenValidTime)) {
                        //超过有效期，不予刷新
                        log.info("{}已超过有效期，不予刷新", authToken);
                        response.getWriter().write(JSON.toJSONString(Result.failed(null, ReturnCode.LOGIN_IS_OVERDUE.getCode(), ReturnCode.LOGIN_IS_OVERDUE.getValue())));
                        return;
                    } else {//仍在刷新时间内，则刷新token，放入请求头中
                        String usernameByToken = (String) jwtTokenUtil.getUsernameByToken(authToken);
                        //更新username
                        username = usernameByToken;
                        //更新ip
                        ip = (String) jwtTokenUtil.getIpByToken(authToken);
                        //获取请求的ip地址
                        Map<String, Object> map = new HashMap<>();
                        map.put("ip", ip);
                        String jwtToken = jwtTokenUtil.generateToken(usernameByToken, map);
                        //更新redis
                        jwtTokenUtil.setTokenRefresh(jwtToken, usernameByToken, ip);
                        //删除旧的token保存的redis
                        redisUtil.deleteKey(authToken);
                        //新的token保存到redis中
                        jwtTokenUtil.setTokenRefresh(jwtToken, username, ip);
                        log.info("redis已删除旧token：{},新token：{}已更新redis", authToken, jwtToken);
                        //更新token，为了后面
                        authToken = jwtToken;
                        response.setHeader(tokenHeaderName, "Bearer " + jwtToken);
                    }
                }

            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                /*
                 * 加入对ip的验证
                 * 如果ip不正确，进入黑名单验证
                 */
                if (!StringUtils.equals(ip, currentIp)) {
                    log.info("用户：{}的ip地址变动，进入黑名单校验", username);
                    //进入黑名单验证
                    if (jwtTokenUtil.isBlackList(authToken)) {
                        log.info("用户：{}的token：{}在黑名单之中，拒绝访问", username, authToken);
                        response.getWriter().write(JSON.toJSONString(
                                Result.failed(null, ReturnCode.TOKEN_IS_BLACKLIST.getCode(),
                                        ReturnCode.TOKEN_IS_BLACKLIST.getValue())));
                        return;
                    }
                    //黑名单没有则继续，如果黑名单存在就退出后面
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
