package com.srnpr.testxmas.order;

import static org.junit.Assert.*;

import org.junit.Test;

import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmassystem.modelorder.OrderDetail;
import com.srnpr.xmassystem.modelorder.PlusModelOrder;
import com.srnpr.xmassystem.support.PlusSupportOrder;
import com.srnpr.zapcom.basehelper.TestHelper;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebResult;
import com.srnpr.zapweb.websupport.DataMapSupport;

public class TestOrder extends TestHelper {

	public void test() {
		testCart();
	}

	@Test
	public void testMap() {
		TeslaModelOrderInfo teslaModelOrderInfo = new TeslaModelOrderInfo();
		teslaModelOrderInfo.setOrderCode(KvHelper.upCode("TEST"));

		new DataMapSupport().saveToDb("oc_orderinfo", teslaModelOrderInfo);

	}

	/**
	 * 测试购物车
	 */
	public void testCart() {

		PlusModelOrder pOrder = new PlusModelOrder();

		// 添加SKU
		OrderDetail oDetail = new OrderDetail();
		oDetail.setSkuCode("133740");
		oDetail.setSkuNum(5);
		pOrder.getDetails().add(oDetail);

		MWebResult mResult = new PlusSupportOrder().refreshOrderProduct(pOrder);

		bLogTest(mResult.upJson());

	}

}
