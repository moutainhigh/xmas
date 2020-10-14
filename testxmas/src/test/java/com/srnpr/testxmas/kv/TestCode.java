package com.srnpr.testxmas.kv;


import java.util.Date;

import org.junit.Test;

import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.ThreadTestHelper;
import com.srnpr.zapdata.helper.KvHelper;

public class TestCode extends ThreadTestHelper {

	@Test
	public void test() {
		bLogTest(KvHelper.upCode("XX"));
		
		//bLogTest(DateHelper.parseDate("2020-01-01 00:00:00").getTime());

		threadPool(100000, 200);
		//singleThread(20000);

		/*
		bLogTest(KvHelper.upCode("PPDD"));

		bLogTest(KvHelper.upCode("XX"));
		bLogTest(KvHelper.upCode("XX"));
		*/
	}

	int i = 0;

	public void TestInsert() {
		i++;

		try {
			KvHelper.upCode("XX");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onRun() {
		TestInsert();
	}

}
