package com.srnpr.xmassystem.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TimeCostUtils {
	
	private Map<String,Long> startMap,costMap;
	
	public TimeCostUtils () {
		startMap = new HashMap<String, Long>();
		costMap = new HashMap<String, Long>();
	}

	public void begin(String name) {
		startMap.put(name, System.nanoTime());
	}
	
	public void end(String name) {
		costMap.put(name, System.nanoTime() - startMap.get(name));
	}
	
	public String println() {
		StringBuilder b = new StringBuilder();
		for(Entry<String, Long> entry : costMap.entrySet()) {
			b.append(entry.getKey()).append(",").append(entry.getValue() / 1000000).append("\n");
		}
		System.out.println(b.toString());
		return b.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{");
		for(Entry<String, Long> entry : costMap.entrySet()) {
			if(b.length() > 0) b.append(",");
			b.append(entry.getKey()).append("=").append(entry.getValue() / 1000000);
		}
		b.append("}");
		return b.toString();
	}

	public static TimeCostUtils create() {
		return new TimeCostUtils();
	}
}
