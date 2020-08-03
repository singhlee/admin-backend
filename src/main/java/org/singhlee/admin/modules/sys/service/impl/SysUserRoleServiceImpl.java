package org.singhlee.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.singhlee.admin.modules.sys.mapper.SysUserRoleMapper;
import org.singhlee.admin.modules.sys.entity.SysUserRole;
import org.singhlee.admin.modules.sys.service.SysUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



/**
 * 用户与角色对应关系
 * 
 * @author singhlee
 * @email singhlee@qq.com
 * @date 2019年4月17日
 */
@Service
@AllArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper,SysUserRole> implements SysUserRoleService {

}
