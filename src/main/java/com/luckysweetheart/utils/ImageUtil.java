package com.luckysweetheart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;

public class ImageUtil {

	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 改变图片的大小
	 * 
	 * @param source
	 *            源文件
	 * @param targetW
	 *            目标长
	 * @param targetH
	 *            目标宽
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		BufferedImage target = null;
		try {
			int type = source.getType();
			double sx = (double) targetW / source.getWidth();
			double sy = (double) targetH / source.getHeight();
			// 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
			// 则将下面的if else语句注释即可
			if (sx > sy) {
				sx = sy;
				targetW = (int) (sx * source.getWidth());
			} else {
				sy = sx;
				targetH = (int) (sy * source.getHeight());
			}
			if (type == BufferedImage.TYPE_CUSTOM) { // handmade
				ColorModel cm = source.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
						targetH);
				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				target = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else {
				target = new BufferedImage(targetW, targetH, type);
			}
			Graphics2D g = target.createGraphics();
			// smoother than exlax:
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
			return target;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		
	}

	/**
	 * byte[] 转 File
	 * @param b 原文件字节
	 * @param outputFile 新文件地址
	 * @return
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {  
	    BufferedOutputStream stream = null;  
	    File file = null;  
	    try {  
	        file = new File(outputFile);  
	        FileOutputStream fstream = new FileOutputStream(file);  
	        stream = new BufferedOutputStream(fstream);  
	        stream.write(b);  
	    } catch (Exception e) {  
	    } finally {  
	        if (stream != null) {  
	            try {  
	                stream.close();  
	            } catch (IOException e1) {  
	            }  
	        }  
	    }  
	    return file;  
	}  
	
	/**
	 * 
	 * @param file 原始图片
	 * @param filePath 新图片
	 * @param targetW 宽
	 * @param targetH 高
	 * @param imgType 新图片类型
	 * @return
	 */
	public static File updateImage(File file,String filePath,int targetW, int targetH,String imgType){
		BufferedImage bi;
		File f = null;
		try {
			bi = ImageIO.read(file);
			BufferedImage bu = ImageUtil.resize(bi,targetW,targetH);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bu, imgType, baos);
			byte[] b = baos.toByteArray();
			f = ImageUtil.getFileFromBytes(b, filePath+"/upload.jpg");
			if (f.exists()) {
				return f;
			}
			return null;
		} catch (Exception e) {
			System.out.println("改变图片大小出异常了");
			return null;
		}
	}
	
}
