package org.singhlee.admin.authentication.handler;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.singhlee.admin.common.utils.JwtTokenUtil;
import org.singhlee.admin.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author singhlee
 * @description: 退出成功
 * @date 2019/12/2415:12
 */
@Slf4j

public class CustomLogoutSuccessHandler implements LogoutHandler {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @SneakyThrows
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());
            //将token放入黑名单中
            jwtTokenUtil.addBlackList(authToken);
            log.info("用户登出成功！token：{}已加入redis黑名单", authToken);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.succeed("退出成功")));
    }
}
