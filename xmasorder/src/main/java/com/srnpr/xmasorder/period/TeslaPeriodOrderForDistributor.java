package com.srnpr.xmasorder.period;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.make.TeslaMakeAccount;
import com.srnpr.xmasorder.make.TeslaMakeCreate;
import com.srnpr.xmasorder.make.TeslaMakeFinish;
import com.srnpr.xmasorder.make.TeslaMakeFormat;
import com.srnpr.xmasorder.make.TeslaMakePayDistributor;
import com.srnpr.xmasorder.make.TeslaMakeProductDistributor;
import com.srnpr.xmasorder.make.TeslaMakeReckonFX;
import com.srnpr.xmasorder.make.TeslaMakeReckonIqiyi;
import com.srnpr.xmasorder.make.TeslaMakeSplit;
import com.srnpr.xmasorder.make.TeslaMakeStock;
import com.srnpr.xmasorder.orderface.ITeslaOrder;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 第三方经销商过来的订单
 * @author jlin
 *
 */
public class TeslaPeriodOrderForDistributor extends BaseClass implements ITeslaOrder {
	
	// 初始化商品基本信息
	private final ITeslaOrder teslaMakeProduct = new TeslaMakeProductDistributor();
	
	// 拆单
	private final ITeslaOrder teslaMakeSplit = new TeslaMakeSplit();
	
	//简单计算
	private final ITeslaOrder teslaMakeReckonIqiyi = new TeslaMakeReckonIqiyi();
	
	//简单计算--分销系统
	private final ITeslaOrder teslaMakeReckonFX = new TeslaMakeReckonFX();
	// 库存扣减
	private final ITeslaOrder teslaMakeStock = new TeslaMakeStock();
	// 核算(重新计算订单价格)--xie
	private final ITeslaOrder teslaMakeAccount = new TeslaMakeAccount();
	// 生成支付信息
	private final ITeslaOrder teslaMakePay = new TeslaMakePayDistributor();

	// 格式化订单信息
	private final ITeslaOrder teslaMakeFormat = new TeslaMakeFormat();
	
	// 创建订单
	private final ITeslaOrder teslaMakeCreate = new TeslaMakeCreate();

	// 各种通知及其他后续操作
	private final ITeslaOrder teslaMakeFinish = new TeslaMakeFinish();

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult result = new TeslaXResult();
		
		// 订单确认
		if (teslaOrder.getStatus().getExecStep() == ETeslaExec.iqiyi) {
			result =  orderProcss(teslaOrder,teslaMakeProduct,teslaMakeSplit,teslaMakeReckonIqiyi,teslaMakeStock,teslaMakePay,teslaMakeFormat,teslaMakeCreate,teslaMakeFinish);
			
		}else{
			//其他分销
			result =  orderProcss(teslaOrder,teslaMakeProduct,teslaMakeSplit,teslaMakeReckonFX,teslaMakeStock,teslaMakeAccount,teslaMakePay,teslaMakeFormat,teslaMakeCreate,teslaMakeFinish);
		}
		
		return result;
	}

	/**
	 * 订单处理方法
	 * 
	 * @param teslaOrder
	 * @param iOrders
	 * @return
	 */
	private TeslaXResult orderProcss(TeslaXOrder teslaOrder, ITeslaOrder... iOrders) {

		TeslaXResult result = new TeslaXResult();

		for (ITeslaOrder iTeslaOrder : iOrders) {

			TeslaXResult teslaXResult = iTeslaOrder.doRefresh(teslaOrder);
			if (teslaXResult.upFlagTrue()) {

			} else {
				result = teslaXResult;
				break;
			}

		}
		return result;
	}
}
