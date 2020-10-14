package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.CouponListQuery;
import com.srnpr.xmassystem.modelevent.ModelCouponForGetInfo;
import com.srnpr.xmassystem.modelevent.PlusModelCouponListInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigCouponList;
import com.srnpr.xmassystem.service.PlusServiceSale;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadCouponListForProduct extends LoadTopMain<PlusModelCouponListInfo, CouponListQuery> {

	@Override
	public PlusModelCouponListInfo topInitInfoMain(CouponListQuery tQuery) {


		return null;
	}
	
	@Override
	public PlusModelCouponListInfo topInitInfo(CouponListQuery tQuery) {
		//根据商品编号获取SKU编号
		PlusModelCouponListInfo result = new PlusModelCouponListInfo();
		String productCode = tQuery.getCode();
		String memberCode = tQuery.getMemberCode();
		String fxFlag = tQuery.getFxFlag();//是否做分销券的查询
		String skuCodes = new PlusSupportProduct().upProductSku(productCode);//根据商品编号获取所有在售SKU编号skuCode。
		PlusSupportProduct psp = new PlusSupportProduct();
		LoadEventInfo loadEventInfo = new LoadEventInfo();
		List<String> eventTypeCodeList = new ArrayList<String>();
		String eventCodes = "";
		if(!StringUtils.isEmpty(skuCodes)) {//判空
			String skuArr[] = skuCodes.split(",");
			for(int i = 0;i<skuArr.length;i++) {
				String skuCode = skuArr[i];
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(skuCode);
				skuQuery.setChannelId(tQuery.getChannelId());
				skuQuery.setMemberCode(memberCode);
				skuQuery.setIsPurchase(1);
				PlusModelSkuInfo skuInfo=psp.upSkuInfo(skuQuery).getSkus().get(0);
				String eventCode = skuInfo.getEventCode();//获取所有活动信息。
				if(!StringUtils.isEmpty(eventCode)) {
					eventCodes += eventCode+",";
				}
			}
		}
		String eventCodeArr[] = eventCodes.split(",");
		for(int i = 0;i<eventCodeArr.length;i++) {
			String eventCode = eventCodeArr[i];
			if(!StringUtils.isEmpty(eventCode)) {
				PlusModelEventQuery query = new PlusModelEventQuery();
				query.setCode(eventCode);
				PlusModelEventInfo eventInfo = loadEventInfo.upInfoByCode(query);
				String eventType = eventInfo.getEventType();
				eventTypeCodeList.add(eventType);
			}
		}
		//判断满减活动
		PlusModelEventSale eventSale = new PlusSupportEvent().upEventSalueByMangeCode("SI2003",tQuery.getChannelId());
		/**添加满减信息**/
		List<PlusModelFullCutMessage> sale = new PlusServiceSale().getEventMessage(productCode, eventSale,memberCode);
		for(PlusModelFullCutMessage fullCut : sale) {
			String eventType = fullCut.getEventType();
			if("4497472600010008".equals(eventType)) {
				eventTypeCodeList.add(eventType);
			}
		}
		for(String eventType :eventTypeCodeList ) {
			if("4497472600010006".equals(eventType)) {//内购没券，返回空null
				return null;
			}
		}
		Map<String, Map<String, Object>> couponTypeCodesMap = new HashMap<String, Map<String, Object>>();
		Map<String, String> fxCouponTypeCodesMap = new HashMap<String,String>();
		Map<String, String> productCode_brandCodeMap = new HashMap<String, String>(); // 商品与品牌对应map
		Map<String, String> productCode_categoryCodeMap = new HashMap<String, String>(); // 订单中的商品与分类对应map(如若为多个分类，分类中间用英文逗号隔开)
		// 优惠券类型对象map信息,第一轮筛选
		String couponCodeTypeSql = "select oa.activity_type activity_type, ot.valid_day coupon_valid_day,ot.start_time coupon_start_time,ot.end_time coupon_end_time,ot.action_type action_type, ot.action_value action_value, ot.uid uid, ot.surplus_money surplus_money, ot.limit_scope limit_scope ,ot.limit_explain limit_explain, ot.coupon_type_code coupon_type_code,ot.coupon_type_name coupon_type_name,ot.activity_code activity_code,ot.money money,ot.limit_money limit_money,oa.begin_time start_time,oa.end_time end_time ,ot.money_type money_type,ot.limit_condition limit_condition from oc_coupon_type ot "
				+ "LEFT JOIN oc_activity oa ON ot.activity_code = oa.activity_code  where ot.produce_type = '4497471600040001' and oa.begin_time <= sysdate() and oa.end_time > sysdate() and oa.provide_type in ('4497471600060002','4497471600060005') and oa.flag = 1 and oa.is_detail_show = '449748350002' and ot.exchange_type = '4497471600390001' and ot.status = '4497469400030002' order by ot.create_time desc";

		List<Map<String, Object>> couponTypeMapList = DbUp.upTable("oc_coupon_type").dataSqlList(couponCodeTypeSql,
				null);
		for (Map<String, Object> couponTypeMap : couponTypeMapList) {
			couponTypeCodesMap.put(couponTypeMap.get("coupon_type_code").toString(), couponTypeMap);
			if("1".equals(fxFlag)&&("449715400008".equals(couponTypeMap.get("activity_type").toString()))) {
				//相同时间段内只能发布一个分销活动
				fxCouponTypeCodesMap.put(couponTypeMap.get("coupon_type_code").toString(), couponTypeMap.get("activity_code").toString());
			}
		}
		List<MDataMap> brandMapList = DbUp.upTable("pc_productinfo").queryAll("product_code,brand_code", "", "product_code in ('" + productCode + "')", null);
		for (MDataMap mDataMap : brandMapList) {
			productCode_brandCodeMap.put(mDataMap.get("product_code"), mDataMap.get("brand_code"));
		}
		List<MDataMap> categoryMapList = DbUp.upTable("uc_sellercategory_product_relation")
				.queryAll("category_code,product_code", "", "product_code = '" + productCode + "'", null);

		for (MDataMap mDataMap : categoryMapList) {
			String categoryCode = mDataMap.get("category_code");
			if (StringUtils.isNotEmpty(productCode_categoryCodeMap.get(productCode))) {
				categoryCode += ("," + productCode_categoryCodeMap.get(productCode));
			}
			productCode_categoryCodeMap.put(productCode, categoryCode);
		}
		List<Map<String, Object>> showList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : couponTypeMapList) {
			String limit_condition = map.get("limit_condition") != null ? map.get("limit_condition").toString() : "";
			if("449715400008".equals(map.get("activity_type"))) {
				if("1".equals(fxFlag)&&fxCouponTypeCodesMap.containsKey(map.get("coupon_type_code"))) {
					//查看该分销活动下该商品是否继续有效
					int dataCount = DbUp.upTable("oc_activity_agent_product").dataCount("activity_code=:activity_code and produt_code=:produt_code and flag_enable=1 ", new MDataMap("activity_code",fxCouponTypeCodesMap.get(map.get("coupon_type_code").toString()),"produt_code",productCode));
					if(dataCount>0) {
						showList.add(map);
					}	
				}
				continue;
			}
		
			if ("4497471600070001".equals(limit_condition)) {// 4497471600070001 限制条件为无限制，不需要校验限制规则
				showList.add(map);
				continue;
			}
			MDataMap couponTypeLimit = DbUp.upTable("oc_coupon_type_limit").one("coupon_type_code",
					map.get("coupon_type_code").toString());
			String channel_codes = couponTypeLimit.get("channel_codes");
			String category_codes = couponTypeLimit.get("category_codes");
			String category_limit = couponTypeLimit.get("category_limit");
			String except_category = couponTypeLimit.get("except_category");
			String brand_limit = couponTypeLimit.get("brand_limit");
			String activity_limit =  couponTypeLimit.get("activity_limit");
			String except_brand = couponTypeLimit.get("except_brand");
			String brand_codes = couponTypeLimit.get("brand_codes");
			String product_limit = couponTypeLimit.get("product_limit");
			String except_product = couponTypeLimit.get("except_product");
			String product_codes = couponTypeLimit.get("product_codes");
			String allowed_activity_type = couponTypeLimit.get("allowed_activity_type");
			String channel_limit = couponTypeLimit.get("channel_limit");
			boolean flagUse = true;
			// 检查品牌限制
			if (flagUse && "4497471600070002".equals(brand_limit)) {
				if ("0".equals(except_brand) && (StringUtils.isEmpty(brand_codes) || null == productCode_brandCodeMap
						|| StringUtils.isEmpty(productCode_brandCodeMap.get(productCode)))) {
					// 指定品牌限制非除外，品牌限制列表为空或者传入商品所属品牌为空时商品不可用
					flagUse = false;
				} else if ("1".equals(except_brand)
						&& (StringUtils.isEmpty(brand_codes) || null == productCode_brandCodeMap
								|| StringUtils.isEmpty(productCode_brandCodeMap.get(productCode)))) {
					// 指定品牌限制为除外，品牌限制列表为空或者传入商品所属品牌为空时商品全部可用
				} else {
					boolean limitBrand = true;
					for (String brandCodeLimit : brand_codes.split(",")) {
						if ("0".equals(except_brand)
								&& brandCodeLimit.equals(productCode_brandCodeMap.get(productCode))) {
							limitBrand = false;
						} else if ("1".equals(except_brand)
								&& brandCodeLimit.equals(productCode_brandCodeMap.get(productCode))) {
							// 指定除外限制的品牌中包含该商品，表示该商品不可使用此优惠券，结束循环
							flagUse = false;
							break;
						}
					}
					// 指定限制的品牌中不包含该商品，表示该商品不可使用此优惠券
					if (limitBrand && "0".equals(except_brand)) {
						flagUse = false;
					}
				}
			}

			// 检查商品限制
			if (flagUse && "4497471600070002".equals(product_limit)) {
				if ("0".equals(except_product) && (StringUtils.isEmpty(product_codes))) {
					// 指定商品限制非除外，商品限制列表为空时商品不可用
					flagUse = false;
				} else if ("1".equals(except_product) && StringUtils.isEmpty(product_codes)) {
					// 指定商品限制为除外，商品限制列表为空时商品全部可用
				} else {
					boolean limitProduct = true;
					for (String productCodeLimit : product_codes.split(",")) {
						if ("0".equals(except_product) && productCodeLimit.equals(productCode)) {
							limitProduct = false;
						} else if ("1".equals(except_product) && productCodeLimit.equals(productCode)) {
							// 指定除外限制的商品中包含该商品，表示该商品不可使用此优惠券，结束循环
							flagUse = false;
							break;
						}
					}
					// 指定限制的商品中不包含该商品，表示该商品不可使用此优惠券
					if (limitProduct && "0".equals(except_product)) {
						flagUse = false;
					}
				}
			}
			// 检查分类限制
			if (flagUse && "4497471600070002".equals(category_limit)) {
				if ("0".equals(except_category)
						&& (StringUtils.isEmpty(category_codes) || null == productCode_categoryCodeMap
								|| StringUtils.isEmpty(productCode_categoryCodeMap.get(productCode)))) {
					// 指定分类限制非除外，分类限制列表为空或传入商品的所属分类为空时商品不可用
					flagUse = false;
				} else if ("1".equals(except_category)
						&& (StringUtils.isEmpty(category_codes) || null == productCode_categoryCodeMap
								|| StringUtils.isEmpty(productCode_categoryCodeMap.get(productCode)))) {
					// 指定分类限制为除外，分类限制列表为空或传入商品的所属分类为空时商品全部可用
				} else {
					boolean limitCategory = true;
					for (String categoryCodeLimit : category_codes.split(",")) {
						if ("0".equals(except_category)) {
							for (String categoryCode : productCode_categoryCodeMap.get(productCode).split(",")) {
								if (categoryCodeLimit.equals(categoryCode)) {
									limitCategory = false;
									break;
								}
							}
						} else if ("1".equals(except_category)) {
							// 指定分类限制为除外，分类限制列表为空时商品全部可用
							if (StringUtils.isEmpty(category_codes)) {
								break;
							}
							for (String categoryCode : productCode_categoryCodeMap.get(productCode).split(",")) {
								if (categoryCodeLimit.equals(categoryCode)) {
									// 指定除外限制的分类中包含该商品，表示该商品不可使用此优惠券，结束循环
									flagUse = false;
									break;
								}
							}
							if (!flagUse) {
								break;
							}
						}
					}
					// 指定限制的品牌中不包含该商品，表示该商品不可使用此优惠券
					if (limitCategory && "0".equals(except_category)) {
						flagUse = false;
					}
				}
			}
			// 检查是否可以参与活动限制
			//449747110001 : 否，449747110002 ： 是
//			LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
			if(flagUse&&("449747110001".equals(activity_limit))) {//不可以参与活动
				if(eventTypeCodeList.size()>0) {//有活动，则不展示优惠券
					flagUse = false;
				}
				
			}
			if(flagUse&&("449747110002".equals(activity_limit))) {//可以参与活动
				flagUse = false;
				if(eventTypeCodeList.size() == 0||(eventTypeCodeList.size() == 1&&"4497472600010021".equals(eventTypeCodeList.get(0)))) {//商品没有参与活动或只参与了在线支付立减活动
					flagUse = true;
				}else{
					for(String eventType : eventTypeCodeList) {
						if(allowed_activity_type.contains(eventType)) {
							flagUse = true;
							break;
						}
					}
				}
				
			}
			if (flagUse) {// flagUse 为true的时候，则在页面展示  
				map.put("channel_codes", channel_codes);
				map.put("product_limit", product_limit);
				map.put("except_product", except_product);
				map.put("product_codes", product_codes);
				map.put("channel_limit", channel_limit);
				showList.add(map);
			}
		}
		
		List<ModelCouponForGetInfo> couponList = new ArrayList<ModelCouponForGetInfo>();
		for (Map<String, Object> map : showList) {
			MDataMap mDataMap = new MDataMap(map);
			ModelCouponForGetInfo couponForGetInfo = new ModelCouponForGetInfo();
			couponForGetInfo.setUid(mDataMap.get("uid"));
			couponForGetInfo.setCouponTypeCode(mDataMap.get("coupon_type_code"));
			couponForGetInfo.setStartTime(mDataMap.get("start_time"));
			couponForGetInfo.setEndTime(mDataMap.get("end_time"));
			couponForGetInfo.setLimitExplain(mDataMap.get("limit_explain"));// 使用说明
			couponForGetInfo.setLimitScope(mDataMap.get("limit_scope"));// 使用范围
			couponForGetInfo.setLimitMoney(new BigDecimal(mDataMap.get("limit_money")));// 使用限额
			couponForGetInfo.setMoneyType(mDataMap.get("money_type"));
			couponForGetInfo.setActivityCode(mDataMap.get("activity_code"));
			couponForGetInfo.setMoney(new BigDecimal(mDataMap.get("money")));
			couponForGetInfo.setCouponTypeName(mDataMap.get("coupon_type_name"));
			couponForGetInfo.setLimitCondition(mDataMap.get("limit_condition"));
			couponForGetInfo.setActionType(mDataMap.get("action_type"));
			couponForGetInfo.setActionValue(mDataMap.get("action_value"));
			couponForGetInfo.setChannelCodes(mDataMap.get("channel_codes"));
			couponForGetInfo.setProductLimit(mDataMap.get("product_limit"));
			couponForGetInfo.setExceptProduct(mDataMap.get("except_product"));
			couponForGetInfo.setProductCodes(mDataMap.get("product_codes"));
			couponForGetInfo.setChannelLimit(mDataMap.get("channel_limit"));
			couponForGetInfo.setCouponStartTime(mDataMap.get("coupon_start_time"));
			couponForGetInfo.setCouponEndTime(mDataMap.get("coupon_end_time"));
			couponForGetInfo.setValidDay(mDataMap.get("coupon_valid_day"));
			couponForGetInfo.setActivityType(mDataMap.get("activity_type"));
			int ifStore = 0;// 是否抢光
			String surplus_money = mDataMap.get("surplus_money");// 剩余金额
			if (surplus_money.compareTo("0") > 0||"449715400008".equals(mDataMap.get("activity_type"))) {// 还有剩余金额,分销券可用
				ifStore = 1;
			}
			couponForGetInfo.setIfStore(ifStore);// 是否有余额
			couponList.add(couponForGetInfo);
		}
		result.setCouponList(couponList);
		if(couponList.size()>0) {
			return result;
		}
		return null;
	}

	private final static PlusConfigCouponList PLUS_CONFIG = new PlusConfigCouponList();

	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

}
