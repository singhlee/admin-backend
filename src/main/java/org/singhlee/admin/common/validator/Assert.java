package org.singhlee.admin.common.validator;

import org.apache.commons.lang.StringUtils;
import org.singhlee.admin.common.exception.CustomizeException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: admin-backend
 * @description: 数据校验
 * @author: singhlee
 * @create: 2020-06-16 14:53
 **/
public class Assert {
    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new CustomizeException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new CustomizeException(message);
        }
    }

    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        return check(cellphone, regex);
    }

    static boolean flag = false;

    public static boolean check(String str, String regex) {
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
