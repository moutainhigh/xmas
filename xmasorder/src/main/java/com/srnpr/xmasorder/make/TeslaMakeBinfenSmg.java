package com.srnpr.xmasorder.make;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.load.LoadErweiCodeBinfen;
import com.srnpr.xmassystem.modelevent.PlusModelErweiCodeBinfen;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 记录缤纷扫码购订单
 */
public class TeslaMakeBinfenSmg  extends TeslaTopOrderMake {

	LoadErweiCodeBinfen loadErweiCode = new LoadErweiCodeBinfen();
	
	@Override
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult xResult = new TeslaXResult();
		
		// 忽略非缤纷扫码购订单
		if(!"449715190048".equals(teslaOrder.getUorderInfo().getOrderSource())) {
			return xResult;
		}
		
		if(StringUtils.isBlank(teslaOrder.getQrcode())) {
			return xResult;
		}
		
		
		PlusModelErweiCodeBinfen erweiCode = loadErweiCode.upInfoByCode(new PlusModelQuery(teslaOrder.getQrcode()));
		// 如果未取到值则删除缓存再查询一次，避免因为缓存不一致导致的问题
		if(StringUtils.isBlank(erweiCode.getMclassId())) {
			loadErweiCode.deleteInfoByCode(teslaOrder.getQrcode());
			erweiCode = loadErweiCode.upInfoByCode(new PlusModelQuery(teslaOrder.getQrcode()));
		}
		
		if(StringUtils.isBlank(erweiCode.getMclassId())) {
			return xResult;
		}
		
		// 记录缤纷扫码订单
		List<TeslaModelOrderInfo> orderList = teslaOrder.getSorderInfo();
		for(TeslaModelOrderInfo order : orderList) {
			DbUp.upTable("oc_order_binfen_smg").dataInsert(new MDataMap(
					"order_code", order.getOrderCode(),
					"qrcode", teslaOrder.getQrcode(),
					"mclass_id", erweiCode.getMclassId(),
					"create_time", FormatHelper.upDateTime()
					));
		}
		
		return xResult;
	}
}
