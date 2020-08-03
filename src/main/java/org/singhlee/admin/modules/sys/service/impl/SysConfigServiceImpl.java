package org.singhlee.admin.modules.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.singhlee.admin.common.exception.CustomizeException;
import org.singhlee.admin.modules.sys.mapper.SysConfigMapper;
import org.singhlee.admin.modules.sys.entity.SysConfig;
import org.singhlee.admin.modules.sys.service.SysConfigService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper,SysConfig> implements SysConfigService {

	private final SysConfigMapper sysConfigMapper;
	
	@Override
	@Transactional
	public boolean save(SysConfig config) {
		sysConfigMapper.insert(config);
		return true;
	}

	@Transactional
	public void update(SysConfig config) {
		sysConfigMapper.updateById(config);
	}

	@Override
	@Transactional
	public void updateValueByKey(String key, String value) {
		UpdateWrapper<SysConfig> wrapper = new UpdateWrapper<>();
		wrapper.eq("config_key",key);
		baseMapper.update(SysConfig.builder().configKey(key).configValue(value).build(),wrapper);
	}

	@Transactional
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfig config = baseMapper.selectById(id);
		}
		sysConfigMapper.deleteById(ids);
	}

	@Override
	public String getValue(String key) {
		SysConfig config = baseMapper.selectOne(Wrappers.<SysConfig>query().lambda().eq(SysConfig::getConfigKey,key));
		return config == null ? null : config.getConfigValue();
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return JSON.parseObject(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new CustomizeException("获取参数失败");
		}
	}
}
