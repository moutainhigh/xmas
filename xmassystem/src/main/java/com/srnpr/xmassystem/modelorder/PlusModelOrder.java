package com.srnpr.xmassystem.modelorder;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 基础订单信息
 * 
 * @author srnpr
 *
 */
public class PlusModelOrder implements IPlusModel {

	@ZapcomApi(value = "订单主信息")
	private OrderInfo info = new OrderInfo();

	@ZapcomApi(value = "订单明细")
	private List<OrderDetail> details = new ArrayList<OrderDetail>();

	@ZapcomApi(value = "订单参与活动信息")
	private List<OrderEvent> events = new ArrayList<OrderEvent>();

	@ZapcomApi(value = "订单地址信息")
	private OrderAddress address = new OrderAddress();

	@ZapcomApi(value = "拆单信息")
	private List<OrderSmall> smalls = new ArrayList<OrderSmall>();

	public OrderInfo getInfo() {
		return info;
	}

	public void setInfo(OrderInfo info) {
		this.info = info;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

	public List<OrderEvent> getEvents() {
		return events;
	}

	public void setEvents(List<OrderEvent> events) {
		this.events = events;
	}

	public OrderAddress getAddress() {
		return address;
	}

	public void setAddress(OrderAddress address) {
		this.address = address;
	}

	public List<OrderSmall> getSmalls() {
		return smalls;
	}

	public void setSmalls(List<OrderSmall> smalls) {
		this.smalls = smalls;
	}

}
