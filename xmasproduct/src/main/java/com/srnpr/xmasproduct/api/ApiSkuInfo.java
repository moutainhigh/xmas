package com.srnpr.xmasproduct.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmasproduct.model.CollagePersonInfo;
import com.srnpr.xmasproduct.model.CykDiscountInfo;
import com.srnpr.xmasproduct.model.ProductActivity;
import com.srnpr.xmasproduct.model.SkuInfos;
import com.srnpr.xmasproduct.paygateway.model.RequestModel;
import com.srnpr.xmasproduct.paygateway.support.CallPaymentGatewaySupport;
import com.srnpr.xmassystem.enumer.Channel;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.load.LoadPayTypeInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelbean.PayTypeInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.modelevent.PlusModelPayTypeInfo;
import com.srnpr.xmassystem.modelevent.PlusModelPayTypeInfoQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.service.HjycoinService;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.xmassystem.service.PlusServiceEventPlus;
import com.srnpr.xmassystem.service.PlusServiceSale;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportFenxiao;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webapi.VersionEntity;

public class ApiSkuInfo extends RootApiForVersion<SkuInfos, PlusModelSkuQuery> {

	PlusServiceAccm accm = new PlusServiceAccm();
	
	HjycoinService hjycoinService = new HjycoinService();
	
	static PlusServiceEventPlus plusServiceEventPlus = new PlusServiceEventPlus();
	static PlusSupportFenxiao plusSupportFenxiao = new PlusSupportFenxiao();
	
