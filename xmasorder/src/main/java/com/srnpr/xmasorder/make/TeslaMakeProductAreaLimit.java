package com.srnpr.xmasorder.make;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 校验商品限购地区
 * 
 * @author xiegj
 *
 */
public class TeslaMakeProductAreaLimit  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();
		if(StringUtils.isNotBlank(teslaOrder.getAddress().getAreaCode())){
			//String addressAreaCode = teslaOrder.getAddress().getAreaCode().substring(0, 4)+"00";//只匹配前四位 省市区中的市
			String addressAreaCode = getCityCode(teslaOrder.getAddress().getAreaCode());
			for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
				if("0".equals(teslaOrder.getShowGoods().get(i).getGiftFlag())){continue;}
				PlusModelTemplateAeraCode area = teslaOrder.getShowGoods().get(i).getAreaCodes();
				boolean flag = false;//默认不支持配送
				if(area!=null&&area.getAreaCodes()!=null&&area.getAreaCodes().size()>0){
					for (int j = 0; j < area.getAreaCodes().size(); j++) {
						if(addressAreaCode.equals(area.getAreaCodes().get(j))){
							flag = true;
							break;
						}
					}
					if (teslaOrder.getStatus().getExecStep()==ETeslaExec.Confirm&&!flag) {
						teslaOrder.getShowGoods().get(i).setAlert(bConfig("xmasorder.areaLimitShow"));
					}else if ((teslaOrder.getStatus().getExecStep()==ETeslaExec.Create||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate)&&!flag) {
						xResult.setResultCode(963904005);
						xResult.setResultMessage(bInfo(963904005));
						break;
					}
				}
			}
		}
		return xResult;
	}
	
	/**
	 * 兼容部分区域的4级编码以99开头匹配不到二级地址的情况
	 * @param areaCode
	 * @return
	 */
	private String getCityCode(String areaCode) {
		String c = areaCode;
		// 区域编码长度超过6位则表示是4级编码，取父级的三级编码做截取
		if(c.length() > 6) {
			MDataMap map = DbUp.upTable("sc_tmp").one("code", areaCode);
			
			// 取一下三级区域编码数据
			if(map != null && "4".equals(map.get("code_lvl"))) {
				map = DbUp.upTable("sc_tmp").one("code", map.get("p_code"));
			}
			
			// 取二级区域编码
			if(map != null) {
				c = map.get("p_code");
			}
			
			if(StringUtils.isBlank(c)) {
				c = areaCode;
			}
		}
		return StringUtils.left(c, 4)+"00";
	}

}
