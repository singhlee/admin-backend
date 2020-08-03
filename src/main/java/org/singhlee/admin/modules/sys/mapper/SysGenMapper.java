package org.singhlee.admin.modules.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.singhlee.admin.modules.sys.entity.ColumnEntity;
import org.singhlee.admin.modules.sys.entity.InfoRmationSchema;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysGenMapper extends BaseMapper<InfoRmationSchema> {

    IPage<InfoRmationSchema> queryTableList(IPage page, @Param("ew") QueryWrapper<InfoRmationSchema> entityWrapper);

    List<ColumnEntity> queryColumns(@Param("ew") QueryWrapper<ColumnEntity> entityWrapper);

    InfoRmationSchema queryTable(@Param("ew") QueryWrapper<InfoRmationSchema> tableName);
}
