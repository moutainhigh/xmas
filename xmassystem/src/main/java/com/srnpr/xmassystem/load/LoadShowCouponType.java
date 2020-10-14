package com.srnpr.xmassystem.load;

import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelShowCouponType;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 加载在商品详情页展示的优惠券类型列表
 */
public class LoadShowCouponType extends LoadTop<PlusModelShowCouponType, PlusModelQuery> {

	public PlusModelShowCouponType topInitInfo(PlusModelQuery query) {
		PlusModelShowCouponType couponType = new PlusModelShowCouponType();
		
		// 查询需要在商品详情页展示的优惠券
		String couponCodeTypeSql = "select ot.coupon_type_code from oc_coupon_type ot,oc_activity oa WHERE ot.activity_code = oa.activity_code "
				+ " and ot.produce_type = '4497471600040001' and oa.begin_time <= sysdate() and oa.end_time > sysdate() "
				+ " and oa.provide_type = '4497471600060002' and oa.flag = 1 "
				+ " and oa.is_detail_show = '449748350002' and oa.seller_code = :seller_code"
				+ " and ot.exchange_type = '4497471600390001' and ot.status = '4497469400030002' "
				+ " order by ot.create_time desc ";
		
		List<Map<String, Object>> list = DbUp.upTable("oc_coupon_type").dataSqlList(couponCodeTypeSql, new MDataMap("seller_code", query.getCode()));
		for(Map<String, Object> m : list) {
			couponType.getCouponTypeList().add(m.get("coupon_type_code").toString());
		}
		return couponType;
		
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.ShowCouponType;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelShowCouponType.class;
		}
		
		public int getExpireSecond() {
			return 60;
		}
		
	};

}
