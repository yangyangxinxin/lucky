package com.luckysweetheart.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by wlinguo on 14-5-7.
 */
public class FileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
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
	
	/*public static void main(String[] args) {
		String name = "folder/fileName.suffix";
		System.out.println(getExtensionNoDot(name));
	}*/

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

	/**
	 * 得到一个本地当前路径
	 * @return
	 */
	public static String getFilePath() {
		File file = new File(".");
		String savePath = null;
		try {
			savePath = file.getCanonicalPath().replace("bin", "temp");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (null != savePath && (!savePath.endsWith("\\") || !savePath.endsWith("/"))) {
			savePath += "/";
		}
		return savePath;
	}

	/**
	 * 获得文件的二进制数组
	 *
	 * @param filePath
	 * @return
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return buffer;
	}

	/**
	 * 根据文件二进制数组下载文件到本地。
	 *
	 * @param bfile    文件二进制数组
	 * @param filePath 文件下载保存路径
	 * @param fileName 文件名称
	 */
	public static File getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		File dir = null;
		try {
			dir = new File(filePath);
			if (!dir.exists()) {//判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			System.out.println("开始写入文件 : " + file.getAbsolutePath());
			bos.write(bfile);
			return file;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			System.out.println("出现异常，删除文件：" + file.getAbsolutePath() + "," + dir.getAbsolutePath());
			if (file != null) {
				file.delete();
			}
			if (dir != null) {
				dir.delete();
			}
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					logger.error(e1.getMessage(),e1);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					logger.error(e1.getMessage(),e1);
				}
			}
		}
		return null;
	}

}
