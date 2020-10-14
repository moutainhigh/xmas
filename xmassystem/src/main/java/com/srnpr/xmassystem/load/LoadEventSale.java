package com.srnpr.xmassystem.load;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFullMoney;
import com.srnpr.xmassystem.modelevent.PlusModelFullRule;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigSale;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 加载一般满减活动
 * @author unkown
 *
 */
public class LoadEventSale extends LoadTopMain<PlusModelEventSale, PlusModelSaleQuery> {

	
	public PlusModelEventSale topInitInfo(PlusModelSaleQuery tQuery) {
		PlusModelEventSale plusModelEventSale = new PlusModelEventSale();
		if(tQuery==null){
			return plusModelEventSale;
		}
		
		addEventSale(plusModelEventSale,tQuery.getCode());
		return plusModelEventSale;
	}

	
	private final static PlusConfigSale PLUS_CONFIG = new PlusConfigSale();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	
	/**
	 * 取满减类型设定
	 * @param plusEventSale
	 * @param sellerCode
	 * modified by zhangbo  
	 */
	private void addEventSale(PlusModelEventSale plusEventSale,String sellerCode){
		//添加满减叠加字段
	    String queryFullCut = "select a.is_false_order,a.full_cut_code,a.full_cut_type,a.full_cut_limit,a.full_cut_product_type,a.full_cut_product_code,"+
	    					  "a.full_cut_category_code,a.full_cut_category_type,a.full_cut_brand_code,a.full_cut_brand_type,a.full_cut_max_price,a.full_cut_cal_type,s.*"+
	    					  "from sc_full_cut a right join (SELECT * FROM sc_event_info  where "+
	    					  " seller_code=:sellerCode and  (event_status='4497472700020002' or event_status='4497472700020004') and event_type_code='4497472600010008' "+ 
	    					  " and end_time>now() order by begin_time asc , zid desc)s on  a.event_code=s.event_code";
		List<Map<String, Object>> listFullCut = DbUp.upTable("sc_full_cut").dataSqlList(queryFullCut, new MDataMap("sellerCode",sellerCode));
		
		List<PlusModelEventFull> eventFulls=new ArrayList<PlusModelEventFull>();
		if(listFullCut!=null && listFullCut.size()>0){
			for(Map<String, Object> map : listFullCut){
				PlusModelEventFull plusModelEventFull =new PlusModelEventFull();
				plusModelEventFull.setFullMoneys(getFullMoney(map.get("full_cut_code")==null?"":map.get("full_cut_code").toString()));
				plusModelEventFull.setFirstOrder(map.get("is_false_order")==null?"":map.get("is_false_order").toString());
				plusModelEventFull.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				plusModelEventFull.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				plusModelEventFull.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				plusModelEventFull.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				plusModelEventFull.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				plusModelEventFull.setActivityUrl(map.get("activity_url")==null?"":map.get("activity_url").toString());
				plusModelEventFull.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				plusModelEventFull.setFullCode(map.get("full_cut_code")==null?"":map.get("full_cut_code").toString());
				plusModelEventFull.setFullType(map.get("full_cut_type")==null?"":map.get("full_cut_type").toString());
				plusModelEventFull.setActivityUrl(map.get("activity_url")==null?"":map.get("activity_url").toString());
				
				plusModelEventFull.setSuprapositionType(map.get("supraposition_type")==null?"":map.get("supraposition_type").toString());
				
				plusModelEventFull.setFullCutMaxPrice(map.get("full_cut_max_price")==null?BigDecimal.ZERO: MoneyHelper.roundHalfUp(  new BigDecimal(map.get("full_cut_max_price").toString())  ));
				plusModelEventFull.setFullCutCalType(map.get("full_cut_cal_type")==null?"":map.get("full_cut_cal_type").toString());
				
				plusModelEventFull.setChannels(map.get("channels")==null?"":map.get("channels").toString());
				
				// 无限制时设置渠道列表为空
				if("4497471600070001".equals(map.get("channel_limit"))) {
					plusModelEventFull.setChannels("");
				}
				
				
				String fullType = map.get("full_cut_type")==null?"":map.get("full_cut_limit").toString();
				if(fullType!=null && !fullType.equals("") && fullType.equals("449747640001")){
					plusModelEventFull.setRuleSku(null);
					plusModelEventFull.setRuleBrand(null);
					plusModelEventFull.setRuleCategory(null);
				}else if(fullType!=null && !fullType.equals("") && fullType.equals("449747640002")){
					String categoryType = map.get("full_cut_category_type")==null?"":map.get("full_cut_category_type").toString();
					String brandType =  map.get("full_cut_brand_type")==null?"":map.get("full_cut_brand_type").toString();
					String skuType =  map.get("full_cut_product_type")==null?"":map.get("full_cut_product_type").toString();
					PlusModelFullRule ruleCategory = new PlusModelFullRule();
					PlusModelFullRule ruleBrand = new PlusModelFullRule();
					PlusModelFullRule ruleSku = new PlusModelFullRule();
					ruleCategory.setLimitType(categoryType);
					ruleBrand.setLimitType(brandType);
					ruleSku.setLimitType(skuType);
					/*分类限制设定*/
					if(categoryType!=null && !categoryType.equals("") && !categoryType.equals("4497476400020001")){
						String[] categoryCode = (map.get("full_cut_category_code")==null?"":map.get("full_cut_category_code").toString()).split(",");
						List<String> limitCode = new ArrayList<String>();
						for(int i=0;i<categoryCode.length;i++){
							limitCode.add(categoryCode[i]);
						}
						ruleCategory.setLimitCode(limitCode);
					}else{
						ruleCategory.setLimitCode(null);
					}
					
					/*品牌限制设定*/
					if(brandType!=null && !brandType.equals("") && !brandType.equals("4497476400020001")){
						String[] brandCode = (map.get("full_cut_brand_code")==null?"":map.get("full_cut_brand_code").toString()).split(",");
						List<String> limitCode = new ArrayList<String>();
						for(int i=0;i<brandCode.length;i++){
							limitCode.add(brandCode[i]);
						}
						ruleBrand.setLimitCode(limitCode);
					}else{
						ruleBrand.setLimitCode(null);
					}
					
					/*sku设定限制*/
					if(skuType!=null && !skuType.equals("") && !skuType.equals("4497476400020001")){
						String[] productCode = (map.get("full_cut_product_code")==null?"":map.get("full_cut_product_code").toString()).split(",");
						List<String> limitCode = new ArrayList<String>();
						for(int i=0;i<productCode.length;i++){
							limitCode.add(productCode[i]);
						}
						ruleSku.setLimitCode(limitCode);
					}else{
						ruleSku.setLimitCode(null);
					}
					plusModelEventFull.setRuleCategory(ruleCategory);
					plusModelEventFull.setRuleBrand(ruleBrand);
					plusModelEventFull.setRuleSku(ruleSku);
					
				}
				
				eventFulls.add(plusModelEventFull);
			}
			plusEventSale.setEventFulls(eventFulls);
		}
		
		
	}
	
	
	/**
	 * 取价格对象
	 * @param fullCutCode
	 * @return
	 */
	public List<PlusModelFullMoney> getFullMoney(String fullCutCode){
		List<PlusModelFullMoney> listFullMoney = new ArrayList<PlusModelFullMoney>();
		if(fullCutCode.isEmpty()){
			return listFullMoney;
		}
		String queryFullMoney = "select * from sc_full_cut_price  where full_cut_code=:fullCutCode ";
	   List<Map<String, Object>> fullMoney = DbUp.upTable("sc_full_cut_price").dataSqlList(queryFullMoney, new MDataMap("fullCutCode",fullCutCode));
		
	   if(fullMoney!=null && fullMoney.size()>0){
		   for(Map<String, Object> map : fullMoney){
				PlusModelFullMoney fullMoneyEntity = new PlusModelFullMoney();
				fullMoneyEntity.setFullMoney(new BigDecimal(map.get("full_price")==null?"":map.get("full_price").toString()));
				fullMoneyEntity.setCutMoney(new BigDecimal(map.get("cut_price")==null?"":map.get("cut_price").toString()));
				fullMoneyEntity.setAdvTitle(map.get("description")==null?"":map.get("description").toString());
				fullMoneyEntity.setFullCutType(StringUtils.trimToEmpty((String)(map.get("full_cut_type"))));
				fullMoneyEntity.setFullCutCode(map.get("full_cut_code")==null?"":map.get("full_cut_code").toString());
				listFullMoney.add(fullMoneyEntity);
			}
	   }
		return listFullMoney;
	}


