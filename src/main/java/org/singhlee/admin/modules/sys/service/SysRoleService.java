package org.singhlee.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.singhlee.admin.modules.sys.entity.SysRole;

import java.util.List;


/**
 * 角色
 * 
 * @author singhlee
 * @email singhlee@qq.com
 * @date 2016年9月18日 上午9:42:52
 */
public interface SysRoleService extends IService<SysRole> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);

	void deleteBath(Long[] ids);

	void saveRoleMenu(SysRole role);
	void updateRoleMenu(SysRole role);

}
