package com.srnpr.xmasorder.model;

import java.math.BigDecimal;

import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.sun.org.apache.bcel.internal.generic.NEW;



/**   
 * 	购物车
*    xiegj
*/
public class ShoppingCartCache  {
	
	@ZapcomApi(value = "用户编号", remark = "用户编号", demo = "MI123456")
	private String member_code = "";
	
	@ZapcomApi(value = "商品编号", remark = "商品编号", demo = "8016123456")
	private String product_code = "";
	
	@ZapcomApi(value = "产品编号", remark = "产品编号", demo = "8019123456")
	private String sku_code = "";
	
	@ZapcomApi(value = "产品数量", remark = "产品数量", demo = "123")
	private long sku_num = 0;
	
	@ZapcomApi(value = "是否选择", remark = "是否选择", demo = "0")
	private String choose_flag = "0";
	
	@ZapcomApi(value = "添加时间", remark = "添加时间", demo = "2015-11-11 11:11:11")
	private String create_time = DateUtil.getSysDateTimeString();
	
	@ZapcomApi(value="商品价格",remark="商品添加到购物车价格",demo = "100.00")
	private BigDecimal sku_add_shop_price = new BigDecimal(0);
	
	@ZapcomApi(value="是否原件购买",remark="（0：否；1：是）针对商品参与活动但活动库存售罄的情况下，想要原价购买")
	private String isSkuPriceToBuy = "0";
	
	@ZapcomApi(value = "分销人编号")
    private String fxrcode = "";
	
	@ZapcomApi(value = "推广人编号")
    private String shareCode = "";
	
	@ZapcomApi(value="推广赚推广人编号",remark = "")
	private String tgzUserCode = "";
	
	@ZapcomApi(value="推广赚买家秀编号",remark = "")
	private String tgzShowCode = "";

	public String getMember_code() {
		return member_code;
	}

	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}

	public String getFxrcode() {
		return fxrcode;
	}

	public void setFxrcode(String fxrcode) {
		this.fxrcode = fxrcode;
	}

	public String getTgzUserCode() {
		return tgzUserCode;
	}

	public void setTgzUserCode(String tgzUserCode) {
		this.tgzUserCode = tgzUserCode;
	}

	public String getTgzShowCode() {
		return tgzShowCode;
	}

	public void setTgzShowCode(String tgzShowCode) {
		this.tgzShowCode = tgzShowCode;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public long getSku_num() {
		return sku_num;
	}

	public void setSku_num(long sku_num) {
		this.sku_num = sku_num;
	}

	public String getChoose_flag() {
		return choose_flag;
	}

	public void setChoose_flag(String choose_flag) {
		this.choose_flag = choose_flag;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public BigDecimal getSku_add_shop_price() {
		return sku_add_shop_price;
	}

	public void setSku_add_shop_price(BigDecimal sku_add_shop_price) {
		this.sku_add_shop_price = sku_add_shop_price;
	}

	public String getIsSkuPriceToBuy() {
		return isSkuPriceToBuy;
	}

	public void setIsSkuPriceToBuy(String isSkuPriceToBuy) {
		this.isSkuPriceToBuy = isSkuPriceToBuy;
	}

	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}
	
	
}

