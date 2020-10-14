package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogos;
import com.srnpr.xmassystem.modelproduct.PlusModelProductAuthorityLogoQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/***
 * 加载权威标识信息
 */
public class LoadSellerAuthorityLogo extends LoadTop<PlusModelAuthorityLogos, PlusModelProductAuthorityLogoQuery> {

	public PlusModelAuthorityLogos topInitInfo(PlusModelProductAuthorityLogoQuery query) {
		MDataMap map = new MDataMap();

		map.put("manage_code", query.getCode());

		List<MDataMap> logoList = DbUp.upTable("pc_authority_logo").queryAll(null, "logo_location desc", "manage_code=:manage_code", map);

		List<PlusModelAuthorityLogo> modelList = new ArrayList<PlusModelAuthorityLogo>();
		PlusModelAuthorityLogo item;
		for(MDataMap logoMap : logoList){
			item = new PlusModelAuthorityLogo();
			item.setAllFlag(logoMap.get("all_flag"));
			item.setLogoContent(logoMap.get("logo_content"));
			item.setLogoLocation(NumberUtils.toInt(logoMap.get("logo_location")));
			item.setLogoPic(logoMap.get("logo_pic"));
			item.setManageCode(logoMap.get("manage_code"));
			item.setShowProductSource(logoMap.get("show_product_source"));
			item.setUid(logoMap.get("uid"));
			modelList.add(item);
		}
		
		PlusModelAuthorityLogos result = new PlusModelAuthorityLogos();
		result.setAuthorityLogos(modelList);
		return result;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop(){

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.SellerAuthorityLogo;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelAuthorityLogos.class;
		}
		
		// 5分钟缓存自动过期
		public int getExpireSecond() {
			return 300;
		}
		
	};

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
