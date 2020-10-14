package com.srnpr.xmassystem.modelbean;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.enumer.Channel;

/**
 * 支付类型信息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月12日
 */
public class PayTypeInfo {

	/**
	 * 支付类型
	 */
	private String payType = "";
	/**
	 * 支付类型支持的渠道
	 */
	private List<Channel> channelList = new ArrayList<Channel>();
	/**
	 * 支付类型屏蔽的商户
	 */
	private List<String> sellerList = new ArrayList<String>();
	/**
	 * 支付类型屏蔽的商户类型
	 */
	private List<String> sellerTypeList = new ArrayList<String>();
	/**
	 * 支付类型支持的商品
	 */
	private List<String> typeProductList = new ArrayList<String>();
	
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public List<Channel> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}
	public List<String> getSellerList() {
		return sellerList;
	}
	public void setSellerList(List<String> sellerList) {
		this.sellerList = sellerList;
	}
	public List<String> getSellerTypeList() {
		return sellerTypeList;
	}
	public void setSellerTypeList(List<String> sellerTypeList) {
		this.sellerTypeList = sellerTypeList;
	}
	public List<String> getTypeProductList() {
		return typeProductList;
	}
	public void setTypeProductList(List<String> typeProductList) {
		this.typeProductList = typeProductList;
	}
	
}
