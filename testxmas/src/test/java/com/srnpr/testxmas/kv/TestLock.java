package com.srnpr.testxmas.kv;

import org.junit.Test;

import com.srnpr.zapcom.basehelper.ThreadTestHelper;
import com.srnpr.zapdata.helper.KvHelper;

public class TestLock extends ThreadTestHelper {

	@Test
	public void testLock() {
		
		String[] sCodes = new String[] { "la", "lb", "lc", "1d" };

		String sLock = KvHelper.lockCodes(60, sCodes);
		bLogTest(sLock);
		bLogTest(KvHelper.lockCodes(10, sCodes));
		bLogTest(KvHelper.lockCodes(10, sCodes));
		bLogTest(KvHelper.lockCodes(10, sCodes));
		bLogTest(KvHelper.lockCodes(10, sCodes));
		KvHelper.unLockCodes("xx", sCodes);
		// KvHelper.unLockCodes(sLock, sCodes);
	}

	public void onRun() {
		// TODO Auto-generated method stub

	}

}
