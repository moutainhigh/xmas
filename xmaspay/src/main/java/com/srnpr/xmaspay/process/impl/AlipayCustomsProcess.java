package com.srnpr.xmaspay.process.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmaspay.common.AlipayCustomsEnum;
import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;
import com.srnpr.xmaspay.config.MerchantCustomsConfig;
import com.srnpr.xmaspay.config.face.IAlipayCustomsConfig;
import com.srnpr.xmaspay.process.IAlipayCustomsProcess;
import com.srnpr.xmaspay.request.AlipayCustomsRequest;
import com.srnpr.xmaspay.request.AlipayUnifyRequest;
import com.srnpr.xmaspay.response.AlipayCustomsResponse;
import com.srnpr.xmaspay.service.IAlipayCustomsService;
import com.srnpr.xmaspay.util.BeanComponent;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basemodel.MDataMap;

/**
 * 支付宝报关流程
 * @author zhaojunling
 *
 */
public class AlipayCustomsProcess extends BasePayProcess implements IAlipayCustomsProcess {

	private MerchantCustomsConfig merchantCustomsConfig;
	
	@Override
	public AlipayCustomsResponse process(String orderCode,AlipayCustomsEnum customs,MDataMap mDataMap) {
		
		AlipayCustomsResponse alipayCustomsResponse = new AlipayCustomsResponse();
		
		IAlipayCustomsService alipayCustomsService  = (IAlipayCustomsService)getPayService();
		
		IAlipayCustomsConfig customConfig = (IAlipayCustomsConfig) getPayConfig();
		
		try {
			
			alipayCustomsResponse = alipayCustomsService.doProcess(initRequest(orderCode,customs,customConfig), customConfig);
			 
		} catch (Exception e) {
			
			alipayCustomsResponse.setIs_success(AlipaySuccessEnum.F.name());
			alipayCustomsResponse.setResult_code(AlipayUnifyResultCodeEnum.FAIL.name());
			alipayCustomsResponse.setError(e.getMessage());
			
		}
		
		return alipayCustomsResponse;
		
	}

	private AlipayCustomsRequest initRequest(String orderCode,AlipayCustomsEnum customs,IAlipayCustomsConfig config) throws Exception {
	
		AlipayCustomsRequest customsRequest = new AlipayCustomsRequest();
		
		if(customs == null) throw new RuntimeException("缺少海关参数");
		
		// 检验订单数据
		MDataMap orderMap = PayServiceFactory.getInstance().getOrderPayService().upOrderInfo(orderCode);
		if(orderMap == null) throw new RuntimeException("未查到订单信息");
		
		// 必须有支付流水号，否则无法报关
		MDataMap payInfoMap = PayServiceFactory.getInstance().getOrderPayService().getAlipayPaymentInfo(orderMap.get("big_order_code"));
		if(payInfoMap == null) throw new RuntimeException("未查到支付流水号");
		
		// 商户编号
		String smallSellerCode = orderMap.get("small_seller_code");
		
		// TODO test
		/*
		smallSellerCode = "SF03100294";
		orderCode = "DD2997409102";
		payInfoMap.put("trade_no", "2016033021001004700298497498");
		payInfoMap.put("total_fee", "226.00");
		orderMap.put("due_money", "226.00");
		orderMap.put("transport_money", "0");
		*/
		
		customsRequest.setSub_out_biz_no(orderCode);
		customsRequest.setService(config.getMethodCustoms());
		customsRequest.setPartner(config.getPartner());
		customsRequest.set_input_charset(config.getRequestCharset());
		customsRequest.setCustoms_place(customs.name());
		
		customsRequest.setOut_request_no(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+(RandomUtils.nextInt(1000)+1000));
		customsRequest.setTrade_no(payInfoMap.get("trade_no"));
		customsRequest.setMerchant_customs_code(merchantCustomsConfig.getMerchantCustomsCode(smallSellerCode));
		customsRequest.setMerchant_customs_name(merchantCustomsConfig.getMerchantCustomsName(smallSellerCode));
		customsRequest.setAmount(new BigDecimal(orderMap.get("due_money")).setScale(2, BigDecimal.ROUND_HALF_UP));
		customsRequest.setIs_split("F");
		
		BigDecimal payedMoney = new BigDecimal(payInfoMap.get("total_fee")).setScale(2, BigDecimal.ROUND_HALF_UP);
		// 如果订单的应付金额不等于支付流水号对应的金额则作为拆单情况处理
		if(payedMoney.compareTo(customsRequest.getAmount()) != 0){
			customsRequest.setIs_split("T");
		}
		
		// 计算签名
		customsRequest.setSign(config.getSign(BeanComponent.getInstance().objectToMap(customsRequest,AlipayUnifyRequest.class,true)));
		// sign_type 在此接口中不参与签名
		customsRequest.setSign_type("MD5");
		
		return customsRequest;
	}
	
	public void setMerchantCustomsConfig(MerchantCustomsConfig merchantCustomsConfig) {
		this.merchantCustomsConfig = merchantCustomsConfig;
	}

}
