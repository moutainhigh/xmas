package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadGiftSkuInfo;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelGiftSkuinfo;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 分销系统下单-初始化商品信息
 * 
 * @author xiegj
 * 
 */

public class TeslaMakeProductDistributor extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();
		BigDecimal Money = BigDecimal.ZERO;
		String param = "";
		if (orderDetails != null && !orderDetails.isEmpty()) {
			for (int i = 0; i < orderDetails.size(); i++) {
				if("0".equals(orderDetails.get(i).getGiftFlag())){
					continue;
				}
				
				// 查询商品信息
				PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct()
						.upSkuInfoBySkuCode(teslaOrder.getOrderDetails().get(i)
								.getSkuCode(), teslaOrder.getUorderInfo()
								.getBuyerCode(),teslaOrder.getIsMemberCode(),teslaOrder.getIsPurchase());
				// 查询商品扩展信息
				PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
				plusModelQuery.setCode(teslaOrder.getOrderDetails().get(i)
						.getProductCode());
				PlusModelSkuInfoSpread plusModelSkuInfoSpread = new LoadSkuInfoSpread()
						.upInfoByCode(plusModelQuery);
				// 赠品信息
				PlusModelGitfSkuInfoList plusModelGitfSkuInfoList1 = new LoadGiftSkuInfo()
						.upInfoByCode(plusModelQuery);
				//只有扫码购关联外联赠品
				PlusModelGitfSkuInfoList plusModelGitfSkuInfoList = new PlusModelGitfSkuInfoList();
				
				if (StringUtils.isNotBlank(plusModelSkuInfo.getSkuCode())) {
					// 商品信息
					teslaOrder.getOrderDetails().get(i)
							.setProductCode(plusModelSkuInfo.getProductCode());
					teslaOrder.getOrderDetails().get(i)
							.setProductPicUrl(plusModelSkuInfo.getProductPicUrl());
					teslaOrder.getOrderDetails().get(i)
							.setSkuCode(plusModelSkuInfo.getSkuCode());
					teslaOrder.getOrderDetails().get(i)
							.setSkuName(plusModelSkuInfo.getSkuName());
					teslaOrder.getOrderDetails().get(i)
							.setSkuPrice(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSkuPrice()));
					// showGoods信息
					teslaOrder.getShowGoods().get(i)
							.setProductCode(plusModelSkuInfo.getProductCode());
					teslaOrder.getShowGoods().get(i)
							.setProductPicUrl(plusModelSkuInfo.getProductPicUrl());
					teslaOrder.getShowGoods().get(i)
							.setSkuCode(plusModelSkuInfo.getSkuCode());
					teslaOrder.getShowGoods().get(i)
							.setMiniOrder(plusModelSkuInfo.getMinBuy());
					teslaOrder.getShowGoods().get(i)
							.setSku_keyValue(plusModelSkuInfo.getSkuKeyvalue());
					teslaOrder.getShowGoods().get(i)
							.setSkuName(plusModelSkuInfo.getSkuName());
					teslaOrder.getShowGoods().get(i)
							.setSkuPrice(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSkuPrice()));
					teslaOrder.getShowGoods().get(i)
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
					} else {
						teslaOrder.getShowGoods().get(i).setFlagTheSea("0");
					}
					// 扩展信息
					if (plusModelSkuInfoSpread != null) {
						teslaOrder.getShowGoods().get(i)
								.setPrchType(plusModelSkuInfoSpread.getPrchType());
						teslaOrder.getShowGoods().get(i)
								.setDlrId(plusModelSkuInfoSpread.getDlrId());
						teslaOrder.getShowGoods().get(i)
								.setSiteNo(plusModelSkuInfoSpread.getSiteNo());
					}
					if (plusModelGitfSkuInfoList.getGiftSkuinfos() != null && !plusModelGitfSkuInfoList.getGiftSkuinfos().isEmpty()) {
						for (int j = 0; j < plusModelGitfSkuInfoList.getGiftSkuinfos().size(); j++) {
							PlusModelGiftSkuinfo mlgi = plusModelGitfSkuInfoList.getGiftSkuinfos().get(j);
							TeslaModelOrderDetail giftDetail = new TeslaModelOrderDetail();
							giftDetail.setSkuCode(mlgi.getGood_id());
							giftDetail.setProductCode(plusModelSkuInfo.getSkuCode());
							giftDetail.setSkuName(mlgi.getGood_nm());
							giftDetail.setSkuPrice(new BigDecimal(0));
							giftDetail.setGiftFlag("0");
							giftDetail.setGiftCd(mlgi.getGift_cd());
							teslaOrder.getOrderDetails().add(giftDetail);
							TeslaModelOrderActivity giftActi = new TeslaModelOrderActivity();
							giftActi.setActivityCode(mlgi.getEvent_id());
							giftActi.setOutActiveCode(mlgi.getEvent_id());
							giftActi.setSkuCode(mlgi.getGood_id());
							giftActi.setProductCode(plusModelSkuInfo.getSkuCode());
							teslaOrder.getActivityList().add(giftActi);
						}
					}
					if(teslaOrder.getOrderDetails().get(i).getSkuNum()<plusModelSkuInfo.getMinBuy()){
						result.setResultCode(963904004);
						result.setResultMessage(bInfo(963904004, plusModelSkuInfo.getSkuCode(),plusModelSkuInfo.getSkuName(),plusModelSkuInfo.getMinBuy()));
						break;
					}
					Money = Money.add(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSkuPrice()).multiply(BigDecimal.valueOf(orderDetails.get(i).getSkuNum())));
				} else {
					result.setResultCode(963904002);
					result.setResultMessage(bInfo(963904002,teslaOrder.getOrderDetails().get(i).getSkuCode()));
					break;
				}
			}
		} else {
			result.setResultCode(963904001);
			result.setResultMessage(bInfo(963904001));
		}
		if(result.upFlagTrue()){
			if((teslaOrder.getStatus().getExecStep() == ETeslaExec.Distributor||teslaOrder.getStatus().getExecStep() == ETeslaExec.iqiyi)&&Money.compareTo(teslaOrder.getCheck_pay_money())!=0){
				result.setResultCode(963904003);
				result.setResultMessage(bInfo(963904003, param));
			}
		}
		return result;
	}

}
