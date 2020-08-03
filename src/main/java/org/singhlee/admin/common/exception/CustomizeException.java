package org.singhlee.admin.common.exception;

import org.singhlee.admin.common.enums.ReturnCode;

/**
 * 自定义异常
 *
 * @Author singhlee
 * @date 2020-06-15 15:44
 */
public class CustomizeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = ReturnCode.SYSTEM_FAILURE.getCode();

    public CustomizeException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public CustomizeException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public CustomizeException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CustomizeException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
