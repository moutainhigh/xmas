package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/***
 * 优惠券类型实体类
 */
public class PlusModelCouponType implements IPlusModel, IPlusModelRefresh {

	/** 当前版本号 */
	public static final int _VERSION = 2;
	/** 版本号 */
	@ZapcomApi(value = "数据版本号")
	private int v = 0;
	
	@Override
	public boolean isRefresh() {
		return v < _VERSION;
	}
	
	public void setV(int v) {
		this.v = v;
	}
	
	public int getV() {
		return v;
	}
	
	private String uid = "";
	private String couponTypeCode = "";
	private String couponTypeName = "";
	private String activityCode = "";
	private BigDecimal money = BigDecimal.ZERO;
	private BigDecimal limitMoney = BigDecimal.ZERO;
	private BigDecimal totalMoney = BigDecimal.ZERO;
	private BigDecimal surplusMoney = BigDecimal.ZERO;
	private BigDecimal privideMoney = BigDecimal.ZERO;
	private String startTime = "";
	private String endTime = "";
	private String status = "";
	private String multiAccount = "";
	private int accountUseTime = 0;
	private String limitCondition = "";
	private String limitScope = "";
	private String limitExplain = "";
	private String validType = "";
	private int validDay = 0;
	private String manageCode = "";
	private String actionType = "";
	private String actionValue = "";
	private String exchangeType = "";
	private String exchangeValue = "";
	private String moneyType = "";
	
	private CouponTypeLimit couponTypeLimit = new CouponTypeLimit();
	
	public static class CouponTypeLimit {
		private String brandLimit = "";
		private String productLimit = "";
		private String categoryLimit = "";
		private String channelLimit = "";
		private String activityLimit = "";
		private int exceptBrand;
		private int exceptProduct;
		private int exceptCategory;
		private int exceptChannel;
		private String brandCodes = "";
		private String productCodes = "";
		private String categoryCodes = "";
		private String channelCodes = "";
		private String sellerLimit = "";
		private String paymentType = "";
		private String allowedActivityType = "";
		
		public String getBrandLimit() {
			return brandLimit;
		}
		public void setBrandLimit(String brandLimit) {
			this.brandLimit = brandLimit;
		}
		public String getProductLimit() {
			return productLimit;
		}
		public void setProductLimit(String productLimit) {
			this.productLimit = productLimit;
		}
		public String getCategoryLimit() {
			return categoryLimit;
		}
		public void setCategoryLimit(String categoryLimit) {
			this.categoryLimit = categoryLimit;
		}
		public String getChannelLimit() {
			return channelLimit;
		}
		public void setChannelLimit(String channelLimit) {
			this.channelLimit = channelLimit;
		}
		public String getActivityLimit() {
			return activityLimit;
		}
		public void setActivityLimit(String activityLimit) {
			this.activityLimit = activityLimit;
		}
		public int getExceptBrand() {
			return exceptBrand;
		}
		public void setExceptBrand(int exceptBrand) {
			this.exceptBrand = exceptBrand;
		}
		public int getExceptProduct() {
			return exceptProduct;
		}
		public void setExceptProduct(int exceptProduct) {
			this.exceptProduct = exceptProduct;
		}
		public int getExceptCategory() {
			return exceptCategory;
		}
		public void setExceptCategory(int exceptCategory) {
			this.exceptCategory = exceptCategory;
		}
		public int getExceptChannel() {
			return exceptChannel;
		}
		public void setExceptChannel(int exceptChannel) {
			this.exceptChannel = exceptChannel;
		}
		public String getBrandCodes() {
			return brandCodes;
		}
		public void setBrandCodes(String brandCodes) {
			this.brandCodes = brandCodes;
		}
		public String getProductCodes() {
			return productCodes;
		}
		public void setProductCodes(String productCodes) {
			this.productCodes = productCodes;
		}
		public String getCategoryCodes() {
			return categoryCodes;
		}
		public void setCategoryCodes(String categoryCodes) {
			this.categoryCodes = categoryCodes;
		}
		public String getChannelCodes() {
			return channelCodes;
		}
		public void setChannelCodes(String channelCodes) {
			this.channelCodes = channelCodes;
		}
		public String getSellerLimit() {
			return sellerLimit;
		}
		public void setSellerLimit(String sellerLimit) {
			this.sellerLimit = sellerLimit;
		}
		public String getPaymentType() {
			return paymentType;
		}
		public void setPaymentType(String paymentType) {
			this.paymentType = paymentType;
		}
		public String getAllowedActivityType() {
			return allowedActivityType;
		}
		public void setAllowedActivityType(String allowedActivityType) {
			this.allowedActivityType = allowedActivityType;
		}
		
	}
	
	public String getCouponTypeCode() {
		return couponTypeCode;
	}
	public void setCouponTypeCode(String couponTypeCode) {
		this.couponTypeCode = couponTypeCode;
	}
	public String getCouponTypeName() {
		return couponTypeName;
	}
	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getLimitMoney() {
		return limitMoney;
	}
	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMultiAccount() {
		return multiAccount;
	}
	public void setMultiAccount(String multiAccount) {
		this.multiAccount = multiAccount;
	}
	public int getAccountUseTime() {
		return accountUseTime;
	}
	public void setAccountUseTime(int accountUseTime) {
		this.accountUseTime = accountUseTime;
	}
	public String getLimitCondition() {
		return limitCondition;
	}
	public void setLimitCondition(String limitCondition) {
		this.limitCondition = limitCondition;
	}
	public String getLimitScope() {
		return limitScope;
	}
	public void setLimitScope(String limitScope) {
		this.limitScope = limitScope;
	}
	public String getLimitExplain() {
		return limitExplain;
	}
	public void setLimitExplain(String limitExplain) {
		this.limitExplain = limitExplain;
	}
	public String getValidType() {
		return validType;
	}
	public void setValidType(String validType) {
		this.validType = validType;
	}
	public int getValidDay() {
		return validDay;
	}
	public void setValidDay(int validDay) {
		this.validDay = validDay;
	}
	public String getManageCode() {
		return manageCode;
	}
	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getActionValue() {
		return actionValue;
	}
	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public CouponTypeLimit getCouponTypeLimit() {
		return couponTypeLimit;
	}
	public void setCouponTypeLimit(CouponTypeLimit couponTypeLimit) {
		this.couponTypeLimit = couponTypeLimit;
	}

	public BigDecimal getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(BigDecimal surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

	public BigDecimal getPrivideMoney() {
		return privideMoney;
	}

	public void setPrivideMoney(BigDecimal privideMoney) {
		this.privideMoney = privideMoney;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getExchangeValue() {
		return exchangeValue;
	}

	public void setExchangeValue(String exchangeValue) {
		this.exchangeValue = exchangeValue;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
