package com.srnpr.xmaspay.process.impl;

import java.math.BigDecimal;

import com.srnpr.xmaspay.common.WechatCustomEnum;
import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.MerchantCustomsConfig;
import com.srnpr.xmaspay.config.face.IWechatCustomConfig;
import com.srnpr.xmaspay.process.IWechatCustomProcess;
import com.srnpr.xmaspay.request.WechatCustomRequest;
import com.srnpr.xmaspay.request.WechatRequest;
import com.srnpr.xmaspay.response.WechatCustomResponse;
import com.srnpr.xmaspay.service.IWechatCustomService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 微信通关解析
 * @author pang_jhui
 *
 */
public class WechatCustomProcess extends BasePayProcess implements IWechatCustomProcess {

	private MerchantCustomsConfig merchantCustomsConfig;
	
	@Override
	public WechatCustomResponse process(String orderCode,WechatCustomEnum customs,MDataMap mDataMap) {
		
		WechatCustomResponse wechatCustomResponse = new WechatCustomResponse();
		
		IWechatCustomService wechatCustomService  = (IWechatCustomService) getPayService();
		
		IWechatCustomConfig customConfig = (IWechatCustomConfig) getPayConfig();
		
		try {
			
			wechatCustomResponse = wechatCustomService.doProcess(initRequest(orderCode,customs,customConfig), customConfig);
			 
		} catch (Exception e) {
			
			wechatCustomResponse.setReturn_code(WechatUnifyResultCodeEnum.FAIL.name());
			
			wechatCustomResponse.setReturn_msg(e.toString());
			
		}
		
		return wechatCustomResponse;
		
	}

	@Override
	public WechatCustomRequest initRequest(String orderCode,WechatCustomEnum customs,IWechatCustomConfig config) throws Exception {
	
		WechatCustomRequest wechatRequest = new WechatCustomRequest();
		
		if(customs == null) throw new RuntimeException("缺少海关参数");
		
		// 检验订单数据
		MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		if(orderMap == null) throw new RuntimeException("未查到订单信息");
		
		// 必须有支付流水号，否则无法报关
		MDataMap payInfoMap = PayServiceFactory.getInstance().getOrderPayService().getWechatPaymentInfo(orderMap.get("big_order_code"));
		if(payInfoMap == null) throw new RuntimeException("未查到支付流水号");
		
		if(orderMap != null){
			
			String manageCode = orderMap.get("seller_code");
			// 商户编号
			String smallSellerCode = orderMap.get("small_seller_code");
			
			// TODO test
			/*  
			smallSellerCode = "SF03100294";
			orderCode = "DD2992128102";
			payInfoMap.put("transaction_id", "4010042001201603294373240310");
			payInfoMap.put("total_fee", "150.00");
			orderMap.put("due_money", "120.00");
			orderMap.put("transport_money", "0");
			*/
			
			wechatRequest.setAppid(payInfoMap.get("appid"));
			wechatRequest.setMch_id(payInfoMap.get("mch_id"));
			wechatRequest.setOut_trade_no(orderCode);  
			wechatRequest.setCustoms(customs.name());
			wechatRequest.setMch_customs_no(merchantCustomsConfig.getMerchantCustomsCode(smallSellerCode));
			wechatRequest.setTransaction_id(payInfoMap.get("transaction_id"));
			
			// 传入订单号和订单金额，所有订单都作为拆单的方式传入
			BigDecimal dueMoney = new BigDecimal(orderMap.get("due_money")).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal transportMoney = new BigDecimal(orderMap.get("transport_money")).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal productFee = dueMoney.subtract(transportMoney);
			
			// 微信的要求金额单位都是分，所以都要乘以100然后取整
			wechatRequest.setOrder_fee(dueMoney.multiply(new BigDecimal(100)).intValue()+"");
			wechatRequest.setProduct_fee(productFee.multiply(new BigDecimal(100)).intValue()+"");
			wechatRequest.setTransport_fee(transportMoney.multiply(new BigDecimal(100)).intValue()+"");
			wechatRequest.setSub_order_no(orderCode);
			wechatRequest.setFee_type("CNY");
			
			MDataMap orderAddress = PayServiceFactory.getInstance().getOrderPayService().getOrderAddressInfo(orderCode);
			if(orderAddress != null){
				// TODO test
				//wechatRequest.setCert_id("512928196703180015");
				//wechatRequest.setName("张建飞");
				//wechatRequest.setCert_type("IDCARD");
				
				wechatRequest.setCert_id(orderAddress.get("auth_idcard_number"));
				wechatRequest.setName(orderAddress.get("auth_true_name"));
				wechatRequest.setCert_type("IDCARD");
			}
			
			MDataMap mDataMap = BeanComponent.getInstance().objectToMap(wechatRequest,WechatRequest.class,true);
			
			String sign = config.getSign(manageCode, mDataMap);
			
			wechatRequest.setSign(sign);
			
		}
		
		return wechatRequest;
	
		
	}
	
	public void setMerchantCustomsConfig(MerchantCustomsConfig merchantCustomsConfig) {
		this.merchantCustomsConfig = merchantCustomsConfig;
	}

}
