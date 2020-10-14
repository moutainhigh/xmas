package com.srnpr.xmasorder.model.jingdong;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 4.3	统一下单接口 响应VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月23日
 */
public class CreateOrderSuccessVO {

	/**
	 * 京东订单号
	 */
	private String jdOrderId = "";
	
	/**
	 * 商品信息
	 */
	private List<OrderDetailSkuRepVO> sku = new ArrayList<OrderDetailSkuRepVO>();
	
	/**
	 * 运费（合同有运费配置才会返回，否则不会返回该字段）
	 */
	private BigDecimal freight = BigDecimal.ZERO;
	
	/**
	 * 订单价格
	 */
	private BigDecimal orderPrice = BigDecimal.ZERO;
	
	/**
	 * 订单裸价
	 */
	private BigDecimal orderNakedPrice = BigDecimal.ZERO;
	
	/**
	 * 订单税额
	 */
	private BigDecimal orderTaxPrice = BigDecimal.ZERO;

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public List<OrderDetailSkuRepVO> getSku() {
		return sku;
	}

	public void setSku(List<OrderDetailSkuRepVO> sku) {
		this.sku = sku;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderNakedPrice() {
		return orderNakedPrice;
	}

	public void setOrderNakedPrice(BigDecimal orderNakedPrice) {
		this.orderNakedPrice = orderNakedPrice;
	}

	public BigDecimal getOrderTaxPrice() {
		return orderTaxPrice;
	}

	public void setOrderTaxPrice(BigDecimal orderTaxPrice) {
		this.orderTaxPrice = orderTaxPrice;
	}
	
}
