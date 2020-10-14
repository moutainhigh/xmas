package com.srnpr.xmasorder.make;

import java.util.List;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 对订单信息特殊处理
 * 
 * @author fq
 *
 */
public class TeslaMakeOrderInfoHandle extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult result = new TeslaXResult();

		if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Create
				|| teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {

			/*
			 * 判断是否是惠家有自营LD商户订单
			 */
			List<TeslaModelOrderInfo> sorderInfo = teslaOrder.getSorderInfo();
			if (sorderInfo.size() > 0) {

				if ("SF03100632".equals(sorderInfo.get(0).getSmallSellerCode())) {// 是惠家有自营LD商户订单

					// 修改订单(oc_orderinfo)信息
					for (TeslaModelOrderInfo teslaModelOrderInfo : sorderInfo) {
						teslaModelOrderInfo.setAppVersion("SF03100632");// 将appVersion更改为惠家有自营LD商户编号
						teslaModelOrderInfo.setSmallSellerCode("SI2003");// 将商户编号更改为LD商户
					}

					// 查询惠家有自营LD商品编号 与 LD商品编号的对应信息 ,并修改订单详情的商品编号
					for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {
						MDataMap one = DbUp.upTable("pc_hjyLD_rel_LD_productinfo").one("product_code",
								teslaOrder.getOrderDetails().get(j).getProductCode(), "sku_code",
								teslaOrder.getOrderDetails().get(j).getSkuCode());
						
						if(null != one) {
							teslaOrder.getOrderDetails().get(j).setProductCode(one.get("ld_product_code"));
							teslaOrder.getOrderDetails().get(j).setSkuCode(one.get("ld_sku_code"));
						} else {
							result.setResultCode(963905001);
							result.setResultMessage(bInfo(963905001, teslaOrder.getOrderDetails().get(j).getProductCode()));
							break;
						}

					}

				}

			}

		}

		return result;
	}

}
