package com.srnpr.xmasorder.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.xmassystem.duohuozhu.model.ApiCode;
import com.srnpr.xmassystem.duohuozhu.model.RequestModel;
import com.srnpr.xmassystem.duohuozhu.model.ResponseModel;
import com.srnpr.xmassystem.duohuozhu.support.RsyncDuohuozhuSupport;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 多货主订单业务
 * @remark 
 * @author 任宏斌
 * @date 2019年6月14日
 */
public class TeslaOrderServiceDuohz {

	/**
	 * 创建订单
	 * @param sInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public MWebResult createOrder(String sInfo) {
		MWebResult result = new MWebResult();
		try {
			
			List<MDataMap> duohzOrderDetail = DbUp.upTable("oc_order_duohz_detail").queryByWhere("order_code",sInfo);
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",sInfo);
			MDataMap orderAddress = DbUp.upTable("oc_orderadress").one("order_code",sInfo);
			
			if(null != duohzOrderDetail && !duohzOrderDetail.isEmpty()) {
				
				RsyncDuohuozhuSupport rsyncDuohuozhuSupport = new RsyncDuohuozhuSupport();
				
				RequestModel requestModel = new RequestModel();
				requestModel.getHead().setFunction_id(ApiCode.CP000002.toString());
				
				LinkedHashMap<String, Object> order = new LinkedHashMap<String,Object>();
				order.put("cp_ord_id", orderInfo.get("order_code"));//第三方订单编号
				order.put("ord_amt", 0);//订单总金额（代收金额） 写死0
				order.put("tel1", "");//收货人电话区号
				order.put("tel2", "");//收货人电话（包含分机）
				order.put("rcv_nm", orderAddress.get("receive_person"));//收货人姓名
				order.put("mobile", orderAddress.get("mobilephone"));//收货人手机
				
				String areaCode = orderAddress.get("area_code");
				Map<String, String> addMap = getRcvAdd1(areaCode);
				String rcvAdd1 = addMap.get("hjyAddressName");
				String rcvAdd2 = addMap.get("levelFour") + orderAddress.get("address");
				order.put("rcv_add1", rcvAdd1);//收货人大地址
				order.put("rcv_add2", rcvAdd2);//收货人详细地址
				
				List<LinkedHashMap<String, Object>> detailList = new ArrayList<LinkedHashMap<String, Object>>();
				for (MDataMap duohzOrder : duohzOrderDetail) {
					LinkedHashMap<String, Object> detail = new LinkedHashMap<String, Object>();
					PlusModelSkuInfo plusModelSkuInfo  = new PlusSupportProduct().upSkuInfoBySkuCode(duohzOrder.get("sku_code"));
					
					detail.put("cp_ord_seq", duohzOrder.get("seq"));//第三方订单序号
					detail.put("cp_good_id", plusModelSkuInfo.getProductCode());//商品编号
					
					String colorId = "";
					String styleId = "";
					String skuKey = plusModelSkuInfo.getSkuKey();
					if (!"".equals(skuKey == null ? "" : skuKey.toString().trim())) {
						String[] ss = skuKey.split("&");
						for (String s : ss) {
							if (s.contains("4497462000010001=")) {
								colorId = s.replace("4497462000010001=", "");
							}
							if (s.contains("4497462000020001=")) {
								styleId = s.replace("4497462000020001=", "");
							}
						}
					}	
					
					String colorNm = "";
					String styleNm = "";
					String skuKeyvalue = plusModelSkuInfo.getSkuKeyvalue();
					if (!"".equals(skuKeyvalue == null ? "" : skuKeyvalue.toString().trim())) {
						String[] ss = skuKeyvalue.split("&");
						for (String s : ss) {
							if (s.contains("颜色=")) {
								colorNm = s.replace("颜色=", "");
							}
							if (s.contains("款式=")) {
								styleNm = s.replace("款式=", "");
							}
						}
					}	
					
					detail.put("color_id", colorId);//颜色编号
					detail.put("style_id", styleId);//款式编号
					detail.put("color_nm", colorNm);//颜色名称
					detail.put("style_nm", styleNm);//款式名称
					detail.put("good_prc", 0);//商品金额 写死0
					detail.put("qty", 1);//数量 特殊处理后都是1
					
					detailList.add(detail);
				}
				order.put("detail", detailList);
				
				LinkedHashMap<String, Object> bodyMap = new LinkedHashMap<String, Object>();
				bodyMap.put("order", Arrays.asList(order));
				requestModel.setBody(bodyMap);
				
				ResponseModel responseModel = rsyncDuohuozhuSupport.callGateway(requestModel);
				if(null != responseModel && "00".equals(responseModel.getHeader().getResp_code())) {
					Map<String, Object> body = responseModel.getBody();
					List<Map<String, Object>> duohzOrderList = (List<Map<String, Object>>) body.get("order");
					Map<String, Object> duohzOrder = duohzOrderList.get(0);
					String errCode  = duohzOrder.get("err_code")+"";
					if("00".equals(errCode)) {
						String orderCode  = duohzOrder.get("cp_ord_id")+"";
						String outOrderCode  = duohzOrder.get("ord_id")+"";
						String site_no  = duohzOrder.get("site_no")+"";
						//回写发货仓库编号
						DbUp.upTable("oc_order_duohz").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "site_no", site_no, "cod_status", "P00"), "", "order_code");
						//回写外部订单号
						DbUp.upTable("oc_orderinfo").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "out_order_code", outOrderCode), "", "order_code");
						DbUp.upTable("oc_order_duohz_detail").dataUpdate(
								new MDataMap("order_code", orderInfo.get("order_code"), "order_id", outOrderCode), "", "order_code");
					}else {
						result.setResultCode(88);
						result.setResultMessage(duohzOrder.get("err_msg")+"");
					}
					
				}else {
					result.setResultCode(99);
					result.setResultMessage("创建订单失败!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(77);
			result.setResultMessage("创建订单失败!");
		}
		return result;
	}

	/**
	 * 根据惠家有areaCode查中文
	 * @param areaCode
	 * @return
	 */
	private Map<String, String> getRcvAdd1(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		
		List<MDataMap> list = new ArrayList<MDataMap>();
		// 递归获取本级以及父级地址
		getHjyAddressAndParent(list, areaCode);
		
		//按地址级别排序
		Collections.sort(list, new Comparator<MDataMap>() {
			@Override
			public int compare(MDataMap o1, MDataMap o2) {
				if(NumberUtils.toInt(o1.get("code_lvl")) - NumberUtils.toInt(o2.get("code_lvl"))>0) return 1;
				else return -1;
			}
		});
		
		String hjyAddressName = "";
		String levelFour = "";
		for (int i = 0; i < list.size(); i ++) {
			if(Integer.parseInt(list.get(i).get("code_lvl"))<=3 && "Y".equals(list.get(i).get("show_yn"))){
				hjyAddressName +=list.get(i).get("name");
			}
			if(Integer.parseInt(list.get(i).get("code_lvl"))==4 && "Y".equals(list.get(i).get("show_yn"))) {
				levelFour = list.get(i).get("name");
			}
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("hjyAddressName", hjyAddressName);
		result.put("levelFour", levelFour);
		return result;
	}
	
	private void getHjyAddressAndParent(List<MDataMap> list, String areaCode) {
		// 如果地址编码已经获取过了则直接返回
		for(MDataMap map : list) {
			if(map.get("code").equals(areaCode)) {
				return;
			}
		}
		
		List<MDataMap> res = DbUp.upTable("sc_tmp").queryAll("code,name,p_code,code_lvl,show_yn", "", "", new MDataMap("code", areaCode));
		if(!res.isEmpty()) {
			list.add(res.get(0));
			
			// 如果有父级则递归获取
			if(StringUtils.isNotBlank(res.get(0).get("p_code"))) {
				getHjyAddressAndParent(list,res.get(0).get("p_code"));
			}
		}
	}

}
 