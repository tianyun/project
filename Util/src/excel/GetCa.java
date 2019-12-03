package excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import jm.SMA;

/**
 * 运动学片段获取特征
 * 
 * 所有时间单位都是 秒（s）
 * 
 *
 */
public class GetCa {
	static int start;
	static String flag;
	public static void main(String[] args) {
		String readPath = "/Users/ubuntu/Desktop/piece3.xls";
		String writePtah = "/Users/ubuntu/Desktop/ca.xls";
		// 读表格
		start = 0;
		int i = 0;
		while (start < 65196) {
			i++;
			List<Sport> listS = readexcel(start, readPath);
			SMA sma = new SMA();
			sma.setFlag(flag);
			
			// 返回当前片段特征
			SMA sma1 = process(listS,sma);

			// 追加到最后到表格中
			sma = exchageObj(sma);
			// System.out.println(sma.toString());
			List<String> listsT = getObjV(sma);
			
			writeExcel(listsT, writePtah);

		}
		System.out.println(i);

	}

	public static SMA process(List<Sport> listS, SMA sma) {
		Sport lastS = listS.get(0); // 上一个对象；

		long ti = 0; // 怠速时间
		long ta = 0; // 加速时间
		long td = 0; // 减速时间
		long tc = 0; // 匀速时间

		double s = lastS.speed / 3.6; // 总路程
		long t = listS.get(listS.size() - 1).time - listS.get(0).time; // 总时间
		double vmax = lastS.speed; // 最大速度
		double amin = 100; // 最大减速度
		double amax = 0; // 最大加速度
		double aaS = 0; // 加速度总和
		double adS = 0; // 减速度总和
		List<Double> lv = new ArrayList<Double>(); // 速度链表
		List<Double> la = new ArrayList<Double>(); // 加速度链表
		lv.add(listS.get(0).speed);

		int P0_10 = 0;
		int P10_20 = 0;
		int P20_30 = 0;
		int P30_40 = 0;
		int P40_50 = 0;
		int P50_60 = 0;
		int P60_70 = 0;
		int P70 = 0;

		Sport sport;
		for (int i = 1; i < listS.size(); i++) {
			sport = listS.get(i);
			if (sport.speed < 10) {
				P0_10++;
			} else if (sport.speed < 20) {
				P10_20++;
			} else if (sport.speed < 30) {
				P20_30++;
			} else if (sport.speed < 40) {
				P30_40++;
			} else if (sport.speed < 50) {
				P40_50++;
			} else if (sport.speed < 60) {
				P50_60++;
			} else if (sport.speed < 70) {
				P60_70++;
			} else {
				P70++;
			}

			double a = sport.speed / 3.6 - lastS.speed / 3.6; // 变速；

			if (a > 0.36) {
				aaS += a;
				ta++;

			} else if (a < -0.36) {
				adS += a;
				td++;

			}

			if (sport.speed == 0) {
				ti++;
			}
			la.add(a);

			lv.add(sport.speed);
			s += sport.speed / 3.6;
			amin = amin < a ? amin : a;
			amax = amax > a ? amax : a;
			vmax = vmax > sport.speed ? vmax : sport.speed;
			lastS = sport;
		}
		tc = t - ta - ti - td;
		double vsd = getStandardDeviation(lv); // 速度标准差
		double asd = getStandardDeviation(la); // 加速度标准差

		// =========================set up==========================================
		sma.setT(t); // 设置总时间
		sma.setS(s); // 设置总路程
		sma.setT_i(ti); // 设置怠速时间
		sma.setT_a(ta); //
		sma.setT_c(tc);
		sma.setT_d(td);

		sma.setV_max(vmax);
		sma.setV_m(3.6 * s / t);
		sma.setV_mr(3.6 * s / (ta + tc + td));

		sma.setA_max(amax);
		sma.setA_min(amin);
		sma.setA_a(aaS / ta);
		sma.setA_d(adS / td);

		sma.setV_sd(vsd);
		sma.setA_sd(asd);

		sma.setPc((double) tc / t);
		sma.setPi((double) ti / t);
		sma.setPd((double) td / t);
		sma.setPa((double) ta / t);

		sma.setP0_10((double) P0_10 / t);
		sma.setP10_20((double) P10_20 / t);
		sma.setP20_30((double) P20_30 / t);
		sma.setP30_40((double) P30_40 / t);
		sma.setP40_50((double) P40_50 / t);
		sma.setP50_60((double) P50_60 / t);
		sma.setP60_70((double) P60_70 / t);
		sma.setP70((double) P70 / t);

		return sma;
	}

