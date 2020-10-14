package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 创建订单
 * @author jlin
 *
 */
public class TeslaMakeCreate  extends TeslaTopOrderMake {
	
	PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult xResult = new TeslaXResult();
		
		// 兼容CPS渠道的数据，如果存在则覆盖原订单中的版本号数据
		if(StringUtils.isNotBlank(teslaOrder.getCpsCode())){
			//teslaOrder.getUorderInfo().setAppVersion(teslaOrder.getCpsCode()); // 保留原始的版本号，便于以后排查
			List<TeslaModelOrderInfo> orderList = teslaOrder.getSorderInfo();
			for(TeslaModelOrderInfo orderInfo : orderList){
				orderInfo.setAppVersion(teslaOrder.getCpsCode());
			}
		}
		
		updateOrderChannel(teslaOrder);
		
		XmasKv.upFactory(EKvSchema.CreateOrder).setex(teslaOrder.getUorderInfo().getBigOrderCode(), 3600*24*30, GsonHelper.toJson(teslaOrder));
		
		return xResult;
	}
	
	private void updateOrderChannel(TeslaXOrder teslaOrder) {
		// 忽略非扫码购来源
		if(!"449715190007".equals(teslaOrder.getUorderInfo().getOrderSource())
				&& !"449715190033".equals(teslaOrder.getUorderInfo().getOrderSource())) {
			return;
		}
		
		if(!checkFlag()) {
			return;
		}
		
		// 扫码购订单
		MDataMap smgOrderMap = new MDataMap(
				"big_order_code", teslaOrder.getUorderInfo().getBigOrderCode(),
				"order_source", teslaOrder.getUorderInfo().getOrderSource(),
				"create_time", FormatHelper.upDateTime()
			);
		
		// 检查配置参数是否满足
		// 变更订单来源
		boolean updateFlag = checkUpdate(teslaOrder);
		if(updateFlag) {
			teslaOrder.getUorderInfo().setOrderSource("449715190025");
			for(TeslaModelOrderInfo info : teslaOrder.getSorderInfo()) {
				info.setOrderChannel("449747430023");
				info.setOrderSource("449715190025");
				info.setOrderType("449715200005");
				
				if(info.getAppVersion().contains("smg")) {
					info.setAppVersion("1.0");
				}
			}
			smgOrderMap.put("change_flag", "1");
		} 
		
		// 记录扫码购订单
		DbUp.upTable("oc_order_change_source").dataInsert(smgOrderMap);
		
		// 记录变更的订单zid
		if(updateFlag) {
			MDataMap map = DbUp.upTable("oc_order_change_source").onePriLib("big_order_code", teslaOrder.getUorderInfo().getBigOrderCode());
			MDataMap defMap = new MDataMap();
			defMap.put("define_dids", "4699233300080003");
			defMap.put("define_name", map.get("zid"));
			DbUp.upTable("zw_define").dataUpdate(defMap, "define_name", "define_dids");
		}
	}
	
	private boolean checkUpdate(TeslaXOrder teslaOrder) {
		// 自上次变更后累计下单数
		int orderNum = getConfigOrderNum();
		int lastZid = getLastZid();
		if(lastZid <= 0 || orderNum < 1) {
			return false;
		}
		int n = DbUp.upTable("oc_order_change_source").dataCount("zid > :lastZid", new MDataMap("lastZid", lastZid + ""));
		// 判断是否满足配置的间隔订单数
		if(n < orderNum) {
			return false;
		}
		
		// 判断收货地址： 北京市、贵州省
		if(teslaOrder.getAddress().getAreaCode().startsWith("11")
				|| teslaOrder.getAddress().getAreaCode().startsWith("52")) {
			return false;
		}
		
		// 判断内购账户
		PlusModelMemberLevel memLvl = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(teslaOrder.getUorderInfo().getBuyerCode()));
		if("70".equals(memLvl.getLevel())) {
			return false;
		}
		
		// 检查优惠券是否小程序可用
		if(!checkCoupon(teslaOrder.getUse().getCoupon_codes())) {
			return false;
		}
		
		// 检查是否存在特价活动，此处会更改活动信息必须放到最后
		if(!checkEventInfo(teslaOrder)) {
			return false;
		}
		
		return true;
	}
	
	// 检查控制开关
	private boolean checkFlag() {
		return DbUp.upTable("zw_define").count("define_dids", "4699233300080001", "define_name", "1") > 0;
	}
	
	// 系统配置的订单间隔数量
	private int getConfigOrderNum() {
		String v = (String)DbUp.upTable("zw_define").dataGet("define_name", "", new MDataMap("define_dids", "4699233300080002"));
		return NumberUtils.toInt(v);
	}
	
	// 获取上次变更的zid
	private int getLastZid() {
		String v = (String)DbUp.upTable("zw_define").dataGet("define_name", "", new MDataMap("define_dids", "4699233300080003"));
		return NumberUtils.toInt(v);
	}
	
	// 检查使用的优惠券是否包含小程序渠道
	private boolean checkCoupon(List<String> couponCodeList) {
		if(couponCodeList != null && !couponCodeList.isEmpty()) {
			LoadCouponType loadCouponType = new LoadCouponType();
			PlusModelCouponType couponType;
			for(String couponCode : couponCodeList) {
				if(StringUtils.isNotBlank(couponCode)) {
					String couponTypeCode = (String)DbUp.upTable("oc_coupon_info").dataGet("coupon_type_code", "", new MDataMap("coupon_code", couponCode));
					if(StringUtils.isNotBlank(couponTypeCode)) {
						couponType = loadCouponType.upInfoByCode(new PlusModelQuery(couponTypeCode));
						// 如果是指定渠道限制但是不包含小程序，则不修改来源
						if("4497471600070002".equals(couponType.getCouponTypeLimit().getChannelLimit())
								&& !couponType.getCouponTypeLimit().getChannelCodes().contains("449747430023")) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// 更新活动信息
	private boolean checkEventInfo(TeslaXOrder teslaOrder) {
		List<TeslaModelOrderDetail> odList = teslaOrder.getOrderDetails();
		String skuCode = null;
		for(TeslaModelOrderDetail detail : odList) {
			if(!"1".equals(detail.getGiftFlag())) {
				continue;
			}
			
			// 多商品的情况做异常处理不变更
			if(skuCode != null) {
				return false;
			}
			
			skuCode = detail.getSkuCode();
		}
		
		List<TeslaModelOrderActivity> oaList = teslaOrder.getActivityList();
		TeslaModelOrderActivity act = null;
		for(TeslaModelOrderActivity oa : oaList) {
			if(!oa.getSkuCode().equals(skuCode)) {
				continue;
			}
			
			if("4497472600010004".equals(oa.getActivityType())) {
				act = oa;
				break;
			}
		}
		
		// 没有扫码购活动则不修改
		if(act == null) {
			return true;
		}
		
		// 检查是否能参与特价活动
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(skuCode);
		pq.setChannelId("449747430023");
		PlusModelSkuResult skuRes = plusSupportProduct.upSkuInfo(pq);
		if(skuRes.getSkus().isEmpty()) {
			return false;
		}
		
		PlusModelSkuInfo skuInfo = skuRes.getSkus().get(0);
		if(StringUtils.isBlank(skuInfo.getEventCode())
				|| !"4497472600010002".equals(skuInfo.getEventType())) {
			// 未查询到特价活动时不变更
			return false;
		}
		
		// 判断特价活动跟扫码购活动是否一致
		BigDecimal money1 = skuInfo.getSkuPrice().subtract(skuInfo.getSellPrice());
		BigDecimal money2 = act.getPreferentialMoney();
		
		// 如果优惠金额不一致则不变更
		if(money1.compareTo(money2) != 0) {
			return false;
		}
		
		act.setActivityCode(skuInfo.getEventCode());
		act.setActivityType(skuInfo.getEventType());
		
		return true;
	}
}
