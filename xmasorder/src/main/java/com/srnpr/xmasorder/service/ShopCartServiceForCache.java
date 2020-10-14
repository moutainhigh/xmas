package com.srnpr.xmasorder.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.ShoppingCartCache;
import com.srnpr.xmasorder.model.ShoppingCartCacheInfo;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.xmasorder.model.TeslaModelJJG;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * 项目名称：familyhas 类名称：ShopCartServiceForCache 
 * @author xiegj
 * 创建时间：2015-11-17 上午11:03:25
 * @version 2.0
 * 
 */
public class ShopCartServiceForCache extends BaseClass {
	
	
	/**
	 *保存商品到购物车 
	 */
	public void saveShopCart(List<ShoppingCartGoodsInfoForAdd> goodsList,String memberCode){
		List<ShoppingCartCache> caches = new ArrayList<ShoppingCartCache>();
		Map<String, ShoppingCartCache> sort = new HashMap<String, ShoppingCartCache>();
		if(goodsList!=null&&!goodsList.isEmpty()){
			ShoppingCartCacheInfo info = queryShopCart(memberCode,new ArrayList<ShoppingCartGoodsInfoForAdd>());
			Map<String, String> exists = new HashMap<String, String>();
			for (int i = 0; i < info.getCaches().size(); i++) {
				for (int j = 0; j < goodsList.size(); j++) {
					ShoppingCartGoodsInfoForAdd good = goodsList.get(j);
					//校验商品合法性,不存在的商品清楚掉
					String productCode = good.getProduct_code();
					PlusSupportProduct psp = new PlusSupportProduct();
					PlusModelProductInfo pInfo = psp.upProductInfo(productCode);
					if(pInfo == null || StringUtils.isEmpty(pInfo.getProductCode())) {
						continue;
					}
					//添加是否加价购商品添加的判断
					if(!StringUtils.equals("", good.getJjgEventCode())) {
						//加价购活动商品添加
						addJJG(info,good);	
					}else {
						if(good.getSku_code().equals(info.getCaches().get(i).getSku_code())&&good.getSku_num()>0){//购物车包含此商品
							//已存在，更新操作
							info.getCaches().get(i).setSku_num(good.getSku_num());
							info.getCaches().get(i).setChoose_flag(good.getChooseFlag());
							
							// 如果有新的推广人则更新
							if(StringUtils.isNotBlank(good.getShareCode())) {
								resetCode(info.getCaches().get(i));
								info.getCaches().get(i).setShareCode(good.getShareCode());
							}
							// 如果有新的推广赚推广人则更新
							if(StringUtils.isNotBlank(good.getTgzUserCode())) {
								resetCode(info.getCaches().get(i));
								info.getCaches().get(i).setTgzUserCode(good.getTgzUserCode());
							}
							// 如果有新的买家秀则更新
							if(StringUtils.isNotBlank(good.getTgzShowCode())) {
								resetCode(info.getCaches().get(i));
								info.getCaches().get(i).setTgzShowCode(good.getTgzShowCode());
							}
						}else if(good.getSku_code().equals(info.getCaches().get(i).getSku_code())&&good.getSku_num()<=0){
							info.getCaches().get(i).setSku_num(0);
							info.getCaches().get(i).setFxrcode("");
						}
					}
				}
			}
			for (int i = 0; i < info.getCaches().size(); i++) {
				if(info.getCaches().get(i).getSku_num()>0){
					exists.put(info.getCaches().get(i).getSku_code(), "");
					// 忽略IC开头的编号加入购物车，防止购物车商品重复
					if(!info.getCaches().get(i).getSku_code().startsWith("IC")){
						sort.put(info.getCaches().get(i).getCreate_time(), info.getCaches().get(i));
					}
				}
			}
			for(int j = goodsList.size()-1; j >=0; j--){
				ShoppingCartGoodsInfoForAdd good = goodsList.get(j);
				if("".equals(good.getJjgEventCode())) {
					if(good.getSku_num()>0&&!exists.containsKey(good.getSku_code())){
						ShoppingCartCache ca = new ShoppingCartCache();
						ca.setChoose_flag(good.getChooseFlag());
						ca.setMember_code(memberCode);
						ca.setSku_code(good.getSku_code());
						ca.setProduct_code(good.getProduct_code());
						ca.setSku_num(good.getSku_num());
						ca.setFxrcode(good.getFxrcode());
						ca.setShareCode(good.getShareCode());
						ca.setTgzShowCode(good.getTgzShowCode());
						ca.setTgzUserCode(good.getTgzUserCode());
						//保存商品加入购物车时的价格，（为展示降价提醒功能）
						ca.setSku_add_shop_price(getSkuPriceBySkuCode(good.getSku_code(),memberCode,1,good.getFlg()));
						
						exists.put(good.getSku_code(), "");
						
						// 忽略IC开头的编号加入购物车，防止购物车商品重复
						if(!good.getSku_code().startsWith("IC")){
							for (int k = 0; k <=99; k++) {
								if(ca.getCreate_time().length()==20){
									ca.setCreate_time(ca.getCreate_time().substring(0, 19));
								}
								if(!sort.containsKey(ca.getCreate_time()+k)){
									ca.setCreate_time(ca.getCreate_time()+k);
									sort.put(ca.getCreate_time(), ca);
									break;
								}
							}
						}
						
						onShopCartAdd(memberCode, good);
					}
				}
			}
			
			Object[] keys =  sort.keySet().toArray();    
			Arrays.sort(keys);    
			for(int i = keys.length-1;i>=0;i--)  
			{    
			    caches.add(sort.get(keys[i]));    
			}
			info.setCaches(caches);
			XmasKv.upFactory(EKvSchema.ShopCart).set(memberCode, GsonHelper.toJson(info));
		}
	}
	
