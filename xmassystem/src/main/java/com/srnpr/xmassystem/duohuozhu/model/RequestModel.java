package com.srnpr.xmassystem.duohuozhu.model;

import java.util.LinkedHashMap;

public class RequestModel {

	private RequestHeadModel head = new RequestHeadModel();
	
	private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();

	public RequestHeadModel getHead() {
		return head;
	}

	public void setHead(RequestHeadModel head) {
		this.head = head;
	}

	public LinkedHashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Object> body) {
		this.body = body;
	}


}
