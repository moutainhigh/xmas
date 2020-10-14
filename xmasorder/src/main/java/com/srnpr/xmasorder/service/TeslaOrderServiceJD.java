package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmasorder.model.jingdong.CheckAreaLimitVO;
import com.srnpr.xmasorder.model.jingdong.CreateOrderSuccessVO;
import com.srnpr.xmasorder.model.jingdong.GiftsVO;
import com.srnpr.xmasorder.model.jingdong.PriceVo;
import com.srnpr.xmasorder.model.jingdong.ProductCheckRepVo;
import com.srnpr.xmasorder.model.jingdong.ResponseVO;
import com.srnpr.xmasorder.model.jingdong.SkuGiftVO;
import com.srnpr.xmasorder.model.jingdong.SkuModel;
import com.srnpr.xmasorder.model.jingdong.SkuNum;
import com.srnpr.xmasorder.model.jingdong.StockNewResultVo;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.homehas.RsyncJingdongSupport;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.webwx.WxGateSupport;

public class TeslaOrderServiceJD extends BaseClass{

	/**
	 * 查sku是否可售
	 * @param skuIds 多个sku 逗号隔开
	 * @param queryExts 扩展参数：SkuStateQueryExts英文逗号间隔输入
	 * @return
	 */
	public List<ProductCheckRepVo> getSaleState(String skuIds, String queryExts) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("skuIds", skuIds);
			params.put("queryExts", null == queryExts ? "" : queryExts);
			String reqResult = RsyncJingdongSupport.callGateway(Constants.GET_SALE_STATE, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.GET_SALE_STATE)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_RESULT_CODE.equals(responseVO.getResultCode())) {
					return JSON.parseArray(responseVO.getResult(), ProductCheckRepVo.class);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 查sku的协议价
	 * @param skuCodeOld
	 * @return
	 */
	public List<PriceVo> getNegotiatedPrice(String skuIds) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sku", skuIds);
			String reqResult = RsyncJingdongSupport.callGateway(Constants.GET_SKU_PRICE, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.GET_SKU_PRICE)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_RESULT_CODE.equals(responseVO.getResultCode())) {
					return JSON.parseArray(responseVO.getResult(), PriceVo.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 查库存<br/>
	 * 编号不存在时用0占位
	 * @param area 地址编码 格式：一级_二级_三级_四级
	 * @param skuNums
	 * @return
	 */
	public List<StockNewResultVo> getStock(String area, List<SkuNum> skuNums) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("area", area);
			params.put("skuNums", skuNums);
			String reqResult = RsyncJingdongSupport.callGateway(Constants.GET_SKU_STOCK, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.GET_SKU_STOCK)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_RESULT_CODE.equals(responseVO.getResultCode())) {
					return JSON.parseArray(responseVO.getResult(), StockNewResultVo.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 查商品区域限制<br/>
	 * 编号不存在时用0占位
	 * @param skuIds 多个sku逗号隔开
	 * @param provinceId 一级编号
	 * @param cityId 二级编号
	 * @param countyId 三级编号
	 * @param villageId 四级编号
	 * @return
	 */
	public List<CheckAreaLimitVO> checkAreaLimit(String skuIds, String provinceId, String cityId, String countyId, String villageId) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("skuIds", skuIds);
			params.put("province", provinceId);
			params.put("city", cityId);
			params.put("county", countyId);
			params.put("town", villageId);
			String reqResult = RsyncJingdongSupport.callGateway(Constants.GET_SKU_AREA_LIMIT, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.GET_SKU_AREA_LIMIT)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_RESULT_CODE.equals(responseVO.getResultCode())) {
					return JSON.parseArray(responseVO.getResult(), CheckAreaLimitVO.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 查商品赠品、附件
	 * @param skuId 仅支持单sku
	 * @param province
	 * @param city
	 * @param county
	 * @param town
	 * @return
	 */
	public GiftsVO getSkuGift(String skuId, String province, String city, String county, String town) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("skuId", Long.parseLong(skuId));
			params.put("province", Long.parseLong(province));
			params.put("city", Long.parseLong(city));
			params.put("county", Long.parseLong(county));
			params.put("town", Long.parseLong(town));
			String reqResult = RsyncJingdongSupport.callGateway(Constants.GET_SKU_GIFT, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.GET_SKU_GIFT)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_RESULT_CODE.equals(responseVO.getResultCode())) {
					return JSON.parseObject(responseVO.getResult(), GiftsVO.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 创建订单
	 * @param model
	 * @return
	 */
	public MWebResult createOrder(String orderCode) {
		MWebResult result = new MWebResult();
		try {
			
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code", orderCode);
			MDataMap jdOrderInfo = DbUp.upTable("oc_order_jd").one("order_code", orderCode);
			MDataMap orderAddress = DbUp.upTable("oc_orderadress").one("order_code", orderCode);
			
			//封装sku信息
			List<SkuModel> sku = new ArrayList<SkuModel>();
			SkuModel skuModel = new SkuModel();
			skuModel.setSkuId(jdOrderInfo.get("sku_id"));
			skuModel.setNum(Integer.parseInt(jdOrderInfo.get("sku_num")));
			skuModel.setbNeedAnnex(true);
			boolean bNeedGift = false;
			if(!"0".equals(jdOrderInfo.get("province"))&&!"0".equals(jdOrderInfo.get("city"))&&!"0".equals(jdOrderInfo.get("county"))) {
				GiftsVO skuGift = getSkuGift(jdOrderInfo.get("sku_id"),jdOrderInfo.get("province"),jdOrderInfo.get("city"),jdOrderInfo.get("county"),jdOrderInfo.get("town"));
				if(null != skuGift) {
					for (SkuGiftVO gift : skuGift.getGifts()) {
						if(gift.getGiftType()==2) {
							int minNum = gift.getMinNum();
							int maxNum = gift.getMaxNum();
							String startTime = DateUtil.toString(new Timestamp(gift.getPromoStartTime()));
							String endTime = DateUtil.toString(new Timestamp(gift.getPromoEndTime()));
							if(0 == minNum || 0 == maxNum || StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
								continue;
							}
							String nowTime = DateUtil.getSysDateTimeString();
							if (DateUtil.compareDate1(nowTime, startTime) && DateUtil.compareDate1(endTime, nowTime)
									&& Integer.parseInt(jdOrderInfo.get("sku_num")) <= maxNum
									&& Integer.parseInt(jdOrderInfo.get("sku_num")) >= minNum) {
								bNeedGift = true;
								break;
							}
						}
					}
				}
			}
			skuModel.setbNeedGift(bNeedGift);
			sku.add(skuModel);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("thirdOrder", orderInfo.get("order_code"));
			params.put("sku", sku);
			params.put("name", orderAddress.get("receive_person"));
			params.put("province", Integer.parseInt(jdOrderInfo.get("province")));
			params.put("city", Integer.parseInt(jdOrderInfo.get("city")));
			params.put("county", Integer.parseInt(jdOrderInfo.get("county")));
			params.put("town", Integer.parseInt(jdOrderInfo.get("town")));
			params.put("address", orderAddress.get("address"));
			params.put("zip", orderAddress.get("postcode"));
			params.put("phone", orderAddress.get("telephone"));
			params.put("mobile", orderAddress.get("mobilephone"));
			params.put("email", bConfig("xmasorder.jd_order_email"));
			params.put("remark", "");
			params.put("invoiceState", 2);
			params.put("invoiceType", Integer.parseInt(bConfig("xmasorder.jd_invoice_type")));
			params.put("selectedInvoiceTitle", 5);
			params.put("companyName", bConfig("xmasorder.jd_company_name"));
			params.put("invoiceContent", 1);
			params.put("bookInvoiceContent", 4);
			params.put("paymentType", 4);
			params.put("isUseBalance", 1);
			params.put("submitState", 1);
			params.put("invoiceName", bConfig("xmasorder.jd_invoice_name"));
			params.put("invoicePhone", bConfig("xmasorder.jd_invoice_phone"));
			params.put("invoiceProvice", Integer.parseInt(bConfig("xmasorder.jd_invoice_provice")));
			params.put("invoiceCity", Integer.parseInt(bConfig("xmasorder.jd_invoice_city")));
			params.put("invoiceCounty", Integer.parseInt(bConfig("xmasorder.jd_invoice_county")));
			params.put("invoiceAddress", bConfig("xmasorder.jd_invoice_address"));
			
			String reqResult = RsyncJingdongSupport.callGateway(Constants.CREATE_JD_ORDER, params);
			if(StringUtils.isNotEmpty(reqResult)) {
				String response = JSON.parseObject(reqResult).get(getResponseKey(Constants.CREATE_JD_ORDER)).toString();
				ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
				if(responseVO.isSuccess() && Constants.SUCCESS_CREATE_ORDER_CODE.equals(responseVO.getResultCode())) {
					CreateOrderSuccessVO createOrderSuccess = JSON.parseObject(responseVO.getResult(), CreateOrderSuccessVO.class);
					//更新订单信息
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("order_code", orderInfo.get("order_code"));
					mDataMap.put("jd_order_id", createOrderSuccess.getJdOrderId());
					mDataMap.put("order_price", createOrderSuccess.getOrderPrice()+"");
					mDataMap.put("order_naked_price", createOrderSuccess.getOrderNakedPrice()+"");
					mDataMap.put("order_tax_price", createOrderSuccess.getOrderTaxPrice()+"");
					mDataMap.put("freight", createOrderSuccess.getFreight()+"");
					mDataMap.put("tax", createOrderSuccess.getSku().get(0).getTax()+"");
					DbUp.upTable("oc_order_jd").dataUpdate(mDataMap, "", "order_code");
					
					//回写外部订单号
					DbUp.upTable("oc_orderinfo").dataUpdate(
							new MDataMap("order_code", orderInfo.get("order_code"), "out_order_code", createOrderSuccess.getJdOrderId()), "", "order_code");
					
					MDataMap exMap = new MDataMap();
					exMap.put("hjy_order_code", orderInfo.get("order_code"));
					exMap.put("delete_flag", "1");
					exMap.put("ex_type", "4497471600400001");
					exMap.put("update_time", FormatHelper.upDateTime());
					if(UserFactory.INSTANCE.checkUserLogin()) {
						exMap.put("updator", UserFactory.INSTANCE.create().getUserCode()+"");
					} else {
						exMap.put("updator", "system");
					}
					
					//更新三方异常单状态
					DbUp.upTable("oc_order_sanfang_exception").dataUpdate(exMap, "delete_flag,update_time,updator", "hjy_order_code,ex_type");
					
					//判断商品协议价是否有变更 有则更改订单中的协议价
					MDataMap orderDetail = DbUp.upTable("oc_orderdetail").one("order_code", orderInfo.get("order_code"),"sku_code",jdOrderInfo.get("sku_code"));
					BigDecimal nowCostPrice = createOrderSuccess.getSku().get(0).getPrice();
					BigDecimal oldCostPrice = new BigDecimal(orderDetail.get("cost_price"));
					if(nowCostPrice.compareTo(BigDecimal.ZERO) > 0 && nowCostPrice.compareTo(oldCostPrice) != 0) {
						//更改订单中的成本价
						DbUp.upTable("oc_orderdetail").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "sku_code",
										jdOrderInfo.get("sku_code"), "cost_price", nowCostPrice.toString()),"", "order_code,sku_code");
						//记录价格变动订单日志表
						DbUp.upTable("lc_jd_order_price").insert("uid",UUID.randomUUID().toString().replaceAll("-", ""),"order_code", orderInfo.get("order_code"),
								"jd_order_id", createOrderSuccess.getJdOrderId(), "sku_code", orderDetail.get("sku_code"), "sku_id", jdOrderInfo.get("sku_id"), "old_cost_price",
								oldCostPrice.toString(), "now_cost_price", nowCostPrice.toString(), "create_time", DateUtil.getSysDateTimeString());
					}
					
					result.setResultCode(1);
				}else if(Constants.REPETI_CREATE_ORDER_CODE.equals(responseVO.getResultCode())) {
					//重复下单时 若没有外部订单编号 回写进去
					if(StringUtils.isEmpty(orderInfo.get("out_order_code"))) {
						CreateOrderSuccessVO createOrderSuccess = JSON.parseObject(responseVO.getResult(), CreateOrderSuccessVO.class);
						DbUp.upTable("oc_orderinfo").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "out_order_code", createOrderSuccess.getJdOrderId()), "", "order_code");
						DbUp.upTable("oc_order_jd").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "jd_order_id", createOrderSuccess.getJdOrderId()), "", "order_code");
					}
				} else {
					//创建订单失败 存入异常单
					int count = DbUp.upTable("oc_order_sanfang_exception").count("hjy_order_code", orderInfo.get("order_code"));
					if(count == 0) {
						DbUp.upTable("oc_order_sanfang_exception").insert("hjy_order_code", orderInfo.get("order_code"),
								"small_seller_code", Constants.SMALL_SELLER_CODE_JD, "reason", responseVO.getResultMessage(),
								"ex_type", "4497471600400001", "delete_flag", "0", "create_time", DateUtil.getSysDateTimeString());
						
						WxGateSupport support = new WxGateSupport();
						String receives = support.bConfig("groupcenter.jd_notice_receives_order");
						List<String> list = support.queryOpenId(receives);
						String msg = String.format("[%s][%s]", orderCode, responseVO.getResultMessage()) ;
						for(String v : list) {
							support.sendWarnCountMsg("京东下单失败", "下单失败", v, msg);
						}
					}
					result.setResultCode(0);
				}
				result.setResultMessage(responseVO.getResultMessage());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.setResultCode(99);
		result.setResultMessage("接口调用失败");
		return result;
	}
	
	private String getResponseKey(String method) {
		if(StringUtils.isEmpty(method)) return "";
		return StringUtils.replace(method, ".", "_")+"_response";
	}
	
	/**
	 * 通过商户编号判断京东商品
	 * @param smallSellerCode
	 * @return
	 */
	public boolean isJdOrder(String smallSellerCode) {
		return Constants.SMALL_SELLER_CODE_JD.equals(smallSellerCode);
	}
	
	/**
	 * 通过惠家有订单编号判断是否为异常单
	 * @param orderCode
	 * @return
	 */
	public boolean isExceptionOrder(String orderCode) {
		int count = DbUp.upTable("oc_order_sanfang_exception").count("hjy_order_code", orderCode,"delete_flag","0");
		return 0 != count;
	}
}
