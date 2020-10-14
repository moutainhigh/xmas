package com.srnpr.xmassystem.load;


import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelErweiCodeBinfen;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缤纷扫码购明细缓存
 */
public class LoadErweiCodeBinfen extends LoadTop<PlusModelErweiCodeBinfen, PlusModelQuery> {

	@Override
	public PlusModelErweiCodeBinfen topInitInfo(PlusModelQuery tQuery) {
		String code = tQuery.getCode();
		PlusModelErweiCodeBinfen plusModel = new PlusModelErweiCodeBinfen();
		if(StringUtils.isNotEmpty(code)) {
			MDataMap map = DbUp.upTable("sc_erwei_code_binfen").one("qrcode", code);
			if(map != null) {
				plusModel.setProductCode(map.get("product_code"));
				plusModel.setMclassId(map.get("mclass_id"));
			}
		}
		
		return plusModel;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.ErweiCodeBinfen;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelErweiCodeBinfen.class;
		}
		
	};


}
