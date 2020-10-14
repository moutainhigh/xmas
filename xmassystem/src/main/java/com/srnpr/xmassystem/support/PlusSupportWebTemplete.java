package com.srnpr.xmassystem.support;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuResult;
import com.srnpr.xmassystem.modelwebtemplete.WebCommodity;
import com.srnpr.xmassystem.modelwebtemplete.WebTemplete;
import com.srnpr.xmassystem.modelwebtemplete.WebTempletePage;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;

public class PlusSupportWebTemplete {
	
	PlusSupportStock stockSupport = new PlusSupportStock();

	/**
	 * 获取专题信息（从数据库中获取）
	 * @param pagenum 专题页面编号
	 * @return
	 */
	public WebTempletePage initWebTempletePageInfoFromDb(String pagenum) {
		String[] vs = pagenum.split("-"); 
		if(vs.length == 2) {
			// 兼容带渠道号的情况
			return this.getInfoFromDb(vs[0],vs[1]);
		} else {
			return this.getInfoFromDb(vs[0]);
		}
	}
	
	private WebTempletePage getInfoFromDb(String pagenum) {
//		WebTempletePage page = new WebTempletePage();
//		
//		MDataMap one = DbUp.upTable("fh_data_page").one("dal_status","1001","page_number",pagenum);
//		if(null != one) {
//			
//			page.setApp_is_share(one.get("app_is_share"));
//			page.setCreate_time(one.get("create_time"));
//			page.setDropdown_type(one.get("dropdown_type"));
//			page.setIs_rel_onlive(one.get("is_rel_onlive"));
//			page.setPage_number(pagenum);
//			page.setProject_ad(one.get("project_ad"));
//			page.setShare_content(one.get("share_content"));
//			page.setShare_img(one.get("share_img"));
//			page.setShare_link(one.get("share_link"));
//			page.setShare_title(one.get("share_title"));
//			page.setTitle(one.get("title"));
//			
//			MDataMap mWhereMap = new MDataMap();
//			mWhereMap.put("page_number", pagenum);
//			//查询页面关联的模板编号
//			List<MDataMap> queryAll = DbUp.upTable("fh_page_template").queryAll("template_number", "cast(sort as signed),zid DESC", "dal_status !='1000' AND page_number=:page_number", mWhereMap);
//			
//			if(queryAll.size() <= 0) {
//				return page;
//			}
//			
//			String templateNumbers = "";//记录所有关联的专题模板编号，以便查询模板对应的内容信息
//			for (MDataMap mDataMap : queryAll) {
//				
//				templateNumbers += "'" + mDataMap.get("template_number") + "',";
//			}
//			
//			templateNumbers = templateNumbers.substring(0, templateNumbers.length()-1);//这时有对应的专题模板，则进行切割最后一位
//			
//			List<WebTemplete> templateContentInfo = getTemplateContentInfo(templateNumbers,pagenum ,true);
//			page.getTempleteList().addAll(templateContentInfo);
//			
//		}
//		
//		return page;
		
		return getInfoFromDb(pagenum, "449747430001");
	}
	
	private WebTempletePage getInfoFromDb(String pagenum, String channelId) {
		WebTempletePage page = new WebTempletePage();
		
		MDataMap one = DbUp.upTable("fh_data_page").one("dal_status","1001","page_number",pagenum);
		if(null != one) {
			
			page.setApp_is_share(one.get("app_is_share"));
			page.setCreate_time(one.get("create_time"));
			page.setDropdown_type(one.get("dropdown_type"));
			page.setIs_rel_onlive(one.get("is_rel_onlive"));
			page.setPage_number(pagenum);
			page.setProject_ad(one.get("project_ad"));
			page.setShare_content(one.get("share_content"));
			page.setShare_img(one.get("share_img"));
			page.setShare_link(one.get("share_link"));
			page.setShare_title(one.get("share_title"));
			page.setTitle(one.get("title"));
			
			MDataMap mWhereMap = new MDataMap();
			mWhereMap.put("page_number", pagenum);
			//查询页面关联的模板编号
			List<MDataMap> queryAll = DbUp.upTable("fh_page_template").queryAll("template_number", "cast(sort as signed),zid DESC", "dal_status !='1000' AND page_number=:page_number", mWhereMap);
			
			if(queryAll.size() <= 0) {
				return page;
			}
			
			String templateNumbers = "";//记录所有关联的专题模板编号，以便查询模板对应的内容信息
			for (MDataMap mDataMap : queryAll) {
				
				templateNumbers += "'" + mDataMap.get("template_number") + "',";
			}
			
			templateNumbers = templateNumbers.substring(0, templateNumbers.length()-1);//这时有对应的专题模板，则进行切割最后一位
			
			List<WebTemplete> templateContentInfo = getTemplateContentInfo(templateNumbers,pagenum ,true, channelId);
			page.getTempleteList().addAll(templateContentInfo);
			
		}
		
		return page;
	}
	
