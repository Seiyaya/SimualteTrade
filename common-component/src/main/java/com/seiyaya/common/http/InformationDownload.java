package com.seiyaya.common.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.seiyaya.common.bean.SystemConfig;
import com.seiyaya.common.utils.FileHelper;
import com.seiyaya.common.utils.SpringUtils;
import com.seiyaya.stock.service.StockCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InformationDownload {
	
	private static HttpUtils httpUtils = HttpUtils.getHttpUtils();

	private static String downloadUrl;
	
	public static final List<Map<String, Object>> bonusDataList = new ArrayList<Map<String,Object>>();
	
	private static StockCacheService cacheService =  SpringUtils.getBean(StockCacheService.BEAN_NAME, StockCacheService.class);
	
	static {
		downloadUrl = cacheService.getSysConfig(SystemConfig.BONUS_DOWNLOAD_URL);
		if(StringUtils.isEmpty(downloadUrl)) {
			log.error("没有配置分红数据下载url");
		}
	}
	
	/**
	 * 下载分红数据
	 * @param down
	 */
	public static void checkBonusUrl(String download) {
		String year = download.substring(0,4);
		if(6 == download.length()){
			//初始化服务数据
			download(downloadUrl+File.separator+year+File.separator+download+".zip");
			getLoadZipData(download);
		}else if(8 == download.length()){
			//每天依次更新
			String nowMonth = download.substring(0, 6);
			download(downloadUrl+File.separator+year+File.separator+nowMonth+File.separator+download+".txt");
			getLoadFileData(download);
		}
	}

	private static void getLoadFileData(String download) {
		List<Map<String, Object>> bonusList = FileHelper.readText(System.getProperty("user.dir")+File.separator+"bonus"+File.separator+download+".txt");
		if(bonusList != null && bonusList.size() > 0){
			bonusDataList.addAll(bonusList);
		}
	}

	private static void getLoadZipData(String download) {
		List<Map<String, Object>> bonusList = FileHelper.readZip(System.getProperty("user.dir")+File.separator+"bonus"+File.separator+download+".zip");
		if(bonusList != null && bonusList.size() > 0){
			bonusDataList.addAll(bonusList);
		}
	}

	private static void download(String downUrl) {
		byte[] resultBytes = httpUtils.sendGetByte(downUrl);
		String saveFileName = downUrl.substring(downUrl.lastIndexOf(File.separator)+1);
		File file = new File(System.getProperty("user.dir")+File.separator+"bonus");
		if(!file.exists()) {
			file.mkdirs();
		}
		file = new File(System.getProperty("user.dir")+File.separator+"bonus"+File.separator+saveFileName);
		if(file.exists()){
			file.delete();
		}
		try {
			FileUtils.writeByteArrayToFile(file, resultBytes);
		} catch (IOException e) {
			log.info("{}写入文件失败",downUrl);
		}
	}
}
