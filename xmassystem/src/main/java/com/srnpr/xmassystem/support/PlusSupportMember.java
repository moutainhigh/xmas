package com.srnpr.xmassystem.support;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class PlusSupportMember {

	/*
	 * 用户有效订单
	 */
	private final static String ACTIVE_ORDER_SUM = "activeOrderSum";

	/*
	 * 返回用户是否首单
	 */
	public boolean upFlagFirstOrder(String sMemberCode) {

		boolean bFlagFirst = true;

		if (StringUtils.isNotBlank(sMemberCode)) {

			bFlagFirst = upMemberOrderSum(sMemberCode) <= 0;

		}

		return bFlagFirst;

	}
	
	/**
	 * 用于下单后判断是否是该用户的首单
	 * @param sMemberCode
	 * @author zht
	 * @return
	 */
	public boolean upFlagFirstOrderAfterBooking(String sMemberCode) {
		boolean bFlagFirst = true;
		if (StringUtils.isNotBlank(sMemberCode)) {
			bFlagFirst = upMemberOrderSumAfterBooking(sMemberCode) <= 1;
		}
		return bFlagFirst;
	}

	public int upMemberOrderSum(String sMemberCode) {
		String sOrderSum = XmasKv.upFactory(EKvSchema.Member).hget(sMemberCode,
				ACTIVE_ORDER_SUM);

		if (StringUtils.isBlank(sOrderSum)) {

			MDataMap map = DbUp
					.upTable("oc_orderinfo")
					.oneWhere(
							"count(1) as c ",
							"",
							"buyer_code=:buyer_code and order_status!='4497153900010006'",
							"buyer_code", sMemberCode);
			sOrderSum = map.get("c");
			XmasKv.upFactory(EKvSchema.Member).hset(sMemberCode,
					ACTIVE_ORDER_SUM, sOrderSum);
			XmasKv.upFactory(EKvSchema.Member).expire(sMemberCode, 3600*24);

		}

		int iSum = Integer.valueOf(sOrderSum);
		if (iSum < 0) {
			iSum = 0;
			XmasKv.upFactory(EKvSchema.Member).hset(sMemberCode,
					ACTIVE_ORDER_SUM, String.valueOf(iSum));
			XmasKv.upFactory(EKvSchema.Member).expire(sMemberCode, 3600*24);
		}

		return iSum;
	}
	
	/**
	 * 用于下单后判断是否是该用户的首单
	 * @param sMemberCode
	 * @author zht
	 * @return
	 */
	public int upMemberOrderSumAfterBooking(String sMemberCode) {
		String sql = "select count(*) as num from oc_orderinfo where buyer_code=:buyer_code and "
				+ " order_status!='4497153900010006'"
				+ " group by big_order_code";
		List<Map<String, Object>> list = DbUp.upTable("oc_orderinfo").dataSqlList(sql, new MDataMap("buyer_code", sMemberCode));
		int bigOrderCount = 0;
		if(null != list) {
			bigOrderCount = list.size();
		}

		return bigOrderCount;
	}

	public long onChangeOrder(String sMemberCode, int iAdd) {
		// upMemberOrderSum(sMemberCode);
		return XmasKv.upFactory(EKvSchema.Member).hdel(sMemberCode, ACTIVE_ORDER_SUM);
	}
}
