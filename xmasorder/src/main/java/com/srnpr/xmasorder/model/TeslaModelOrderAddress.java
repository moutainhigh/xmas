package com.srnpr.xmasorder.model;

/**
 * 订单收货地址及发票信息
 * @author jlin
 *
 */
public class TeslaModelOrderAddress {

	private String uid = "";
	/**
	 * 订单编号
	 */
	private String orderCode = "";
	
	/**
	 * 地区编码
	 */
	private String areaCode = "";
	
	/**
	 * 地址信息
	 */
	private String address="";
	
	/**
	 * 邮政编码
	 */
	private String postcode="";
	
	/**
	 * 电话
	 */
	private String mobilephone="";
	
	/**
	 * 固定电话
	 */
	private String telephone = "";
	
	/**
	 * 收货人
	 */
	private String receivePerson = "";
	
	/**
	 * 电子邮箱
	 */
	private String email = "";
	
	/**
	 * 发票抬头
	 */
	private String invoiceTitle = "";
	
	/**
	 * 是否开发票 1 开， 0 不开 
	 */
	private int flagInvoice=1;
	
	/**
	 * 发票类型
	 * 449746310001	普通发票
	 * 449746310002	增值税发票
	 */
	private String invoiceType="";
	
	
	/**
	 * 发票内容-暂时请添明细
	 */
	private String invoiceContent="";
	
	
	/**
	 * 订单备注
	 */
	private String remark = "";
	
	/**
	 * 地址编号
	 */
    private String addressCode = "";
	
	/**
	 * 发票状态
	 */
	private String invoiceStatus="449747240001";
	
	/**  真实姓名 **/
	private String authTrueName = "";
	
	/**  证件类型 **/
	private String authIdcardType = "";
	
	/**  证件编号 **/
	private String authIdcardNumber = "";
	
	/**  联系电话 **/
	private String authPhoneNumber = "";
	
	/**  电子邮件**/
	private String authEmail = "";
	
	/**  联系地址 **/
	private String authAddress = "";
	
	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getAreaCode() {
		return areaCode;
	}


	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobilephone() {
		return mobilephone;
	}


	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getReceivePerson() {
		return receivePerson;
	}


	public void setReceivePerson(String receivePerson) {
		this.receivePerson = receivePerson;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getInvoiceTitle() {
		return invoiceTitle;
	}


	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceType() {
		return invoiceType;
	}


	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}


	public String getInvoiceContent() {
		return invoiceContent;
	}


	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getPostcode() {
		return postcode;
	}


	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}


	public String getInvoiceStatus() {
		return invoiceStatus;
	}


	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}


	public String getAddressCode() {
		return addressCode;
	}


	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public int getFlagInvoice() {
		return flagInvoice;
	}


	public void setFlagInvoice(int flagInvoice) {
		this.flagInvoice = flagInvoice;
	}


	public String getAuthTrueName() {
		return authTrueName;
	}


	public void setAuthTrueName(String authTrueName) {
		this.authTrueName = authTrueName;
	}


	public String getAuthIdcardType() {
		return authIdcardType;
	}


	public void setAuthIdcardType(String authIdcardType) {
		this.authIdcardType = authIdcardType;
	}


	public String getAuthIdcardNumber() {
		return authIdcardNumber;
	}


	public void setAuthIdcardNumber(String authIdcardNumber) {
		this.authIdcardNumber = authIdcardNumber;
	}


	public String getAuthPhoneNumber() {
		return authPhoneNumber;
	}


	public void setAuthPhoneNumber(String authPhoneNumber) {
		this.authPhoneNumber = authPhoneNumber;
	}


	public String getAuthEmail() {
		return authEmail;
	}


	public void setAuthEmail(String authEmail) {
		this.authEmail = authEmail;
	}


	public String getAuthAddress() {
		return authAddress;
	}


	public void setAuthAddress(String authAddress) {
		this.authAddress = authAddress;
	}
}
