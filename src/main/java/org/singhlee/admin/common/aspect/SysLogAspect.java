package org.singhlee.admin.common.aspect;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.singhlee.admin.common.utils.AddressUtil;
import org.singhlee.admin.common.utils.HttpContextUtils;
import org.singhlee.admin.modules.sys.entity.SysLog;
import org.singhlee.admin.modules.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;


/**
 * 系统日志，切面处理类
 * 
 * @author singhlee
 * @email singhlee@qq.com
 * @date 2017年3月8日 上午11:07:35
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;


	@Pointcut("@annotation(org.singhlee.admin.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	@SneakyThrows
	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		org.singhlee.admin.common.annotation.SysLog syslog = method.getAnnotation(org.singhlee.admin.common.annotation.SysLog.class);
		if(syslog != null){
			//注解上的描述
			sysLog.setOperation(syslog.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = JSON.toJSONString(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(AddressUtil.getIpAddress(request));

		//用户名
		// String username = ((CustomUserDetailsUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(); 强转有问题...原因有待研究
		String userStr = JSON.toJSONString(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		HashMap<String,Object> userDetailsUser = JSON.parseObject(userStr,HashMap.class);
		String username = MapUtil.getStr(userDetailsUser,"username");
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
}
