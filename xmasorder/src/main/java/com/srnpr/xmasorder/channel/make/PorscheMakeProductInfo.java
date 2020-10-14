package com.srnpr.xmasorder.channel.make;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.channel.enumer.EPorscheExec;
import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelGoodsInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderDetail;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfoSpread;
import com.srnpr.xmassystem.load.LoadTemplateAreaCode;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfoSpread;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 初始化商品信息
 * @remark 
 * @author 任宏斌
 * @date 2019年12月3日
 */
public class PorscheMakeProductInfo extends PorscheTopOrderMake {

	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		PorscheGtResult result = new PorscheGtResult();

		LoadSkuInfoSpread loadSkuInfoSpread = new LoadSkuInfoSpread();
		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
		List<PorscheModelOrderDetail> orderDetails = porscheGtOrder.getOrderDetails();

		if (orderDetails != null && !orderDetails.isEmpty()) {
			for (int i = 0; i < orderDetails.size(); i++) {
				String skuCode = porscheGtOrder.getOrderDetails().get(i).getSkuCode();
				// 查询商品信息
				PlusModelSkuInfo plusModelSkuInfo = plusSupportProduct.upSkuInfoBySkuCode(skuCode);
				if (StringUtils.isNotBlank(plusModelSkuInfo.getSkuCode())) {
					// 判断商品是否在商品池中
					MDataMap procuctChannel = DbUp.upTable("pc_channel_productinfo").one("sku_code", skuCode,"is_delete","0");
					if (null == procuctChannel) {
						result.setResultCode(0);
						result.setResultMessage("SKU不在商品池中," + skuCode);
						break;
					}
					// 商品价格取供货价
					BigDecimal supplyPriceProportion = new BigDecimal(procuctChannel.get("supply_price_proportion")).divide(new BigDecimal(100), RoundingMode.HALF_UP);
					BigDecimal costPrice = plusModelSkuInfo.getCostPrice();
					// （供货价比例+1）*成本价 向上保留1位小数
					BigDecimal supplyPrice = supplyPriceProportion.add(BigDecimal.ONE).multiply(costPrice).setScale(1, RoundingMode.UP);

					// 校验供货价
					/*if (porscheGtOrder.getOrderDetails().get(i).getCostPrice().compareTo(supplyPrice) != 0) {
						result.setResultCode(0);
						result.setResultMessage("SKU价格有变动," + skuCode);
						break;
					}*/

					// 商品信息
					porscheGtOrder.getOrderDetails().get(i).setProductCode(plusModelSkuInfo.getProductCode());
					porscheGtOrder.getOrderDetails().get(i).setProductPicUrl(plusModelSkuInfo.getProductPicUrl());
					porscheGtOrder.getOrderDetails().get(i).setSkuCode(plusModelSkuInfo.getSkuCode());
					porscheGtOrder.getOrderDetails().get(i).setSkuName(plusModelSkuInfo.getSkuName());
					porscheGtOrder.getOrderDetails().get(i).setSkuPrice(supplyPrice);
					porscheGtOrder.getOrderDetails().get(i).setSellPrice(supplyPrice);
					porscheGtOrder.getOrderDetails().get(i).setShowPrice(supplyPrice);
					porscheGtOrder.getOrderDetails().get(i).setCostPrice(costPrice);
					
					PlusModelProductQuery plusModelProductQuery = new PlusModelProductQuery(plusModelSkuInfo.getProductCode());
					PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(plusModelProductQuery);
					
					porscheGtOrder.getOrderDetails().get(i).setTaxRate(plusModelProductinfo.getTax_rate());
					
					// showGoods信息
					PorscheModelGoodsInfo good = new PorscheModelGoodsInfo();
					good.setProductCode(porscheGtOrder.getOrderDetails().get(i).getProductCode());
					good.setSkuCode(porscheGtOrder.getOrderDetails().get(i).getSkuCode());
					good.setSkuNum(porscheGtOrder.getOrderDetails().get(i).getSkuNum());
					good.setMiniOrder(plusModelSkuInfo.getMinBuy());
					
					if(StringUtils.isNotBlank(plusModelProductinfo.getAreaTemplate())){
						PlusModelTemplateAreaQuery taq = new PlusModelTemplateAreaQuery();
						taq.setCode(plusModelProductinfo.getAreaTemplate());
						good.setAreaCodes(new LoadTemplateAreaCode().upInfoByCode(taq));
					}
					porscheGtOrder.getShowGoods().add(good);
					
					if(porscheGtOrder.getStatus().getExecStep() != EPorscheExec.Feight 
							&& porscheGtOrder.getOrderDetails().get(i).getSkuNum() < porscheGtOrder.getShowGoods().get(i).getMiniOrder()) {
						result.setResultCode(0);
						result.setResultMessage("SKU未满足最小起订数量," + skuCode);
						break;
					}
					porscheGtOrder.getShowGoods().get(i).setSku_keyValue(plusModelSkuInfo.getSkuKeyvalue());
					porscheGtOrder.getShowGoods().get(i).setSkuName(plusModelSkuInfo.getSkuName());
					porscheGtOrder.getShowGoods().get(i).setSkuPrice(supplyPrice);
					porscheGtOrder.getShowGoods().get(i).setSmallSellerCode(plusModelSkuInfo.getSmallSellerCode());
					// 扩展信息
					// 查询商品扩展信息
					PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
					plusModelQuery.setCode(plusModelSkuInfo.getProductCode());
					PlusModelSkuInfoSpread plusModelSkuInfoSpread = loadSkuInfoSpread.upInfoByCode(plusModelQuery);
					if (plusModelSkuInfoSpread != null) {
						porscheGtOrder.getShowGoods().get(i).setPrchType(plusModelSkuInfoSpread.getPrchType());
						porscheGtOrder.getShowGoods().get(i).setDlrId(plusModelSkuInfoSpread.getDlrId());
						porscheGtOrder.getShowGoods().get(i).setSiteNo(plusModelSkuInfoSpread.getSiteNo());
						porscheGtOrder.getShowGoods().get(i).setProductTradeType(plusModelSkuInfoSpread.getProductTradeType());
					}
					if (porscheGtOrder.getStatus().getExecStep() != EPorscheExec.Feight 
							&& porscheGtOrder.getOrderDetails().get(i).getSkuNum() > new PlusSupportStock().upSalesStock(skuCode)) {
						result.setResultCode(0);
						result.setResultMessage("库存不足," + skuCode);
						break;
					}
				} else {
					result.setResultCode(0);
					result.setResultMessage("SKU编号不正确," + skuCode);
					break;
				}
			}
		}
		return result;
	}

}
