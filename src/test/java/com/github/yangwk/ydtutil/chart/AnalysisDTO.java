package com.github.yangwk.ydtutil.chart;
//图形分析通用DTO模板
public	class AnalysisDTO {

		private Integer year;	//年
		private Integer month;	//月
		private String divCode;	//地区码
		private String divName;	//地区名
		private String indexCode;	//指标码
		private String indexName;	//指标名
		private String unitName;	//指标单位名
		private String indexValue;	//指标值
		
		public AnalysisDTO(Integer year, Integer month, String divCode, String divName, String indexCode,
				String indexName, String unitName, String indexValue) {
			super();
			this.year = year;
			this.month = month;
			this.divCode = divCode;
			this.divName = divName;
			this.indexCode = indexCode;
			this.indexName = indexName;
			this.unitName = unitName;
			this.indexValue = indexValue;
		}
		
		public Integer getYear() {
			return year;
		}
		public void setYear(Integer year) {
			this.year = year;
		}
		public Integer getMonth() {
			return month;
		}
		public void setMonth(Integer month) {
			this.month = month;
		}
		public String getDivCode() {
			return divCode;
		}
		public void setDivCode(String divCode) {
			this.divCode = divCode;
		}
		public String getDivName() {
			return divName;
		}
		public void setDivName(String divName) {
			this.divName = divName;
		}
		public String getIndexCode() {
			return indexCode;
		}
		public void setIndexCode(String indexCode) {
			this.indexCode = indexCode;
		}
		public String getIndexName() {
			return indexName;
		}
		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}
		public String getUnitName() {
			return unitName;
		}
		public void setUnitName(String unitName) {
			this.unitName = unitName;
		}
		public String getIndexValue() {
			return indexValue;
		}
		public void setIndexValue(String indexValue) {
			this.indexValue = indexValue;
		}
		
	}