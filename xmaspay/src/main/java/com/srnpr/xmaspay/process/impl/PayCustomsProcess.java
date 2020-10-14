package com.srnpr.xmaspay.process.impl;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmaspay.common.AlipayCustomsEnum;
import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.xmaspay.common.WechatCustomEnum;
import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.MerchantCustomsConfig;
import com.srnpr.xmaspay.process.IPayCustomsProcess;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;
import com.srnpr.xmaspay.response.PayCustomsResponse;
import com.srnpr.xmaspay.response.WechatCustomResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 订单报关业务统一入口实现
 * @author zhaojunling
 *
 */
public class PayCustomsProcess implements IPayCustomsProcess {
	
	private MerchantCustomsConfig merchantCustomsConfig;
	
	@Override
	public PayCustomsResponse process(MDataMap mDataMap) {
		String orderCode = StringUtils.trimToEmpty(mDataMap.get("orderCode"));
		PayCustomsResponse payCustomsResponse = new PayCustomsResponse();
		payCustomsResponse.setOrderCode(orderCode);
		
		MDataMap orderInfoMap = PayServiceFactory.getInstance().getOrderPayService().getOrderInfo(orderCode);
		if(orderInfoMap == null){
			payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
			payCustomsResponse.setResultMsg("订单号不存在");
			return payCustomsResponse;
		}
		
		// 判断支付平台使用分别查询的方式
		String payType = null;
		if(PayServiceFactory.getInstance().getOrderPayService().getAlipayPaymentInfo(orderInfoMap.get("big_order_code")) != null){
			payType = Constants.ORDER_PAY_TYPE_ALIPAY;
		}else if(PayServiceFactory.getInstance().getOrderPayService().getWechatPaymentInfo(orderInfoMap.get("big_order_code")) != null){
			payType = Constants.ORDER_PAY_TYPE_WECHAT;
		}
		
		String smallSellerCode = orderInfoMap.get("small_seller_code");
		
		// TODO test
		//smallSellerCode = "SF03100294";
		
		if(!merchantCustomsConfig.getCustomsEnabled(smallSellerCode)){
			payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
			payCustomsResponse.setResultMsg("商户报关参数未启用");
			return payCustomsResponse;
		}
		
		// 海关代码
		String customsCode = StringUtils.trimToEmpty(merchantCustomsConfig.getCustomsCode(smallSellerCode));
		
		// 根据不同的订单支付平台，调用不同的报关程序
		if(Constants.ORDER_PAY_TYPE_ALIPAY.equals(payType)){
			AlipayCustomsEnum customsEnum = null;
			
			try {
				customsEnum = AlipayCustomsEnum.valueOf(customsCode);
			} catch (Exception e) {}
			
			if(customsEnum == null){
				payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
				payCustomsResponse.setResultMsg("不支持的支付宝海关编号枚举值");
				return payCustomsResponse;
			}
			
			// 支付宝报关
			AlipayCustomsResponse alipayCustomsResponse = PayServiceFactory.getInstance().getAlipayCustomsProcess().process(orderCode, customsEnum, mDataMap);
			payCustomsResponse.setTradeNo(alipayCustomsResponse.getTrade_no());
			
			// 请求成功不一定处理成功
			if(AlipaySuccessEnum.T.name().equals(alipayCustomsResponse.getIs_success())){
				payCustomsResponse.setResultCode(alipayCustomsResponse.getResult_code());
				payCustomsResponse.setResultMsg(alipayCustomsResponse.getDetail_error_des());
			}else{
				payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
				payCustomsResponse.setResultMsg(alipayCustomsResponse.getError());
			}
		}else if(Constants.ORDER_PAY_TYPE_WECHAT.equals(payType)){
			WechatCustomEnum customsEnum = null;
			
			try {
				customsEnum = WechatCustomEnum.valueOf(customsCode);
			} catch (Exception e) {}
			
			if(customsEnum == null){
				payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
				payCustomsResponse.setResultMsg("不支持的微信支付海关编号枚举值");
				return payCustomsResponse;
			}
			
			// 微信报关
			WechatCustomResponse wechatCustomResponse = (WechatCustomResponse)PayServiceFactory.getInstance().getWechatCustomProcess().process(orderCode, customsEnum, mDataMap);
			payCustomsResponse.setTradeNo(wechatCustomResponse.getSub_order_id());
			
			if(WechatUnifyResultCodeEnum.SUCCESS.name().equals(wechatCustomResponse.getReturn_code())){
				payCustomsResponse.setResultCode(wechatCustomResponse.getResult_code());
				payCustomsResponse.setResultMsg(wechatCustomResponse.getErr_code_des());
			}else{
				payCustomsResponse.setResultCode(wechatCustomResponse.getReturn_code());
				payCustomsResponse.setResultMsg(wechatCustomResponse.getReturn_msg());
			}
		}else{
			payCustomsResponse.setResultCode(AlipayUnifyResultCodeEnum.FAIL.name());
			payCustomsResponse.setResultMsg("不支持的支付方式");
			return payCustomsResponse;
		}
		
		return payCustomsResponse;
		
	}
	
	public void setMerchantCustomsConfig(MerchantCustomsConfig merchantCustomsConfig) {
		this.merchantCustomsConfig = merchantCustomsConfig;
	}
}
