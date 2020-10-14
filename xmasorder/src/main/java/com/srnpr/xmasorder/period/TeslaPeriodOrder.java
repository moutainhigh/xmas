package com.srnpr.xmasorder.period;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.make.TeslaMakeAccount;
import com.srnpr.xmasorder.make.TeslaMakeActivity;
import com.srnpr.xmasorder.make.TeslaMakeAdress;
import com.srnpr.xmasorder.make.TeslaMakeAgentOrder;
import com.srnpr.xmasorder.make.TeslaMakeBalanced;
import com.srnpr.xmasorder.make.TeslaMakeBinfenSmg;
import com.srnpr.xmasorder.make.TeslaMakeBlacklisted;
import com.srnpr.xmasorder.make.TeslaMakeCheckCodeLevel;
import com.srnpr.xmasorder.make.TeslaMakeCheckJdSku;
import com.srnpr.xmasorder.make.TeslaMakeCollageValidate;
import com.srnpr.xmasorder.make.TeslaMakeContraband;
import com.srnpr.xmasorder.make.TeslaMakeCouponSelect;
import com.srnpr.xmasorder.make.TeslaMakeCreate;
import com.srnpr.xmasorder.make.TeslaMakeCtrlAccmCrdtPpcServer;
import com.srnpr.xmasorder.make.TeslaMakeCtrlCrdtServer;
import com.srnpr.xmasorder.make.TeslaMakeCtrlPpcServer;
import com.srnpr.xmasorder.make.TeslaMakeEvent;
import com.srnpr.xmasorder.make.TeslaMakeEventFO;
import com.srnpr.xmasorder.make.TeslaMakeEventOnlinePay;
import com.srnpr.xmasorder.make.TeslaMakeEventPlus;
import com.srnpr.xmasorder.make.TeslaMakeFarmProduct;
import com.srnpr.xmasorder.make.TeslaMakeFinish;
import com.srnpr.xmasorder.make.TeslaMakeFormat;
import com.srnpr.xmasorder.make.TeslaMakeGiftSkuInfo;
import com.srnpr.xmasorder.make.TeslaMakeGiveIntegral;
import com.srnpr.xmasorder.make.TeslaMakeHudongEvent;
import com.srnpr.xmasorder.make.TeslaMakeInit;
import com.srnpr.xmasorder.make.TeslaMakeInitForPC;
import com.srnpr.xmasorder.make.TeslaMakeLock;
import com.srnpr.xmasorder.make.TeslaMakeOccupyAmt;
import com.srnpr.xmasorder.make.TeslaMakeOrderInfo;
import com.srnpr.xmasorder.make.TeslaMakeOrderInfoHandle;
import com.srnpr.xmasorder.make.TeslaMakePointShop;
import com.srnpr.xmasorder.make.TeslaMakeProduct;
import com.srnpr.xmasorder.make.TeslaMakeProductAreaLimit;
import com.srnpr.xmasorder.make.TeslaMakeReckon;
import com.srnpr.xmasorder.make.TeslaMakeRedeemProduct;
import com.srnpr.xmasorder.make.TeslaMakeShareOrder;
import com.srnpr.xmasorder.make.TeslaMakeShopCartShow;
import com.srnpr.xmasorder.make.TeslaMakeShow;
import com.srnpr.xmasorder.make.TeslaMakeSpecialEvent;
import com.srnpr.xmasorder.make.TeslaMakeSpellGoods;
import com.srnpr.xmasorder.make.TeslaMakeSplit;
import com.srnpr.xmasorder.make.TeslaMakeStock;
import com.srnpr.xmasorder.make.TeslaMakeTgzOrder;
import com.srnpr.xmasorder.make.TeslaMakeThirdOrderDetail;
import com.srnpr.xmasorder.make.TeslaMakeTransport;
import com.srnpr.xmasorder.make.TeslaMakeVJPay;
import com.srnpr.xmasorder.make.TeslaMakeValidateStock;
import com.srnpr.xmasorder.make.TeslaMakeVoucherSplit;
import com.srnpr.xmasorder.make.TeslaMakeYYG;
import com.srnpr.xmasorder.orderface.ITeslaOrder;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.baseclass.BaseClass;

