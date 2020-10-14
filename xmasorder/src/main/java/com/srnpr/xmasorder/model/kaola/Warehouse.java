package com.srnpr.xmasorder.model.kaola;

public class Warehouse {
	/**
	 * 仓库id，下单时传入
	 */
	private int warehouseId;
	
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	
	/**
	 * 仓库别名
	 */
	private String warehouseNameAlias;

	public int getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getWarehouseNameAlias() {
		return warehouseNameAlias;
	}

	public void setWarehouseNameAlias(String warehouseNameAlias) {
		this.warehouseNameAlias = warehouseNameAlias;
	}
}
