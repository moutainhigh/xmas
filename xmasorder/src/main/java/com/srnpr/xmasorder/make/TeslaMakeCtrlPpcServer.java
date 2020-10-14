package com.srnpr.xmasorder.make;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 储值金占用、使用
 */
public class TeslaMakeCtrlPpcServer  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();
		
		CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		
		if(teslaOrder.getUse().getCzj_money() > 0){
			List<TeslaModelOrderPay> orderPayList = teslaOrder.getOcOrderPayList();
			
			List<TeslaModelOrderInfo> orderList = teslaOrder.getSorderInfo();
			Map<String,TeslaModelOrderInfo> orderMap = new HashMap<String,TeslaModelOrderInfo>();
			for(TeslaModelOrderInfo order : orderList){
				orderMap.put(order.getOrderCode(), order);
			}
			
			UpdateCustAmtInput custInput = new UpdateCustAmtInput();   // 占用
			UpdateCustAmtInput custInputC = new UpdateCustAmtInput();  // 使用
			TeslaModelOrderInfo orderInfo = null;
			UpdateCustAmtInput.ChildOrder childOrder = null;
			for(TeslaModelOrderPay op : orderPayList){
				// 储值金
				if("449746280006".equals(op.getPayType())){
					childOrder = new UpdateCustAmtInput.ChildOrder();
					childOrder.setChildPpcAmt(op.getPayedMoney());
					childOrder.setAppChildOrdId(op.getOrderCode());
					custInput.getOrderList().add(childOrder);
					
					// 使用接口需要排掉TV品
					orderInfo = orderMap.get(op.getOrderCode());
					if(orderInfo != null && !"SI2003".equalsIgnoreCase(orderInfo.getSmallSellerCode()) 
							&& !"SI2009".equalsIgnoreCase(orderInfo.getSmallSellerCode())){
						childOrder = new UpdateCustAmtInput.ChildOrder();
						childOrder.setChildPpcAmt(op.getPayedMoney());
						childOrder.setAppChildOrdId(op.getOrderCode());
						custInputC.getOrderList().add(childOrder);
					}
				}
			}
			
			// 如果存在则调用占用接口
			if(!custInput.getOrderList().isEmpty()){
				custInput.setBigOrderCode(teslaOrder.getUorderInfo().getBigOrderCode());
				custInput.setCustId(teslaOrder.getCustId());
				custInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.C);
				RootResult res = custRelAmtRef.updateCustAmt(custInput);
				if(res.getResultCode() != 1){
					xResult.inOtherResult(res);
					return xResult;
				}
				
				// 调用占用接口成功后再调用一次使用的接口
				try{
					if(!custInputC.getOrderList().isEmpty()){
						custInputC.setBigOrderCode(teslaOrder.getUorderInfo().getBigOrderCode());
						custInputC.setCustId(teslaOrder.getCustId());
						custInputC.setCurdFlag(UpdateCustAmtInput.CurdFlag.U);
						res = custRelAmtRef.updateCustAmt(custInputC);
						if(res.getResultCode() != 1){
							LogFactory.getLog(getClass()).warn("家有接口调用失败["+res.getResultCode()+"]: "+res.getResultMessage());
						}
					}
				}catch(Exception e){
					LogFactory.getLog(getClass()).warn("家有接口调用异常", e);
				}
				
			}
			
		}
		return xResult;
	}
	
	

}
