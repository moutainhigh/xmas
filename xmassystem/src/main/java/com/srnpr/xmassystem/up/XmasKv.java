package com.srnpr.xmassystem.up;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.zapdata.kvsupport.KvFactory;

public class XmasKv {

	public static KvFactory upFactory(EKvSchema eKvSchema) {
		return new KvFactory("xs-" + eKvSchema.toString() + "-");
	}

}
