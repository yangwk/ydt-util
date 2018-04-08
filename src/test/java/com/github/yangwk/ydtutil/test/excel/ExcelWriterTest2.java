package com.github.yangwk.ydtutil.test.excel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.github.yangwk.ydtutil.excel.write.CellStyle;
import com.github.yangwk.ydtutil.excel.write.ExcelWriter;

public class ExcelWriterTest2 {
	
	private static List<Student> students;
	
	static{
		students = new LinkedList<Student>();
		
		Student student1 = new Student();
		student1.setBirthDate(new Date());
		student1.setId("23");
		student1.setIdCardNo("132423423324");
		student1.setIsChinese(Boolean.TRUE);
		student1.setMoney(25458676756543554354.24D);	
		student1.setName("帅哥");
		student1.setNumber("osid2342l");
		student1.setRank(235252454);
		student1.setReadHour(20395345743543342L);
		student1.setSex("1");
		student1.setWeight(25254542.23F);
		student1.setBigMoney(new BigDecimal("4845498465498465465465.235"));
		
		
		try {
			Student student2 = (Student)student1.clone();
			student1.setId("1265");
			student2.setName("美女");
			student2.setNumber("akdfapojf");
			
			
			students.add(student1);
			students.add(student2);
			students.add((Student)student1.clone());
			students.add(student2);
			students.add((Student)student1.clone());
			students.add((Student)student1.clone());
			students.add((Student)student1.clone());
			students.add(student2);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 复杂表头
	 * @author yangwk
	 */
	@Test
	public void test(){
		ExcelWriter excelWriter = new ExcelWriter(0, 0);
		excelWriter.setWorkbook(ExcelWriter.WORKBOOK_TYPE_XLSX);
		excelWriter.setSheet("测试");
		
		
		String title ="多样式合并";
		String remark = "查询条件：  时间 2017-01-01";
		String[] headTitles = {
			"金钱","列组-1","列组-1","列组-2","列组-2","列组-2","列组-2","列组-3","列组-3","列组-3","列组-4","列组-4"
		};
		String[] headNames = {"金钱","主键","姓名","学号","性别","出生日期","身份证","等级","是否中国人","读书计时","体重","大钱财"};
		String[] columnFields = {"money","id","name","number","sex","birthDate","idCardNo","rank","isChinese","readHour","weight","bigMoney"};
		
		int cells = headNames.length;
		excelWriter.start(title, cells, null );
		excelWriter.start(remark, cells, null );
		
		CellStyle cs = new CellStyle();
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setFontName("宋体");
		cs.setBorder(CellStyle.BORDER_THIN);
		cs.setFontColor(CellStyle.COLOR_BLUE);
		
		excelWriter.start(headTitles, cs );
		//合并
		excelWriter.merge(2, 2, 1, 2);
		excelWriter.merge(2, 2, 3, 6);
		excelWriter.merge(2, 2, 7, 9);
		excelWriter.merge(2, 2, 10, 11);
		
		excelWriter.start(headNames, cs );
		excelWriter.mergeSameCell(2, 0, new int[]{0});
		
		excelWriter.start(Student.class, students, columnFields, null, null);
		excelWriter.mergeSameCell(3, 6, new int[]{6});
		try {
			FileOutputStream output = new FileOutputStream("f:/temp/test/template-7.xlsx");
			excelWriter.write(output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

