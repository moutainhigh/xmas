package com.srnpr.xmassystem.load;


import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelProductOrderNum;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 一栏大图模版的商品销量
 */
public class LoadProductOrderNum extends LoadTop<PlusModelProductOrderNum, PlusModelQuery> {

	@Override
	public PlusModelProductOrderNum topInitInfo(PlusModelQuery tQuery) {
		PlusModelProductOrderNum plusModel = new PlusModelProductOrderNum();
		
		String sql = "select IFNull(count(a.order_code),0) as saleNum from oc_orderdetail a, oc_orderinfo b where a.product_code=:product_code and  a.order_code=b.order_code and  b.order_status= '4497153900010005'";
		Map<String, Object> map = DbUp.upTable("oc_orderdetail").dataSqlOne(sql, new MDataMap("product_code",tQuery.getCode()));
		plusModel.setNum(NumberUtils.toInt(map.get("saleNum")+""));
		
		return plusModel;
	}
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}

	private final static ConfigTop PLUS_CONFIG = new ConfigTop() {

		@Override
		public EKvSchema getSchema() {
			return EKvSchema.ProductOrderNum;
		}

		@Override
		public Class<?> getPlusClass() {
			return PlusModelProductOrderNum.class;
		}
		
		// 缓存5-10分钟左右
		public int getExpireSecond() {
			return 300 + RandomUtils.nextInt(300);
		}

		// 数据量比较小不用存本地缓存
		@Override
		public boolean enabledLocalCache() {
			return false;
		}
		
	};


}
