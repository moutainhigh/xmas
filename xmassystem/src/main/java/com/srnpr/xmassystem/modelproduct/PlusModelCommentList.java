package com.srnpr.xmassystem.modelproduct;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapweb.webapi.RootResultWeb;

public class PlusModelCommentList extends RootResultWeb{
	//商品评论列表
	private List<PlusModelComment> productComment = new ArrayList<PlusModelComment>();

	public List<PlusModelComment> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<PlusModelComment> productComment) {
		this.productComment = productComment;
	}
	
}
