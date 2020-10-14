package com.srnpr.xmassystem.modelevent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 秒杀列表页，返回规则：开始时间是今日的秒杀活动，以及两条明日的即将开始
 * @author Angel Joy
 *
 */
public class PlusModelFlashSaleList implements IPlusModel {
	@ZapcomApi(value="今日秒杀时间列表",remark="")
	private List<PlusModelFlashSale> todayFlashSaleEventList = new ArrayList<PlusModelFlashSale>();
	@ZapcomApi(value="明日秒杀时间列表",remark="")
	private List<PlusModelFlashSale> tomorrowFlashSaleEventList = new ArrayList<PlusModelFlashSale>();
	public List<PlusModelFlashSale> getTodayFlashSaleEventList() {
		return todayFlashSaleEventList;
	}
	public void setTodayFlashSaleEventList(List<PlusModelFlashSale> todayFlashSaleEventList) {
		this.todayFlashSaleEventList = todayFlashSaleEventList;
	}
	public List<PlusModelFlashSale> getTomorrowFlashSaleEventList() {
		return tomorrowFlashSaleEventList;
	}
	public void setTomorrowFlashSaleEventList(List<PlusModelFlashSale> tomorrowFlashSaleEventList) {
		this.tomorrowFlashSaleEventList = tomorrowFlashSaleEventList;
	}

	
}
