package com.srnpr.xmasorder.make;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 用于校验拼团相关信息
 */
public class TeslaMakeCollageValidate extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		if("1".equals(teslaOrder.getCollageFlag())) {//拼团订单
			String memberCode = teslaOrder.getUorderInfo().getBuyerCode();//当前用户memberCode
			String collageCode = teslaOrder.getCollageCode();//参团编码
			MDataMap itemMap = DbUp.upTable("sc_event_collage_item").one("collage_code", collageCode, "collage_member", memberCode);
			if(itemMap != null && !itemMap.isEmpty()) {
				xResult.setResultCode(963909021);
				xResult.setResultMessage(bInfo(963909021));
				return xResult;
			}
			MDataMap collageMap = DbUp.upTable("sc_event_collage").one("collage_code", collageCode);
			String eventCode = collageMap != null&&!collageMap.isEmpty()?collageMap.get("event_code"):"";
			if(StringUtils.isNotEmpty(eventCode)) {
				MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code",eventCode);
				if(eventInfo ==null ||eventInfo.isEmpty() ) {//活动为空
					xResult.setResultCode(0);
					xResult.setResultMessage("数据异常，未查询到对应的拼团活动");
				}else {
					String collageType = eventInfo.get("collage_type");
					boolean flag = this.checkUser(memberCode,collageType,collageCode,teslaOrder);
					if(!flag) {//返回错误信息
						xResult.setResultCode(0);
						xResult.setResultMessage("不满足邀新团活动要求");
					}
				}
			}
		}
		
		return xResult;
	}
	
	/**
	 * 
	 * @param userCode
	 * @param result
	 * 2020年5月20日
	 * Angel Joy
	 * void
	 */
	private boolean checkUser(String userCode,String collageType,String collageCode,TeslaXOrder teslaOrder) {
		if(StringUtils.isEmpty(collageCode)) {
			return true;
		}
		if("4497473400050001".equals(collageType)) {//普通团允许
			return true;
		}
		//校验用户是否满足资格
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("member_code",userCode,"manage_code","SI2003");
		if(loginInfo == null) {
			return false;
		}
		String createTime = loginInfo.get("create_time");
		String expireTime = DateUtil.addDateHour(createTime, 24);
		if(DateUtil.compareDate1(DateUtil.getSysDateTimeString(),expireTime)) {
			return false;
		}
		//校验有没有下过单
		if(DbUp.upTable("oc_orderinfo").count("buyer_code",userCode)>0) {
			return false;
		}
		List<TeslaModelOrderDetail> goods = teslaOrder.getOrderDetails();
		for(TeslaModelOrderDetail good : goods) {
			String giftFlag = good.getGiftFlag();
			if("0".equals(giftFlag)) {//赠品不校验数量
				continue;
			}
			Integer skuNum = good.getSkuNum();
			if(skuNum > 1) {//写死限购一.邀新团只能购买一件商品。
				return false;
			}
		}
		return true;
	}
}
