package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.zapcom.topcache.SimpleCache;


/**
 * 获取商品价格
 * @author fq
 *
 */
public class ProductPriceService {
	
	static SimpleCache simpleCache = new SimpleCache(new SimpleCache.Config(60,60,"MinPrice",false));
	
	static PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
	
	/**
	 * 获取商品最低价格(此方法为3.9.4及以上版本调用)
	 * @param skuQuery
	 * @return
	 */
	public Map<String,BigDecimal> getProductMinPrice(PlusModelSkuQuery skuQuery){
		String pro = skuQuery.getCode();
		Map<String,BigDecimal> resultMap = new HashMap<String,BigDecimal>();
		
		if(StringUtils.isNotBlank(pro)){
					
			String[] proCode = pro.split(",");
			for(int j=0;j<proCode.length;j++){
				PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(proCode[j]));
				List<PlusModelProductSkuInfo> list = productInfo.getSkuList();
				List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
				for(PlusModelProductSkuInfo model : list ){
//					PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(model.getSkuCode(),skuQuery.getMemberCode());
					//PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(model.getSkuCode(),skuQuery.getMemberCode(),"",skuQuery.getIsPurchase());
					PlusModelSkuInfo skuInfo = getPlusModelSkuInfo(model.getSkuCode(),skuQuery.getMemberCode(),skuQuery.getIsPurchase(),skuQuery.getChannelId(),skuQuery.getFxFlag());
					if(skuInfo!=null){
						listPrice.add(skuInfo.getSellPrice());
					}
					
				}
				Collections.sort(listPrice); 
				//如果listPrice 几个为空  设定内购的价格为123456789   这是神坑的逻辑业务    
//				resultMap.put(proCode[j], listPrice.size()<=0?new BigDecimal(123456789):MoneyHelper.roundHalfUp(listPrice.get(0)));
				
				//去掉价格为123456789 的展示 ，展示商品市场价marketPrice  fq++
				resultMap.put(proCode[j], listPrice.size()<=0? productInfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP) :listPrice.get(0).setScale(2, RoundingMode.HALF_UP));
				
			}
		}
		return resultMap;
	}
	
	/**
	 * 获取商品最低价的SKU信息以及活动类型，优先判断参与活动的SKU
	 * @param skuQuery
	 * @return
	 */
	public Map<String,PlusModelSkuInfo> getProductMinPriceSkuInfo(PlusModelSkuQuery skuQuery){
//		String pro = skuQuery.getCode();
//		Map<String,PlusModelSkuInfo> resultMap = new HashMap<String,PlusModelSkuInfo>();
//		
//		if(StringUtils.isNotBlank(pro)){
//			
//			String[] proCode = pro.split(",");
//			PlusModelProductInfo productInfo;
//			PlusModelSkuInfo skuInfo,tmp;
//			for(int j=0;j<proCode.length;j++){
//				productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(proCode[j]));
//				for(PlusModelProductSkuInfo model : productInfo.getSkuList()){
//					skuInfo = getPlusModelSkuInfo(model.getSkuCode(),skuQuery.getMemberCode(),1);
//					if(skuInfo!=null){
//						if(!resultMap.containsKey(productInfo.getProductCode())) {
//							resultMap.put(productInfo.getProductCode(), skuInfo);
//							continue;
//						}
//						
//						tmp = resultMap.get(productInfo.getProductCode());
//						// 比较同个商品下面的SKU最小销售价
//						if(skuInfo.getSellPrice().compareTo(tmp.getSellPrice()) < 0){
//							// 如果最小销售价未参与活动但其他SKU参与活动则参与活动的SKU优先
//							if(StringUtils.isBlank(skuInfo.getEventCode()) && StringUtils.isNotBlank(tmp.getEventCode())) {
//								continue;
//							}
//							
//							resultMap.put(productInfo.getProductCode(), skuInfo);
//						}
//					}
//				}
//			}
//		}
		return getProductMinPriceSkuInfo(skuQuery, false);
	}
	
	/**
	 * 获取商品最低价的SKU信息以及活动类型，优先判断参与活动的SKU
	 * @param skuQuery
	 * @param salableFirst  true 有库存商品优先
	 * @return
	 */
	public Map<String,PlusModelSkuInfo> getProductMinPriceSkuInfo(PlusModelSkuQuery skuQuery, boolean salableFirst){
		String pro = skuQuery.getCode();
		Map<String,PlusModelSkuInfo> resultMap = new HashMap<String,PlusModelSkuInfo>();
		
		if(StringUtils.isNotBlank(pro)){
			
			String[] proCode = pro.split(",");
			PlusModelProductInfo productInfo;
			PlusModelSkuInfo skuInfo,tmp;
			for(int j=0;j<proCode.length;j++){
				productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(proCode[j]));
				for(PlusModelProductSkuInfo model : productInfo.getSkuList()){
					skuInfo = getPlusModelSkuInfo(model.getSkuCode(),skuQuery.getMemberCode(),1,skuQuery.getChannelId(),skuQuery.getFxFlag());
					if(skuInfo!=null){
						if(!resultMap.containsKey(productInfo.getProductCode())) {
							resultMap.put(productInfo.getProductCode(), skuInfo);
							continue;
						}
						
						tmp = resultMap.get(productInfo.getProductCode());
						
						// 有库存商品优先的情况下，商品无库存则不再检查价格
						if(salableFirst && (skuInfo.getMaxBuy() <= 0)) {
							continue;
						} 
						
						// 有库存商品优先的情况下，优先取可售的sku价格
						if(salableFirst && tmp.getMaxBuy() <= 0 && skuInfo.getMaxBuy() > 0) {
							resultMap.put(productInfo.getProductCode(), skuInfo);
							continue;
						}
						
						// 比较同个商品下面的SKU最小销售价
						if(skuInfo.getSellPrice().compareTo(tmp.getSellPrice()) < 0){
							// 如果最小销售价未参与活动但其他SKU参与活动则参与活动的SKU优先
							if(StringUtils.isBlank(skuInfo.getEventCode()) && StringUtils.isNotBlank(tmp.getEventCode())) {
								continue;
							}
							
							resultMap.put(productInfo.getProductCode(), skuInfo);
						}
					}
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 获取商品最低价格，如果商品中有拼团sku 的话，最低价格用拼团价格代替，并且新增拼团类型以及销售原价。（5.4.0及以上版本兼容）
	 * @param skuQuery
	 * @return
	 */
	public Map<String,PlusModelSkuInfo> getProductMinPriceIncloudGroupPrice(PlusModelSkuQuery skuQuery){
		String pro = skuQuery.getCode();
		Map<String,PlusModelSkuInfo> resultMap = new HashMap<String,PlusModelSkuInfo>();
		
		if(StringUtils.isNotBlank(pro)){
					
			String[] proCode = pro.split(",");
			for(int j=0;j<proCode.length;j++){
				PlusModelSkuInfo skuResult = new PlusModelSkuInfo();
				PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(proCode[j]));
				List<PlusModelProductSkuInfo> list = productInfo.getSkuList();
				List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
				Map<String,BigDecimal> skuPrices = new HashMap<String,BigDecimal>();
				List<BigDecimal> groupBuyingPrices = new ArrayList<BigDecimal>();
				for(PlusModelProductSkuInfo model : list ){
					//PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(model.getSkuCode(),skuQuery.getMemberCode(),"",1);
					PlusModelSkuInfo skuInfo = getPlusModelSkuInfo(model.getSkuCode(),skuQuery.getMemberCode(),1,skuQuery.getChannelId(),skuQuery.getFxFlag());
					if(skuInfo!=null){
						listPrice.add(skuInfo.getSellPrice());
						//SKU是否参与拼团活动，如果参与，需要返回拼团标识以及该sku的实际销售价格 4497472600010024为拼团。
						String eventType = skuInfo.getEventType();
						if("4497472600010024".equals(eventType)){
							skuResult.setEventType("4497472600010024");
//							skuResult.setSkuPrice(skuInfo.getSkuPrice());//不参与任何活动的价格
//							skuResult.setGroupBuyingPrice(skuInfo.getSellPrice());//当前销售价（拼团价）
							skuResult.setEventCode(skuInfo.getEventCode());
						}else if(StringUtils.isNotBlank(eventType)){
							skuResult.setEventType(eventType);
						}
						skuPrices.put(skuInfo.getSellPrice().setScale(2, RoundingMode.HALF_UP).toString(), skuInfo.getSkuPrice());
						groupBuyingPrices.add(skuInfo.getSellPrice().setScale(2, RoundingMode.HALF_UP));
					}
					
				}
				Collections.sort(listPrice);
				//如果非拼团商品设置最小价格，用于非拼团商品的价格显示
				skuResult.setSellPrice(listPrice.size()<=0? productInfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP) :listPrice.get(0).setScale(2, RoundingMode.HALF_UP));
				Collections.sort(groupBuyingPrices);
				if(groupBuyingPrices.size()>0) {
					BigDecimal a = groupBuyingPrices.get(0).setScale(2, RoundingMode.HALF_UP);
					skuResult.setGroupBuyingPrice(a);//当前销售价（拼团价）
					skuResult.setSkuPrice(skuPrices.get(a.toString()));
				}
				skuResult.setDescriptionUrlHref(productInfo.getAdpicUrl());//赋值广告图
				skuResult.setProductCode(productInfo.getProductCode());
				skuResult.setSmallSellerCode(productInfo.getSmallSellerCode());
				skuResult.setDescriptionUrlHref(productInfo.getAdpicUrl());//广告图
				//去掉价格为123456789 的展示 ，展示商品市场价marketPrice  fq++
				resultMap.put(proCode[j], skuResult);
				
			}
		}
		return resultMap;
	}
	
	/**
	 * 优先取本地缓存，临时解决批量调用时总耗时较长的问题
	 * @param skuCode
	 * @param memberCode
	 * @param isPurchase
	 * @param fxFlag 
	 * @return
	 */
	private PlusModelSkuInfo getPlusModelSkuInfo(String skuCode, String memberCode, Integer isPurchase,String channelId, String fxFlag) {
		if(StringUtils.isBlank(skuCode)) return null;
		
		String level = "";
		if(StringUtils.isNotBlank(memberCode)) {
			level = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode)).getLevel();
		}
		
		if(StringUtils.isBlank(level)) {
			level = XmasSystemConst.CUST_LVL_CD;
		}
		
		String cacheKey = skuCode+"-"+level+"-"+isPurchase+"-"+channelId;
		
		PlusModelSkuInfo skuInfo = simpleCache.get(cacheKey);
		if(skuInfo == null) {
			PlusModelSkuQuery pq = new PlusModelSkuQuery();
			pq.setFxFlag(fxFlag);
			pq.setCode(skuCode);
			pq.setMemberCode(memberCode);
			pq.setIsPurchase(isPurchase);
			pq.setChannelId(channelId);
			skuInfo = plusSupportProduct.upSkuInfo(pq).getSkus().get(0);
			
			if(skuInfo != null) {
				simpleCache.put(cacheKey, skuInfo);
			}
		}
		return skuInfo;
	}
	
	public PlusModelSkuInfo getPlusModelSkuInfoForPublic(String skuCode, String memberCode, Integer isPurchase,String channelId, String fxFlag) {
		return getPlusModelSkuInfo( skuCode,  memberCode,  isPurchase, channelId,  fxFlag);
	}
}