	public SkuInfos Process(PlusModelSkuQuery inputParam, MDataMap mRequestMap) {
		// 以ApiClient中的渠道编号为准，如果为空则默认APP
		if(StringUtils.isNotBlank(getApiClientValue("channelId"))) {
			inputParam.setChannelId(getChannelId());
		}
		
		/**
		 * 因为有预告的时候，其他SKU是不能购买的，这边选择将IC编号替换成productCode，20190515 edit by zhouenzhi
		 */
		String code = inputParam.getCode();
		String integralFlag = inputParam.getIntegralFlag();
		if(PlusHelperEvent.checkEventItem(inputParam.getCode())) {//IC 开头的
			MDataMap map = DbUp.upTable("sc_event_item_product").one("item_code",code);
			LoadEventInfo load = new LoadEventInfo();
			if(map != null && !map.isEmpty()) {
				String productCode = map.get("product_code");//获取商品编号
				String eventCode = map.get("event_code");
				PlusModelEventQuery tQuery = new PlusModelEventQuery();
				tQuery.setCode(eventCode);
				PlusModelEventInfo eventInfo = load.upInfoByCode(tQuery);
				String beginTime = eventInfo.getBeginTime();
				String eventType = eventInfo.getEventType();
				if("4497472600010001".equals(eventType)||"4497472600010005".equals(eventType)) {//只有秒杀跟闪购的时候才校验是否是预告
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date begin;
					try {
						begin = sdf.parse(beginTime);
						if(begin.after(new Date())) {//开始时间晚于当前时间，说明是预告
							inputParam.setCode(productCode);//将IC编号替换成商品编号
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		SkuInfos skuinfos = oldProcess(inputParam, mRequestMap);
		String version = "";
		if(getApiClient()!=null) {
			version = getApiClient().get("app_vision");
		}
		if(StringUtils.isEmpty(version)) {
			version = "5.4.2";
		}
		if(AppVersionUtils.compareTo(version,"5.4.2")>=0&&!"1".equals(skuinfos.getIfShowFXPrice())) {
			this.setPro(skuinfos,integralFlag,inputParam.getChannelId());
		}
		//处理拼团人数
		this.setCollagePersonCount(skuinfos,version);
		
		if(!skuinfos.getSkus().isEmpty()) {
			// 橙意会员卡商品只支持立即购买，不支持加入购物车
			if(skuinfos.getSkus().get(0).getProductCode().equals(bConfig("xmassystem.plus_product_code"))) {
				skuinfos.setButtonControl(4);
			}
		}
		
		return skuinfos;
	}
	
	/**
	 * @param skuinfos
	 * 5.5.2版本之后，拼团人列表由原来的最多展示两个增加到最多展示10个
	 */
	private void setCollagePersonCount(SkuInfos skuinfos,String version) {
		List<CollagePersonInfo> list = skuinfos.getCollagePersonList();
		List<CollagePersonInfo> newList = new ArrayList<CollagePersonInfo>();
		if(AppVersionUtils.compareTo(version,"5.5.2")>=0) {
			if(list.size()<=10) {
				return;
			}else {
				for(int i = 0;i<10;i++) {
					newList.add(list.get(i));
				}
			}
		}else {
			if(list.size()<=2) {
				return;
			}else {
				for(int i = 0;i<2;i++) {
					newList.add(list.get(i));
				}
			}
		}
		skuinfos.setCollagePersonList(newList);
	}

	/**
	 * 5.4.2新增秒杀、闪购倒计时提醒以及预告提醒相关属性
	 */
	private void setPro(SkuInfos skuInfos,String integralFlag,String channelId) {
		List<PlusModelSkuInfo> skus = skuInfos.getSkus();
		String eventCode = "";
		String productCode = "";
		String eventTime = "";
		String eventTypes = "";
		ProductActivity pa = new ProductActivity();
		if(skus.size()>0) {//有活动需要校验有没有闪购，秒杀活动
			for(PlusModelSkuInfo plusModelSkuInfo:skus) {
				String eventType = plusModelSkuInfo.getEventType();
				eventTypes += eventType;
				String smallSellerCode = plusModelSkuInfo.getSmallSellerCode();
				productCode = plusModelSkuInfo.getProductCode();
				if("SI2003".equals(smallSellerCode)) {
					if(plusModelSkuInfo.getLimitBuy() > 5) {
						plusModelSkuInfo.setLimitBuy(5);
					}
					if(plusModelSkuInfo.getMaxBuy() > 5) {
						plusModelSkuInfo.setMaxBuy(5);
					}
				}
				if(eventType.contains("4497472600010001")) {//秒杀
					//排序，将秒杀放到第一位置
					List<ProductActivity> activitys = skuInfos.getEvents();
					List<ProductActivity> activityList = new ArrayList<ProductActivity>();
					for(ProductActivity ac : activitys) {
						if("4497472600010001".equals(ac.getEventType())) {
							pa = ac;
						}else {
							activityList.add(ac);
						}
					}
					activityList.add(0, pa);
					eventCode = plusModelSkuInfo.getEventCode();
					skuInfos.setEvents(activityList);//替换掉原List
					//如果有秒杀需要有收藏
					if("SI2003".equals(smallSellerCode)) {//LD 商品
						skuInfos.setButtonControl(2);
					}else {
						skuInfos.setButtonControl(1);
					}
					break;
				}
				if(eventType.contains("4497472600010005")) {//闪购
					//排序，将秒杀放到第一位置
					List<ProductActivity> activitys = skuInfos.getEvents();
					List<ProductActivity> activityList = new ArrayList<ProductActivity>();
					for(ProductActivity ac : activitys) {
						if("4497472600010005".equals(ac.getEventType())) {
							pa = ac;
						}else {
							activityList.add(ac);
						}
					}
					activityList.add(0, pa);
					eventCode = plusModelSkuInfo.getEventCode();
					skuInfos.setEvents(activityList);//替换掉原List
					skuInfos.setEvents(activityList);//替换掉原List
					//如果有秒杀需要有收藏
					if("SI2003".equals(smallSellerCode)) {//LD 商品
						skuInfos.setButtonControl(2);
					}else {
						skuInfos.setButtonControl(1);
					}
					break;
				}
			}
		}
		if(eventTypes.contains("4497472600010005")||eventTypes.contains("4497472600010001")) {//有秒杀或是闪购活动的时候
			PlusModelEventInfo eventInfo = new PlusSupportEvent().upEventInfoByCode(eventCode);
			eventTime = eventInfo.getEndTime();
			//根据结束时间算倒计时
			Long nowLong = System.currentTimeMillis();
			Long endLong = 1l;
			try {
				endLong = getToLong(eventTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			skuInfos.setLimitSecond((endLong-nowLong)/1000);
		}else if(this.justHasFullCutAndOnlinePay(eventTypes)&&"0".equals(integralFlag)){//没有其他活动或是只有满减活动时&&并且非积分商城访问时
			//根据商品编号查询是否有秒杀或是闪购信息。
			String sql = "SELECT a.event_name event_name,a.event_code event_code,a.begin_time begin_time ,a.event_type_code event_type_code,a.channels FROM systemcenter.sc_event_info a LEFT JOIN systemcenter.sc_event_item_product b ON a.event_code = b.event_code WHERE b.flag_enable = 1 and  a.begin_time >= sysdate() and b.product_code = '"+productCode+"' AND a.event_status = '4497472700020002' and a.event_type_code in ('4497472600010001','4497472600010005') order by a.begin_time asc";
			List<Map<String,Object>> maps = DbUp.upTable("sc_event_info").dataSqlList(sql, null);
			for(Map<String,Object> map : maps) {
				String eventType = map.get("event_type_code").toString();
				String eventName = map.get("event_name").toString();
				String channels = map.get("channels").toString();
				
				if(StringUtils.isBlank(channels) || channels.contains(channelId)) {
					eventTime = map.get("begin_time").toString();
					ProductActivity activity = new ProductActivity();
					activity.setEventName(eventName);
					activity.setEventType(eventType);
					skuInfos.getEvents().add(0, activity);//将预告放入events
					//设置buyStatus为预告
					skuInfos.setBuyStatus(6);
//					skuInfos.getEvents().add(activity);//将预告放入events
//					if(skuInfos.getEvents().size()==1) {
//						//设置buyStatus为预告
//						skuInfos.setBuyStatus(6);
//					}
					Long nowLong = System.currentTimeMillis();
					Long endLong = 1l;
					try {
						endLong = getToLong(eventTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
					skuInfos.setLimitSecond((endLong-nowLong)/1000);
					skuInfos.setNoticeStartTime(eventTime.substring(5, 16).replace("-", "月").replaceAll(" ", "日 "));
					break;
				}
			}
		}
	}
	
	private boolean justHasFullCutAndOnlinePay(String eventTypeCodes) {
		if(eventTypeCodes.contains("4497472600010001")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010002")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010003")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010004")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010005")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010007")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010009")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010011")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010012")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010014")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010015")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010016")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010006")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010017")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010018")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010019")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010020")) {
			return false;
		}
		if(eventTypeCodes.contains("4497472600010024")) {
			return false;
		}
		return true;
	}
	
	//将字符串时间格式转为long形
	public long getToLong(String DateTime) throws Exception{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date time = sdf.parse(DateTime);
		 Long a = time.getTime();
		 return a;
	 }
	
	/**
	 * 5.4.2之前逻辑
	 * @param inputParam
	 * @param mRequestMap
	 * @return
	 */
	public SkuInfos oldProcess(PlusModelSkuQuery inputParam, MDataMap mRequestMap) {

		SkuInfos skuInfos = new SkuInfos();
		String saleCode=inputParam.getCode();
		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();

		//读取app版本
		/*String appVersion = "";
		try {
			String clientInfo = mRequestMap.get("api_client");
			JSONObject parseClientInfo = JSONObject.parseObject(clientInfo);
			if(null != parseClientInfo) {
				appVersion = String.valueOf(parseClientInfo.get("app_vision"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		if (getFlagLogin()) {
			inputParam.setMemberCode(getOauthInfo().getUserCode());
		}

		
		//分销商品不参与活动
		String fxFlag = inputParam.getFxFlag();
		RootResult rootResult = new RootResult();
		//校验分销活动是否有效
		String channelId = StringUtils.isBlank(inputParam.getChannelId())?getChannelId():inputParam.getChannelId();
		if("449747430023".equals(channelId)) {
			checkIfFXProduct(saleCode,fxFlag,rootResult);
			if(1==rootResult.getResultCode()) {
				fxFlag="1";
				inputParam.setFxFlag("1");
				}
			else {
				fxFlag="0";
				inputParam.setFxFlag("0");
				}
		}
		// 不是IC编号开头时传入的product_code变成下面所有的sku_code
		if (!PlusHelperEvent.checkEventItem(inputParam.getCode())) {

			inputParam.setCode(new PlusSupportProduct().upProductSku(inputParam.getCode()));
		}
		
		//该商品下如果没有sku，则为已全部下架状态  fq++
		if(StringUtils.isBlank(inputParam.getCode())) {
			MDataMap info = DbUp.upTable("pc_productinfo").one("product_code",saleCode);
			// 无sku时默认返回码
			//skuInfos.setResultCode(964305106);
			skuInfos.setResultMessage(bInfo(964305106));
			
			if(info == null){
				LogFactory.getLog(getClass()).warn("ApiSkuInfo Error Product -> "+saleCode);
				skuInfos.setButtonText(bInfo(964305106));
				skuInfos.setBuyStatus(6);
				return skuInfos;
			}
			
			skuInfos.setButtonText(bInfo(964305106));
			skuInfos.setBuyStatus(6);
			skuInfos.setSellPrice(info.get("min_sell_price"));
			skuInfos.setMarketPrice(info.get("market_price"));
			return skuInfos;
		}

		PlusModelSkuResult plusModelSkuResult = plusSupportProduct.upSkuInfo(inputParam);

		skuInfos.setSkus(plusModelSkuResult.getSkus());

		// 如果是活动明细编号 则特殊判断 拿到活动明细的活动下的所有商品对应的SKU，返回
		if (PlusHelperEvent.checkEventItem(inputParam.getCode())) {

			String sConstCode = new PlusSupportEvent().upConstItemCode(inputParam.getCode());

			if (StringUtils.isNotBlank(sConstCode)) {

				for (String sIcCode : StringUtils.split(sConstCode, ",")) {
					inputParam.setCode(sIcCode);

					skuInfos.getSkus().addAll(plusSupportProduct.upSkuInfo(inputParam).getSkus());

				}

			}

		}

		// 判断如果数量大于0 则按照状态值从小到大重新排序
		if (plusModelSkuResult.getSkus().size() > 0) {

			Collections.sort(plusModelSkuResult.getSkus(), new Comparator<PlusModelSkuInfo>() {

				public int compare(PlusModelSkuInfo o1, PlusModelSkuInfo o2) {

					return String.valueOf(o2.getBuyStatus()).compareTo(String.valueOf(o1.getBuyStatus()));
				}
			});

		}

		int iBuyStatus = 99;

		BigDecimal bMin = skuInfos.getSkus().get(0).getSellPrice();
		BigDecimal bMax = skuInfos.getSkus().get(0).getSellPrice();

		BigDecimal skuMin = skuInfos.getSkus().get(0).getSkuPrice();
		BigDecimal skuMinCost = skuInfos.getSkus().get(0).getCostPrice();
		BigDecimal skuMax = skuInfos.getSkus().get(0).getSkuPrice();

		String fxSkuCode = skuInfos.getSkus().get(0).getSkuCode();
		
		Map<String, String> mapEvent = new HashMap<String, String>();
		
		String client = mRequestMap.get("api_client");
		VersionEntity clientEntry = null;
		try{
			clientEntry = new JsonHelper<VersionEntity>().StringToObj(client, new VersionEntity());
		}catch(Exception e){}
		
		
		List<Integer> discountList = new ArrayList<Integer>();
		PlusModelProductInfo productInfo = null;
		int integral = -1;
		String sdEventCodeTmp = ""; //临时存储打折促销活动编号
		BigDecimal hjycoin = BigDecimal.ZERO; 
		boolean hjycoinFlag = hjycoinService.checkFlagEnabled();
		
		for (PlusModelSkuInfo pSkuInfo : skuInfos.getSkus()) {
			//为方便前端展示方便，将几人团和拼团活动结束时间放到返回结果的最外层
			if("0".equals(fxFlag)&&"1".equals(pSkuInfo.getIsCollage())) {
				if("".equals(skuInfos.getCollagePersonCount())) {
					skuInfos.setCollagePersonCount(pSkuInfo.getCollagePersonCount());
				}
				if("".equals(skuInfos.getCollageEndTime())) {
					skuInfos.setCollageEndTime(pSkuInfo.getCollageEndTime());
				}
			}
			
			if(productInfo == null){
				productInfo = plusSupportProduct.upProductInfo(pSkuInfo.getProductCode());
			}
			
			//zgh 20160311  控制前台APP 在秒杀活动未开始     图片上方“显示没货啦，下次早点来哦~”的bug    默认当前库存为1   
			if(pSkuInfo.getBuyStatus()==2 && pSkuInfo.getEventType().equals("4497472600010001")){
				pSkuInfo.setLimitStock(1l);
			}
			
			/**
			 * 秒杀状态如下
			 * 即将开始    对应文案：立即购买（置灰详见效果图）
			 * 进行中       对应文案：立即购买
			 * 已抢光       对应文案：卖光啦！下次早点来哦
			 * 已结束       对应文案：已结束、下次早点来哦！
			 */
			if(pSkuInfo.getEventType().equals("4497472600010001")){
				if(pSkuInfo.getBuyStatus() == 2){
					pSkuInfo.setButtonText("立即购买");
				}else if(pSkuInfo.getBuyStatus() == 3){
					pSkuInfo.setButtonText("已结束、下次早点来哦！");
				}else if(pSkuInfo.getBuyStatus() == 4){
					pSkuInfo.setButtonText("卖光啦！下次早点来哦");
				}
			}
			//--------到此结束-------

			if (pSkuInfo.getBuyStatus() < iBuyStatus) {
				iBuyStatus = pSkuInfo.getBuyStatus();
				
				skuInfos.setButtonText(pSkuInfo.getButtonText());
				skuInfos.setBuyStatus(pSkuInfo.getBuyStatus());
				skuInfos.setLimitSecond(pSkuInfo.getLimitSecond());
				

			} else if (pSkuInfo.getBuyStatus() == 1 && skuInfos.getLimitSecond() <= 0 && "4497472600010005".equals(pSkuInfo.getEventType())){//可售状态 && limitSecond > 0 && 为闪购活动
				//添加else if 判断，解决 一个商品下有多个参与闪购的sku时，如果前几个闪购库存卖光时，致使其他闪购的sku在app端显示 闪购已结束的bug  fq++
				skuInfos.setButtonText(pSkuInfo.getButtonText());
				skuInfos.setBuyStatus(pSkuInfo.getBuyStatus());
				skuInfos.setLimitSecond(pSkuInfo.getLimitSecond());
				
			}

			if (bMin.compareTo(pSkuInfo.getSellPrice()) > 0) {
				bMin = pSkuInfo.getSellPrice();
			}

			if (bMax.compareTo(pSkuInfo.getSellPrice()) < 0) {
				bMax = pSkuInfo.getSellPrice();
			}

			if (skuMin.compareTo(pSkuInfo.getSkuPrice()) > 0) {
				skuMin = pSkuInfo.getSkuPrice();
				skuMinCost = pSkuInfo.getCostPrice();
				fxSkuCode = pSkuInfo.getSkuCode();
			}

			if (skuMax.compareTo(pSkuInfo.getSkuPrice()) < 0) {
				skuMax = pSkuInfo.getSkuPrice();
			}
			
			//计算折扣
			if(pSkuInfo.getSellPrice().compareTo(pSkuInfo.getSkuPrice()) < 0) {
				BigDecimal discount = pSkuInfo.getSellPrice().divide(pSkuInfo.getSkuPrice(), 3, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
				discountList.add(discount.intValue());
			}

			//积分商城不返回活动类型，活动名称，活动图片
			if("0".equals(inputParam.getIntegralFlag())) {
				// 如果有活动 则将活动添加到商品返回的属性上
				if (StringUtils.isNotBlank(pSkuInfo.getEventType()) && !mapEvent.containsKey(pSkuInfo.getEventType())) {

					ProductActivity pActivity = new ProductActivity();
					pActivity.setEventType(pSkuInfo.getEventType());
					pActivity.setEventName(pSkuInfo.getSellNote());
					pActivity.setProductEventPic(pSkuInfo.getDescriptionUrlHref());
					skuInfos.getEvents().add(pActivity);
					
					//临时存储打折促销活动编号 为后续生成标签做准备
					if("4497472600010030".equals(pSkuInfo.getEventType())) {
						sdEventCodeTmp = pSkuInfo.getEventCode();
					}
				}
			}

			// 排除不赋予积分的商品，参与秒杀活动不赋予积分
			if(!"N".equalsIgnoreCase(productInfo.getAccmYn()) && !"4497472600010001".equalsIgnoreCase(pSkuInfo.getEventType())){
				Double cost = productInfo.getSkus().get(pSkuInfo.getSkuCode());
				if(cost == null || cost <= 0){
					integral = 0;  // 未查询到成本价时赋予积分固定为0
				}else{
					int num = 0;
					if(new BigDecimal(100).compareTo(pSkuInfo.getSellPrice()) < 0) {
						num = accm.productForAccmAmt(pSkuInfo.getSellPrice(), pSkuInfo.getSellPrice(), new BigDecimal(cost).setScale(2, BigDecimal.ROUND_HALF_UP), pSkuInfo.getSmallSellerCode());
					}
					if(integral >= 0){
						// 多SKU时取赋予积分最小的展示
						integral = Math.min(integral, num);
					}else{
						integral = num;
					}
				}
			}
			
			// 兼容处理IOS扫码的商品没有活动时因为ItemCode为空造成报错的问题
			if(StringUtils.isBlank(pSkuInfo.getItemCode())){
				pSkuInfo.setItemCode(pSkuInfo.getSkuCode());
			}
			
			// 成本价字段需要屏蔽，避免价格泄漏
			pSkuInfo.setCostPrice(null);
		}

		// 设置主数据上的状态值

		skuInfos.setSellPrice(bMin.toString());
		
		// 560:只要商品其中一个sku当前参与了秒杀，前端都不展示赠与积分提示语
		String eventSql = "SELECT DISTINCT ei.zid FROM sc_event_info ei, sc_event_item_product eip WHERE " + 
				"	ei.event_code = eip.event_code AND eip.product_code = '"+saleCode+"' AND eip.flag_enable = 1 " + 
				"AND ei.event_type_code = '4497472600010001' AND ei.begin_time <= NOW() AND ei.end_time > NOW() " + 
				"AND ei.event_status = '4497472700020002'  AND ei.flag_enable = '1' AND ei.seller_code = 'SI2003'";
		List<Map<String,Object>> dataSqlList = DbUp.upTable("sc_event_info").dataSqlList(eventSql, new MDataMap());
		if(null != dataSqlList && dataSqlList.size() > 0) {
			// 根据商品编号可以查询到当前发布可用的秒杀活动,不展示积分赠与提示
			integral = 0;
		}
		
		// 如果订单来源存在值，判断一下是不是能赋予惠币
		if(hjycoinFlag && StringUtils.isNotBlank(inputParam.getOrderSource())) {
			if(!hjycoinService.checkGiveEnabled(inputParam.getOrderSource())) {
				hjycoinFlag = false;
			}
		}
		
		if(integral > 0 && "0".equals(inputParam.getIntegralFlag())){
			// 根据惠币的全局开关确认赋予的是积分还是惠币，扫码购不赋予惠币只赋予积分
			if(hjycoinFlag && !inputParam.getCode().startsWith("IC_SMG")) {
				// 如果赋予的是惠币则把积分转成钱，因为前端显示2位所以最后一位直接抹去
				// 实际赋予时还是按正常的三位小数赋予
				hjycoin = accm.accmAmtToMoney(new BigDecimal(integral), 3);
				if(hjycoin.compareTo(BigDecimal.ZERO) > 0) {
					skuInfos.setHjycoin(hjycoin.floatValue());
					skuInfos.setHjycoinTips(bConfig("xmassystem.hjycoinGiveTips"));
				}
			} else {
				skuInfos.setIntegral(integral);
				skuInfos.setIntegralTips(bConfig("xmassystem.integralGiveTips"));
			}
		}
		
		if (bMin.compareTo(bMax) < 0) {
			skuInfos.setSellPrice(bMin.toString() + "-" + bMax.toString());
		}
		
		skuInfos.setSkuPrice(skuMin.toString());

		if (skuMin.compareTo(skuMax) < 0) {
			skuInfos.setSkuPrice(skuMin.toString() + "-" + skuMax.toString());
		}

		//特殊活动的价格处理
		for (ProductActivity ptactivity : skuInfos.getEvents()) {
			//特价活动 仅显示最低售价 最低划线价
			if("4497472600010002".equals(ptactivity.getEventType())) {
				skuInfos.setSellPrice(bMin.toString());
				skuInfos.setSkuPrice(skuMin.toString());
			}else if("4497472600010030".equals(ptactivity.getEventType())) {
				//打折促销 取最低折扣
				if(null != discountList && discountList.size() > 0) {
					Collections.sort(discountList);
					Integer discount = discountList.get(0);
					if(discount > 0 && discount < 100) {
						if(discount%10 == 0) discount /= 10;
						skuInfos.setEventDiscount(discount);
					}
				}
				
			}
		}
		
		// 特殊判断 如果是由于限购造成不可购买 2015-08-31 18:50 mazc强烈表示让加的猥琐代码
		if (skuInfos.getBuyStatus() == 7) {
			skuInfos.setBuyStatus(1);
			skuInfos.setButtonText(bInfo(964305101));

			for (PlusModelSkuInfo pSkuInfo : skuInfos.getSkus()) {
				
				pSkuInfo.setBuyStatus(1);
				pSkuInfo.setButtonText(bInfo(964305101));
			}

		}
		
		//积分商城时，仅显示立即兑换(立即购买)
		if(!"0".equals(inputParam.getIntegralFlag())) {
			skuInfos.setButtonControl(4);
		}else {
			//开始进行按钮控制
			if(skuInfos.getSkus().get(0).getSmallSellerCode().equals("SI2003"))
			{
				skuInfos.setButtonControl(2);
			}
			for(ProductActivity pActivity :skuInfos.getEvents())
			{
				if(pActivity.getEventType().equals("4497472600010001"))	//秒杀带倒计时的立即购买
				{
					skuInfos.setButtonControl(5);
				}else if (PlusHelperEvent.checkSmgOrDm(pActivity.getEventType())){	//扫码购立即购买
					skuInfos.setButtonControl(4);
				}else if(pActivity.getEventType().equals("4497472600010024")) {//拼团购买
					skuInfos.setButtonControl(6);
				}
			}
			if(saleCode.contains("SMG")){
				skuInfos.setButtonControl(4);
			}
		}
		
		boolean is_true = true;
		boolean is_special_smg = false;
		PlusModelEventFull eventFullInfo = null;
		Set<String> eventTypeCodeList = new HashSet<String>();
		for(ProductActivity pActivity :skuInfos.getEvents())
		{
			eventTypeCodeList.add(pActivity.getEventType());
			
			if(pActivity.getEventType().contains("4497472600010001") || 
			   pActivity.getEventType().contains("4497472600010002") ||	
			   pActivity.getEventType().contains("4497472600010003") ||
			   pActivity.getEventType().contains("4497472600010004") ||
			   pActivity.getEventType().contains("4497472600010005") ||
			   pActivity.getEventType().contains("4497472600010006") ||
			   pActivity.getEventType().contains("4497472600010007") ||
			   pActivity.getEventType().contains("4497472600010009") ||
			   pActivity.getEventType().contains("4497472600010011") ||
			   pActivity.getEventType().contains("4497472600010012") ||
			   pActivity.getEventType().contains("4497472600010014") ||
			   pActivity.getEventType().contains("4497472600010015") ||
			   pActivity.getEventType().contains("4497472600010018") ||
			   pActivity.getEventType().contains("4497472600010019") ||
			   pActivity.getEventType().contains("4497472600010020") ||
			   pActivity.getEventType().contains("4497472600010024") ||
			   pActivity.getEventType().contains("4497472600010030"))  	
			{
				//判断商品是否参与特价并参加特殊满减   fq++
				/*
				 * 支持 扫码购-满减 或 特价-满减   活动的并存 (此满减为 特殊满减  ，满减类型为：每满X减Y-LD多重促销活动_449747630008)
				 * 4497472600010002 : 特价
				 * 4497472600010004 : 扫码购-微信
				 * 4497472600010015 : 扫码购-APP
				 * eventFullInfo != null 说明此商品参与了特殊满减
				 */
				if(skuInfos.getSkus().size() > 0&&"0".equals(fxFlag)) {//获取商品编号
					String productCode = skuInfos.getSkus().get(0).getProductCode();
					//查看商品是否参与特殊满减
					eventFullInfo = new PlusSupportEvent().upEventSalueByMangeCodeAndProductCode(getManageCode(),productCode,pActivity.getEventType());
				} 
				
				//eventFullInfo = new PlusSupportEvent().upEventSalueByMangeCodeAndProductCode(getManageCode(),saleCode);
				if(null != eventFullInfo) {
					//如果此商品参与特价并参与特殊满减，则不进行标记
					is_special_smg = true;
				} else {
					is_true=false;
				}
			}
		}
		
		//积分商城，不显示满减等字样
		// 橙意会员卡商品，不显示满减等字样
		//分销商品不展示满减字样
		if(("0".equals(inputParam.getIntegralFlag()) 
				&& !productInfo.getProductCode().equals(bConfig("xmassystem.plus_product_code")))&& "0".equals(fxFlag)) {
			if(is_true&&VersionHelper.checkServerVersion("3.5.93.55")){
				PlusModelEventSale eventSale = new PlusSupportEvent().upEventSalueByMangeCode(getManageCode(),inputParam.getChannelId());
				if(!PlusHelperEvent.checkEventItem(saleCode) ){
					/**添加满减信息**/
					List<PlusModelFullCutMessage> sale = new PlusServiceSale().getEventMessage(saleCode, eventSale,inputParam.getMemberCode());
					skuInfos.setSaleMessage(sale);
				} else if(is_special_smg){
					/**添加满减信息**/
					String productCode = skuInfos.getSkus().get(0).getProductCode();
					List<PlusModelFullCutMessage> sale = new PlusServiceSale().getEventMessage(productCode, eventSale,inputParam.getMemberCode());
					skuInfos.setSaleMessage(sale);
				}
			}
			
			if(!skuInfos.getSaleMessage().isEmpty()) {
				eventTypeCodeList.add("4497472600010008");
			}
			
			// 橙意会员卡折扣
			PlusModelEventInfoPlus eventPlus = plusServiceEventPlus.getEventInfoPlus();
			if(plusServiceEventPlus.checkEventLimit(eventPlus, skuInfos.getSkus().get(0).getProductCode(), new ArrayList<String>(eventTypeCodeList))) {
				PlusModelFullCutMessage tagEvent = new PlusModelFullCutMessage();
				tagEvent.setEventName(eventPlus.getShowName());
				tagEvent.setEventCode(eventPlus.getEventCode());
				tagEvent.setEventType(eventPlus.getEventType());
				tagEvent.setSaleMessage(eventPlus.getShowNotes());
				tagEvent.setBeginTime(eventPlus.getBeginTime());
				tagEvent.setEndTime(eventPlus.getEndTime());
				tagEvent.setEventNameColor("#A37B3A");
				skuInfos.getSaleMessage().add(tagEvent);
			}
			//判断用户是否是橙意卡用户
			PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(inputParam.getMemberCode()));
			CykDiscountInfo cykInfo = new CykDiscountInfo();
			if(StringUtils.isBlank(levelInfo.getPlusEndDate())) {//非橙意卡会员
				PlusServiceEventPlus psep = new PlusServiceEventPlus();
				BigDecimal discountAmount = psep.discountAmount(skuInfos.getSkus().get(0).getProductCode(), inputParam.getMemberCode(),new ArrayList<String>(eventTypeCodeList));
				if(discountAmount.compareTo(BigDecimal.ZERO) == 1) {//减少金额不为零。
					cykInfo.setCykDiscountAmount(discountAmount);
					cykInfo.setCykProductCode(bConfig("xmassystem.plus_product_code"));
					cykInfo.setCykSkuCode(bConfig("xmassystem.plus_sku_code"));
					cykInfo.setIfShow(1);
				}
			}else {
				cykInfo.setReason("您已是橙意卡会员，无需重复购买");
			}
			skuInfos.setCykInfo(cykInfo);
			
			// 打折促销的自定义标签
			if(StringUtils.isNotEmpty(sdEventCodeTmp)) {
				PlusModelEventQuery plusModelEventQuery = new PlusModelEventQuery();
				plusModelEventQuery.setCode(sdEventCodeTmp);
				PlusModelEventInfo eventInfo = new LoadEventInfo().upInfoByCode(plusModelEventQuery);
				
				PlusModelFullCutMessage tagEvent = new PlusModelFullCutMessage();
				tagEvent.setEventName(eventInfo.getEventTipName());
				tagEvent.setEventCode(eventInfo.getEventCode());
				tagEvent.setEventType(eventInfo.getEventType());
				tagEvent.setSaleMessage(eventInfo.getEventDescription());
				tagEvent.setBeginTime(eventInfo.getBeginTime());
				tagEvent.setEndTime(eventInfo.getEndTime());
				tagEvent.setEventNameColor("#FE5454");

				skuInfos.getSaleMessage().add(tagEvent);
			}
		}
		
		skuInfos.setMarketPrice(skuInfos.getSkus().get(0).getSourcePrice().toString());

		// 5.0.2之前的老版本满减固定返回一个颜色值，新版本固定返回空值
		if(clientEntry != null && !skuInfos.getSaleMessage().isEmpty()){
			for(PlusModelFullCutMessage sl : skuInfos.getSaleMessage()){
				if(compareAppVersion(clientEntry.getApp_vision(), "5.0.2") < 0){
					sl.setEventNameColor("#fa6565");
				}else{
					sl.setEventNameColor("");
				}
			}
		}
		//一个商品如果有多个sku 当第一个sku促销价小于销售价时候秒杀页倒计时为0, 最后这里对于活动为秒杀,倒计时为0的做简单的单独判断处理
		if(skuInfos.getEvents().size()>0) {
			ProductActivity productActivity = skuInfos.getEvents().get(0);
			String eventType = productActivity.getEventType();
			if("4497472600010001".equals(eventType)) {
				if(skuInfos.getLimitSecond()==-1l) {
					for(PlusModelSkuInfo skuInfo : skuInfos.getSkus()) {
						if(skuInfo.getLimitSecond()!=-1l) {
							skuInfos.setLimitSecond(skuInfo.getLimitSecond());
							break;
						}
					}
				}
			}
			if(skuInfos.getButtonControl()==5&&!"4497472600010001".equals(eventType)) {
				skuInfos.setButtonControl(4);
			}
		}
		
		//拼团业务处理
		boolean isCollage = false;
		List<PlusModelSkuInfo> skuInfoList = skuInfos.getSkus();
		for(PlusModelSkuInfo skuInfo : skuInfoList) {
			if("1".equals(skuInfo.getIsCollage())) {
				isCollage = true;
			}
		}
		if(isCollage) {//当前商品下的sku有拼团的
			List<CollagePersonInfo> collagePersonList = new ArrayList<CollagePersonInfo>();
			String sql = "select t.* from (select i.collage_code, ei.end_time, i.collage_time,i.collage_member,ei.collage_type, "
					+ "(ei.collage_person_count - ("
						+ "select count(1) from sc_event_collage_item ci where ci.collage_code = i.collage_code and ci.is_confirm = '449748320002')"
					+ ") need_person_count,"
					+ "(select s.nickname from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) nickname,"
					+ "(select s.avatar from membercenter.mc_member_sync s where s.member_code = i.collage_member order by s.last_update_time desc limit 0, 1) avatar "
					+ "from sc_event_collage c, sc_event_collage_item i, ordercenter.oc_orderdetail od, sc_event_item_product ip, sc_event_info ei "
					+ "where ip.product_code = :product_code and ip.event_code = ei.event_code and (ei.event_status='4497472700020002' or ei.event_status='4497472700020004') and ip.flag_enable = '1' "
					+ "and ei.event_type_code = '4497472600010024' and ei.begin_time <= sysdate() and c.expire_time >= sysdate() and i.collage_member_type = '449748310001' and c.collage_code = i.collage_code "
					+ "and od.sku_code = ip.sku_code and od.order_code = i.collage_ord_code and i.is_confirm = '449748320002' and c.collage_status = '449748300001' and c.event_code = ei.event_code "
					+ "and (ei.channels = '' OR locate(:channelId,ei.channels) > 0)";
			if(getFlagLogin()) {
				sql += "and c.collage_code not in (select item.collage_code from sc_event_collage_item item where item.collage_member = :me_collage_member)";
			}
			sql += ")t " + "order by t.need_person_count asc, t.collage_time asc limit 0, 10 ";
			
			MDataMap collageParam = new MDataMap();
			collageParam.put("product_code", saleCode);
			collageParam.put("channelId", inputParam.getChannelId());
			if(getFlagLogin()) {
				collageParam.put("me_collage_member", getOauthInfo().getUserCode());
			}
			List<Map<String, Object>> personList = DbUp.upTable("sc_event_collage_item").dataSqlList(sql, collageParam);
			for(Map<String, Object> person : personList) {
				String collageCode = MapUtils.getString(person, "collage_code", "");
				String collageMember = MapUtils.getString(person, "collage_member", "");
				if(collageMember.contains("XN")) {
					continue;//过滤掉机器人开团单
				}
				String endTime = "";
				if(StringUtils.isNotBlank(collageCode)) {
					MDataMap expireQuery = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
					if(expireQuery != null && !expireQuery.isEmpty()) {
						endTime = expireQuery.get("expire_time");
						if(StringUtils.isNotBlank(endTime)&&endTime.length()>19) {
							endTime = endTime.substring(0, 19);//拼团时效时间
						}
					}
				}
				CollagePersonInfo collagePerson = new CollagePersonInfo();
				collagePerson.setNickname(MapUtils.getString(person, "nickname", ""));//昵称
				collagePerson.setHeadPhoto(MapUtils.getString(person, "avatar", ""));//头像
				collagePerson.setCollageCode(collageCode);//拼团编码
				collagePerson.setCollageEndTime(endTime);//拼团活动结束时间
				collagePerson.setNeedPersonCount(MapUtils.getString(person, "need_person_count", ""));//还需要几人成团
				collagePersonList.add(collagePerson);
				skuInfos.setCollageType(MapUtils.getString(person, "collage_type", ""));//拼团类型
			}
			/**
			 * 拼接上机器人开团列表
			 */
			String productCode = saleCode;
			if(saleCode.contains("IC")) {
				MDataMap pInfo = DbUp.upTable("sc_event_item_product").one("item_code",saleCode);
				productCode = pInfo.get("product_code");
			}
			String robotSql = "SELECT * FROM systemcenter.sc_event_collage_item a left join systemcenter.sc_event_collage b ON a.collage_code = b.collage_code  WHERE a.product_code = '"+productCode+"' AND a.collage_ord_code like 'XN%' AND a.collage_member_type = 449748310001 AND b.expire_time > sysdate() AND b.collage_status = '449748300001'";
			List<Map<String,Object>> robotList = DbUp.upTable("sc_event_collage_item").dataSqlList(robotSql, null);
			if(robotList != null && robotList.size()>0) {
				for(Map<String,Object> map : robotList) {
					MDataMap collageItem = new MDataMap(map);
					String collageCode = collageItem.get("collage_code");
					String endTime = "";
					if(StringUtils.isNotBlank(collageCode)) {
						MDataMap expireQuery = DbUp.upTable("sc_event_collage").one("collage_code",collageCode);
						if(!"449748300001".equals(expireQuery.get("collage_status"))) {
							continue;
						}
						if(expireQuery != null && !expireQuery.isEmpty()) {
							endTime = expireQuery.get("expire_time");
							if(StringUtils.isNotBlank(endTime)&&endTime.length()>19) {
								endTime = endTime.substring(0, 19);//拼团时效时间
							}
						}
					}
					String memberCode = collageItem.get("collage_member");
					MDataMap robot = DbUp.upTable("mc_robot_info").one("member_code",memberCode);
					if(robot == null || robot.isEmpty()) {
						continue;
					}
					CollagePersonInfo collagePerson = new CollagePersonInfo();
					collagePerson.setNickname(robot.get("nick_name"));//昵称
					collagePerson.setHeadPhoto(robot.get("head_photo"));//头像
					collagePerson.setCollageCode(collageItem.get("collage_code"));//拼团编码
					collagePerson.setCollageEndTime(endTime);//拼团活动结束时间
					collagePerson.setNeedPersonCount("1");//还需要几人成团
					collagePersonList.add(collagePerson);
				}
			}
			skuInfos.setCollagePersonList(collagePersonList);
			
			skuInfos.setCollageRuleUrl(bConfig("xmasproduct.collage_rule_page"));//拼团规则
			skuInfos.setCollagePlayWay(bConfig("xmasproduct.collage_play_way"));//拼团玩法
		}
		
		//538增加需求
		/*
		 * List<ProductActivity> events = skuInfos.getEvents(); List<PlusModelSkuInfo>
		 * skus = skuInfos.getSkus(); boolean flagmiaosha = false; boolean flagshangou =
		 * false; if(skus.size() == 1) { PlusModelSkuInfo plusModelSkuInfo =
		 * skus.get(0);
		 * if(plusModelSkuInfo.getSellPrice().compareTo(plusModelSkuInfo.getSkuPrice())
		 * == -1) { for(ProductActivity pro : events) {
		 * if(pro.getEventType().equals("4497472600010005")) { flagmiaosha=true; }
		 * if(pro.getEventType().equals("4497472600010001")) { flagshangou=true; } }
		 * if(flagmiaosha && flagshangou) { int buyStatus = skuInfos.getBuyStatus();
		 * if(buyStatus != 2 && buyStatus != 3) { skuInfos.setShowPriceCurve("Y"); } } }
		 * }
		 */
		if(StringUtils.isBlank(inputParam.getShowPriceCurve())) {
			skuInfos.setShowPriceCurve(bConfig("xmasproduct.isShowPriceCurve"));
		}
		//分销商品做券后价格修改
		if("1".equals(fxFlag)) {
			String coupon_money = rootResult.getResultMessage();
			BigDecimal minSellPrice = BigDecimal.ZERO;
			if(skuInfos.getSellPrice().contains("-")) {
				String[] split = skuInfos.getSellPrice().split("-");
				String string = split[0];
				minSellPrice = new BigDecimal(string);
			}else {
				minSellPrice = new BigDecimal(skuInfos.getSellPrice());
			}
		    BigDecimal subtract =minSellPrice.subtract(new BigDecimal(coupon_money));
		    skuInfos.setSellPrice(subtract.toString());
		    skuInfos.setIfShowFXPrice("1");
		    //sku价格改为券后价显示
		    List<PlusModelSkuInfo> skus = skuInfos.getSkus();
		    for (PlusModelSkuInfo plusModelSkuInfo : skus) {
		    	plusModelSkuInfo.setSellPrice(plusModelSkuInfo.getSellPrice().subtract(new BigDecimal(coupon_money)));
			}
		    
		    // 只有当前用户在分销人表中才展示分销收益
		    if(getFlagLogin() 
		    		&& DbUp.upTable("fh_agent_member_info").count("member_code", getOauthInfo().getUserCode()) > 0
		    		&& StringUtils.isNotBlank(fxSkuCode)) {
			    
			    // 以SKU档案价为准
			    MDataMap fxSkuInfo = DbUp.upTable("pc_skuinfo").oneWhere("sell_price,cost_price", "", "", "sku_code", fxSkuCode);
			    BigDecimal fxSellPrice = new BigDecimal(fxSkuInfo.get("sell_price"));
			    BigDecimal fxCostPrice = new BigDecimal(fxSkuInfo.get("cost_price"));
			    BigDecimal fxMoney = plusSupportFenxiao.getFenxiaoMoney(fxSellPrice, fxCostPrice, productInfo.getSmallSellerCode());
			    
			    if(fxMoney.compareTo(BigDecimal.ZERO) > 0) {
			    	skuInfos.setFxMoney(MoneyHelper.format(fxMoney));
			    }
		    }
		    
		}
		//550支持银联分期预览
		if(clientEntry==null || compareAppVersion(clientEntry.getApp_vision(), "5.5.0") >= 0) {
			try {
				handleInstallment(skuInfos);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return skuInfos;
	
	}
	
	private void checkIfFXProduct(String productCode, String fxFlag, RootResult rootResult) {
		// TODO Auto-generated method stub
		    rootResult.setResultCode(0);
			List<Map<String, Object>> listMap= DbUp.upTable("oc_activity").dataSqlList("select * from oc_activity where activity_type='449715400008' and flag=1 and begin_time<=now() and end_time>now() order by zid desc", null);
		    if(listMap!=null&&listMap.size()>0) {
		    	Map<String, Object> map = listMap.get(0);
		    	 Map<String, Object> dataSqlOne = DbUp.upTable("oc_activity_agent_product").dataSqlOne("select * from oc_activity_agent_product where activity_code=:activity_code and produt_code=:produt_code and flag_enable=1 ",new MDataMap("activity_code",map.get("activity_code").toString(),"produt_code",productCode));
			     if(dataSqlOne!=null) {
			    	 String coupon_money = dataSqlOne.get("coupon_money").toString();
			    	 rootResult.setResultCode(1);
			    	 rootResult.setResultMessage(coupon_money);
			     }
		    }
	}

	/**
	 * 对比版本
	 * appVersion > compareVersion   返回正数
	 * appVersion = compareVersion   返回0
	 * appVersion < compareVersion   返回负数
	 * @param appVersion
	 * @param compareVersion
	 */
	public static Integer compareAppVersion ( String appVersion ,String compareVersion) {
		if(StringUtils.isBlank(appVersion)) {
			appVersion = "";
		}
		if(StringUtils.isBlank(compareVersion)) {
			compareVersion = "";
		}
		return AppVersionUtils.compareTo(appVersion, compareVersion);
	}

	/**
	 * 处理分期展示
	 * 临时方案
	 * @param skuInfos
	 */
	private void handleInstallment(SkuInfos skuInfos){
		Channel channel = Channel.IOS;
		MDataMap apiClient = getApiClient();
		//IPHONE OS 为ios8版本 此处做兼容
		if(null != apiClient && !apiClient.isEmpty() && !"IPHONE OS".equals(apiClient.get("os").toUpperCase())) {
			try {
				channel = Channel.valueOf(apiClient.get("os").toUpperCase());
			} catch (Exception e) {
				LogFactory.getLog(getClass()).warn("handleInstallment -> channel UNKNOWN ~: " + apiClient.get("os"));
				channel = Channel.UNKNOWN;
			}
		}
		
		//排查网站
		if(channel!=Channel.WEB) {
			PlusModelPayTypeInfo info = new LoadPayTypeInfo().upInfoByCode(new PlusModelPayTypeInfoQuery());
			List<PayTypeInfo> typeInfoList = info.getTypeInfoList();
			for (PayTypeInfo payTypeInfo : typeInfoList) {
				if("449746280020".equals(payTypeInfo.getPayType())) {
					//校验当前渠道是否支持分期 
					if(!payTypeInfo.getChannelList().contains(channel)) {
						break;
					}
					//校验商品是否支持分期付款
					if(!payTypeInfo.getTypeProductList().contains(skuInfos.getSkus().get(0).getProductCode())) {
						break;
					}
					// 如果有任一个商户被禁用了当前的支付方式则整单都不支持
					if(payTypeInfo.getSellerList().contains(StringUtils.trimToEmpty(skuInfos.getSkus().get(0).getSmallSellerCode()).toUpperCase())){
						break;
					}
					// 如果商户类型在被屏蔽中则整单都不支持
					PlusModelSellerInfo sellerInfo = new LoadSellerInfo().upInfoByCode(new PlusModelSellerQuery(skuInfos.getSkus().get(0).getSmallSellerCode()));
					if(sellerInfo != null && payTypeInfo.getSellerTypeList().contains(sellerInfo.getUc_seller_type())){
						break;
					}
					
					//取sku中的最大价格做分期展示
					BigDecimal maxSellPrice = BigDecimal.ZERO;
					for (PlusModelSkuInfo skuInfo : skuInfos.getSkus()) {
						if(maxSellPrice.compareTo(skuInfo.getSellPrice())<0) {
							maxSellPrice = skuInfo.getSellPrice();
						}
					}
					
					//校验商品最大金额
					if(maxSellPrice.compareTo(new BigDecimal(bConfig("familyhas.unionpay_stages_min_money"))) < 0) {
						break;
					}
					
					RequestModel requestModel = new RequestModel();
					requestModel.setMethod("Pay_UNCOM_000001");
					Map<String, Object> installmentReqModel = new LinkedHashMap<String, Object>();
					installmentReqModel.put("bank_cd", bConfig("xmasproduct.installment_show_bank_cd"));
					installmentReqModel.put("order_amount", maxSellPrice.toString());
					requestModel.setBody(installmentReqModel);
					String result = CallPaymentGatewaySupport.callGateway(requestModel);
					if(StringUtils.isNotEmpty(result)) {
						JSONArray jsonArray = JSON.parseArray(JSON.parseObject(result).get("body")+"");
						List<String> installShow = new ArrayList<String>();
						for (Object model : jsonArray) {
							JSONObject jsonObject = JSON.parseObject(model+"");
							String x = "￥" + jsonObject.getString("period_fee") + " x "
									+ jsonObject.getIntValue("period") + "期<br/>" + "另需服务费:￥"
									+ jsonObject.getString("service_fee") + " , 费率:"
									+ new BigDecimal(jsonObject.getDoubleValue("pay_rate") * 100).setScale(2,
											BigDecimal.ROUND_HALF_DOWN)
									+ "%";
							installShow.add(x);
						}
						if(!installShow.isEmpty()) {
							skuInfos.setInstallmentTitle(bConfig("xmasproduct.installment_title"));
							skuInfos.setInstallmentContentTitle(bConfig("xmasproduct.installment_content_title"));
							skuInfos.setInstallmentContent(installShow);
						}
					}
					break;
				}
			}
		}
	}
}
