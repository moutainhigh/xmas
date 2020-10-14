package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.EEventPriceType;
import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.load.LoadEventExcludeProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventExclude;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventNgProduct;
import com.srnpr.xmassystem.modelevent.PlusModelEventPriceStep;
import com.srnpr.xmassystem.modelevent.PlusModelEventPurchase;
import com.srnpr.xmassystem.modelevent.PlusModelNgProduct;
import com.srnpr.xmassystem.modelevent.PlusModelPurchase;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportUser;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;

/***
 * 获取内购价格方法
 * @author zhouguohui
 *
 */
public class PlusServicePurchase  extends PlusServiceTop implements IPlusServiceProduct {

	/**
	 * 获取内购价
	 * @param sellerCode  系统编号
	 * @param memberCode  用户编号
	 * @return
	 */
	public  String getPurchase(String sellerCode,String memberCode,String channelId){
		return getPurchase(sellerCode, memberCode, null, channelId);
	}
	
	/**
	 * 获取内购价
	 * @param sellerCode  系统编号
	 * @param memberCode  用户编号
  	 * @param productCode  商品编号
	 * @return
	 */
	public  String getPurchase(String sellerCode,String memberCode, String productCode, String channelId){
		String eventCode = null;
		String itemCode=null;
		if(sellerCode==null){
			return null;
		}
		/*不是内购用户*/
		if(!new PlusSupportUser().upVipType(memberCode)){
			return null;
		}
		
		PlusModelEventPurchase pmep =  getEventPurchase(sellerCode);
		
		/***
		 * 当该商品不参加别的sku的时间修改价格
		 * 判断是否满足内购
		 */
		boolean is_true = false;
		String sellprice =null;
		if(pmep!=null){
			List<PlusModelPurchase> list = pmep.getListPurchase();
			if(list!=null && !list.isEmpty()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date());
				for(int i=0;i<list.size();i++){
					if(is_true){
						break;
					}
					PlusModelPurchase pmp = list.get(i);
					//判断当前是不是在内购活动的开始和结束时间之间
					if(PlusSupportEvent.compareDate(date,pmep.getListPurchase().get(i).getEndTime())<=0 && PlusSupportEvent.compareDate(pmep.getListPurchase().get(i).getBeginTime(),date)<=0){
						if(StringUtils.isBlank(pmp.getChannels()) || pmp.getChannels().contains(channelId)) {
							is_true=true;
							sellprice=pmp.getFavorablePrice();
							eventCode = pmp.getEventCode();
							itemCode = pmp.getItemCode();
						}
					}
					
					// 再判断一下内购是否有排除商品
					if(is_true && StringUtils.isNotBlank(productCode) && StringUtils.isNotBlank(eventCode)) {
						PlusModelEventExclude excludeProduct = new LoadEventExcludeProduct().upInfoByCode(new PlusModelQuery(eventCode));
						// 如果商品在活动的排除列表则此商品不参与内购活动
						if(excludeProduct.getProductCodeList().contains(productCode)) {
							is_true = false;
							sellprice = null;
							eventCode = null;
							itemCode = null;
						}
					}
				}
				
				
			}
			
			
		}
		
