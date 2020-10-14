package com.srnpr.xmassystem.load;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoods;
import com.srnpr.xmassystem.modelevent.PlusModelPcTvGoodsQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取指定日期当天的节目单商品
 */
public class LoadPcTvGoods extends LoadTop<PlusModelPcTvGoods,PlusModelPcTvGoodsQuery>{

	public PlusModelPcTvGoods topInitInfo(PlusModelPcTvGoodsQuery tQuery) {
		String day = tQuery.getCode();
		if(StringUtils.isBlank(day)){
			day = FormatHelper.upDateTime("yyyy-MM-dd");
		}
		
		List<Map<String, Object>> list = DbUp.upTable("pc_tv").dataSqlList("SELECT DISTINCT good_id FROM pc_tv WHERE form_fr_date LIKE :form_fr_date", new MDataMap("form_fr_date", day+"%"));
		PlusModelPcTvGoods tvGoods = new PlusModelPcTvGoods();
		
		for(Map<String, Object> map : list){
			tvGoods.getGoods().add(map.get("good_id")+"");
		}
		
		return tvGoods;
	}

    private final static PlusConfigPcTvGoods PLUS_CONFIG = new PlusConfigPcTvGoods();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	public static class PlusConfigPcTvGoods extends ConfigTop {

		public EKvSchema getSchema() {
			return EKvSchema.PcTvGoods;
		}

		public Class<?> getPlusClass() {
			return PlusModelPcTvGoods.class;
		}

		// 5分钟过期时间
		@Override
		public int getExpireSecond() {
			return 300;
		}
		
	}
}
