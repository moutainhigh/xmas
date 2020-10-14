package com.srnpr.xmasorder.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;

import com.srnpr.xmasorder.model.DistributionInfoModel;
import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.xmasorder.model.ShoppingCartShow;
import com.srnpr.xmasorder.model.TelsaModelOrderStockInfo;
import com.srnpr.xmasorder.model.TeslaModelCouponInfo;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.srnpr.xmasorder.model.TeslaModelJJG;
import com.srnpr.xmasorder.model.TeslaModelJdOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderActivity;
import com.srnpr.xmasorder.model.TeslaModelOrderAddress;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderDetailForThird;
import com.srnpr.xmasorder.model.TeslaModelOrderExtras;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderInfoUpper;
import com.srnpr.xmasorder.model.TeslaModelOrderOther;
import com.srnpr.xmasorder.model.TeslaModelOrderPay;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.model.TeslaModelStatus;
import com.srnpr.xmasorder.model.TeslaModelSubRole;
import com.srnpr.xmasorder.model.TeslaModelUseForPay;
import com.srnpr.xmasorder.model.TeslaModelYYG;
import com.srnpr.xmasorder.model.kaola.OrderForm;
import com.srnpr.xmassystem.modelbean.ActivityAgent;
import com.srnpr.xmassystem.modelbean.HjybeanConsumeSetModel;
import com.srnpr.xmassystem.modelevent.PlusModelHuDongEvent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class TeslaXOrder {

	@ZapcomApi(value = "订单主信息")
	private TeslaModelOrderInfoUpper uorderInfo = new TeslaModelOrderInfoUpper();

	@ZapcomApi(value = "小订单主信息")
	private List<TeslaModelOrderInfo> sorderInfo = new ArrayList<TeslaModelOrderInfo>();
	
	@ZapcomApi(value = "订单明细")
	private List<TeslaModelOrderDetail> orderDetails = new ArrayList<TeslaModelOrderDetail>();
	
	@ZapcomApi(value = "活动信息")
	private List<TeslaModelOrderActivity> activityList = new ArrayList<TeslaModelOrderActivity>(); 
	
	@ZapcomApi(value = "支付信息")
	private List<TeslaModelOrderPay> ocOrderPayList = new ArrayList<TeslaModelOrderPay>();
	
	@ZapcomApi(value = "地址信息")
	private TeslaModelOrderAddress address = new TeslaModelOrderAddress();
	
	@ZapcomApi(value = "订单标记")
	private TeslaModelStatus status = new TeslaModelStatus();
	
	@ZapcomApi(value="用户订单支付的支付信息")
	private TeslaModelUseForPay use = new TeslaModelUseForPay();
	
	@ZapcomApi(value = "微信支付时客户端IP地址", remark = "微信支付时客户端IP地址", demo = "8.8.8.8")
	private String pay_ip = "";
	
	@ZapcomApi(value = "拆单规则")
	private List<TeslaModelSubRole> roles = new ArrayList<TeslaModelSubRole>();
	
	@ZapcomApi(value = "商品展示使用")
	private List<TeslaModelShowGoods> showGoods  = new ArrayList<TeslaModelShowGoods>();
	
	@ZapcomApi(value="确认订单页金额显示：List（折扣名称，折扣类型（1加0减），折扣金额）",remark="List{首单立减30元,0,30.00}")
	private List<TeslaModelDiscount> showMoney = new ArrayList<TeslaModelDiscount>();
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003，微信小程序：449747430023", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "小程序公众号渠道来源", remark = "",demo = "")
	private String outChannelId = "";
	
	@ZapcomApi(value = "小程序直播来源房间号", remark = "",demo = "")
	private String liveRoomID = "";
	
	@ZapcomApi(value = "应付款", remark = "应付款",require=1, demo = "8888.88")
	private BigDecimal check_pay_money = BigDecimal.ZERO;
	
	@ZapcomApi(value = "订单附加信息")
	private TeslaModelOrderExtras extras = new TeslaModelOrderExtras();
	
	@ZapcomApi(value = "订单分公用扩展字段")
	private TeslaModelOrderOther orderOther = new TeslaModelOrderOther();
	
	@ZapcomApi(value = "购物车展示使用")
	private ShoppingCartShow cartShow = new ShoppingCartShow();
	
	@ZapcomApi(value = "是否拼好货")
	private boolean phhSucc = false; 
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "用户编号", remark = "默认为空   传递值为MI开头的那串数字")
	private String isMemberCode="";
	
	@ZapcomApi(value = "一元购信息")
	private TeslaModelYYG yyg = new TeslaModelYYG();
	
	@ZapcomApi(value="主播房间号",remark="主播房间号",require=0,demo="46465456")
	private String roomId="";
	
	@ZapcomApi(value="主播ID",remark="主播ID",require=0,demo="46579")
	private String anchorId="";
	
	@ZapcomApi(value="用户拥有的总惠豆数")
	private BigDecimal allHjyBean = BigDecimal.ZERO;
	
	@ZapcomApi(value="最大能使用的惠豆数")
	private BigDecimal maxUseHjyBean = BigDecimal.ZERO; 
	
	@ZapcomApi(value="惠豆相关配置")
	private HjybeanConsumeSetModel homehasBeanConsumeConfig = new HjybeanConsumeSetModel();
	
	@ZapcomApi(value = "家有客代号", remark = "后台根据用户编号获取")
	private String custId="";
	
	@ZapcomApi(value="用户拥有的总积分")
	private BigDecimal allIntegral = BigDecimal.ZERO;
	
	@ZapcomApi(value="最大能使用的积分数")
	private BigDecimal maxUseIntegral = BigDecimal.ZERO; 
	
	@ZapcomApi(value="在线支付立减活动预计优惠金额")
	private BigDecimal onlinePayEventMoney = BigDecimal.ZERO; 
	
	
	@ZapcomApi(value="用户拥有的总储值金")
	private BigDecimal allCzj = BigDecimal.ZERO;
	
	@ZapcomApi(value="最大能使用的储值金")
	private BigDecimal maxUseCzj = BigDecimal.ZERO;
	
	@ZapcomApi(value="用户拥有的总暂存款")
	private BigDecimal allZck = BigDecimal.ZERO;
	
	@ZapcomApi(value="最大能使用的暂存款")
	private BigDecimal maxUseZck = BigDecimal.ZERO;
	
	@ZapcomApi(value="用户拥有的总惠币")
	private BigDecimal allHjycoin = BigDecimal.ZERO;
	
	@ZapcomApi(value="最大能使用的惠币")
	private BigDecimal maxUseHjycoin = BigDecimal.ZERO;
	
	@ZapcomApi(value="是否是积分商城")
	private boolean isPointShop = false;
	
	@ZapcomApi(value="单件商品订单时的库存信息")
	private TelsaModelOrderStockInfo stockInfo = new TelsaModelOrderStockInfo();
	
	@ZapcomApi(value = "CPS渠道编码", remark = "默认为空")
	private String cpsCode = "";
	

	@ZapcomApi(value="第三方商户商品详情", remark= "")
	private List<TeslaModelOrderDetailForThird> thirdDetail = new ArrayList<TeslaModelOrderDetailForThird>();

	@ZapcomApi(value = "是否有考拉订单", remark = "默认为0，有考拉订单时为1")
	private Integer isKaolaOrder = 0;
	
	@ZapcomApi(value="网易考拉订单确认时返回的订单信息")
	private OrderForm orderForm = new OrderForm();

	
	@ZapcomApi(value="是否原价购买", remark="0：不原价，1：原价")
	private String isOriginal = "0";
	
	@ZapcomApi(value="拼团标示", remark="0：非拼团，1：拼团")
	private String collageFlag = "0";
	
	@ZapcomApi(value="团编码", remark="要参与团的团编码")
	private String collageCode = "";
	
	@ZapcomApi(value="优惠券自动选中标识", remark="0：不自动选中，1: 需要自动选中")
	private String autoSelectCoupon = "0";
	
	@ZapcomApi(value="未过期优惠券列表")
	private List<TeslaModelCouponInfo> couponInfoList = new ArrayList<TeslaModelCouponInfo>();
	
	@ZapcomApi(value = "京东小订单主信息")
	private List<TeslaModelJdOrderInfo> jdOrderInfo = new ArrayList<TeslaModelJdOrderInfo>();
	
	@ZapcomApi(value="活动编号", remark = "兑换码兑换活动")
	private String activityCode = "";
	
	@ZapcomApi(value = "兑换码", remark = "兑换码兑换活动")
	private String redeemCode = "";
	
	@ZapcomApi(value="版块标识", remark="版块标识:积分签到版块为10")
	private String blockSign = "";
	
	@ZapcomApi(value="缤纷扫码编号", remark="")
	private String qrcode = "";
	
	@ZapcomApi(value="没库存的商品列表")
	private List<NoStockOrFailureGoods> noStockOrFailureGoods = new ArrayList<NoStockOrFailureGoods>();
		
	@ZapcomApi(value="下单商品所属分销人信息")
	private List<DistributionInfoModel> productSharedInfos = new ArrayList<DistributionInfoModel>();
	
	@ZapcomApi(value="惠惠农场果树编号")
	private String treeCode = "";
	
	@ZapcomApi(value="互动活动编号")
	private String huDongCode = "";
	
	/** 订单参与的互动活动信息 */
	private PlusModelHuDongEvent huDongEvent = null;

	/** 分销活动  如果有分销商品且存在分销活动则此字段是当前的分销活动 */
	/** 没有分销活动时则字段值必须为null，不要设置默认对象**/
	private ActivityAgent activityAgent = null;
	
	@ZapcomApi(value="特殊活动编号", remark="例：投票换购")
	private String eventCode = "";
	
	@SuppressWarnings("rawtypes")
	@ZapcomApi(value = "需要最后插入定时任务表的订单数据，同一个key可以添加多条定时任务")
	private MultiValueMap execInfoMap = MultiValueMap.decorate(new HashMap(), HashSet.class);
	
	@ZapcomApi(value="分销商品编号", remark="如果走了分销逻辑的商品，推广赚逻辑跳过，此字段在TeslaMakeShareOrder使用")
	private Map<String,String> fxFlagMap = new HashMap<String,String>();
	
	
	public List<DistributionInfoModel> getProductSharedInfos() {
		return productSharedInfos;
	}

	public void setProductSharedInfos(List<DistributionInfoModel> productSharedInfos) {
		this.productSharedInfos = productSharedInfos;
	}
	
	@ZapcomApi(value="换购信息列表")
	private List<TeslaModelJJG> JJGList = new ArrayList<TeslaModelJJG>();
	
	@ZapcomApi(value = "订单页面来源", remark = "449748740012 进线短信召回订单（集团渠道） ；449748740013进线短信召回订单（惠家有渠道）；449748740007	赠送雨滴页下单\n" + 
			"449748740006	逛会场下单\n" + 
			"449748740005	看商品下单\n" + 
			"449748740004	为您推荐\n" + 
			"449748740003	节目表\n" + 
			"449748740002	福利大转盘\n" + 
			"449748740001	积分打卡", demo = "449715190001" )
	private String orderPageSouce = "";
	
	public List<TeslaModelJJG> getJJGList() {
		return JJGList;
	}

	public void setJJGList(List<TeslaModelJJG> jJGList) {
		JJGList = jJGList;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getBlockSign() {
		return blockSign;
	}

	public void setBlockSign(String blockSign) {
		this.blockSign = blockSign;
	}

	public MultiValueMap getExecInfoMap() {
		return execInfoMap;
	}

	public void setExecInfoMap(MultiValueMap execInfoMap) {
		this.execInfoMap = execInfoMap;
	}

	public ActivityAgent getActivityAgent() {
		return activityAgent;
	}

	public void setActivityAgent(ActivityAgent activityAgent) {
		this.activityAgent = activityAgent;
	}

	public List<TeslaModelOrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<TeslaModelOrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public TeslaModelStatus getStatus() {
		return status;
	}

	public void setStatus(TeslaModelStatus status) {
		this.status = status;
	}

	public List<TeslaModelOrderActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<TeslaModelOrderActivity> activityList) {
		this.activityList = activityList;
	}

	public List<TeslaModelOrderPay> getOcOrderPayList() {
		return ocOrderPayList;
	}

	public void setOcOrderPayList(List<TeslaModelOrderPay> ocOrderPayList) {
		this.ocOrderPayList = ocOrderPayList;
	}

	public TeslaModelOrderAddress getAddress() {
		return address;
	}

	public void setAddress(TeslaModelOrderAddress address) {
		this.address = address;
	}


	public List<TeslaModelSubRole> getRoles() {
		return roles;
	}

	public void setRoles(List<TeslaModelSubRole> roles) {
		this.roles = roles;
	}

	public List<TeslaModelShowGoods> getShowGoods() {
		return showGoods;
	}

	public void setShowGoods(List<TeslaModelShowGoods> showGoods) {
		this.showGoods = showGoods;
	}

	public TeslaModelUseForPay getUse() {
		return use;
	}

	public void setUse(TeslaModelUseForPay use) {
		this.use = use;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getOutChannelId() {
		return outChannelId;
	}

	public void setOutChannelId(String outChannelId) {
		this.outChannelId = outChannelId;
	}
	
	public String getLiveRoomID() {
		return liveRoomID;
	}

	public void setLiveRoomID(String liveRoomID) {
		this.liveRoomID = liveRoomID;
	}

	public BigDecimal getCheck_pay_money() {
		return check_pay_money;
	}

	public void setCheck_pay_money(BigDecimal check_pay_money) {
		this.check_pay_money = check_pay_money;
	}

	public TeslaModelOrderExtras getExtras() {
		return extras;
	}

	public void setExtras(TeslaModelOrderExtras extras) {
		this.extras = extras;
	}

	public List<TeslaModelOrderInfo> getSorderInfo() {
		return sorderInfo;
	}

	public void setSorderInfo(List<TeslaModelOrderInfo> sorderInfo) {
		this.sorderInfo = sorderInfo;
	}

	public TeslaModelOrderInfoUpper getUorderInfo() {
		return uorderInfo;
	}

	public void setUorderInfo(TeslaModelOrderInfoUpper uorderInfo) {
		this.uorderInfo = uorderInfo;
	}

	public List<TeslaModelDiscount> getShowMoney() {
		return showMoney;
	}

	public void setShowMoney(List<TeslaModelDiscount> showMoney) {
		this.showMoney = showMoney;
	}

	public String getPay_ip() {
		return pay_ip;
	}

	public void setPay_ip(String pay_ip) {
		this.pay_ip = pay_ip;
	}

	public TeslaModelOrderOther getOrderOther() {
		return orderOther;
	}

	public void setOrderOther(TeslaModelOrderOther orderOther) {
		this.orderOther = orderOther;
	}

	public ShoppingCartShow getCartShow() {
		return cartShow;
	}

	public void setCartShow(ShoppingCartShow cartShow) {
		this.cartShow = cartShow;
	}

	public boolean isPhhSucc() {
		return phhSucc;
	}

	public void setPhhSucc(boolean phhSucc) {
		this.phhSucc = phhSucc;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	/**
	 * @return the isMemberCode
	 */
	public String getIsMemberCode() {
		return isMemberCode;
	}

	/**
	 * @param isMemberCode the isMemberCode to set
	 */
	public void setIsMemberCode(String isMemberCode) {
		this.isMemberCode = isMemberCode;
	}
	
	public TeslaModelYYG getYyg() {
		return yyg;
	}

	public void setYyg(TeslaModelYYG yyg) {
		this.yyg = yyg;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public BigDecimal getAllHjyBean() {
		return allHjyBean;
	}

	public void setAllHjyBean(BigDecimal allHjyBean) {
		this.allHjyBean = allHjyBean;
	}

	public BigDecimal getMaxUseHjyBean() {
		return maxUseHjyBean;
	}

	public void setMaxUseHjyBean(BigDecimal maxUseHjyBean) {
		this.maxUseHjyBean = maxUseHjyBean;
	}
	
	/**
	 * TeslaMakeBalanced节点 赋值 homehasBeanConsumeConfig
	 * @return
	 */
	public HjybeanConsumeSetModel getHomehasBeanConsumeConfig() {
		return homehasBeanConsumeConfig;
	}

	public void setHomehasBeanConsumeConfig(HjybeanConsumeSetModel homehasBeanConsumeConfig) {
		this.homehasBeanConsumeConfig = homehasBeanConsumeConfig;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public BigDecimal getAllIntegral() {
		return allIntegral;
	}

	public void setAllIntegral(BigDecimal allIntegral) {
		this.allIntegral = allIntegral;
	}

	public BigDecimal getMaxUseIntegral() {
		return maxUseIntegral;
	}

	public void setMaxUseIntegral(BigDecimal maxUseIntegral) {
		this.maxUseIntegral = maxUseIntegral;
	}

	public BigDecimal getOnlinePayEventMoney() {
		return onlinePayEventMoney;
	}

	public void setOnlinePayEventMoney(BigDecimal onlinePayEventMoney) {
		this.onlinePayEventMoney = onlinePayEventMoney;
	}

	public TelsaModelOrderStockInfo getStockInfo() {
		return stockInfo;
	}

	public void setStockInfo(TelsaModelOrderStockInfo stockInfo) {
		this.stockInfo = stockInfo;
	}
	
	public BigDecimal getAllCzj() {
		return allCzj;
	}

	public void setAllCzj(BigDecimal allCzj) {
		this.allCzj = allCzj;
	}

	public BigDecimal getAllZck() {
		return allZck;
	}

	public void setAllZck(BigDecimal allZck) {
		this.allZck = allZck;
	}

	public BigDecimal getMaxUseCzj() {
		return maxUseCzj;
	}

	public void setMaxUseCzj(BigDecimal maxUseCzj) {
		this.maxUseCzj = maxUseCzj;
	}

	public BigDecimal getMaxUseZck() {
		return maxUseZck;
	}

	public void setMaxUseZck(BigDecimal maxUseZck) {
		this.maxUseZck = maxUseZck;
	}
	
	public BigDecimal getAllHjycoin() {
		return allHjycoin;
	}

	public void setAllHjycoin(BigDecimal allHjycoin) {
		this.allHjycoin = allHjycoin;
	}

	public BigDecimal getMaxUseHjycoin() {
		return maxUseHjycoin;
	}

	public void setMaxUseHjycoin(BigDecimal maxUseHjycoin) {
		this.maxUseHjycoin = maxUseHjycoin;
	}

	public boolean isPointShop() {
		return isPointShop;
	}

	public void setPointShop(boolean isPointShop) {
		this.isPointShop = isPointShop;
	}

	public String getCpsCode() {
		return cpsCode;
	}

	public void setCpsCode(String cpsCode) {
		this.cpsCode = cpsCode;
	}


	public List<TeslaModelOrderDetailForThird> getThirdDetail() {
		return thirdDetail;
	}

	public void setThirdDetail(List<TeslaModelOrderDetailForThird> thirdDetail) {
		this.thirdDetail = thirdDetail;
	}
	public Integer getIsKaolaOrder() {
		return isKaolaOrder;
	}

	public void setIsKaolaOrder(Integer isKaolaOrder) {
		this.isKaolaOrder = isKaolaOrder;
	}

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;

	}

	public String getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(String isOriginal) {
		this.isOriginal = isOriginal;
	}

	public String getCollageFlag() {
		return collageFlag;
	}

	public void setCollageFlag(String collageFlag) {
		this.collageFlag = collageFlag;
	}

	public String getCollageCode() {
		return collageCode;
	}

	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}

	public List<TeslaModelJdOrderInfo> getJdOrderInfo() {
		return jdOrderInfo;
	}

	public void setJdOrderInfo(List<TeslaModelJdOrderInfo> jdOrderInfo) {
		this.jdOrderInfo = jdOrderInfo;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}
	public String getAutoSelectCoupon() {
		return autoSelectCoupon;
	}

	public void setAutoSelectCoupon(String autoSelectCoupon) {
		this.autoSelectCoupon = autoSelectCoupon;
	}

	public List<TeslaModelCouponInfo> getCouponInfoList() {
		return couponInfoList;
	}

	public void setCouponInfoList(List<TeslaModelCouponInfo> couponInfoList) {
		this.couponInfoList = couponInfoList;
	}

	public List<NoStockOrFailureGoods> getNoStockOrFailureGoods() {
		return noStockOrFailureGoods;
	}

	public void setNoStockOrFailureGoods(List<NoStockOrFailureGoods> noStockOrFailureGoods) {
		this.noStockOrFailureGoods = noStockOrFailureGoods;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getHuDongCode() {
		return huDongCode;
	}

	public void setHuDongCode(String huDongCode) {
		this.huDongCode = huDongCode;
	}

	public PlusModelHuDongEvent getHuDongEvent() {
		return huDongEvent;
	}

	public void setHuDongEvent(PlusModelHuDongEvent huDongEvent) {
		this.huDongEvent = huDongEvent;
	}

	public Map<String, String> getFxFlagMap() {
		return fxFlagMap;
	}

	public void setFxFlagMap(Map<String, String> fxFlagMap) {
		this.fxFlagMap = fxFlagMap;
	}

	public String getOrderPageSouce() {
		return orderPageSouce;
	}

	public void setOrderPageSouce(String orderPageSouce) {
		this.orderPageSouce = orderPageSouce;
	}

	
}
