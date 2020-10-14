package com.srnpr.xmassystem;

import org.junit.Test;

import com.srnpr.xmassystem.modelbean.HjybeanConsumeSetModel;
import com.srnpr.xmassystem.modelbean.HjybeanProduceSetModel;
import com.srnpr.xmassystem.service.HjybeanService;
import com.srnpr.zapcom.basehelper.TestHelper;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestHelper{

	@Test
	public void testCall()
	{
		//abcd
		
		//xxxxs
		//ff
		
		//xxx
		
		
		//PlusSupportOrder pOrder=new PlusSupportOrder();
		//pOrder.upOrder("DD140120100014");
//		PlusModelSaleProObject pro = new PlusModelSaleProObject();
//		PlusModelProObject object = new PlusModelProObject();
//		
//		List<PlusModelProObject> model = new ArrayList<PlusModelProObject>();
//		List<String> list = new ArrayList<String>();
//		list.add("1111");
//		list.add("2222");
//		object.setSkuCode("123");
//		object.setSkuNum(2);
//		object.setBrandCode("1234567");
//		object.setCategoryCodes(list);
//		model.add(object);
//		pro.setSaleObject(model);
//		
//		PlusServiceSale sale = new PlusServiceSale();
		
	//	sale.getEventSale(pro, "SI2003");
		
		HjybeanService hs = new HjybeanService();
		HjybeanConsumeSetModel set = hs.getHomehasBeanConsumeConfig();
		
		HjybeanProduceSetModel set1 = hs.getHomehasBeanProduceConfig();
		
	}
	
	
}
