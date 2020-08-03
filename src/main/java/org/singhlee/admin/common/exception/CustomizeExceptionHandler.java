package org.singhlee.admin.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.singhlee.admin.common.utils.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * 异常处理器
 *
 * @Author singhlee
 * @date 2020-06-15 15:44
 */
@Slf4j
@RestControllerAdvice
public class CustomizeExceptionHandler extends Result {
    /**
     * 自定义异常
     */
    @ExceptionHandler(CustomizeException.class)
    public Result handleCustomizeException(CustomizeException e) {
        log.error("发生异常:{}", e);
        return Result.failed("系统错误");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        return Result.failed("数据库中已存在该记录");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return Result.failed(HttpStatus.FORBIDDEN.value(), "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("发生异常:{}", e);
        return Result.failed("系统错误");
    }
}
