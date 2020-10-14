package com.srnpr.xmaspay;

import com.srnpr.xmaspay.common.WechatCustomEnum;
import com.srnpr.xmaspay.response.WechatCustomResponse;
import com.srnpr.xmaspay.util.PayServiceFactory;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopTest;

/**
 * Unit test for simple App.
 */
public class AppTest extends TopTest {
	@org.junit.Test
	public void run() {

		WechatCustomResponse response = (WechatCustomResponse) PayServiceFactory.getInstance().getWechatCustomProcess()
				.process("DD150723100208", WechatCustomEnum.NINGBO, new MDataMap());

		System.out.println(response.getSub_order_no() + "----------------" + response.getSub_order_id()
				+ response.getResult_code() + "-------" + response.getErr_code_des());

	}

}
