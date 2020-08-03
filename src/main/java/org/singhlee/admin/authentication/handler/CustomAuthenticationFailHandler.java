package org.singhlee.admin.authentication.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.common.enums.ReturnCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 用户登录失败时返回给前端的数据
 * @date 21:05
 * @Author singhlee
 **/
@Slf4j

public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.failed(null, ReturnCode.USER_LOGIN_FAILED.getCode(), ReturnCode.USER_LOGIN_FAILED.getValue())));
    }
}
