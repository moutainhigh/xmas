package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelorder.OrderInfo;
import com.srnpr.xmassystem.modelorder.PlusModelOrder;
import com.srnpr.xmassystem.plusconfig.PlusConfigOrder;
import com.srnpr.xmassystem.plusquery.PlusQueryOrder;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basehelper.MapHelper;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadOrder extends LoadTop<PlusModelOrder, PlusQueryOrder> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.srnpr.xmassystem.top.LoadTop#topInitInfo(com.srnpr.xmassystem.face
	 * .IPlusQuery)
	 */
	public PlusModelOrder topInitInfo(PlusQueryOrder tQuery) {

		String sOrderCode = tQuery.getCode();

		PlusModelOrder plusModelOrder = new PlusModelOrder();

//		plusModelOrder.setInfo(MapHelper.serialize(DbUp.upTable("oc_orderinfo")
//				.one("order_code", sOrderCode), new OrderInfo()));

		return plusModelOrder;
	}

	private final static PlusConfigOrder PLUS_CONFIG = new PlusConfigOrder();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
