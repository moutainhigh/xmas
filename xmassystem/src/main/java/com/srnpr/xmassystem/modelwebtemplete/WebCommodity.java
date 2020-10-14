package com.srnpr.xmassystem.modelwebtemplete;

import java.math.BigDecimal;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class WebCommodity {

	@ZapcomApi(value = "商品图")
	private String commodity_picture = "";

	@ZapcomApi(value = "位置")
	private String commodity_location = "";

	@ZapcomApi(value = "是否展示折扣")
	private String is_dis = "";

	@ZapcomApi(value = "商品名称")
	private String commodity_name = "";

	@ZapcomApi(value = "商品描述")
	private String commodity_describe = "";

	@ZapcomApi(value = "sku编号")
	private String commodity_number = "";

	@ZapcomApi(value = "模版编号")
	private String template_number = "";

	@ZapcomApi(value = "打开方式选择")
	private String skip = "";

	@ZapcomApi(value = "打开方式输入")
	private String skip_input = "";

	@ZapcomApi(value = "栏目图")
	private String programa_picture = "";

	@ZapcomApi(value = "开始时间")
	private String start_time = "";

	@ZapcomApi(value = "结束时间")
	private String end_time = "";

	@ZapcomApi(value = "创建时间")
	private String create_time = "";

	@ZapcomApi(value = "优惠码")
	private String coupon = "";

	@ZapcomApi(value = "连接")
	private String url = "";

	@ZapcomApi(value = "宽度")
	private String width = "";

	@ZapcomApi(value = "描述")
	private String template_desc = "";

	@ZapcomApi(value = "图")
	private String img = "";

	@ZapcomApi(value = "城市名称")
	private String city_name = "";

	@ZapcomApi(value = "主播手机号")
	private String live_mobile = "";
	
	@ZapcomApi(value="商品编号")
	private String product_code = "";
	
	@ZapcomApi(value="商品主图")
	private String main_url = "";
	
	@ZapcomApi(value= "商品售价")
	private BigDecimal price = BigDecimal.ZERO;
	
	@ZapcomApi(value="微信商城扫码价",remark="扫码购模板才会赋值")
	private BigDecimal smg_price = BigDecimal.ZERO;
	
	@ZapcomApi(value="app扫码价",remark="扫码购模板才会赋值")
	private BigDecimal app_smg_price = BigDecimal.ZERO;
	
	@ZapcomApi(value="市场价")
	private BigDecimal market_price = BigDecimal.ZERO;
	
	@ZapcomApi(value="商品状态")
	private String product_status = "";
	
	@ZapcomApi(value="销售库存")
	private long sale_num = 0L;
	
	@ZapcomApi(value="标题名称",remark="左右两栏的标题")
	private String title = "";
	
	@ZapcomApi(value="标题颜色")
	private String title_color = "";
	
	@ZapcomApi(value="标题选中之后的颜色")
	private String title_checked_color = "";
	
	@ZapcomApi(value="描述",remark="左右两栏标题下的描述")
	private String describes = "";
	
	@ZapcomApi(value="描述字体颜色")
	private String describe_color = "";
	
	@ZapcomApi(value = "优惠描述")
	private String preferential_desc = "";
	
	@ZapcomApi(value="页面定位模板子模板",remark="多个编号用','号拼接")
	private String sub_template_number = "";
	
	@ZapcomApi(value="关联模板信息")
	private List<WebTemplete> rel_templete = null;
	
	@ZapcomApi(value = "关联活动编号")
	private String event_code = "";

	/*商户编号*/
	private String smallSellerCode = "";
	/*商品成本 判断内购价格时使用*/
	private BigDecimal costPrice = BigDecimal.ZERO;
	/*商品原始价格*/
	private BigDecimal skuPrice = BigDecimal.ZERO;
	
	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}
	public String getCommodity_picture() {
		return commodity_picture;
	}

	public void setCommodity_picture(String commodity_picture) {
		this.commodity_picture = commodity_picture;
	}

	public String getCommodity_location() {
		return commodity_location;
	}

	public void setCommodity_location(String commodity_location) {
		this.commodity_location = commodity_location;
	}

	public String getIs_dis() {
		return is_dis;
	}

	public void setIs_dis(String is_dis) {
		this.is_dis = is_dis;
	}

	public String getCommodity_name() {
		return commodity_name;
	}

	public void setCommodity_name(String commodity_name) {
		this.commodity_name = commodity_name;
	}

	public String getCommodity_describe() {
		return commodity_describe;
	}

	public void setCommodity_describe(String commodity_describe) {
		this.commodity_describe = commodity_describe;
	}

	public String getCommodity_number() {
		return commodity_number;
	}

	public void setCommodity_number(String commodity_number) {
		this.commodity_number = commodity_number;
	}

	public String getTemplate_number() {
		return template_number;
	}

	public void setTemplate_number(String template_number) {
		this.template_number = template_number;
	}

	public String getSkip() {
		return skip;
	}

	public void setSkip(String skip) {
		this.skip = skip;
	}

	public String getSkip_input() {
		return skip_input;
	}

	public void setSkip_input(String skip_input) {
		this.skip_input = skip_input;
	}

	public String getPrograma_picture() {
		return programa_picture;
	}

	public void setPrograma_picture(String programa_picture) {
		this.programa_picture = programa_picture;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getTemplate_desc() {
		return template_desc;
	}

	public void setTemplate_desc(String template_desc) {
		this.template_desc = template_desc;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getLive_mobile() {
		return live_mobile;
	}

	public void setLive_mobile(String live_mobile) {
		this.live_mobile = live_mobile;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getMain_url() {
		return main_url;
	}

	public void setMain_url(String main_url) {
		this.main_url = main_url;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMarket_price() {
		return market_price;
	}

	public void setMarket_price(BigDecimal market_price) {
		this.market_price = market_price;
	}

	public String getProduct_status() {
		return product_status;
	}

	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}

	public long getSale_num() {
		return sale_num;
	}

	public void setSale_num(long sale_num) {
		this.sale_num = sale_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle_color() {
		return title_color;
	}

	public void setTitle_color(String title_color) {
		this.title_color = title_color;
	}

	public String getTitle_checked_color() {
		return title_checked_color;
	}

	public void setTitle_checked_color(String title_checked_color) {
		this.title_checked_color = title_checked_color;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getDescribe_color() {
		return describe_color;
	}

	public void setDescribe_color(String describe_color) {
		this.describe_color = describe_color;
	}

	public String getPreferential_desc() {
		return preferential_desc;
	}

	public void setPreferential_desc(String preferential_desc) {
		this.preferential_desc = preferential_desc;
	}

	public String getSub_template_number() {
		return sub_template_number;
	}

	public void setSub_template_number(String sub_template_number) {
		this.sub_template_number = sub_template_number;
	}

	public List<WebTemplete> getRel_templete() {
		return rel_templete;
	}

	public void setRel_templete(List<WebTemplete> rel_templete) {
		this.rel_templete = rel_templete;
	}

	public BigDecimal getSmg_price() {
		return smg_price;
	}

	public void setSmg_price(BigDecimal smg_price) {
		this.smg_price = smg_price;
	}

	public BigDecimal getApp_smg_price() {
		return app_smg_price;
	}

	public void setApp_smg_price(BigDecimal app_smg_price) {
		this.app_smg_price = app_smg_price;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

}
