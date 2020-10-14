package com.srnpr.xmaspay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.config.face.IApplePayConfig;
import com.srnpr.xmaspay.model.ApplePayRiskItem;
import com.srnpr.xmaspay.request.ApplePayRequest;
import com.srnpr.xmaspay.response.ApplePayResponse;
import com.srnpr.xmaspay.service.IApplePayService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * applepay业务处理实现类
 * @author pang_jhui
 * @author zhaojunling
 *
 */
public class ApplePayServiceImpl implements IApplePayService {

	@Override
	public ApplePayResponse doProcess(ApplePayRequest applePayRequest, IApplePayConfig payConfig) {
		
		MDataMap mDataMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(applePayRequest.getOrderCode());
		
		ApplePayResponse applePayResponse = new ApplePayResponse();
		
		if(mDataMap != null){	
			
			applePayResponse.setBusi_partner(payConfig.getBusiType());
			
			Date createDate = DateUtil.toDate(mDataMap.get("create_time"), DateUtil.sdfDateTime);
			
			applePayResponse.setDt_order(DateUtil.sdfDateTimeTamp.format(createDate));
			
			applePayResponse.setMoney_order(mDataMap.get("due_money"));
			
			applePayResponse.setNo_order(applePayRequest.getOrderCode());
			
			applePayResponse.setNotify_url(payConfig.getNotifyUrl());
			
			applePayResponse.setOid_partner(payConfig.getMerchantId());
			
			ApplePayRiskItem riskItem = new ApplePayRiskItem();
			
			JsonHelper<ApplePayRiskItem> riskItemHelper = new JsonHelper<ApplePayRiskItem>();
			
			applePayResponse.setRisk_item(riskItemHelper.ObjToString(riskItem));
			
			applePayResponse.setSign_type(payConfig.getSignType());
			
			/*订单有效时间，默认为分钟*/
			int valid_time = PayServiceFactory.getInstance().getOrderPayService()
					.getOrderValidTime(applePayRequest.getOrderCode());
			
			if(valid_time == 0) valid_time = 24*60;
			applePayResponse.setValid_order(valid_time+"");
			
			try {
				
				MDataMap orderAddressMap = PayServiceFactory.getInstance().getOrderPayService().getOrderAddressInfo(applePayRequest.getOrderCode());
				MDataMap memberInfoMap = PayServiceFactory.getInstance().getOrderPayService().getMemberInfo(mDataMap.get("buyer_code"));
				Map<String,Object> riskMap = new HashMap<String,Object>();
				// 商品分类
				riskMap.put("frms_ware_category", "4001");
				// 用户唯一编号
				//riskMap.put("user_info_mercht_userno", mDataMap.get("buyer_code"));
				// 用户注册时间
				riskMap.put("user_info_dt_registe", memberInfoMap.get("create_time").replaceAll("[-:\\s]", ""));
				// 收货地址省编码
				riskMap.put("delivery_addr_province", orderAddressMap.get("area_code").substring(0, 2)+"0000");
				// 收获地址市编码
				riskMap.put("delivery_addr_city", orderAddressMap.get("area_code").substring(0, 4)+"00");
				// 收货人手机号
				riskMap.put("delivery_phone", orderAddressMap.get("mobilephone"));
				// 物流方式
				riskMap.put("logistics_mode", "普通快递");
				// 发货时间
				riskMap.put("delivery_cycle", "24h");
				
				applePayResponse.setRisk_item(new JSONObject(riskMap).toJSONString());
				
				MDataMap requestDataMap = BeanComponent.getInstance().objectToMap(applePayResponse, null, false);
				
				// 排除不需要签名的属性
				requestDataMap.remove("sign");
				requestDataMap.remove("resultcode");
				requestDataMap.remove("resultmsg");
				
				String sign = payConfig.getSign(requestDataMap);
				
				applePayResponse.setUser_id(mDataMap.get("buyer_code"));
				applePayResponse.setSign(sign);
				applePayResponse.setAp_merchant_id(payConfig.getApMerchantId());
				
			} catch (Exception e) {
				
				applePayResponse.setResultcode(-1);
				
				applePayResponse.setResultmsg(e.getMessage());
				
			}
			
			
		} else {

			applePayResponse.setResultcode(-1);

			applePayResponse.setResultmsg("订单编号：【" + applePayRequest.getOrderCode() + "】在系统中不存在");

		}
		
		return applePayResponse;
		
	}
	
	

}
