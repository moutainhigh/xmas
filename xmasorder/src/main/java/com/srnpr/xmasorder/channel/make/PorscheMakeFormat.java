package com.srnpr.xmasorder.channel.make;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderAddress;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfoUpper;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;

/**
 * 格式化订单信息
 * @remark 
 * @author 任宏斌
 * @date 2019年12月5日
 */
public class PorscheMakeFormat  extends PorscheTopOrderMake {

	private final static char[] seq = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {

		PorscheGtResult gtResult = new PorscheGtResult();

		// 大订单信息
		PorscheModelOrderInfoUpper orderInfoUpper = porscheGtOrder.getOrderInfoUpper();
		orderInfoUpper.setUid(WebHelper.upUuid());
		orderInfoUpper.setBigOrderCode(KvHelper.upCode("OS"));
		orderInfoUpper.setOrderNum(porscheGtOrder.getOrderInfo().size());
		orderInfoUpper.setCreateTime(DateUtil.getSysDateTimeString());
		orderInfoUpper.setUpdateTime(orderInfoUpper.getCreateTime());
		// 地址信息 此处地址信息绑定在大订单上
		PorscheModelOrderAddress orderAddress = porscheGtOrder.getAddress();
		orderAddress.setOrderCode(orderInfoUpper.getBigOrderCode());
		orderAddress.setUid(WebHelper.upUuid());
		
		// //订单详情
		for (PorscheModelOrderDetail orderDetail : porscheGtOrder.getOrderDetails()) {
			orderDetail.setUid(WebHelper.upUuid());
		}

		// 订单信息
		int order_seq=0;
		for (PorscheModelOrderInfo orderInfo : porscheGtOrder.getOrderInfo()) {
			orderInfo.setUid(WebHelper.upUuid());
			orderInfo.setCreateTime(orderInfoUpper.getCreateTime());
			orderInfo.setUpdateTime(orderInfoUpper.getCreateTime());
			orderInfo.setBigOrderCode(orderInfoUpper.getBigOrderCode());
			orderInfo.setOrderSeq(String.valueOf(seq[order_seq++]));
			orderInfo.setOutOrderCode(porscheGtOrder.getOutOrderCode());
			orderInfo.setOrderStatus("4497153900010002");//订单属于已支付订单
			orderInfo.setPayType(orderInfoUpper.getPayType());
		}

		return gtResult;
	}
	
}
