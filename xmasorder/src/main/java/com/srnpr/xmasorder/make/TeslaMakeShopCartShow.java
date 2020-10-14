package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.ShoppingCartActivity;
import com.srnpr.xmasorder.model.ShoppingCartEvent;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfo;
import com.srnpr.xmasorder.model.ShoppingCartItem;
import com.srnpr.xmasorder.model.ShoppingCartPropertyinfo;
import com.srnpr.xmasorder.model.ShoppingCartShow;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadEventFree;
import com.srnpr.xmassystem.load.LoadEventOnlinePay;
import com.srnpr.xmassystem.load.LoadEventSale;
import com.srnpr.xmassystem.load.LoadGiftSkuInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelGiftSkuinfo;
import com.srnpr.xmassystem.modelproduct.PlusModelGitfSkuInfoList;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
/**
 * 促销计算(获取商品的最新价格)
 * 
 * @author xiegj
 *
 */
public class TeslaMakeShopCartShow extends TeslaTopOrderMake {

	// 满减要求件数的类型
	static String[] fullCutTypeSkuNum = {"449747630004","449747630006","449747630007"};
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		ShoppingCartShow show = new ShoppingCartShow();
		Map<String,BigDecimal> derateMoneyFlagMap = new HashMap<String, BigDecimal>(); // 计算满折的优惠金额
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {//循环所有商品
			TeslaModelShowGoods good = teslaOrder.getShowGoods().get(i);
			if(StringUtils.endsWithAny(good.getEventType(), "4497472600010008")){//需要分组的活动类型
				boolean addFlag = true;//是否新活动分组
				for (int j = 0; j < show.getShoppingCartList().size(); j++) {
					if(show.getShoppingCartList().get(j).getEventCode().equals(good.getItem_code())
							&&"4497153900060002".equals(good.getProductStatus())){
						addFlag=false;
						ShoppingCartItem item = getItem( show, show.getShoppingCartList().get(j), good,teslaOrder.getChannelId(),derateMoneyFlagMap ,teslaOrder);
						if(item != null){
							show.getShoppingCartList().set(j, item);
						}
					}
				}
				if(addFlag){
					ShoppingCartItem item = getItem( show, new ShoppingCartItem(), good,teslaOrder.getChannelId(), derateMoneyFlagMap ,teslaOrder);
					if(item!=null){
						show.getShoppingCartList().add(item);
					}
				}
			}else {//除满减外不需要分组的商品
				ShoppingCartItem item = getItem( show, new ShoppingCartItem(), good,teslaOrder.getChannelId(), null , teslaOrder);
				if(item!=null){
					show.getShoppingCartList().add(item);
				}
			}
		}
		
