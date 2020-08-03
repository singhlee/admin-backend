package org.singhlee.admin.common.annotation;

import java.lang.annotation.*;

/**
 * api接口，忽略Token验证
 *
 * @Author singhlee
 * @date 2020-06-15 15:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {

}
