package org.singhlee.admin.modules.sys.service.impl;

import org.singhlee.admin.common.utils.Constant;
import org.singhlee.admin.modules.sys.entity.SysMenu;
import org.singhlee.admin.modules.sys.mapper.SysUserMapper;
import org.singhlee.admin.modules.sys.service.PermissionsService;
import org.singhlee.admin.modules.sys.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 权限查询
 *
 * @Author hdxj_lx
 * @date 2020-06-15 15:44
 */
@Lazy
@Service
@AllArgsConstructor
public class PermissionsServiceImpl implements PermissionsService {

    private final SysMenuService sysMenuService;
    private final SysUserMapper sysUserMapper;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenu> menuList = sysMenuService.list();
            permsList = new ArrayList<>(menuList.size());
            for (SysMenu menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserMapper.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
}