		ShoppingCartItem item;
		for (int i = 0; i < show.getShoppingCartList().size(); i++) {
			item = show.getShoppingCartList().get(i);
			// 优惠金额兼容plus折扣
			show.setAllDerateMoney(show.getAllDerateMoney()+item.getDerateMoney()+item.getPlusMoney().doubleValue());
			show.setAllNormalMoney(show.getAllNormalMoney()+item.getPayMoney());
			show.setAllPayMoney(BigDecimal.valueOf(show.getAllNormalMoney()).subtract(BigDecimal.valueOf(show.getAllDerateMoney())).doubleValue());
			for (int j = 0; j < show.getShoppingCartList().get(i).getGoods().size(); j++) {
				ShoppingCartGoodsInfo info = show.getShoppingCartList().get(i).getGoods().get(j);
				show.setAcount_num(show.getAcount_num()+info.getSku_num());
				if("0".equals(info.getFlag_product())){
					continue;
				}
				show.setChooseGoodsNum(show.getChooseGoodsNum()+("1".equals(info.getChooseFlag())?info.getSku_num():0));
			}
		}
		for (int i = 0; i < show.getDisableGoods().size(); i++) {
			show.setDisable_account_num(show.getDisable_account_num()+("1".equals(show.getDisableGoods().get(i).getFlag_product())?0:show.getDisableGoods().get(i).getSku_num()));
			show.setDisableSku(show.getDisableSku()+("1".equals(show.getDisableGoods().get(i).getFlag_product())?0:1));
		}
		show.setAcount_num(show.getAcount_num()+show.getDisable_account_num());
		show.setSalesAdv(bConfig("xmasorder.first_title_shopcart"));
		/* 
		 * 处理小数问题
		 */
		show.setAllDerateMoney(BigDecimal.valueOf(show.getAllDerateMoney()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		show.setAllNormalMoney(BigDecimal.valueOf(show.getAllNormalMoney()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		show.setAllPayMoney(BigDecimal.valueOf(show.getAllPayMoney()).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		teslaOrder.setCartShow(show);
		return result;
	}
	private ShoppingCartItem getItem(ShoppingCartShow show,ShoppingCartItem item,TeslaModelShowGoods good,String channelId,Map<String,BigDecimal> derateMoneyFlagMap ,TeslaXOrder teslaOrder){
		String sellerCode=teslaOrder.getUorderInfo().getSellerCode();
		ShoppingCartGoodsInfo info = new ShoppingCartGoodsInfo();
		info.setOrder_code(good.getOrderCode());
		info.setProduct_code(good.getProductCode());
		info.setSku_code(good.getSkuCode());
		info.setPic_url(good.getProductPicUrl());
		info.setSku_name(good.getSkuName());
		info.setSku_property(reProperties(good.getSkuCode(), good.getSku_keyValue()));
		info.setSku_price(good.getSkuPrice().doubleValue());
		info.setSku_num(good.getSkuNum());
		info.setLimit_order_num(good.getLimit_order_num());
		info.setSku_stock(new PlusSupportStock().upAllStock(good.getSkuCode()));
		info.setLabelsList(good.getLabelsList());
		info.setLabelsPic(good.getLabelsPic());
		info.setIsSkuPriceToBuy(good.getIsSkuPriceToBuy());
		
		// 如果是分销商品再设置分销人
		if(good.getFxFlag() == 1) {
			info.setFxFlag(good.getFxFlag());
			info.setFxrcode(good.getFxrcode());
		}
		
		info.setTgzUserCode(good.getTgzUserCode());
		info.setTgzShowCode(good.getTgzShowCode());
		
		List<TeslaModelOrderActivity> activityList = teslaOrder.getActivityList();
		
		ShoppingCartActivity activity = new ShoppingCartActivity();
		if(!StringUtils.endsWithAny(good.getEventType(), "4497472600010008")){
			activity.setActivity_info(good.getActivity_name());
			activity.setActivity_name(good.getActivity_name());
			info.getActivitys().add(activity);
		} else {
			//5.4.2前规则：如果满减类型是  449747630008  时，如果还参加特价，则添加特价标签
		for (TeslaModelOrderActivity a : activityList) {
					String activityType = a.getActivityType();
					/*
					 * 支持 扫码购-满减 或 特价-满减   活动的并存 (此满减为 特殊满减  ，满减类型为：每满X减Y-LD多重促销活动_449747630008)
					 * 4497472600010002 : 特价
					 * 4497472600010004 : 扫码购-微信
					 * 4497472600010015 : 扫码购-APP
					 */
					//if(a.getSkuCode().equals(good.getSkuCode()) && (activityType.equals("4497472600010002") || activityType.equals("4497472600010004") || activityType.equals("4497472600010015") )) {
					/*-------------------------------------------*/
					
					//5.4.2新规则：判断满减活动类型是否绑定了叠加活动类型,查询改产品所参见的满减活动
					
					if(a.getSkuCode().equals(good.getSkuCode()) &&this.getFullCutFieldOfSuprapositionType(good.getProductCode(), activityType, sellerCode)) {
						activity.setActivity_info(a.getActivityName());
						activity.setActivity_name(a.getActivityName());
						info.getActivitys().add(activity);
						break;
					}
				}
			
		
			
		}
		
		// 计算被勾选的商品plus折扣总优惠金额
		if("1".equals(good.getChoose_flag())) {
			for (TeslaModelOrderActivity a : activityList) {
				String activityType = a.getActivityType();
				if(a.getSkuCode().equals(good.getSkuCode()) 
						&& activityType.equals("4497472600010026")
						&& a.getOrderCode().equals(good.getOrderCode())) {
					item.setPlusMoney(item.getPlusMoney().add(a.getPreferentialMoney().multiply(new BigDecimal(good.getSkuNum()))));
					break;
				}
			}
		}
			
		PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
		plusModelQuery.setCode(good.getProductCode());
		
		
		PlusModelGitfSkuInfoList pmgsList =  new TeslaMakeGiftSkuInfo().getGiftByChannId(plusModelQuery,channelId);
		
		for(PlusModelGiftSkuinfo p: pmgsList.getGiftSkuinfos()){
			info.getOtherShow().add("赠品");break;
			
		}
			
		
		// 赠品信息                //zhouguohui   20160711   修改购物车赠品bug
//		PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
//		plusModelQuery.setCode(good.getProductCode());
//		PlusModelGitfSkuInfoList plusModelGitfSkuInfoList = new LoadGiftSkuInfo()
//				.upInfoByCode(plusModelQuery);
//		if (plusModelGitfSkuInfoList.getGiftSkuinfos() != null && !plusModelGitfSkuInfoList.getGiftSkuinfos().isEmpty()) {
//			info.getOtherShow().add("赠品");
//		}
		info.setFlag_stock(info.getSku_num()>info.getSku_stock()?"0":"1");

		//add by zht
		//商品上架并且当前sku售卖状态为Y或售卖状态为空时,则saleStatus为1;sku售卖状态为N时,saleStatus为0. 
		//sale_yn为空时默认可售,是为了兼容上线,因为线上xs-Sku-xx无sale_yn字段,升级会产生两种
		//情况1:线上删除所有sku缓存,但后台LoadSkuInfo未更新,立即会有新缓存加载,还是没有sale_yn;
		//情况2:若先升级后台接口且sale_yn为空时默认不可售, 则所有用户的购物车内的商品都失效;
		String saleStatus = "4497153900060002".equals(good.getProductStatus()) 
				&& (StringUtils.isBlank(good.getSaleYn()) || good.getSaleYn().equals("Y")) ? "1" : "0";
		info.setFlag_product(saleStatus);
		
		info.setMini_order(Long.valueOf(good.getMiniOrder()).intValue());
		info.setChooseFlag(good.getChoose_flag());
//		info.setFlagTheSea("SF03KJT".equals(good.getSmallSellerCode())||"SF03MLG".equals(good.getSmallSellerCode())
//				||"SF03100294".equals(good.getSmallSellerCode())
//				||"SF03100327".equals(good.getSmallSellerCode())
//				||"SF03100329".equals(good.getSmallSellerCode())?"1":"0");
		info.setFlagTheSea(new PlusServiceSeller().isKJSeller(good.getSmallSellerCode())?"1":"0");
		
		if("0".equals(info.getFlag_product())){
			show.getDisableGoods().add(info);
			return null;
		}
		item.setChooseFlag("1".equals(item.getChooseFlag())||"1".equals(good.getChoose_flag())?"1":"0");
		item.setEventCode(StringUtils.isBlank(good.getEventCode())?(StringUtils.isBlank(good.getItem_code())?"":good.getItem_code()):good.getEventCode());//类型满减和组合购的采用分组显示
		if(StringUtils.endsWithAny(good.getEventType(), "4497472600010008")){
			item.getEvent().setDescription(good.getFullMoneys().getAdvTitle());
			if("449747630005".equals(good.getFullType())) {//满减类型为n元任选几件时
				item.getEvent().setTagname("N元任选");
				//item.getEvent().setEventNameColor(bConfig("xmassystem.enentNameNColor"));
			} else {
				item.getEvent().setTagname(good.getActivity_name());
			}
			
			// 5.1.2版本调整颜色
			item.getEvent().setEventNameColor("#FF5050");
			
			//添加活动跳转类型  fq  活动跳转
			if(StringUtils.isNotBlank(good.getActivityUrl())) {
				//跳转活动专题页
				item.getEvent().setForwardType("100002");
				item.getEvent().setForwardVal(good.getActivityUrl());
			} else {
				//跳转原生活动页(与4.0.6版本之前的跳转方式一致)
				item.getEvent().setForwardType("100001");
				item.getEvent().setForwardVal(item.getEventCode());
			}
			boolean isChangePrice = true;
			for (TeslaModelOrderActivity activity2 : activityList) {
				if((activity2.getActivityType().equals("4497472600010002")||activity2.getActivityType().equals("4497472600010001")||activity2.getActivityType().equals("4497472600010004")
						||activity2.getActivityType().equals("4497472600010005")||activity2.getActivityType().equals("4497472600010018")||activity2.getActivityType().equals("4497472600010024")) && good.getSkuCode() .equals( activity2.getSkuCode())) {
					isChangePrice = false;
					break;
				}
			}
			
			if(isChangePrice) {//如果满减活动为（每满X减Y-LD多重促销活动 ）  ，则支持特价商品的价格
				info.setSku_price(good.getOrig_sku_price().doubleValue());
			}
			
			item.setStart_time(good.getStart_time());
			item.setEnd_time(good.getEnd_time());
			
			if(ArrayUtils.contains(fullCutTypeSkuNum, good.getFullMoneys().getFullCutType())) {
				item.getEvent().setAddFullMoneyType(ShoppingCartEvent.FULL_MONEY_TYPE_SKUNUM);
			} else {
				item.getEvent().setAddFullMoneyType(ShoppingCartEvent.FULL_MONEY_TYPE_MONEY);
			}
		}
		
		if(good.isIs_activity()&&"1".equals(good.getChoose_flag())){
			if("449747630006".equals(good.getFullType())){
				// 累加同一个满折活动（449747630006）下不同商品的满减金额
				if(!derateMoneyFlagMap.containsKey(good.getProductCode())){
					item.setDerateMoney(item.getDerateMoney() + good.getFullMoneys().getCutMoney().doubleValue());
					derateMoneyFlagMap.put(good.getProductCode(), good.getFullMoneys().getCutMoney());
				}
			}else{
				item.setDerateMoney(good.getFullMoneys().getCutMoney().doubleValue());
			}
			
		}
		//item.setDerateMoney(good.isIs_activity()&&"1".equals(good.getChoose_flag())&&BigDecimal.valueOf(item.getDerateMoney()).compareTo(BigDecimal.ZERO)==0
		//		?good.getFullMoneys().getCutMoney().doubleValue():item.getDerateMoney());
		
		item.setType(good.getEventType());
		item.setShowChoose(StringUtils.endsWithAny(good.getEventType(), "4497472600010008")?"1":"0");
		item.setPayMoney("1".equals(good.getChoose_flag())?
				BigDecimal.valueOf(item.getPayMoney()).add(
				BigDecimal.valueOf(info.getSku_price()).multiply(BigDecimal.valueOf(info.getSku_num()))).doubleValue()
						:item.getPayMoney());
		//添加是否去凑单标识
		if(good.isIs_activity()) {
			//如果已经参与了活动，则不去凑单
			item.setFlagAddOrder("10001");
		}
		item.getGoods().add(info);
		//计算差额
		if(item.getEvent().getAddFullMoneyType() == 1) {//计算差额
			BigDecimal fullMoney = good.getFullMoneys().getFullMoney();
			item.getEvent().setAddFullMoney(MoneyHelper.format(fullMoney.subtract(new BigDecimal(item.getPayMoney()))));
		}else {
			String fullCutCode = good.getFullMoneys().getFullCutCode();
			String sqlFullPrice = "SELECT * FROM systemcenter.sc_full_cut_price WHERE full_cut_code = '"+fullCutCode+"' ORDER BY full_price ASC LIMIT 1";
			Map<String,Object> fullCutInfo = DbUp.upTable("sc_full_cut_price").dataSqlOne(sqlFullPrice, new MDataMap());
			Integer fullNum = 0;
			if(fullCutInfo != null) {
				fullNum = MapUtils.getInteger(fullCutInfo, "full_price", 0);
			}
			Integer skuNum = 0;
			for(ShoppingCartGoodsInfo goodInfo : item.getGoods()) {
				if("1".equals(goodInfo.getChooseFlag())) {//选中
					skuNum += goodInfo.getSku_num();
				}
			}
			item.getEvent().setAddFullMoney((fullNum - skuNum)+"");
		}
		return item;
	}
	
	
	
	private boolean getFullCutFieldOfSuprapositionType(String productCode,String eventType ,String sellerCode) {
		// TODO Auto-generated method stub
		PlusModelEventFull result = null;
		PlusModelSaleQuery tQuery = new PlusModelSaleQuery();
		tQuery.setCode(sellerCode);
		
		PlusModelEventSale eventSale = new LoadEventSale().upInfoByCode(tQuery);
		
		if(eventSale.getEventFulls() != null && eventSale.getEventFulls().size() > 0) {
			String sysFormat = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
			SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
			String now = sysDateTime.format(new Date()).toString();
			
			List<PlusModelEventFull> delList = new LinkedList<PlusModelEventFull>();
			for(int i = eventSale.getEventFulls().size() - 1; i >= 0; --i) {
				PlusModelEventFull currentEvent = eventSale.getEventFulls().get(i);
				//活动已结束或活动还未开始
				if(DateUtil.compareDateTime(currentEvent.getEndTime(),now)  || DateUtil.compareDateTime(now,currentEvent.getBeginTime()) ) {
					delList.add(currentEvent);
				}
				if(StringUtils.isBlank(currentEvent.getSuprapositionType())||!currentEvent.getSuprapositionType().contains(eventType)) {
					delList.add(currentEvent);
				}
				
			}
			eventSale.getEventFulls().removeAll(delList);

			if(!eventSale.getEventFulls().isEmpty()) {
				for(PlusModelEventFull eventFull : eventSale.getEventFulls()) {
					String limitType = eventFull.getRuleSku().getLimitType();
					List<String> limitcodes = eventFull.getRuleSku().getLimitCode();
					if(limitType.equals("4497476400020001")) {
						//不限
						result = eventFull;
					} else if(limitType.equals("4497476400020002")) {
						//仅包含,双重break,优先取
						boolean found = false;
						if(limitcodes != null) {
							for(String code : limitcodes) {
								if(productCode.equals(code)) {
									result = eventFull;
									found = true;
									break;
								}
							}
						}
						if(found) break;
					} else if(limitType.equals("4497476400020003")) {
						//以下除外
						boolean found = false;
						if(limitcodes != null) {
							for(String code : limitcodes) {
								if(productCode.equals(code)) {
									found = true;
									break;
								}
							}
						}
						if(!found) {
							result = eventFull;
						}
					}
				}
			}
			
		}
		return result==null?false:true;
		
	}
	/**
	 * 解析skuValue
	 */
	private List<ShoppingCartPropertyinfo> reProperties(String skuCode,
			String skuValue) {
		List<ShoppingCartPropertyinfo> piffList = new ArrayList<ShoppingCartPropertyinfo>();
		if (skuValue == null || "".equals(skuValue)) {
			return piffList;
		}
		String[] pro = skuValue.split("&");
		for (int j = 0; j < pro.length; j++) {
			ShoppingCartPropertyinfo piff = new ShoppingCartPropertyinfo();
			String[] va = pro[j].split("=");
			piff.setSku_code(skuCode);
			piff.setPropertyKey(va[0]);
			piff.setPropertyValue(va[1]);
			piffList.add(piff);
		}
		return piffList;
	}
}

