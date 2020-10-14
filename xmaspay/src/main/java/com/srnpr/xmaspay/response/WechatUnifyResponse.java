package com.srnpr.xmaspay.response;

import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.common.WechatUnifyResultCodeEnum;
import com.srnpr.xmaspay.face.IPayResponse;

/**
 * 微信支付统一响应信息
 * @author pang_jhui
 *
 */
public class WechatUnifyResponse implements IPayResponse {
	
	/*返回状态码*/
	private String return_code = "";
	
	/*返回信息*/
	private String return_msg = "";

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
	/**
	 * 判断接口是否处理成功
	 * @return 是否成功
	 */
	public boolean upFlagTrue(){
		
		boolean flag = false;
		
		if (StringUtils.equals(getReturn_code(), WechatUnifyResultCodeEnum.SUCCESS.name())) {
			
			flag = true;

		}
		
		return flag;
		
		
	}

}
