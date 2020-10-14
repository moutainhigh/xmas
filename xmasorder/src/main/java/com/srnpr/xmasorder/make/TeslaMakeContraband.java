package com.srnpr.xmasorder.make;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 校验订单商品中是否包含违禁品
 * 注：行政地址编号
 * @author cc
 *
 */
public class TeslaMakeContraband extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		String areaCode = teslaOrder.getAddress().getAreaCode();
		List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();
		boolean flag = false;
		String skuName = "";
		String productCode = "";
		String contrabandStr = "";
		if(StringUtils.isNotBlank(areaCode)){
			String mAreaCode = areaCode.substring(0, 4) + "00";
			String lAreaCode = areaCode.substring(0, 2) + "0000";
			areaCode = areaCode.substring(0, 6);
			for(TeslaModelOrderDetail detail : orderDetails) {
				productCode = detail.getProductCode();
				MDataMap mProductExtMap = DbUp.upTable("pc_productinfo_ext").one("product_code", productCode, "check_danger", "Y");
				//需要检验商品是否是违禁品
				if(mProductExtMap != null) {					
					String is_danger = "";
					Map<String, Object> map = new HashMap<String, Object>();
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					String sql = "select danger_type,toplimit from pc_product_contraband where ((lrgn_cd =:lAreaCode and mrgn_cd = '000000') or (mrgn_cd =:mAreaCode and srgn_cd = '000000') or srgn_cd =:area_code) and danger_type =:is_danger and vl_yn='Y'";
					
					BigDecimal wd = new BigDecimal(mProductExtMap.get("wd").toString()); //长
					BigDecimal dp = new BigDecimal(mProductExtMap.get("dp").toString()); //宽
					BigDecimal hg = new BigDecimal(mProductExtMap.get("hg").toString()); //高
					BigDecimal wg = new BigDecimal(mProductExtMap.get("wg").toString()); //重量
					String is_unpack = mProductExtMap.get("is_unpack").toString();//是否为拆包件
					//处理长宽高
					if(wd.compareTo(BigDecimal.ZERO) > 0 || dp.compareTo(BigDecimal.ZERO) > 0 || hg.compareTo(BigDecimal.ZERO) > 0) {
						is_danger = "E";
						list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lAreaCode,"mAreaCode",mAreaCode,"area_code",areaCode,"is_danger",is_danger));						
						if(list != null && list.size() > 0) {
							map = list.get(0);
							BigDecimal toplimit = new BigDecimal(map.get("toplimit").toString());						
							if(toplimit.compareTo(wd) <= 0 || toplimit.compareTo(dp) <= 0 || toplimit.compareTo(hg) <= 0) {
								//有违禁品，不准下单
								flag = true;
								skuName = detail.getSkuName();
								contrabandStr = "订单地区：" + areaCode + ",商品长宽高超过限制，商品长：" + wd.toString() + ",宽：" + dp.toString() + ",高：" + hg.toString() + ",限制的最大边距为：" + toplimit.toString();
								break;
							}
						}						
					} 
					//处理重量
					if(wg.compareTo(BigDecimal.ZERO) > 0) {
						is_danger = "D";
						list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lAreaCode,"mAreaCode",mAreaCode,"area_code",areaCode,"is_danger",is_danger));
						if(list != null && list.size() > 0) {
							map = list.get(0);
							BigDecimal toplimit = new BigDecimal(map.get("toplimit").toString());						
							if(toplimit.compareTo(wg) <= 0) {
								//有违禁品，不准下单
								flag = true;
								skuName = detail.getSkuName();
								contrabandStr = "订单地区：" + areaCode + ",商品重量超过限制，商品重：" + wg.toString() + ",限制的最大重量为：" + toplimit.toString();
								break;
							}
						}
					}
					//处理拆包件
					if("Y".equals(is_unpack)) {
						is_danger = "F";
						list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lAreaCode,"mAreaCode",mAreaCode,"area_code",areaCode,"is_danger",is_danger));
						if(list != null && list.size() > 0) {
							//有违禁品，不准下单
							flag = true;
							skuName = detail.getSkuName();
							contrabandStr = "订单地区：" + areaCode + ",商品为拆包件,该地区拆包件为违禁品";
							break;
						}
					}
					is_danger = mProductExtMap.get("is_danger");//不包含D、E、F
					list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lAreaCode,"mAreaCode",mAreaCode,"area_code",areaCode,"is_danger",is_danger));														
					if(list != null && list.size() > 0) {
						//匹配违禁品属性+地区编号(A：酒水 B：粉末 C：易燃易爆 Y：刀具)
						if("A".equals(is_danger) || "B".equals(is_danger) || "C".equals(is_danger) || "Y".equals(is_danger)) {
							/**
							 * 1.商品违禁品属性+收货地址 匹配 违禁品配置表 ABCY 
							 */						
							//有违禁品，不准下单
							flag = true;
							skuName = detail.getSkuName();
							contrabandStr = "订单地区：" + areaCode + ",商品属于" + is_danger + "类物品,在该地区为违禁品";
							break;
						} 
					}					
				}
			}			
		}	
		if(flag) {
			//记录违禁品下单失败日志begin
			MDataMap logMap = new MDataMap();
			logMap.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
			logMap.put("member_code", teslaOrder.getUorderInfo().getBuyerCode());
			logMap.put("area_code", areaCode);
			logMap.put("product_code", productCode);
			logMap.put("sku_name", skuName);
			logMap.put("create_time", DateUtil.getSysDateTimeString());
			logMap.put("remark", contrabandStr);
			DbUp.upTable("lc_contraband_log").dataInsert(logMap);
			//记录违禁品下单失败日志end
			xResult.setResultCode(963902223);
			xResult.setResultMessage(bInfo(963902223, skuName));
		}
		return xResult;
	}
}
