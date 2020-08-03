package org.singhlee.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.singhlee.admin.modules.sys.entity.SysLog;
import org.singhlee.admin.modules.sys.mapper.SysLogMapper;
import org.singhlee.admin.modules.sys.service.SysLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper,SysLog> implements SysLogService {

}
