package com.srnpr.xmasorder.channel.period;

import com.srnpr.xmasorder.channel.enumer.EPorscheExec;
import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.make.PorscheMakeAccount;
import com.srnpr.xmasorder.channel.make.PorscheMakeAdvanceBalance;
import com.srnpr.xmasorder.channel.make.PorscheMakeCheckAreaCode;
import com.srnpr.xmasorder.channel.make.PorscheMakeCheckChannel;
import com.srnpr.xmasorder.channel.make.PorscheMakeCreate;
import com.srnpr.xmasorder.channel.make.PorscheMakeFormat;
import com.srnpr.xmasorder.channel.make.PorscheMakeProductAreaLimit;
import com.srnpr.xmasorder.channel.make.PorscheMakeProductInfo;
import com.srnpr.xmasorder.channel.make.PorscheMakeSplit;
import com.srnpr.xmasorder.channel.make.PorscheMakeStock;
import com.srnpr.xmasorder.channel.make.PorscheMakeTransport;
import com.srnpr.xmasorder.channel.orderface.IPorscheOrder;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;

/**
 * 渠道商的订单
 * @remark 
 * @author 任宏斌
 * @date 2019年11月29日
 */
public class PorschePeriodOrder extends PorscheTopOrderMake {

	//区域编码校验
	private final IPorscheOrder porscheMakeCheckAreaCode = new PorscheMakeCheckAreaCode();
	
	// 初始化商品基本信息
	private final IPorscheOrder porscheMakeProductInfo = new PorscheMakeProductInfo();
	
	//校验渠道商状态 余额
	private final IPorscheOrder porscheMakeCheckChannel = new PorscheMakeCheckChannel();
	
	// 拆单
	private final IPorscheOrder porscheMakeSplit = new PorscheMakeSplit();
	
	//校验商品限购地区
	private final IPorscheOrder porscheMakeProductAreaLimit = new PorscheMakeProductAreaLimit();
	
	// 运费计算
	private final IPorscheOrder porscheMakeTransport = new PorscheMakeTransport();
	
	// 核算(重新计算订单价格)
	private final IPorscheOrder porscheMakeAccount = new PorscheMakeAccount();
	
	// 库存扣减
	private final IPorscheOrder porscheMakeStock = new PorscheMakeStock();
	
	//扣减预存款
	private final IPorscheOrder porscheMakeAdvanceBalance = new PorscheMakeAdvanceBalance();
	
	// 格式化订单信息
	private final IPorscheOrder porscheMakeFormat = new PorscheMakeFormat();
	
	// 创建订单
	private final IPorscheOrder porscheMakeCreate = new PorscheMakeCreate();

	
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		
		PorscheGtResult result = new PorscheGtResult();
		
		if (porscheGtOrder.getStatus().getExecStep() == EPorscheExec.Pre) {
			// 预下单
			result = orderProcss(porscheGtOrder, porscheMakeCheckAreaCode, porscheMakeProductInfo,
					porscheMakeCheckChannel, porscheMakeSplit, porscheMakeProductAreaLimit, porscheMakeTransport);
		} else if (porscheGtOrder.getStatus().getExecStep() == EPorscheExec.Create) {
			// 创建订单
			result = orderProcss(porscheGtOrder, porscheMakeCheckAreaCode, porscheMakeProductInfo,
					porscheMakeCheckChannel, porscheMakeSplit, porscheMakeProductAreaLimit, porscheMakeTransport,
					porscheMakeAccount, porscheMakeStock, porscheMakeAdvanceBalance, porscheMakeFormat,
					porscheMakeCreate);
		} else if (porscheGtOrder.getStatus().getExecStep() == EPorscheExec.Feight) {
			// 查询运费
			result = orderProcss(porscheGtOrder, porscheMakeCheckAreaCode, 
					porscheMakeProductInfo, porscheMakeSplit, porscheMakeTransport);
		}
		
		return result;
	}

	/**
	 * 订单处理流程启动类
	 * @param porscheGtOrder
	 * @param iOrders
	 * @return
	 */
	private PorscheGtResult orderProcss(PorscheGtOrder porscheGtOrder, IPorscheOrder... iOrders) {

		PorscheGtResult result = new PorscheGtResult();

		for (IPorscheOrder iPorscheOrder : iOrders) {

			PorscheGtResult porscheGtResult = iPorscheOrder.doRefresh(porscheGtOrder);
			if (porscheGtResult.upFlagTrue()) {

			} else {
				result = porscheGtResult;
				break;
			}

		}
		return result;
	}
}