	@Override
	public PlusModelEventSale topInitInfoMain(PlusModelSaleQuery tQuery) {
		PlusModelEventSale plusModelEventSale = new PlusModelEventSale();
		
		String sellerCode = tQuery.getCode();
		
		 String queryFullCut = "select a.is_false_order,a.full_cut_code,a.full_cut_type,a.full_cut_limit,a.full_cut_product_type,a.full_cut_product_code,"+
							  "a.full_cut_category_code,a.full_cut_category_type,a.full_cut_brand_code,a.full_cut_brand_type,a.full_cut_max_price,a.full_cut_cal_type,s.*"+
							  "from sc_full_cut a right join (SELECT * FROM sc_event_info  where "+
							  " seller_code=:sellerCode and  (event_status='4497472700020002' or event_status='4497472700020004') and event_type_code='4497472600010008' "+ 
							  " and end_time>now() order by begin_time asc , zid desc)s on  a.event_code=s.event_code";
		List<Map<String, Object>> listFullCut = DbUp.upTable("sc_full_cut").dataSqlPriLibList(queryFullCut, new MDataMap("sellerCode",sellerCode));
		
		List<PlusModelEventFull> eventFulls=new ArrayList<PlusModelEventFull>();
		if(listFullCut!=null && listFullCut.size()>0){
		for(Map<String, Object> map : listFullCut){
			PlusModelEventFull plusModelEventFull =new PlusModelEventFull();
			plusModelEventFull.setFullMoneys(getFullMoney(map.get("full_cut_code")==null?"":map.get("full_cut_code").toString()));
			plusModelEventFull.setFirstOrder(map.get("is_false_order")==null?"":map.get("is_false_order").toString());
			plusModelEventFull.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
			plusModelEventFull.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
			plusModelEventFull.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
			plusModelEventFull.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
			plusModelEventFull.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
			plusModelEventFull.setActivityUrl(map.get("activity_url")==null?"":map.get("activity_url").toString());
			plusModelEventFull.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
			plusModelEventFull.setFullCode(map.get("full_cut_code")==null?"":map.get("full_cut_code").toString());
			plusModelEventFull.setFullType(map.get("full_cut_type")==null?"":map.get("full_cut_type").toString());
			plusModelEventFull.setActivityUrl(map.get("activity_url")==null?"":map.get("activity_url").toString());
			
			plusModelEventFull.setFullCutMaxPrice(map.get("full_cut_max_price")==null?BigDecimal.ZERO: MoneyHelper.roundHalfUp(  new BigDecimal(map.get("full_cut_max_price").toString())  ));
			plusModelEventFull.setFullCutCalType(map.get("full_cut_cal_type")==null?"":map.get("full_cut_cal_type").toString());
			
			plusModelEventFull.setSuprapositionType(map.get("supraposition_type")==null?"":map.get("supraposition_type").toString());
			
			String fullType = map.get("full_cut_type")==null?"":map.get("full_cut_limit").toString();
			if(fullType!=null && !fullType.equals("") && fullType.equals("449747640001")){
				plusModelEventFull.setRuleSku(null);
				plusModelEventFull.setRuleBrand(null);
				plusModelEventFull.setRuleCategory(null);
			}else if(fullType!=null && !fullType.equals("") && fullType.equals("449747640002")){
				String categoryType = map.get("full_cut_category_type")==null?"":map.get("full_cut_category_type").toString();
				String brandType =  map.get("full_cut_brand_type")==null?"":map.get("full_cut_brand_type").toString();
				String skuType =  map.get("full_cut_product_type")==null?"":map.get("full_cut_product_type").toString();
				PlusModelFullRule ruleCategory = new PlusModelFullRule();
				PlusModelFullRule ruleBrand = new PlusModelFullRule();
				PlusModelFullRule ruleSku = new PlusModelFullRule();
				ruleCategory.setLimitType(categoryType);
				ruleBrand.setLimitType(brandType);
				ruleSku.setLimitType(skuType);
				/*分类限制设定*/
				if(categoryType!=null && !categoryType.equals("") && !categoryType.equals("4497476400020001")){
					String[] categoryCode = (map.get("full_cut_category_code")==null?"":map.get("full_cut_category_code").toString()).split(",");
					List<String> limitCode = new ArrayList<String>();
					for(int i=0;i<categoryCode.length;i++){
						limitCode.add(categoryCode[i]);
					}
					ruleCategory.setLimitCode(limitCode);
				}else{
					ruleCategory.setLimitCode(null);
				}
				
				/*品牌限制设定*/
				if(brandType!=null && !brandType.equals("") && !brandType.equals("4497476400020001")){
					String[] brandCode = (map.get("full_cut_brand_code")==null?"":map.get("full_cut_brand_code").toString()).split(",");
					List<String> limitCode = new ArrayList<String>();
					for(int i=0;i<brandCode.length;i++){
						limitCode.add(brandCode[i]);
					}
					ruleBrand.setLimitCode(limitCode);
				}else{
					ruleBrand.setLimitCode(null);
				}
				
				/*sku设定限制*/
				if(skuType!=null && !skuType.equals("") && !skuType.equals("4497476400020001")){
					String[] productCode = (map.get("full_cut_product_code")==null?"":map.get("full_cut_product_code").toString()).split(",");
					List<String> limitCode = new ArrayList<String>();
					for(int i=0;i<productCode.length;i++){
						limitCode.add(productCode[i]);
					}
					ruleSku.setLimitCode(limitCode);
				}else{
					ruleSku.setLimitCode(null);
				}
				plusModelEventFull.setRuleCategory(ruleCategory);
				plusModelEventFull.setRuleBrand(ruleBrand);
				plusModelEventFull.setRuleSku(ruleSku);
				
			}
			eventFulls.add(plusModelEventFull);
		}
			plusModelEventSale.setEventFulls(eventFulls);
		}
		return plusModelEventSale;
	}

}
