package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
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
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 积分/惠币占用、使用
 */
public class TeslaMakeCtrlAccmCrdtPpcServer  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();
		
		CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		
		if(teslaOrder.getUse().getIntegral() > 0){
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
			
			BigDecimal integralMoney = BigDecimal.ZERO;
			for(TeslaModelOrderPay op : orderPayList){
				// 积分
				if("449746280008".equals(op.getPayType())){
					childOrder = new UpdateCustAmtInput.ChildOrder();
					childOrder.setChildAccmAmt(op.getPayedMoney());
					childOrder.setAppChildOrdId(op.getOrderCode());
					custInput.getOrderList().add(childOrder);
					
					integralMoney = integralMoney.add(op.getPayedMoney());
					
					// 使用接口需要排掉TV品
					orderInfo = orderMap.get(op.getOrderCode());
					if(orderInfo != null && !"SI2003".equalsIgnoreCase(orderInfo.getSmallSellerCode()) 
							&& !"SI2009".equalsIgnoreCase(orderInfo.getSmallSellerCode())){
						childOrder = new UpdateCustAmtInput.ChildOrder();
						childOrder.setChildAccmAmt(op.getPayedMoney());
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
				
				// 记录积分变更日志
				MDataMap changeDataMap = new MDataMap();
				changeDataMap.put("member_code", teslaOrder.getUorderInfo().getBuyerCode());
				changeDataMap.put("cust_id", teslaOrder.getCustId());
				changeDataMap.put("change_type", "449748080003"); // 积分占用
				changeDataMap.put("change_money", integralMoney.toString());
				changeDataMap.put("remark", teslaOrder.getUorderInfo().getBigOrderCode());
				changeDataMap.put("create_time", FormatHelper.upDateTime());
				DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
			}
			
		}
		
		if(teslaOrder.getUse().getHjycoin() > 0){
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
				// 惠币
				if("449746280025".equals(op.getPayType())){
					childOrder = new UpdateCustAmtInput.ChildOrder();
					childOrder.setChildHcoinAmt(op.getPayedMoney());
					childOrder.setAppChildOrdId(op.getOrderCode());
					custInput.getOrderList().add(childOrder);
					
					// 使用接口需要排掉TV品
					orderInfo = orderMap.get(op.getOrderCode());
					if(orderInfo != null && !"SI2003".equalsIgnoreCase(orderInfo.getSmallSellerCode()) 
							&& !"SI2009".equalsIgnoreCase(orderInfo.getSmallSellerCode())){
						childOrder = new UpdateCustAmtInput.ChildOrder();
						childOrder.setChildHcoinAmt(op.getPayedMoney());
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
