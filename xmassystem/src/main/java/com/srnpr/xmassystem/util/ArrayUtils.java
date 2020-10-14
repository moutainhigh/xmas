package com.srnpr.xmassystem.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

	/**
	 * 给定一个列表，输出所有的组合，组合元素顺序以列表顺序为准<br>
	 * 输入：[1,2,3]
	 * 输出：[[1],[1,2],[1,3],[1,2,3],[2],[2,3],[3]]
	 * @param items
	 * @return
	 */
	/*public static <T> List<List<T>> combinationList(List<T> items) {
		items = new ArrayList<T>(items);
		
		List<List<T>> resultList = new ArrayList<List<T>>();
		
		List<T> subList;
		// 按位置循环一次外层列表
		for(int start = 0; start < items.size(); start++) {
			// 循环可能的列表长度
			for(int len = 1; len <= items.size() - start; len++) {
				// 单元素的情况特殊处理
				if(len == 1) {
					resultList.add(new ArrayList<T>(Arrays.asList(items.get(start))));
					continue;
				}
				// 循环可能的组合方式
				for(int i = (start + len - 1); i < items.size(); i++) {
					subList = new ArrayList<T>(items.subList(start, start + len - 1));
					subList.add(items.get(i));
					resultList.add(subList);
				}
			}
		}
		
		return resultList;
	}*/
	
	public static <T> List<List<T>> combinationList(List<T> items) {
		List<List<T>> result = new ArrayList<List<T>>();
		int len = items.size();
		int blen = 1 << len;
		for (int i = 1; i < blen; i++) {
			List<T> temp = new ArrayList<T>();
			for (int j = 0; j < len; j++) {
				if ((i << j & blen>>1) != 0) {
					temp.add(items.get(j));
				}
			}
			result.add(temp);
		}
		return result;
	}
}
