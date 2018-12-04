package com.seiyaya.stock.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.seiyaya.common.bean.Bargain;
import com.seiyaya.common.bean.DBParam;

@Mapper
public interface BargainMapper {

	List<Bargain> queryTodayBargainList(DBParam param);

	List<Bargain> queryHistBargainList(DBParam param);

}
