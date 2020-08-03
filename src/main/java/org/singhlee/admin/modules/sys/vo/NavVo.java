package org.singhlee.admin.modules.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.singhlee.admin.modules.sys.entity.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * @program: admin-backend
 * @description:
 * @author: hdxj_lx
 * @create: 2020-06-16 14:20
 **/
@Getter
@Setter
@ApiModel(value = "导航菜单", description = "导航菜单")
public class NavVo {
    @ApiModelProperty(value = "菜单集合")
    List<SysMenu> menuList;
    @ApiModelProperty(value = "权限集合")
    Set<String> permissions;

}
