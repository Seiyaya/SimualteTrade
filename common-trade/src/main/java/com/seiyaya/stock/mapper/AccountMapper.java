package com.seiyaya.stock.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Account;
import com.seiyaya.common.bean.DBParam;

@Mapper
public interface AccountMapper {

	Account queryAccount(DBParam param);

	/**
	 * 更新可用资产
	 * @param param
	 * @return
	 */
	int updateCurrentBalance(DBParam param);
	
}
