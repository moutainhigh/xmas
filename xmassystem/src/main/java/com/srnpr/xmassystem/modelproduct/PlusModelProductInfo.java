package com.srnpr.xmassystem.modelproduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusAbstractModel;
import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 商品信息
 * @author jlin
 *
 */
public class PlusModelProductInfo extends IPlusAbstractModel implements IPlusModel, IPlusModelRefresh {
	
	@ZapcomApi(value = "商品分类字段,该属性是存放分类商品的列表标签proTypeListPic和详情标签proTypeInfoPic")
	private Map<String,String> productTypeMap = new HashMap<String,String>();


	/** 增加这个属性的值可在加载缓存时自动刷新旧版缓存数据 */
	public static final int _VERSION = 9;
	
	public PlusModelProductInfo() {
		super();
	}
	
	@Override
	protected int getCurrentVersion() {
		return _VERSION;
	}

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	
	@ZapcomApi(value = "对接的其他系统商品编号")
	private String productCodeOld = "";

	@ZapcomApi(value = "商品名称")
	private String productName = "";

	@ZapcomApi(value = "商品状态",remark="4497153900060002:已上架，其他状态统一为已下架")
	private String productStatus = "";
	
	@ZapcomApi(value = "商品体积")
	private BigDecimal productVolume = BigDecimal.ZERO;
	
	@ZapcomApi(value = "运费模板")
	private String transportTemplate = "";
	
	@ZapcomApi(value = "限购模板")
	private String areaTemplate = "";

	@ZapcomApi(value = "长宽高")
	private String productVolumeItem = "";
	
	@ZapcomApi(value = "所属系统")
	private String sellerCode = "";	
	
	@ZapcomApi(value = "")
	private String smallSellerCode = "";
	
	@ZapcomApi(value = "商品重量")
	private BigDecimal productWeight = BigDecimal.ZERO;

	@ZapcomApi(value = "成本价")
	private BigDecimal cost_price = BigDecimal.ZERO;
	
	@ZapcomApi(value = "最大价格")
	private BigDecimal maxSellPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value = "最小价格")
	private BigDecimal minSellPrice = BigDecimal.ZERO;

	@ZapcomApi(value = "税率")
	private BigDecimal tax_rate = BigDecimal.ZERO;

	@ZapcomApi(value = "品牌")
	private String brandCode = "";

	@ZapcomApi(value = "品牌名称")
	private String brandName = "";
	
	@ZapcomApi(value = "品牌英文名称")
	private String brandNameEn = "";
	
	@ZapcomApi(value = "分类")
	private List<String> categorys = new ArrayList<String>();
	
	@ZapcomApi(value="市场价",remark="商品市场价")
	private BigDecimal marketPrice = BigDecimal.ZERO;

	@ZapcomApi(value="商品视频链接")
	private String videoUrl = "" ;
	
	@ZapcomApi(value="商品介绍视频")
	private String productDescVideo = "" ;
	
	@ZapcomApi(value="商品视频封面图")
	private String videoMainPic = "" ;
	
	@ZapcomApi(value="商品列表图片",remark="")
	private String mainpicUrl = "";
	
	@ZapcomApi(value="商品广告图片",remark="")
	private String adpicUrl = "";
	
//	@ZapcomApi(value = "商品评论列表")
//	private List<PlusModelProductComment> productComment = new ArrayList<PlusModelProductComment>();
	
	@ZapcomApi(value="轮播图")
    private List<String> pcPicList = new ArrayList<String>();
	
	@ZapcomApi(value="描述图片")
    private PlusModelPcProductdescription description = new PlusModelPcProductdescription();
	
