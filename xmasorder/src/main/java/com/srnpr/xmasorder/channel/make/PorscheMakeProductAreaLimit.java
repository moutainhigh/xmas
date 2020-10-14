package com.srnpr.xmasorder.channel.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.channel.gt.PorscheGtOrder;
import com.srnpr.xmasorder.channel.gt.PorscheGtResult;
import com.srnpr.xmasorder.channel.top.PorscheTopOrderMake;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 校验商品限购地区
 * @remark
 * @author 任宏斌
 * @date 2019年12月4日
 */
public class PorscheMakeProductAreaLimit extends PorscheTopOrderMake {

	@Override
	public PorscheGtResult doRefresh(PorscheGtOrder porscheGtOrder) {

		PorscheGtResult gtResult = new PorscheGtResult();
		if (StringUtils.isNotBlank(porscheGtOrder.getAddress().getAreaCode())) {
			String addressAreaCode = getCityCode(porscheGtOrder.getAddress().getAreaCode());
			for (int i = 0; i < porscheGtOrder.getShowGoods().size(); i++) {
				if ("0".equals(porscheGtOrder.getShowGoods().get(i).getGiftFlag())) {
					continue;
				}
				PlusModelTemplateAeraCode area = porscheGtOrder.getShowGoods().get(i).getAreaCodes();
				boolean flag = false;// 默认不支持配送
				if (area != null && area.getAreaCodes() != null && area.getAreaCodes().size() > 0) {
					for (int j = 0; j < area.getAreaCodes().size(); j++) {
						if (addressAreaCode.equals(area.getAreaCodes().get(j))) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						String skuCode = porscheGtOrder.getShowGoods().get(i).getSkuCode();
						gtResult.setResultCode(0);
						gtResult.setResultMessage(skuCode + "在" + addressAreaCode + "区域限售");
						break;
					}
				}
			}
		}
		return gtResult;
	}

	/**
	 * 兼容部分区域的4级编码以99开头匹配不到二级地址的情况
	 * 
	 * @param areaCode
	 * @return
	 */
	private String getCityCode(String areaCode) {
		String c = areaCode;
		// 区域编码长度超过6位则表示是4级编码，取父级的三级编码做截取
		if (c.length() > 6) {
			MDataMap map = DbUp.upTable("sc_tmp").one("code", areaCode);

			// 取一下三级区域编码数据
			if (map != null && "4".equals(map.get("code_lvl"))) {
				map = DbUp.upTable("sc_tmp").one("code", map.get("p_code"));
			}

			// 取二级区域编码
			if (map != null) {
				c = map.get("p_code");
			}

			if (StringUtils.isBlank(c)) {
				c = areaCode;
			}
		}
		return StringUtils.left(c, 4) + "00";
	}

}
