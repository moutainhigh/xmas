package com.srnpr.xmassystem.load;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelproduct.PlusModelCommonProblems;
import com.srnpr.xmassystem.modelproduct.PlusModelProductCommonProblemQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigProductCommonProblem;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.top.LoadTop;

/***
 * 加载跨境通常见问题
 * @author ligj
 *
 */
public class LoadProductCommonProblem extends LoadTop<PlusModelCommonProblems, PlusModelProductCommonProblemQuery> {

	public PlusModelCommonProblems topInitInfo(PlusModelProductCommonProblemQuery query) {
		return new PlusSupportProduct().initProductCommonProblemFromDb(query.getCode());
	}

	private final static PlusConfigProductCommonProblem PLUS_CONFIG = new PlusConfigProductCommonProblem();

	public IPlusConfig topConfig() {

		return PLUS_CONFIG;
	}

}
