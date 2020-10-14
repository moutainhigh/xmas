package com.srnpr.xmasorder.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
/**
 * 促销计算(获取商品的最新价格)
 * 
 * @author xiegj
 *
 */
public class TeslaMakeEvent extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		
		//兑换码兑换、惠惠农场兑换不参与促销
		if(StringUtils.isNotEmpty(teslaOrder.getActivityCode()) && StringUtils.isNotEmpty(teslaOrder.getRedeemCode())
				|| StringUtils.isNotBlank(teslaOrder.getTreeCode())) {
			return result;
		}
		
		// 互动活动不跟其他促销叠加
		if(teslaOrder.getHuDongEvent() != null) {
			return result;
		}
		
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {//showgoods内的商品价格及活动名称标签
			TeslaModelShowGoods good = teslaOrder.getShowGoods().get(i);  
			if("0".equals(good.getGiftFlag()) || teslaOrder.isPointShop()){continue;};
			//判断是否原价购买
			boolean isOriginal = false, isSupportCollage = false;
			
			if("1".equals(teslaOrder.getIsOriginal())) {
				isOriginal = true;
			}
			if("1".equals(teslaOrder.getCollageFlag())) {
				isSupportCollage = true;
			}
			
			// 分销商品不参与促销活动
			if(good.getFxFlag() == 1) {
				continue;
			}
			
			PlusModelSkuInfo info = null;
			PlusModelSkuQuery pq = new PlusModelSkuQuery();
			pq.setChannelId(teslaOrder.getChannelId());
			if(isOriginal) {
				pq.setIsOriginal("1"); 
			}
			if(!isSupportCollage) {
				pq.setIsSupportCollage("0");
			}
			pq.setIfJJGFlag(good.getIfJJGFlag());
			pq.setMemberCode(teslaOrder.getUorderInfo().getBuyerCode());
			pq.setIsPurchase(teslaOrder.getIsPurchase());
			pq.setOrderSource(teslaOrder.getUorderInfo().getOrderSource());
			pq.setEventCode(teslaOrder.getEventCode());
			
			//根据sku_code 查完一次 又要根据item_code查 因为打折促销没有维护sku_code 此处如果传item_code后续调用loadSkuItem会出问题 所以只能再传sku_code 
			//TODO 为啥查两遍 目的不明确 待优化
			if(good.getEventType().equals("4497472600010006") || good.getEventType().equals("4497472600010018") 
					||"1".equals(good.getIfJJGFlag()) || good.getEventType().equals("4497472600010030")){
				pq.setCode(good.getSkuCode());
			}else{
				pq.setCode(good.getSkuActivityCode());
			}
			
			info = new PlusSupportProduct().upSkuInfo(pq).getSkus().get(0);
			
			if(info.getBuyStatus()==1&&StringUtils.isNotBlank(info.getEventCode())){
				teslaOrder.getShowGoods().get(i).setEventCode(info.getEventCode());
				teslaOrder.getShowGoods().get(i).setActivity_name(info.getSellNote());
				teslaOrder.getShowGoods().get(i).setSkuPrice(info.getSellPrice());
				teslaOrder.getShowGoods().get(i).setIs_activity(true);
				teslaOrder.getShowGoods().get(i).setSkuCode(info.getSkuCode());
				if(good.getSkuNum()>info.getMaxBuy()&&info.getMaxBuy()>0&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
					result.setResultCode(916401132);
					result.setResultMessage(bInfo(963901132, "“" + good.getSkuName()+ "”",info.getMaxBuy()));
					break;
				}else if (good.getSkuNum()>info.getMaxBuy()&&info.getMaxBuy()==0&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart) {
					NoStockOrFailureGoods noStockOrFailureGoods = new NoStockOrFailureGoods();
					noStockOrFailureGoods.setProduct_code(info.getProductCode());
					noStockOrFailureGoods.setSku_code(info.getSkuCode());
					noStockOrFailureGoods.setSku_name(info.getSkuName());
					noStockOrFailureGoods.setSku_pic(info.getSkuPicUrl());
					noStockOrFailureGoods.setSku_num(good.getSkuNum());
					teslaOrder.getNoStockOrFailureGoods().add(noStockOrFailureGoods);
					result.inErrorMessage(916425001);
					break;
				}
				if(result.upFlagTrue()){
					for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {//orderdetail内的商品价格及saveAmt
						TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(j);
						if(detail.getSkuCode().equals(good.getSkuCode())&&detail.getIfJJGFlag().equals(good.getIfJJGFlag())){
							teslaOrder.getOrderDetails().get(j).setSaveAmt(detail.getSkuPrice().subtract(info.getSellPrice()));
							teslaOrder.getOrderDetails().get(j).setSkuPrice(info.getSellPrice());
							teslaOrder.getOrderDetails().get(j).setSkuCode(info.getSkuCode());
							teslaOrder.getOrderDetails().get(j).setShowPrice(info.getSellPrice());
						}
					}
				}
				TeslaModelOrderActivity activity = new TeslaModelOrderActivity();//将参加促销活动的商品天机到订单activiti中
				activity.setOrderCode(teslaOrder.getShowGoods().get(i).getOrderCode());
				activity.setActivityCode(info.getEventCode());
				activity.setActivityName(info.getSellNote());
				activity.setActivityType(info.getEventType());
				activity.setPreferentialMoney(info.getSkuPrice().subtract(info.getSellPrice()));
				activity.setProductCode(good.getProductCode());
				activity.setSkuCode(good.getSkuCode());
				activity.setJjgFlag(good.getIfJJGFlag());
				activity.setOutActiveCode(info.getItemCode());//用此字段存sku的活动商品IC编号
				teslaOrder.getActivityList().add(activity);
			}else if (info.getBuyStatus()==6&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
				result.setResultCode(963901134);
				result.setResultMessage(bInfo(963901134, good.getSkuName()));
			}else if (info.getBuyStatus()==7&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart) {
				result.setResultCode(963901135);
				result.setResultMessage(bInfo(963901135, good.getSkuName(),String.valueOf(info.getLimitBuy())));
			}else if(info.getBuyStatus()==4&&"4497472600010006".equals(info.getEventType())&&(teslaOrder.getStatus().getExecStep()==ETeslaExec.shopCart||teslaOrder.getStatus().getExecStep()==ETeslaExec.Confirm)) {
				teslaOrder.getShowGoods().get(i).setEventCode(info.getEventCode());
				teslaOrder.getShowGoods().get(i).setActivity_name(info.getSellNote());
				teslaOrder.getShowGoods().get(i).setSkuPrice(info.getSellPrice());
				teslaOrder.getShowGoods().get(i).setIs_activity(true);
				teslaOrder.getShowGoods().get(i).setSkuCode(info.getSkuCode());
				if(good.getSkuNum()>info.getMaxBuy()&&info.getMaxBuy()>0&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart){
					result.setResultCode(916401132);
					result.setResultMessage(bInfo(963901132, "“" + good.getSkuName()+ "”",info.getMaxBuy()));
					break;
				}else if (good.getSkuNum()>info.getMaxBuy()&&info.getMaxBuy()==0&&teslaOrder.getStatus().getExecStep()!=ETeslaExec.shopCart) {
					result.setResultCode(916401131);
					result.setResultMessage(bInfo(963901131, "“" + good.getSkuName()+ "”"));
					break;
				}
				if(result.upFlagTrue()){
					for (int j = 0; j < teslaOrder.getOrderDetails().size(); j++) {//orderdetail内的商品价格及saveAmt
						TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(j);
						if(detail.getSkuCode().equals(good.getSkuCode())){
							teslaOrder.getOrderDetails().get(j).setSaveAmt(detail.getSkuPrice().subtract(info.getSellPrice()));
							teslaOrder.getOrderDetails().get(j).setSkuPrice(info.getSellPrice());
							teslaOrder.getOrderDetails().get(j).setSkuCode(info.getSkuCode());
							teslaOrder.getOrderDetails().get(j).setShowPrice(info.getSellPrice());
						}
					}
				}
			}
		}
		for (int j = 0; j < teslaOrder.getActivityList().size(); j++) {
			for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
				TeslaModelOrderDetail detail = teslaOrder.getOrderDetails().get(i);
				if("1".equals(detail.getGiftFlag())&&detail.getSkuCode().equals(teslaOrder.getActivityList().get(j).getProductCode())&&detail.getIfJJGFlag().equals(teslaOrder.getActivityList().get(j).getJjgFlag())){
					teslaOrder.getActivityList().get(j).setOrderCode(teslaOrder.getOrderDetails().get(i).getOrderCode());
					break;
				}
			}
		}
		return result;
	}
}
