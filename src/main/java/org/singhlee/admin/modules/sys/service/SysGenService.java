package org.singhlee.admin.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.singhlee.admin.modules.sys.entity.GenConfig;
import org.singhlee.admin.modules.sys.entity.InfoRmationSchema;

public interface SysGenService extends IService<InfoRmationSchema> {

    IPage<InfoRmationSchema> queryTableList(IPage page, QueryWrapper<InfoRmationSchema> entityWrapper);

    byte[] generatorCode(GenConfig config);
}