public class TeslaPeriodOrder extends BaseClass implements ITeslaOrder {

	// 校验黑名单信息--shi
	private final ITeslaOrder teslaMakeBlacklisted  = new TeslaMakeBlacklisted();
	// 初始化基本信息--shi
	private final ITeslaOrder teslaMakeInit = new TeslaMakeInit();

	// PC版订单创建 重写teslaMakeInit |为了兼容内购下单 - 2016-06-07 - Yangcl   
	private final ITeslaOrder teslaMakeInitPC = new TeslaMakeInitForPC();

	// 锁定各种需要锁定(创建订单使用)--shi
	private final ITeslaOrder teslaMakeLock = new TeslaMakeLock();

	// 初始化商品基本信息--shi
	private final ITeslaOrder teslaMakeProduct = new TeslaMakeProduct();
	
	// 调用实时接口验证LD商品库存
	private final ITeslaOrder teslaMakeValidateStock = new TeslaMakeValidateStock();
	
	// 微匠支付订单过滤
	private final TeslaMakeVJPay teslaMakeVJPay = new TeslaMakeVJPay();

	// 拆单--xie
	private final ITeslaOrder teslaMakeSplit = new TeslaMakeSplit();

	// 简单计算--xie
	private final ITeslaOrder teslaMakeReckon = new TeslaMakeReckon();

	// 互动活动
	private final ITeslaOrder teslaMakeHuDongEvent = new TeslaMakeHudongEvent();
	
	// 促销--xie
	private final ITeslaOrder teslaMakeEvent = new TeslaMakeEvent();
	
	// 促销(满减类)--xie
	private final ITeslaOrder teslaMakeEventFO = new TeslaMakeEventFO();
	
	// 橙意卡专享
	private final ITeslaOrder teslaMakeEventPlus = new TeslaMakeEventPlus();
	
	// 在线支付立减
	private final ITeslaOrder teslaMakeEventOnlinePay = new TeslaMakeEventOnlinePay();
	
	// 用户可使用的优惠券
	private final ITeslaOrder teslaMakeCouponSelect = new TeslaMakeCouponSelect();
	
	// 促销(购物车展示使用)--xie
	private final ITeslaOrder teslaMakeShopCartShow = new TeslaMakeShopCartShow();
	
	//添加外联赠品   fq++
	private final ITeslaOrder teslaMakeGiftSkuInfo = new TeslaMakeGiftSkuInfo();
	
	//校验商品限购地区
	private final ITeslaOrder teslaMakeProductAreaLimit = new TeslaMakeProductAreaLimit();
	
	// 运费计算 如果有运费免减也在此类中实现--jiang
	private final ITeslaOrder teslaMakeTransport = new TeslaMakeTransport();
	// 核算(重新计算订单价格)--xie
	private final ITeslaOrder teslaMakeAccount = new TeslaMakeAccount();

	// 均衡分配各种金额--xie
	private final ITeslaOrder teslaMakeBalanced = new TeslaMakeBalanced();

	// 展示信息-shi
	private final ITeslaOrder teslaMakeShow = new TeslaMakeShow();
	
	// 一元购信息处理 -- fq
	private final ITeslaOrder teslaMakeYYG = new TeslaMakeYYG();
	
	//特殊订单处理    --  fq
	private final ITeslaOrder teslaMakeOrder = new TeslaMakeOrderInfo();

	// 校验地址信息-xie
	private final ITeslaOrder teslaMakeAdress = new TeslaMakeAdress();
	
	// 库存扣减--jiang
	private final ITeslaOrder teslaMakeStock = new TeslaMakeStock();

	// 提货券商品拆单
	private final ITeslaOrder teslaMakeVoucherSplit = new TeslaMakeVoucherSplit();
	
	// 活动扣减(券,微公社,惠豆等钱类相关的扣减)--xie
	// 优惠券扣减放到订单相关信息持久化后操作 -rhb
	private final ITeslaOrder teslaMakeActivity = new TeslaMakeActivity();

	// 格式化订单信息--jiang
	private final ITeslaOrder teslaMakeFormat = new TeslaMakeFormat();
	
