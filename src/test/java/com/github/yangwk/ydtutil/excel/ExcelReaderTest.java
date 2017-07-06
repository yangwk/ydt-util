package com.github.yangwk.ydtutil.excel;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;

import com.github.yangwk.ydtutil.excel.read.ExcelReader;

public class ExcelReaderTest {

	@Test
	public void test() throws Exception{
		String filePath = "F:/temp/read.xlsx";
		int startRowIndex = 2;
		String[] columnFields = new String[]{"id","name","number","sex","birthDate","idCardNo"};
		//idCardNo
		ExcelReader<Student> excelReader = new ExcelReader<Student>(Student.class);
		excelReader.setWorkbook(new FileInputStream(filePath));
		excelReader.setSheet(0);
		
		List<Student> students = excelReader.start(startRowIndex,columnFields);
		
		for(Student s : students){
			System.out.println(ReflectionToStringBuilder.toString(s));
		}
		
	}

}
