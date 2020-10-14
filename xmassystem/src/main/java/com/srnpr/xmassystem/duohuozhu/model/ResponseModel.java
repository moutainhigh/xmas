package com.srnpr.xmassystem.duohuozhu.model;

import java.util.HashMap;
import java.util.Map;

public class ResponseModel {

	private ResponseHeaderModel header = new ResponseHeaderModel();
	
	/**
	 * body中子节点对象皆为List
	 */
	private Map<String, Object> body = new HashMap<String, Object>();

	public ResponseHeaderModel getHeader() {
		return header;
	}

	public void setHeader(ResponseHeaderModel header) {
		this.header = header;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	
}
