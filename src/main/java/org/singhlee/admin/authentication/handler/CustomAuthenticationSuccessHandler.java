package org.singhlee.admin.authentication.handler;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.singhlee.admin.authentication.detail.CustomUserDetailsUser;
import org.singhlee.admin.common.utils.AddressUtil;
import org.singhlee.admin.common.utils.JwtTokenUtil;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.modules.sys.entity.SysUser;
import org.singhlee.admin.modules.sys.service.SysUserService;
import org.singhlee.admin.modules.sys.vo.SysUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 用户登录成功时返回给前端的数据
 * @date 21:06
 * @Author singhlee
 **/
@Slf4j
@Component

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
     SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //获取请求的ip地址
        String ip = AddressUtil.getIpAddress(httpServletRequest);
        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        CustomUserDetailsUser userDetails = (CustomUserDetailsUser) authentication.getPrincipal();
        String jwtToken = "Bearer " + jwtTokenUtil.generateToken(userDetails.getUsername(), map);
        jwtTokenUtil.setTokenRefresh(jwtToken, userDetails.getUsername(), ip);
        log.info("用户{}登录成功，信息已保存至redis", userDetails.getUsername());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        SysUser sysUser= sysUserService.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, userDetails.getUsername()));
        SysUserVo sysUserVo=new SysUserVo();
        BeanUtils.copyProperties(sysUser,sysUserVo);
        sysUserVo.setToken(jwtToken);
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.succeed(sysUserVo, "登录成功")));
    }

}
