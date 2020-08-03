package org.singhlee.admin.common.base;

import lombok.SneakyThrows;
import org.singhlee.admin.authentication.detail.CustomUserDetailsUser;
import org.singhlee.admin.modules.sys.entity.SysUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Controller公共组件
 *
 * @Author singhlee
 * @date 2020-06-15 15:44
 */

public abstract class AbstractController {

    protected SysUser getUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object != null) {
            CustomUserDetailsUser detailsUser= (CustomUserDetailsUser) object;
            SysUser sysUser=new SysUser();
            sysUser.setUserId(detailsUser.getUserId());
            sysUser.setUsername(detailsUser.getUsername());
            return sysUser;
        }
        return null;
    }

    @SneakyThrows
    protected Long getUserId() {
        return getUser() == null ? null : getUser().getUserId();
    }
}
