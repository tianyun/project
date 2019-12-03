package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class read_write {
	public static void main(String[] args) throws ParseException {
		InputStream is = null;
		String reUrl = "/Users/zty/Desktop/33.xls";
		String wrUrl = "/Users/zty/Desktop/process.xls";
		List<String> listFir = Arrays.asList("时间", "GPS车速", "X轴加速度", "Y轴加速度", "Z轴加速度", "经度", "纬度", "发动机转速", "扭矩百分比",
				"瞬时油耗", "油门踏板开度", "空燃比", "发动机负荷百分比", "进气流量");

		try {
			// 写表格
			writeInit(wrUrl, listFir);

			is = new FileInputStream(reUrl);
			// 读取EXCEL表格
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);// 读取第一个 sheet

			// ==================================================
			// 第一行是列名，所以从第二行开始遍历
			int firstRowNum = sheet.getFirstRowNum() + 1;
			int lastRowNum = sheet.getLastRowNum();
			List<Map<String, String>> listmap = new ArrayList<>();

			// 遍历行
			for (int rIndex = firstRowNum; rIndex <= lastRowNum; rIndex++) {
				System.out.println("读取成功" + rIndex);

				Map<String, String> map = new HashMap<>();
				// 获取当前行的内容
				Row row = sheet.getRow(rIndex);

				if (row != null) {
					int firstCellNum = row.getFirstCellNum();
					int lastCellNum = row.getLastCellNum();
					for (int cIndex = firstCellNum; cIndex < lastCellNum; cIndex++) {
						row.getCell(cIndex).setCellType(Cell.CELL_TYPE_STRING);
						// 获取单元格的值
						String value = row.getCell(cIndex).getStringCellValue();
						// System.out.println(value);
						// 获取此单元格对应第一行的值
						String key = sheet.getRow(0).getCell(cIndex).getStringCellValue();

						// 第一行中的作为键，第n行的作为值
						map.put(key, value);
					}
				}
				listmap.add(map);

				if (listmap.size() > 1) {
					Map<String, String> map1 = listmap.get(0);
					Map<String, String> map2 = listmap.get(1);
					String t1 = (String) map1.get("时间");
					String t2 = (String) map2.get("时间");
					t1 = t1.replaceAll("\\/", "-").replaceAll("\\.", " ").trim();
					t2 = t2.replaceAll("\\/", "-").replaceAll("\\.", " ").trim();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(t1));
					long a1 = calendar.getTimeInMillis();
					calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(t2));

					long a2 = calendar.getTimeInMillis();
					
					if (a2 - a1 > 1000) {
						// 写入表格
						while (a2 > a1 + 1000) {
							a1 += 1000;

							Date date = new Date(a1);
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss SSS");
							String yy = format.format(date).replace("000", "").trim();
							List<String> listsT = Arrays.asList(yy, "", "", "", "", "", "", "", "", "",
									"", "", "", "");
							write(wrUrl, listsT);

						}

					} // 正常写入表格
					List<String> listsT = new ArrayList<>();
					for (int i = 0; i < listFir.size(); i++) {
						if("时间".equals(listFir.get(i))) {
							String tt = (String) map2.get("时间");
							tt = tt.replaceAll("\\/", "-").replaceAll("\\.", " ").replace("000", "").trim();
							listsT.add(tt);
						}else {
							listsT.add(map2.get(listFir.get(i)));
						}
					}

					write(wrUrl, listsT);
					listmap.remove(0);
				} else {

				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static void writeInit(String url, List<String> listFir) throws IOException {
		// 先写一个Excel表格
		HSSFWorkbook wbwrite = new HSSFWorkbook(); // 创建工作薄
		Sheet sheetwrite = wbwrite.createSheet("sheet1"); // 创建工作表
		Row roww = sheetwrite.createRow(0); // 行
		// 添加表头数据
		for (int i = 0; i < listFir.size(); i++) {
			// 从前端接受到的参数封装成list集合，然后遍历下标从而取出对应的值
			String value = listFir.get(i);
			System.out.println(value);
			// 将取到的值依次写到Excel的第一行的cell中
			roww.createCell(i).setCellValue(value.trim());
		}

		// 输出流,下载时候的位置
		FileOutputStream outputStream = new FileOutputStream(url);
		wbwrite.write(outputStream);
		outputStream.flush();
		outputStream.close();
		System.out.println("初始化成功");
	}

	public static void write(String url, List<String> listsT) throws IOException {
		System.out.println(listsT.toString());

		FileInputStream fileInputStream = new FileInputStream(url); // 获取d://test.xls,建立数据的输入通道
		POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream); // 使用POI提供的方法得到excel的信息
		HSSFWorkbook Workbook = new HSSFWorkbook(poifsFileSystem);// 得到文档对象
		HSSFSheet sheet = Workbook.getSheet("sheet1"); // 根据name获取sheet表
		int lastRowNum = sheet.getLastRowNum();
		HSSFRow row = sheet.createRow(lastRowNum+1); // 获取第一行


		// 添加数据
		for (int i = 0; i < listsT.size(); i++) {
			// 从前端接受到的参数封装成list集合，然后遍历下标从而取出对应的值
			String value = listsT.get(i);
			if (value == null) {
				value = "";
			} else {
				value = value.trim();
			}
			// 将取到的值依次写到Excel的第一行的cell中
			row.createCell(i).setCellValue(value);
		}
		// 输出流,下载时候的位置
//          FileWriter outputStream1 = new FileWriter(path);
		FileOutputStream outputStreama = new FileOutputStream(url);
		Workbook.write(outputStreama);
		outputStreama.flush();
		outputStreama.close();
		System.out.println("追加成功");
	}
}