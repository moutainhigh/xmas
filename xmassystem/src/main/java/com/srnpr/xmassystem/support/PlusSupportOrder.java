package com.srnpr.xmassystem.support;

import com.srnpr.xmassystem.load.LoadOrder;
import com.srnpr.xmassystem.modelorder.OrderDetail;
import com.srnpr.xmassystem.modelorder.PlusModelOrder;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.plusquery.PlusQueryOrder;
import com.srnpr.zapweb.webmodel.MWebResult;

public class PlusSupportOrder {

	/**
	 * 创建订单的逻辑
	 * 
	 * @param plusModelOrder
	 * @return
	 */
	public MWebResult createOrder(PlusModelOrder plusModelOrder) {

		MWebResult mWebResult = new MWebResult();

		if (mWebResult.upFlagTrue()) {
			refreshOrderProduct(plusModelOrder);
		}

		if (mWebResult.upFlagTrue()) {
			refreshOrderEvent(plusModelOrder);
		}

		if (mWebResult.upFlagTrue()) {
			refreshOrderInfo(plusModelOrder);
		}

		return mWebResult;
	}

	/**
	 * 刷新订单商品 该逻辑通常用于在购物车/订单提交前的操作
	 * 
	 * @param plusModelOrder
	 * @return
	 */
	public MWebResult refreshOrderProduct(PlusModelOrder plusModelOrder) {

		MWebResult mWebResult = new MWebResult();

		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();

		// 循环并初始化明细
		for (OrderDetail oDetail : plusModelOrder.getDetails()) {
			if (mWebResult.upFlagTrue()) {

				PlusModelSkuQuery pSkuQuery = new PlusModelSkuQuery();
				pSkuQuery.setCode(oDetail.getSkuCode());

				pSkuQuery
						.setMemberCode(plusModelOrder.getInfo().getBuyerCode());
				PlusModelSkuInfo pSkuInfo = plusSupportProduct
						.upSkuInfo(pSkuQuery).getSkus().get(0);

				// 判断购买状态
				if (pSkuInfo.getBuyStatus() != 1) {
					mWebResult.inErrorMessage(964305151,
							pSkuInfo.getProductCode());
				}
				// 判断是否超过最大购买数量
				if (pSkuInfo.getMaxBuy() < oDetail.getSkuNum()) {
					mWebResult.inErrorMessage(964305152,
							pSkuInfo.getProductCode());
				}

				oDetail.setSkuCode(pSkuInfo.getSkuCode());
				oDetail.setSkuPrice(pSkuInfo.getSellPrice());

			}
		}

		return mWebResult;

	}

	/**
	 * 刷新订单活动
	 * 
	 * @param plusModelOrder
	 * @return
	 */
	public MWebResult refreshOrderEvent(PlusModelOrder plusModelOrder) {
		MWebResult mWebResult = new MWebResult();

		return mWebResult;
	}

	/**
	 * 刷新订单信息
	 * 
	 * @param plusModelOrder
	 * @return
	 */
	public MWebResult refreshOrderInfo(PlusModelOrder plusModelOrder) {
		MWebResult mWebResult = new MWebResult();

		return mWebResult;
	}

	/**
	 * 获取订单信息
	 * 
	 * @param sOrderCode
	 * @return
	 */
	public PlusModelOrder upOrder(String sOrderCode) {
		PlusQueryOrder pQueryOrder = new PlusQueryOrder();
		pQueryOrder.setCode(sOrderCode);
		return new LoadOrder().upInfoByCode(pQueryOrder);
	}

}
