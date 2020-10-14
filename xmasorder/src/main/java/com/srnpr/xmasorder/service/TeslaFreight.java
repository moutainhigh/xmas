package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.modelproduct.PlusModelFreight;
import com.srnpr.xmassystem.modelproduct.PlusModelFreightDetail;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.support.PlusSupportFreight;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 运费计算
 * @author jlin
 *
 */
public class TeslaFreight extends BaseClass {

	public BigDecimal getFreightMoney(String productCode,int num,String areaCode){
		
		BigDecimal freightMoney = BigDecimal.ZERO;
		if(StringUtils.isNotBlank(areaCode)){
			areaCode = getProvinceCode(areaCode);//转换成省级的编码
			
			//获取商品信息
			PlusModelProductInfo plusModelProductInfo = new PlusSupportProduct().upProductInfo(productCode);
			BigDecimal productVolume=plusModelProductInfo.getProductVolume();
			BigDecimal productWeight=plusModelProductInfo.getProductWeight();
			String transportTemplate = plusModelProductInfo.getTransportTemplate();
			
			//若没有设置运费模板，则运费为0
			if(StringUtils.isBlank(transportTemplate)){
				return BigDecimal.ZERO;
			}
			
			//若运费模板的不是uid，则是用户承担的运费
			if(transportTemplate.length()<32){
				return new BigDecimal(transportTemplate);
			}
			
			//运费模板计算开始
			PlusModelFreight plusModelFreight = new PlusSupportFreight().upFreight(transportTemplate);
			String isDisable = plusModelFreight.getIsDisable();
			String valuationType = plusModelFreight.getValuationType();//计价方式 449746290001  按件数, 449746290003  按体积,449746290002  按重量
			
			if("449746250001".equals(isDisable)){//运费模板禁用
				return BigDecimal.ZERO;
			}
			
			List<PlusModelFreightDetail> freightDetails = plusModelFreight.getFreightDetails();
			
			BigDecimal expressStart = BigDecimal.ZERO;
			BigDecimal expressPostage = BigDecimal.ZERO;
			BigDecimal expressPlus = BigDecimal.ZERO;
			BigDecimal expressPostageplus = BigDecimal.ZERO;
			for (PlusModelFreightDetail plusModelFreightDetail : freightDetails) {
	
				String areaCodes = plusModelFreightDetail.getAreaCode();
				if ("global".equals(areaCodes) || isHasArea(areaCode, areaCodes)) {
					expressStart = plusModelFreightDetail.getExpressStart();
					expressPostage = plusModelFreightDetail.getExpressPostage();
					expressPlus = plusModelFreightDetail.getExpressPlus();
					expressPostageplus = plusModelFreightDetail.getExpressPostageplus();
	
					if (!"global".equals(areaCodes)) {
						break;
					}
				}
			}
			
			//计价方式 449746290001  按件数, 449746290003  按体积,449746290002  按重量
			
			BigDecimal sunit=BigDecimal.ONE;//件数 默认为1
			if("449746290003".equals(valuationType)){
				sunit=productVolume;
			}else if("449746290002".equals(valuationType)){
				sunit=productWeight;
			}
		freightMoney=freightMoney(expressStart, expressPostage, expressPlus, expressPostageplus, num, sunit);
		}
		return freightMoney;
	}

	/***
	 * 超牛逼运费算法
	 * @param expressStart
	 * @param expressPostage
	 * @param expressPlus
	 * @param expressPostageplus
	 * @param num
	 * @param sunit
	 * @return
	 */
	private BigDecimal freightMoney(BigDecimal expressStart,BigDecimal expressPostage,BigDecimal expressPlus,BigDecimal expressPostageplus,int num,BigDecimal sunit){
		
		//首费+ceil((数量*体积-首体积)/续体积)*续费
		return expressPostage.add(((BigDecimal.valueOf(Math.ceil((((BigDecimal.valueOf(num)).multiply(sunit).subtract(expressStart)).divide(expressPlus,RoundingMode.CEILING)).doubleValue()))).multiply(expressPostageplus)));
	}
	
	/**
	 * 获取省区域代码
	 * @param areaCode
	 * @return
	 */
	private String getProvinceCode(String areaCode){
		return areaCode.subSequence(0, 2)+"0000";
	}
	
	/**
	 * 判断areaCode 是否在 areaCodes 中
	 * @param areaCode
	 * @param areaCodes
	 * @return
	 */
	private boolean isHasArea(String areaCode,String areaCodes){
		return areaCodes.contains(areaCode);
	}
}
