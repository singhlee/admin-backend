package org.singhlee.admin.common.utils;

/**
 * 常量
 *
 * @author singhlee
 * @date 2020-06-15 15:44
 */
public class Constant {
    public static final String TOKEN_ENTRY_POINT_URL = "/admin/login";
    public static final String TOKEN_LOGOUT_URL = "/admin/logout";
    public static final String CASE_PREFIX = "case_";
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    public static final int CODE_SIZE = 4;

    /**
     * 菜单类型
     *
     * @date 20:53
     * @Author singhlee
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


}
