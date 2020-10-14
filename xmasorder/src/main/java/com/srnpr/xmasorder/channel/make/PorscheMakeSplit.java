package com.srnpr.xmasorder.channel.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.channel.enumer.EPorscheExec;
import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.model.PorscheModelChannelOrder;
import com.srnpr.xmasorder.channel.model.PorscheModelGoodsInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelOrderInfo;
import com.srnpr.xmasorder.channel.model.PorscheModelSubRole;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapdata.helper.KvHelper;

/**
 * 拆单
 * @remark 
 * @author 任宏斌
 * @date 2019年12月3日
 */
public class PorscheMakeSplit extends PorscheTopOrderMake {

	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {
		PorscheGtResult result = new PorscheGtResult();
		Map<String, List<String>> map = new HashMap<String,List<String>>();
		for (int i = 0; i < porscheGtOrder.getShowGoods().size(); i++) {//根据规则就行拆单
			PorscheModelGoodsInfo good = porscheGtOrder.getShowGoods().get(i);
			String role = good.getSmallSellerCode()+good.getValidateFlag()+good.getPrchType()+good.getDlrId()+good.getSiteNo()+good.getLowGood()+good.getProductTradeType();
			
			if(map.containsKey(role)){
				map.get(role).add(good.getSkuCode());
			}else {
				List<String> list = new ArrayList<String>();
				list.add(good.getSkuCode());
				map.put(role, list);
			}
		}
		
		//创建订单时 不允许出现拆单情况
		if(porscheGtOrder.getStatus().getExecStep() == EPorscheExec.Create && map.size() > 1) {
			result.setResultCode(0);
			result.setResultMessage("存在拆单情况");
			return result;
		}
		
		//对商品信息中的订单号进行赋值
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			PorscheModelSubRole subRole = new PorscheModelSubRole();
			String key = iterator.next().toString();
			subRole.setOrderCode(KvHelper.upCode("DD"));
			subRole.setSubRole(key);
			subRole.setSkus(map.get(key));
			porscheGtOrder.getRoles().add(subRole);
			String productName = "";
			String small_seller_code="";
			for (int i = 0; i < porscheGtOrder.getOrderDetails().size(); i++) {
				if(map.get(key).contains(porscheGtOrder.getOrderDetails().get(i).getSkuCode())
						||map.get(key).contains(porscheGtOrder.getOrderDetails().get(i).getProductCode())){
					porscheGtOrder.getOrderDetails().get(i).setOrderCode(subRole.getOrderCode());
					productName=productName+"|"+porscheGtOrder.getOrderDetails().get(i).getSkuName();
				}
			}
			PorscheModelOrderInfo modelOrderInfo = new PorscheModelOrderInfo();//初始化小订单数据
			modelOrderInfo.setOrderType(porscheGtOrder.getOrderInfoUpper().getOrderType());
			modelOrderInfo.setOrderSource(porscheGtOrder.getOrderInfoUpper().getOrderSource());
			for (int i = 0; i < porscheGtOrder.getShowGoods().size(); i++) {
				if(map.get(key).contains(porscheGtOrder.getShowGoods().get(i).getSkuCode())){
					porscheGtOrder.getShowGoods().get(i).setOrderCode(subRole.getOrderCode());
					small_seller_code=porscheGtOrder.getShowGoods().get(i).getSmallSellerCode();
				}
			}
			modelOrderInfo.setOrderCode(subRole.getOrderCode());
			modelOrderInfo.setBuyerCode(porscheGtOrder.getOrderInfoUpper().getBuyerCode());
			modelOrderInfo.setCreateTime(DateHelper.upNow());
			modelOrderInfo.setOrderChannel(porscheGtOrder.getChannelId());
			if(StringUtils.isNotBlank(porscheGtOrder.getOrderInfoUpper().getPayType())){
				modelOrderInfo.setPayType("449716200002".equals(porscheGtOrder.getOrderInfoUpper().getPayType())?"449716200002":"449716200001");
			}
			
			if(StringUtils.endsWith(key, "449747110002")){
				modelOrderInfo.setLowOrder("449747110002");
			}else{
				modelOrderInfo.setLowOrder("449747110001");
			}
			
			modelOrderInfo.setProductName(productName);
			modelOrderInfo.setSellerCode(porscheGtOrder.getOrderInfoUpper().getSellerCode());
			modelOrderInfo.setSmallSellerCode(small_seller_code);
			modelOrderInfo.setOutOrderCode(porscheGtOrder.getOutOrderCode());
			
			porscheGtOrder.getOrderInfo().add(modelOrderInfo);
			
			if(porscheGtOrder.getStatus().getExecStep() == EPorscheExec.Create) {
				PorscheModelChannelOrder channelOrder = new PorscheModelChannelOrder();
				channelOrder.setChannelSellerCode(porscheGtOrder.getChannelSellerCode());
				channelOrder.setOrderCode(modelOrderInfo.getOrderCode());
				channelOrder.setOutOrderCode(porscheGtOrder.getOutOrderCode());
				porscheGtOrder.getChannelOrder().add(channelOrder);
			}
		}
		
		return result;
	}
}
