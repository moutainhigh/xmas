package com.srnpr.xmasorder.model.kaola;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Packages {
	/**
	 * 该包裹总的支付金额，含税含运
	 */
	private BigDecimal payAmount;
	
	/**
	 * 总税费，含运费税
	 */
	private BigDecimal taxPayAmount;
	
	/**
	 * 运费
	 */
	private BigDecimal logisticsPayAmount;
	
	private int importType;
	
	/**
	 * level=0,无需实名；level=1，需真实姓名和证件号；level=2，需真实姓名+证件号+证件正反照片
	 */
	private int needVerifyLevel;
	
	private int packageOrder;
	
	/**
	 * 仓库
	 */
	private Warehouse warehouse = new Warehouse();
	
	/**
	 * 商品列表
	 */
	private List<KaolaGoods> goodsList = new ArrayList<KaolaGoods>();
	
	/**
	 * 
	 */
	private String goodsSource;

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getTaxPayAmount() {
		return taxPayAmount;
	}

	public void setTaxPayAmount(BigDecimal taxPayAmount) {
		this.taxPayAmount = taxPayAmount;
	}

	public BigDecimal getLogisticsPayAmount() {
		return logisticsPayAmount;
	}

	public void setLogisticsPayAmount(BigDecimal logisticsPayAmount) {
		this.logisticsPayAmount = logisticsPayAmount;
	}

	public int getImportType() {
		return importType;
	}

	public void setImportType(int importType) {
		this.importType = importType;
	}

	public int getNeedVerifyLevel() {
		return needVerifyLevel;
	}

	public void setNeedVerifyLevel(int needVerifyLevel) {
		this.needVerifyLevel = needVerifyLevel;
	}

	public int getPackageOrder() {
		return packageOrder;
	}

	public void setPackageOrder(int packageOrder) {
		this.packageOrder = packageOrder;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public List<KaolaGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<KaolaGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getGoodsSource() {
		return goodsSource;
	}

	public void setGoodsSource(String goodsSource) {
		this.goodsSource = goodsSource;
	}
}
