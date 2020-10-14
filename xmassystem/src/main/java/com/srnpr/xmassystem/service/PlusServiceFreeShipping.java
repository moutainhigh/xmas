package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.modelevent.PlusModelEventFree;
import com.srnpr.xmassystem.modelevent.PlusModelFreeShipping;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportMember;


/**
 * 运费减免  暂时只供订单使用
 * @author zhouguohui
 *
 */
public class PlusServiceFreeShipping {

	/**
	 * 运费过滤
	 * @param memberCode  用户编号        用于判断是否首单免运费
	 * @param orderPrice  订单价格
	 * @param freePrice   当前运费价格
	 * @param sellerCode  系统编号
	 * @return  返回 运费价格
	 */
	public BigDecimal getFreeShipping(String memberCode,BigDecimal orderPrice,BigDecimal freePrice,String sellerCode){
		if(freePrice.compareTo(BigDecimal.ZERO)<=0){
			return BigDecimal.ZERO;
		}
		BigDecimal price =BigDecimal.ZERO;
		
		/**取是不是首单减运费**/
		boolean  firstOrder =true;
		if(StringUtils.isNotBlank(memberCode)){
			firstOrder= new PlusSupportMember().upFlagFirstOrder(memberCode);
		}
		/**取运费活动数据**/
		PlusModelEventFree eventFree = new PlusSupportEvent().upEventFreeByMangeCode(sellerCode);
		if(null==eventFree.getEventFree() || eventFree.getEventFree().size()<=0 ){
			return freePrice;
		}
		
		List<PlusModelFreeShipping> listFree = eventFree.getEventFree();
		
		boolean is_true = false;
		if(listFree!=null && listFree.size()>0){
			/**首单**/
			for(int i=0;i<listFree.size();i++){
				if(!firstOrder){
					break;
				}
				if(is_true){
					break;
				}
				PlusModelFreeShipping eventFreeObj = listFree.get(i);
				if(eventFreeObj.getFirstOrder().equals("449747710001")){
					continue;
				}
				
				
				/***订单金额已经满足***/
				if(orderPrice.compareTo(new BigDecimal(eventFreeObj.getFullPrice()))>=0){
					is_true =true;
					price=new BigDecimal(eventFreeObj.getCutPrice());
				}
				
			}
			
			
			/**不是首单**/
			for(int i=0;i<listFree.size();i++){
				
				if(firstOrder){
					break;
				}
				
				if(is_true){
					break;
				}
				
				PlusModelFreeShipping eventFreeObj = listFree.get(i);
				if(eventFreeObj.getFirstOrder().equals("449747710002")){
					continue;
				}
				
				/***订单金额已经满足***/
				if(orderPrice.compareTo(new BigDecimal(eventFreeObj.getFullPrice()))>=0){
					is_true =true;
					price=new BigDecimal(eventFreeObj.getCutPrice());
				}
				
			}
			
		}
		
		/**去运费最小值**/
		if(price.compareTo(freePrice)>=0 && is_true){
			price=freePrice;
		}
		if(!is_true){
			return freePrice;
		}
		return price;
		
	}
	
}
