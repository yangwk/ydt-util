package com.github.yangwk.ydtutil.chart;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.yangwk.ydtutil.reflect.Reflector;

/**
 * @author yangwk
 * @see #ChartExtract(List, Class, List, List)
 */
public class ChartExtract<T> {

	private List<T> dataList;
	private List<LegendNode> legendNodes;
	private List<AxisNode> axisNodes;
	
	private List<Boolean> legendNodesFound;
	private List<Boolean> axisNodesFound;
	private List<LegendLink> legendLinkList;
	
	private boolean filterNotFound = true;	//过滤查找不到的结果
	private Reflector<T> reflector; 
	/**
	 * 图表数据提取
	 * @param dataList	操作数据
	 * @param clazz	数据的class
	 * @param legendNodes	视图
	 * @param axisNodes	轴
	 */
	public ChartExtract(List<T> dataList, Class<T> clazz, List<LegendNode> legendNodes, List<AxisNode> axisNodes) {
		if(dataList == null || clazz == null || legendNodes == null || axisNodes == null ){
			throw new IllegalArgumentException();
		}
		this.dataList = dataList;
		this.legendNodes = legendNodes;
		this.axisNodes = axisNodes;
		
		this.legendNodesFound = new ArrayList<Boolean>();
		this.axisNodesFound = new ArrayList<Boolean>();
		this.legendLinkList = new ArrayList<LegendLink>();
		this.reflector = new Reflector<T>(clazz);
		this.extract();
	}
	
	/**
	 * 开始提取
	 */
	private void extract() {
		for (int k = 0; k < axisNodes.size(); k++) {
			axisNodesFound.add(false);	//初始化，没找到
		}
		
		Map<Object, List<T>> legendDataMap = new HashMap<Object, List<T>>();
		
		//按视图进行提取
		for (int k = 0; k < legendNodes.size(); k++) {
			legendNodesFound.add(false);	//没找到
			LegendNode legendNode = legendNodes.get(k);
			Object legendNodeId = legendNode.getId();	//视图id
			if (legendDataMap.get( legendNodeId ) == null) { // 没有检索过
				List<T> legendData = new ArrayList<T>();	//视图对应的数据
				for (T t : dataList) {	//遍历数据
					if ( legendNodeId.equals( value(t, legendNode.getIdField()) )) { // 视图id字段对应的数据
						legendNodesFound.set(k, true);	//找到
						if (legendNode.getNameField() != null) {
							legendNode.setName( value(t,legendNode.getNameField()) );
						}
						if (legendNode.getSuffixNameField() != null) {
							legendNode.setSuffixName(value(t,legendNode.getSuffixNameField()) );
						}
						legendData.add(t); //保存数据
					}
				}
				legendDataMap.put( legendNodeId, legendData); // 保存对应数据
			}
			else{	//检索了
				if(legendDataMap.get( legendNodeId ).size() > 0){	//而且有数据
					legendNodesFound.set(k, true);	//找到
				}
			}
			
			List<Object> value = new ArrayList<Object>();	//每个legendnode对应的轴 的值
			List<Boolean> axisFound = new ArrayList<Boolean>();	//每个legendnode对应的轴 的查找结果
			//对视图按轴进行提取
			for (int m = 0; m < axisNodes.size(); m++) {
				axisFound.add(false);	//没找到
				AxisNode axisNode = axisNodes.get(m);
				List<T> legendData = legendDataMap.get( legendNodeId );	//视图对应的数据
				
				boolean found = false;
				for (T t : legendData) {
					if (axisNode.getId().equals(value(t, axisNode.getIdField())) ) { //轴对应的数据
						found = true;
						axisNodesFound.set(m, true);	//找到
						axisFound.set(m,true);	//找到
						value.add( value(t, legendNode.getValueField()) );	// 取值
						if (axisNode.getNameField() != null) {
							axisNode.setName(value(t, axisNode.getNameField()));
						}
						if(axisNode.getSuffixNameField() != null){
							axisNode.setSuffixName(value(t, axisNode.getSuffixNameField()));
						}
						break;
					}
				}
				if(! found){	//没找到
					value.add( legendNode.getDefaultValue() );	// 默认值
				}
			}
			
			//将视图相关封装起来
			LegendLink legendLink = new LegendLink(legendNode, value, axisFound);
			legendLinkList.add(legendLink);
		}

	}
	
