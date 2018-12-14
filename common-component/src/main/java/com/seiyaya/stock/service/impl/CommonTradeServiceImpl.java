package com.seiyaya.stock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.stock.mapper.SystemConfigMapper;
import com.seiyaya.stock.service.CommonTradeService;

@Service
public class CommonTradeServiceImpl extends AbstractTradeService implements CommonTradeService{

	@Autowired
	private SystemConfigMapper configMapper;
	
	
	@Override
	public String querySysConfigValue(String key) {
		return configMapper.querySysConfigValue(key);
	}


	@Override
	public boolean isSettlementTime() {
		String configValue = querySysConfigValue(SystemConfig.SETTLE_FLAG);
		return SystemConfig.isSettlementTime(configValue);
	}


	@Override
	public void updateSysConfig(String key, String value) {
		DBParam param = new DBParam().set("key",key)
				.set("value", value);
		configMapper.updateSysConfig(param);
	}
	
}
