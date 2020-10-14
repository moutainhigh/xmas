package com.srnpr.xmassystem.support;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadEventItemProduct;
import com.srnpr.xmassystem.load.LoadEventVipDiscount;
import com.srnpr.xmassystem.load.LoadPcTvGoods;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.load.LoadSkuItem;
import com.srnpr.xmassystem.load.LoadSkuPriceChange;
import com.srnpr.xmassystem.load.LoadSubItem;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventVipDiscount;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoods;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoodsQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceChange;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceChangeQuery;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceFlow;
import com.srnpr.xmassystem.modelevent.PlusModelVipDiscount;
import com.srnpr.xmassystem.modelorder.ActivityInfoDetail;
import com.srnpr.xmassystem.modelproduct.PlusModelAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelComment;
import com.srnpr.xmassystem.modelproduct.PlusModelCommentList;
import com.srnpr.xmassystem.modelproduct.PlusModelCommonProblem;
import com.srnpr.xmassystem.modelproduct.PlusModelCommonProblems;
import com.srnpr.xmassystem.modelproduct.PlusModelGiftSkuinfo;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelMediMclassGift;
import com.srnpr.xmassystem.modelproduct.PlusModelOrderAddress;
import com.srnpr.xmassystem.modelproduct.PlusModelPcProductdescription;
import com.srnpr.xmassystem.modelproduct.PlusModelCouponTypeInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSales;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuPropertyValueInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.xmassystem.service.PlusServicePurchase;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.SerializeSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;

