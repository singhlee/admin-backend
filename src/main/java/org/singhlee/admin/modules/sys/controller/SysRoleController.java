package org.singhlee.admin.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import org.apache.commons.collections4.MapUtils;
import org.singhlee.admin.common.base.AbstractController;
import org.singhlee.admin.common.utils.Constant;
import org.singhlee.admin.common.utils.MPPageConvert;
import org.singhlee.admin.common.utils.PageResult;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.common.validator.ValidatorUtils;
import org.singhlee.admin.modules.sys.entity.SysRole;
import org.singhlee.admin.modules.sys.service.SysRoleMenuService;
import org.singhlee.admin.modules.sys.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @Author hdxj_lx
 * @date 2020-06-15 15:44
 */

@RestController
@RequestMapping("/sys/role")
@AllArgsConstructor
@Api(value = "管理后台-角色",tags = "管理后台-角色")
public class SysRoleController extends AbstractController {
    private final SysRoleService sysRoleService;
    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('sys:role:list')")
    public Result<PageResult<SysRole>> list(@RequestParam Map<String, Object> params) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            queryWrapper.eq("create_user_id", getUserId());
        }

        //查询列表数据
        if (MapUtils.getString(params, "key") != null) {
            queryWrapper
                    .like("role_name", MapUtils.getString(params, "key"));
        }
        IPage<SysRole> sysRoleIPage = sysRoleService.page(MPPageConvert.<SysRole>pageParamConvert(params), queryWrapper);
        return Result.succeed(MPPageConvert.pageValueConvert(sysRoleIPage));
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @PreAuthorize("hasRole('sys:role:select')")
    public Result<List<SysRole>> select() {
        List<SysRole> list;
        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            list = sysRoleService.list(Wrappers
                    .<SysRole>query()
                    .lambda()
                    .eq(SysRole::getCreateUserId, getUserId())
            );
        } else {
            list = sysRoleService.list();
        }
        return Result.succeed(list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info/{roleId}")
    @PreAuthorize("hasRole('sys:role:info')")
    public Result info(@PathVariable("roleId") Long roleId) {
        SysRole role = sysRoleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        return Result.succeed(role);
    }

    /**
     * 保存角色
     */
    @RequestMapping("/save")
    @PreAuthorize("hasRole('sys:role:save')")
    public Result save(@RequestBody SysRole role) {
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.saveRoleMenu(role);
        return Result.succeed("保存角色成功");
    }

    /**
     * 修改角色
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('sys:role:update')")
    public Result update(@RequestBody SysRole role) {
        ValidatorUtils.validateEntity(role);
        role.setCreateUserId(getUserId());
        sysRoleService.updateRoleMenu(role);
        return Result.succeed("修改角色成功");
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('sys:role:delete')")
    public Result delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBath(roleIds);
        return Result.succeed("删除角色成功");
    }
}
