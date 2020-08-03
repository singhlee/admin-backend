package org.singhlee.admin.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.singhlee.admin.common.base.AbstractController;
import org.singhlee.admin.common.utils.Constant;
import org.singhlee.admin.common.utils.MPPageConvert;
import org.singhlee.admin.common.utils.PageResult;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.common.validator.Assert;
import org.singhlee.admin.common.validator.ValidatorUtils;
import org.singhlee.admin.modules.sys.entity.SysUser;
import org.singhlee.admin.modules.sys.entity.SysUserRole;
import org.singhlee.admin.modules.sys.service.SysUserRoleService;
import org.singhlee.admin.modules.sys.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @Author hdxj_lx
 * @date 2020-06-15 15:44
 */

@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
@Api(value = "管理后台-用户",tags = "管理后台-用户")
public class SysUserController extends AbstractController {

    private final SysUserService sysUserService;
    private final SysUserRoleService sysUserRoleService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 所有用户列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @PreAuthorize("hasRole('sys:user:list')")
    @ApiOperation(value = "用户列表", httpMethod = "POST")

    public Result<PageResult<SysUser>> list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }

        //查询列表数据
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (MapUtils.getString(params, "key") != null) {
            queryWrapper.like("username", MapUtils.getString(params, "key"));
        }
        IPage<SysUser> sysUserList = sysUserService.page(MPPageConvert.<SysUser>pageParamConvert(params), queryWrapper);
        return Result.succeed(MPPageConvert.pageValueConvert(sysUserList));
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public Result info() {
        return Result.succeed(getUser());
    }

    /**
     * 修改登录用户密码
     */
    @RequestMapping("/password")
    public Result password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");
        password = passwordEncoder.encode(password);
        newPassword = passwordEncoder.encode(newPassword);

        SysUser user = sysUserService.getById(getUserId());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.failed("原密码不正确");
        }
        //更新密码
        sysUserService.updatePassword(getUserId(), password, newPassword);
        return Result.succeed("修改密码");
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @PreAuthorize("hasRole('sys:user:info')")
    public Result info(@PathVariable("userId") Long userId) {
        SysUser user = sysUserService.getById(userId);
        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.list(
                new QueryWrapper<SysUserRole>()
                        .lambda()
                        .eq(SysUserRole::getUserId, userId)
        ).stream().map(sysUserRole -> sysUserRole.getRoleId()).collect(Collectors.toList());
        user.setRoleIdList(roleIdList);
        return Result.succeed(user);
    }

    /**
     * 保存用户
     */
    @RequestMapping("/save")
    @PreAuthorize("hasRole('sys:user:save')")
    public Result save(@RequestBody SysUser user) {
        ValidatorUtils.validateEntity(user);
        user.setCreateUserId(getUserId());
        sysUserService.saveUserRole(user);
        return Result.succeed("保存用户");
    }

    /**
     * 修改用户
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('sys:user:update')")
    public Result update(@RequestBody SysUser user) {
        ValidatorUtils.validateEntity(user);
        user.setCreateUserId(getUserId());
        sysUserService.updateUserRole(user);
        return Result.succeed("修改用户");
    }

    /**
     * 删除用户
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('sys:user:delete')")
    public Result delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return Result.failed("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return Result.failed("当前用户不能删除");
        }
        sysUserService.removeByIds(Arrays.asList(userIds));
        return Result.succeed("修改用户");
    }
}
