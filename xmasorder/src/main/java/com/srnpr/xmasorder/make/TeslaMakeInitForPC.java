package com.srnpr.xmasorder.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelOrderAddress;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadOrderAddressPC;
import com.srnpr.xmassystem.modelproduct.PlusModelOrderAddress;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;

/**
 * @descriptions
 * 
 * @date 2016年6月7日下午4:33:17
 * @author Yangcl
 * @version 1.0.1
 */
public class TeslaMakeInitForPC extends TeslaMakeInit {
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		// 合并重复数据
		mergeOrderDetail(teslaOrder);
		
		TeslaXResult result = new TeslaXResult();

		for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
			TeslaModelShowGoods good = new TeslaModelShowGoods();
			good.setProductCode(teslaOrder.getOrderDetails().get(i).getProductCode());
			good.setSkuCode(teslaOrder.getOrderDetails().get(i).getSkuCode());
			good.setSkuActivityCode(teslaOrder.getOrderDetails().get(i).getSkuCode());
			good.setSkuNum(teslaOrder.getOrderDetails().get(i).getSkuNum());
			good.setChoose_flag(teslaOrder.getOrderDetails().get(i).getChoose_flag());
			good.setIsKaolaGood(teslaOrder.getOrderDetails().get(i).getIsKaolaGood());
			teslaOrder.getShowGoods().add(good);
		}

		if (teslaOrder.getStatus().getExecStep() == ETeslaExec.Create || teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {
			// 查询订单地址信息并初始化

			PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();

			plusModelQuery.setCode(teslaOrder.getAddress().getAddressCode());

			PlusModelOrderAddress plusModelAddress = new LoadOrderAddressPC().checkForInner(teslaOrder.getIsPurchase() , plusModelQuery);
			if(teslaOrder.getIsPurchase() == 1){ // 内购情况 nc_staff_address 表无手机和收件人两个字段
				plusModelAddress.setMobilephone(teslaOrder.getAddress().getMobilephone());		// 手机
				plusModelAddress.setReceivePerson(teslaOrder.getAddress().getReceivePerson()); 	// 收件人
			}

			if (plusModelAddress.getAddressCode() != null && !"".equals(plusModelAddress.getAddressCode())) {

				TeslaModelOrderAddress address = new TeslaModelOrderAddress();

				address.setAddress(plusModelAddress.getAddress());

				address.setAddressCode(plusModelAddress.getAddressCode());

				address.setAreaCode(plusModelAddress.getAreaCode());

				address.setEmail(plusModelAddress.getEmail());

				address.setMobilephone(plusModelAddress.getMobilephone());

				address.setPostcode(plusModelAddress.getPostCode());

				address.setAuthIdcardType(plusModelAddress.getIdcardType());

				address.setAuthPhoneNumber(plusModelAddress.getPhoneNumber());

				address.setAuthTrueName(plusModelAddress.getTrueName());

				address.setAuthAddress(plusModelAddress.getCardAddress());

				address.setAuthIdcardNumber(plusModelAddress.getIdcardNumber());

				address.setAuthEmail(plusModelAddress.getCardEmail());

				address.setReceivePerson(plusModelAddress.getReceivePerson());

				address.setInvoiceTitle(teslaOrder.getAddress().getInvoiceTitle());

				address.setInvoiceContent(teslaOrder.getAddress().getInvoiceContent());

				address.setInvoiceType(teslaOrder.getAddress().getInvoiceType());

				if ((address.getInvoiceContent() == null || "".equals(address.getInvoiceContent()))
						&& (address.getInvoiceTitle() == null || "".equals(address.getInvoiceTitle()))
						&& (address.getInvoiceType() == null || "".equals(address.getInvoiceType()))) {
					address.setFlagInvoice(0);
				} else {
					address.setFlagInvoice(1);
				}
				teslaOrder.setAddress(address);
			}

			if (StringUtils.isEmpty(teslaOrder.getAddress().getAddress())
					|| StringUtils.isEmpty(teslaOrder.getAddress().getAreaCode())
					|| StringUtils.isEmpty(teslaOrder.getAddress().getMobilephone())) {// 地址三级编号、手机号、详细地址都不能为空
				result.setResultCode(963902216);
				result.setResultMessage(bInfo(963902216));
				return result;
			}
		}

		return result;
	}
	
	/**
	 * 合并相同SKU的明细数据，重复SKU明细会造成满减金额计算异常
	 * @param teslaOrder
	 */
	private void mergeOrderDetail(TeslaXOrder teslaOrder){
		List<TeslaModelOrderDetail> oldList = teslaOrder.getOrderDetails();
		List<TeslaModelOrderDetail> newList = new ArrayList<TeslaModelOrderDetail>();
		
		Map<String,TeslaModelOrderDetail> detailMap = new HashMap<String, TeslaModelOrderDetail>();
		
		for(TeslaModelOrderDetail detail : oldList){
			if(detailMap.containsKey(detail.getSkuCode())){
				// 合并相同SKU数量
				detailMap.get(detail.getSkuCode()).setSkuNum(detailMap.get(detail.getSkuCode()).getSkuNum()+detail.getSkuNum());
			}else{
				detailMap.put(detail.getSkuCode(), detail);
				newList.add(detail);
			}
		}
		
		teslaOrder.setOrderDetails(newList);
	}
}
