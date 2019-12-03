package excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

/**
 * 写入表格
 * @author zty
 *
 */
public class write {
	public static void main(String[] args) {
		OutputStream os = null;
		try {
			os = new FileOutputStream("d:/T19.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 要写入的EXCEL文件
		HSSFWorkbook hw = new HSSFWorkbook();
		// 写入SHEET的名字
		HSSFSheet cre = hw.createSheet("T19");
		// 把 单元格（第一行,第一列）到单元格（第一行, 第五列）进行合并。
		cre.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// 写入第一行
		HSSFRow row1 = cre.createRow(0);
		// 写入第二行
		HSSFRow row2 = cre.createRow(1);
		// 写入第三行
		HSSFRow row3 = cre.createRow(2);
		// 分别写入每行的第一列
		HSSFCell c1 = row1.createCell(0);

		HSSFCell c3 = row3.createCell(0);
		// 第一行写入
		c1.setCellValue("**消费记录**");
		// 第二行写入
		for (int i = 0; i < 6; i++) {
			HSSFCell c2 = row2.createCell(i);
			for (int j = 0; j < 1; j++) {
				switch (i) {
				case 1:
					c2.setCellValue("1");
					break;
				case 2:
					c2.setCellValue("2");
					break;
				case 3:
					c2.setCellValue("3");
					break;
				case 4:
					c2.setCellValue("4");
					break;
				case 5:
					c2.setCellValue("5");
					break;
				case 6:
					c2.setCellValue("6");
					break;

				default:
					break;
				}
			}
		}
	

		c3.setCellValue("hi俄黑");
		try {
			hw.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
