package org.singhlee.admin.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 * 
 * @author singhlee
 * @email singhlee@qq.com
 * @date 2016年9月18日 上午9:28:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenu extends Model<SysRoleMenu> implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 菜单ID
	 */
	private Long menuId;
	
}
