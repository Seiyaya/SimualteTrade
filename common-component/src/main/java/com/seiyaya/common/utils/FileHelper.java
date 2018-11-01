package com.seiyaya.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件帮助类，为了与FileUtils区分
 * @author Seiyaya
 *
 */
@Slf4j
public class FileHelper {

	public static List<Map<String, Object>> readZip(String filePath) {
		ZipFile zipFile = null;
		ZipInputStream zipInputStream = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			File file = new File(filePath);
			if(!file.exists()){
				return null;
			}
			zipFile = new ZipFile(filePath);
			ZipEntry entry = null;
			zipInputStream = new ZipInputStream(new FileInputStream(filePath));
			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					if (12 == entry.getName().length()) {
						List<Map<String, Object>> childList;
						StringBuffer result = readJsonFile(zipFile.getInputStream(entry));
						zipInputStream.closeEntry();
						childList = getListFromJsonString(result.toString());
						resultList.addAll(childList);
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("建立文件连接出错：{}" ,e);
			return null;
		} catch (IOException e) {
			log.error("文件读取出错：{}",e);
			return null;
		} finally {
			if (zipInputStream != null) {
				try {
					zipInputStream.close();
				} catch (IOException e) {
					log.error("关闭输入流出错，{}",e);
				}
			}
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					log.error("关闭输入流出错，{}", e);
				}
			}
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Map<String, Object>> getListFromJsonString(String jsonString) {
		JSONArray resultJson = JSON.parseArray(jsonString);
		List<Map<String, Object>> result = new ArrayList<>();
		if (resultJson != null && resultJson.size() > 0) {
			result = JSONArray.toJavaObject(resultJson, result.getClass());
		}
		return result;
	}

	public static StringBuffer readJsonFile(InputStream input)
			throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(input));
		String temp = "";
		StringBuffer result = new StringBuffer();
		while ((temp = buff.readLine()) != null) {
			result.append(temp);
		}
		buff.close();
		return result;
	}

}
