package com.seiyaya.common.utils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import lombok.Data;

/**
 * class相关帮助类
 * 
 * @author Seiyaya
 *
 */
@Data
public class ClassUtils {

	/**
	 * 根据父类获取所有子类
	 * 
	 * @author Seiyaya
	 * @date 2019年1月23日 上午10:58:35
	 * @param clazz
	 * @return
	 */
	public List<String> getClassNameByFather(Class<?> clazz) {
		List<String> sonClassNameList = new ArrayList<String>();
		List<Class<?>> sonClassList = getClassByFather(clazz);
		if (sonClassList == null) {
			return sonClassNameList;
		}
		sonClassList.forEach((cur) -> {
			sonClassNameList.add(cur.getSimpleName());
		});

		return sonClassNameList;
	}

	/**
	 * 获取父类所有的子类class
	 * 
	 * @author Seiyaya
	 * @date 2019年1月23日 上午11:00:01
	 * @param clazz
	 * @return
	 */
	private List<Class<?>> getClassByFather(Class<?> clazz) {
		List<Class<?>> resultList = new ArrayList<>();
		String packageName = clazz.getPackage().getName();

		// 获取当前包以及子包的所有类
		List<Class<?>> allClass = getClasses(packageName);
		if (allClass != null) {
			allClass.forEach((cur) -> {
				if(cur.isAssignableFrom(clazz)) {
					if(!clazz.equals(cur)) {
						allClass.add(cur);
					}
				}
			});
		}
		return resultList;
	}

	/**
	 * 获取当前包以及子包的所有类
	 * 
	 * @author Seiyaya
	 * @date 2019年1月23日 上午11:21:36
	 * @param packageName
	 * @return
	 */
	private List<Class<?>> getClasses(String packageName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String packageDirName = packageName.replace(".", File.separator);
		Enumeration<URL> dirs;
		try {
			dirs = ClassUtils.class.getClassLoader().getResources(packageDirName);
			while(dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if("file".equals(protocol)) {
					//classes下面的文件
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath , classes);
				}else if("jar".equals(protocol)) {
					//jar包中的文件
					JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.charAt(0) == File.separatorChar) {
							name = name.substring(0);
						}
						
						if(name.startsWith(packageDirName)) {
							
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	private void findAndAddClassesInPackageByFile(String packageName, String filePath, List<Class<?>> classes) {
		
	}
}