	/**
	 * 清理已经存在的：分享人、推广赚、买家秀
	 * @param item
	 */
	private void resetCode(ShoppingCartCache item) {
		item.setShareCode("");
		item.setTgzShowCode("");
		item.setTgzUserCode("");
	}
	
	// 记录购物车添加记录
	private void onShopCartAdd(String memberCode,ShoppingCartGoodsInfoForAdd ca) {
		DbUp.upTable("lc_shoptcart_add_log").dataInsert(new MDataMap(
					"member_code", memberCode,
					"product_code", ca.getProduct_code(),
					"sku_code", ca.getSku_code(),
					"create_time", FormatHelper.upDateTime()
				));
	}
	
	private void addJJG(ShoppingCartCacheInfo info,ShoppingCartGoodsInfoForAdd good) {
		// TODO Auto-generated method stub
		List<TeslaModelJJG> jjgList = info.getJJGList();
		String jjgEventCode = good.getJjgEventCode();
		//不存在加价购商品直接添加
		if(jjgList.size()==0&&!"".endsWith(good.getSku_code())) {
			TeslaModelJJG teslaModelJJG = new TeslaModelJJG();
			teslaModelJJG.setEventCode(jjgEventCode);
			teslaModelJJG.setSkuCodes(good.getSku_code());
			jjgList.add(teslaModelJJG);
		}else {
			//存在加价购商品
			int index = -1;
			for (int i=0;i< jjgList.size();i++) {
				String eventCode = jjgList.get(i).getEventCode();
				if(StringUtils.equals(eventCode, jjgEventCode)) {
					//已经保有该加价购活动,则清理之后再添加最新的
                   index = i;
                   break;
				}
			}
			if(index!=-1) {jjgList.remove(index);}
			if(!"".equals(good.getSku_code())) {
				TeslaModelJJG teslaModelJJG = new TeslaModelJJG();
				teslaModelJJG.setEventCode(jjgEventCode);
				teslaModelJJG.setSkuCodes(good.getSku_code());
				jjgList.add(teslaModelJJG);
			}
		}
		info.setJJGList(jjgList);
	}