		if(sellprice==null && eventCode==null && itemCode==null){
			
			return null;
		}else{
			
			return sellprice+","+eventCode+","+itemCode;
		}
	}
	
	/***
	 * 内购活动
	 * @param sellerCode  系统编号
	 * @return
	 */
	public PlusModelEventPurchase getEventPurchase(String sellerCode){
		if(sellerCode==null){
			return null;
		}
		
		PlusModelEventPurchase goodsProduct = new PlusSupportEvent().getPurchase(sellerCode);
		return goodsProduct;
	  }
	
	
	/***
	 * 过滤购物车内购活动的价格
	 * @param sellerCode
	 */
	public PlusModelEventNgProduct getCartPurchase(PlusModelEventNgProduct pong ,String sellerCode, String channelId){
		
		/**取满足内购的活动价格**/
		String psps= new PlusServicePurchase().getPurchase(sellerCode, pong.getMemberCode(), channelId);
		if(psps!=null){
			String eventCode="";
			String itemCode="";
			String[] priceEvent = psps.split(",");
			for(int m=0;m<priceEvent.length;m++){
				if(m==0){
					psps=priceEvent[m];
				}else if(m==1){
					eventCode=priceEvent[m];
				}else if(m==2){
					itemCode=priceEvent[m];
				}
			}
			/**以下字段为过滤活动后的价格**/
			Map<String,PlusModelNgProduct> mapNew = new HashMap<String,PlusModelNgProduct>();
			List<PlusModelNgProduct> listPro = pong.getNgPro();
			//productCode的成本价  用于后期sku的成本价为0的时间取productCode成本价
			BigDecimal costPrice =pong.getProCostPrice();
				
			Map<String,BigDecimal> mapPrice = new HashMap<String,BigDecimal>();//<sku编号,sku成本价>
			List<PlusModelProductSkuInfo> listSkuInfo = pong.getListSku();
			/**将所有sku为key   sku的成本价的value放入map**/
			if(listSkuInfo!=null && listSkuInfo.size()>0){
				for(int j=0;j<listSkuInfo.size();j++){
					PlusModelProductSkuInfo pmpSkuInfo = listSkuInfo.get(j);
					mapPrice.put(pmpSkuInfo.getSkuCode(), pmpSkuInfo.getCostPrice());
				}
			}
				
			PlusModelEventExclude excludeProduct = null;
			if(StringUtils.isNotBlank(eventCode)) {
				excludeProduct = new LoadEventExcludeProduct().upInfoByCode(new PlusModelQuery(eventCode));	
			}
				
			for(int i=0;i<listPro.size();i++){
				PlusModelNgProduct ngPro = listPro.get(i);
				    
				    	boolean is_true_event=false;
						BigDecimal sellPrice = ngPro.getSkuPrice();//过活动后的价格  
						BigDecimal priceNew = BigDecimal.ZERO;
						
							/**如果sku的成本价为0  取productCode的成本价**/
							if(mapPrice.get(ngPro.getSkuCode()) != null && mapPrice.get(ngPro.getSkuCode()).compareTo(BigDecimal.ZERO)>0){
								priceNew = MoneyHelper.roundHalfUp(priceNew.add(mapPrice.get(ngPro.getSkuCode())).add(new BigDecimal(psps)));
							}else{
								priceNew = MoneyHelper.roundHalfUp(priceNew.add(costPrice).add(new BigDecimal(psps)));
							}
							
							// 内购包含小数时，只对第一位小数做四舍五入后取整
							priceNew = priceNew.setScale(1, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_HALF_UP);
							
							if(priceNew.compareTo(sellPrice)<=0){
								is_true_event=true;
								ngPro.setSkuPrice(priceNew);
							}
						
							// 判断内购活动屏蔽商品
							if(is_true_event && excludeProduct != null) {
								// 如果在屏蔽列表中则不参与活动
								if(excludeProduct.getProductCodeList().contains(ngPro.getProductCode())) {
									is_true_event = false;
								}
							}
							
							if(is_true_event){
								ngPro.setTrue(true);
								ngPro.setEventCode(eventCode);
								ngPro.setItemCode(itemCode);
							}
							
				mapNew.put(ngPro.getSkuCode(), ngPro);			
							
				    	
			}
			
			pong.setMap(mapNew);
				    
	   }
				
		return pong;
		
	}
	
	public void refreshSkuInfo(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {
		


		String sIcCode = plusSku.getItemCode();

		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();

		PlusModelEventItemProduct plusItemProduct = plusSupportEvent
				.upItemProductByIcCode(sIcCode);

		// 商品相关的通用检查和设置
		checkForProduct(plusSku, plusQuery, plusEvent, plusItemProduct);

		// ## 开始进入设置价格部分####################
		// 设置价格部分   内购不需要在设置价格
		//plusSku.setSellPrice(plusItemProduct.getPriceEvent());

		if (plusItemProduct.getPriceType() == EEventPriceType.Base) {
			//plusSku.setSellPrice(plusItemProduct.getPriceEvent());
		} else if (plusItemProduct.getPriceType() == EEventPriceType.StepTime) {

			// 获取差距的分钟数
			long lDeepTime = ((new Date().getTime()) - DateHelper.parseDate(
					plusEvent.getBeginTime()).getTime()) / (1000);

			for (PlusModelEventPriceStep pStep : plusItemProduct
					.getPriceSteps()) {
				if (pStep.getStepDeep().compareTo(new BigDecimal(lDeepTime)) <= 0) {
					plusSku.setSellPrice(pStep.getStepPrice());

				} else {
					// break;
				}

			}

		} else if (plusItemProduct.getPriceType() == EEventPriceType.StepStock) {

			// 获取已销售的数量
			long lDeep = plusItemProduct.getSalesStock()
					- plusSupportEvent.upEventItemSkuStock(sIcCode);

			for (PlusModelEventPriceStep pStep : plusItemProduct
					.getPriceSteps()) {
				if (pStep.getStepDeep().compareTo(new BigDecimal(lDeep)) <= 0) {
					plusSku.setSellPrice(pStep.getStepPrice());

				} else {
					// break;
				}

			}
		}

	
		
	}
}
