package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusServiceProduct;
import com.srnpr.xmassystem.helper.PlusHelperEvent;
import com.srnpr.xmassystem.load.LoadSubItem;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventItemProduct;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 扫码购
 * 
 * @author srnpr
 *
 */
public class PlusServiceScanner extends PlusServiceTop implements
		IPlusServiceProduct {

	public void refreshSkuInfo(PlusModelSkuInfo plusSku,
			PlusModelSkuQuery plusQuery, PlusModelEventInfo plusEvent) {
		
		PlusSupportProduct psp = new PlusSupportProduct();
		String sIcCode = plusSku.getItemCode();
		PlusSupportEvent plusSupportEvent = new PlusSupportEvent();

		PlusModelEventItemProduct plusItemProduct = null;

		String sSubIcCode = XmasKv.upFactory(EKvSchema.SubIcCode).get(
				PlusHelperEvent.upSubEvent(sIcCode));

		if (StringUtils.isNotBlank(sSubIcCode)) {

			for (String sKey : StringUtils.split(sSubIcCode, ",")) {

				PlusModelEventItemProduct pIp = plusSupportEvent
						.upItemProductByIcCode(sKey);

				PlusModelEventInfo pEventInfo = plusSupportEvent
						.upEventInfoByCode(pIp.getEventCode());

				if (plusSupportEvent.checkEventEnable(pEventInfo)) {

					plusItemProduct = plusSupportEvent
							.upItemProductByIcCode(sKey);

					// 如果当前有效的活动编号与传入的内容绑定的活动编号不一直时 强制更新所有信息
					if (!plusEvent.getEventCode().equals(
							pEventInfo.getEventCode())) {

						XmasKv.upFactory(EKvSchema.SubEventCode).set(
								PlusHelperEvent.upSubEvent(sIcCode),
								pEventInfo.getEventCode());

						new LoadSubItem().deleteInfoByCode(sIcCode);

					}

					// 将活动的对象强制赋值
					plusEvent = pEventInfo;

				}

			}

		}

		if (plusItemProduct != null) {
			// 商品相关的通用检查和设置
			checkForProduct(plusSku, plusQuery, plusEvent, plusItemProduct);

			// ## 开始进入设置价格部分####################
			// 设置价格部分
			if (StringUtils.isNotBlank(sSubIcCode)) {

				String sProductCode = plusSku.getProductCode();
				String sKvCode = XmasKv.upFactory(EKvSchema.ScannerAllow).get(
						sProductCode);
				// 标记是否允许扫码购
				boolean bFlagAllow = StringUtils.isNotBlank(sKvCode)
						&& sKvCode.equals(sProductCode);
				if (!bFlagAllow) {
					if (DbUp.upTable("sc_erwei_code").count("product_code",
							sProductCode) > 0) {
						XmasKv.upFactory(EKvSchema.ScannerAllow).set(
								sProductCode, sProductCode);
						bFlagAllow = true;
					}
				}

				
				if (bFlagAllow) {
					
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(plusSku.getSkuCode());
					PlusModelSkuResult skuResult = psp.upSkuInfo(skuQuery);
					BigDecimal difference = BigDecimal.ZERO;
					
					boolean is_true=false;
					List<PlusModelSkuInfo> listSkuInfo = skuResult.getSkus();
					if(listSkuInfo.size()>0){
						
						for(int j=0;j<listSkuInfo.size();j++){
							PlusModelSkuInfo skuInfo=listSkuInfo.get(j);
							
							if(!plusSku.getSkuCode().equals(skuInfo.getSkuCode())){
								continue;
							}
							if(StringUtils.isNotBlank(skuInfo.getEventType()) && skuInfo.getEventType().equals("4497472600010014")){
								difference = skuInfo.getSellPrice();
							}else if(StringUtils.isNotBlank(skuInfo.getEventType()) && skuInfo.getEventType().equals("4497472600010017")){
								is_true=true;
								difference = skuInfo.getSellPrice();
							}
							
						}
					}
					
					if(difference.compareTo(BigDecimal.ZERO)>0){
						plusSku.setSellPrice(difference);
					}
					
					if(is_true){
						
					}else{
						BigDecimal bSubtract = plusSku.getSellPrice().subtract(
								plusItemProduct.getPriceEvent());
						
						if (bSubtract.compareTo(BigDecimal.ZERO) > 0) {
							plusSku.setSellPrice(bSubtract);
						}
					}
				}
				
			}
		} else {
			plusSku.setBuyStatus(3);
			plusSku.setButtonText(bInfo(964305104));
		}

	}
	

}
