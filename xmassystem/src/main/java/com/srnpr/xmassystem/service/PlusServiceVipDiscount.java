package com.srnpr.xmassystem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.load.LoadEventVipDiscount;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.load.LoadPcTvGoods;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventVipDiscount;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoods;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoodsQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelevent.PlusModelVipDiscount;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;


/**
 * LD会员日活动相关
 * @author fq
 *
 */
public class PlusServiceVipDiscount  extends PlusServiceTop implements IPlusServiceProduct {

	
	
	/**
	 * 根据sku编号获取会员日活动
	 * @param sellerCode
	 * @param memberCode
	 * @param productCodes  115178,116178    多个编号用","  
	 * @return
	 */
	public Map<String, PlusModelVipDiscount> getVipDiscountActivity(String sellerCode,String memberCode,String productCodes,String channelId) {
		
		 Map<String, PlusModelVipDiscount> skuEventMap = new HashMap<String, PlusModelVipDiscount>();
		
		if(StringUtils.isNotBlank(productCodes)) {
			productCodes =  "'"+ productCodes +"'";
			productCodes = productCodes.replaceAll(",", "','");
			
			String sSql = " SELECT vipday_flag,product_code FROM productcenter.pc_productinfo WHERE product_code IN ("+productCodes+") AND small_seller_code = 'SI2003'";
			List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productinfo").dataSqlList(sSql, new MDataMap());
			
			PlusModelSaleQuery discountQuery = new PlusModelSaleQuery();
			discountQuery.setCode("SI2003");
			PlusModelEventVipDiscount discountInfo = new LoadEventVipDiscount().upInfoByCode(discountQuery );
			List<PlusModelVipDiscount> discountList = discountInfo.getListVipDiscount();
			
			String today = FormatHelper.upDateTime("yyyy-MM-dd");
			PlusModelPcTvGoodsQuery plusModelPcTvGoodsQuery = new PlusModelPcTvGoodsQuery();
			plusModelPcTvGoodsQuery.setCode(today);
			PlusModelPcTvGoods goods = new LoadPcTvGoods().upInfoByCode(plusModelPcTvGoodsQuery);
			
			String sysTime = DateUtil.getSysDateTimeString();
			String level = null; // 默认级别
			for (int i = 0; i < dataSqlList.size(); i++) {
				Map<String, Object> map = dataSqlList.get(i);
				String productCode = String.valueOf(map.get("product_code"));
				String vipday_flag = String.valueOf(map.get("vipday_flag"));
				/*
				 * 查看商品是否参与会员日
				 */
				if( "Y".equals(vipday_flag) ){
					//如果存在则不参与会员日折扣活动
					
					for (PlusModelVipDiscount plusModelVipDiscount : discountList) {//遍历查看等级匹配的折扣信息
						if(PlusSupportEvent.compareDate(sysTime,plusModelVipDiscount.getEndTime())<=0 && PlusSupportEvent.compareDate(plusModelVipDiscount.getBeginTime(),sysTime)<=0) {
							// 增加渠道判断逻辑
							if(StringUtils.isBlank(plusModelVipDiscount.getChannels()) || plusModelVipDiscount.getChannels().contains(channelId)) {
								// 如果有用户编号则查询一下用户等级
								if(level == null){
									if(StringUtils.isNotBlank(memberCode)){
										level = checkIsVipDiscount(memberCode);
									}else{
										level = XmasSystemConst.CUST_LVL_CD;
									}
								}
								
								if(plusModelVipDiscount.getVipLevel().equals(level)) {
									List<String> days = discountInfo.getExcludeDayMap().get(plusModelVipDiscount.getEventCode());
									// 当天在会员日排除日期内且当前商品在当天的节目单中，则此商品不参与会员日活动
									if(days != null && days.contains(today)){
										if(goods.getGoods().contains(productCode)){
											continue;
										}
									}
									
									//会员日排除商品判断
									Integer discountOutProduct = DbUp.upTable("sc_event_vipdiscount_exclude_product").count("product_code",productCode,"flag_enable","1","event_code",plusModelVipDiscount.getEventCode());
									if(discountOutProduct != 0){
										continue;
									}
									
									skuEventMap.put(productCode, plusModelVipDiscount);
										
									break;
								}
							}
						}
					}
				}
				
				
			}
			
		}
		
		return skuEventMap;
	}
	
	/**
	 * 监测会员是否是家有会员
	 * @param userCode
	 * @return  返回用户信息
	 */
	public String checkIsVipDiscount(String userCode) {
		if (StringUtils.isEmpty(userCode)) {
			return null;
		}
		//mc_extend_info_homehas该表里存的会员都是LD会员
		//MDataMap one = DbUp.upTable("mc_extend_info_homehas").one("member_code", userCode);
		//return one;
		PlusModelMemberLevel level = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(userCode));
		return level.getLevel();
	}
	
	public void refreshSkuInfo(PlusModelSkuInfo plusSku, PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {
		
		// 商品相关的通用检查和设置
		checkForProduct(plusSku, plusQuery, plusEvent, null);
		
	}

}