	// 储值金、暂存款占用--pang
	private final ITeslaOrder teslaMakeOccupyAmt = new TeslaMakeOccupyAmt();
	
	//订单信息修改(惠家有自营LD商户订单)
	private final ITeslaOrder teslaMakeOrderInfoHandle = new TeslaMakeOrderInfoHandle();
	
	//初始化第三方订单详情信息(暂时仅为多彩订单)
	private final ITeslaOrder teslaMakeThirdOrderDetail = new TeslaMakeThirdOrderDetail();
	
	// 计算积分赋予的金额
	private final ITeslaOrder teslaMakeGiveIntegral = new TeslaMakeGiveIntegral();
	
	// 积分占用、使用
	private final ITeslaOrder teslaMakeCtrlAccmCrdtPpcServer = new TeslaMakeCtrlAccmCrdtPpcServer();
	
	// 储值金占用、使用  20180521 --rhb
	private final ITeslaOrder teslaMakeCtrlPpcServer = new TeslaMakeCtrlPpcServer();
	
	// 暂存款占用、使用  20180521 --rhb
	private final ITeslaOrder teslaMakeCtrlCrdtServer = new TeslaMakeCtrlCrdtServer();
		
	// 创建订单--jiang
	private final ITeslaOrder teslaMakeCreate = new TeslaMakeCreate();

	// 各种通知及其他后续操作--jiang
	private final ITeslaOrder teslaMakeFinish = new TeslaMakeFinish();
	
	// 分销下单逻辑处理
	private final ITeslaOrder teslaMakeAgentOrder = new TeslaMakeAgentOrder();
	
	// 分销下单逻辑处理
	private final ITeslaOrder teslaMakeShareOrder = new TeslaMakeShareOrder();
	
	// 推广赚下单逻辑处理
	private final ITeslaOrder teslaMakeTgzOrder = new TeslaMakeTgzOrder();
	
	// 记录缤纷扫码购订单
	private final ITeslaOrder teslaMakeBinfenSmg = new TeslaMakeBinfenSmg();
	
	//拼好货订单处理 -- fq
	private final ITeslaOrder teslaMakeSpellGoods = new TeslaMakeSpellGoods();
	
	//积分商城订单处理
	private final ITeslaOrder teslaMakePointShop = new TeslaMakePointShop();
	
	//违禁品校验
	private final ITeslaOrder teslaMakeContraband = new TeslaMakeContraband();

	//拼团校验
	private final ITeslaOrder teslaMakeCollageValidate = new TeslaMakeCollageValidate();
	
	//校验地址编码是否合规
	private final ITeslaOrder teslaMakeCheckCodeLevel = new TeslaMakeCheckCodeLevel();
	
	//京东商品校验
	private final ITeslaOrder teslaMakeCheckJdSku = new TeslaMakeCheckJdSku();
	
	//兑换码兑换活动校验
	private final ITeslaOrder teslaMakeRedeemProduct = new TeslaMakeRedeemProduct();
	
	//惠惠农场兑换活动校验
	private final ITeslaOrder teslaMakeFarmProduct = new TeslaMakeFarmProduct();
	
	//特殊活动校验 投票换购
	private final ITeslaOrder teslaMakeSpecialEvent = new TeslaMakeSpecialEvent();
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult result = new TeslaXResult();

		//购物车
		if(teslaOrder.getStatus().getExecStep()==ETeslaExec.shopCart){
			result = orderProcss(teslaOrder, teslaMakeInit,teslaMakeProduct,teslaMakeEvent,teslaMakeEventFO,teslaMakeEventPlus,teslaMakeShopCartShow);
		}
		
