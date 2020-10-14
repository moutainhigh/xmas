package com.srnpr.xmassystem.invoke.ref.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新客户的积分、暂存款、储值金、惠币
 */
public class UpdateCustAmtInput {

	// 操作标识
	private CurdFlag curdFlag;
	// 客代号
	private String custId;
	// 客户名称
	private String custName = "";
	// 地址
	private String address = "";
	// 大订单号
	private String bigOrderCode;
	//操作惠币类型，10：正式，20：预估
	private String hcoinStatCd;
	// 子订单明细
	private List<ChildOrder> orderList = new ArrayList<ChildOrder>();

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getBigOrderCode() {
		return bigOrderCode;
	}

	public void setBigOrderCode(String bigOrderCode) {
		this.bigOrderCode = bigOrderCode;
	}

	public List<ChildOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<ChildOrder> orderList) {
		this.orderList = orderList;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CurdFlag getCurdFlag() {
		return curdFlag;
	}

	public void setCurdFlag(CurdFlag curdFlag) {
		this.curdFlag = curdFlag;
	}

	public String getHcoinStatCd() {
		return hcoinStatCd;
	}

	public void setHcoinStatCd(String hcoinStatCd) {
		this.hcoinStatCd = hcoinStatCd;
	}

	public static class ChildOrder {
		// 子订单号
		private String appChildOrdId;
		// 积分
		private BigDecimal childAccmAmt = new BigDecimal(0);
		// 暂存款
		private BigDecimal childCrdtAmt = new BigDecimal(0);
		// 储值金
		private BigDecimal childPpcAmt = new BigDecimal(0);
		// 惠币
		private BigDecimal childHcoinAmt = new BigDecimal(0);

		public BigDecimal getChildHcoinAmt() {
			return childHcoinAmt;
		}

		public void setChildHcoinAmt(BigDecimal childHcoinAmt) {
			this.childHcoinAmt = childHcoinAmt;
		}

		public String getAppChildOrdId() {
			return appChildOrdId;
		}

		public void setAppChildOrdId(String appChildOrdId) {
			this.appChildOrdId = appChildOrdId;
		}

		public BigDecimal getChildAccmAmt() {
			return childAccmAmt;
		}

		public void setChildAccmAmt(BigDecimal childAccmAmt) {
			this.childAccmAmt = childAccmAmt;
		}

		public BigDecimal getChildCrdtAmt() {
			return childCrdtAmt;
		}

		public void setChildCrdtAmt(BigDecimal childCrdtAmt) {
			this.childCrdtAmt = childCrdtAmt;
		}

		public BigDecimal getChildPpcAmt() {
			return childPpcAmt;
		}

		public void setChildPpcAmt(BigDecimal childPpcAmt) {
			this.childPpcAmt = childPpcAmt;
		}

	}

	/**
	 * 操作标识
	 */
	public static enum CurdFlag {
		/** 占用 */
		C,
		/** 取消 */
		D,
		/** 使用 */
		U,
		/** 退货 */
		R,
		/** 
		 * 取消占用 , TV品未生成LD订单号时取消订单的情况下使用
		 */
		F,
		/** 自营品签收 */
		ZA,
		/** 自营品退货 */
		ZB,
		/** 自营品取消退货 */
		ZC,
		/** 积分共享赋予 */
		G,
		/** 积分共享还原 */
		GR,
		/** 积分共享取消退后*/
		GRC,
		/** 积分兑换优惠券 */
		JFU,
		/** 评论送积分 */
		GI,
		/** 退货挽留送积分 */
		DT,
		/** 签到送积分 */
		DK,
		/** 福利转盘送积分 */
		DZ,
		/** 积分大转盘兑换抽奖次数 */
		DZC,
		/** 浏览专题送积分 */
		ZT,
		/** 切蛋糕送积分 */
		CC,
		/** 惠币预估收益转正 */
		ZZHB,
		/** 推广赚提现 */
		TXHB,
		/** 提现还原 */
		TXHY,
		/**自营品订单奖励惠币，预估收益*/
		ZAHB,
		/**惠家有推广下单赋予推广人惠币，预估收益*/
		ZBHB,
		/**惠家有自营品取消订单还原赋予的惠币，预估收益*/
		ZDHB,
		/**惠家有自营品取消订单还原赋予推广人的惠币，预估收益*/
		ZEHB,
		/**惠家有自营品退货订单还原赋予的惠币，预估收益*/
		ZFHB,
		/**惠家有自营品退货订单还原赋予推广人的惠币，预估收益*/
		ZGHB,
		/**惠家有自营品取消退货订单还原赋予的惠币，预估收益*/
		ZHHB,
		/**惠家有自营品取消退货订单还原赋予推广人的惠币，预估收益*/
		ZIHB,
		/**惠家有通过买家秀下单赋予买家秀推广惠币*/
		ZMHB
		
	}
}
