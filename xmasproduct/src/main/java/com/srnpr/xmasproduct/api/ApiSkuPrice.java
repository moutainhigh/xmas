package com.srnpr.xmasproduct.api;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmasproduct.model.SkuPrice;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

/******
 * 根据用户传递的ProductCode字符串  返回productCode 对应最低的字符串价格
 * @author zhouguohui
 *
 */
public class ApiSkuPrice extends RootApiForMember<SkuPrice, PlusModelSkuQuery>{

	public SkuPrice Process(PlusModelSkuQuery inputParam, MDataMap mRequestMap) {
		SkuPrice skuPrice = new SkuPrice();
		String pro = inputParam.getCode();
		
		/*
		 * 兼容ios没有传用户编号，因此取用户手机号进行转换
		 */
		try {
			String clientInfo = mRequestMap.get("api_client");
			JSONObject parseClientInfo = JSONObject.parseObject(clientInfo);
			if(null != parseClientInfo && parseClientInfo.containsKey("useID")) {
				String user_mobile = String.valueOf(parseClientInfo.get("useID"));
				MDataMap userInfo = DbUp.upTable("mc_login_info").one("login_name",user_mobile,"manage_code","SI2003");
				if(null != userInfo) {
					inputParam.setMemberCode(userInfo.get("member_code"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(StringUtils.isBlank(inputParam.getMemberCode())) {
			String usermobile = inputParam.getMobile();
			MDataMap one = DbUp.upTable("mc_login_info").one("login_name",usermobile,"manage_code","SI2003");
			if(null != one) {
				inputParam.setMemberCode(one.get("member_code"));
			}
		}
		
		Map<String,BigDecimal> map = new HashMap<String,BigDecimal>();
		
		if(StringUtils.isNotBlank(pro)){
			PlusSupportProduct psp = new PlusSupportProduct();
					
			String[] proCode = pro.split(",");
			
			for(int j=0;j<proCode.length;j++){
				PlusModelProductInfo productInfo = new LoadProductInfo().topInitInfo(new PlusModelProductQuery(proCode[j]));
				List<PlusModelProductSkuInfo> list = productInfo.getSkuList();
				List<BigDecimal> listPrice = new ArrayList<BigDecimal>();
				for(PlusModelProductSkuInfo model : list ){
					PlusModelSkuInfo skuInfo = psp.upSkuInfoBySkuCode(model.getSkuCode(),inputParam.getMemberCode(),inputParam.getMemberCode(),inputParam.getIsPurchase());
					if(skuInfo!=null){
						listPrice.add(skuInfo.getSellPrice());
					}
					
				}
				
				Collections.sort(listPrice); 
				//如果listPrice 几个为空  设定内购的价格为123456789   这是神坑的逻辑业务    
//				map.put(proCode[j], listPrice.size()<=0?new BigDecimal(123456789):listPrice.get(0).setScale(2, RoundingMode.HALF_UP));
				

				//去掉价格为123456789 的展示 ，展示商品市场价marketPrice  fq++
				map.put(proCode[j], listPrice.size()<=0? productInfo.getMinSellPrice().setScale(2, RoundingMode.HALF_UP) :listPrice.get(0).setScale(2, RoundingMode.HALF_UP));
				
				
			}
			
		}
		skuPrice.setMap(map);
		
		return skuPrice;
	}

}