public class PlusSupportProduct extends BaseClass {

//	private final String KJT = "SF03KJT";
//	private final String MLG = "SF03MLG";
//	private final String QQT = "SF03100294";//全球淘
	/**
	 * 宁波保税区三叶草进出口有限公司
	 */
	public final String SYC = "SF03100327";
	/**
	 * 上海楚玥国际贸易有限公司
	 */
	public final String CYGJ = "SF03100329";
	private final String CDOG = "SI3003";
	private final String CFAMILY = "SI2003";
	/**
	 * 获取SKU信息
	 * 
	 * @param plusModelQuery
	 * @return
	 */
	public PlusModelSkuResult upSkuInfo(PlusModelSkuQuery plusModelQuery) {

		PlusModelSkuResult result = new PlusModelSkuResult();

		if (result.upFlagTrue()) {

			for (String sCode : plusModelQuery.getCode().split(WebConst.CONST_SPLIT_COMMA)) {

				plusModelQuery.setCode(sCode);
				PlusModelSkuInfo plusModelSkuInfo = null;
				if (PlusHelperEvent.checkEventItem(sCode)) {

					String sSubEvent = PlusHelperEvent.upSubEvent(sCode);

					if (StringUtils.isBlank(sSubEvent)) {
						plusModelSkuInfo = new LoadSkuItem().upInfoByCode(plusModelQuery);
						
						// 设置商品的动态售价
						PlusModelSkuPriceFlow skuPriceFlow = getSkuPriceChange(plusModelSkuInfo.getSkuCode(), new Date());
						if(skuPriceFlow != null) {
							plusModelSkuInfo.setSkuPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setSellPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setCostPrice(skuPriceFlow.getCostPrice());
						}
						
						//在活动发布期商品下架 传递IC编号提示不可购买
						PlusModelSkuQuery skuquery = new PlusModelSkuQuery();
						skuquery.setCode(plusModelSkuInfo.getSkuCode());
						PlusModelSkuInfo plusModelSkuInfoNew = new LoadSkuInfo().upInfoByCode(skuquery);
						if(plusModelSkuInfoNew.getBuyStatus()==6){
							plusModelSkuInfo.setBuyStatus(6);
							plusModelSkuInfo.setButtonText(bInfo(964305106));
						}
						
					} else {
						// 扫码购等特殊来源渠道价格
						plusModelQuery.setSourceCode(sSubEvent);

						plusModelSkuInfo = new LoadSubItem().upInfoByCode(plusModelQuery);
						
						// 设置商品的动态售价
						PlusModelSkuPriceFlow skuPriceFlow = getSkuPriceChange(plusModelSkuInfo.getSkuCode(), new Date());
						if(skuPriceFlow != null) {
							plusModelSkuInfo.setSkuPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setSellPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setCostPrice(skuPriceFlow.getCostPrice());
						}
						
						if (StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())) {
							//扫码购例外商品判断
							String sql = "select count(*) cnt from sc_event_saomagou_exclude_product a where a.event_code = '"+plusModelSkuInfo.getEventCode()+"' and a.flag_enable = '1' and a.product_code = "+plusModelSkuInfo.getProductCode()+" and NOW() BETWEEN a.start_time and a.end_time";
							Map<String, Object> map = DbUp.upTable("sc_event_vipdiscount_exclude_product").dataSqlOne(sql, null);
							if(map != null && Integer.parseInt(map.get("cnt").toString())>0){
								plusModelSkuInfo.setEventCode("");
							}
						}
						
						/** 扫码购转会员日活动相关 */
						String sysTime = DateUtil.getSysDateTimeString();
						boolean hasVipDiscount = false;
						
						//查看当前是否有会员日活动
						PlusModelSaleQuery discountQuery = new PlusModelSaleQuery();
						discountQuery.setCode("SI2003");
						PlusModelEventVipDiscount discountInfo = new LoadEventVipDiscount().upInfoByCode(discountQuery);
						PlusModelVipDiscount vipDiscount = null;
						for (PlusModelVipDiscount plusModelVipDiscount : discountInfo.getListVipDiscount()) {
							if(PlusSupportEvent.compareDate(sysTime,plusModelVipDiscount.getEndTime())<=0 
									&& PlusSupportEvent.compareDate(plusModelVipDiscount.getBeginTime(),sysTime)<=0
									// 判断渠道
									&& (StringUtils.isBlank(plusModelVipDiscount.getChannels()) || plusModelVipDiscount.getChannels().contains(plusModelQuery.getChannelId()))) {
								vipDiscount = plusModelVipDiscount;
								hasVipDiscount = true;
								break;
							}
						}
						
						// 如果当前有会员日活动再判断当前商品支不支持会员日
						if(hasVipDiscount){
							// 商品是否可以参与会员日活动
							String vipdayFlag = (String)DbUp.upTable("pc_productinfo").dataGet("vipday_flag", "", new MDataMap("product_code", plusModelSkuInfo.getProductCode()));
							hasVipDiscount = "Y".equalsIgnoreCase(vipdayFlag);
						}
						//如果当前商品支持会员日活动，在判断该商品是不是经过人工导入，需要排除的商品。NG-Start
						if(hasVipDiscount){
							Integer discountOutProduct = DbUp.upTable("sc_event_vipdiscount_exclude_product").count("product_code",plusModelSkuInfo.getProductCode(),"flag_enable","1","event_code",vipDiscount.getEventCode());
							//如果存在则不参与会员日折扣活动
							if(discountOutProduct > 0){
								hasVipDiscount = false;
							}
						}
						//NG-end
						
						// 再判断是否需要排除当天节目商品
						if(hasVipDiscount && vipDiscount != null){
							List<String> days = discountInfo.getExcludeDayMap().get(vipDiscount.getEventCode());
							
							String today = FormatHelper.upDateTime("yyyy-MM-dd");
							
							// 当天在会员日排除日期内且当前商品在当天的节目单中，则此商品不参与会员日活动
							if(days != null && days.contains(today)){
								PlusModelPcTvGoodsQuery plusModelPcTvGoodsQuery = new PlusModelPcTvGoodsQuery();
								plusModelPcTvGoodsQuery.setCode(today);
								PlusModelPcTvGoods goods = new LoadPcTvGoods().upInfoByCode(plusModelPcTvGoodsQuery);
								if(goods.getGoods().contains(plusModelSkuInfo.getProductCode())){
									hasVipDiscount = false;
								}
							}
						}

						// 如果不是会员日则判断是否是内购
						if(!hasVipDiscount && plusModelQuery.getIsPurchase() != null 
								&& plusModelQuery.getIsPurchase() == 1
								&& "SI2003".equalsIgnoreCase(plusModelSkuInfo.getSmallSellerCode())){
							String price = new PlusServicePurchase().getPurchase("SI2003", plusModelQuery.getMemberCode(), plusModelSkuInfo.getProductCode(),plusModelQuery.getChannelId());
							if(StringUtils.isNotBlank(price)){
								hasVipDiscount = true;
							}
						}
						
						// 如果有会员日活动且商品可以参与会员日则设置为会员日活动
						// 支持内购活动时也需要获取内购价格
						if(hasVipDiscount){
							PlusModelSkuQuery query = new PlusModelSkuQuery();
							query.setCode(plusModelSkuInfo.getSkuCode());
							// 查询SKU信息
							PlusModelSkuInfo plusModelSkuInfoVip = new LoadSkuInfo().upInfoByCode(query);
							// 以计算出的当前价格为基础计算会员日价格
							plusModelSkuInfoVip.setSellPrice(plusModelSkuInfo.getSellPrice());
							plusModelSkuInfoVip.setSkuPrice(plusModelSkuInfo.getSkuPrice());
							
							// 计算内购和会员日价格
							plusModelSkuInfoVip = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfoVip, plusModelQuery);
							
							// 使用计算后的对象，此对象中包含内购价格
							plusModelSkuInfo = plusModelSkuInfoVip;
						}
						/** 扫码购转会员日活动相关逻辑 */
					}
					
					if (StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())) {
						// 最大可售数量
						plusModelSkuInfo.setMaxBuy(Math.min(plusModelSkuInfo.getMaxBuy(),new PlusSupportStock().upSkuStockBySkuCode(plusModelSkuInfo.getSkuCode())));
						plusModelSkuInfo = new PlusSupportEvent().refreshSkuItem(plusModelSkuInfo, plusModelQuery);
						plusModelSkuInfo.setItemCode(sCode);//兼容前端获取此编号进行传递下单
					}
					
					//如果活动编号为空，则进行原价购买
					if (StringUtils.isBlank(plusModelSkuInfo.getEventCode())) {
						// 如果没有活动 则将库存数据设置为可售库存
						plusModelSkuInfo.setMaxBuy(Math.min(plusModelSkuInfo.getMaxBuy(),new PlusSupportStock().upSkuStockBySkuCode(plusModelSkuInfo.getSkuCode())));
						plusModelSkuInfo.setLimitStock(plusModelSkuInfo.getMaxBuy());
						plusModelSkuInfo = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfo, plusModelQuery);
						
						plusModelSkuInfo.setItemCode(sCode);//兼容前端获取此编号进行传递下单
					}

				} else {
					plusModelSkuInfo = new LoadSkuInfo().upInfoByCode(plusModelQuery);

					// 设置商品的动态售价和成本价，多彩宝渠道订单除外
					if(!"449715190014".equals(plusModelQuery.getOrderSource())) {
						PlusModelSkuPriceFlow skuPriceFlow = getSkuPriceChange(plusModelSkuInfo.getSkuCode(), new Date());
						if(skuPriceFlow != null) {
							plusModelSkuInfo.setSkuPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setSellPrice(skuPriceFlow.getSellPrice());
							plusModelSkuInfo.setCostPrice(skuPriceFlow.getCostPrice());
						}
					}
					
					//多彩下单时 清空商品活动信息
					//判断是否为原价购买，如果为原价购买，则置空活动信息
					//兑换码兑换清空商品活动信息
					if((null != plusModelQuery.getOrderSource() && "449715190014".equals(plusModelQuery.getOrderSource())) 
							|| "1".equals(plusModelQuery.getIsOriginal()) || plusModelQuery.isRedeem()||"1".equals(plusModelQuery.getFxFlag())){
						plusModelSkuInfo.setEventCode("");
						plusModelSkuInfo.setEventType("");
						plusModelSkuInfo.setItemCode("");
					}
					
					// 最大可售库存不能超过SKU总库存
					plusModelSkuInfo.setMaxBuy(Math.min(plusModelSkuInfo.getMaxBuy(),new PlusSupportStock().upSkuStockBySkuCode(plusModelSkuInfo.getSkuCode())));
					
					// 橙意卡商品不走促销活动
					// 兑换商品不走促销活动
					// 分销商品不走促销活动
					if(plusModelSkuInfo.getProductCode().equals(bConfig("xmassystem.plus_product_code"))
							|| plusModelQuery.isRedeem()||"1".equals(plusModelQuery.getFxFlag())|| "1".equals(plusModelQuery.getIsOriginal())) {
						plusModelSkuInfo.setEventCode("");
						plusModelSkuInfo.setEventType("");
						plusModelSkuInfo.setItemCode("");
					} else {
							plusModelSkuInfo = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfo, plusModelQuery);
					}
					
					/*
					 * 代码注释
					 * PlusModelSkuInfo plusModelSkuInfonew = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfo, plusModelQuery);
					
					//zgh  如果商品的sku上没有关联活动信息  先过滤一下内购
//					if(StringUtils.isBlank(plusModelSkuInfo.getEventCode()) && StringUtils.isBlank(plusModelSkuInfo.getEventType())){
//					}
						
					if(plusModelSkuInfonew.getEventType().equals("4497472600010006")){
						plusModelSkuInfo = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfo, plusModelQuery);
					}else{
						// 如果关联有活动编号
						if (StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())) {
							
							plusModelSkuInfo = new PlusSupportEvent().refreshSkuEvent(plusModelSkuInfo, plusModelQuery);
						}
					}*/
					
					
					//edit ligj  再判断一次是否有关联活动编号
					if (StringUtils.isBlank(plusModelSkuInfo.getEventCode())) {
						// 如果没有活动 则将库存数据设置为可售库存
						plusModelSkuInfo.setMaxBuy(new PlusSupportStock().upSkuStockBySkuCode(plusModelSkuInfo.getSkuCode()));
						plusModelSkuInfo.setLimitStock(plusModelSkuInfo.getMaxBuy());
					}
				}
				
				// maxbuy为0且buystatus为1时，设置buystatus为4
				if (plusModelSkuInfo.getBuyStatus() == 1 && plusModelSkuInfo.getMaxBuy() == 0) {
					plusModelSkuInfo.setBuyStatus(4);
					plusModelSkuInfo.setButtonText(bInfo(964305105));
				}

				// 0价格商品强制校验
				if (plusModelSkuInfo.getSellPrice().compareTo(BigDecimal.ZERO) <= 0) {
					plusModelSkuInfo.setSellPrice(BigDecimal.ZERO);
					
					// 不允许非活动的情况下商品价格设置为0元  2018-08-05
					if(StringUtils.isBlank(plusModelSkuInfo.getEventCode())){
						plusModelSkuInfo.setSellPrice(plusModelSkuInfo.getSourcePrice());
					}
				}
				// 如果有活动编号 则返回活动类型编号
				plusModelSkuInfo.setEventType("");
				if (StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())) {

					PlusModelEventInfo plusModelEventInfo = new PlusSupportEvent()
							.upEventInfoByCode(plusModelSkuInfo.getEventCode());

					plusModelSkuInfo.setEventType(plusModelEventInfo.getEventType());
					//add ligj 满减，
//					if (!"4497472600010008".equals(plusModelEventInfo.getEventType()) 
//							&& !"4497472600010004".equals(plusModelEventInfo.getEventType())
//							&& !"4497472600010007".equals(plusModelEventInfo.getEventType())
//							&& !"4497472600010009".equals(plusModelEventInfo.getEventType())
//							&& !"4497472600010011".equals(plusModelEventInfo.getEventType())) {
//						plusModelSkuInfo.setShowLimitNum(1);  
//					}
					
					// 秒杀、特价、闪购、会员日闪购、打折促销显示限购
					if("4497472600010001".equals(plusModelEventInfo.getEventType())
							|| "4497472600010002".equals(plusModelEventInfo.getEventType())
							|| "4497472600010005".equals(plusModelEventInfo.getEventType())
							|| "4497472600010020".equals(plusModelEventInfo.getEventType())
							|| "4497472600010030".equals(plusModelEventInfo.getEventType())){
						plusModelSkuInfo.setShowLimitNum(1);  
					}
				}
				
				//添加sku非活动的库存
				PlusSupportStock st = new PlusSupportStock();
				int remainStock = st.upAllStock(plusModelSkuInfo.getSkuCode());
				plusModelSkuInfo.setLimitSellStock(remainStock);;
				if("SI2003".equals(plusModelSkuInfo.getSmallSellerCode()) || StringUtils.isNotBlank(plusModelSkuInfo.getEventType())) {
					
					if(plusModelSkuInfo.getLimitBuy() > 5) {
						plusModelSkuInfo.setLimitBuy(5);
					}
				
				}
				result.getSkus().add(plusModelSkuInfo);
			}

		}

		return result;
	}

	/*
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode) {

		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(sSkuCode);

		return upSkuInfo(pq).getSkus().get(0);

	}

	/*
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode) {

		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(sSkuCode);
		pq.setMemberCode(sMemberCode);

		return upSkuInfo(pq).getSkus().get(0);

	}
	
	
	/*
	 * zhouguohui 20160407重载内购方法
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase) {

		//防止membercode和memberCodeNew都为空空指针
		if(sMemberCode==null&&memberCodeNew==null) {
			memberCodeNew = "";
		}
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(sSkuCode);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	
	/*
	 * zhouguohui 20160407重载内购方法
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, boolean isOriginal, boolean isSupportCollage) {

		//防止membercode和memberCodeNew都为空空指针
		if(sMemberCode==null&&memberCodeNew==null) {
			memberCodeNew = "";
		}
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, boolean isOriginal, boolean isSupportCollage,String ifJJGFlag) {

		//防止membercode和memberCodeNew都为空空指针
		if(sMemberCode==null&&memberCodeNew==null) {
			memberCodeNew = "";
		}
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setIfJJGFlag(ifJJGFlag);
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, boolean isOriginal, boolean isSupportCollage, boolean isRedeem) {
		
		//防止membercode和memberCodeNew都为空空指针
		if(sMemberCode==null&&memberCodeNew==null) {
			memberCodeNew = "";
		}
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		pq.setRedeem(isRedeem);
		return upSkuInfo(pq).getSkus().get(0);
		
	}
	
	/*
	 * renhongbin 20180126用于多彩
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, String orderSource) {

		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(sSkuCode);
		pq.setOrderSource(orderSource);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	
	/*
	 * renhongbin 20180126用于多彩
	 * 根据SKU编号返回SKU的信息
	 */
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, String orderSource, boolean isOriginal, boolean isSupportCollage) {

		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		pq.setOrderSource(orderSource);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, String orderSource, boolean isOriginal, boolean isSupportCollage,String ifJJgFlag) {

		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		pq.setOrderSource(orderSource);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		return upSkuInfo(pq).getSkus().get(0);

	}
	
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, String orderSource, boolean isOriginal, boolean isSupportCollage, boolean isRedeem) {
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		pq.setOrderSource(orderSource);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		pq.setRedeem(isRedeem);
		return upSkuInfo(pq).getSkus().get(0);
	}
	
	public PlusModelSkuInfo upSkuInfoBySkuCode(String sSkuCode, String sMemberCode,String memberCodeNew,Integer isPurchase, String orderSource, boolean isOriginal, boolean isSupportCollage, boolean isRedeem,String ifJJGFlag) {
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setIfJJGFlag(ifJJGFlag);
		if(isOriginal) {
			pq.setIsOriginal("1");
		}
		if(!isSupportCollage) {
			pq.setIsSupportCollage("0");
		}
		pq.setCode(sSkuCode);
		pq.setOrderSource(orderSource);
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}
		pq.setIsPurchase(isPurchase);
		pq.setRedeem(isRedeem);
		return upSkuInfo(pq).getSkus().get(0);
	}
	
	public int upSkuAllStockForInt(String sSkuCode) {
		return new PlusSupportStock().upAllStock(sSkuCode);
	}

	/**
	 * 获取某一sku的所有库存数量 这里用于获取SKU的所有库存地下的所有库存 只能传入真实SKU的编号
	 * 
	 * @param sSkuCode
	 *            SKU实际编号
	 * @return
	 */
	public Long upSkuAllStock(String sSkuCode) {

		return new PlusSupportStock().upSkuStockBySkuCode(sSkuCode);

	}

	/**
	 * 获取SKU的锁定库存
	 * 
	 * @param sStockCode
	 * @return
	 */
	public int upLockStock(String sSkuCode) {
		return new PlusSupportStock().upLockStock(sSkuCode);
	}

	/**
	 * 数据库中初始化SKU信息
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public PlusModelSkuInfo initBaseInfoFromDb(String sSkuCode) {
		PlusModelSkuInfo plusSku = new PlusModelSkuInfo();
		if (StringUtils.isBlank(sSkuCode)) {
			plusSku.setButtonText(bInfo(964305106));
			plusSku.setBuyStatus(6);
			return plusSku;
		}
		plusSku.setSkuCode(sSkuCode);

		MDataMap mSkuMap = DbUp.upTable("pc_skuinfo").one("sku_code", sSkuCode);
		MDataMap mProductMap = DbUp.upTable("pc_productinfo").one("product_code", mSkuMap.get("product_code"));

		if (mProductMap != null && mProductMap.size() > 0) {

			plusSku.setSellProductcode(mSkuMap.get("sell_productcode"));
			plusSku.setCostPrice(new BigDecimal(mSkuMap.get("cost_price")));
			plusSku.setSourcePrice(new BigDecimal(mProductMap.get("market_price")));
			plusSku.setSellPrice(new BigDecimal(mSkuMap.get("sell_price")));
			plusSku.setSkuPrice(new BigDecimal(mSkuMap.get("sell_price")));
			plusSku.setSourceNote(bInfo(964305102));
			plusSku.setProductCode(mSkuMap.get("product_code"));
			plusSku.setButtonText(bInfo(964305101));
			plusSku.setBuyStatus(1);
			plusSku.setSkuKey(mSkuMap.get("sku_key"));
			plusSku.setSkuKeyvalue(mSkuMap.get("sku_keyvalue"));
			plusSku.setValidateFlag(mProductMap.get("validate_flag"));
			plusSku.setSkuName(mSkuMap.get("sku_name"));
			plusSku.setSkuPicUrl(mSkuMap.get("sku_picurl"));
			plusSku.setProductPicUrl(mProductMap.get("mainpic_url"));
			plusSku.setSmallSellerCode(mProductMap.get("small_seller_code"));
			plusSku.setOnlinepayFlag(mProductMap.get("onlinepay_flag"));
			plusSku.setOnlinepayStart(mProductMap.get("onlinepay_start"));
			plusSku.setOnlinepayEnd(mProductMap.get("onlinepay_end"));
			if (StringUtils.isNotBlank(mSkuMap.get("mini_order"))) {
				plusSku.setMinBuy(Long.parseLong(mSkuMap.get("mini_order")));
			}
			
			//当前sku是否可售. add by zht
			plusSku.setSaleYn(mSkuMap.get("sale_yn"));
			
			// 判断商品是否可售
			if (!mProductMap.get("product_status").equals("4497153900060002")) {
				plusSku.setButtonText(bInfo(964305106));
				plusSku.setBuyStatus(6);
			}
		} else {
			plusSku.setButtonText(bInfo(964305106));
			plusSku.setBuyStatus(6);

		}

		return plusSku;
	}
	
	/**
	 * 数据库中初始化SKU信息
	 * 数据查询主库
	 * @param sSkuCode
	 * @return
	 */
	public PlusModelSkuInfo initBaseInfoFromDbMain(String sSkuCode) {
		PlusModelSkuInfo plusSku = new PlusModelSkuInfo();
		if (StringUtils.isBlank(sSkuCode)) {
			plusSku.setButtonText(bInfo(964305106));
			plusSku.setBuyStatus(6);
			return plusSku;
		}
		plusSku.setSkuCode(sSkuCode);

		MDataMap mSkuMap = DbUp.upTable("pc_skuinfo").onePriLib("sku_code", sSkuCode);
		if (mSkuMap != null && mSkuMap.get("product_code") != null) {
			MDataMap mProductMap = DbUp.upTable("pc_productinfo").onePriLib("product_code", mSkuMap.get("product_code"));
			if (mProductMap != null && mProductMap.size() > 0) {
				plusSku.setSellProductcode(mSkuMap.get("sell_productcode"));
				plusSku.setCostPrice(new BigDecimal(mSkuMap.get("cost_price")));
				plusSku.setSourcePrice(new BigDecimal(mProductMap.get("market_price")));
				plusSku.setSellPrice(new BigDecimal(mSkuMap.get("sell_price")));
				plusSku.setSkuPrice(new BigDecimal(mSkuMap.get("sell_price")));
				plusSku.setSourceNote(bInfo(964305102));
				plusSku.setProductCode(mSkuMap.get("product_code"));
				plusSku.setButtonText(bInfo(964305101));
				plusSku.setBuyStatus(1);
				plusSku.setSkuKey(mSkuMap.get("sku_key"));
				plusSku.setSkuKeyvalue(mSkuMap.get("sku_keyvalue"));
				plusSku.setValidateFlag(mProductMap.get("validate_flag"));
				plusSku.setSkuName(mSkuMap.get("sku_name"));
				plusSku.setSkuPicUrl(mSkuMap.get("sku_picurl"));
				plusSku.setProductPicUrl(mProductMap.get("mainpic_url"));
				plusSku.setSmallSellerCode(mProductMap.get("small_seller_code"));
				plusSku.setOnlinepayFlag(mProductMap.get("onlinepay_flag"));
				plusSku.setOnlinepayStart(mProductMap.get("onlinepay_start"));
				plusSku.setOnlinepayEnd(mProductMap.get("onlinepay_end"));
				if (StringUtils.isNotBlank(mSkuMap.get("mini_order"))) {
					plusSku.setMinBuy(Long.parseLong(mSkuMap.get("mini_order")));
				}
				
				//当前sku是否可售. add by zht
				plusSku.setSaleYn(mSkuMap.get("sale_yn"));
				
				// 判断商品是否可售
				if (!mProductMap.get("product_status").equals("4497153900060002")) {
					plusSku.setButtonText(bInfo(964305106));
					plusSku.setBuyStatus(6);
				}
			} else {
				plusSku.setButtonText(bInfo(964305106));
				plusSku.setBuyStatus(6);

			}
		}
		return plusSku;
	}
	
	/**
	 * 获取商品编号下面的所有SKU编号 用逗号分隔
	 * 
	 * @param sProductCode
	 * @return
	 */
	public String upProductSku(String sProductCode) {
		String sReturn = XmasKv.upFactory(EKvSchema.ProductSku).get(sProductCode);
		if (StringUtils.isBlank(sReturn)) {

			List<String> skuCodeList = new ArrayList<String>();
			//edit ligj 增加了sale_yn判断
			List<MDataMap> skuMapList = DbUp.upTable("pc_skuinfo").queryAll("sku_code", "", "",
					new MDataMap("product_code", sProductCode,"sale_yn","Y"));
			for (MDataMap mDataMap : skuMapList) {
				skuCodeList.add(mDataMap.get("sku_code"));
			}
			sReturn = StringUtils.join(skuCodeList, ",");
			XmasKv.upFactory(EKvSchema.ProductSku).set(sProductCode, sReturn);
		}
		return sReturn;
	}

	/**
	 * 数据库中初始化SKU扩展信息
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public PlusModelSkuInfoSpread initBaseInfoFromSP(String productCode) {
		PlusModelSkuInfoSpread plusSku = new PlusModelSkuInfoSpread();

		plusSku.setProductCode(productCode);

		MDataMap mSkuMap = DbUp.upTable("pc_productinfo_ext").one("product_code", productCode);
		if (mSkuMap != null) {

			plusSku.setDlrId(mSkuMap.get("dlr_id"));

			plusSku.setPrchType(mSkuMap.get("prch_type"));

			plusSku.setSiteNo(mSkuMap.get("oa_site_no"));
			
			plusSku.setProductTradeType(mSkuMap.get("product_trade_type"));
			
			plusSku.setDeliveryStoreType(mSkuMap.get("delivery_store_type"));

		}
		return plusSku;
	}

	/**
	 * 数据库中初始化赠品信息信息
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public PlusModelGitfSkuInfoList getProductGiftsDetailList(String productCode) {

		PlusModelGitfSkuInfoList giftList = new PlusModelGitfSkuInfoList();
		if (StringUtils.isBlank(productCode)) {
			return giftList;
		}
		// 获取外联赠品的输入参数
		List<MDataMap> giftsMapList = DbUp.upTable("pc_product_gifts_new").queryAll("", "",
				"seller_code='SI2003' and product_code = '" + productCode + "'", null);
		if (null != giftsMapList) {
			for (MDataMap giftMap : giftsMapList) {
				PlusModelGiftSkuinfo giftModel = new PlusModelGiftSkuinfo();
				giftModel.setGood_id(giftMap.get("gift_id"));
				giftModel.setGood_nm(giftMap.get("gift_name"));
				giftModel.setStyle_id(giftMap.get("style_id"));
				giftModel.setColor_id(giftMap.get("color_id"));
				giftModel.setEvent_id(giftMap.get("event_id"));
				giftModel.setGift_cd(giftMap.get("gift_cd"));

				giftModel.setFr_date(giftMap.get("fr_date"));
				giftModel.setEnd_date(giftMap.get("end_date"));
				
				List<MDataMap> mediMclssMapList = DbUp.upTable("pc_product_gifts_medimclass").queryAll("medi_mclass_id,medi_mclass_nm", "",
						"uid_ref='" + giftMap.get("uid") + "'", null);
				
				for (MDataMap mediMclassMap : mediMclssMapList) {
					PlusModelMediMclassGift mediMclass = new PlusModelMediMclassGift();
					mediMclass.setMEDI_MCLSS_ID(mediMclassMap.get("medi_mclass_id"));
					mediMclass.setMEDI_MCLSS_NM(mediMclassMap.get("medi_mclass_nm"));
					giftModel.getMedi_mclss_nm().add(mediMclass);
				}
				
				giftList.getGiftSkuinfos().add(giftModel);
			}
		}
		return giftList;
	}

	/**
	 * 数据库中初始化订单地址信息
	 * 
	 * @param sSkuCode
	 * @return
	 */
	public PlusModelOrderAddress initAdderssFrom(String addressCode) {

		PlusModelOrderAddress plusaAddress = new PlusModelOrderAddress();

		plusaAddress.setAddressCode(addressCode);

		MDataMap mAddressMap = DbUp.upTable("nc_address").one("address_id", addressCode);
		
		MDataMap carMap = DbUp.upTable("mc_authenticationInfo").one("address_id",addressCode);
		//缓存地址信息
		if (mAddressMap!=null&&!mAddressMap.isEmpty()) {

			plusaAddress.setAddress(mAddressMap.get("address_street"));

			plusaAddress.setAreaCode(mAddressMap.get("area_code"));

			plusaAddress.setEmail(mAddressMap.get("email"));

			plusaAddress.setMobilephone(mAddressMap.get("address_mobile"));

			plusaAddress.setPostCode(mAddressMap.get("address_postalcode"));

			plusaAddress.setReceivePerson(mAddressMap.get("address_name"));
		}
		//缓存证件信息
		if(carMap!=null&&!carMap.isEmpty()){
			
			plusaAddress.setIdcardNumber(carMap.get("idcard_number"));
			
			plusaAddress.setCardAddress(carMap.get("address"));
			
			plusaAddress.setIdcardType(carMap.get("idcard_type"));
			
			plusaAddress.setPhoneNumber(carMap.get("phone_number"));
			
			plusaAddress.setTrueName(carMap.get("true_name"));
			
			plusaAddress.setCardEmail(carMap.get("email"));
			
		}
		

		return plusaAddress;
	}

	/**
	 * 根据活动编号获取活动类型编号
	 * 
	 * @param eventCode
	 * @return
	 */
	public String upEventTypeCode(String eventCode) {

		String sReturn = "";
		if (StringUtils.isNotEmpty(eventCode)) {

			/*
			 * List<MDataMap> mSkuMapList = DbUp.upTable("sc_event_info")
			 * .queryAll( "event_type_code", "", "event_code in ('" +
			 * eventCode.replace(",", "','") + "')", null); if (null !=
			 * mSkuMapList && !mSkuMapList.isEmpty()) { return
			 * mSkuMapList.get(0).get("event_type_code"); }
			 */
			sReturn = new PlusSupportEvent().upEventInfoByCode(eventCode).getEventType();
		}
		return sReturn;
	}

	/**
	 * 获取订单自动取消时间字符串
	 * 
	 * @param icCode
	 * @return
	 */
	public String getOrderAging(List<String> icCodes) {
		String stringValue = null;
		if (icCodes != null && !icCodes.isEmpty()) {
			String str = "";
			for (int i = 0; i < icCodes.size(); i++) {
				if (i == 0) {
					str = "'" + icCodes.get(i) + "'";
				} else {
					str = str + ",'" + icCodes.get(i) + "'";
				}
			}
			String sqlString = "select order_aging,time_category from sc_event_info where event_code in (SELECT event_code FROM sc_event_item_product where item_code in ("
					+ str + "));";
			List<Map<String, Object>> list = DbUp.upTable("sc_event_info").dataSqlList(sqlString, new MDataMap());
			Map<String, Map<String, Object>> m1 = new HashMap<String, Map<String, Object>>();
			Map<String, Long> m2 = new HashMap<String, Long>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> mm = list.get(i);
				m1.put(String.valueOf(i), mm);
				if ("449747280001".equals(mm.get("time_category").toString())) {
					m2.put(String.valueOf(i), Long.valueOf(mm.get("order_aging").toString()) * 3600);
				} else if ("449747280002".equals(mm.get("time_category").toString())) {
					m2.put(String.valueOf(i), Long.valueOf(mm.get("order_aging").toString()) * 60);
				} else {
					m2.put(String.valueOf(i), Long.valueOf(mm.get("order_aging").toString()));
				}
			}
			List<Map.Entry<String, Long>> ls = mapValueSortDesc(m2);
			if (ls != null && ls.size() > 0) {
				Map.Entry<String, Long> m3 = ls.get(0);
				String num = m1.get(m3.getKey()).get("order_aging").toString();
				String cate = m1.get(m3.getKey()).get("time_category").toString();
				if ("449747280001".equals(cate)) {
					stringValue = num + "小时";
				} else if ("449747280002".equals(cate)) {
					stringValue = num + "分钟";
				} else {
					stringValue = num + "秒";
				}
			}
		}
		return stringValue;

	}

	/**
	 * 根据hashmap的value进行从小到大排序
	 * 
	 */
	public static List<Map.Entry<String, Long>> mapValueSortDesc(Map<String, Long> ms) {
		List<Map.Entry<String, Long>> list_Data = new ArrayList<Map.Entry<String, Long>>(ms.entrySet());
		Collections.sort(list_Data, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				if (o2.getValue() != null && o1.getValue() != null && o2.getValue().compareTo(o1.getValue()) < 0) {
					return 1;
				} else {
					return -1;
				}

			}
		});

		return list_Data;
	}

	/**
	 * 获取订单自动取消时间字符串 zhough 备注：只供二维码扫描使用 因为 2015-08-31这个时间的二维码设计逻辑比较的简单
	 * 449747280001小时
	 * 449747280002分钟
	 * 449747280003秒
	 * @param sellerCode
	 *            系统编号 惠家有 SI2003 家有汇 SI2009 沙皮狗 SI3003
	 * @return 449747280001&30 类型和数字
	 */
	public String getQrCodeAging(List<String> ics) {
		String stringValue = null;
		if (ics != null && !ics.isEmpty()) {
			List<MDataMap> maps = DbUp.upTable("sc_event_info").queryIn("order_aging,time_category", "", "", new MDataMap(), 0, 0, "event_code", StringUtils.join(ics,","));
			if(null == maps || maps.size() == 0) {
				return stringValue;
			}
			String time_category = maps.get(0).get("time_category");
			
			BigDecimal min = BigDecimal.valueOf(
					"449747280001".equals(time_category)?Double.valueOf(maps.get(0).get("order_aging"))*60:
						"449747280002".equals(time_category)?Double.valueOf(maps.get(0).get("order_aging")):
							Double.valueOf(maps.get(0).get("order_aging"))/60);//最小值 按分钟计算
			
			stringValue = maps.get(0).get("time_category")+"&"+maps.get(0).get("order_aging");
			for (int i = 0; i < maps.size(); i++) {
				MDataMap map = maps.get(i);
				if (map != null && map.size() > 0) {
					
					BigDecimal th = BigDecimal.valueOf(
							"449747280001".equals(map.get("time_category"))?Double.valueOf(map.get("order_aging"))*60:
								"449747280002".equals(map.get("time_category"))?Double.valueOf(map.get("order_aging")):
									Double.valueOf(map.get("order_aging"))/60);
					if (min.compareTo(th)>0) {	
						
						min = th;
						
						time_category = map.get("time_category");
						
					}
					
				}
			}
			
			
			if(StringUtils.equals("449747280001", time_category)){
				/*将分钟转换为小时*/
				min = min.divide(new BigDecimal(60));
				
			}else if(StringUtils.equals("449747280002", time_category)){
				
				
				
			}else{
				
				/*将分钟转换为秒*/
				min = min.multiply(new BigDecimal(60)); 
				
			}
			
			min = min.setScale(0, BigDecimal.ROUND_DOWN);
			
			stringValue = time_category+"&"+min;
		}
		return stringValue;

	}

	public String getOrderRemind(List<String> ics){
		String result = null;
		if (ics != null && !ics.isEmpty()) {
			String value = getQrCodeAging(ics);
			if(StringUtils.isNotEmpty(value)){
				String type = value.split("&")[0];
				String va = value.split("&")[1];
				if ("449747280001".equals(type)) {
					result = va + "小时";
				} else if ("449747280002".equals(type)) {
					result = va + "分钟";
				} else {
					result = va + "秒";
				}
			}
		}
		return result;
	}
	
	public int compareOrderRemind(List<String> ics, Long seconds) {
		int ret = 1;
		Long sec = 0L;
		if (ics != null && !ics.isEmpty()) {
			String value = getQrCodeAging(ics);
			if(StringUtils.isNotEmpty(value)){
				String type = value.split("&")[0];
				String va = value.split("&")[1];
				if ("449747280001".equals(type)) {
					sec = Long.parseLong(va) * 60 * 60;
				} else if ("449747280002".equals(type)) {
					sec = Long.parseLong(va) * 60;
				} else {
					sec = Long.parseLong(va);
				}
				if(sec >= seconds) {
					ret = 1;
				} else {
					ret = -1;
				}
			} else {
				ret = 1;
			}
		} else {
			ret = 1;
		}
		return ret;
	}
	
	/**
	 * 从数据加载商品信息
	 * 
	 * @param productCode
	 * @return
	 */
	/*public PlusModelProductInfo initProductInfoFromDb(String productCode) {

		PlusModelProductInfo plusModelProductInfo = new PlusModelProductInfo();

		plusModelProductInfo.setProductCode(productCode);

		MDataMap mProductMap = DbUp.upTable("pc_productinfo").one("product_code", productCode);
		List<MDataMap> skus = DbUp.upTable("pc_skuinfo").queryByWhere("product_code", productCode);
		List<MDataMap> li = DbUp.upTable("uc_sellercategory_product_relation").queryByWhere("product_code",productCode);
		if (mProductMap != null && mProductMap.size() > 0) {
			plusModelProductInfo.setProductCodeOld(mProductMap.get("product_code_old"));
			plusModelProductInfo.setProductName(mProductMap.get("product_name"));
			plusModelProductInfo.setProductStatus(mProductMap.get("product_status"));
			plusModelProductInfo.setProductVolume(new BigDecimal(mProductMap.get("product_volume")));
			plusModelProductInfo.setProductVolumeItem(mProductMap.get("product_volume_item"));
			plusModelProductInfo.setProductWeight(new BigDecimal(mProductMap.get("product_weight")));
			plusModelProductInfo.setTransportTemplate(mProductMap.get("transport_template"));
			plusModelProductInfo.setCost_price(new BigDecimal(mProductMap.get("cost_price")));
			plusModelProductInfo.setTax_rate(new BigDecimal(mProductMap.get("tax_rate")));
			plusModelProductInfo.setBrandCode(mProductMap.get("brand_code"));
			if(li!=null&&!li.isEmpty()){
				for (int i = 0; i < li.size(); i++) {
					if(StringUtils.isNotBlank(li.get(i).get("category_code"))){
						plusModelProductInfo.getCategorys().add(li.get(i).get("category_code"));
					}
				}
			}
			if(skus!=null&&!skus.isEmpty()){
				for (int i = 0; i < skus.size(); i++) {
					plusModelProductInfo.getSkus().put(skus.get(i).get("sku_code"), Double.valueOf(skus.get(i).get("cost_price")));
				}
			}
		}

		return plusModelProductInfo;
	}*/
	
	/**
	 * 加载可下单的第三级行政地址编号
	 * 
	 * @return
	 */
	public PlusModelAeraCode initAreaCodeInfosFromDb(String areaCode) {

		PlusModelAeraCode code = new PlusModelAeraCode();

		String sql = "select code from sc_tmp where send_yn = 'Y'";
		List<Map<String, Object>> list = DbUp.upTable("sc_tmp").dataSqlList(sql, new MDataMap());
		for(Map<String, Object> map : list) {
			code.getAreaCodes().add(MapUtils.getString(map, "code", ""));
		}
		return code;
	}
	
	/** 
	* @Description: 根据区域模板获取相关城市
	* @param templateCode 模板编号
	* @author 张海生
	* @date 2015-12-22 下午3:16:54
	* @return PlusModelTemplateAeraCode 
	* @throws 
	*/
	public PlusModelTemplateAeraCode initTemplateAreaCode(String templateCode){
		PlusModelTemplateAeraCode code = new PlusModelTemplateAeraCode();
		Set<String> cityCodes = new HashSet<String>();
		
		//查询模板编码对应的模板类型
		MDataMap templateMap = DbUp.upTable("sc_area_template").one("template_code", templateCode);
		String template_type = templateMap.get("template_type");
		if("449748370001".equals(template_type)) {//销售区域
			List<Map<String, Object>> cityList = DbUp.upTable("sc_area_template_info").dataSqlList("select city_code from sc_area_template_info where template_code = :template_code and city_code != 'all'", 
					new MDataMap("template_code", templateCode));
			List<Map<String, Object>> allList = DbUp.upTable("sc_tmp").dataSqlList("select t.code from sc_area_template_info i, sc_tmp t where i.template_code = :template_code and i.city_code = 'all' and "
					+ "i.province_code = t.p_code and t.use_yn = 'Y' and t.code_lvl = 2", new MDataMap("template_code", templateCode));
			for(Map<String, Object> city : cityList) {
				String cityCode = MapUtils.getString(city, "city_code", "");
				cityCodes.add(cityCode);
			}
			for(Map<String, Object> all : allList) {
				String cityCode = MapUtils.getString(all, "code", "");
				cityCodes.add(cityCode);
			}
		}else if("449748370002".equals(template_type)) {//限售区域
			String sql = "select t.code from sc_tmp t where t.use_yn = 'Y' and t.code_lvl = 2 and (t.code not in ("
					+ "select i1.city_code from sc_area_template_info i1 where i1.template_code = :template_code and i1.city_code != 'all'"
				+ ") and t.p_code not in ("
					+ "select i2.province_code from sc_area_template_info i2 where i2.template_code = :template_code and i2.city_code = 'all'"
				+ "))";
			List<Map<String, Object>> allCityList = DbUp.upTable("sc_tmp").dataSqlList(sql, new MDataMap("template_code", templateCode));
			for(Map<String, Object> allCity : allCityList) {
				String cityCode = MapUtils.getString(allCity, "code", "");
				cityCodes.add(cityCode);
			}
		}
		code.getAreaCodes().addAll(cityCodes);
		return code;
	}
	
	/***
	 * 获取商品信息
	 * 
	 * @param productCode
	 * @return
	 */
	public PlusModelProductInfo upProductInfo(String productCode) {
		return new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
	}

	/*
	 * 
	 * 根据商品编号返回价格 这个返回的是经过促销活动的商品的所有SKU的一个最低价
	 */
	public BigDecimal upPriceByProductCode(String sProductCode, String sMemberCode) {

		PlusModelSkuQuery inputParam = new PlusModelSkuQuery();

		inputParam.setCode(new PlusSupportProduct().upProductSku(sProductCode));

		inputParam.setMemberCode(sMemberCode);

		PlusModelSkuResult plusModelSkuResult = upSkuInfo(inputParam);

		// 判断如果数量大于0 则按照状态值从小到大重新排序
		if (plusModelSkuResult.getSkus().size() > 0) {

			Collections.sort(plusModelSkuResult.getSkus(), new Comparator<PlusModelSkuInfo>() {

				public int compare(PlusModelSkuInfo o1, PlusModelSkuInfo o2) {

					return o1.getSellPrice().compareTo(o2.getSellPrice());
				}
			});

		}

		return plusModelSkuResult.getSkus().get(0).getSellPrice();

	}
	/**
	 * 从数据加载商品信息
	 * 
	 * @param productCode
	 * @return
	 */
	public PlusModelProductInfo initProductInfoFromDb(String productCode) {

		PlusModelProductInfo plusModelProductInfo = new PlusModelProductInfo();

		plusModelProductInfo.setProductCode(productCode);
		MDataMap param = new MDataMap("product_code",productCode);
		//需要查询三张表：pc_productinfo，pc_skuinfo，uc_sellercategory_product_relation
		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productinfo").dataSqlList("select p.seller_code,p.small_seller_code,p.product_code,p.product_code_old,p.product_name,p.product_status,p.product_volume,p.product_volume_item,"
				+ "p.product_weight,p.transport_template,p.cost_price,p.min_sell_price,p.max_sell_price,p.tax_rate,p.product_weight,p.market_price,p.video_main_pic,p.product_desc_video,p.video_url,p.mainpic_url,p.adpic_url,p.area_template,p.brand_code,p.validate_flag,p.product_adv,p.low_good,"
				+ "p.voucher_good,p.vipday_flag,p.accm_yn,e.settlement_type,p.vl_ors,p.dlr_charge,p.csps_flag,e.fictitious_sales "

				+ "from pc_productinfo p,pc_productinfo_ext e where p.product_code = :product_code and p.product_code = e.product_code",  param);
		