//	@ZapcomApi(value="商品描述")
//    private String discriptInfo = "";
	
	@ZapcomApi(value="sku信息")
    private List<PlusModelProductSkuInfo> skuList = new ArrayList<PlusModelProductSkuInfo>();
	
	@ZapcomApi(value="商品规格",remark="商品的规格")
	private List<PlusModelSkuPropertyInfo> propertyList = new ArrayList<PlusModelSkuPropertyInfo>();
	
	@ZapcomApi(value="商品属性",remark="商品的自定义属性")
	private List<PlusModelPropertyInfo> propertyInfoList = new ArrayList<PlusModelPropertyInfo>();
	
	@ZapcomApi(value="权威标志")
	private List<PlusModelAuthorityLogo> authorityLogo = new ArrayList<PlusModelAuthorityLogo>();
	
	@ZapcomApi(value="常见问题",remark="现在只有跨境通商品有常见问题")
    private List<PlusModelCommonProblem> commonProblem = new ArrayList<PlusModelCommonProblem>();
	
	@ZapcomApi(value = "sku成本价")
	private Map<String,Double> skus = new HashMap<String, Double>();
	
	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;

	@ZapcomApi(value="商品标签",remark="")
    private List<String> labelsList = new ArrayList<String>();
	
    @ZapcomApi(value="是否是虚拟商品 ",remark=" Y：是  N：否")
    private String validateFlag="";
	
    @ZapcomApi(value="是否生鲜",remark="0:否，1:是")
    private String flagTheFresh = "0" ;
    
    @ZapcomApi(value="商品广告语",remark="卖点可被搜索，优先级低于商品名称、品牌、标签(3.9.2新增字段)")
    private String productAdv = "" ;
    
    @ZapcomApi(value="抄底价商品",remark=" 449747110001:否，449747110002:是")
    private String lowGood="";
    
    @ZapcomApi(value="提货券商品",remark=" 449747110001:否，449747110002:是")
    private String voucherGood="";
    
    @ZapcomApi(value="是否参与会员日",remark="Y：是  N：否")
    private String vipdayFlag = "";
    
    @ZapcomApi(value="是否赋予积分",remark="Y：是  N：否")
    private String accmYn = "";
    
    @ZapcomApi(value="结算类型",remark="")
    private String settlementType = "";
    
    @ZapcomApi(value="是否一件代发:Y/N",remark="Y：是  N：否")
    private String vlOrs = "";
    
    @ZapcomApi(value="是否厂商收款:Y/N",remark="Y：是  N：否")
    private String dlrCharge = "";
    
    @ZapcomApi(value="是否厂商配送:Y/N",remark="Y：是  N：否")
    private String cspsFlag = "";
    
    @ZapcomApi(value="虚拟销量")
    private int fictitiousSales = 0;
    
	public String getProductDescVideo() {
		return productDescVideo;
	}

	public void setProductDescVideo(String productDescVideo) {
		this.productDescVideo = productDescVideo;
	}
    
	public String getCspsFlag() {
		return cspsFlag;
	}

	public void setCspsFlag(String cspsFlag) {
		this.cspsFlag = cspsFlag;
	}

	public int getFictitiousSales() {
		return fictitiousSales;
	}

	public void setFictitiousSales(int fictitiousSales) {
		this.fictitiousSales = fictitiousSales;

	}

	public String getDlrCharge() {
		return dlrCharge;
	}

	public void setDlrCharge(String dlrCharge) {
		this.dlrCharge = dlrCharge;
	}

	public String getVoucherGood() {
		return voucherGood;
	}

	public void setVoucherGood(String voucherGood) {
		this.voucherGood = voucherGood;
	}
	
	public Map<String, String> getProductTypeMap() {
		return productTypeMap;
	}

	public void setProductTypeMap(Map<String, String> productTypeMap) {
		this.productTypeMap = productTypeMap;
	}
	/**
	 * @return the flagTheSea
	 */
	public String getFlagTheSea() {
		return flagTheSea;
	}

	/**
	 * @param flagTheSea the flagTheSea to set
	 */
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getTransportTemplate() {
		return transportTemplate;
	}

	public void setTransportTemplate(String transportTemplate) {
		this.transportTemplate = transportTemplate;
	}

	public String getProductVolumeItem() {
		return productVolumeItem;
	}

	public void setProductVolumeItem(String productVolumeItem) {
		this.productVolumeItem = productVolumeItem;
	}

	public String getAdpicUrl() {
		return adpicUrl;
	}

	public void setAdpicUrl(String adpicUrl) {
		this.adpicUrl = adpicUrl;
	}

	public BigDecimal getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(BigDecimal productWeight) {
		this.productWeight = productWeight;
	}

	public BigDecimal getProductVolume() {
		return productVolume;
	}

	public void setProductVolume(BigDecimal productVolume) {
		this.productVolume = productVolume;
	}

	public BigDecimal getCost_price() {
		return cost_price;
	}

	public void setCost_price(BigDecimal cost_price) {
		this.cost_price = cost_price;
	}

	public BigDecimal getTax_rate() {
		return tax_rate;
	}

	public void setTax_rate(BigDecimal tax_rate) {
		this.tax_rate = tax_rate;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public List<String> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
	}

	public Map<String, Double> getSkus() {
		return skus;
	}

	public void setSkus(Map<String, Double> skus) {
		this.skus = skus;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public List<String> getPcPicList() {
		return pcPicList;
	}

	public void setPcPicList(List<String> pcPicList) {
		this.pcPicList = pcPicList;
	}

	public PlusModelPcProductdescription getDescription() {
		return description;
	}

	public void setDescription(PlusModelPcProductdescription description) {
		this.description = description;
	}

	public List<PlusModelProductSkuInfo> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<PlusModelProductSkuInfo> skuList) {
		this.skuList = skuList;
	}

	public List<PlusModelSkuPropertyInfo> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<PlusModelSkuPropertyInfo> propertyList) {
		this.propertyList = propertyList;
	}

	public List<PlusModelPropertyInfo> getPropertyInfoList() {
		return propertyInfoList;
	}

	public void setPropertyInfoList(List<PlusModelPropertyInfo> propertyInfoList) {
		this.propertyInfoList = propertyInfoList;
	}

	public List<PlusModelAuthorityLogo> getAuthorityLogo() {
		return authorityLogo;
	}

	public void setAuthorityLogo(List<PlusModelAuthorityLogo> authorityLogo) {
		this.authorityLogo = authorityLogo;
	}

	public List<PlusModelCommonProblem> getCommonProblem() {
		return commonProblem;
	}

	public void setCommonProblem(List<PlusModelCommonProblem> commonProblem) {
		this.commonProblem = commonProblem;
	}

	public String getProductCodeOld() {
		return productCodeOld;
	}

	public void setProductCodeOld(String productCodeOld) {
		this.productCodeOld = productCodeOld;
	}

	public String getAreaTemplate() {
		return areaTemplate;
	}

	public void setAreaTemplate(String areaTemplate) {
		this.areaTemplate = areaTemplate;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public String getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(String validateFlag) {
		this.validateFlag = validateFlag;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public BigDecimal getMaxSellPrice() {
		return maxSellPrice;
	}

	public void setMaxSellPrice(BigDecimal maxSellPrice) {
		this.maxSellPrice = maxSellPrice;
	}

	public BigDecimal getMinSellPrice() {
		return minSellPrice;
	}

	public void setMinSellPrice(BigDecimal minSellPrice) {
		this.minSellPrice = minSellPrice;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public String getFlagTheFresh() {
		return flagTheFresh;
	}

	public void setFlagTheFresh(String flagTheFresh) {
		this.flagTheFresh = flagTheFresh;
	}

	public String getProductAdv() {
		return productAdv;
	}

	public void setProductAdv(String productAdv) {
		this.productAdv = productAdv;
	}

	public String getLowGood() {
		return lowGood;
	}

	public void setLowGood(String lowGood) {
		this.lowGood = lowGood;
	}

	public String getBrandNameEn() {
		return brandNameEn;
	}

	public void setBrandNameEn(String brandNameEn) {
		this.brandNameEn = brandNameEn;
	}

	public String getVipdayFlag() {
		return vipdayFlag;
	}

	public void setVipdayFlag(String vipdayFlag) {
		this.vipdayFlag = vipdayFlag;
	}

	public String getAccmYn() {
		return accmYn;
	}

	public void setAccmYn(String accmYn) {
		this.accmYn = accmYn;
	}

	public String getSettlementType() {
		return settlementType;
	}

	public void setSettlementType(String settlementType) {
		this.settlementType = settlementType;
	}

	public String getVlOrs() {
		return vlOrs;
	}

	public void setVlOrs(String vlOrs) {
		this.vlOrs = vlOrs;
	}

	public String getVideoMainPic() {
		return videoMainPic;
	}

	public void setVideoMainPic(String videoMainPic) {
		this.videoMainPic = videoMainPic;
	}

	public static int getVersion() {
		return _VERSION;
	}

}
