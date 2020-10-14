package com.srnpr.xmasorder.model.jingdong;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.1.13	查询赠品信息接口 响应VO
 * @remark 
 * @author 任宏斌
 * @date 2019年5月16日
 */
public class GiftsVO {

	private List<SkuGiftVO> gifts = new ArrayList<SkuGiftVO>();

	public List<SkuGiftVO> getGifts() {
		return gifts;
	}

	public void setGifts(List<SkuGiftVO> gifts) {
		this.gifts = gifts;
	}
	
}
