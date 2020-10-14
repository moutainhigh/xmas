package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.load.LoadTemplateAreaCode;
import com.srnpr.xmassystem.modelbean.ActivityAgent;
import com.srnpr.xmassystem.modelevent.PlusModelSkuPriceFlow;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.service.PlusServiceActivityAgent;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.support.PlusSupportUser;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 初始化商品信息
 * 
 * @author shiyz
 * 
 */

public class TeslaMakeProduct extends TeslaTopOrderMake {
	
	static PlusServiceActivityAgent plusServiceActivityAgent = new PlusServiceActivityAgent();

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult result = new TeslaXResult();
		
		List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();

		boolean hasFlagTheSea = false;
		boolean hasNeigou = false;
		if (orderDetails != null && !orderDetails.isEmpty()) {
			
			// 当前有效的分销活动
			// 目前仅支持小程序
			ActivityAgent activityAgent = null;
			if("449747430023".equals(teslaOrder.getChannelId())) {
				activityAgent = plusServiceActivityAgent.getActivityAgent();
			}

			TeslaModelOrderDetail orderDetail;
			for (int i = 0; i < orderDetails.size(); i++) {
				if("0".equals(orderDetails.get(i).getGiftFlag())){
					continue;
				}
				orderDetail = orderDetails.get(i);
				String isSkuPriceToBuy = orderDetails.get(i).getIsSkuPriceToBuy();//是否原价购买 （0：否；1：是）
				String integralDetailId = orderDetails.get(i).getIntegralDetailId();//积分商城活动ID， 如果有值，则为积分商城活动
				
				//判断是否原价购买
				boolean isOriginal = false, isSupportCollage = false;
				if("1".equals(teslaOrder.getIsOriginal())) {
					isOriginal = true;
				}
				if("1".equals(teslaOrder.getCollageFlag())) {
					isSupportCollage = true;
				}
				
				//兑换码兑换、惠惠农场兑换 原价购买 不支持内购
				boolean isRedeem = false;
				int isPurchase = teslaOrder.getIsPurchase();
				if(StringUtils.isNotEmpty(teslaOrder.getRedeemCode()) || StringUtils.isNotEmpty(teslaOrder.getTreeCode())) {
					isRedeem = true;
					isPurchase = 0;
				}
				
				PlusModelSkuQuery pq = new PlusModelSkuQuery();
				pq.setChannelId(teslaOrder.getChannelId());
				if(isOriginal) {
					pq.setIsOriginal("1");
				}
				if(!isSupportCollage) {
					pq.setIsSupportCollage("0");
				}
				pq.setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
				pq.setIsPurchase(isPurchase);
				pq.setOrderSource(teslaOrder.getUorderInfo().getOrderSource());
				pq.setRedeem(isRedeem);
				pq.setCode(teslaOrder.getOrderDetails().get(i).getSkuCode());
				pq.setIfJJGFlag(orderDetails.get(i).getIfJJGFlag());
				pq.setEventCode(teslaOrder.getEventCode());
				
				// 判断当前商品是否走分销逻辑，分销商品不参与任何活动
				if(activityAgent != null  // 分销活动存在
						// 商品在分销活动商品列表中
						&& activityAgent.getCouponTypeMap().containsKey(orderDetail.getProductCode())
						// 不是扫码购
						&& !orderDetail.getSkuCode().contains("SMG")
						// 不是兑换码
						&& !isRedeem
						// 不是加价购
						&& !"1".equals(orderDetail.getIfJJGFlag())
						// 不是积分兑换
						&& !teslaOrder.isPointShop()
						//不是特殊活动
						&& StringUtils.isBlank(teslaOrder.getEventCode())) {
					
					pq.setIsOriginal("1");
					teslaOrder.setActivityAgent(activityAgent);
					// 设置商品的分销标识，后续逻辑此商品按分销商品处理
					orderDetail.setFxFlag(1);
				}
				
				// 查询商品信息
				//多彩下单商品不参加促销活动
				PlusModelSkuInfo plusModelSkuInfo  = new PlusSupportProduct().upSkuInfo(pq).getSkus().get(0);
				// 查询商品扩展信息

				PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();

				plusModelQuery.setCode(teslaOrder.getOrderDetails().get(i)
						.getProductCode());

				PlusModelSkuInfoSpread plusModelSkuInfoSpread = new LoadSkuInfoSpread()
						.upInfoByCode(plusModelQuery);
				
				
				if (StringUtils.isNotBlank(plusModelSkuInfo.getSkuCode())) {
					// 除了购物车，其他都做SKU是否可售校验
					if(teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
						if(!"Y".equalsIgnoreCase(plusModelSkuInfo.getSaleYn())){
							result.inErrorMessage(963901134, plusModelSkuInfo.getSkuName());
							return result;
						}
					}
					
					PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(plusModelSkuInfo.getProductCode());
 					PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
					if(StringUtils.isNotBlank(plusModelProductinfo.getProductCodeOld())){
						plusModelQuery.setCode(plusModelProductinfo.getProductCodeOld());
					}
					if(StringUtils.isNotBlank(plusModelProductinfo.getAreaTemplate())){
						PlusModelTemplateAreaQuery taq = new PlusModelTemplateAreaQuery();
						taq.setCode(plusModelProductinfo.getAreaTemplate());
						teslaOrder.getShowGoods().get(i).setAreaCodes(new LoadTemplateAreaCode().upInfoByCode(taq));
					}
					// 赠品信息
//					PlusModelGitfSkuInfoList plusModelGitfSkuInfoList = new LoadGiftSkuInfo()
//							.upInfoByCode(plusModelQuery);
					Map<String, Double> skus = plusModelProductinfo.getSkus();
					teslaOrder.getOrderDetails().get(i).setCostPrice(
							skus!=null&&skus.containsKey(plusModelSkuInfo.getSkuCode())&&
							skus.get(plusModelSkuInfo.getSkuCode())>0?
									BigDecimal.valueOf(skus.get(plusModelSkuInfo.getSkuCode()))
									:plusModelProductinfo.getCost_price());
					
					teslaOrder.getOrderDetails().get(i).setTaxRate(plusModelProductinfo.getTax_rate());
					// 商品信息
					teslaOrder.getOrderDetails().get(i)
							.setProductCode(plusModelSkuInfo.getProductCode());
					
					teslaOrder
							.getOrderDetails()
							.get(i)
							.setProductPicUrl(
									plusModelSkuInfo.getProductPicUrl());
					
					// 结算类型，后续判断积分兑换订单时会使用
					teslaOrder.getOrderDetails().get(i).setSettlementType(plusModelProductinfo.getSettlementType());
					
					//如果为多彩订单 则为下单时传过来的价格
					if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())){
						BigDecimal sku_supply = (BigDecimal) DbUp.upTable("pc_bf_skuinfo").dataGet("sku_supply", "", new MDataMap("sku_code", teslaOrder.getOrderDetails().get(i).getSkuCode()));
						teslaOrder.getOrderDetails().get(i).setShowPrice(sku_supply);
						teslaOrder.getOrderDetails().get(i).setSkuPrice(sku_supply);
					}else{
						teslaOrder.getOrderDetails().get(i)
						.setShowPrice(plusModelSkuInfo.getSellPrice());
					}
					
					teslaOrder.getOrderDetails().get(i)
							.setSkuCode(plusModelSkuInfo.getSkuCode());

					teslaOrder.getOrderDetails().get(i)
							.setSkuName(plusModelSkuInfo.getSkuName());
					
					// LD商品可单独设置是不是只支持在线支付
					teslaOrder.getOrderDetails().get(i).setOnlinepayFlag(plusModelSkuInfo.getOnlinepayFlag());
					teslaOrder.getOrderDetails().get(i).setOnlinepayStart(plusModelSkuInfo.getOnlinepayStart());
					teslaOrder.getOrderDetails().get(i).setOnlinepayEnd(plusModelSkuInfo.getOnlinepayEnd());
					
					//多彩下单不赋予积分
					if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())) {
						teslaOrder.getOrderDetails().get(i).setAccmYn("Y");
					}else {
						teslaOrder.getOrderDetails().get(i).setAccmYn(plusModelProductinfo.getAccmYn());
					}

					/*
					 * 记录sku此时的档案价（实售价，非活动价格）,记录在订单详情的sell_price 字段里
					 */
					teslaOrder.getOrderDetails().get(i).setSellPrice(plusModelSkuInfo.getSkuPrice());
					
					// 重新设置商品的动态成本价和动态销售价（如果存在调价记录）,多彩宝渠道订单除外
					if(!"449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())) {
						PlusModelSkuPriceFlow flow = new PlusSupportProduct().getSkuPriceChange(plusModelSkuInfo.getSkuCode(), new Date());
						if(flow != null) {
							teslaOrder.getOrderDetails().get(i).setCostPrice(flow.getCostPrice());
							teslaOrder.getOrderDetails().get(i).setSellPrice(flow.getSellPrice());
						}
					}
					
					/* 更换获取售价的方法 start fq++*/
					//判断是否是拼好货订单
					if("449715200013".equals(teslaOrder.getUorderInfo().getOrderType())) {
						teslaOrder.getOrderDetails().get(i)
								.setSkuPrice(MoneyHelper.roundHalfUp(new PlusSupportProduct().getPrice(plusModelSkuInfo.getEventCode())));
					//多彩订单 此处不做处理
					} else if(!"449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())) {
						teslaOrder.getOrderDetails().get(i)
								.setSkuPrice(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSellPrice()));
					} 
					/* 更换获取售价的方法 end */
					
					if(i>0 && teslaOrder.getUorderInfo().getOrderSource().equals("449715190014") 
							&& !teslaOrder.getOrderDetails().get(i - 1).getSmallSellerCode().equals(plusModelSkuInfo.getSmallSellerCode())){
						//多彩宝订单如果存在多商户商品，则不予创建订单
						result.setResultCode(963902222);
						result.setResultMessage(bInfo(963902222));
						return result;
					}
					
					/* 缤纷商品添加佣金百分比、利润百分比 rhb++*/
					if("449715190014".equals(teslaOrder.getUorderInfo().getOrderSource())){
						//取消佣金百分比、利润百分比  添加多彩商品成本价20180806
						teslaOrder.getOrderDetails().get(i).setSkuCharge("0");
						teslaOrder.getOrderDetails().get(i).setSkuProfit("0");
						
					}
					/* 缤纷商品添加佣金百分比、利润百分比 rhb++*/
					
					//添加商户编号，为后续的平摊金额做准备（平摊金额需分LD和自营商户;LD商品不支持小数，自行商品支持小数）
					teslaOrder.getOrderDetails().get(i).setSmallSellerCode(plusModelSkuInfo.getSmallSellerCode());
					
					// showGoods信息
					teslaOrder.getShowGoods().get(i).setIfJJGFlag(teslaOrder.getOrderDetails().get(i).getIfJJGFlag());
					teslaOrder.getShowGoods().get(i)
							.setProductCode(plusModelSkuInfo.getProductCode());
					teslaOrder.getShowGoods().get(i).setLabelsList(plusModelProductinfo.getLabelsList());
					
					teslaOrder.getShowGoods().get(i).setLowGood(plusModelProductinfo.getLowGood());
					
					teslaOrder.getShowGoods().get(i).setLabelsPic(new ProductLabelService().getLabelInfo(plusModelSkuInfo.getProductCode()).getListPic());
					
					if(StringUtils.isNotBlank(plusModelSkuInfo.getSkuPicUrl())) {
						teslaOrder.getShowGoods().get(i).setProductPicUrl(plusModelSkuInfo.getSkuPicUrl());
					} else {
						teslaOrder.getShowGoods().get(i).setProductPicUrl(plusModelSkuInfo.getProductPicUrl());
					}

					teslaOrder.getShowGoods().get(i).setSkuCode(plusModelSkuInfo.getSkuCode());
					teslaOrder.getShowGoods().get(i).setMiniOrder(plusModelSkuInfo.getMinBuy());
					teslaOrder.getShowGoods().get(i).setSku_keyValue(plusModelSkuInfo.getSkuKeyvalue());
					teslaOrder.getShowGoods().get(i).setOrig_sku_price(plusModelSkuInfo.getSkuPrice());
					teslaOrder.getShowGoods().get(i).setProductStatus(plusModelProductinfo.getProductStatus());
					//单sku是否可售 add by zht
					teslaOrder.getShowGoods().get(i).setSaleYn(plusModelSkuInfo.getSaleYn());
					
					// 分销标识
					teslaOrder.getShowGoods().get(i).setFxFlag(orderDetail.getFxFlag());
					teslaOrder.getShowGoods().get(i).setFxrcode(orderDetail.getFxrcode());
					//推广人标识
					teslaOrder.getShowGoods().get(i).setShareCode(orderDetail.getFxrcode());
					
					// 推广赚标识
					teslaOrder.getShowGoods().get(i).setTgzUserCode(orderDetail.getTgzUserCode());
					teslaOrder.getShowGoods().get(i).setTgzShowCode(orderDetail.getTgzShowCode());
					
					if( "1".equals(isSkuPriceToBuy) || !"".equals(integralDetailId)) {//判断是否原价购买或积分商城活动
						//如果原价购买，不检测活动  活动数据清空
						plusModelSkuInfo.setItemCode("");
						plusModelSkuInfo.setEventCode("");
						plusModelSkuInfo.setEventType("");
						plusModelSkuInfo.setItemCode("");
					}
					if(StringUtils.isNotBlank(plusModelSkuInfo.getItemCode()) && StringUtils.isNotBlank(plusModelSkuInfo.getEventCode())){
						teslaOrder.getShowGoods().get(i).setSkuActivityCode(plusModelSkuInfo.getItemCode());
						teslaOrder.getShowGoods().get(i).setEventType(plusModelSkuInfo.getEventType());
						teslaOrder.getShowGoods().get(i).setEventCode(plusModelSkuInfo.getEventCode());
						teslaOrder.getShowGoods().get(i).setLimit_order_num(Long.valueOf(plusModelSkuInfo.getMaxBuy()).intValue());
						teslaOrder.getShowGoods().get(i).setIs_activity(plusModelSkuInfo.getBuyStatus()==1?true:false);
					}else {
						PlusSupportStock supportStock = new PlusSupportStock();
						int sum = supportStock.upSalesStock(plusModelSkuInfo.getSkuCode());
						if(sum == 0 && teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart) {
							NoStockOrFailureGoods noStockOrFailureGoods = new NoStockOrFailureGoods();
							noStockOrFailureGoods.setProduct_code(plusModelSkuInfo.getProductCode());
							noStockOrFailureGoods.setSku_code(plusModelSkuInfo.getSkuCode());
							noStockOrFailureGoods.setSku_name(plusModelSkuInfo.getSkuName());
							noStockOrFailureGoods.setSku_pic(plusModelSkuInfo.getSkuPicUrl());
							noStockOrFailureGoods.setSku_num(teslaOrder.getOrderDetails().get(i).getSkuNum());
							teslaOrder.getNoStockOrFailureGoods().add(noStockOrFailureGoods);
							result.inErrorMessage(916425001);
						}else if(teslaOrder.getOrderDetails().get(i).getSkuNum()>sum&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
							result.inErrorMessage(964305152, teslaOrder.getOrderDetails().get(i).getProductCode());
						}
						
					}
					teslaOrder.getShowGoods().get(i).setSkuName(plusModelSkuInfo.getSkuName());
					teslaOrder.getShowGoods().get(i).setLimitBuy(plusModelSkuInfo.getLimitBuy());
					teslaOrder.getShowGoods().get(i).setMaxBuyCount(plusModelSkuInfo.getMaxBuy());
					teslaOrder.getShowGoods().get(i).setMiniOrder(plusModelSkuInfo.getMinBuy());
					teslaOrder.getShowGoods().get(i).setStockNumSum(plusModelSkuInfo.getLimitStock());
					teslaOrder.getShowGoods().get(i).setShowLimitNum(plusModelSkuInfo.getShowLimitNum());
					
					// 在订单确认页面时，需要返回扫码购的item，否则微信商城就不会记录为扫码购通路
					if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Confirm 
							&& teslaOrder.getOrderDetails().get(i).getSkuCode().contains("SMG")){
						teslaOrder.getShowGoods().get(i).setSkuActivityCode(teslaOrder.getOrderDetails().get(i).getSkuCode());
					}
					
					/* 更换获取售价的方法 start fq++*/
					if("449715200013".equals(teslaOrder.getUorderInfo().getOrderType())) {
						teslaOrder.getShowGoods().get(i).setSkuPrice(MoneyHelper.roundHalfUp(new PlusSupportProduct().getPrice(plusModelSkuInfo.getEventCode())));
					} else {
						teslaOrder.getShowGoods().get(i).setSkuPrice(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSellPrice()));
					}
					/* 更换获取售价的方法 end */
					
					teslaOrder
							.getShowGoods()
							.get(i)
							.setValidateFlag(plusModelSkuInfo.getValidateFlag());
					teslaOrder.getShowGoods().get(i).setSmallSellerCode(plusModelSkuInfo.getSmallSellerCode());
