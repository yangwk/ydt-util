package com.github.yangwk.ydtutil.test.excel;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.github.yangwk.ydtutil.excel.write.CellStyle;
import com.github.yangwk.ydtutil.excel.write.DefaultWriterConvert;
import com.github.yangwk.ydtutil.excel.write.ExcelWriter;

public class ExcelWriterTest {
	
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
		student1.setBigMoney(BigDecimal.ONE);
		
		
		
		
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
	
	static class MyExcelWriterConvert extends DefaultWriterConvert{

		@Override
		public String convert(Object object,String fieldName,Object fieldValue) {
			if(fieldName.equals("sex")){
				if("0".equals(fieldValue)){
					return "男";
				}
				else if("1".equals(fieldValue)){
					return "女";
				}
			}
			
			return super.convert(object, fieldName, fieldValue);
		}
		
	}

	public static void main(String[] args) throws Exception {
		
		ExcelWriter excelWriter = new ExcelWriter(0,0);
		
		FileOutputStream out = new FileOutputStream("F:/temp/write-11.xlsx");
		String[] columnFields = {"money","id","name","number","sex","birthDate","idCardNo","rank","isChinese","readHour","weight","bigMoney"};
		String[] columnNames = {"金钱","主键","姓名","学号","性别","出生日期","身份证","等级","是否中国人","读书计时","体重","大钱财"};
		String sheetName = "poi创建excel";
		String data = "设计的佛爱是费尽垃圾佛iauajdiofuafj时间大佛寺ufo世界的覅";
		
		CellStyle cellStyle = new CellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setColumnWidth(30);
		cellStyle.setBorder(1);
		cellStyle.setFontName("宋体");
		cellStyle.setFontColor(CellStyle.COLOR_RED);
		cellStyle.setFontHeight(20);
		cellStyle.setForegroundColor(CellStyle.COLOR_BLUE);
		/* */
		excelWriter.setWorkbook(ExcelWriter.WORKBOOK_TYPE_XLSX);
		excelWriter.setSheet(sheetName);
		excelWriter.start(data, 3, cellStyle);
		
		excelWriter.start(columnNames, cellStyle);
		
		excelWriter.start(Student.class, students, columnFields, new MyExcelWriterConvert(),null);
		
		excelWriter.write(out);
		
		/*
		FileInputStream in = new FileInputStream("F:/test/write-1.xlsx");
		excelWriter.setWorkbook(in);
		excelWriter.setSheet(0);
		excelWriter.mergeSameCell(1, 3);
		
		excelWriter.write(out);
		*/
	}

}
