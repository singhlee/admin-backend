package org.singhlee.admin.common.xss;

import org.apache.commons.lang.StringUtils;
import org.singhlee.admin.common.exception.CustomizeException;

/**
 * @program: admin-backend
 * @description: SQL过滤
 * @author: singhlee
 * @create: 2020-06-16 10:17
 **/
public class SQLFilter {
    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.indexOf(keyword) != -1) {
                throw new CustomizeException("包含非法字符");
            }
        }

        return str;
    }
}
