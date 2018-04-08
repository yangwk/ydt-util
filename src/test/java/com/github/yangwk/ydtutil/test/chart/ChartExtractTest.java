package com.github.yangwk.ydtutil.test.chart;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.yangwk.ydtutil.chart.AxisNode;
import com.github.yangwk.ydtutil.chart.ChartExtract;
import com.github.yangwk.ydtutil.chart.LegendNode;
import com.github.yangwk.ydtutil.chart.SeriesNode;

public class ChartExtractTest {
	
	Logger log = LoggerFactory.getLogger(getClass());
	List<AnalysisDTO> dataList;
	
	void beforeClass(){
		dataList = new ArrayList<AnalysisDTO>();
		AnalysisDTO dto1 = new AnalysisDTO(2016, 1, "1", "地区1", "001", "GDP", "元", "45");
		AnalysisDTO dto2 = new AnalysisDTO(2016, 2, "1", "地区1", "001", "GDP", "元", "48.2");
		AnalysisDTO dto3 = new AnalysisDTO(2016, 3, "1", "地区1", "001", "GDP", "元", "156");
		
		AnalysisDTO dto4 = new AnalysisDTO(2016, 1, "2", "地区2", "001", "GDP", "元", "12");
		AnalysisDTO dto5 = new AnalysisDTO(2016, 2, "2", "地区2", "001", "GDP", "元", "1654");
		AnalysisDTO dto6 = new AnalysisDTO(2016, 3, "2", "地区2", "001", "GDP", "元", "146");
		
		AnalysisDTO dto7 = new AnalysisDTO(2016, 1, "3", "地区3", "001", "GDP", "元", "123.54");
		AnalysisDTO dto8 = new AnalysisDTO(2016, 2, "3", "地区3", "001", "GDP", "元", "689");
		AnalysisDTO dto9 = new AnalysisDTO(2016, 3, "3", "地区3", "001", "GDP", "元", "1548.26");
		
		dataList.add(dto1);
		dataList.add(dto2);
		dataList.add(dto3);
		dataList.add(dto4);
		dataList.add(dto5);
		dataList.add(dto6);
		dataList.add(dto7);
		dataList.add(dto8);
		dataList.add(dto9);
	}
	
	@Test
	public void test(){
		beforeClass();
		
		List<LegendNode> legendNodes = new ArrayList<LegendNode>();
		legendNodes.add(new LegendNode("1","divCode", null, "divName",null,"year", "indexValue", 0));
		legendNodes.add(new LegendNode("2","divCode", null, "divName",null,"year", "indexValue", 0));
		legendNodes.add(new LegendNode("3","divCode", "未知地区", null,null,"year", "indexValue", 0));
		
		List<AxisNode> axisNodes = new ArrayList<AxisNode>();
		axisNodes.add( new AxisNode(1, "month","1月",null,null,null ) );
		axisNodes.add( new AxisNode(2, "month","2月",null,null,null ) );
		axisNodes.add( new AxisNode(3, "month","3月", null,null,null) );
		axisNodes.add( new AxisNode(4, "month","4月", null,null,null) );
		axisNodes.add( new AxisNode(5, "month","5月", null,null,null) );
		axisNodes.add( new AxisNode(6, "month","6月", null,null,null) );
		axisNodes.add( new AxisNode(7, "month","7月", null,null,null) );
		axisNodes.add( new AxisNode(8, "month","8月", null,null,null) );
		axisNodes.add( new AxisNode(9, "month","9月", null,null,null) );
		axisNodes.add( new AxisNode(10, "month","10月", null,null,null) );
		axisNodes.add( new AxisNode(11, "month","11月", null,null,null) );
		axisNodes.add( new AxisNode(12, "month","12月", null,null,null) );
		
		ChartExtract<AnalysisDTO> chartExtract = new ChartExtract<AnalysisDTO>(dataList, AnalysisDTO.class, legendNodes, axisNodes);
		List<Object> legends = chartExtract.extractLegend();
		List<Object> axis = chartExtract.extractAxis();
		List<SeriesNode> series = chartExtract.extractSeries();
		
		log.debug( JSONObject.toJSONString(legends) );
		log.debug( JSONObject.toJSONString(axis) );
		log.debug( JSONObject.toJSONString(series) );
	}

}
