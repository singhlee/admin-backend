package org.singhlee.admin.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Author singhlee
 * @date 2020-06-15 15:44
 */
public class CustomAuthenticationException extends AuthenticationException {

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
