package org.singhlee.admin.common.enums;

public enum ReturnCode {
    SUCCESS("成功", 20000),
    SYSTEM_FAILURE("系统错误", 40000),
    MAPPINGCODE_FAIL("映射编码不存在", 40001),
    USERCODE_FAIL("用户编码不存在", 40002),
    DESIGNCODE_FAIL("设计码不存在", 40003),
    BUSINESS_FAIL("业务错误", 50000),
    PARAM_FAIL("参数错误", 50001),
    USER_NEED_AUTHORITIES("用户未登录", 50101),
    USER_LOGIN_FAILED("用户账号或密码错误", 50102),
    USER_NO_ACCESS("用户无权访问", 50103),
    TOKEN_IS_BLACKLIST("此token为黑名单", 50104),
    LOGIN_IS_OVERDUE("登录已失效", 50105);
    private String value;
    private int code;

    private ReturnCode(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