		// 订单确认
		if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Confirm) {

			
			result = orderProcss(teslaOrder, teslaMakeSpecialEvent, teslaMakeCollageValidate, teslaMakeInit, teslaMakeLock,teslaMakeProduct,teslaMakeGiftSkuInfo,teslaMakeCheckJdSku,teslaMakeSplit,teslaMakeReckon,teslaMakeHuDongEvent,teslaMakeEvent,teslaMakeEventFO,teslaMakeEventPlus,teslaMakeTransport,teslaMakeAccount,teslaMakeEventOnlinePay,teslaMakeCouponSelect,teslaMakeBalanced,teslaMakeProductAreaLimit,teslaMakeShow,teslaMakeContraband,teslaMakeRedeemProduct,teslaMakeFarmProduct,teslaMakeFormat);

		}
		// 订单创建
		else if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
			
			result = orderProcss(teslaOrder, teslaMakeSpecialEvent, teslaMakeCollageValidate, teslaMakeBlacklisted, teslaMakeInit, teslaMakeLock,teslaMakeProduct,teslaMakeVJPay,teslaMakeGiftSkuInfo,teslaMakeCheckJdSku,teslaMakeSplit,teslaMakeReckon,teslaMakeHuDongEvent,teslaMakeEvent,teslaMakeEventFO,teslaMakeEventPlus,teslaMakeTransport,teslaMakePointShop, teslaMakeAccount,teslaMakeEventOnlinePay,teslaMakeBalanced,teslaMakeProductAreaLimit,teslaMakeShow,teslaMakeYYG,teslaMakeOrder,teslaMakeAdress,teslaMakeValidateStock,teslaMakeStock,teslaMakeVoucherSplit,teslaMakeActivity,teslaMakeContraband,teslaMakeCheckCodeLevel,teslaMakeRedeemProduct,teslaMakeFarmProduct,teslaMakeFormat,teslaMakeOrderInfoHandle,teslaMakeThirdOrderDetail,teslaMakeGiveIntegral,teslaMakeCtrlAccmCrdtPpcServer,teslaMakeCtrlPpcServer,teslaMakeCtrlCrdtServer,teslaMakeTgzOrder,teslaMakeCreate,teslaMakeFinish,teslaMakeAgentOrder,teslaMakeBinfenSmg,teslaMakeSpellGoods,teslaMakeShareOrder);
			
		}
		// PC版订单创建
		else if (teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {

			
			//result = orderProcss(teslaOrder, teslaMakeBlacklisted,teslaMakeInitPC, teslaMakeLock,teslaMakeProduct,teslaMakeSplit,teslaMakeReckon,teslaMakeEvent,teslaMakeEventFO,teslaMakeTransport,teslaMakeAccount,teslaMakeBalanced,teslaMakeGiftSkuInfo,teslaMakeProductAreaLimit,teslaMakeShow,teslaMakeAdress,teslaMakeStock,teslaMakeActivity,teslaMakeFormat,teslaMakeOccupyAmt,teslaMakeCreate,teslaMakeFinish);
			result = orderProcss(teslaOrder, teslaMakeBlacklisted,teslaMakeInitPC, teslaMakeLock,teslaMakeProduct,teslaMakeVJPay,teslaMakeGiftSkuInfo,teslaMakeCheckJdSku,teslaMakeSplit,teslaMakeReckon,teslaMakeEvent,teslaMakeEventFO,teslaMakeEventPlus,teslaMakeTransport,teslaMakeAccount,teslaMakeBalanced,teslaMakeProductAreaLimit,teslaMakeShow,teslaMakeAdress,teslaMakeValidateStock,teslaMakeStock,teslaMakeVoucherSplit,teslaMakeActivity,teslaMakeFormat,teslaMakeOrderInfoHandle,teslaMakeGiveIntegral,teslaMakeCtrlAccmCrdtPpcServer,teslaMakeTgzOrder,teslaMakeCreate,teslaMakeFinish,teslaMakeAgentOrder,teslaMakeShareOrder);

		}
		// 可用优惠券列表
		else if (teslaOrder.getStatus().getExecStep() == ETeslaExec.AvailableCoupon) {
			
			result = orderProcss(teslaOrder, teslaMakeInit, teslaMakeProduct,teslaMakeSplit,teslaMakeHuDongEvent,teslaMakeEvent,teslaMakeEventFO,teslaMakeEventPlus,teslaMakeAccount,teslaMakeEventOnlinePay,teslaMakeCouponSelect);

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
	private TeslaXResult orderProcss(TeslaXOrder teslaOrder,
			ITeslaOrder... iOrders) {

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
