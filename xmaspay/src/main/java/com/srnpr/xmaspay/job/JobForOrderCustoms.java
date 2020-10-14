package com.srnpr.xmaspay.job;

import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;
import com.srnpr.xmaspay.response.PayCustomsResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 订单支付单号海关报关
 * @author zhaojunling
 *
 */
public class JobForOrderCustoms extends RootJobForExec{

	ConfigJobExec config = new ConfigJobExec();
	{
		config.setExecType("449746990005");
		config.setMaxExecNumber(5);
	}
	
	@Override
	public IBaseResult execByInfo(String orderCode) {
		MWebResult mWebResult = new MWebResult();
		
		// 如果已经推送过了则不再重复推送
		MDataMap mDataMap = PayServiceFactory.getInstance().getOrderCustomsService().getOrderCustoms(orderCode);
		if(mDataMap != null){
			return mWebResult; 
		}
		
		mDataMap = new MDataMap();
		mDataMap.put("order_code", orderCode);
		// 执行报关逻辑
		PayCustomsResponse payCustomsResponse = PayServiceFactory.getInstance().getPayCustomsProcess().process(mDataMap);
		
		// 报关结果
		if(AlipayUnifyResultCodeEnum.FAIL.name().equals(payCustomsResponse.getResultCode())){
			mWebResult.inErrorMessage(964101005, orderCode);
		}
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		return config;
	}

}
