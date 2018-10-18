package com.seiyaya.stock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.SystemConfig;

@Mapper
public interface SystemConfigMapper {
	
	List<SystemConfig> querySystemConfigList();

	List<String> queryHolidayList();

	String querySysConfigValue(String key);
}
