package org.singhlee.admin.modules.sys.service;

import java.util.Set;

/**
 * 权限查询
 *
 * @author hdxj_lx
 * @date 2020-06-15 15:44
 */
public interface PermissionsService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);
}
