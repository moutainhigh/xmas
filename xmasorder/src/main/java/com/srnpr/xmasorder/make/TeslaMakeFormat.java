package com.srnpr.xmasorder.make;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderAddress;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderExtras;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.support.PlusSupportPay;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 格式化订单信息
 * @author jlin
 *
 */
public class TeslaMakeFormat  extends TeslaTopOrderMake {

	private final static char[] seq = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();

		// 大订单信息
		TeslaModelOrderInfoUpper orderInfoUpper = teslaOrder.getUorderInfo();
		orderInfoUpper.setUid(WebHelper.upUuid());
		orderInfoUpper.setBigOrderCode(KvHelper.upCode("OS"));
		orderInfoUpper.setPayedMoney(BigDecimal.ZERO);
		orderInfoUpper.setOrderNum(teslaOrder.getSorderInfo().size());
		orderInfoUpper.setCreateTime(DateUtil.getSysDateTimeString());
		orderInfoUpper.setUpdateTime(orderInfoUpper.getCreateTime());
		// 缓存记录支付方式
		new PlusSupportPay().fixPayFrom(orderInfoUpper.getBigOrderCode(), orderInfoUpper.getPayType());
		// 地址信息 此处地址信息绑定在大订单上
		TeslaModelOrderAddress orderAddress = teslaOrder.getAddress();
		orderAddress.setOrderCode(orderInfoUpper.getBigOrderCode());
		orderAddress.setUid(WebHelper.upUuid());
		
		// 活动信息
		for (TeslaModelOrderActivity activity : teslaOrder.getActivityList()) {
			activity.setUid(WebHelper.upUuid());
		}

		// 支付信息
		for (TeslaModelOrderPay orderPay : teslaOrder.getOcOrderPayList()) {
			orderPay.setUid(WebHelper.upUuid());
			orderPay.setCreateTime(orderInfoUpper.getCreateTime());
		}

		// //订单详情
		for (TeslaModelOrderDetail orderDetail : teslaOrder.getOrderDetails()) {
			orderDetail.setUid(WebHelper.upUuid());
		}

		// 订单信息
		int order_seq=0;
		for (TeslaModelOrderInfo orderInfo : teslaOrder.getSorderInfo()) {
			orderInfo.setUid(WebHelper.upUuid());
			orderInfo.setCreateTime(orderInfoUpper.getCreateTime());
			orderInfo.setUpdateTime(orderInfoUpper.getCreateTime());
			orderInfo.setBigOrderCode(orderInfoUpper.getBigOrderCode());
			orderInfo.setOrderSeq(String.valueOf(seq[order_seq++]));
			
			//加入外部订单号
			if(teslaOrder.getStatus().getExecStep()==ETeslaExec.iqiyi){
				orderInfo.setOutOrderCode(teslaOrder.getOrderOther().getOut_order_code());
				orderInfo.setOrderStatus("4497153900010002");//订单属于已支付订单
				orderInfo.setPayType(orderInfoUpper.getPayType());
			}
			
			// 计算一下大订单相关信息
			orderInfoUpper.setOrderMoney(orderInfoUpper.getOrderMoney().add(orderInfo.getDueMoney()));// 汇总订单的金额
			orderInfoUpper.setAllMoney(orderInfoUpper.getAllMoney().add(orderInfo.getProductMoney()));
		}
		orderInfoUpper.setDueMoney(orderInfoUpper.getOrderMoney());

		// 订单附加信息
		TeslaModelOrderExtras teslaModelOrderExtras = teslaOrder.getExtras();
		teslaModelOrderExtras.setOrderCode(orderInfoUpper.getBigOrderCode());
		teslaModelOrderExtras.setSellerCode(orderInfoUpper.getSellerCode());
		teslaModelOrderExtras.setCreateTime(orderInfoUpper.getCreateTime());
		teslaModelOrderExtras.setCreateUser(orderInfoUpper.getBuyerCode());
		
		//处理一下大订单
		if(teslaOrder.getStatus().getExecStep()==ETeslaExec.iqiyi){
			orderInfoUpper.setPayedMoney(orderInfoUpper.getDueMoney());
			orderInfoUpper.setDueMoney(BigDecimal.ZERO);
		}
		
		return xResult;
	}
	
}
