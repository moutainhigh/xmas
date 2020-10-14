package com.srnpr.xmassystem.support;

import java.util.List;

import com.srnpr.xmassystem.load.LoadFreight;
import com.srnpr.xmassystem.modelproduct.PlusModelFreight;
import com.srnpr.xmassystem.modelproduct.PlusModelFreightDetail;
import com.srnpr.xmassystem.modelproduct.PlusModelFreightQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.SerializeSupport;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 运费模板信息
 * @author jlin
 *
 */
public class PlusSupportFreight {

	/**
	 * 加载运费模板信息
	 * @param transportTemplateUid
	 * @return
	 */
	public PlusModelFreight initFreightFromDb(String transportTemplateUid) {

		PlusModelFreight plusModelFreight = new PlusModelFreight();

		MDataMap freightTpl = DbUp.upTable("uc_freight_tpl").one("uid", transportTemplateUid);
		SerializeSupport<PlusModelFreight> support = new SerializeSupport<PlusModelFreight>();
		support.serialize(freightTpl, plusModelFreight);

		List<MDataMap> details = DbUp.upTable("uc_freight_tpl_detail").queryByWhere("tpl_uid", transportTemplateUid);
		SerializeSupport<PlusModelFreightDetail> support2 = new SerializeSupport<PlusModelFreightDetail>();
		for (MDataMap mDataMap : details) {
			PlusModelFreightDetail detail = new PlusModelFreightDetail();
			support2.serialize(mDataMap, detail);
			plusModelFreight.getFreightDetails().add(detail);
		}

		return plusModelFreight;
	}
	
	/**
	 * 获取运费模板信息
	 * @param transportTemplateUid
	 * @return
	 */
	public PlusModelFreight upFreight(String transportTemplateUid) {
		return new LoadFreight().upInfoByCode(new PlusModelFreightQuery(transportTemplateUid));
	}
	
}