	/**
	 * 模板编号用“'”号拼接，多个编号之间再用“,” 号拼接
	 * eg：  'PTN151103100002','PTN151103100002','PTN151103100002'
	 * @param templateNum 
	 * @param pagenum 页面编号
	 * @param isAddPageNum 查询条件是否关联页面编号（参数pagenum）
	 * @return
	 */
	private List<WebTemplete> getTemplateContentInfo(String templateNum,String pagenum ,boolean isAddPageNum,String channelId) {
		List<WebTemplete> returnList = new ArrayList<WebTemplete>();
		if(StringUtils.isBlank(templateNum)) {
			return returnList;
		}
		
		/*
		 * 查询页面对应所有的专题模板
		 */
//		MDataMap param = new MDataMap();
		String sSql = "";
		if(isAddPageNum) {
			sSql = "SELECT t.column_num,t.template_title_color_selected,t.template_title_color,t.template_backcolor_selected,t.commodity_buy_picture,t.commodity_min_dis,t.commodity_picture,t.commodity_text_color,t.commodity_text_pic," +
					"t.commodity_text_picture,t.commodity_text_value,t.template_backcolor,t.template_number,t.template_type,t.template_title_name,t.sell_price_color,t.event_code,t.split_bar " +
					" FROM familyhas.fh_page_template p_t JOIN familyhas.fh_data_template t ON p_t.template_number = t.template_number AND p_t.page_number='"+pagenum+"' AND p_t.template_number IN("+templateNum+") AND p_t.dal_status !='1000' AND t.dal_status !='1000' GROUP BY t.template_number ORDER BY cast(p_t.sort as signed),p_t.zid DESC ";
		} else {
			sSql = "SELECT t.column_num,t.template_title_color_selected,t.template_title_color,t.template_backcolor_selected,t.commodity_buy_picture,t.commodity_min_dis,t.commodity_picture,t.commodity_text_color,t.commodity_text_pic," +
					"t.commodity_text_picture,t.commodity_text_value,t.template_backcolor,t.template_number,t.template_type,t.template_title_name,t.sell_price_color,t.event_code,t.split_bar " +
					" FROM familyhas.fh_data_template t WHERE t.template_number IN("+templateNum+")  AND t.dal_status !='1000' GROUP BY t.template_number ORDER BY t.zid DESC ";
		}
//		param.put("template_number", templateNum);
	
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_data_template").dataSqlList(sSql, new MDataMap());
		if(dataSqlList.size() <= 0) {
			return returnList;
		}
		
		Map<String, String> number_type = new HashMap<String, String>();//记录专题模板对应的模板类型（key：模板编号；value：模板类型编号）
		
		String templateNumbers = templateNum;//记录所有关联的专题模板编号，以便查询模板对应的内容信息
		for (Map<String, Object> map : dataSqlList) {
			WebTemplete templete = new WebTemplete();
			
			templete.setCommodity_buy_picture(objToStr(map.get("commodity_buy_picture")));
			templete.setCommodity_min_dis(objToStr(map.get("commodity_min_dis")));
			templete.setCommodity_picture(objToStr(map.get("commodity_picture")));
			templete.setCommodity_text_color(objToStr(map.get("commodity_text_color")));
			templete.setCommodity_text_pic(objToStr(map.get("commodity_text_pic")));
			templete.setCommodity_text_picture(objToStr(map.get("commodity_text_picture")));
			templete.setCommodity_text_value(objToStr(map.get("commodity_text_value")));
			templete.setTemplate_backcolor(objToStr(map.get("template_backcolor")));
			templete.setTemplate_number(objToStr(map.get("template_number")));
			templete.setTemplate_title_name(objToStr(map.get("template_title_name")));
			templete.setTemplate_type(objToStr(map.get("template_type")));
			templete.setSell_price_color(objToStr(map.get("sell_price_color")));
			templete.setTemplate_backcolor_selected(objToStr(map.get("template_backcolor_selected")));
			templete.setTemplate_title_color(objToStr(map.get("template_title_color")));
			templete.setTemplate_title_color_selected(objToStr(map.get("template_title_color_selected")));
			templete.setColumn_num(objToStr(map.get("column_num")));
			templete.setEvent_code(objToStr(map.get("event_code")));
			templete.setSplit_bar(objToStr(map.get("split_bar")));
			
			returnList.add(templete);
			
			number_type.put(templete.getTemplate_number(), templete.getTemplate_type());
		
			
		}
		
		Map<String, List<WebCommodity>> templateNumberMap = new HashMap<String, List<WebCommodity>>();//记录所有关联的专题模板 所对应维护的内容( key:模板编号；value：存放模板关联的内容;)
		//List<Map<String, Object>> dataSqlList2 = DbUp.upTable("fh_data_commodity").dataSqlList(" SELECT * FROM familyhas.fh_data_commodity WHERE dal_status !='1000' AND template_number IN ("+templateNumbers+") ORDER BY cast(commodity_location as signed),zid DESC ", new MDataMap());
		String queryComun = "event_code,template_number,city_name,commodity_describe,commodity_location,commodity_name,commodity_number,commodity_picture,coupon,create_time,end_time,img,is_dis,live_mobile,programa_picture,skip,skip_input,start_time,template_desc,template_number,url,width,title,title_color,title_checked_color,describes,describe_color,preferential_desc,sub_template_number,good_number";
		//根据模板编号查询没有结束，以及没有开始的数据
		List<Map<String, Object>> dataSqlList2 = DbUp.upTable("fh_data_commodity").dataSqlList(" SELECT "+queryComun+" FROM familyhas.fh_data_commodity WHERE dal_status !='1000' AND template_number IN ("+templateNumbers+") AND end_time >= '"+FormatHelper.upDateTime()+"' ORDER BY cast(commodity_location as signed),zid DESC ", new MDataMap());
		
		
		List<WebCommodity> templist = null;//临时用
		PlusModelSkuQuery commQuery = new PlusModelSkuQuery();
		if(StringUtils.isNotBlank(channelId)) {
			commQuery.setChannelId(channelId);
		}
		for (Map<String, Object> map : dataSqlList2) {
			
			WebCommodity commodity = new WebCommodity();
			commodity.setTemplate_number(objToStr(map.get("template_number")));//模板编号
			commodity.setCity_name(objToStr(map.get("city_name")));
			commodity.setCommodity_describe(objToStr(map.get("commodity_describe")));
			commodity.setCommodity_location(objToStr(map.get("commodity_location")));
			commodity.setCommodity_name(objToStr(map.get("commodity_name")));
			commodity.setProduct_code(objToStr(map.get("good_number")));
			commodity.setEvent_code(objToStr(map.get("event_code")));
			String commodity_number = "";
			if( "449747500022".equals( number_type.get(commodity.getTemplate_number()) ) ){//兑换商品模板的sku信息
				commodity_number = objToStr(map.get("commodity_number"));
			}else{
				if(StringUtils.isNotBlank(commodity.getProduct_code())){
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					skuQuery.setCode(commodity.getProduct_code());
					PlusModelSkuInfo plusModelSkuInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery).get(commodity.getProduct_code());
					if(plusModelSkuInfo == null) {
						// 兼容商品不可售时默认取第一个sku
						commodity_number = DbUp.upTable("pc_skuinfo").one("product_code", commodity.getProduct_code()).get("sku_code");
					} else {
						commodity_number = plusModelSkuInfo.getSkuCode();
					}
				}				
			}
			commodity.setCommodity_number(commodity_number);
			//如果关联sku，则加载对应的sku信息
			if(StringUtils.isNotBlank(commodity_number)) {
				//商品信息
				commQuery.setCode(commodity_number);
				PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfo(commQuery).getSkus().get(0);
				PlusModelProductQuery plus = new PlusModelProductQuery(plusModelSkuInfo.getProductCode());
				PlusModelProductInfo pcinfo = new LoadProductInfo().upInfoByCode(plus);
				
//				if(StringUtils.isNotBlank(plusModelSkuInfo.getSkuPicUrl())) {
//					commodity.setMain_url(plusModelSkuInfo.getSkuPicUrl());//商品改为取配置的SKU对应的图片
//				} else {
					commodity.setMain_url(plusModelSkuInfo.getProductPicUrl());//商品主图
//				}
				
				commodity.setProduct_code(plusModelSkuInfo.getProductCode());
				commodity.setCostPrice(pcinfo.getCost_price());
				commodity.setSmallSellerCode(pcinfo.getSmallSellerCode());
				commodity.setSkuPrice(plusModelSkuInfo.getSkuPrice());
				if( "449747500017".equals( number_type.get(commodity.getTemplate_number()) ) ){//如果是扫码购模板，需要读取扫码价,取所有sku中的最低价格
					BigDecimal smg_minPrice = BigDecimal.ZERO;
					BigDecimal app_Smg_minPrice = BigDecimal.ZERO;
					
					List<PlusModelProductSkuInfo>  smgSkuList = pcinfo.getSkuList();
//					List<PlusModelSkuResult> smgSkuPriceList = new ArrayList<PlusModelSkuResult>();
					PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
					PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
					List<PlusModelSkuInfo> smgSkuPriceList = new ArrayList<PlusModelSkuInfo>(); 
					List<PlusModelSkuInfo> appSmgSkuPriceList = new ArrayList<PlusModelSkuInfo>(); 
					
					for (PlusModelProductSkuInfo plusModelProductSkuInfo : smgSkuList) {
						
						String smg_appendCode = "IC_SMG_"+plusModelProductSkuInfo.getSkuCode();
						String appSmg_appendCode = "IC_APPSMG_"+plusModelProductSkuInfo.getSkuCode();
						
						if(StringUtils.isNotBlank(channelId)) {
							skuQuery.setChannelId(channelId);
						}
						
						/*
						 *  扫码购等特殊来源渠道价格
						 */
						//微信商城扫码价
						skuQuery.setCode(smg_appendCode);
						PlusModelSkuResult plusModelSkuResult = plusSupportProduct.upSkuInfo(skuQuery);
						smgSkuPriceList.addAll(plusModelSkuResult.getSkus());
						
						//app扫码价
						skuQuery.setCode(appSmg_appendCode);
						plusModelSkuResult = plusSupportProduct.upSkuInfo(skuQuery);
						appSmgSkuPriceList.addAll(plusModelSkuResult.getSkus());
						
					}
					
					for (int i = 0; i < smgSkuPriceList.size(); i++) {
						PlusModelSkuInfo plusModelSkuInfo2 = smgSkuPriceList.get(i);
						if(i == 0) {
							smg_minPrice = plusModelSkuInfo2.getSellPrice();
						} else if(smg_minPrice.compareTo(plusModelSkuInfo2.getSellPrice()) > 0) {
							smg_minPrice = plusModelSkuInfo2.getSellPrice();
						}
						
					}
					
					for (int i = 0; i < appSmgSkuPriceList.size(); i++) {
						PlusModelSkuInfo plusModelSkuInfo2 = appSmgSkuPriceList.get(i);
						if(i == 0) {
							app_Smg_minPrice = plusModelSkuInfo2.getSellPrice();
						} else if(app_Smg_minPrice.compareTo(plusModelSkuInfo2.getSellPrice()) > 0) {
							app_Smg_minPrice = plusModelSkuInfo2.getSellPrice();
						}
						
					}
					
					commodity.setSmg_price(MoneyHelper.roundHalfUp(smg_minPrice));
					commodity.setApp_smg_price(MoneyHelper.roundHalfUp(app_Smg_minPrice));
					
					
				}
				commodity.setPrice(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSellPrice()));
				commodity.setMarket_price(MoneyHelper.roundHalfUp(plusModelSkuInfo.getSourcePrice()));
				commodity.setProduct_status(pcinfo.getProductStatus());
				
				/*
				 * 计算商品总库存
				 */
//				List<PlusModelProductSkuInfo> skuList = pcinfo.getSkuList();
//				PlusSupportProduct psp = new PlusSupportProduct();
//				long salesNum = 0;
//				for (int i = 0; i < skuList.size(); i++) {
//					PlusModelSkuInfo upSkuInfoBySkuCode = psp.upSkuInfoBySkuCode(skuList.get(i).getSkuCode());
//					salesNum += upSkuInfoBySkuCode.getLimitStock();
//				}
				commodity.setSale_num(stockSupport.upAllStockForProduct(pcinfo.getProductCode()));//商品库存
				
			}
			
