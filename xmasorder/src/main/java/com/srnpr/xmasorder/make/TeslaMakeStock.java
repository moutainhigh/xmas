package com.srnpr.xmasorder.make;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 库存扣减
 * @author jlin
 *
 */
public class TeslaMakeStock  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();

		PlusSupportStock plusSupportStock = new PlusSupportStock();

		//此处加入分销商库存逻辑
		if(teslaOrder.getStatus().getExecStep() == ETeslaExec.Distributor||teslaOrder.getStatus().getExecStep() == ETeslaExec.iqiyi){
			//分销商 下单库存允许超扣
			for (TeslaModelOrderDetail orderdetail : teslaOrder.getOrderDetails()) {
				orderdetail.setStoreCode(plusSupportStock.subtractSkuStock(orderdetail.getOrderCode(), orderdetail.getSkuCode(), orderdetail.getSkuNum()));
			}
		}else if(teslaOrder.getStatus().getExecStep() == ETeslaExec.Create||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate){
			for (TeslaModelShowGoods orderdetail : teslaOrder.getShowGoods()) {
	
				// 赠品不扣库存
				if ("0".equals(orderdetail.getGiftFlag())) {
					continue;
				}
				String orderCode = orderdetail.getOrderCode();// 先减促销库存
				if (StringUtils.isNotBlank(orderdetail.getSkuActivityCode())
						&& PlusHelperEvent.checkEventItem(orderdetail.getSkuActivityCode())) {
						if (new PlusSupportEvent().subtractSkuStock(orderdetail.getSkuActivityCode(),Long.valueOf(orderdetail.getSkuNum())) < 0) {
							
							if(orderdetail.getEventType().equals("4497472600010006") || orderdetail.getEventType().equals("4497472600010030")){
								//内购、打折促销扣减活动库存失败 流程继续 因为本就没有促销库存
						   }else{
							    xResult.setResultCode(963903001);
								xResult.setResultMessage(bInfo(963903001, orderdetail.getSkuName()));
								break;
						   }
					    }
						
				}
				if(xResult.upFlagTrue()) {//针对积分商城，扣减积分活动库存
					if(teslaOrder.isPointShop()) {
						for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
							if(detail.getSkuCode().equals(orderdetail.getSkuCode())) {
								String integralDetailId = detail.getIntegralDetailId();
								if(!"".equals(integralDetailId)) {
									int count = DbUp.upTable("fh_apphome_channel_details").dataCount("uid = '" + integralDetailId + "' and allow_count >= '" + detail.getSkuNum() + "'", new MDataMap());
									if(count == 1) {
										DbUp.upTable("fh_apphome_channel_details").upTemplate().update("update fh_apphome_channel_details set allow_count = allow_count - " + detail.getSkuNum() + " where uid = '"
												+ integralDetailId + "'", new HashMap<String, Object>());
										
										//积分商城订单绑定订单和活动关系
										String activityType = "";
										Map<String, Object> map = DbUp.upTable("fh_apphome_channel_details").dataSqlOne("select c.channel_type from fh_apphome_channel c, fh_apphome_channel_details d "
												+ "where c.uid = d.channel_uid and d.uid = '" + integralDetailId + "'", new MDataMap());
										if("449748130001".equals(MapUtils.getString(map, "channel_type", ""))) {//加价购
											activityType = "4497472600010022";
										}else {//449748130002 积分兑换
											activityType = "4497472600010023";
										}
										MDataMap mDataMap = new MDataMap();
										mDataMap.put("order_code", detail.getOrderCode());
										mDataMap.put("product_code", detail.getProductCode());
										mDataMap.put("sku_code", detail.getSkuCode());
										mDataMap.put("activity_code", integralDetailId);
										mDataMap.put("activity_type", activityType);
										DbUp.upTable("oc_order_activity").dataInsert(mDataMap);
									}else {
										xResult.setResultCode(963903001);
										xResult.setResultMessage(bInfo(963903001, orderdetail.getSkuName()));
										break;
									}
								}
							}
						}
					}
				}
				if (xResult.upFlagTrue()) {// 再减实际商品库存
					String skuCode = orderdetail.getSkuCode();
					int skuNum = orderdetail.getSkuNum();
	
					String stockInfo = plusSupportStock.subtractSkuStock(orderCode, skuCode, skuNum);
					if (StringUtils.isBlank(stockInfo)) {
						// 如果库存扣除失败
						xResult.inErrorMessage(963903001, orderdetail.getSkuName());
						break;
					} else {
						// 库存扣除成功，把库信息写入订单对象中
						orderdetail.setStoreCode(stockInfo);
					}
				}
				if(xResult.upFlagTrue()){
					for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
						if("1".equals(orderdetail.getGiftFlag())&&teslaOrder.getOrderDetails().get(i).getSkuCode().equals(orderdetail.getSkuCode())){
							teslaOrder.getOrderDetails().get(i).setStoreCode(orderdetail.getStoreCode());
						}
					}
				}
			}
		}
		return xResult;
	}

}
