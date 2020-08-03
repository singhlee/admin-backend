package org.singhlee.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.singhlee.admin.common.utils.GenUtils;
import org.singhlee.admin.modules.sys.entity.ColumnEntity;
import org.singhlee.admin.modules.sys.entity.GenConfig;
import org.singhlee.admin.modules.sys.entity.InfoRmationSchema;
import org.singhlee.admin.modules.sys.mapper.SysGenMapper;
import org.singhlee.admin.modules.sys.service.SysGenService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class SysGenServiceImpl extends ServiceImpl<SysGenMapper, InfoRmationSchema> implements SysGenService {

    private final SysGenMapper sysGenMapper;
    private final GenUtils genUtils;

    @Override
    public IPage<InfoRmationSchema> queryTableList(IPage page, QueryWrapper<InfoRmationSchema> entityWrapper) {
        return sysGenMapper.queryTableList(page,entityWrapper);
    }
    @Override
    public byte[] generatorCode(GenConfig config) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(String tableName : config.getGenTable()){
            //查询表信息
            InfoRmationSchema table = sysGenMapper.queryTable(new QueryWrapper<InfoRmationSchema>().eq("tableName",tableName));
            //查询列信息
            List<ColumnEntity> columns = sysGenMapper.queryColumns(new QueryWrapper<ColumnEntity>().eq("tableName",tableName));
            //生成代码
            genUtils.generatorCode(config,table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
