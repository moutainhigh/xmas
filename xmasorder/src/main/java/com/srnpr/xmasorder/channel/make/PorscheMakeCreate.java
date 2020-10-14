package com.srnpr.xmasorder.channel.make;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelChannelOrder;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderAddress;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfoUpper;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.websupport.DataMapSupport;

/**
 * 创建订单
 * @remark 
 * @author 任宏斌
 * @date 2019年12月5日
 */
public class PorscheMakeCreate  extends PorscheTopOrderMake {

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		
		PorscheGtResult gtResult = new PorscheGtResult();
		
		DataMapSupport dataMapSupport = new DataMapSupport();
		
		try {
			//持久化  oc_orderinfo_upper
			PorscheModelOrderInfoUpper orderInfoUpper = porscheGtOrder.getOrderInfoUpper();
			dataMapSupport.saveToDb("oc_orderinfo_upper", orderInfoUpper,orderInfoUpper.getBigOrderCode());
			
			//持久化  oc_orderinfo  oc_orderadress
			for (PorscheModelOrderInfo orderInfo : porscheGtOrder.getOrderInfo()) {
				
				dataMapSupport.saveToDb("oc_orderinfo", orderInfo,orderInfo.getOrderCode());
				
				//持久化地址信息
				PorscheModelOrderAddress orderAddress = porscheGtOrder.getAddress();
				orderAddress.setUid(WebHelper.upUuid());
				orderAddress.setOrderCode(orderInfo.getOrderCode());
				dataMapSupport.saveToDb("oc_orderadress", orderAddress,orderAddress.getOrderCode());
				
				//添加日志信息
				DbUp.upTable("lc_orderstatus").dataInsert(
						new MDataMap("code",orderInfo.getOrderCode(),
							"info","订单创建",
							"create_time",orderInfo.getCreateTime(),
							"create_user",orderInfo.getBuyerCode(),
							"now_status",orderInfo.getOrderStatus()));
			}
			
			//持久化 oc_order_channel
			for (PorscheModelChannelOrder channelOrder : porscheGtOrder.getChannelOrder()) {
				dataMapSupport.saveToDb("oc_order_channel", channelOrder,channelOrder.getChannelSellerCode());
			}
			
			//持久化  oc_orderdetail
			for (PorscheModelOrderDetail orderDetail : porscheGtOrder.getOrderDetails()) {
				dataMapSupport.saveToDb("oc_orderdetail", orderDetail,orderDetail.getOrderCode());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			gtResult.setResultCode(0);
			gtResult.setResultMessage("订单创建失败，" + porscheGtOrder.getOutOrderCode());
		}
		
		return gtResult;
	}

}
