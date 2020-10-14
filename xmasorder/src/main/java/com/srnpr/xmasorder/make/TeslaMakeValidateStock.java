package com.srnpr.xmasorder.make;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.invoke.ref.ValidateStockRef;
import com.srnpr.xmassystem.invoke.ref.model.ValidateStockInput;
import com.srnpr.xmassystem.invoke.ref.model.ValidateStockResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.util.ProCityLoader;
import com.srnpr.zapcom.basehelper.BeansHelper;

/**
 * 调用实时接口验证LD商品库存
 */
public class TeslaMakeValidateStock extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
	   TeslaXResult result = new TeslaXResult();
	   
	   LoadProductInfo loadProductInfo = new LoadProductInfo();
	   
	   ValidateStockInput input = new ValidateStockInput();
	   ValidateStockInput.GoodInfo gi;
	   PlusModelProductInfo productInfo;
	   
	   // 循环商品列表，取出LD商品的具体颜色和款式属性值
	   for(TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
		   if("1".equals(detail.getGiftFlag()) && "1".equals(detail.getChoose_flag()) && "SI2003".equals(detail.getSmallSellerCode())) {
			   gi = new ValidateStockInput.GoodInfo();
			   gi.setGood_cnt(detail.getSkuNum()+"");
			   
			   productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(detail.getProductCode()));
			   List<PlusModelProductSkuInfo> skuList =  productInfo.getSkuList();
			   for(PlusModelProductSkuInfo sku : skuList) {
				   if(sku.getSkuCode().equals(detail.getSkuCode())) {
					   String[] colorStyles = sku.getSkuKey().split("&");
					   String colorId = null;
					   String styleId = null;
					   if(colorStyles.length == 2){
						   colorId = colorStyles[0].replace("color_id=", "");
						   styleId = colorStyles[1].replace("style_id=", "");
						   
						   gi.setColor_id(StringUtils.trimToEmpty(colorId));
						   gi.setStyle_id(StringUtils.trimToEmpty(styleId));
						   gi.setGood_id(productInfo.getProductCode());
						   gi.setGood_name(sku.getSkuName());
					   }
					   break;
				   }
			   }
			   
			   if(StringUtils.isNotBlank(gi.getStyle_id()) && StringUtils.isNotBlank(gi.getStyle_id())) {
				   input.getGood_info().add(gi);
			   }
		   }
	   }
	   
	   // 没有符合条件的商品直接返回
	   if(input.getGood_info().isEmpty()) {
		   return result;
	   }
	 
	   input.setZip_no(teslaOrder.getAddress().getPostcode());
	   input.setSrgn_cd(teslaOrder.getAddress().getAreaCode());
	   input.setSend_addr(teslaOrder.getAddress().getAddress());
	   input.setPay_type(teslaOrder.getUorderInfo().getPayType().equals("449716200002") ? "true" : "false");
	   
	   input.setSaddr(ProCityLoader.getName(input.getSrgn_cd()));
	   input.setMaddr(ProCityLoader.getName(StringUtils.left(input.getSrgn_cd(), 4)+"00")); 
	   input.setLaddr(ProCityLoader.getName(StringUtils.left(input.getSrgn_cd(), 2)+"0000"));
	   
	   ValidateStockRef ref = BeansHelper.upBean(ValidateStockRef.NAME);
	   ValidateStockResult res = ref.stockCheck(input);
	   
	   // 库存不足则阻止下单
	   if(res.isSuccess() && !Boolean.valueOf(res.getIs_ok())){
		   if(input.getGood_info().size() == 1) {
			   if(res.getMax_cnt() > 0){
				   // 单商品时提示可最大购买数量
				   result.inErrorMessage(963903001, "");
				   result.setResultMessage("商品["+input.getGood_info().get(0).getGood_name()+"]库存不足，最多可购买数量："+res.getMax_cnt());
			   }else{
				   result.inErrorMessage(963903001, "");
				   result.setResultMessage("商品["+input.getGood_info().get(0).getGood_name()+"]库存不足");
			   }
		   } else {
			   result.inErrorMessage(963903001, "");
			   result.setResultMessage("商品库存不足");
		   }
	   }
	   
	   return result;
	}
}
