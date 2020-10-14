package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.top.QueryTop;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 用于查询商品信息类
 * 
 * @author srnpr
 *
 */
public class PlusModelSkuQuery extends QueryTop {

	@ZapcomApi(value = "编号", remark = "用逗号分隔,传入活动明细编号IC开头的编号", require = 1)
	private String code = "";

	@ZapcomApi(value = "用户编号", remark = "用户编号，除非特别要求下默认情况下请传空")
	private String memberCode = "";

	@ZapcomApi(value = "区域编号", remark = "地址区域编号，用于分仓分库存使用，默认情况下传空")
	private String areaCode = "";

	@ZapcomApi(value = "来源编号", remark = "来源编号，用于分来源显示不同价格，默认情况下传空")
	private String sourceCode = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value="用户手机号",remark="如果没有用户编号，则传手机号也可以进行查询相应的价格")
	private String mobile = "";
	
	@ZapcomApi(value="订单来源",remark="暂时仅用于判断多彩订单--rhb")
	private String orderSource = "";
	
	@ZapcomApi(value="积分标示",remark="0: 非积分商城, 1: 积分商城")
	private String integralFlag = "0";
	
	@ZapcomApi(value = "是否显示价格曲线", remark = "标识是否显示价格曲线,显示:Y,不显示:N")
	private String showPriceCurve = "";
	
	@ZapcomApi(value="是否原价购买",remark="0:非原价购买，1:原价购买")
	private String isOriginal = "0";
	
	@ZapcomApi(value="是否支持拼团",remark="0：不拼团，过滤掉拼团价格，1：支持拼团")
	private String isSupportCollage = "1";
	
	@ZapcomApi(value="渠道编号",remark="449747430001: APP，449747430003: 微信商城，449747430004: PC，449747430023 :小程序")
	private String channelId = "449747430001";
	
	@ZapcomApi(value="是否兑换码兑换")
	private boolean isRedeem = false;
	
	@ZapcomApi(value="是否为加价购商品 0否 1是")
	private String ifJJGFlag = "0";
	
	@ZapcomApi(value="是否从分销模板进入详情1是 0否")
	private String fxFlag = "0";
	
	@ZapcomApi(value="特殊促销活动编号",remark="换购投票")
	private String eventCode = "";
	
	public String getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}

	public String getIfJJGFlag() {
		return ifJJGFlag;
	}

	public void setIfJJGFlag(String ifJJGFlag) {
		this.ifJJGFlag = ifJJGFlag;
	}
	
	public String getShowPriceCurve() {
		return showPriceCurve;
	}

	public void setShowPriceCurve(String showPriceCurve) {
		this.showPriceCurve = showPriceCurve;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getIntegralFlag() {
		return integralFlag;
	}

	public void setIntegralFlag(String integralFlag) {
		this.integralFlag = integralFlag;
	}

	public String getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(String isOriginal) {
		this.isOriginal = isOriginal;
	}

	public String getIsSupportCollage() {
		return isSupportCollage;
	}

	public void setIsSupportCollage(String isSupportCollage) {
		this.isSupportCollage = isSupportCollage;
	}
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public boolean isRedeem() {
		return isRedeem;
	}

	public void setRedeem(boolean isRedeem) {
		this.isRedeem = isRedeem;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

}
