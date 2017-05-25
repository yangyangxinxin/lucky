package com.luckysweetheart.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ExcelUtil {
	/**
	 * xls读取
	 * @throws Exception 
	 */
	public static String[][] loadXls(File file,int startNum,int colNum) throws Exception {
		if (file == null) {
			throw new Exception("获取文件失败");
		}
		//验证文件后缀
		String fileName = file.getName();
		FileUtil.getExtension(fileName);
		if (!StringUtils.equalsIgnoreCase(".xls",FileUtil.getExtension(fileName))) {
			throw new Exception("文件格式不为xls");
		}
		
		//指定要读取的文件，本例使用上面生成的helloworld.xls
		FileInputStream fileInputStream = new FileInputStream(file);
		
		//创建一个WorkBook，从指定的文件流中创建，即上面指定了的文件流
		Workbook wb = null;
		Sheet st = null;
		Row row = null;
		Cell cell = null;
		wb = new  HSSFWorkbook(fileInputStream);
		
		//注意，如果不能确定具体的名称，可以用getSheetAt(int)方法取得Sheet
		st = wb.getSheet("Sheet1");
		if(st == null){
			st = wb.getSheetAt(0);
		}
		if(st.getPhysicalNumberOfRows() == 0){
			closeStream(fileInputStream);
			throw new Exception("所选工作表为空表");
		}
		
		//建立x列坐标记录与y行坐标记录
		int x = 0;
		int y = startNum;

		//建立集合存放数据
		//List list = new ArrayList<Ren_temp>();
		String [][] data =  new String[(st.getPhysicalNumberOfRows()) - 1][colNum];
		//初始化行与列对象
		row = null;
		cell = null;
		
		//循环获取所有行
		for (; y < st.getPhysicalNumberOfRows(); y++) {
			//获得单条行
			row = st.getRow(y);
			if(row != null){
				//循环获取单条行所有列			
				for (; x < colNum; x++) {
					cell = row.getCell((short) x);
					String cellValue = "";
					
					if(cell != null){
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cellValue = cell.getRichStringCellValue().getString();
						cellValue = cellValue.trim();
					}
					data[y - 1][x] = cellValue;
				}
				x = 0;
			}else{
				closeStream(fileInputStream);
				throw new Exception("第"+y+"列，列数少于要求");
			}
			
		}
		//把cell中的内容按字符串方式读取出来，并显示在控制台上
		//注意，getRichStringCellValue()方法是3.0.1新追加的，
		//老版本中的getStringCellValue()方法被deprecated了
		//System.out.println(cell.getRichStringCellValue());
		//关闭流
		closeStream(fileInputStream);
		return data;
	}
	
	
	public static byte[] outXls(String[] title,String[][] excelData) {  
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		byte[] dataArrayByte = null;
		try {  
			if(title != null && excelData != null){
				HSSFWorkbook wb = new HSSFWorkbook();  
				int sheetIndex = 1;
				HSSFSheet sheet = wb.createSheet("Sheet"+(sheetIndex));

				sheet.setDefaultRowHeightInPoints(25);
				//设置样式
				HSSFCellStyle style =  wb.createCellStyle();
				//自动换行
				style.setWrapText(true);
				style.setBorderBottom((short) 1);   
				style.setBorderTop((short) 1);
				style.setBorderLeft((short) 1);
				style.setBorderRight((short) 1);  

				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直   
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平  

				HSSFFont  font  =  wb.createFont(); 
				font.setFontHeightInPoints((short)10);
				font.setFontName("仿宋");
				style.setFont(font);
				
				int rowIndex = 0;
				//title标题
				HSSFRow titleRow = sheet.createRow(rowIndex);
				for (int i = 0; i < title.length; i++) {
					HSSFCell  cell = titleRow.createCell(i);
					cell.setCellStyle(style);             	
					cell.setCellValue(title[i]);
				}
				rowIndex++;
				//数据内容
				for (String[] data : excelData) { 
					if (rowIndex >= 65536) {
						sheet = wb.createSheet("Sheet"+(sheetIndex+1));
						rowIndex = 0;
						sheetIndex++;
					}

					
					HSSFRow newRow = sheet.createRow(rowIndex);
					for (int i = 0; i < data.length; i++) {
						//获取单元格内容
						HSSFCell  cell = newRow.createCell(i);
						cell.setCellStyle(style);             	
						cell.setCellValue(data[i]);
					}
					rowIndex++;
				}  
				// 输出文件
				wb.write(bStream);
				dataArrayByte = bStream.toByteArray();
			}
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {
			if (bStream != null) {
				try {
					bStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataArrayByte;  
	} 
	
	public static void main(String[] args) {
		ExcelUtil xls = new ExcelUtil();
		try {
			String[] title = new String[]{"一","二","三","四","五","六","七","八","九"};
			String[][] test = new String[][]{{"5","51","52","53","54","啊神单反","暗色调","57","58"},{"5","51","52","53","54","啊神单反","暗色调","57","58"},{"8","81","82","83","","85","86","87","88"},{"9","91","92","93",null,"95","96","97","98"}};
			//WordUtil.betyToFile("d:/ee.xls", xls.outXls(title, test));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void closeStream(FileInputStream fileInputStream) throws IOException {
		if (fileInputStream != null) {
			fileInputStream.close();
		}
	}
}
