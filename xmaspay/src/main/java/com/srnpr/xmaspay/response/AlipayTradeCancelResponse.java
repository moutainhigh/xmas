package com.srnpr.xmaspay.response;

import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.common.AlipayUnifyResultCodeEnum;

/**
 * 支付宝交易取消信息
 * @author pang_jhui
 *
 */
public class AlipayTradeCancelResponse extends AlipayUnifyResponse {
	
	/*关闭处理结果响应码*/
	private String result_code = "";
	
	/*支付宝交易号*/
	private String trade_no = "";
	
	/*商户唯一订单编号*/
	private String out_trade_no = "";
	
	/*详细错误码*/
	private String detail_error_code = "";
	
	/*详细错误描述*/
	private String detail_error_des = "";

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getDetail_error_code() {
		return detail_error_code;
	}

	public void setDetail_error_code(String detail_error_code) {
		this.detail_error_code = detail_error_code;
	}

	public String getDetail_error_des() {
		return detail_error_des;
	}

	public void setDetail_error_des(String detail_error_des) {
		this.detail_error_des = detail_error_des;
	}
	
	/**
	 * 判断接口业务是否处理成功
	 * @return
	 */
	public boolean upFlagTrue(){
		
		boolean flag = false;
		
		if (StringUtils.equals(getIs_success(), AlipayUnifyResultCodeEnum.SUCCESS.name())) {
			
			flag = true;

		}
		
		return flag;
		
		
	}
	
}
