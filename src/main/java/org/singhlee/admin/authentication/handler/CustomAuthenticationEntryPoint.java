package org.singhlee.admin.authentication.handler;

import com.alibaba.fastjson.JSON;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.common.enums.ReturnCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: admin-backend
 * @description: 用户未登录时返回给前端的数据
 * @author: singhlee
 * @date: 2020-06-16 16:59
 **/

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failed(null, ReturnCode.USER_NEED_AUTHORITIES.getCode(), ReturnCode.USER_NEED_AUTHORITIES.getValue())));
    }
}
