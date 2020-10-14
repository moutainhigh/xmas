package com.srnpr.xmasorder.channel.gt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.channel.model.PorscheModelChannelOrder;
import com.srnpr.xmasorder.channel.model.PorscheModelGoodsInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderAddress;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfoUpper;
import com.srnpr.xmasorder.channel.model.PorscheModelStatus;
import com.srnpr.xmasorder.channel.model.PorscheModelSubRole;

public class PorscheGtOrder {

	/**
	 * 订单主信息
	 */
	private PorscheModelOrderInfoUpper orderInfoUpper = new PorscheModelOrderInfoUpper();

	/**
	 * 小订单主信息
	 */
	private List<PorscheModelOrderInfo> orderInfo = new ArrayList<PorscheModelOrderInfo>();
	
	/**
	 * 订单明细
	 */
	private List<PorscheModelOrderDetail> orderDetails = new ArrayList<PorscheModelOrderDetail>();
	
	/**
	 * 地址信息
	 */
	private PorscheModelOrderAddress address = new PorscheModelOrderAddress();
	
	/**
	 * 订单标记
	 */
	private PorscheModelStatus status = new PorscheModelStatus();
	
	/**
	 * 拆单规则
	 */
	private List<PorscheModelSubRole> roles = new ArrayList<PorscheModelSubRole>();
	
	/**
	 * 商品展示使用
	 */
	private List<PorscheModelGoodsInfo> showGoods = new ArrayList<PorscheModelGoodsInfo>();
	
	/**
	 * 渠道商订单信息
	 */
	private List<PorscheModelChannelOrder> channelOrder = new ArrayList<PorscheModelChannelOrder>();
	
	/**
	 * 渠道编号
	 */
	private String channelId = "449747430011";
	
	/**
	 * 用户编号
	 */
	private String memberCode="";
	
	/**
	 * 渠道商编号
	 */
	private String channelSellerCode = "";
	
	/**
	 * 渠道商订单编号
	 */
	private String outOrderCode = "";
	
	/**
	 * 渠道商运费
	 */
	private BigDecimal transportMoney = BigDecimal.ZERO;

	public PorscheModelOrderInfoUpper getOrderInfoUpper() {
		return orderInfoUpper;
	}

	public void setOrderInfoUpper(PorscheModelOrderInfoUpper orderInfoUpper) {
		this.orderInfoUpper = orderInfoUpper;
	}

	public List<PorscheModelOrderInfo> getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(List<PorscheModelOrderInfo> orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<PorscheModelOrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<PorscheModelOrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public PorscheModelOrderAddress getAddress() {
		return address;
	}

	public void setAddress(PorscheModelOrderAddress address) {
		this.address = address;
	}

	public PorscheModelStatus getStatus() {
		return status;
	}

	public void setStatus(PorscheModelStatus status) {
		this.status = status;
	}

	public List<PorscheModelSubRole> getRoles() {
		return roles;
	}

	public void setRoles(List<PorscheModelSubRole> roles) {
		this.roles = roles;
	}

	public List<PorscheModelGoodsInfo> getShowGoods() {
		return showGoods;
	}

	public void setShowGoods(List<PorscheModelGoodsInfo> showGoods) {
		this.showGoods = showGoods;
	}

	public List<PorscheModelChannelOrder> getChannelOrder() {
		return channelOrder;
	}

	public void setChannelOrder(List<PorscheModelChannelOrder> channelOrder) {
		this.channelOrder = channelOrder;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getChannelSellerCode() {
		return channelSellerCode;
	}

	public void setChannelSellerCode(String channelSellerCode) {
		this.channelSellerCode = channelSellerCode;
	}

	public String getOutOrderCode() {
		return outOrderCode;
	}

	public void setOutOrderCode(String outOrderCode) {
		this.outOrderCode = outOrderCode;
	}

	public BigDecimal getTransportMoney() {
		return transportMoney;
	}

	public void setTransportMoney(BigDecimal transportMoney) {
		this.transportMoney = transportMoney;
	}

}
