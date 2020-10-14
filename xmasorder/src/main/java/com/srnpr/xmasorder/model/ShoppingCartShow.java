package com.srnpr.xmasorder.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车展示使用
*    xiegj
*/
public class ShoppingCartShow  {

	@ZapcomApi(value="商品总数量",remark="有效和无效商品总数量")
	private long acount_num = 0;
	
	@ZapcomApi(value="优惠",remark="优惠")
	private Double allDerateMoney = 0.00;
	
	@ZapcomApi(value="总计",remark="总计")
	private Double allNormalMoney = 0.00;
	
	@ZapcomApi(value="合计",remark="合计")
	private Double allPayMoney = 0.00;
	
	@ZapcomApi(value="结算时选中商品个数",remark="结算时选中商品个数")
	private long chooseGoodsNum = 0;
	
	@ZapcomApi(value="失效商品数量",remark="无效商品数量")
	private long disable_account_num = 0;
	
	@ZapcomApi(value="失效商品条数",remark="失效商品条数")
	private long disableSku = 0;
	
	@ZapcomApi(value="优惠头条",remark="优惠头条信息")
	private String salesAdv = "";

	@ZapcomApi(value="购物车商品列表",remark="购物车商品列表信息")
	private List<ShoppingCartItem> shoppingCartList = new ArrayList<ShoppingCartItem>();

	@ZapcomApi(value = "购物车中无效商品", remark = "购物车中无效商品", demo = "0")
	private List<ShoppingCartGoodsInfo> disableGoods = new ArrayList<ShoppingCartGoodsInfo>();
	
	public long getAcount_num() {
		return acount_num;
	}

	public void setAcount_num(long acount_num) {
		this.acount_num = acount_num;
	}

	public Double getAllDerateMoney() {
		return allDerateMoney;
	}

	public void setAllDerateMoney(Double allDerateMoney) {
		this.allDerateMoney = allDerateMoney;
	}

	public Double getAllNormalMoney() {
		return allNormalMoney;
	}

	public void setAllNormalMoney(Double allNormalMoney) {
		this.allNormalMoney = allNormalMoney;
	}

	public Double getAllPayMoney() {
		return allPayMoney;
	}

	public void setAllPayMoney(Double allPayMoney) {
		this.allPayMoney = allPayMoney;
	}

	public long getChooseGoodsNum() {
		return chooseGoodsNum;
	}

	public void setChooseGoodsNum(long chooseGoodsNum) {
		this.chooseGoodsNum = chooseGoodsNum;
	}

	public long getDisable_account_num() {
		return disable_account_num;
	}

	public void setDisable_account_num(long disable_account_num) {
		this.disable_account_num = disable_account_num;
	}

	public long getDisableSku() {
		return disableSku;
	}

	public void setDisableSku(long disableSku) {
		this.disableSku = disableSku;
	}

	public String getSalesAdv() {
		return salesAdv;
	}

	public void setSalesAdv(String salesAdv) {
		this.salesAdv = salesAdv;
	}

	public List<ShoppingCartItem> getShoppingCartList() {
		return shoppingCartList;
	}

	public void setShoppingCartList(List<ShoppingCartItem> shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}

	public List<ShoppingCartGoodsInfo> getDisableGoods() {
		return disableGoods;
	}

	public void setDisableGoods(List<ShoppingCartGoodsInfo> disableGoods) {
		this.disableGoods = disableGoods;
	}
}