			commodity.setCommodity_picture(objToStr(map.get("commodity_picture")));
			commodity.setCoupon(objToStr(map.get("coupon")));
			commodity.setCreate_time(objToStr(map.get("create_time")));
			commodity.setEnd_time(objToStr(map.get("end_time")));
			commodity.setImg(objToStr(map.get("img")));
			commodity.setIs_dis(objToStr(map.get("is_dis")));
			commodity.setLive_mobile(objToStr(map.get("live_mobile")));
			commodity.setPrograma_picture(objToStr(map.get("programa_picture")));
			commodity.setSkip(objToStr(map.get("skip")));
			commodity.setSkip_input(objToStr(map.get("skip_input")));
			commodity.setStart_time(objToStr(map.get("start_time")));
			commodity.setTemplate_desc(objToStr(map.get("template_desc")));
			commodity.setUrl(objToStr(map.get("url")));
			commodity.setWidth(objToStr(map.get("width")));
			commodity.setTitle(objToStr(map.get("title")));
			commodity.setTitle_color(objToStr(map.get("title_color")));
			commodity.setTitle_checked_color(objToStr(map.get("title_checked_color")));
			commodity.setDescribes(objToStr(map.get("describes")));
			commodity.setDescribe_color(objToStr(map.get("describe_color")));
			commodity.setPreferential_desc(objToStr(map.get("preferential_desc")));
			commodity.setEvent_code(objToStr(map.get("event_code")));
			
