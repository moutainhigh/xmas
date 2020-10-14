package com.srnpr.testxmas.stock;

import org.junit.Test;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.helper.PlusHelperScheduler;
import com.srnpr.xmassystem.plusjob.PlusJobChangeStock;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.TestHelper;
import com.srnpr.zapdata.helper.KvHelper;

public class TestStock extends TestHelper {

	@Test
	public void test() {

		// XmasKv.upFactory(EKvSchema.Stock).set("aa", "vv");

		bLogTest(new PlusSupportStock().upAllStock("119823"));

		bLogTest(new PlusSupportStock().subtractSkuStock("x", "119823", 30));

		new PlusJobChangeStock().doExecute(null);
		bLogTest(new PlusSupportStock().upAllStock("119823"));

	}

}
