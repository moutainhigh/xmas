package com.srnpr.xmassystem.load;

import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelJDToken;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class LoadJDToken extends LoadTop<PlusModelJDToken, PlusModelQuery> {

	@Override
	public PlusModelJDToken topInitInfo(PlusModelQuery tQuery) {
		MDataMap map = new MDataMap();
		PlusModelJDToken plusModelJDToken = new PlusModelJDToken();
		String sql = "SELECT\r\n" + 
				"	*\r\n" + 
				"FROM\r\n" + 
				"	sc_jingdong_token\r\n" + 
				"ORDER BY\r\n" + 
				"	create_time DESC\r\n" + 
				"LIMIT 0,1";
		Map<String, Object> dataSqlOne = DbUp.upTable("sc_jingdong_token").dataSqlOne(sql, map);
		plusModelJDToken.setAccess_token(dataSqlOne.get("access_token").toString());
		return plusModelJDToken;
	}
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private static ConfigTop PLUS_CONFIG = new ConfigTop() {
		@Override
		public EKvSchema getSchema() {
			return EKvSchema.JingDongToken;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelJDToken.class;
		}
		
		public int getExpireSecond() {
			return 60*60*24;
		}
		
	};

}
