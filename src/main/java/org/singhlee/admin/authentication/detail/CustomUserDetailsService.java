package org.singhlee.admin.authentication.detail;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.singhlee.admin.modules.sys.entity.SysUser;
import org.singhlee.admin.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description //UserDetailsService
 * @date 21:09
 * @Author singhlee
 **/
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(sysUser);
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getById(userId);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(sysUser);
    }

    private UserDetails getDetail(SysUser sysUser) {
//        Set<String> permissions = permissionsService.getUserPermissions(sysUser.getUserId());
        Set<String> permissions =new HashSet<String>();
        String[] roles = new String[0];
        if (CollectionUtils.isNotEmpty(permissions)) {
            roles = permissions.stream().map(role -> "ROLE_" + role).toArray(String[]::new);
        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
        CustomUserDetailsUser customUserDetailsUser = new CustomUserDetailsUser(sysUser.getUserId(), sysUser.getUsername(), sysUser.getPassword(), authorities);
        return customUserDetailsUser;
    }
}
