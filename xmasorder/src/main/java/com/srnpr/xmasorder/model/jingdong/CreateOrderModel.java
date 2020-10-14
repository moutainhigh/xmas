package com.srnpr.xmasorder.model.jingdong;

import java.util.ArrayList;
import java.util.List;

/**
 * 下单请求对象
 * @remark 
 * @author 任宏斌
 * @date 2019年5月15日
 */
public class CreateOrderModel {

	/**
	 * 第三方的订单单号（客户根据自己规则定义，不超20位）
	 */
	private String thirdOrder = "";
	
	/**
	 * sku信息
	 */
	private List<SkuModel> sku = new ArrayList<SkuModel>();
	
	/**
	 * 收货人（少于10字）
	 */
	private String name = "";
	
	/**
	 * 一级地址id
	 */
	private int province = 0;
	
	/**
	 * 二级地址id
	 */
	private int city = 0;
	
	/**
	 * 三级地址id
	 */
	private int county = 0;
	
	/**
	 * 四级地址id
	 */
	private int town = 0;
	
	/**
	 * 详细地址（少于30字）
	 */
	private String address = "";
	
	/**
	 * 邮编
	 */
	private String zip = "";
	
	/**
	 * 座机号
	 */
	private String phone = "";
	
	/**
	 * 手机号
	 */
	private String mobile = "";
	
	/**
	 * 邮箱（接收订单邮件，买断模式可传B端商家邮箱地址）
	 */
	private String email = "";
	
	/**
	 * 备注（少于100字）
	 */
	private String remark = "";
	
	/**
	 * 开票方式(0为订单预借，1为随货开票， 2为集中开票(买断模式固定传2) )
	 */
	private int invoiceState = 2;
	
	/**
	 * 1普通发票2增值税发票
	 */
	private int invoiceType = 2;
	
	/**
	 * 4个人，5单位（买断模式下仅传5）
	 */
	private int selectedInvoiceTitle = 5;
	
	/**
	 * 发票抬头  (如果selectedInvoiceTitle=5则此字段Y)
	 */
	private String companyName = "";
	
	/**
	 * 1:明细，3：电脑配件，19:耗材，22：办公用品
	 * 备注:若增值发票则只能选1 明细
	 */
	private int invoiceContent = 1;
	
	/**
	 * bookInvoiceContent=4(图书普票随货开必传，其他不传)
	 */
	private int bookInvoiceContent = 4;

	/**
	 * 4：在线支付（余额支付）
	 * 101：金采支付
	 * 买断业务预存款客户下单传4
	 */
	private int paymentType = 4;
	
	/**
	 * 买断业务预存款下单固定传1， 使用余额
	 * 非预存款下单固定0 不使用余额
	 */
	private int isUseBalance = 1;
	
	/**
	 * 是否预占库存：
	 * 0是预占库存（后续需要调用确认预占库存订单接口）
	 * 1不预占库存
	 */
	private int submitState = 0;
	
	/**
	 * 增值票收票人姓名
	 * 备注：当invoiceType=2 且invoiceState=1时则此字段必填
	 */
	private String invoiceName = "";
	
	/**
	 * 增值票收票人电话
	 * 备注：当invoiceType=2且invoiceState=1 时则此字段必填
	 */
	private String invoicePhone = "";
	
	/**
	 * 增值票收票人所在省(京东地址编码)
	 * 备注：当invoiceType=2且invoiceState=1 时则此字段必填
	 */
	private int invoiceProvice = 0;
	
	/**
	 * 增值票收票人所在市(京东地址编码)
	 * 备注：当invoiceType=2 且invoiceState=1时则此字段必填
	 */
	private int invoiceCity = 0;
	
	/**
	 * 增值票收票人所在区/县(京东地址编码)
	 * 备注：当invoiceType=2 且invoiceState=1时则此字段必填
	 */
	private int invoiceCounty = 0;
	
	/**
	 * 增值票收票人所在地址
	 * 备注：当invoiceType=2 且invoiceState=1时则此字段必填
	 */
	private String invoiceAddress = "";

	public String getThirdOrder() {
		return thirdOrder;
	}

	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}

	public List<SkuModel> getSku() {
		return sku;
	}

	public void setSku(List<SkuModel> sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}

	public int getTown() {
		return town;
	}

	public void setTown(int town) {
		this.town = town;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getInvoiceState() {
		return invoiceState;
	}

	public void setInvoiceState(int invoiceState) {
		this.invoiceState = invoiceState;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public int getSelectedInvoiceTitle() {
		return selectedInvoiceTitle;
	}

	public void setSelectedInvoiceTitle(int selectedInvoiceTitle) {
		this.selectedInvoiceTitle = selectedInvoiceTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(int invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public int getBookInvoiceContent() {
		return bookInvoiceContent;
	}

	public void setBookInvoiceContent(int bookInvoiceContent) {
		this.bookInvoiceContent = bookInvoiceContent;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public int getIsUseBalance() {
		return isUseBalance;
	}

	public void setIsUseBalance(int isUseBalance) {
		this.isUseBalance = isUseBalance;
	}

	public int getSubmitState() {
		return submitState;
	}

	public void setSubmitState(int submitState) {
		this.submitState = submitState;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public int getInvoiceProvice() {
		return invoiceProvice;
	}

	public void setInvoiceProvice(int invoiceProvice) {
		this.invoiceProvice = invoiceProvice;
	}

	public int getInvoiceCity() {
		return invoiceCity;
	}

	public void setInvoiceCity(int invoiceCity) {
		this.invoiceCity = invoiceCity;
	}

	public int getInvoiceCounty() {
		return invoiceCounty;
	}

	public void setInvoiceCounty(int invoiceCounty) {
		this.invoiceCounty = invoiceCounty;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getInvoicePhone() {
		return invoicePhone;
	}

	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}
	
}
