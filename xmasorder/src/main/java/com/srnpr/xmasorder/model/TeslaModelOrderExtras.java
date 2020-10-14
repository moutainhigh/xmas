package com.srnpr.xmasorder.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class TeslaModelOrderExtras {

	@ZapcomApi(value = "订单编号", remark = "订单编号",require=1, demo = "OS")
	private String orderCode = "";
	
	@ZapcomApi(value = "app编号", remark = "app编号",require=1, demo = "SI2003")
	private String sellerCode = "";
	
	@ZapcomApi(value = "app版本信息", remark = "app版本信息",require=1, demo = "1.0.0")
	private String vision = "";
	
	@ZapcomApi(value = "手机型号", remark = "手机型号", demo = "mi3")
	private String model = "";

	@ZapcomApi(value = "设备的唯一标识", remark = "设备的唯一标识", demo = "advertisingIdentifier")
	private String uniqid = "";

	@ZapcomApi(value = "mac地址", remark = "mac地址", demo = "mac")
	private String mac = "";

	@ZapcomApi(value = "手机操作系统", remark = "手机操作系统", demo = "ios")
	private String os = "";

	@ZapcomApi(value = "手机操作系统及版本", remark = "手机操作系统及版本", demo = "os_info")
	private String osInfo = "";

	@ZapcomApi(value = "渠道号", remark = "渠道号", demo = "9100701")
	private String fromCode = "";

	@ZapcomApi(value = "屏幕分辨率", remark = "屏幕分辨率", demo = "800x480")
	private String screen = "";

	@ZapcomApi(value = "运营商SIM卡国家码和网络码", remark = "运营商SIM卡国家码和网络码", demo = "46001")
	private String op = "";

	@ZapcomApi(value = "产品名称", remark = "产品名称", demo = "56mv_phone")
	private String product = "";

	@ZapcomApi(value = "网络状态", remark = "网络状态", demo = "wifi")
	private String netType = "";
	
	@ZapcomApi(value = "创建人", remark = "创建人", demo = "")
	private String createUser = "";
	
	@ZapcomApi(value = "创建时间", remark = "创建时间", demo = "")
	private String createTime = "";
	
	@ZapcomApi(value = "设备唯一编号", remark = "设备唯一编号", demo = "")
	private String idfa = "";
	
	
	public String getVision() {
		return vision;
	}

	public void setVision(String vision) {
		this.vision = vision;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUniqid() {
		return uniqid;
	}

	public void setUniqid(String uniqid) {
		this.uniqid = uniqid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public String getOsInfo() {
		return osInfo;
	}

	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

}
