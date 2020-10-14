package com.srnpr.testxmas.kv;

import java.util.Map;

import org.junit.Test;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.EPlusScheduler;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.top.PlusTopScheduler;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basehelper.TestHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.kvdo.KvUp;

public class TestHash extends TestHelper {

	@Test
	public void testTopHash() {

		
		
		

	}

	public void testHash() {

		KvUp.upDefault().hset("test-ha", "a", "a1");
		KvUp.upDefault().hset("test-ha", "b", "b1");
		KvUp.upDefault().hset("test-ha", "c", "c1");

		MDataMap mDataMap = KvUp.upDefault().hgetAll("test-ha");

		bLogTest(mDataMap.get("a"));

	}

}
