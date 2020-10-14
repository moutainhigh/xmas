package com.srnpr.xmaspay.response;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmaspay.common.AlipaySuccessEnum;
import com.srnpr.xmaspay.face.IPayResponse;

/**
 * 支付宝响应信息
 * @author pang_jhui
 *
 */
public class AlipayUnifyResponse implements IPayResponse {
	
	/*请求是否成功。请求成功不代表业务处理成功*/
	private String is_success = "";

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}
	
	/**
	 * 判断接口是否请求成功
	 * @return
	 */
	public boolean upFlagTrue(){
		
		boolean flag = false;
		
		if (StringUtils.equals(getIs_success(), AlipaySuccessEnum.T.name())) {
			
			flag = true;

		}
		
		return flag;
		
		
	}
	
	

}
