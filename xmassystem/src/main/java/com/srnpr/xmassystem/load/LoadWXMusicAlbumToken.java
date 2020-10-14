package com.srnpr.xmassystem.load;

import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelwxMusicAlbumToken;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;

import com.srnpr.zapdata.dbdo.DbUp;


public class LoadWXMusicAlbumToken extends LoadTop<PlusModelwxMusicAlbumToken, PlusModelQuery> {

	@Override
	public PlusModelwxMusicAlbumToken topInitInfo(PlusModelQuery tQuery) {
		PlusModelwxMusicAlbumToken pModel = new PlusModelwxMusicAlbumToken();
		String sql = "select * from sc_token where code='wxMA' and expires_time>now() order by zid desc limit 0,1";
		List<Map<String,Object>> dataSqlList = DbUp.upTable("sc_token").dataSqlList(sql, null);
		if(dataSqlList!=null&&dataSqlList.size()>0) {
			Map<String, Object> subMap = dataSqlList.get(0);
			pModel.setAccess_token(subMap.get("access_token").toString());
			pModel.setCreate_time(subMap.get("create_time").toString());
			pModel.setExpires_time(subMap.get("expires_time").toString());
		}
        
		return pModel;
	}
	@Override
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private static ConfigTop PLUS_CONFIG = new ConfigTop() {
		@Override
		public EKvSchema getSchema() {
			return EKvSchema.wxMusicAlbumToken;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelwxMusicAlbumToken.class;
		}
		
		public int getExpireSecond() {
			return 60*60*2;
		}
		
	};
	

}
