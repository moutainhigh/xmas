package com.srnpr.xmasorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车查询的商品信息对象
*    xiegj
*/
public class ShoppingCartGoodsInfo  {
	
	@ZapcomApi(value = "订单编号", remark = "订单编号",require = 1, demo = "")
	private String order_code = "";
	
	@ZapcomApi(value = "商品编号", remark = "商品编号",require = 1, demo = "8016123456")
	private String product_code = "";
	
	@ZapcomApi(value = "sku编号", remark = "sku编号",require = 1, demo = "8019123456")
	private String sku_code = "";
	
	@ZapcomApi(value = "商品图片链接", remark = "商品图片链接", demo = "http:~~~")
	private String pic_url = "";
	
	@ZapcomApi(value = "商品名称", remark = "商品名称",require = 1, demo = "花露水")
	private String sku_name = "";
	
	@ZapcomApi(value = "商品属性", remark = "商品规格,商品款式",require = 1, demo = "商品规格,商品款式")
	private List<ShoppingCartPropertyinfo> sku_property = new ArrayList<ShoppingCartPropertyinfo>();
	
	@ZapcomApi(value = "商品价格", remark = "商品价格",require = 1, demo = "")
	private Double sku_price = 0.00;
	
	@ZapcomApi(value = "商品数量", remark = "商品数量",require = 1, demo = "123456")
	private int sku_num = 0;
	
	@ZapcomApi(value = "每单限购数量", remark = "每单限购数量",require = 1, demo = "123456")
	private int limit_order_num = 99;
	
	@ZapcomApi(value = "库存", remark = "库存",require = 1, demo = "123456")
	private int sku_stock = 0;

	@ZapcomApi(value="商品活动相关显示语",remark="闪购，内购，特价")
	private List<ShoppingCartActivity> activitys = new ArrayList<ShoppingCartActivity>();
	
	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private List<String> otherShow = new ArrayList<String>();
	
	@ZapcomApi(value = "库存是否足够", remark = "1:足够，0:不足",require = 1, demo = "1")
	private String flag_stock = "1";
	
	@ZapcomApi(value = "是否有效商品", remark = "1:有效商品，0:无效商品",require = 1, demo = "1")
	private String flag_product = "1";
	
	@ZapcomApi(value = "最小起订数量", remark = "最小起订数量，最小为1个",require = 1, demo = "1")
	private int mini_order = 1;
	
	@ZapcomApi(value = "是否选择", remark = "是否选择：1：是，0：否", demo = "1")
	private String chooseFlag = "0";
	
	@ZapcomApi(value = "是否海外购商品", remark = "1：是，0：否", demo = "1")
	private String flagTheSea = "0";
	
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="降价提示文字")
	private String sub_price_title = "";
	
	@ZapcomApi(value="是否原价购买")
    private String isSkuPriceToBuy = "0";
	
	@ZapcomApi(value="是否分销商品标识 0否 1是")
    private int fxFlag = 0;
	
	@ZapcomApi(value="分销人编号", remark="")
	private String fxrcode = "";
	
	@ZapcomApi(value="推广赚推广人编号",remark = "")
	private String tgzUserCode = "";
	
	@ZapcomApi(value="推广赚买家秀编号",remark = "")
	private String tgzShowCode = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag;
	@ZapcomApi(value = "商品活动标签",remark="秒杀、闪购、拼团、特价、会员日、满减、领券、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value = "带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();
	
	@ZapcomApi(value="用户是否收藏该商品",remark="Y/N")
    private String isCollect = "N";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	
	@ZapcomApi(value="是否厂商收款商品",remark="Y/N")
    private String dlrCharge = "";
	
	public String getDlrCharge() {
		return dlrCharge;
	}
	public void setDlrCharge(String dlrCharge) {
		this.dlrCharge = dlrCharge;
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
	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}
	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}
	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}


	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public List<ShoppingCartPropertyinfo> getSku_property() {
		return sku_property;
	}

	public void setSku_property(List<ShoppingCartPropertyinfo> sku_property) {
		this.sku_property = sku_property;
	}

	public Double getSku_price() {
		return sku_price;
	}

	public void setSku_price(Double sku_price) {
		this.sku_price = sku_price;
	}

	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

	public int getLimit_order_num() {
		return limit_order_num;
	}

	public void setLimit_order_num(int limit_order_num) {
		this.limit_order_num = limit_order_num;
	}

	public int getSku_stock() {
		return sku_stock;
	}

	public void setSku_stock(int sku_stock) {
		this.sku_stock = sku_stock;
	}

	public List<ShoppingCartActivity> getActivitys() {
		return activitys;
	}

	public void setActivitys(List<ShoppingCartActivity> activitys) {
		this.activitys = activitys;
	}

	public List<String> getOtherShow() {
		return otherShow;
	}

	public void setOtherShow(List<String> otherShow) {
		this.otherShow = otherShow;
	}

	public String getFlag_stock() {
		return flag_stock;
	}

	public void setFlag_stock(String flag_stock) {
		this.flag_stock = flag_stock;
	}

	public String getFlag_product() {
		return flag_product;
	}

	public void setFlag_product(String flag_product) {
		this.flag_product = flag_product;
	}

	public int getMini_order() {
		return mini_order;
	}

	public void setMini_order(int mini_order) {
		this.mini_order = mini_order;
	}

	public String getChooseFlag() {
		return chooseFlag;
	}

	public void setChooseFlag(String chooseFlag) {
		this.chooseFlag = chooseFlag;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public String getLabelsPic() {
		return labelsPic;
	}

	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}
	
	public int getFxFlag() {
		return fxFlag;
	}
	public void setFxFlag(int fxFlag) {
		this.fxFlag = fxFlag;
	}
	public String getFxrcode() {
		return fxrcode;
	}
	
	public void setFxrcode(String fxrcode) {
		this.fxrcode = fxrcode;
	}
	
	public String getSub_price_title() {
		return sub_price_title;
	}

	public void setSub_price_title(String sub_price_title) {
		this.sub_price_title = sub_price_title;
	}

	public String getIsSkuPriceToBuy() {
		return isSkuPriceToBuy;
	}

	public void setIsSkuPriceToBuy(String isSkuPriceToBuy) {
		this.isSkuPriceToBuy = isSkuPriceToBuy;
	}
	
}

