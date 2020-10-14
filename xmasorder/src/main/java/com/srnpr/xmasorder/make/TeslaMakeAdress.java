package com.srnpr.xmasorder.make;

import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadAdressAreaCode;
import com.srnpr.xmassystem.modelproduct.PlusModelAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelAreaQuery;

/**
 * 校验地址信息
 * 注：针对行政地址编号变更的情况，对用户维护的地址信息中无效的行政地址编号进行提示，
 * 让用户及时更新地址信息，避免出现订单因行政地址编号不能写入LD系统的问题。
 * @author xiegj
 *
 */
public class TeslaMakeAdress  extends TeslaTopOrderMake {

	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {

		TeslaXResult xResult = new TeslaXResult();
		boolean ldFlag = false;
		for (int i = 0; i < teslaOrder.getSorderInfo().size(); i++) {
			if("SI2003".equals(teslaOrder.getSorderInfo().get(i).getSmallSellerCode())){
				ldFlag=true;
				break;
			}
		}
		if(ldFlag){
			String areaCode = teslaOrder.getAddress().getAreaCode();
			PlusModelAreaQuery plusAreaQuery = new PlusModelAreaQuery();
			plusAreaQuery.setCode(areaCode);
			PlusModelAeraCode codes = new LoadAdressAreaCode().upInfoByCode(plusAreaQuery);
			boolean flag = false;
			if(codes.getAreaCodes()!=null&&!codes.getAreaCodes().isEmpty()){
				for (int i = 0; i < codes.getAreaCodes().size(); i++) {
					if(areaCode.equals(codes.getAreaCodes().get(i))){
						flag=true;
						break;
					}
				}
			}
			if(!flag){
				xResult.setResultCode(963902219);
				xResult.setResultMessage(bInfo(963902219));
			}
		}
		
		return xResult;
	}

}
