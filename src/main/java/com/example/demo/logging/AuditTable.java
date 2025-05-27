package com.example.demo.logging;

public class AuditTable {

	private String corelationId;
	private long duration;
	private String url;
	private String request;
	private String response;
	public String getCorelationId() {
		return corelationId;
	}
	public void setCorelationId(String corelationId) {
		this.corelationId = corelationId;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	@Override
	public String toString() {
		return "AuditTable [corelationId=" + corelationId + ", duration=" + duration + ", url=" + url + ", request="
				+ request + ", response=" + response + "]";
	}
	
	
	
}
