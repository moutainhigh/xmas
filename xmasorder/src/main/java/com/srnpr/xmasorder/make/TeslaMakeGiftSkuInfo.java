package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.List;

import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadGiftSkuInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelGiftSkuinfo;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelMediMclassGift;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basehelper.DateHelper;
/**
 * 
 * 类: TeslaMakeGiftSkuInfo <br>
 * 描述: 扫码购商品添加外联赠品 <br>
 * 作者: 付强 fuqiang@huijiayou.cn<br>
 * 时间: 2016-7-11 下午3:41:53
 */
public class TeslaMakeGiftSkuInfo extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		//判断是否是扫码购商品
		List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();
		if (orderDetails != null && !orderDetails.isEmpty()) {
			LoadProductInfo load = new LoadProductInfo();
			PlusModelProductInfo productInfo;
			
			List<TeslaModelOrderActivity> activityList = teslaOrder.getActivityList();
			TeslaMakeGiftSkuInfo teslaMakeGiftSkuInfo = new TeslaMakeGiftSkuInfo();
			// 查询商品扩展信息
			PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
			
			if(activityList.size() > 0 /*&& "449715190007".equals(teslaOrder.getUorderInfo().getOrderSource()) && "449715200010".equals(teslaOrder.getUorderInfo().getOrderType())*/) {
				
				
				for (int i = 0; i < activityList.size(); i++) {
					if("4497472600010015".equals(activityList.get(i).getActivityCode()) || "4497472600010012".equals(activityList.get(i).getActivityCode()) || "4497472600010004".equals(activityList.get(i).getActivityCode()) ) {
						
						productInfo = load.upInfoByCode(new PlusModelProductQuery(activityList.get(i).getProductCode()));
						// 忽略提货券商品的赠品，否则后续拆单会出错
						if("449747110002".equals(productInfo.getVoucherGood())){
							continue;
						}
						
						// 查询商品信息
						PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct()
						.upSkuInfoBySkuCode(activityList.get(i)
								.getSkuCode(), teslaOrder.getUorderInfo()
								.getBuyerCode(),teslaOrder.getIsMemberCode(),teslaOrder.getIsPurchase());
						plusModelQuery.setCode(activityList.get(i)
								.getProductCode());
						// 赠品信息
						teslaMakeGiftSkuInfo.saveInfo(teslaOrder, teslaMakeGiftSkuInfo.getGiftByChannId(plusModelQuery, "449747432222"),  plusModelSkuInfo);
						
					}
						
						
					
				}
				
			}
			/*
			 * 
			 */
			else if("449747430001".equals(teslaOrder.getChannelId()) || "449747430002".equals(teslaOrder.getChannelId()) || "449747430003".endsWith(teslaOrder.getChannelId()) || "449747430004".endsWith(teslaOrder.getChannelId())) {//app
				
				String channelId = teslaOrder.getChannelId();
				for (int i = 0; i < orderDetails.size(); i++) {
					
					TeslaModelOrderDetail orderDetail = orderDetails.get(i);
					if("0".equals(orderDetail.getGiftFlag())) {
						continue;
					}
					plusModelQuery.setCode(orderDetail.getProductCode());
					
					productInfo = load.upInfoByCode(new PlusModelProductQuery(orderDetail.getProductCode()));
					// 忽略提货券商品的赠品，否则后续拆单会出错
					if("449747110002".equals(productInfo.getVoucherGood())){
						continue;
					}
					
					// 查询商品信息
					PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct()
					.upSkuInfoBySkuCode(orderDetail
							.getSkuCode(), teslaOrder.getUorderInfo()
							.getBuyerCode(),teslaOrder.getIsMemberCode(),teslaOrder.getIsPurchase());
					//保存赠品信息
					teslaMakeGiftSkuInfo.saveInfo(teslaOrder, teslaMakeGiftSkuInfo.getGiftByChannId(plusModelQuery, channelId), plusModelSkuInfo);
					
				}
				
			}
			
			
			
		}
		
		
		return result;
		
	}
	
	/**
	 * 
	 * 方法: getGiftByChannId <br>
	 * 描述: TODO <br>
	 * 作者: 付强 fuqiang@huijiayou.cn<br>
	 * 时间: 2016-7-20 下午2:41:33
	 * @param plusModelQuery
	 * @param channelId ：
	 * 
	 * 	2	网站             	449747430002  || 449747430004
		34	APP通路     	449747430001
		39	扫码购       		449747432222   (由于下单等等没有明确的扫码购的渠道，通过下单的类型和来源有渠道号所以  需要传入  449747432222:代表扫码购渠道  （注：449747432222  此编号只是在此生效）  )
		42	微信商城             449747430003

	 * @return
	 */
	public  PlusModelGitfSkuInfoList getGiftByChannId(PlusModelSkuQuery plusModelQuery,String channelId){
		
		PlusModelGitfSkuInfoList returnList = new PlusModelGitfSkuInfoList();
		
		// 赠品信息
		PlusModelGitfSkuInfoList plusModelGitfSkuInfoList = new LoadGiftSkuInfo()
		.upInfoByCode(plusModelQuery);
		
		if (null != plusModelGitfSkuInfoList && null != plusModelGitfSkuInfoList.getGiftSkuinfos()) {
			 List<PlusModelGiftSkuinfo> giftSkuinfos = plusModelGitfSkuInfoList.getGiftSkuinfos();
			 
			
			 for (PlusModelGiftSkuinfo plusModelGiftSkuinfo : giftSkuinfos) {
				 String sysTime = DateHelper.upNow();
				 //判断在有效期内
				 if (plusModelGiftSkuinfo.getFr_date().compareTo(sysTime) <= 0
						 && plusModelGiftSkuinfo.getEnd_date().compareTo(sysTime) >= 0) {
					
					 List<PlusModelMediMclassGift> medi_mclssList = plusModelGiftSkuinfo.getMedi_mclss_nm();
					 
					 boolean flag = false;
					 // 2:网站；34:APP通路；39:扫码购；42:微信商城
					 for (PlusModelMediMclassGift plusModelMediMclassGift : medi_mclssList) {
						 //APP通路
						 if ("449747430001".equals(channelId) && "34".equals(plusModelMediMclassGift.getMEDI_MCLSS_ID())) {
							 flag = true;
								break;
						 }
						//网站渠道
						 if (("449747430004".equals(channelId) || "449747430002".equals(channelId))  && "2".equals(plusModelMediMclassGift.getMEDI_MCLSS_ID())) {
							 flag = true;
								break;
						}
						 //微信商城
						 if ("449747430003".equals(channelId) && "42".equals(plusModelMediMclassGift.getMEDI_MCLSS_ID())) {
							 flag = true;
								break;
							}
						//扫码购渠道  
						if ("449747432222".equals(channelId) && "39".equals(plusModelMediMclassGift.getMEDI_MCLSS_ID())) {
							flag = true;
							break;
						}
						
						
					}
					 if (flag) {
						 returnList.getGiftSkuinfos().add(plusModelGiftSkuinfo);
					}
				}
			}
		}
		return returnList;
	}
	
	public void saveInfo (TeslaXOrder teslaOrder,PlusModelGitfSkuInfoList plusModelGitfSkuInfoList,PlusModelSkuInfo plusModelSkuInfo) {

		
		if (plusModelGitfSkuInfoList!= null && plusModelGitfSkuInfoList.getGiftSkuinfos()!=null &&plusModelGitfSkuInfoList.getGiftSkuinfos().size()>0 ) {
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
		
	}
	
	
}