//		MDataMap mProductMap = DbUp.upTable("pc_productinfo").one("product_code", productCode);
		List<MDataMap> skus = DbUp.upTable("pc_skuinfo").queryByWhere("product_code", productCode,"sale_yn","Y","flag_enable","1");
//		DbUp.upTable("uc_sellercategory_product_relation").queryAll("category_code", "", "product_code=:product_code", param);
//		List<MDataMap> li = DbUp.upTable("uc_sellercategory_product_relation").queryByWhere("product_code",productCode);
		List<MDataMap> li = DbUp.upTable("uc_sellercategory_product_relation").queryAll("category_code", "", "product_code=:product_code", param);//优化

		String smallSellerCode = "";
		String sellerCode = "";
		List<PlusModelProductSkuInfo> skuList = new ArrayList<PlusModelProductSkuInfo>();
		List<String> pcPicList = new ArrayList<String>();			//轮播图
	    List<PlusModelPropertyInfo> propertyInfoList = new ArrayList<PlusModelPropertyInfo>();//商品的自定义属性
		if (null != dataSqlList && dataSqlList.size() > 0) {
			Map<String, Object> mProductMap = dataSqlList.get(0);
			sellerCode = toString(mProductMap.get("seller_code"));
			smallSellerCode = toString(mProductMap.get("small_seller_code"));
			plusModelProductInfo.setProductCode(toString(mProductMap.get("product_code")));
			plusModelProductInfo.setProductCodeOld(toString(mProductMap.get("product_code_old")));
			plusModelProductInfo.setProductName(toString(mProductMap.get("product_name")));
			plusModelProductInfo.setProductStatus(toString(mProductMap.get("product_status")));
			plusModelProductInfo.setProductVolume(new BigDecimal(toString(mProductMap.get("product_volume"))));
			plusModelProductInfo.setProductVolumeItem(toString(mProductMap.get("product_volume_item")));
			plusModelProductInfo.setProductWeight(new BigDecimal(toString(mProductMap.get("product_weight"))));
			plusModelProductInfo.setTransportTemplate(toString(mProductMap.get("transport_template")));
			plusModelProductInfo.setCost_price(new BigDecimal(toString(mProductMap.get("cost_price"))));
			plusModelProductInfo.setMinSellPrice(new BigDecimal(toString(mProductMap.get("min_sell_price"))));
			plusModelProductInfo.setMaxSellPrice(new BigDecimal(toString(mProductMap.get("max_sell_price"))));
			plusModelProductInfo.setTax_rate(new BigDecimal(toString(mProductMap.get("tax_rate"))));
			plusModelProductInfo.setProductWeight(new BigDecimal(toString(mProductMap.get("product_weight"))));
			plusModelProductInfo.setMarketPrice(new BigDecimal(toString(mProductMap.get("market_price"))));
			plusModelProductInfo.setVideoUrl(toString(mProductMap.get("video_url")));
			plusModelProductInfo.setVideoMainPic(toString(mProductMap.get("video_main_pic")));
			plusModelProductInfo.setProductDescVideo(toString(mProductMap.get("product_desc_video")));
			plusModelProductInfo.setMainpicUrl(toString(mProductMap.get("mainpic_url")));
			String adPic = "";
			if("SI2003".equals(smallSellerCode)) {//LD品，LD品的广告图需要去pc_productadpic这张表的数据
				adPic = getPcProductAdpic(plusModelProductInfo.getProductCode());
			}else {//商户品取pc_productinfo表中的adpic_url字段。
				adPic = toString(mProductMap.get("adpic_url"));
			}
			if(StringUtils.isEmpty(adPic)) {
				adPic = bConfig("xmassystem.default_ad_pic");
			}
			plusModelProductInfo.setAdpicUrl(adPic);
			plusModelProductInfo.setAreaTemplate(toString(mProductMap.get("area_template")));
			plusModelProductInfo.setBrandCode(toString(mProductMap.get("brand_code")));
			plusModelProductInfo.setSellerCode(sellerCode);
			plusModelProductInfo.setSmallSellerCode(smallSellerCode);
			plusModelProductInfo.setValidateFlag(toString(mProductMap.get("validate_flag")));
			plusModelProductInfo.setProductAdv(toString(mProductMap.get("product_adv")));
			plusModelProductInfo.setLowGood(toString(mProductMap.get("low_good")));
			plusModelProductInfo.setVoucherGood(toString(mProductMap.get("voucher_good")));
			plusModelProductInfo.setVipdayFlag(toString(mProductMap.get("vipday_flag")));
			plusModelProductInfo.setAccmYn(toString(mProductMap.get("accm_yn")));
			plusModelProductInfo.setSettlementType(toString(mProductMap.get("settlement_type")));
			plusModelProductInfo.setVlOrs(toString(mProductMap.get("vl_ors")));
			plusModelProductInfo.setDlrCharge(toString(mProductMap.get("dlr_charge")));
			plusModelProductInfo.setFictitiousSales(Integer.parseInt(mProductMap.get("fictitious_sales").toString()));
			plusModelProductInfo.setCspsFlag(toString(mProductMap.get("csps_flag")));
			if (StringUtils.isNotBlank(plusModelProductInfo.getBrandCode())) {
				MDataMap brandMapParam = DbUp.upTable("pc_brandinfo").one("brand_code",plusModelProductInfo.getBrandCode());
				if (brandMapParam != null) {
					plusModelProductInfo.setBrandName(brandMapParam.get("brand_name"));			//品牌
					plusModelProductInfo.setBrandNameEn(brandMapParam.get("brand_name_en"));	//品牌英文名称
				}
			}
//			if (KJT.equals(smallSellerCode)||MLG.equals(smallSellerCode)||QQT.equals(smallSellerCode)||SYC.equals(smallSellerCode)||CYGJ.equals(smallSellerCode)) {
			if (new PlusServiceSeller().isKJSeller(smallSellerCode)) {
				plusModelProductInfo.setFlagTheSea("1");
			}
			if(li!=null&&!li.isEmpty()){
				for (int i = 0; i < li.size(); i++) {
					if(StringUtils.isNotBlank(li.get(i).get("category_code"))){
						plusModelProductInfo.getCategorys().add(li.get(i).get("category_code"));
					}
				}
			}
			if(skus!=null&&!skus.isEmpty()){
				//sku信息
				for (int i = 0; i < skus.size(); i++) {
					plusModelProductInfo.getSkus().put(skus.get(i).get("sku_code"), Double.valueOf(skus.get(i).get("cost_price")));
					
					PlusModelProductSkuInfo skuObj = new PlusModelProductSkuInfo();
					SerializeSupport<PlusModelProductSkuInfo> sSupport = new SerializeSupport<PlusModelProductSkuInfo>();
					sSupport.serialize(skus.get(i), skuObj);
					skuList.add(skuObj);
				}
				plusModelProductInfo.setSkuList(skuList);
				plusModelProductInfo.setPropertyList(this.propertyListSku(skus));		//商品的规格
			}else{
				/**
				 * 如果商品下不存在sku信息则把此商品标注为已下架状态
				 * add by ligj
				 * time:2015-12-10 21:28:30
				 */
				plusModelProductInfo.setProductStatus("4497153900060003");
			}
			
			//产品详情（内容图片）
			MDataMap descriptMap = DbUp.upTable("pc_productdescription").one("product_code" ,productCode);
			if (null != descriptMap) {
				PlusModelPcProductdescription description = new PlusModelPcProductdescription();
				SerializeSupport<PlusModelPcProductdescription> ss = new SerializeSupport<PlusModelPcProductdescription>();
				ss.serialize(descriptMap, description);
				//商品标签
				if (StringUtils.isNotBlank(description.getKeyword())) {
					for (String label : description.getKeyword().split(",")) {
							plusModelProductInfo.getLabelsList().add(label);
					}
				}
				plusModelProductInfo.setDescription(description);
			}
			//图片列表list（轮播图）
//			List<MDataMap> picUrlsMap  = DbUp.upTable("pc_productpic").queryByWhere("product_code",productCode);
			List<MDataMap> picUrlsMap = DbUp.upTable("pc_productpic").queryAll("pic_url", "", "product_code=:product_code", param);//优化
			
			if (null != picUrlsMap) {
				for (MDataMap picUrlMap : picUrlsMap) {
					pcPicList.add(picUrlMap.get("pic_url"));
				}
			}
			plusModelProductInfo.setPcPicList(pcPicList);
			
			
			// 自定义属性
			MDataMap mWhereMapProperty = new MDataMap();
			mWhereMapProperty.put("property_type", "449736200004");
			mWhereMapProperty.put("product_code", productCode);
			
			List<MDataMap> properties = DbUp.upTable("pc_productproperty").queryAll("property_key,property_value,start_date,end_date", "property_type,small_sort desc,zid asc ", "", mWhereMapProperty);
			PlusModelPropertyInfo propertyProductCode = new PlusModelPropertyInfo();
			propertyProductCode.setPropertykey("商品编码");
			propertyProductCode.setPropertyValue(plusModelProductInfo.getProductCode());
			propertyInfoList.add(propertyProductCode);
			for (MDataMap mDataMap : properties) {
				PlusModelPropertyInfo property = new PlusModelPropertyInfo();
				property.setPropertykey(mDataMap.get("property_key"));
				property.setPropertyValue(mDataMap.get("property_value"));
				
				if(StringUtils.contains(mDataMap.get("property_key"), "内联赠品")){  // 不在时间段内的内联赠品不显示 - Yangcl 2016-12-08
					String startDate = mDataMap.get("start_date");
					String endDate = mDataMap.get("end_date");
					if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
						property.setStartDate(startDate);
						property.setEndDate(endDate); 
					}
				}
				
				//此步过滤在商品详情页做，否则在赠品页面取不到赠品
//				if ("内联赠品".equals(property.getPropertykey())) {
//					continue;
//				}
				propertyInfoList.add(property);
			}
			plusModelProductInfo.setPropertyInfoList(propertyInfoList);
			
			
			//权威标志
			/**
			 * 已经有专门给权威标志提供缓存方法，优化代码时确定没有调用此值的地方时可以删除
			 */
			List<PlusModelAuthorityLogo> authorityLogo = new ArrayList<PlusModelAuthorityLogo>();
			List<MDataMap> logoList = DbUp.upTable("pc_authority_logo").queryAll(
					null, "logo_location desc", "manage_code=:manage_code", new MDataMap("manage_code",sellerCode));
			
			String productType = "";
			/**
			 * 商户类型 2016-12-02 zhy
			 * 
			 * 524需求：根据商户类型映射对应的所属产品，获取后台配置的标签   2018-7-15  zhangbo
			 */
			//对LD商品做一下特殊判断
			String seller_type="";
			seller_type = WebHelper.getSellerType(smallSellerCode);
