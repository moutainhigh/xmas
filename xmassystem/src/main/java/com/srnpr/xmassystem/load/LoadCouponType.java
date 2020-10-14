package com.srnpr.xmassystem.load;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载优惠券类型数据
 */
public class LoadCouponType extends LoadTop<PlusModelCouponType, PlusModelQuery> {

	public PlusModelCouponType topInitInfo(PlusModelQuery query) {
		PlusModelCouponType couponType = new PlusModelCouponType();
		
		// 基本信息
		MDataMap couponTypeMap = DbUp.upTable("oc_coupon_type").one("coupon_type_code", query.getCode());
		if(couponTypeMap != null) {
			couponType.setUid(couponTypeMap.get("uid"));
			couponType.setCouponTypeCode(couponTypeMap.get("coupon_type_code"));
			couponType.setCouponTypeName(couponTypeMap.get("coupon_type_name"));
			couponType.setActivityCode(couponTypeMap.get("activity_code"));
			couponType.setMoney(new BigDecimal(couponTypeMap.get("money")));
			couponType.setLimitMoney(new BigDecimal(couponTypeMap.get("limit_money")));
			couponType.setTotalMoney(new BigDecimal(couponTypeMap.get("total_money")));
			couponType.setSurplusMoney(new BigDecimal(couponTypeMap.get("surplus_money")));
			couponType.setPrivideMoney(new BigDecimal(couponTypeMap.get("privide_money")));
			couponType.setStartTime(couponTypeMap.get("start_time"));
			couponType.setEndTime(couponTypeMap.get("end_time"));
			couponType.setStatus(couponTypeMap.get("status"));
			couponType.setMultiAccount(couponTypeMap.get("multi_account"));
			couponType.setAccountUseTime(NumberUtils.toInt(couponTypeMap.get("account_useTime")));
			couponType.setLimitCondition(couponTypeMap.get("limit_condition"));
			couponType.setLimitScope(couponTypeMap.get("limit_scope"));
			couponType.setLimitExplain(couponTypeMap.get("limit_explain"));
			couponType.setValidType(couponTypeMap.get("valid_type"));
			couponType.setValidDay(NumberUtils.toInt(couponTypeMap.get("valid_day")));
			couponType.setManageCode(couponTypeMap.get("manage_code"));
			couponType.setActionType(couponTypeMap.get("action_type"));
			couponType.setActionValue(couponTypeMap.get("action_value"));
			couponType.setMoneyType(couponTypeMap.get("money_type"));
			couponType.setExchangeType(couponTypeMap.get("exchange_type"));
			couponType.setExchangeValue(couponTypeMap.get("exchange_value"));
		}
		
		// 限制条件
		MDataMap couponTypeLimitMap = DbUp.upTable("oc_coupon_type_limit").one("coupon_type_code", query.getCode());
		if(couponTypeLimitMap != null) {
			couponType.getCouponTypeLimit().setBrandLimit(couponTypeLimitMap.get("brand_limit"));
			couponType.getCouponTypeLimit().setProductLimit(couponTypeLimitMap.get("product_limit"));
			couponType.getCouponTypeLimit().setCategoryLimit(couponTypeLimitMap.get("category_limit"));
			couponType.getCouponTypeLimit().setChannelLimit(couponTypeLimitMap.get("channel_limit"));
			couponType.getCouponTypeLimit().setActivityLimit(couponTypeLimitMap.get("activity_limit"));
			couponType.getCouponTypeLimit().setExceptBrand(NumberUtils.toInt(couponTypeLimitMap.get("except_brand")));
			couponType.getCouponTypeLimit().setExceptProduct(NumberUtils.toInt(couponTypeLimitMap.get("except_product")));
			couponType.getCouponTypeLimit().setExceptCategory(NumberUtils.toInt(couponTypeLimitMap.get("except_category")));
			couponType.getCouponTypeLimit().setExceptChannel(NumberUtils.toInt(couponTypeLimitMap.get("except_channel")));
			couponType.getCouponTypeLimit().setProductCodes(couponTypeLimitMap.get("product_codes"));
			couponType.getCouponTypeLimit().setCategoryCodes(couponTypeLimitMap.get("category_codes"));
			couponType.getCouponTypeLimit().setBrandCodes(couponTypeLimitMap.get("brand_codes"));
			couponType.getCouponTypeLimit().setChannelCodes(couponTypeLimitMap.get("channel_codes"));
			couponType.getCouponTypeLimit().setSellerLimit(couponTypeLimitMap.get("seller_limit"));
			couponType.getCouponTypeLimit().setPaymentType(couponTypeLimitMap.get("payment_type"));
			couponType.getCouponTypeLimit().setAllowedActivityType(couponTypeLimitMap.get("allowed_activity_type"));
		}
		
		// 设置版本号
		couponType.setV(PlusModelCouponType._VERSION);
		
		return couponType;
		
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.CouponType;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelCouponType.class;
		}
		
		public int getExpireSecond() {
			return 60*30;
		}
		
	};

}