//					if (teslaOrder.getShowGoods().get(i).getSmallSellerCode()
//							.equals("SF03KJT")||teslaOrder.getShowGoods().get(i).getSmallSellerCode()
//							.equals("SF03MLG")||teslaOrder.getShowGoods().get(i).getSmallSellerCode()
//							.equals("SF03100294")||teslaOrder.getShowGoods().get(i).getSmallSellerCode()
//							.equals("SF03100327")||teslaOrder.getShowGoods().get(i).getSmallSellerCode()
//							.equals("SF03100329")) {
					if(new PlusServiceSeller().isKJSeller(teslaOrder.getShowGoods().get(i).getSmallSellerCode())){
					
						teslaOrder.getShowGoods().get(i).setFlagTheSea("1");
						hasFlagTheSea = true;
					} else {
						
						teslaOrder.getShowGoods().get(i).setFlagTheSea("0");
					}
					
					if("4497472600010006".equals(teslaOrder.getShowGoods().get(i).getEventType())) {
						hasNeigou = true;
					}
					
					// 扩展信息
					if (plusModelSkuInfoSpread != null) {

						teslaOrder
								.getShowGoods()
								.get(i)
								.setPrchType(
										plusModelSkuInfoSpread.getPrchType());

						teslaOrder.getShowGoods().get(i)
								.setDlrId(plusModelSkuInfoSpread.getDlrId());

						teslaOrder.getShowGoods().get(i)
								.setSiteNo(plusModelSkuInfoSpread.getSiteNo());
						
						teslaOrder.getShowGoods().get(i).setProductTradeType(plusModelSkuInfoSpread.getProductTradeType());
						
						teslaOrder.getShowGoods().get(i).setDeliveryStoreType(plusModelSkuInfoSpread.getDeliveryStoreType());
						
					}
