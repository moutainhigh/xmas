package com.srnpr.xmassystem.modelproduct;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 订单收货地址及发票信息
 * @author shiyz
 *
 */
public class PlusModelOrderAddress implements IPlusModel{

	@ZapcomApi(value="地址编号")
	private  String  addressCode = "";
	
	@ZapcomApi(value="地区编码")
	private String areaCode = "";
	
	@ZapcomApi(value="地址信息")
	private String address="";
	
	@ZapcomApi(value="邮政编码")
	private String postCode="";
	
	@ZapcomApi(value="电话")
	private String mobilephone="";
	
	@ZapcomApi(value="固定电话")
	private String telephone = "";
	
	@ZapcomApi(value="收货人")
	private String receivePerson = "";
	
	@ZapcomApi(value="电子邮箱")
	private String email = "";
	
	@ZapcomApi(value="证件号码")
	private String idcardNumber = "";
	
	@ZapcomApi(value="真实姓名")
	private String trueName = "";
	
	@ZapcomApi(value="证件类型")
	private String idcardType = "";
	
	@ZapcomApi(value="联系电话")
	private String phoneNumber = "";
	
	@ZapcomApi(value="联系地址")
	private String cardAddress = "";
	
	@ZapcomApi(value="证件邮箱")
	private String cardEmail = "";

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


	public String getPostCode() {
		return postCode;
	}


	public void setPostCode(String postCode) {
		this.postCode = postCode;
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


	public String getAddressCode() {
		return addressCode;
	}


	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}


	public String getIdcardNumber() {
		return idcardNumber;
	}


	public void setIdcardNumber(String idcardNumber) {
		this.idcardNumber = idcardNumber;
	}

	public String getTrueName() {
		return trueName;
	}


	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}


	public String getIdcardType() {
		return idcardType;
	}


	public void setIdcardType(String idcardType) {
		this.idcardType = idcardType;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getCardAddress() {
		return cardAddress;
	}


	public void setCardAddress(String cardAddress) {
		this.cardAddress = cardAddress;
	}


	public String getCardEmail() {
		return cardEmail;
	}


	public void setCardEmail(String cardEmail) {
		this.cardEmail = cardEmail;
	}
	
}