//			if("SI2003".equals(smallSellerCode)) {
//				seller_type="4497478100050000";
//			}
//			else {
//				seller_type = WebHelper.getSellerType(smallSellerCode);
//			}
			//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
//			Map productTypeMap = WebHelper.getAttributeProductType(seller_type);
			
			//plusModelProductInfo.setProductTypeMap(productTypeMap);
//			plusModelProductInfo.getProductTypeMap().put("proTypeListPic", productTypeMap.get("proTypeListPic")==null?"":productTypeMap.get("proTypeListPic").toString());
//			plusModelProductInfo.getProductTypeMap().put("proTypeInfoPic", productTypeMap.get("proTypeInfoPic")==null?"":productTypeMap.get("proTypeInfoPic").toString());
			
			
			if (CFAMILY.equals(smallSellerCode) && CFAMILY.equals(sellerCode)) {
				productType = "4497471600150001";		//LD商品
//			}else if (KJT.equals(smallSellerCode)||MLG.equals(smallSellerCode)||QQT.equals(smallSellerCode)||SYC.equals(smallSellerCode)||CYGJ.equals(smallSellerCode)) {
			}else if (new PlusServiceSeller().isKJSeller(smallSellerCode)) {
				productType = "4497471600150003";		//跨境通商品 || 麦乐购商品
//			}else if (smallSellerCode.startsWith("SF031")) {
			/**
			 * 修改判断商户条件 2016-12-02 zhy
			 */
			}else if (StringUtils.equals(seller_type,"4497478100050001")) {
				productType = "4497471600150002";		//商户商品
			}else if((CDOG.equals(smallSellerCode))&& 
					(CFAMILY.equals(sellerCode)||CDOG.equals(sellerCode))){
				productType = "4497471600150004";//沙皮狗商品
			}
			for (MDataMap logoMap : logoList) {
				PlusModelAuthorityLogo model = new PlusModelAuthorityLogo();
				model.setLogoContent(logoMap.get("logo_content"));
				model.setLogoPic(logoMap.get("logo_pic"));
				model.setLogoLocation(StringUtils.isBlank(logoMap.get("logo_location")) ? null : Integer.parseInt(logoMap.get("logo_location")));
				if ("449747110001".equals(logoMap.get("all_flag"))) {		//是否全场为否时，判断商品类型
					if (StringUtils.isNotEmpty(logoMap.get("show_product_source"))) {
						for (String channelCode : logoMap.get("show_product_source").split(",")) {
							if (productType.equals(channelCode)) {
								authorityLogo.add(model);
								break;
							}
						}
					}
				}else {
					authorityLogo.add(model);
				}
				
			}
			plusModelProductInfo.setAuthorityLogo(authorityLogo);
			
			//常见问题
			List<PlusModelCommonProblem> commonProblemList = new ArrayList<PlusModelCommonProblem>();