	/**
	 *查询购物车商品 
	 */
	public ShoppingCartCacheInfo queryShopCart(String memberCode,List<ShoppingCartGoodsInfoForAdd> goodsList){
		ShoppingCartCacheInfo info = new ShoppingCartCacheInfo();
		if(StringUtils.isNotBlank(memberCode)){
			String json = XmasKv.upFactory(EKvSchema.ShopCart).get(memberCode);
			info = new GsonHelper().fromJson(json, new ShoppingCartCacheInfo());
		}else {
			if(goodsList!=null&&!goodsList.isEmpty()){
				for (int i = 0; i < goodsList.size(); i++) {
					if(goodsList.get(i).getSku_num()>0){
					ShoppingCartCache cache = new ShoppingCartCache();
						cache.setChoose_flag(goodsList.get(i).getChooseFlag());
						cache.setSku_code(goodsList.get(i).getSku_code());
						cache.setProduct_code(goodsList.get(i).getProduct_code());
						cache.setSku_num(goodsList.get(i).getSku_num());
						cache.setIsSkuPriceToBuy(goodsList.get(i).getFlg());
						cache.setTgzUserCode(goodsList.get(i).getTgzUserCode());
						cache.setTgzShowCode(goodsList.get(i).getTgzShowCode());
						info.getCaches().add(cache);
					}
				}
			}
		}
		
		return info==null?new ShoppingCartCacheInfo():info;
	}
	
	
	/**
	 *查询购物中加价购商品编号
	 */
	public String queryJJGForShopCart(String memberCode,String eventCode){
		List<ShoppingCartGoodsInfoForAdd> goodsList = new ArrayList<ShoppingCartGoodsInfoForAdd>();
		ShoppingCartCacheInfo info = new ShoppingCartCacheInfo();
		if(StringUtils.isNotBlank(memberCode)){
			String json = XmasKv.upFactory(EKvSchema.ShopCart).get(memberCode);
			info = new GsonHelper().fromJson(json, new ShoppingCartCacheInfo());
		}else {
			if(goodsList!=null&&!goodsList.isEmpty()){
				for (int i = 0; i < goodsList.size(); i++) {
					if(goodsList.get(i).getSku_num()>0){
					ShoppingCartCache cache = new ShoppingCartCache();
						cache.setChoose_flag(goodsList.get(i).getChooseFlag());
						cache.setSku_code(goodsList.get(i).getSku_code());
						cache.setProduct_code(goodsList.get(i).getProduct_code());
						cache.setSku_num(goodsList.get(i).getSku_num());
						cache.setIsSkuPriceToBuy(goodsList.get(i).getFlg());
						info.getCaches().add(cache);
					}
				}
			}
		}
		List<TeslaModelJJG> jjgList = info.getJJGList();
		if(jjgList!=null&&jjgList.size()>0) {
			for (TeslaModelJJG teslaModelJJG : jjgList) {
				if(eventCode.equals(teslaModelJJG.getEventCode())) {
					return teslaModelJJG.getSkuCodes();
				}
			}
		}
		return "";
	}
	
	/**
	 *清除购物车中的加价购添品
	 */
	public void clearJJGForShopCart(String memberCode){
		ShoppingCartCacheInfo info = new ShoppingCartCacheInfo();
		if(StringUtils.isNotBlank(memberCode)){
			String json = XmasKv.upFactory(EKvSchema.ShopCart).get(memberCode);
			info = new GsonHelper().fromJson(json, new ShoppingCartCacheInfo());
			info.setJJGList(new  ArrayList<TeslaModelJJG>());
			XmasKv.upFactory(EKvSchema.ShopCart).set(memberCode, GsonHelper.toJson(info));
		}
		
	}
	
	public static void main(String[] args) {
		String str = "8016447956 8016460705 8016451674 8016448222 8016466201 8016468215 8016457840 8016466402 8016463513 8016466685 8016445532";
		Map<String, Integer> counts = new HashMap<String, Integer>();
		ShopCartServiceForCache ss = new ShopCartServiceForCache();
		List<Map<String, Object>> list = DbUp.upTable("mc_login_info").dataSqlList("select member_code from mc_login_info where flag_enable=1", new MDataMap());
		for(Map<String, Object> map : list) {
			String memberCode = null == map.get("member_code") ? "" : map.get("member_code").toString();
			if(StringUtils.isNotBlank(memberCode)) {
				ShoppingCartCacheInfo info = ss.queryShopCart(memberCode, null);
				List<ShoppingCartCache> cacheList = info.getCaches();
				if(null != cacheList && cacheList.size() > 0) {
					for(ShoppingCartCache ci : cacheList) {
						if(StringUtils.isNotBlank(ci.getProduct_code())) {
							if(str.contains(ci.getProduct_code()) && ci.getProduct_code().length()==10) {
								Integer in = counts.get(ci.getProduct_code());
								if(null == in) {
									in = new Integer(1);
								} else {
									in = new Integer(in.intValue() + 1);
								}
								counts.put(ci.getProduct_code(), in);
							}
						}
					}
				}
			}
		}
		
		System.out.println(counts.toString());
	}
	
	/**
	 * 获取sku价格
	 * @param skuCode
	 * @param membercode
	 * @param isPurchase
	 * @param isSkuPriceToBuy  是否原价购买
	 * @return
	 */
	private BigDecimal getSkuPriceBySkuCode(String skuCode,String membercode,Integer isPurchase,String isSkuPriceToBuy) {
		//保存商品加入购物车时的价格，（为展示降价提醒功能）
		// 查询商品信息
		PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct()
				.upSkuInfoBySkuCode(skuCode, membercode,membercode,isPurchase);//内购
		if("1".equals(isSkuPriceToBuy)) {
			return plusModelSkuInfo.getSkuPrice();
		} else {
			return plusModelSkuInfo.getSellPrice();
		}
	}
		
	
}
