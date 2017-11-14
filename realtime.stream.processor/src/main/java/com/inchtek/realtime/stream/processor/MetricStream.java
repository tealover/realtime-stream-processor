/**
 * 
 */
package com.inchtek.realtime.stream.processor;

import java.util.Map;

/**
 * @author inchin
 *
 */
public class MetricStream {
	/**
	 * 指标
	 */
	String metric;
	/**
	 * 时间戳
	 */
	long timestamp;
	/**
	 * 指标值
	 */
	double value;
	/**
	 * 标签
	 */
	Map<String, String> tags;
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Map<String, String> getTags() {
		return tags;
	}
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