			/*
			 * 添加关联模板信息
			 */
			
			String sub_template_number = objToStr(map.get("sub_template_number"));
			commodity.setSub_template_number(sub_template_number);
			
			if(StringUtils.isNotBlank(sub_template_number) && ("449747500018".equals(number_type.get(commodity.getTemplate_number()))||"449747500021".equals(number_type.get(commodity.getTemplate_number())))) {
				
				String[] sub_template_numberArr = sub_template_number.split(",");
				List<WebTemplete> rel_templete = new ArrayList<WebTemplete>();
				//由于要根据顺序展示，因此只能循环一个一个查
				for (String tmp_tnumber : sub_template_numberArr) {
					//如果是页面定位模板，则关联模板信息
					List<WebTemplete> templateContentInfo = this.getTemplateContentInfo("'"+tmp_tnumber+"'",pagenum , false, channelId);//递归调用自己方法
					if(templateContentInfo.size() > 0) {//此list只会返回一条记录(因为按单个模板编号查询)
						rel_templete.add(templateContentInfo.get(0));
					}
				}
				commodity.setRel_templete(rel_templete);
			}
			
			if(templateNumberMap.containsKey(objToStr(map.get("template_number")))) {
				
				templist = templateNumberMap.get(objToStr(map.get("template_number")));
				templist.add(commodity);
				
			} else {
				List<WebCommodity> list = new ArrayList<WebCommodity>();
				list.add(commodity);
				templateNumberMap.put(objToStr(map.get("template_number")), list);
			}
		}
		
		//模板页面数据添加查询出的维护内容
		for (WebTemplete templete : returnList) {
			if( null != templateNumberMap.get(templete.getTemplate_number())) {
				templete.getCommodList().addAll(templateNumberMap.get(templete.getTemplate_number()));
			}
		}
		
		return returnList;
			
			
	}
	
	private String objToStr (Object obj) {
		String reverStr = "";
		try {
			if(null != obj) {
				reverStr = String.valueOf(obj);
			}
		} catch (Exception e) {
		}
		
		return reverStr;
		
	}
	
	/**
	 * 更新专题的更新时间
	 * @param pagenum
	 */
	public void onDataPageChanged(String page_number) {
		if(StringUtils.isNotBlank(page_number)) {
			String sql = "update fh_data_page set update_time = now() where page_number = :page_number";
			DbUp.upTable("fh_data_page").dataExec(sql, new MDataMap("page_number", page_number));
		}
	}
	
	/**
	 * 模版内容变更时也更新专题的更新时间
	 * @param pagenum
	 */
	public void onDataTemplateChanged(String template_number) {
		if(StringUtils.isNotBlank(template_number)) {
			return;
		}
		Set<String> pageNumberList = new HashSet<String>();
		
		List<String> templateNumberList = new ArrayList<String>();
		templateNumberList.add(template_number);
		
		// 查询模版关联的上级定位模版
		List<MDataMap> list = DbUp.upTable("fh_data_commodity").queryAll("template_number", "", "sub_template_number like :template_number", new MDataMap("template_number", "%"+template_number+"%"));
		for(MDataMap m : list) {
			templateNumberList.add(m.get("template_number"));
		}
		
		// 查询模版关联的页面
		String tnWhere = "'"+StringUtils.join(templateNumberList,"','")+"'";
		list = DbUp.upTable("fh_page_template").queryAll("page_number", "", "dal_status = '1001' AND template_number IN("+tnWhere+")", new MDataMap());
		for(MDataMap m : list) {
			pageNumberList.add(m.get("page_number"));
		}
		
		for(String pnm : pageNumberList) {
			onDataPageChanged(pnm);
		}
	}
	
}
