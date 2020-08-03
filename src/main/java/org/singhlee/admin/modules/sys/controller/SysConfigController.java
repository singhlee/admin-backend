package org.singhlee.admin.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.singhlee.admin.common.base.AbstractController;
import org.singhlee.admin.common.utils.MPPageConvert;
import org.singhlee.admin.common.utils.PageResult;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.common.validator.ValidatorUtils;
import org.singhlee.admin.modules.sys.entity.SysConfig;
import org.singhlee.admin.modules.sys.service.SysConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统参数信息
 *
 * @Author hdxj_lx
 * @date 2020-06-15 15:44
 */
@RestController
@RequestMapping("/sys/config")
@AllArgsConstructor
@Api(value = "管理后台-配置",tags = "管理后台-配置")
public class SysConfigController extends AbstractController {
    private final SysConfigService sysConfigService;

    /**
     * 所有配置列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('sys:config:list')")
    public Result<PageResult<SysConfig>> list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        if (MapUtils.getString(params, "key") != null) {
            queryWrapper
                    .like("remark", MapUtils.getString(params, "key"));
        }
        IPage<SysConfig> sysConfigList = sysConfigService.page(MPPageConvert.<SysConfig>pageParamConvert(params), queryWrapper);
        return Result.succeed(MPPageConvert.pageValueConvert(sysConfigList));
    }


    /**
     * 配置信息
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasRole('sys:config:info')")
    public Result info(@PathVariable("id") Long id) {
        SysConfig config = sysConfigService.getById(id);
        return Result.succeed(config);
    }

    /**
     * 保存配置
     */
    @RequestMapping("/save")
    @PreAuthorize("hasRole('sys:config:save')")
    public Result save(@RequestBody SysConfig config) {
        ValidatorUtils.validateEntity(config);
        sysConfigService.save(config);
        return Result.succeed("保存成功");
    }

    /**
     * 修改配置
     */
    @RequestMapping("/update")
    @PreAuthorize("hasRole('sys:config:update')")
    public Result update(@RequestBody SysConfig config) {
        ValidatorUtils.validateEntity(config);
        sysConfigService.updateById(config);
        return Result.succeed("修改成功");
    }

    /**
     * 删除配置
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('sys:config:delete')")
    public Result delete(@RequestBody Long[] ids) {
        sysConfigService.removeByIds(Arrays.asList(ids));
        return Result.succeed("删除成功");
    }

}
