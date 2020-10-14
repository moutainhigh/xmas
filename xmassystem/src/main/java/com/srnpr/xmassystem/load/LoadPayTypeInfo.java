package com.srnpr.xmassystem.load;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.enumer.Channel;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelbean.PayTypeInfo;
import com.srnpr.xmassystem.modelevent.PlusModelPayTypeInfo;
import com.srnpr.xmassystem.modelevent.PlusModelPayTypeInfoQuery;
import com.srnpr.xmassystem.plusconfig.ConfigTop;
import com.srnpr.xmassystem.top.LoadTop;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 获取支付类型信息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月12日
 */
public class LoadPayTypeInfo extends LoadTop<PlusModelPayTypeInfo,PlusModelPayTypeInfoQuery>{

	public PlusModelPayTypeInfo topInitInfo(PlusModelPayTypeInfoQuery tQuery) {

		List<MDataMap> typeList = DbUp.upTable("fh_payment_type").queryAll("pay_type,visible", "pay_type", "", new MDataMap()); // 已配置支持的支付类型
		PayTypeInfo typeInfo = null;
		PlusModelPayTypeInfo result = new PlusModelPayTypeInfo();
		
		for(MDataMap map : typeList){
			typeInfo = new PayTypeInfo();
			typeInfo.setPayType(map.get("pay_type"));
			
			// 取前端显示的渠道
			String[] texts = map.get("visible").split(",");
			for(String v : texts){
				if(StringUtils.isNotBlank(v)){
					try {
						typeInfo.getChannelList().add(Channel.valueOf(StringUtils.trimToEmpty(v).toUpperCase()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			// 如果支付类型未配置任何前端显示，则不展示
			if(typeInfo.getChannelList().isEmpty()){
				continue;
			}
			
			// 取支付屏蔽的商户
			List<MDataMap> dataList = DbUp.upTable("fh_payment_type_seller").queryAll("small_seller_code", "", "", new MDataMap("pay_type",typeInfo.getPayType()));
			for(MDataMap data : dataList){
				typeInfo.getSellerList().add(StringUtils.trimToEmpty(data.get("small_seller_code")).toUpperCase());
			}
			
			// 取屏蔽的商户类型
			dataList = DbUp.upTable("fh_payment_type_seller_type").queryAll("small_seller_type", "", "", new MDataMap("pay_type",typeInfo.getPayType()));
			for(MDataMap data : dataList){
				typeInfo.getSellerTypeList().add(StringUtils.trimToEmpty(data.get("small_seller_type")).toUpperCase());
			}
			
			//支付类型支持的商品
			dataList = DbUp.upTable("fh_payment_type_product").queryAll("product_code", "", "", new MDataMap("pay_type",typeInfo.getPayType()));
			for (MDataMap data : dataList) {
				typeInfo.getTypeProductList().add(data.get("product_code"));
			}
			result.getTypeInfoList().add(typeInfo);
		}
		
		return result;
	}

    private final static PlusConfigPayTypeInfo PLUS_CONFIG = new PlusConfigPayTypeInfo();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	public static class PlusConfigPayTypeInfo extends ConfigTop {

		public EKvSchema getSchema() {
			return EKvSchema.payTypeInfo;
		}

		public Class<?> getPlusClass() {
			return PlusModelPayTypeInfo.class;
		}

		// 10分钟过期时间
		@Override
		public int getExpireSecond() {
			return 600;
		}
		
	}
}