	/**
	 * 写入表格
	 * 
	 * @param sma
	 */
	public static void writeExcel(List<String> listsT, String pathUrl) {
		

		FileOutputStream outputStreama;
		try {

			FileInputStream fileInputStream = new FileInputStream(pathUrl); // 获取d://test.xls,建立数据的输入通道
			POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream); // 使用POI提供的方法得到excel的信息
			HSSFWorkbook Workbook = new HSSFWorkbook(poifsFileSystem);// 得到文档对象
			HSSFSheet sheet = Workbook.getSheet("sheet1"); // 根据name获取sheet表
			int lastRowNum = sheet.getLastRowNum();
			HSSFRow row = sheet.createRow(lastRowNum + 1); // 获取第一行
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
			outputStreama = new FileOutputStream(pathUrl);
			Workbook.write(outputStreama);
			outputStreama.flush();
			outputStreama.close();
			System.out.println("追加成功");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 读取表格
	 * 
	 * @param url
	 * @return
	 */
	public static List<Sport> readexcel(int startRowNum, String url) {
		List<Sport> listS = new ArrayList<>();
		InputStream is = null;
		try {
			is = new FileInputStream(url);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(2);// 读取第一个 sheet
			// 第一行是列名，所以从第二行开始遍历
			int firstRowNum = startRowNum + 1;
			int lastRowNum = sheet.getLastRowNum();
			for (int rIndex = firstRowNum; rIndex <= lastRowNum; rIndex++) {
				// 获取当前行的内容
				Row row = sheet.getRow(rIndex);
				if (row != null) {

					if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						start = rIndex;
						System.out.println("----s---------" + start);
						break;
					}

					int firstCellNum = row.getFirstCellNum();
					int lastCellNum = row.getLastCellNum();
					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					Date StartDate = HSSFDateUtil.getJavaDate(Double.valueOf(row.getCell(0).getStringCellValue()));
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
					String dateString = formatter.format(StartDate);
					flag = dateString;
					String[] ts = new String[2];
					ts[0] = dateString;
					row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
					ts[1] = row.getCell(2).getStringCellValue();

					String t1 = ts[0];
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").parse(t1));
					long a1 = (calendar.getTimeInMillis()) / 1000;

					listS.add(new Sport(Double.valueOf(ts[1]), a1));
					start = rIndex;
					// System.out.println("--------------------" + start);

				} else {
					start = rIndex;
					System.out.println("--------------------" + start);
					break;
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listS;
	}

	/**
	 * 计算标准差
	 * 
	 * @param array
	 * @return
	 */
	public static double getStandardDeviation(List<Double> ld) {
		double sum = 0;
		for (Double double1 : ld) {
			sum += double1; // 求出数组的总和
		}
		double average = SMA.exchange(sum / ld.size()); // 求出数组的平均数
		double total = 0;
		for (Double double1 : ld) {
			total += (double1 - average) * (double1 - average); // 求出方差，如果要计算方差的话这一步就可以了
		}
		double standardDeviation = Math.sqrt(total / ld.size()); // 求出标准差

		return SMA.exchange(standardDeviation);
	}

	/**
	 * 数值返回
	 * 
	 * @param obj
	 * @return
	 */
	public static List<String> getObjV(Object obj) {
		List<String> list = new ArrayList<String>();

		Field[] fields = obj.getClass().getDeclaredFields();

		for (int i = 0, len = fields.length; i < len; i++) {
			// 对于每个属性，获取属性名
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o;
				try {
					o = fields[i].get(obj);
					list.add("" + o);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}

		}
		return list;
	}

	/**
	 * 修改double 为三位小数点
	 * 
	 * @param obj
	 * @return
	 */
	public static SMA exchageObj(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		System.out.println(fields.length);

		for (int i = 0, len = fields.length; i < len; i++) {
			// 对于每个属性，获取属性名
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o;
				try {
					o = fields[i].get(obj);
					if (o instanceof Double) {
						fields[i].set(obj, SMA.exchange((double) o));
					}

					// System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}

		}
		return (SMA) obj;
	}
}

/**
 * 运动片段
 * 
 * @author zty
 *
 */
class Sport {
	double speed;
	long time;

	
	public Sport() {
	}

	public Sport(double speed, long time) {
		super();
		this.speed = speed;
		this.time = time;
	}

}