//			if (StringUtils.isNotBlank(sellerCode) && StringUtils.isNotBlank(smallSellerCode) && (smallSellerCode.equals(KJT) || smallSellerCode.equals(MLG)||QQT.equals(smallSellerCode)||SYC.equals(smallSellerCode)||CYGJ.equals(smallSellerCode))) {
			if (StringUtils.isNotBlank(sellerCode) && StringUtils.isNotBlank(smallSellerCode) && (new PlusServiceSeller().isKJSeller(smallSellerCode))) {
//				List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort desc,update_time desc", "seller_code='"+sellerCode+"'and small_seller_code='"+smallSellerCode+"'", null);
				List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort desc,update_time desc", "seller_code='"+sellerCode+"'", null);
				if (null != commonProblemMapList && !commonProblemMapList.isEmpty()) {
					for (MDataMap commonProblemMap : commonProblemMapList) {
						PlusModelCommonProblem commonProblem = new PlusModelCommonProblem();
						commonProblem.setTitle(commonProblemMap.get("title"));
						commonProblem.setContent(commonProblemMap.get("content"));
						commonProblemList.add(commonProblem);
					}
				}
				plusModelProductInfo.setCommonProblem(commonProblemList);
			}
			
			
			
		}
		
		
		return plusModelProductInfo;
	}
	
	/**
	 * 根据规则取商品的广告图信息
	 * @param productCode
	 * @return
	 */
	private String getPcProductAdpic(String productCode) {
		String ret = "";
		//取得商品广告图信息
		MDataMap pcAdpicListMapParam = new MDataMap();
		pcAdpicListMapParam.put("product_code", productCode);
		pcAdpicListMapParam.put("now", DateUtil.getSysDateTimeString());
		List<MDataMap> pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "start_date desc",
				"product_code=:product_code  and (sku_code='' or sku_code is null) and start_date <=:now and end_date >=:now", pcAdpicListMapParam, -1, -1);
		if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
			ret = pcAdpicListMap.get(0).get("pic_url");
		} else {
			pcAdpicListMapParam = new MDataMap();
			pcAdpicListMapParam.put("product_code", productCode);
			pcAdpicListMap = DbUp.upTable("pc_productadpic").query("pic_url", "",
					"product_code=:product_code  and (sku_code='' or sku_code is null) and (start_date='' or start_date is null) and (end_date='' or end_date is null)", pcAdpicListMapParam, -1, -1);
			if (pcAdpicListMap != null && pcAdpicListMap.size() > 0) {
				ret = pcAdpicListMap.get(0).get("pic_url");
			}
		}
		return ret;
	}
	
	/**
	 * 权威标志       show_type
	 */
	public PlusModelAuthorityLogos initProductAuthorityLogoFromDb(String sellerCode){
		PlusModelAuthorityLogos authorityLogos = new PlusModelAuthorityLogos();
		List<PlusModelAuthorityLogo> authorityLogo = new ArrayList<PlusModelAuthorityLogo>();
		MDataMap map = new MDataMap();
		map.put("manage_code",sellerCode);
		map.put("show_type","449747960001");   // 通路标签  
		List<MDataMap> logoList = DbUp.upTable("pc_authority_logo").queryAll(null, "logo_location desc", "manage_code=:manage_code and show_type=:show_type" , map );
		
		for (MDataMap logoMap : logoList) {
			PlusModelAuthorityLogo model = new PlusModelAuthorityLogo();
			model.setLogoContent(logoMap.get("logo_content"));
			model.setLogoPic(logoMap.get("logo_pic"));
			model.setLogoLocation(StringUtils.isBlank(logoMap.get("logo_location")) ? null : Integer.parseInt(logoMap.get("logo_location")));
			model.setAllFlag(logoMap.get("all_flag"));
			model.setManageCode(logoMap.get("manage_code"));
			model.setShowProductSource(logoMap.get("show_product_source"));
			authorityLogo.add(model);
		}
		authorityLogos.setAuthorityLogos(authorityLogo);
		return authorityLogos;
	}
	
	/**
	 * @description: 来自商户自己添加的权威标识
	 *
	 * @author Yangcl
	 * @date 2017年3月3日 下午2:40:01 
	 * @version 1.0.0.1
	 */
	public PlusModelAuthorityLogos initProductAuthorityLogoForSellerFromDb(String productCode){
		PlusModelAuthorityLogos authorityLogos = new PlusModelAuthorityLogos();
		List<PlusModelAuthorityLogo> list = new ArrayList<PlusModelAuthorityLogo>(); 
		
		List<MDataMap> pLogoList = DbUp.upTable("pc_product_authority_logo").queryAll(null, "zid desc", "product_code=:product_code" , new MDataMap("product_code" , productCode) );
		if(pLogoList != null && pLogoList.size() > 0){
			List<PlusModelAuthorityLogo> alog = new ArrayList<PlusModelAuthorityLogo>();   // 保存来自商户自己添加的权威标识的列表
			// 从redis中找reids key为：xs-ProductAuthorityLogo-SI2003-ProductLog 格式的key
			if (!XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).exists("SI2003-ProductLog")){
				MDataMap map = new MDataMap();
				map.put("manage_code","SI2003"); 
				map.put("show_type","449747960002");   // 商品标签
				List<MDataMap> logoList = DbUp.upTable("pc_authority_logo").queryAll(null, "logo_location desc", "manage_code=:manage_code and show_type=:show_type" , map );
				for (MDataMap logoMap : logoList) {
					PlusModelAuthorityLogo m = new PlusModelAuthorityLogo();
					m.setUid(logoMap.get("uid")); 
					m.setLogoContent(logoMap.get("logo_content"));
					m.setLogoPic(logoMap.get("logo_pic"));
					m.setLogoLocation(StringUtils.isBlank(logoMap.get("logo_location")) ? null : Integer.parseInt(logoMap.get("logo_location")));
					m.setAllFlag(logoMap.get("all_flag"));
					m.setManageCode(logoMap.get("manage_code"));
					m.setShowProductSource(logoMap.get("show_product_source"));
					alog.add(m);
				}
				// 有效时间30天
				XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).set("SI2003-ProductLog" ,GsonHelper.toJson(alog));
			}else{
				try {
					alog = JSONObject.parseArray(XmasKv.upFactory( EKvSchema.ProductAuthorityLogo ).get("SI2003-ProductLog"), PlusModelAuthorityLogo.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(alog.size() != 0){
				for(PlusModelAuthorityLogo e : alog){
					for (MDataMap m : pLogoList){
						if(e.getUid().equals( m.get("authority_logo_uid") )){     
							list.add(e);
						}
					}
				}
			}
		}
		
		authorityLogos.setAuthorityLogos(list);
		return authorityLogos;
	}
	
	/**
	 * 查询属于商品标签类型的权威标识
	 */
	public List<PlusModelAuthorityLogo> initProductAuthorityLogoForSellerFromDb(){
		List<PlusModelAuthorityLogo> alog = new ArrayList<PlusModelAuthorityLogo>();  
		if (!XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).exists("SI2003-ProductLog")){
			MDataMap map = new MDataMap();
			map.put("manage_code","SI2003"); 
			map.put("show_type","449747960002");   // 商品标签
			List<MDataMap> logoList = DbUp.upTable("pc_authority_logo").queryAll(null, "logo_location desc", "manage_code=:manage_code and show_type=:show_type" , map );
			for (MDataMap logoMap : logoList) {
				PlusModelAuthorityLogo m = new PlusModelAuthorityLogo();
				m.setUid(logoMap.get("uid")); 
				m.setLogoContent(logoMap.get("logo_content"));
				m.setLogoPic(logoMap.get("logo_pic"));
				m.setLogoLocation(StringUtils.isBlank(logoMap.get("logo_location")) ? null : Integer.parseInt(logoMap.get("logo_location")));
				m.setAllFlag(logoMap.get("all_flag"));
				m.setManageCode(logoMap.get("manage_code"));
				m.setShowProductSource(logoMap.get("show_product_source"));
				alog.add(m);
			}
			// 有效时间30天
			XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).set("SI2003-ProductLog" ,GsonHelper.toJson(alog));
		}else{
			try {
				alog = JSONObject.parseArray(XmasKv.upFactory( EKvSchema.ProductAuthorityLogo ).get("SI2003-ProductLog"), PlusModelAuthorityLogo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return alog;
	}	
	
	/**
	 * 跨境通商品常见问题
	 */
	public PlusModelCommonProblems initProductCommonProblemFromDb(String sellerCode){
		PlusModelCommonProblems commonProblems = new PlusModelCommonProblems();
		//常见问题
		List<PlusModelCommonProblem> commonProblemList = new ArrayList<PlusModelCommonProblem>();
		List<MDataMap> commonProblemMapList = DbUp.upTable("fh_common_problem").queryAll("", "sort desc,update_time desc", "seller_code='"+sellerCode+"'", null);
		if (null != commonProblemMapList && !commonProblemMapList.isEmpty()) {
			for (MDataMap commonProblemMap : commonProblemMapList) {
				PlusModelCommonProblem commonProblem = new PlusModelCommonProblem();
				commonProblem.setTitle(commonProblemMap.get("title"));
				commonProblem.setContent(commonProblemMap.get("content"));
				commonProblemList.add(commonProblem);
			}
		}
		commonProblems.setCommonProblems(commonProblemList);
		return commonProblems;
	}
		/*
	 * 
	 * 获取sku规格型号
	 */
	public List<PlusModelSkuPropertyInfo> propertyListSku(List<MDataMap> skuMapList) {
		List<PlusModelSkuPropertyInfo> propertyList = new ArrayList<PlusModelSkuPropertyInfo>();
		if (null == skuMapList || skuMapList.isEmpty()) {
			return propertyList;
		}
		MDataMap propertyMap = new MDataMap();
		for (MDataMap mDataMap : skuMapList) {
			String proCodeStr = mDataMap.get("sku_key"); // 属性code
			String proValueStr = mDataMap.get("sku_keyvalue"); // 属性value

			if (null == proCodeStr || null == proValueStr
					|| "".equals(proCodeStr) || "".equals(proValueStr)) {
				continue;
			}
			// 获得不重复的key-value
			String[] propertiesCodeArr = proCodeStr.split("&");
			String[] propertiesValue = proValueStr.split("&");
			for (int i = 0; i < propertiesCodeArr.length; i++) {
				propertyMap.put(propertiesCodeArr[i], propertiesValue[i]);
			}
		}
		MDataMap proKeyMap = new MDataMap(); // keyMap
		String[] propertyMapKey = propertyMap.convertKeysToStrings();
		for (int i = 0; i < propertyMapKey.length; i++) { // 获得不重复的规格key
			String[] codesStr = propertyMapKey[i].split("=");
			String[] valueStr = propertyMap.get(propertyMapKey[i]).split("=");
			proKeyMap.put(codesStr[0], valueStr[0]); // key
		}
		for (String keyCode : proKeyMap.convertKeysToStrings()) {
			List<PlusModelSkuPropertyValueInfo> propertyValueList = new ArrayList<PlusModelSkuPropertyValueInfo>(); // valueObjList
			PlusModelSkuPropertyInfo proCodeObj = new PlusModelSkuPropertyInfo(); // keyObj
			for (int i = 0; i < propertyMapKey.length; i++) {
				PlusModelSkuPropertyValueInfo proValueObj = new PlusModelSkuPropertyValueInfo(); // valueObj
				String keyCodes = propertyMapKey[i];
				String[] codesStr = keyCodes.split("=");
				String[] valueStr = propertyMap.get(keyCodes).split("=");

				if (codesStr[0].equals(keyCode)) {
					proValueObj.setPropertyValueCode(codesStr[1]); // value
					proValueObj.setPropertyValueName(valueStr[1]);
					propertyValueList.add(proValueObj);
				}
			}
			// 商品规格属性值按照key进行字典排序
			if (null != propertyValueList && propertyValueList.size() > 1) {
				  Collections.sort(propertyValueList, new Comparator<PlusModelSkuPropertyValueInfo>() {
			            public int compare(PlusModelSkuPropertyValueInfo arg0, PlusModelSkuPropertyValueInfo arg1) {
			                return arg0.getPropertyValueName().compareTo(arg1.getPropertyValueName());
			            }
			        });
			}
			//商品详情接口商品规格排序按ASCII码升序排列
			//衣服尺码特别处理，按以下方式排序：XXS、XS、S、M、L、XL、XXL、XXXL、XXXXL、XXXXXL
			String[] specialProperties = new String[]{"XXS","XS","S","M"};						//需要进行特殊排序的衣服尺码
			String[] normalProperties = new String[]{"L","XL","XXL","XXXL","XXXXL","XXXXXL"};	//不需要进行特殊排序的衣服尺码
			boolean flagExistSpecial = false;		//标志是否含有需要特殊排序的衣服尺码
			boolean flagExistNormal = false;		//标志是否含有不需要特殊排序的衣服尺码
			List<PlusModelSkuPropertyValueInfo> specialPropertyInfoList = new ArrayList<PlusModelSkuPropertyValueInfo>();		//结果集中包含的需要进行特殊排序的衣服尺码数组
			List<Integer> specialIndexArr = new ArrayList<Integer>();									//结果集中包含的需要进行特殊排序的衣服尺码下标数组
			for (int j = 0; j < propertyValueList.size(); j++) {
				for (String specialProperty : specialProperties) {
					if (propertyValueList.get(j).getPropertyValueName().equals(specialProperty)) {
						flagExistSpecial = true;
						specialPropertyInfoList.add(propertyValueList.get(j));
						specialIndexArr.add(j);
					}
				}
			}
			if (flagExistSpecial) {
				int normalStrIndex = 0;													//含有的第一个不需要特殊排序的衣服尺码下标
				for (int j = 0; j < propertyValueList.size(); j++) {
					for (String normalProperty : normalProperties) {
						if (propertyValueList.get(j).getPropertyValueName().equals(normalProperty)) {
							flagExistNormal = true;
							normalStrIndex = j;
							break;
						}
					}
					if (flagExistNormal) break;
				}
				
				//开始进行排序,有个规律，需要进行特殊排序的衣服尺码都位于不需要进行特殊排序的衣服尺码前面，插入顺序为{M、S、XS、XXS}，插入的下标为normalStrIndex
				for (int i = specialProperties.length-1; i >= 0; i--) {
					for (int j = 0;j < specialPropertyInfoList.size();j++ ) {
						if (specialPropertyInfoList.get(j).getPropertyValueName().equals(specialProperties[i])) {
							propertyValueList.add(normalStrIndex, specialPropertyInfoList.get(j));
							propertyValueList.remove(specialIndexArr.get(j)+1);
						}
					}
				}
			}
			
			proCodeObj.setPropertyKeyCode(keyCode);
			proCodeObj.setPropertyKeyName(proKeyMap.get(keyCode));
			proCodeObj.setPropertyValueList(propertyValueList);
			propertyList.add(proCodeObj);
		}
		// 商品规格属性按照key进行字典排序
		if (null != propertyList && propertyList.size() > 1) {	
			  Collections.sort(propertyList, new Comparator<PlusModelSkuPropertyInfo>() {
		            public int compare(PlusModelSkuPropertyInfo arg0, PlusModelSkuPropertyInfo arg1) {
		                return arg0.getPropertyKeyCode().compareTo(arg1.getPropertyKeyCode());
		            }
		        });
		}
		return propertyList;
	}

	/**
	 * 获取商品近30天的虚拟销售量
	 * @param productCode		商品编码
	 */
	public PlusModelProductSales getProductSales(String productCode){
		int day = 30;
		//获取day天前的日期
    	Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,   -day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String beforeDay = sdf.format(cal.getTime());
		String whereSql = " day >= '"+beforeDay+"' and product_code='"+productCode+"'";
		List<MDataMap> productSalesMapList = DbUp.upTable("pc_productsales_everyday").queryAll("product_code,day,sales", "", whereSql, null);
		
		MDataMap productFictitiousSales = DbUp.upTable("pc_productinfo_ext").oneWhere("fictitious_sales", "", "product_code='"+productCode+"'");
		int fx = 0;			//总销量
		int trueSale = 0;	//真实销量
		int fictition = 0;
		if (null!= productFictitiousSales) {
			fictition =Integer.parseInt(StringUtils.isBlank(productFictitiousSales.get("fictitious_sales")) ? "0" : productFictitiousSales.get("fictitious_sales"));
		}
		//循环productCode，获取到各个商品的销量mapList
		for (int i = day; i > 0; i--) {
			//循环求出每日销量
			Calendar everyDay = Calendar.getInstance();
			everyDay.add(Calendar.DATE,   -i);
			String nowDay = sdf.format(everyDay.getTime());
			int sales = 0;				//商品在这天的真实销量
			for (MDataMap productSalesMap : productSalesMapList) {
				if (productCode.equals(productSalesMap.get("product_code")) && nowDay.equals(productSalesMap.get("day"))) {
					sales = Integer.parseInt(productSalesMap.get("sales"));
					break;
				}
			}
			double fictitiousSales = fx + Math.abs(1/Math.sin(3*(day-i+1))) + 2*sales;
			fx = Integer.parseInt(new java.text.DecimalFormat("0").format(fictitiousSales));
			trueSale += sales;
		}
		fx = fx+fictition;
		PlusModelProductSales productSales = new PlusModelProductSales();
		productSales.setFictition(fictition);
		productSales.setFictitionSales30(fx);
		
		/**
		 * 2016-06-30新需求
		 * 当虚拟销量基数设置为0或真实销量大于等于100时，取真实销量
		 * 否则走算法取虚拟销量
		 */
		/**
		 * 2016-09-12需求
		 * 当真实销量为0时，取虚拟销量基数
		 */
		if (trueSale == 0) {
			productSales.setFictitionSales30(fictition);
		}if (fictition == 0 || trueSale >= 100) {
			productSales.setFictitionSales30(trueSale);
		}
		return productSales;
	}
	/**
	 * 根据标签编号获取标签信息  pc_product_labels
	 * @param labelsCode 非空，中间用英文逗号分隔
	 * @return
	 */
	public PlusModelProductLabel getProductLabelInfo(String labelCode){
		PlusModelProductLabel productLabelInfo = new PlusModelProductLabel();
		if (StringUtils.isBlank(labelCode)) {
			return productLabelInfo;
		}
		String sWhere = "label_code='"+labelCode+"' and flag_enable='1'";
		MDataMap labelsMap = DbUp.upTable("pc_product_labels").oneWhere("", "update_time desc", sWhere);
		SerializeSupport<PlusModelProductLabel> labelSerialize = new SerializeSupport<PlusModelProductLabel>();
		if (null!=labelsMap && !labelsMap.isEmpty()) {
			labelSerialize.serialize(labelsMap, productLabelInfo);
		}
		return productLabelInfo;
	}
	
	/**
	 * fq
	 * 根据活动编号查询活动信息
	 * @param activityCodes 活动编号 
	 * @return 活动List
	 */
	public ActivityInfoDetail getActivityInfo (String activityCode) {
		ActivityInfoDetail detailList = null;
		if(!StringUtils.isEmpty(activityCode)) {
			detailList = new ActivityInfoDetail();
			MDataMap one = DbUp.upTable("sc_event_conglobation").one("event_code",activityCode);
			if(null != one) {
				detailList.setEvent_code(one.get("event_code"));
				detailList.setFavorable_price(one.get("favorable_price"));
				detailList.setFlag_enable(one.get("flag_enable"));
				detailList.setProduct_code(one.get("product_code"));
				detailList.setPurchase_num(one.get("purchase_num"));
				detailList.setSelling_price(one.get("selling_price"));
				detailList.setSku_code(one.get("sku_code"));
				detailList.setSku_name(one.get("sku_name"));
				
				
				MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code",activityCode);
				
				detailList.setEnd_time(eventInfo.get("end_time"));
			}
		}
		
		return detailList;
	}
	
	/**
	 * 获取在指定时间内生效的唯一变价记录
	 * @param skuCode
	 * @param date
	 * @return
	 */
	public PlusModelSkuPriceFlow getSkuPriceChange(String skuCode, Date date) {
		if(date == null) date = new Date();
		PlusModelSkuPriceChange skuPriceChange = new LoadSkuPriceChange().upInfoByCode(new PlusModelSkuPriceChangeQuery(skuCode));
		
		String theDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		PlusModelSkuPriceFlow priceFlow = null;
		
		for(PlusModelSkuPriceFlow flow : skuPriceChange.getItemList()) {
			// 按开始时间倒序排列，取最近生效的一个
			if(flow.getStartTime().compareTo(theDate) <= 0
					&& flow.getEndTime().compareTo(theDate) >= 0){
				priceFlow = flow;
				break;
			}
		}
		
		return priceFlow;
	}
	
	 /**
	  * 根据sku当前的成本价和净毛利比例计算售价
	  * @param skuCode
	  * @param profitMargin
	  * @return
	  */
	 public BigDecimal computePriceByGross(String skuCode, String profitMargin) {
		 BigDecimal price = BigDecimal.ZERO;
		 BigDecimal cost = BigDecimal.ZERO;
		
		 PlusModelSkuPriceFlow skuflow = getSkuPriceChange(skuCode, new Date());
		 if(skuflow != null) {
			 cost = skuflow.getCostPrice();
		 } else {
			 PlusModelSkuQuery q = new PlusModelSkuQuery();
			 q.setCode(skuCode);
			 PlusModelSkuInfo skuInfo = new LoadSkuInfo().upInfoByCode(q);
			 cost = skuInfo.getCostPrice();
		 }
		 
		 if(cost.compareTo(BigDecimal.ZERO) > 0) {
			 price = computePriceByGross(cost, profitMargin);
		 }
		 
		 return price;
	 }
	 
	 /**
	  * 根据成本价和净毛利比例计算售价
	  * 售价 = 成本 / (1 - 毛利率)
	  * @param costPrice
	  * @param profitRate
	  * @return
	  */
	 public BigDecimal computePriceByGross(BigDecimal costPrice, String profitRate) {
		 BigDecimal money = null;
		 if(new BigDecimal(profitRate).compareTo(new BigDecimal(100)) >= 0
				 || new BigDecimal(profitRate).compareTo(BigDecimal.ZERO) < 0) {
			 // 毛利率超过100%或者小于0走异常的固定值
			 money = costPrice.multiply(new BigDecimal("1.3"));
		 } else {
			 money = costPrice.divide(new BigDecimal(1).subtract(new BigDecimal(profitRate).divide(new BigDecimal(100))),0, BigDecimal.ROUND_HALF_UP);
		 }
		 String moneyText = money.toString();
		 if(moneyText.endsWith("4")) {
			 // 最后1位数字是4时，则改为5
			 moneyText = moneyText.substring(0, moneyText.length() - 1) + "5";
		 } else if(moneyText.endsWith("38")) {
			 // 最后2位是38时，则改为39
			 moneyText = moneyText.substring(0, moneyText.length() - 2) + "39";
		 } else if(moneyText.endsWith("89")) {
			 // 最后2位是89时，则直接加1
			 moneyText = money.add(new BigDecimal(1)).toString();
		 }
		 return new BigDecimal(moneyText);
	 }
	
	/**
	 * 拼好货获取价格
	 * @param eventCode  活动编号  以CX开头的编号
	 */
	public BigDecimal getPrice(String eventCode){
		BigDecimal price = BigDecimal.ZERO;
		if (!XmasKv.upFactory(EKvSchema.PinHaoHuo).exists(eventCode)) {
			
			MDataMap mData = DbUp.upTable("sc_event_conglobation").oneWhere(
					"favorable_price", null, null, "event_code", eventCode);
			if(mData != null){
				XmasKv.upFactory(EKvSchema.PinHaoHuo).set(eventCode,mData.get("favorable_price"));
				XmasKv.upFactory(EKvSchema.PinHaoHuo).expire(eventCode,XmasSystemConst.STOCK_TTL_TIME);
				price = new BigDecimal(mData.get("favorable_price"));
			}
			
			
		}else{
			
			price = new BigDecimal(XmasKv.upFactory(EKvSchema.PinHaoHuo).get(eventCode));
		}
		
		if(price.compareTo(BigDecimal.ZERO)<=0){
			MDataMap mData = DbUp.upTable("sc_event_conglobation").oneWhere(
					"favorable_price", null, null, "event_code", eventCode);
			if(mData != null){
				price=new BigDecimal(mData.get("favorable_price"));
			}
		}
		
		return price;
		
	}
	
	
	/**
	 * 惠家有获取商品评论列表
	 */
	public PlusModelCommentList getProductComments(String productCode){
		PlusModelCommentList commentsList = new PlusModelCommentList();
		/**
		 * 上线下线：449746530001、449746530002  待审核 审核通过 审核拒绝：4497172100030001、4497172100030002、4497172100030003
		 */
		final String hasAccept = "4497172100030002";
		
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("product_code", productCode);
		mWhereMap.put("check_flag", hasAccept);
		//根据商品编码和评价等级查询商品列表评论
		String sWhere = " check_flag=:check_flag and product_code=:product_code";
		List<MDataMap> mapList = DbUp.upTable("nc_order_evaluation").queryAll("", "-oder_creattime,-grade", sWhere, mWhereMap);
		
		if (mapList == null || mapList.size() == 0) {
			return commentsList;
		}
		
		//查找商品下sku规格颜色
		List<MDataMap> skuMapList = DbUp.upTable("pc_skuinfo").queryAll("sku_code,sku_keyvalue", "", "product_code=:product_code", mWhereMap);
		
		//key:skuCode,value:keyValue
		Map<String,String> skuKeyValueMap = new HashMap<String, String>(); 
		
		if(skuMapList != null){
			
			for (MDataMap skuMap : skuMapList) {
				String sku_code = skuMap.get("sku_code");
				String sku_keyvalue = skuMap.get("sku_keyvalue");
				
				skuKeyValueMap.put(sku_code, null == sku_keyvalue ? "" : sku_keyvalue);
			}
		}
		//获取所有的用户编号，统一查询一次数据库
		Set<String> userSet = new HashSet<String>();
		for (MDataMap commenMap : mapList) {
			//用户编号
			String userCode = commenMap.get("order_name");
			if (StringUtils.isNotBlank(userCode)) {
				userSet.add(userCode);
			}
		}
		//用户对象Map，key：用户编号，value:用户信息
		Map<String,MDataMap> userMap = new HashMap<String, MDataMap>();
		List<MDataMap> userInfoMapList = DbUp.upTable("mc_extend_info_star").queryAll("nickname,member_avatar,status,member_code", "", "member_code in ('"+StringUtils.join(userSet,"','")+"')", new MDataMap());
		if (userInfoMapList != null) {
			for (MDataMap userInfoMap : userInfoMapList) {
				userMap.put(userInfoMap.get("member_code"), userInfoMap);
			}
		}
		
		for (MDataMap commenMap : mapList) {
			PlusModelComment comment = new PlusModelComment();
			//评论内容
			String commenContent = commenMap.get("order_assessment");
			//评论时间
			String commentTime = commenMap.get("oder_creattime");	
			//评论图片
			String oderPhotos = commenMap.get("oder_photos");
			//评论类型
			String gradeType = commenMap.get("grade_type");
			//评分
			String grade = commenMap.get("grade");
			//用户手机号
			String userMobile = commenMap.get("user_mobile");
			//用户编号
			String userCode = commenMap.get("order_name");
			//回复内容
			String replyContent = commenMap.get("reply_content");
			//回复时间
			String replyTime = commenMap.get("reply_createtime");
			//评论sku编号
			String skuCode = commenMap.get("order_skuid");
			//sku颜色
			String colorValue = "";
			//sku规格
			String styleValue = "";
			//用户头像
			String userFace = "";
			//用户昵称
			String nickName = "";
			
			{
				if(StringUtils.isNotBlank(skuKeyValueMap.get(skuCode))){
					String[] values = skuKeyValueMap.get(skuCode).split("&");
					if(values.length > 1){
						String[] colors = values[0].split("=");
						String[] styles = values[1].split("=");
						//sku“共同”属性值不显示
						if(colors.length > 1 && colors[1].toString().indexOf("共同") < 0){
							colorValue = colors[1].toString(); 
						}
						if(styles.length > 1 && styles[1].toString().indexOf("共同") < 0){
							styleValue = styles[1].toLowerCase();
						}
					}
				}
			}
			{
				MDataMap userInfoMap = userMap.get(userCode);
				if (userInfoMap != null) {
					//用户状态为正常时获取用户头像
					if ("449746600001".equals(userInfoMap.get("status"))) {
						userFace = userInfoMap.get("member_avatar");
					}
					nickName = userInfoMap.get("nickname");
				}
			}
			
			comment.setCommentContent(StringUtils.isBlank(commenContent) ? "" : commenContent);
			comment.setCommentTime(StringUtils.isBlank(commentTime) ? "" : commentTime);
			comment.setOderPhotos(oderPhotos);
			comment.setGradeType(gradeType);
			comment.setGrade(grade);
			comment.setUserMobile(userMobile);
			comment.setUserCode(userCode);
			comment.setReplyContent(StringUtils.isBlank(replyContent) ? "" : replyContent);
			comment.setReplyTime(replyTime);
			comment.setSkuCode(skuCode);
			comment.setSkuColor(colorValue);
			comment.setSkuStyle(styleValue);
			comment.setUserFace(userFace);
			comment.setNickName(nickName);
			
			commentsList.getProductComment().add(comment);
		}
		return commentsList;
	}
	private String toString(Object obj) {
		String rtn = "";
		if(null != obj ) {
			rtn = String.valueOf(obj);
		}
		return rtn;
	}
	
	public PlusModelCouponTypeInfo initCouponTypeInfoFromDb(String couponTypeCode) {
		PlusModelCouponTypeInfo result = new PlusModelCouponTypeInfo();
		MDataMap couponType = DbUp.upTable("oc_coupon_type").one("coupon_type_code",couponTypeCode);
		String sSql = "SELECT coupon_type_code FROM oc_coupon_info WHERE activity_code=:activity_code AND coupon_type_code=:coupon_type_code AND status = 0 AND SYSDATE() < end_time ORDER BY end_time DESC LIMIT 1";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_info").dataSqlList(sSql, new MDataMap("activity_code", couponType.get("activity_code"),"coupon_type_code",couponTypeCode));
		if(null != dataSqlList && dataSqlList.size()>0) {
			return null;
		}else {
			result.setFlag(0);
		}
		return result;
	}

	public long getProductStock(String productCode) {
		long allStock = 0;
		
		PlusSupportStock plusStock = new PlusSupportStock();
		PlusModelProductInfo productInfo = new LoadProductInfo().topInitInfo(new PlusModelProductQuery(productCode));
		List<PlusModelProductSkuInfo> skuList = productInfo.getSkuList();
		for(PlusModelProductSkuInfo sku : skuList) {
			long skuStock = plusStock.upAllStock(sku.getSkuCode());
			if(skuStock > 0) {
				allStock += skuStock;
			}
		}
		
		return allStock;
	}
public PlusModelSkuInfo upSkuInfoBySkuCodeForJJG(String sSkuCode, String sMemberCode,String memberCodeNew) {
		// 		//防止membercode和memberCodeNew都为空空指针
		if(sMemberCode==null&&memberCodeNew==null) {
			memberCodeNew = "";
		}
		
		PlusModelSkuQuery pq = new PlusModelSkuQuery();
		pq.setCode(sSkuCode);
		pq.setIfJJGFlag("1");
		if(StringUtils.isNotBlank(sMemberCode)){
			pq.setMemberCode(sMemberCode);
		}else{
			pq.setMemberCode(memberCodeNew);
		}

		return upSkuInfo(pq).getSkus().get(0);

	}
}
