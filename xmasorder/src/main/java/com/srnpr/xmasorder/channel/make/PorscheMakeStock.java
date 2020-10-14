package com.srnpr.xmasorder.channel.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelGoodsInfo;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmassystem.support.PlusSupportStock;

/**
 * 库存扣减
 * @remark 
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeStock  extends PorscheTopOrderMake {

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {

		PorscheGtResult gtResult = new PorscheGtResult();

		PlusSupportStock plusSupportStock = new PlusSupportStock();

		for (PorscheModelGoodsInfo goodInfo : porscheGtOrder.getShowGoods()) {

			// 赠品不扣库存
			if ("0".equals(goodInfo.getGiftFlag())) {
				continue;
			}
			
			String orderCode = goodInfo.getOrderCode();
			String skuCode = goodInfo.getSkuCode();
			int skuNum = goodInfo.getSkuNum();

			String stockInfo = plusSupportStock.subtractSkuStock(orderCode, skuCode, skuNum);
			if (StringUtils.isBlank(stockInfo)) {
				// 如果库存扣除失败
				gtResult.setResultCode(0);
				gtResult.setResultMessage("库存扣减失败");
				break;
			} else {
				// 库存扣除成功，把库信息写入订单对象中
				goodInfo.setStoreCode(stockInfo);
			}
			
			if(gtResult.upFlagTrue()){
				for (int i = 0; i < porscheGtOrder.getOrderDetails().size(); i++) {
					if("1".equals(goodInfo.getGiftFlag())&&porscheGtOrder.getOrderDetails().get(i).getSkuCode().equals(goodInfo.getSkuCode())){
						porscheGtOrder.getOrderDetails().get(i).setStoreCode(goodInfo.getStoreCode());
					}
				}
			}
		}
		
		return gtResult;
	}

}
