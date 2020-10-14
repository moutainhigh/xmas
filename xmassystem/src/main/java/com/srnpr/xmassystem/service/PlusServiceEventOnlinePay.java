package com.srnpr.xmassystem.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadEventOnlinePay;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventOnlinePay;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.plusquery.PlusQueryOrder;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basehelper.FormatHelper;

/**
 * 在线支付立减活动
 */
public class PlusServiceEventOnlinePay {

	LoadEventOnlinePay loadEventOnlinePay = new LoadEventOnlinePay();
	LoadEventInfo loadEventInfo = new LoadEventInfo();
	
	/***
	 * 获取当前可用的下单立减活动信息
	 * @return
	 */
	public PlusModelEventOnlinePay getEventOnlinePay(String channelId) {
		PlusQueryOrder query = new PlusQueryOrder();
		query.setCode("SI2003");
		List<PlusModelEventOnlinePay> list = loadEventOnlinePay.upInfoByCode(query).getList();
		String sys_time = FormatHelper.upDateTime();
		
		PlusModelEventQuery q = new PlusModelEventQuery();
		PlusModelEventInfo eventInfo = null;
		for(PlusModelEventOnlinePay item : list) {
			if(PlusSupportEvent.compareDate(sys_time,item.getEndTime())<=0 && PlusSupportEvent.compareDate(item.getBeginTime(),sys_time)<=0) {
				q.setCode(item.getEventCode());
				eventInfo = loadEventInfo.upInfoByCode(q);
				
				// 检查一下活动状态
				if(eventInfo != null && eventInfo.getEventStatus().equals("4497472700020002")
						&& (StringUtils.isBlank(eventInfo.getChannels()) || eventInfo.getChannels().contains(channelId))){
					return item;
				}
			}
		}
			
		return null;
	}
	
	
	
}