//					if (plusModelGitfSkuInfoList.getGiftSkuinfos() != null && !plusModelGitfSkuInfoList.getGiftSkuinfos().isEmpty()
//							&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart) {
//						for (int j = 0; j < plusModelGitfSkuInfoList.getGiftSkuinfos().size(); j++) {
//							
//							PlusModelGiftSkuinfo mlgi = plusModelGitfSkuInfoList.getGiftSkuinfos().get(j);
//							TeslaModelOrderDetail giftDetail = new TeslaModelOrderDetail();
//							giftDetail.setSkuCode(mlgi.getGood_id());
//							giftDetail.setProductCode(plusModelSkuInfo.getSkuCode());
//							giftDetail.setSkuName(mlgi.getGood_nm());
//							giftDetail.setSkuPrice(new BigDecimal(0));
//							giftDetail.setGiftFlag("0");
//							giftDetail.setGiftCd(mlgi.getGift_cd());
//							teslaOrder.getOrderDetails().add(giftDetail);
//							TeslaModelOrderActivity giftActi = new TeslaModelOrderActivity();
//							giftActi.setActivityCode(mlgi.getEvent_id());
//							giftActi.setOutActiveCode(mlgi.getEvent_id());
//							giftActi.setSkuCode(mlgi.getGood_id());
//							giftActi.setProductCode(plusModelSkuInfo.getSkuCode());
//							teslaOrder.getActivityList().add(giftActi);
//						}
//					}
					if(teslaOrder.getStatus().getExecStep()==ETeslaExec.Confirm||teslaOrder.getStatus().getExecStep()==ETeslaExec.Create){
						//判断如果有内购商品是否符合内购条件 , ---单个sku不超过2件，单月总sku不超过5件
						if(teslaOrder.getIsPurchase()==1) {
							//判断用户是否内购用户,并且不是积分商城
							if(new PlusSupportUser().upVipType(teslaOrder.getUorderInfo().getBuyerCode())&&!teslaOrder.isPointShop()){
								List<TeslaModelShowGoods> showGoods = teslaOrder.getShowGoods();
								List<TeslaModelShowGoods> neiGouList  = judgeNeiGou(showGoods);
								if(null!=neiGouList&&neiGouList.size()>0) {
									//判断单个sku购买数量是否>2，总sku是否>5
									boolean skuFlag = true;
									Integer totalSku = 0;
									//判断此次购买是否有两件以上相同productCode商品
									Map<String,Integer> map = new HashMap<>();//String为 productCode,Integer为数量
									for(TeslaModelShowGoods goodsInfoForAdd : neiGouList) {
										if(goodsInfoForAdd.getSkuNum()>2) {
											skuFlag = false;
											break;
										};
										String productCode = goodsInfoForAdd.getProductCode();
										if(!map.containsKey(productCode)) {
											map.put(productCode, goodsInfoForAdd.getSkuNum());
										}else {
											Integer integer = map.get(productCode); 
											integer += goodsInfoForAdd.getSkuNum();
											map.remove(productCode);
											map.put(productCode, integer);
										}
									}
									
									if(skuFlag) {
										Set<String> keySet = map.keySet();
										for(String productCode : keySet) {
											Integer num = map.get(productCode);
											if(num>2) {
												skuFlag =  false;
												break;
											}
											if(num+numHistory(teslaOrder.getUorderInfo().getBuyerCode(), productCode)>2) {
												skuFlag = false;
												break;
											}
											totalSku +=  num;
										}
									}
									if(!skuFlag) {//内购单个商品每月最多购买两件
										result.setResultCode(963906057);
										result.setResultMessage(bInfo(963906057));
										return result;
									}
									if(totalSku>5) {//内购商品每月最多购买五件
										result.setResultCode(963906058);
										result.setResultMessage(bInfo(963906058));
										return result;
									}
									//查看用户本月已内购商品数量
									Integer numHistory = numHistory(teslaOrder.getUorderInfo().getBuyerCode(),"");
									if(totalSku+numHistory>5) {//内购商品每月最多购买五件
										result.setResultCode(963906058);
										result.setResultMessage(bInfo(963906058));
										return result;
									}
								}
							}
						}
					}
					

				} else {

					result.setResultCode(963902249);

					result.setResultMessage(bInfo(963902249));

					return result;
				}
			}
			
		} else {
			if(teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
				result.setResultCode(963902249);
				result.setResultMessage(bInfo(963902249));
				return result;
			}
		}
		
		// 内购和海外购暂不支持同时下单，避免海外购订单记录不到身份证信息的情况
		if(hasFlagTheSea && hasNeigou && teslaOrder.getStatus().getExecStep() == ETeslaExec.Confirm) {
			result.setResultCode(916424003);
			result.setResultMessage("参与内购的商品不支持跟海外购商品同时结算，请分开进行结算");
			return result;
		}
		
		// 海外购商品必须提交一个身份证信息
		if(hasFlagTheSea && teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
			if(StringUtils.isBlank(teslaOrder.getAddress().getAuthIdcardNumber())) {
				result.setResultCode(963902216);
				result.setResultMessage("海外购商品需要提供收货人身份证信息，请完善后重试");
				return result;
			}
		}

		return result;
	}
	
	private Integer numHistory(String isMemberCode,String productCode) {
		String sql = "";
		if("".equals(productCode)) {
			sql = "SELECT SUM(b.sku_num) num	"
					+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
					+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
					+ "AND a.order_type = '449715200007' "
					+ "AND b.gift_flag = '1' "					
					+ "AND a.order_status != '4497153900010006'	"
					+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
		}else { 
			sql = "SELECT SUM(b.sku_num) num	"
					+ "FROM		oc_orderinfo a	LEFT JOIN oc_orderdetail b ON a.order_code = b.order_code	"
					+ "WHERE		a.buyer_code = '"+isMemberCode+"' "
					+ "AND a.order_type = '449715200007' "
					+ "AND b.gift_flag = '1' "
					+ "AND a.order_status != '4497153900010006' "
					+ "AND b.product_code = '"+productCode+"'	"
					+ "AND a.create_time > (SELECT			DATE_ADD(				curdate(),				INTERVAL - DAY (curdate()) + 1 DAY			)	)";
		}
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_orderinfo").dataSqlList(sql, null);
		Integer object = 0;
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			Map<String, Object> map = dataSqlList.get(0);
			Object object2 = map.get("num");
			if(object2!=null) {
				object = ((BigDecimal)object2).intValue();
			}
		}
		return object;
	}
	private List<TeslaModelShowGoods> judgeNeiGou(List<TeslaModelShowGoods> infoForAdds) {
		List<TeslaModelShowGoods> neiGouList =  new ArrayList<>();
		for(TeslaModelShowGoods teslaModelShowGoods : infoForAdds) {
			String eventType = teslaModelShowGoods.getEventType();
			if("4497472600010006".equals(eventType)) {
				neiGouList.add(teslaModelShowGoods);
			}
		}
		return neiGouList;
	}

}