	/**
	 * 一个封装对象
	 */
	private class LegendLink {
		public LegendNode legendNode;
		public List<Object> value;
		public List<Boolean> axisFound;
		public LegendLink(LegendNode legendNode, List<Object> value, List<Boolean> axisFound) {
			this.legendNode = legendNode;
			this.value = value;
			this.axisFound = axisFound;
		}
	}
	
	/**
	 * 反射取值
	 */
	private Object value(T t,String fieldName){
		Method method = reflector.getGetter(fieldName);
		Object value = reflector.invokeMethod(method, t);
		return value;
	}
	
	/**
	 * 获取视图显示名
	 */
	private Object getLegendName(LegendNode legendNode){
		if(legendNode.getName() == null){
			return null;
		}
		StringBuilder fullName = new StringBuilder(); 
		fullName.append( legendNode.getName() );
		if(legendNode.getSuffixName() != null){
			fullName.append( legendNode.getSuffixName() );
		}
		return fullName.toString();
	}
	
	/**
	 * 获取轴显示名
	 */
	private Object getAxisName(AxisNode axisNode){
		if(axisNode.getName() == null){
			return null;
		}
		StringBuilder fullName = new StringBuilder(); 
		fullName.append( axisNode.getName() );
		if(axisNode.getSuffixName() != null){
			fullName.append( axisNode.getSuffixName() );
		}
		return fullName.toString();
	}
	
	/**
	 * 提取视图
	 * @author yangwk
	 *
	 * @return
	 */
	public List<Object> extractLegend(){
		List<Object> legend = new ArrayList<Object>();
		for(int k=0; k < legendNodes.size(); k++){
			if( filterNotFound && ! legendNodesFound.get(k) ){	//如果过滤找不到的
				continue;
			}
			LegendNode legendNode = legendNodes.get(k);
			legend.add( getLegendName(legendNode) );
		}
		return legend;
	}
	
	/**
	 * 提取轴
	 * @author yangwk
	 *
	 * @return
	 */
	public List<Object> extractAxis(){
		List<Object> axis = new ArrayList<Object>();
		for(int k=0; k < axisNodes.size(); k++){
			if( filterNotFound && ! axisNodesFound.get(k)){
				continue;
			}
			AxisNode axisNode = axisNodes.get(k);
			axis.add( getAxisName(axisNode) );
		}
		return axis;
	}
	
	/**
	 * 提取数据定义
	 * <p>返回的SeriesNode的type值为null
	 * @author yangwk
	 *
	 * @return
	 */
	public List<SeriesNode> extractSeries(){
		List<SeriesNode> series = new ArrayList<SeriesNode>();
		for(int k=0; k<legendLinkList.size();k++){
			if( filterNotFound && ! legendNodesFound.get(k) ){
				continue;
			}
			LegendLink legendLink = legendLinkList.get(k);
			LegendNode legendNode = legendLink.legendNode;
			List<Object> value = legendLink.value;
			List<Boolean> axisFound = legendLink.axisFound;
			
			List<Object> newValue = new ArrayList<Object>();	//过滤后
			if(filterNotFound){
				for(int m=0;m<axisNodes.size(); m++){
					if(axisNodesFound.get(m)){	//找到
						Object v = value.get(m);
						//轴没找到 或 legendnode值为null
						if(! axisFound.get(m) || v == null ){
							newValue.add("-");	//用-代表无
						}
						else{
							newValue.add( v );	//取值
						}
					}
				}
			}
			
			SeriesNode seriesNode = new SeriesNode();
			seriesNode.setName( getLegendName(legendNode) );
			seriesNode.setType(null);
			seriesNode.setData( filterNotFound ? newValue : value);
			
			series.add(seriesNode);
		}
		return series;
	}

	public List<LegendNode> getLegendNodes() {
		return legendNodes;
	}

	public List<AxisNode> getAxisNodes() {
		return axisNodes;
	}

	public List<T> getDataList() {
		return dataList;
	}

}
