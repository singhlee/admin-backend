package org.singhlee.admin.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.singhlee.admin.common.annotation.AuthIgnore;
import org.singhlee.admin.common.base.AbstractController;
import org.singhlee.admin.common.utils.MPPageConvert;
import org.singhlee.admin.common.utils.Result;
import org.singhlee.admin.modules.sys.entity.GenConfig;
import org.singhlee.admin.modules.sys.entity.InfoRmationSchema;
import org.singhlee.admin.modules.sys.service.SysGenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/sys/gen")
@AllArgsConstructor
@Api(value = "管理后台-代码生成",tags = "管理后台-代码生成")
public class SysGenController extends AbstractController {

    private final SysGenService sysGenService;

    /**
     * 列表
     */
    @AuthIgnore
    @ResponseBody
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        //查询列表数据
        QueryWrapper<InfoRmationSchema> queryWrapper = new QueryWrapper<>();
        if(MapUtils.getString(params, "tableName") != null){
            queryWrapper
                    .like("tableName",MapUtils.getString(params,"tableName"));
        }
        IPage<InfoRmationSchema> sysConfigList = sysGenService.queryTableList(MPPageConvert.<InfoRmationSchema>pageParamConvert(params),queryWrapper);
        return Result.succeed(MPPageConvert.pageValueConvert(sysConfigList));
    }

    /**
     * 生成代码
     */
    @AuthIgnore
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String data = request.getParameter("data");
        GenConfig config = JSON.parseObject(data, GenConfig.class);
        byte[] zipData = sysGenService.generatorCode(config);
        response.reset();
        response.addHeader("Content-Disposition", "attachment; filename=\"haers.zip\"");
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        response.addHeader("Content-Length", "" + zipData.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(zipData, response.getOutputStream());
    }
}
