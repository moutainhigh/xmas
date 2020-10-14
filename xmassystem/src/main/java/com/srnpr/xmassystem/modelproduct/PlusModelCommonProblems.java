package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**   
 * 	常见问题
*    ligj
*/
public class PlusModelCommonProblems implements IPlusModel {
	@ZapcomApi(value="常见问题",remark="现在只有跨境通商品有常见问题")
    private List<PlusModelCommonProblem> commonProblems = new ArrayList<PlusModelCommonProblem>();

	public List<PlusModelCommonProblem> getCommonProblems() {
		return commonProblems;
	}

	public void setCommonProblems(List<PlusModelCommonProblem> commonProblems) {
		this.commonProblems = commonProblems;
	}
	
}

