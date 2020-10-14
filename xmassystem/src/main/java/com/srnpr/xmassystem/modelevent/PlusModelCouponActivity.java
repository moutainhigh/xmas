package com.srnpr.xmassystem.modelevent;

import java.math.BigDecimal;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/***
 * 优惠券活动实体类
 */
public class PlusModelCouponActivity implements IPlusModel, IPlusModelRefresh {

	/** 当前版本号 */
	public static final int _VERSION = 4;
	/** 版本号 */
	@ZapcomApi(value = "数据版本号")
	private int v = 0;
	
	@Override
	public boolean isRefresh() {
		return v < _VERSION;
	}
	
	public void setV(int v) {
		this.v = v;
	}
	
	public int getV() {
		return v;
	}

	private String activityCode = "";
	private String activityName = "";
	private String activityType = "";
	private String beginTime = "";
	private String endTime = "";
	private String provideType = "";
	private int flag;
	private int provideNum;
	private String outActivityCode = "";
	private String isMultiUse = "";
	private String isDetailShow = "";
	private BigDecimal disup_amt = BigDecimal.ZERO;
	private String minlimit_tp = "";
	private BigDecimal minlimit_amt = BigDecimal.ZERO;
	private String is_onelimit = "";
	private BigDecimal mindis_amt = BigDecimal.ZERO;
	private String is_change = "";

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProvideType() {
		return provideType;
	}

	public void setProvideType(String provideType) {
		this.provideType = provideType;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getProvideNum() {
		return provideNum;
	}

	public void setProvideNum(int provideNum) {
		this.provideNum = provideNum;
	}

	public String getOutActivityCode() {
		return outActivityCode;
	}

	public void setOutActivityCode(String outActivityCode) {
		this.outActivityCode = outActivityCode;
	}

	public String getIsMultiUse() {
		return isMultiUse;
	}

	public void setIsMultiUse(String isMultiUse) {
		this.isMultiUse = isMultiUse;
	}

	public String getIsDetailShow() {
		return isDetailShow;
	}

	public void setIsDetailShow(String isDetailShow) {
		this.isDetailShow = isDetailShow;
	}

	public BigDecimal getDisup_amt() {
		return disup_amt;
	}

	public void setDisup_amt(BigDecimal disup_amt) {
		this.disup_amt = disup_amt;
	}

	public String getMinlimit_tp() {
		return minlimit_tp;
	}

	public void setMinlimit_tp(String minlimit_tp) {
		this.minlimit_tp = minlimit_tp;
	}

	public BigDecimal getMinlimit_amt() {
		return minlimit_amt;
	}

	public void setMinlimit_amt(BigDecimal minlimit_amt) {
		this.minlimit_amt = minlimit_amt;
	}

	public String getIs_onelimit() {
		return is_onelimit;
	}

	public void setIs_onelimit(String is_onelimit) {
		this.is_onelimit = is_onelimit;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public BigDecimal getMindis_amt() {
		return mindis_amt;
	}

	public void setMindis_amt(BigDecimal mindis_amt) {
		this.mindis_amt = mindis_amt;
	}

	public String getIs_change() {
		return is_change;
	}

	public void setIs_change(String is_change) {
		this.is_change = is_change;
	}
	
}
