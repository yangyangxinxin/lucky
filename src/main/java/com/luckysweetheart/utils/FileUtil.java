package com.luckysweetheart.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by wlinguo on 14-5-7.
 */
public class FileUtil {
	public static final String DOT = ".";

	/**
	 * 获取没有扩展名的文件名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getWithoutExtension(String fileName) {
		String name = StringUtils.substring(fileName, 0,
				StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(name);
	}

	/**
	 * 获取带点的扩展名 
	 * FileUtil.getExtension("folder/fileName.suffix") = ".suffix"
	 *
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT))
			return StringUtils.EMPTY;
		String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT));
		return StringUtils.trimToEmpty(ext);
	}
	
	/**
	 * 获取不带点的扩展名 
	 * FileUtil.getExtensionNoDot("folder/fileName.suffix") = "suffix"
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtensionNoDot(String fileName) {
		if (StringUtils.INDEX_NOT_FOUND == StringUtils.indexOf(fileName, DOT)){
			return StringUtils.EMPTY;
		}
		String ext = StringUtils.substring(fileName, StringUtils.lastIndexOf(fileName, DOT)+1);
		return StringUtils.trimToEmpty(ext);
	}
	
	public static void main(String[] args) {
		String name = "folder/fileName.suffix";
		System.out.println(getExtensionNoDot(name));
	}

	/**
	 * 判断是否存在扩展名
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean hasExtension(String fileName) {
		return !isExtension(fileName, StringUtils.EMPTY);
	}

	/**
	 * 判断是否同为扩展名
	 *
	 * @param fileName
	 * @param ext
	 * @return
	 */
	public static boolean isExtension(String fileName, String ext) {
		return StringUtils.equalsIgnoreCase(getExtension(fileName), ext);
	}

	/**
	 * 获取文件mime类型
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getMimeType(String fileName) throws IOException {
		Path source = Paths.get(fileName);
		return Files.probeContentType(source);
	}

}
