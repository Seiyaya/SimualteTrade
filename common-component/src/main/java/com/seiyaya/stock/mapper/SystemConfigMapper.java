package com.seiyaya.stock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.DBParam;
import com.seiyaya.common.bean.Fare;
import com.seiyaya.common.bean.Industry;
import com.seiyaya.common.bean.Stock;
import com.seiyaya.common.bean.SystemConfig;

@Mapper
public interface SystemConfigMapper {
	
	List<SystemConfig> querySystemConfigList();

	List<String> queryHolidayList();

	String querySysConfigValue(String key);

	void batchAddIndustry(List<Industry> industrys);

	void deleteIndustry();

	void batchAddExponent(List<Stock> stockList);

	List<Fare> queryFareList();

	List<DBParam> queryEnumValue();
}
