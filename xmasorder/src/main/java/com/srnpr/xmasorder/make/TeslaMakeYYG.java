package com.srnpr.xmasorder.make;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.MD5Code;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 判断是否是一元购订单，（是一元购订单，则将订单金额置0）
 * @author fq
 *
 */
public class TeslaMakeYYG  extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		
		TeslaXResult result = new TeslaXResult();
		/*
		 * 判断是否是一元购订单    fq++
		 */
		String orderType = teslaOrder.getUorderInfo().getOrderType();
		if(StringUtils.isNotBlank(orderType) && "449715200014".equals(orderType) && teslaOrder.getStatus().getExecStep() == ETeslaExec.Create) {
			
			String yygMac = teslaOrder.getYyg().getYygMac();
			String yygOrderNo = teslaOrder.getYyg().getYygOrderNo();
			String yygPayMoney = teslaOrder.getYyg().getYygPayMoney();
			
			String key = bConfig("familyhas.sync_yyg_key");
			/*验证加密字符串*/
			try {
				String encode = MD5Code.encode(yygOrderNo+yygPayMoney+key);
				if(StringUtils.isNotBlank(yygMac) && !encode.equals(yygMac)) {
					result.setResultCode(3);
					result.setResultMessage("一元购加密认证失败");
					return result;
				} else {
					//一层验证通过
					MDataMap one = DbUp.upTable("lc_sync_yyg_orderInfo").one("order_no",yygOrderNo);
					if(null != one) {
						String period_num = one.get("period_num");
						String product_id = one.get("product_id");
						
						MDataMap paramMap = new MDataMap();
						paramMap.put("period_num", period_num);
						paramMap.put("product_id", product_id);
						paramMap.put("order_no", yygOrderNo);
						
						String sSql = " SELECT SUM(pay_money) as money FROM logcenter.lc_sync_yyg_orderInfo WHERE period_num = :period_num AND product_id = :product_id And sync_status = '1000'  GROUP BY period_key " ;
						Map<String, Object> dataSqlOne = DbUp.upTable("lc_sync_yyg_orderInfo").dataSqlOne(sSql, paramMap);
						if(null != dataSqlOne && dataSqlOne.size() > 0) {
							BigDecimal payMoney = BigDecimal.valueOf(Double.valueOf(StringUtils.isNotBlank(String.valueOf(dataSqlOne.get("money")))?String.valueOf(dataSqlOne.get("money")):"0"));
							if(payMoney.compareTo(BigDecimal.valueOf(Double.valueOf(yygPayMoney))) == 0 ){//一元购支付的金额等于同步记录的总金额，则成功
								/*
								 * 将订单价格，和sku价格置为0
								 */
								for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
									teslaOrder.getOrderDetails().get(i).setSkuPrice(new BigDecimal(0.00));
								}
								for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
									teslaOrder.getShowGoods().get(i).setSkuPrice(new BigDecimal(0.00));
								}
								teslaOrder.getUorderInfo().setDueMoney(new BigDecimal(0.00));//应付款
								teslaOrder.getUorderInfo().setOrderMoney(new BigDecimal(0.00));
								teslaOrder.getUorderInfo().setAllMoney(new BigDecimal(0.00));//
								/*
								 * 将订单状态置为下单成功未发货(由于一元购支付金额已经在微信商城支付完成，所以要此下0元单，且是支付成功的状态)
								 */
								for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
									teslaOrder.getSorderInfo().get(i).setOrderStatus("4497153900010002");
									teslaOrder.getSorderInfo().get(i).setDueMoney(new BigDecimal(0.00));
									teslaOrder.getSorderInfo().get(i).setOrderMoney(new BigDecimal(0.00));
								}
								
								/*
								 * 记录此次下单
								 * 
								 */
								paramMap.put("order_code", teslaOrder.getSorderInfo().get(0).getOrderCode());//一元购下单只有一个商品
								
								String updateSql1 = " UPDATE logcenter.lc_sync_yyg_orderInfo SET sync_status = '1001' WHERE period_num = :period_num AND product_id = :product_id ";
								String updateSql2 = " UPDATE logcenter.lc_sync_yyg_orderInfo SET order_code = :order_code  WHERE order_no = :order_no ";
								DbUp.upTable("lc_sync_yyg_orderInfo").dataExec(updateSql1, paramMap);
								DbUp.upTable("lc_sync_yyg_orderInfo").dataExec(updateSql2, paramMap);
								
								
							} else {
								result.setResultCode(0);
								result.setResultMessage("同步记录验证失败");
								return result;
							}
						} else {
							result.setResultCode(0);
							result.setResultMessage("没有发现同步记录!!");
							return result;
						}
					} else {
						result.setResultCode(0);
						result.setResultMessage("没有发现同步记录!");
						return result;
					}
				}
			} catch (UnsupportedEncodingException e) {
				result.setResultCode(0);
				result.setResultMessage("一元购加密认证失败");
				return result;
				//e.printStackTrace();
			}
			
		}
		return result;
	}

}
