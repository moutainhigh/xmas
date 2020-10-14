package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.AverageMoneyModel;
import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;

public class TeslaCouponService {
	
	/**
	 * @Description:获取优惠券剩余金额
	 * @param couponTypeCode
	 *            优惠券编号
	 * @author 张海生
	 * @date 2015-6-18 下午5:29:30
	 * @return BigDecimal
	 * @throws
	 */
	public BigDecimal getCouponMoney(String couponCode,String memberCode,List<AverageMoneyModel> averageModel) {
		if (StringUtils.isEmpty(couponCode)) {
			return new BigDecimal("0");
		} else {
			MDataMap couponMap = DbUp
					.upTable("oc_coupon_info")
					.oneWhere(
							"surplus_money,coupon_type_code",
							"",
							"coupon_code=:couponCode and member_code=:memberCode and start_time <= now() and end_time >= now()",
							"couponCode", couponCode,"memberCode",memberCode);// 查询优惠券信息
			if (couponMap != null
					&& StringUtils.isNotEmpty(couponMap.get("surplus_money"))) {
				MDataMap typeMap = DbUp.upTable("oc_coupon_type").oneWhere("money_type", "", "", "coupon_type_code",couponMap.get("coupon_type_code"));
				if(typeMap == null) return new BigDecimal("0");
				
				if("449748120001".equals(typeMap.get("money_type")) || "449748120003".equals(typeMap.get("money_type"))){
					 // 金额卷 和 礼金券
					return new BigDecimal(couponMap.get("surplus_money")); 
				}else if("449748120002".equals(typeMap.get("money_type"))){
					// 根据商品总金额计算打折优惠的金额
					BigDecimal money = BigDecimal.ZERO; 
					for(AverageMoneyModel mm : averageModel){
						money = money.add(new BigDecimal(mm.getSkuNum()*mm.getSkuPrice()).setScale(2,BigDecimal.ROUND_HALF_UP));
					}
					money = money.multiply(new BigDecimal(1).subtract(new BigDecimal(couponMap.get("surplus_money")).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP)));
					return money.setScale(0, BigDecimal.ROUND_UP);
				}
				
				return new BigDecimal("0");
			} else {
				return new BigDecimal("0");
			}
		}
	}
	
	/**
	 * 获取优惠券类型
	 * @param couponCode
	 * @param memberCode
	 * @return
	 */
	public String getCouponMoneyType(String couponCode,String memberCode) {
		String sql = "select oc_coupon_type.money_type from oc_coupon_info left join oc_coupon_type on oc_coupon_info.coupon_type_code=oc_coupon_type.coupon_type_code"
				   +" where oc_coupon_info.coupon_code=:coupon_code and oc_coupon_info.member_code=:member_code";
		MDataMap params = new MDataMap();
		params.put("coupon_code", couponCode);
		params.put("member_code", memberCode);
		Map<String, Object> typeMap = DbUp.upTable("oc_coupon_info").dataSqlOne(sql, params);
		return typeMap.get("money_type") == null ? "" : typeMap.get("money_type").toString();
	}
	
	/**
	 * 获取优惠券的活动
	 * @param couponCode
	 * @return
	 */
	public String getCouponActivityCode(String couponCode) {
		String sql = "select activity_code from oc_coupon_info where coupon_code=:coupon_code";
		MDataMap params = new MDataMap();
		params.put("coupon_code", couponCode);
		Map<String, Object> activityCodeMap = DbUp.upTable("oc_coupon_info").dataSqlOne(sql, params);
		return activityCodeMap.get("activity_code") == null ? "" : activityCodeMap.get("activity_code").toString();
	}
	
	/**
	 * 获取优惠券是否可以叠加使用
	 * @param couponCode
	 * @param memberCode
	 * @return
	 */
	public String getCouponIsMulti(String couponCode,String memberCode) {
		String sql = "select oc_activity.is_multi_use from oc_coupon_info left join oc_activity on oc_coupon_info.activity_code=oc_activity.activity_code"
				   +" where oc_coupon_info.coupon_code=:coupon_code and oc_coupon_info.member_code=:member_code";
		MDataMap params = new MDataMap();
		params.put("coupon_code", couponCode);
		params.put("member_code", memberCode);
		Map<String, Object> typeMap = DbUp.upTable("oc_coupon_info").dataSqlOne(sql, params);
		return typeMap.get("is_multi_use") == null ? "" : typeMap.get("is_multi_use").toString();
	}
	
	public Map<String, Object> checkIsLdAndStatus(String couponCode) {
		String sql = "select oi.out_coupon_code,ot.creater,ot.status,oa.flag from ordercenter.oc_coupon_info oi,ordercenter.oc_coupon_type ot,ordercenter.oc_activity oa where oi.coupon_type_code=ot.coupon_type_code and oi.activity_code=oa.activity_code and oi.coupon_code=:coupon_code";
		Map<String, Object> result = DbUp.upTable("oc_coupon_info").dataSqlOne(sql, new MDataMap("coupon_code", couponCode));
		return result;
	}
	
	/**
	 * 转换优惠券面额的显示方式 <br>
	 * 折扣券面额 88 转换为 8.8 <br>
	 * @param couponInfo
	 */
	public void convertMoneyShow(TeslaModelCouponInfo couponInfo){
		// 折扣卷
		if("449748120002".equals(couponInfo.getMoneyType())){
			BigDecimal initialMoney = couponInfo.getInitialMoney(); 
			
			// 折扣券需要把数据库里面的值除以10后显示
			initialMoney = initialMoney.divide(new BigDecimal(10),1,BigDecimal.ROUND_HALF_UP);
			// 如果没有小数的不再保留小数位，方面客户端显示
			if(initialMoney.setScale(0, BigDecimal.ROUND_UP).compareTo(initialMoney) == 0){
				initialMoney = initialMoney.setScale(0, BigDecimal.ROUND_UP);
			}
			// 折扣券的初始金额和剩余金额保持一致
			couponInfo.setInitialMoney(initialMoney);
			couponInfo.setSurplusMoney(initialMoney);
		}
	}
	
	/**
	 * 查询用户的未使用的优惠券列表
	 * @return
	 */
	public List<TeslaModelCouponInfo> getAllCouponInfoList(TeslaXOrder order) {
		String version = order.getUorderInfo().getAppVersion();
		String couponLimit = TopConfig.Instance.bConfig("familyhas.coupon_limit");
		
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", order.getUorderInfo().getBuyerCode());
		mWhereMap.put("manage_code", order.getUorderInfo().getSellerCode());
		
		String sql = "select ci.coupon_code,ci.initial_money,ci.surplus_money,ci.status,ci.end_time,ci.limit_money,ci.start_time,ci.end_time,ci.blocked,ci.big_order_code,ci.coupon_type_code,ct.money_type,ci.activity_code,ct.creater,ci.out_coupon_code,oa.provide_type  "
				+ "from oc_coupon_info ci,oc_coupon_type ct,oc_activity oa where ci.coupon_type_code = ct.coupon_type_code and ci.activity_code = oa.activity_code and ci.member_code=:member_code and ci.surplus_money>0 and (ci.status=0 or (oa.is_change='Y' and ci.status=1)) "
				+ "and IFNULL(ci.blocked,0) = 0 and ci.surplus_money>0 and ci.end_time>now() and ci.start_time<now() and ci.manage_code=:manage_code "
				+ " order by ct.money_type asc,ci.end_time asc,ci.initial_money desc,ci.limit_money asc,ci.coupon_code asc";
		
		List<Map<String,Object>> listMaps = DbUp.upTable("oc_coupon_info").upTemplate().queryForList(sql, mWhereMap);
		
		LoadCouponType loadCouponType = new LoadCouponType();
		
		Map<String, PlusModelCouponType> couponTypeCodesMap = new HashMap<String, PlusModelCouponType>();
		double deadlineDay = Double.parseDouble(StringUtils.isEmpty(TopConfig.Instance.bConfig("ordercenter.COUPON_DEADLINE_DAY")) ? "2.0" : TopConfig.Instance.bConfig("ordercenter.COUPON_DEADLINE_DAY"));
		
		List<TeslaModelCouponInfo> couponInfoList = new ArrayList<TeslaModelCouponInfo>();
		TeslaModelCouponInfo couponInfo;
		for (Map<String,Object> maps : listMaps) {
			//ld的优惠券 没有外部优惠券编号、版本低于5.2.8、活动未发布、活动类型未发布 任一情况 则不能使用此优惠券 -rhb 20181113
			String creater = maps.get("creater") + "";
			String coupon_type_code = maps.get("coupon_type_code") + "";
			if(StringUtils.isNotBlank(creater) && StringUtils.isNotBlank(coupon_type_code) && "ld".equals(creater)) {
				if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.2.8") < 0) {
					continue;
				}
				if(StringUtils.isBlank(maps.get("out_coupon_code")+"")) {
					continue;
				}
				String sSql = "select ot.status,oa.flag from ordercenter.oc_coupon_type ot,ordercenter.oc_activity oa where ot.activity_code=oa.activity_code and ot.coupon_type_code=:coupon_type_code";
				Map<String, Object> qResult = DbUp.upTable("oc_coupon_type").dataSqlOne(sSql, new MDataMap("coupon_type_code", coupon_type_code));
				if(!"1".equals(qResult.get("flag")+"") || !"4497469400030002".equals(qResult.get("status"))) {
					continue;
				}
			}
			
			PlusModelCouponType couponType = couponTypeCodesMap.get(maps.get("coupon_type_code"));
			if(couponType == null) {
				couponType = loadCouponType.upInfoByCode(new PlusModelQuery(maps.get("coupon_type_code")+""));
				couponTypeCodesMap.put(maps.get("coupon_type_code")+"", couponType);
			}
			
			// 限制金额时可用优惠劵
			String initial_money = (null == maps.get("initial_money") ? "" : maps.get("initial_money").toString());
			String limit_money = (null == maps.get("limit_money") ? "" : maps.get("limit_money").toString());
			String surplus_money = (null == maps.get("surplus_money") ? "" : maps.get("surplus_money").toString());
			String coupon_code = (null == maps.get("coupon_code") ? "" : maps.get("coupon_code").toString());
			String status = (null == maps.get("status") ? "0" : maps.get("status").toString());
			String start_time = (null == maps.get("start_time") ? "" : maps.get("start_time").toString());
			String end_time = (null == maps.get("end_time") ? "" : maps.get("end_time").toString());
			String activity_code = (null == maps.get("activity_code") ? "" : maps.get("activity_code").toString());
			
			couponInfo = new TeslaModelCouponInfo();
			
			//542版本返回优惠券类型编号 -rhb 20190423
			couponInfo.setCouponTypeCode(coupon_type_code);
			//565版本返回优惠券活动类型，针对系统返利活动
			couponInfo.setProvide_type(maps.get("provide_type").toString());
			// 平台限制
			if ("4497471600070002".equals(couponType.getLimitCondition()) && "4497471600070002".equals(couponType.getCouponTypeLimit().getChannelLimit())) {
				couponInfo.setChannelLimit("1");
				
				// 不符合平台限制的优惠券不展示
				boolean isContain = (","+couponType.getCouponTypeLimit().getChannelCodes()+",").contains(","+order.getChannelId()+",");
				if(!isContain) {
					continue;
				}
			}
			// 优惠券的金额类型： 449748120001: 金额卷，449748120002: 折扣券
			couponInfo.setMoneyType(maps.get("money_type")+"");
			// 初始金额，页面显示使用
			couponInfo.setInitialMoney(new BigDecimal(initial_money));
			couponInfo.setCouponCode(coupon_code);
			couponInfo.setSurplusMoney(new BigDecimal(surplus_money));
			couponInfo.setStatus(Integer.parseInt(status));
			
			//560 可找零优惠券状态特殊处理
			if("Y".equals(maps.get("is_change"))) {
				couponInfo.setStatus(0);
			}else {
				couponInfo.setStatus(Integer.parseInt(status));
			}
			
			couponInfo.setEndTime(end_time);
			couponInfo.setStartTime(start_time);
			couponInfo.setLimitMoney(new BigDecimal(limit_money));
			couponInfo.setUseLimit(couponLimit);
			couponInfo.setActivityCode(activity_code);
			
			if (StringUtils.isEmpty(couponType.getLimitScope())) {
				couponInfo.setUseLimit(couponLimit);
			} else {
				couponInfo.setUseLimit(couponType.getLimitScope());
			}
			couponInfo.setLimitExplain(couponType.getLimitExplain());
			
			
			//判断增加未激活标志
			String blocked = (null == maps.get("blocked") ? "" : maps.get("blocked").toString());
			if(StringUtils.isNotEmpty(blocked) && blocked.equals("1")) {
				couponInfo.setStatus(5);
			}
			//判断增加送此优惠券的大订单号
			String big_order_code = (null == maps.get("big_order_code") ? "" : maps.get("big_order_code").toString());
			if(StringUtils.isNotEmpty(big_order_code)) {
				couponInfo.setBigOrderCode(big_order_code);
			}
			
			//判断增加即将过期天数
			String endTime = maps.get("end_time").toString();
			try {
				double diffDay = DateHelper.daysBetween(DateHelper.upNow(), endTime);
				if(diffDay>=1.0 && diffDay <= deadlineDay) {
					couponInfo.setDeadline("还剩" + (new Double(diffDay).intValue() + 1) + "天");
				} else if(diffDay >= 0.0 && diffDay < 1.0) {
					int diffHour = DateHelper.hoursBetween(DateHelper.upNow(), endTime);
					if(diffHour != 0) {
						//diffHour = 1;
						couponInfo.setDeadline("还剩" + diffHour + "小时");
					}else {
						int diffMinute = DateHelper.minuteBetween(DateHelper.upNow(), endTime);
						if(diffMinute == 0) {
							diffMinute = 1;
						}
						couponInfo.setDeadline("还剩" + diffMinute + "分钟");
					}
					
				}
				
				//542版本增加快过期标签 -rhb 20190412
				if(diffDay>=0 && diffDay<=2.0) {
					couponInfo.setIsShowDue("1");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//判断是否可以叠加使用 -rhb 20181024
			String is_multi_use = "Y";
			String is_change = "Y";
			if(StringUtils.isNotEmpty(activity_code)) {
				MDataMap activity = DbUp.upTable("oc_activity").one("activity_code",activity_code);
				is_multi_use = activity.get("is_multi_use");
				is_change = activity.get("is_change");
			}
			couponInfo.setIs_multi_use(is_multi_use);
			couponInfo.setIs_change(is_change);
			
			/**
			 * 542增加限制条件 -rhb 20190424
			 * 优惠券类型定义    使用下限金额     限制条件
			 * 无门槛                  0                       无限制
			 * 无金额限制           0                       指定
			 * 满X元可用             X             ——
			 */
			String limitCondition = "";
			if("0".equals(limit_money)) {
				if("4497471600070001".equals(couponType.getLimitCondition())) {
					limitCondition = TopConfig.Instance.bConfig("familyhas.no_threshold");
				}
				if("4497471600070002".equals(couponType.getLimitCondition())) {
					limitCondition = TopConfig.Instance.bConfig("familyhas.unlimited_amount");
				}
			}else {
				limitCondition = FormatHelper.formatString(TopConfig.Instance.bConfig("familyhas.with_x_available"),limit_money);
			}
			couponInfo.setLimitCondition(limitCondition);
			// 折扣券显示处理
			convertMoneyShow(couponInfo);
			
			couponInfoList.add(couponInfo);
		}
		
		return couponInfoList;
	}
	
	/**
	 * 计算给定的优惠券列表，每个优惠券可用的商品
	 * @param order
	 * @param couponCodeList
	 * @return
	 */
	public Map<String,List<TeslaModelOrderDetail>> getCouponUseProductList(TeslaXOrder order, List<String> couponCodeList) {
		Map<String,List<TeslaModelOrderDetail>> resultMap = new HashMap<String, List<TeslaModelOrderDetail>>();
		Map<String,PlusModelCouponType> couponTypeMap = new HashMap<String, PlusModelCouponType>();
		Map<String,PlusModelCouponActivity> couponActivityMap = new HashMap<String, PlusModelCouponActivity>();
		Map<String,PlusModelProductInfo> productMap = new HashMap<String, PlusModelProductInfo>();
		
		PlusModelCouponType couponType;
		PlusModelCouponActivity couponActivity;
		PlusModelProductInfo productInfo;
		LoadCouponType loadCouponType = new LoadCouponType();
		LoadProductInfo loadProductInfo = new LoadProductInfo();
		LoadCouponActivity loadCouponActivity = new LoadCouponActivity();
		
		if(couponCodeList == null || couponCodeList.isEmpty()) {
			return resultMap;
		}
		
		// 每个SKU参与的活动列表
		Map<String,List<String>> skuActivityTypeMap = new HashMap<String, List<String>>();
		// 初始化
		for(TeslaModelOrderDetail detail : order.getOrderDetails()) {
			skuActivityTypeMap.put(detail.getSkuCode(), new ArrayList<String>());
		}
		
		for(TeslaModelOrderActivity oa : order.getActivityList()) {
			if(StringUtils.isBlank(oa.getSkuCode()) || StringUtils.isBlank(oa.getActivityType())) {
				continue;
			}
			
			// 忽略橙意卡专享活动，这个可以跟任何优惠券叠加
			if("4497472600010026".equals(oa.getActivityType())) {
				continue;
			}
			
			// 忽略商品的在线支付立减活动，这个可以跟任何优惠券叠加
			if("4497472600010021".equals(oa.getActivityType())) {
				continue;
			}
			// 忽略加价购活动商品
			if("4497472600010025".equals(oa.getActivityType())) {
				continue;
			}
			
			List<String> list = skuActivityTypeMap.get(oa.getSkuCode());
			if(list == null) {
				list = new ArrayList<String>();
				skuActivityTypeMap.put(oa.getSkuCode(), list);
			}
			
			list.add(oa.getActivityType());
		}
		
		List<TeslaModelOrderDetail> tempDetaiList;
		MDataMap couponInfo;
		for(String couponCode : couponCodeList) {
			couponInfo = DbUp.upTable("oc_coupon_info").one("coupon_code", couponCode);
			if(new BigDecimal(couponInfo.get("limit_money")).compareTo(order.getUorderInfo().getDueMoney()) > 0) {
				continue;
			}
			
			couponType = couponTypeMap.get(couponInfo.get("coupon_type_code"));
			if(couponType == null) {
				couponType = loadCouponType.upInfoByCode(new PlusModelQuery(couponInfo.get("coupon_type_code")));
				couponTypeMap.put(couponInfo.get("coupon_type_code"), couponType);
			}
			
			couponActivity = couponActivityMap.get(couponInfo.get("activity_code"));
			if(couponActivity == null) {
				couponActivity = loadCouponActivity.upInfoByCode(new PlusModelQuery(couponInfo.get("activity_code")));
				couponActivityMap.put(couponInfo.get("activity_code"), couponActivity);
			}
			
			tempDetaiList = new ArrayList<TeslaModelOrderDetail>();
			BigDecimal productMoney = BigDecimal.ZERO;
			for(TeslaModelOrderDetail detail : order.getOrderDetails()) {
				if(!"1".equals(detail.getGiftFlag())) continue;
				if("1".equals(detail.getIfJJGFlag())) continue;
				
				// 分销商品只能使用分销类型优惠券，订单确认页面自动勾选可用优惠券以此处为准
				if(detail.getFxFlag() == 1 && !"449715400008".equals(couponActivity.getActivityType())) {
					continue;
				}
				
				// 内购活动不参与任何优惠券
				if(skuActivityTypeMap.get(detail.getSkuCode()).contains("4497472600010006")) {
					continue;
				}
				
				// 橙意会员卡商品不能使用任何优惠券
				if(detail.getProductCode().equals(TopConfig.Instance.bConfig("xmassystem.plus_product_code"))) {
					continue;
				}
				
				// 其他指定条件判断
				if("4497471600070002".equals(couponType.getLimitCondition())) {
					// 商品参与活动的情况下检查活动是否可以叠加优惠券
					if(!skuActivityTypeMap.get(detail.getSkuCode()).isEmpty()) {
						if("449747110002".equals(couponType.getCouponTypeLimit().getActivityLimit())) {
							// 可以叠加的情况，需要再判断具体活动
							List<String> types = Arrays.asList(StringUtils.trimToEmpty(couponType.getCouponTypeLimit().getAllowedActivityType()).split(","));
							// 如果参与的活动不在支持叠加的列表中则此商品不可用优惠券
							if(types.isEmpty() || !types.containsAll(skuActivityTypeMap.get(detail.getSkuCode()))) {
								continue;
							}
						} else {
							continue;
						}
					}
					
					productInfo = productMap.get(detail.getProductCode());
					if(productInfo == null) {
						productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(detail.getProductCode()));
						productMap.put(detail.getProductCode(), productInfo);
					}
					
					// 检查商户限制
					if("449748230002".equals(couponType.getCouponTypeLimit().getSellerLimit())) {
						if(!"SI2003".equalsIgnoreCase(detail.getSmallSellerCode())) {
							continue;
						}
					}
					
					// 检查品牌限制
					if("4497471600070002".equals(couponType.getCouponTypeLimit().getBrandLimit())) {
						boolean isContain = (","+couponType.getCouponTypeLimit().getBrandCodes()+",").contains(","+productInfo.getBrandCode()+",");
						// 如果是排除指定品牌，且当前商品的品牌在排除列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptBrand() == 1 && isContain) {
							continue;
						}
						
						// 如果是限定指定品牌，且当前商品的品牌未在限定列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptBrand() == 0 && !isContain) {
							continue;
						}
					}
					
					// 检查商品限制
					if("4497471600070002".equals(couponType.getCouponTypeLimit().getProductLimit())) {
						boolean isContain = (","+couponType.getCouponTypeLimit().getProductCodes()+",").contains(","+detail.getProductCode()+",");
						// 如果是排除指定商品，且当前商品的在排除列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptProduct() == 1 && isContain) {
							continue;
						}
						
						// 如果是限定指定商品，且当前商品未在限定列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptProduct() == 0 && !isContain) {
							continue;
						}
					}
					
					// 检查分类限制
					if("4497471600070002".equals(couponType.getCouponTypeLimit().getCategoryLimit())) {
						boolean isContain = false;
						String[] limitCats = StringUtils.trimToEmpty(couponType.getCouponTypeLimit().getCategoryCodes()).split(",");
						
						// 循环一下商品的分类，判断限定的分类是否包含商品分类
						for(String cat : productInfo.getCategorys()) {
							for(String c : limitCats) {
								if(cat.startsWith(c)) {
									isContain = true;
									break;
								}
							}
							
							if(isContain) break;
						}
						
						// 如果是排除指定分类，且当前商品的分类在排除列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptCategory() == 1 && isContain) {
							continue;
						}
						
						// 如果是限定指定商品，且当前商品的分类未在限定列表则不可用优惠券
						if(couponType.getCouponTypeLimit().getExceptCategory() == 0 && !isContain) {
							continue;
						}
					}
					
					// 检查支付方式限制
					if(!"449748290003".equals(couponType.getCouponTypeLimit().getPaymentType()) && StringUtils.isNotBlank(couponType.getCouponTypeLimit().getPaymentType())) {
						if("449748290002".equals(couponType.getCouponTypeLimit().getPaymentType())) {
							// 货到付款判断
							if(!"449716200002".equals(order.getUorderInfo().getPayType())) {
								continue;
							}
						} else if("449748290001".equals(couponType.getCouponTypeLimit().getPaymentType())) {
							// 在线支付判断
							if(!"449716200001".equals(order.getUorderInfo().getPayType())) {
								continue;
							}
						} else {
							continue;
						}
						 
					}
					
					// 检查渠道限制
					if("4497471600070002".equals(couponType.getCouponTypeLimit().getChannelLimit())) {
						boolean isContain = (","+couponType.getCouponTypeLimit().getChannelCodes()+",").contains(","+order.getChannelId()+",");
						if(!isContain) {
							continue;
						}
					}
				}
				
				productMoney = productMoney.add(detail.getSkuPrice().multiply(new BigDecimal(detail.getSkuNum())));
				tempDetaiList.add(detail);
			}
			
			// 最后再检查一下优惠券的使用下限金额是不是超过了可用商品的总金额
			if(new BigDecimal(couponInfo.get("limit_money")).compareTo(productMoney) > 0) {
				continue;
			}
			
			if(!tempDetaiList.isEmpty()) {
				resultMap.put(couponCode, tempDetaiList);
			}
		}
		
		return resultMap;
	}
}
