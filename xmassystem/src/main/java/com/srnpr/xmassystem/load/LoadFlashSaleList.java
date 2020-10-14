package com.srnpr.xmassystem.load;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelFlashSale;
import com.srnpr.xmassystem.modelevent.PlusModelFlashSaleList;
import com.srnpr.xmassystem.plusconfig.PlusConfigFlashSaleList;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadFlashSaleList extends LoadTopMain<PlusModelFlashSaleList,PlusModelQuery>{

	/**
	 * 刷主库库存，暂时不需要
	 */
	@Override
	public PlusModelFlashSaleList topInitInfoMain(PlusModelQuery tQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查询走缓存的方法
	 */
	@Override
	public PlusModelFlashSaleList topInitInfo(PlusModelQuery tQuery) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date)+" 00:00:00";
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,1);
		String tomorrow = sdf.format(calendar.getTime())+" 00:00:00";
		calendar.add(calendar.DATE,1);//再加一，算后天
		String houtian = sdf.format(calendar.getTime())+" 00:00:00";
		String sqlToday = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010001' AND end_time >= sysdate() AND event_status = '4497472700020002' AND begin_time >= '"+today+"' AND begin_time < '"+tomorrow+"' order by begin_time asc";
		String sqlTomorrow = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010001' AND event_status = '4497472700020002' AND begin_time >= '"+tomorrow+"' AND begin_time < '"+houtian+"'  order by begin_time asc";
		List<Map<String,Object>> todalFlashSale = DbUp.upTable("sc_event_info").dataSqlList(sqlToday, null);
		List<Map<String,Object>> tomorrowFlashSale = DbUp.upTable("sc_event_info").dataSqlList(sqlTomorrow, null);
		if((todalFlashSale == null||todalFlashSale.size() == 0)&&(tomorrowFlashSale == null||tomorrowFlashSale.size() == 0)) {
			return null;
		}
		PlusModelFlashSaleList result = new PlusModelFlashSaleList();
		List<PlusModelFlashSale> todalFlashSaleList =new ArrayList<PlusModelFlashSale>();
		List<PlusModelFlashSale> tomorrowFlashSaleList = new ArrayList<PlusModelFlashSale>();
		for(Map<String,Object> map : todalFlashSale) {
			PlusModelFlashSale sale = new PlusModelFlashSale();
			sale.setBeginTime(map.get("begin_time").toString());
			sale.setEventCode(map.get("event_code").toString());
			sale.setChannels(map.get("channels").toString());
			todalFlashSaleList.add(sale);
		}
		for(Map<String,Object> map : tomorrowFlashSale) {
			PlusModelFlashSale sale = new PlusModelFlashSale();
			sale.setBeginTime(map.get("begin_time").toString());
			sale.setEventCode(map.get("event_code").toString());
			sale.setChannels(map.get("channels").toString());
			tomorrowFlashSaleList.add(sale);
		}
		result.setTodayFlashSaleEventList(todalFlashSaleList);
		result.setTomorrowFlashSaleEventList(tomorrowFlashSaleList);
		return result;
	}
	private final static PlusConfigFlashSaleList PLUS_CONFIG = new PlusConfigFlashSaleList();
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date()));
	}

}
