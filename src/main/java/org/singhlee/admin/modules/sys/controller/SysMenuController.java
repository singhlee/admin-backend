package org.singhlee.admin.modules.sys.controller;


import io.swagger.annotations.Api;
import org.singhlee.admin.common.exception.CustomizeException;
import org.singhlee.admin.common.utils.Constant;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.modules.sys.entity.SysMenu;
import org.singhlee.admin.modules.sys.service.PermissionsService;
import org.singhlee.admin.modules.sys.service.SysMenuService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.singhlee.admin.common.base.AbstractController;
import org.singhlee.admin.modules.sys.vo.NavVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 *
 * @Author hdxj_lx
 * @date 2020-06-15 15:44
 */

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Api(value = "管理后台-菜单",tags = "管理后台-菜单")
public class SysMenuController extends AbstractController {
    private final SysMenuService sysMenuService;
    private final PermissionsService shiroService;

    /**
     * 导航菜单
     */
    @RequestMapping("/nav")
    public Result<NavVo> nav() {
        List<SysMenu> menuList = sysMenuService.getUserMenuList(getUserId());
        Set<String> permissions = shiroService.getUserPermissions(getUserId());
        NavVo navVo = new NavVo();
        navVo.setMenuList(menuList);
        navVo.setPermissions(permissions);
        return Result.succeed(navVo);
    }

    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('sys:menu:list')")
    public Result<List<SysMenu>> list() {
        List<SysMenu> menuList = sysMenuService.list();
        return Result.succeed(menuList);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @PreAuthorize("hasRole('sys:menu:select')")
    public Result<List<SysMenu>> select() {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenu root = new SysMenu();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        menuList.add(root);
        return Result.succeed(menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @PreAuthorize("hasRole('sys:menu:info')")
    public Result info(@PathVariable("menuId") Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return Result.succeed(menu);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasRole('sys:menu:save')")
    public Result save(@RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);
        sysMenuService.save(menu);
        return Result.succeed("保存菜单成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('sys:menu:update')")
    public Result update(@RequestBody SysMenu menu) {
        //数据校验
        verifyForm(menu);

        sysMenuService.updateById(menu);

        return Result.succeed("修改菜单成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('sys:menu:delete')")
    public Result delete(long menuId) {
        //判断是否有子菜单或按钮
        List<SysMenu> menuList = sysMenuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return Result.failed("请先删除子菜单或按钮");
        }

        sysMenuService.removeById(menuId);
        return Result.succeed("删除菜单成功");
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new CustomizeException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new CustomizeException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new CustomizeException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new CustomizeException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new CustomizeException("上级菜单只能为菜单类型");
            }
            return;
        }
    }
}
