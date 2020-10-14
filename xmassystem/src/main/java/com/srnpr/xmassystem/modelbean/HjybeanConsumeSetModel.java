package com.srnpr.xmassystem.modelbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 惠家有  惠豆消费配置 model
 * @author fq
 *
 */
public class HjybeanConsumeSetModel {

	/**
	 * 商户类型
	 */
	private List<String> sellerType = new ArrayList<String>();
	/**
	 * 最小使用数量
	 */
	private BigDecimal minUse = BigDecimal.ZERO;
	/**
	 * 最大不超过单笔订单的比例
	 * eg ：maxPercent = 10, 则比率为：10%;
	 */
	private BigDecimal maxPercent = BigDecimal.ZERO;
	
	/**
	 * 惠豆与人品币的兑换比   ratio = 人品币(RMB) /惠豆
	 * 默认值为  100(惠豆) = 1元(RMB)
	 */
	private BigDecimal ratio = BigDecimal.valueOf(0.01);
	
	public List<String> getSellerType() {
		return sellerType;
	}
	public void setSellerType(List<String> sellerType) {
		this.sellerType = sellerType;
	}
	public BigDecimal getMinUse() {
		return minUse;
	}
	public void setMinUse(BigDecimal minUse) {
		this.minUse = minUse;
	}
	public BigDecimal getMaxPercent() {
		return maxPercent;
	}
	public void setMaxPercent(BigDecimal maxPercent) {
		this.maxPercent = maxPercent;
	}
	public BigDecimal getRatio() {
		return ratio;
	}
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
}
