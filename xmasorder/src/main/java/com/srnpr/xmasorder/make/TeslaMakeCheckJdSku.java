package com.srnpr.xmasorder.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.jingdong.CheckAreaLimitVO;
import com.srnpr.xmasorder.model.jingdong.PriceVo;
import com.srnpr.xmasorder.model.jingdong.ProductCheckRepVo;
import com.srnpr.xmasorder.model.jingdong.SkuNum;
import com.srnpr.xmasorder.model.jingdong.StockNewResultVo;
import com.srnpr.xmasorder.service.TeslaOrderServiceJD;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.support.PlusSupportJdAddress;
import com.srnpr.xmassystem.support.PlusSupportJdAddress.JdAddress;
import com.srnpr.xmassystem.support.PlusSupportProduct;

/**
 * 京东sku校验
 * 
 * @remark
 * @author 任宏斌
 * @date 2019年5月13日
 */
public class TeslaMakeCheckJdSku extends TeslaTopOrderMake {

	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();

		List<TeslaModelOrderDetail> orderDetails = teslaOrder.getOrderDetails();

		if (orderDetails != null && !orderDetails.isEmpty()) {

			Map<String, PlusModelSkuInfo> sku = new HashMap<String, PlusModelSkuInfo>();
			for (int i = 0; i < orderDetails.size(); i++) {
				// 判断是否原价购买
				boolean isOriginal = false, isSupportCollage = false;
				if ("1".equals(teslaOrder.getIsOriginal())) {
					isOriginal = true;
				}
				if ("1".equals(teslaOrder.getCollageFlag())) {
					isSupportCollage = true;
				}
				PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(
						teslaOrder.getOrderDetails().get(i).getSkuCode(), teslaOrder.getUorderInfo().getBuyerCode(),
						teslaOrder.getIsMemberCode(), teslaOrder.getIsPurchase(),
						teslaOrder.getUorderInfo().getOrderSource(), isOriginal, isSupportCollage,orderDetails.get(i).getIfJJGFlag());

				//没有收货地址的京东订单 不进行校验
				if (Constants.SMALL_SELLER_CODE_JD.equals(plusModelSkuInfo.getSmallSellerCode()) 
						&& StringUtils.isNotEmpty(teslaOrder.getAddress().getAddressCode())) {
					sku.put(plusModelSkuInfo.getSellProductcode(), plusModelSkuInfo);
				}
			}

			if (sku.size() > 0) {
				String skuIds = StringUtils.join(sku.keySet(), ",");
				String areaCode = teslaOrder.getAddress().getAreaCode();
				TeslaOrderServiceJD teslaOrderServiceJD = new TeslaOrderServiceJD();

				if (StringUtils.isNotEmpty(areaCode)) {

					JdAddress jdAddress = null;
					PlusSupportJdAddress plusSupportJdAddress = new PlusSupportJdAddress();
					// 先查客服维护地址表
					jdAddress = plusSupportJdAddress.getJdAddressName(areaCode);
					if (null == jdAddress) {
						// 再查京东地址标准转换表
						String jdTownId = plusSupportJdAddress.getJdAddressId(areaCode);
						if(StringUtils.isNotBlank(jdTownId)) {
							jdAddress = plusSupportJdAddress.getJdAddress(jdTownId);
						}
					}

					if (null != jdAddress && StringUtils.isNotEmpty(jdAddress.getProvinceId())
							&& StringUtils.isNotEmpty(jdAddress.getCityId())
							&& StringUtils.isNotEmpty(jdAddress.getCountyId())) {

						String provinceId = jdAddress.getProvinceId();
						String cityId = jdAddress.getCityId();
						String countyId = jdAddress.getCountyId();
						String villageId = StringUtils.isEmpty(jdAddress.getVillageId()) ? "0"
								: jdAddress.getVillageId();
						// 校验区域是否可售
						List<CheckAreaLimitVO> checkAreaLimitList = teslaOrderServiceJD.checkAreaLimit(skuIds,
								provinceId, cityId, countyId, villageId);
						if (null != checkAreaLimitList) {
							boolean flag = true;
							List<String> skuNameList = new ArrayList<String>();
							for (CheckAreaLimitVO checkAreaLimitVO : checkAreaLimitList) {
								if (checkAreaLimitVO.isAreaRestrict()) {
									flag = false;
									skuNameList.add(sku.get(checkAreaLimitVO.getSkuId()).getSkuName());
								}
							}
							if (!flag) {
								result.inErrorMessage(963912004, StringUtils.join(skuNameList, ","));
								return result;
							}
						} else {
							result.inErrorMessage(963902249);
							return result;
						}

						// 校验库存
						String area = provinceId + "_" + cityId + "_" + countyId + "_" + villageId;
						List<SkuNum> skuNums = new ArrayList<SkuNum>();
						for (String skuCodeOld : sku.keySet()) {
							SkuNum skuNum = new SkuNum();
							skuNum.setSkuId(skuCodeOld);
							for (TeslaModelOrderDetail detail : teslaOrder.getOrderDetails()) {
								if (sku.get(skuCodeOld).getSkuCode().equals(detail.getSkuCode())) {
									skuNum.setNum(detail.getSkuNum());
								}
							}
							skuNums.add(skuNum);
						}
						List<StockNewResultVo> stockList = teslaOrderServiceJD.getStock(area, skuNums);
						if (null != stockList) {
							boolean flag = true;
							List<String> skuNameList = new ArrayList<String>();
							for (StockNewResultVo stockNewResultVo : stockList) {
								if (34 == stockNewResultVo.getStockStateId()
										|| 36 == stockNewResultVo.getStockStateId()) {
									flag = false;
									skuNameList.add(sku.get(stockNewResultVo.getSkuId()).getSkuName());
								}
							}
							if (!flag) {
								result.inErrorMessage(963903001, StringUtils.join(skuNameList, ","));
								return result;
							}
						} else {
							result.inErrorMessage(963902249);
							return result;
						}
					}
				} else {
					result.inErrorMessage(963902216);
					return result;
				}

				// 校验是否可售
				List<ProductCheckRepVo> productCheckRepVoList = teslaOrderServiceJD.getSaleState(skuIds,null);
				if (null != productCheckRepVoList) {
					boolean flag = true;
					List<String> skuNameList = new ArrayList<String>();
					for (ProductCheckRepVo productCheckRepVo : productCheckRepVoList) {
						if (productCheckRepVo.getSaleState() != 1) {
							flag = false;
							skuNameList.add(sku.get(productCheckRepVo.getSkuId()).getSkuName());
						}
					}
					if (!flag) {
						result.inErrorMessage(963912005, StringUtils.join(skuNameList, ","));
						return result;
					}
				} else {
					result.inErrorMessage(963902249);
					return result;
				}

				// 校验协议价
				List<PriceVo> negotiatedPriceList = teslaOrderServiceJD.getNegotiatedPrice(skuIds);
				if (null != negotiatedPriceList) {
					boolean flag = true;
					List<String> skuNameList = new ArrayList<String>();
					for (PriceVo priceVo : negotiatedPriceList) {
						if (priceVo.getPrice().compareTo(sku.get(priceVo.getSkuId()).getCostPrice()) != 0) {
							flag = false;
							skuNameList.add(sku.get(priceVo.getSkuId()).getSkuName());
						}
					}
					if (!flag) {
						result.inErrorMessage(963912006, StringUtils.join(skuNameList, ","));
						return result;
					}
				} else {
					result.inErrorMessage(963902249);
					return result;
				}

			}
		}
		return result;
	}
}
