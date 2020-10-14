package com.srnpr.xmaspay.process.prepare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.xmaspay.AbstractPaymentProcess;
import com.srnpr.xmaspay.config.XmasPayConfig;
import com.srnpr.xmaspay.process.prepare.WechatPreparePayProcess.PaymentResult;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput.ChildOrder;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webwx.WxGateSupport;

/**
 * 
 *<p>Description:微信小程序提现 <／p> 
 * @author zb
 * @date 2020年7月9日
 *
 */
public class WechatPrepareCashProcess extends AbstractPaymentProcess<WechatPrepareCashProcess.PaymentInput, WechatPrepareCashProcess.PaymentResult>{

	static Log log = LogFactory.getLog(WechatPrepareCashProcess.class);
	
	@Override
	public PaymentResult process(PaymentInput input) {
		// TODO Auto-generated method stub

		input.bigOrderCode = input.c_transfer_order;
		String c_transfer_order = input.c_transfer_order;
		String c_transfer_reason = "用户提现";
		String receiveName = input.receiveName;
		BigDecimal transfer_amount = new BigDecimal(input.transfer_amount) ;
		String wxOpenId = input.wxOpenId;
		String apply_code = input.apply_code;
		String cust_id = input.cust_id;
		 PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		 String custId = plusServiceAccm.getCustId(cust_id);
		//int count = DbUp.upTable("fh_tgz_withdraw_info").count("apply_code",apply_code,"status","4497471600620001");
		//调用app接口查询余额
		CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		//GetCustAmtResult custAmt = custRelAmtRef.getCustAmt(custId);
		
		JSONObject postData = new JSONObject();
		postData.put("c_mid", XmasPayConfig.getPayGateMid());
		postData.put("c_ymd", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		postData.put("c_transfer_order", c_transfer_order);
		postData.put("c_transfer_reason", c_transfer_reason);
		postData.put("paygate", "764");
		postData.put("receiveName", receiveName);
		if("Y".equals(TopConfig.Instance.bConfig("familyhas.daguan_recommend_testdata"))) {
			//测试环境固定提现一块钱
			postData.put("transfer_amount", new BigDecimal("1"));
		}else {
			postData.put("transfer_amount", transfer_amount);
		}
		postData.put("wxOpenId", wxOpenId);
		String postDataText = postData.toJSONString();
		
		StringBuilder text = new StringBuilder();
		text.append("TransferCreateOrderV1").append(postDataText).append(XmasPayConfig.getPayGatePass());
		String sign = DigestUtils.md5Hex(text.toString()).toLowerCase();
		String url = XmasPayConfig.getWeXinCashReUrl()+"?sign="+sign;

		MDataMap logMap = new MDataMap();
		logMap.put("uid", WebHelper.upUuid());
		logMap.put("url",  XmasPayConfig.getWeXinCashReUrl());
		logMap.put("request", postDataText);
		logMap.put("request_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") );

		
		JSONObject resp = null;
		String content="";
		try {
			content = WebClientSupport.poolRequest(url, new StringEntity(postDataText, "UTF-8"));
			resp = JSONObject.parseObject(content);
			logMap.put("response_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("response", content);
			logMap.put("flag_success", "1");
		} catch (Exception e) {
			logMap.put("response_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap.put("response", content);
			logMap.put("flag_success", "0");
		} finally {
			DbUp.upTable("lc_tgz_request_gate_log").dataInsert(logMap);
		}
		
		if(resp.getIntValue("result") == 1){
			//成功,则修改申请状态为提现成功
			DbUp.upTable("fh_tgz_withdraw_info").dataUpdate(new MDataMap("status","4497471600620002","apply_code",apply_code), "status", "apply_code");
			MDataMap logMap2 = new MDataMap();
			logMap2.put("uid", WebHelper.upUuid());
			logMap2.put("code",  apply_code);
			logMap2.put("old_status",  "4497471600620001");
			logMap2.put("now_status",  "4497471600620002");
			logMap2.put("create_time",  DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap2.put("create_user",  "system");
			logMap2.put("remark",  "提现成功");
			DbUp.upTable("lc_tgz_withdraw_status_log").dataInsert(logMap2);	

		}else{
			//修改状态为提现失败
			DbUp.upTable("fh_tgz_withdraw_info").dataUpdate(new MDataMap("status","4497471600620003","apply_code",apply_code), "status", "apply_code");
			//添加状态变更日志
			MDataMap logMap2 = new MDataMap();
			logMap2.put("uid", WebHelper.upUuid());
			logMap2.put("code",  apply_code);
			logMap2.put("old_status",  "4497471600620001");
			logMap2.put("now_status",  "4497471600620003");
			logMap2.put("create_time",  DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logMap2.put("create_user",  "system");
			logMap2.put("remark",  "提现失败");
			DbUp.upTable("lc_tgz_withdraw_status_log").dataInsert(logMap2);
			//提现还原
			UpdateCustAmtInput paramInput = new UpdateCustAmtInput();
			paramInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.TXHY);
			paramInput.setCustId(custId);
			ChildOrder childOrder = new ChildOrder();
			childOrder.setChildHcoinAmt(transfer_amount);
			paramInput.getOrderList().add(childOrder);
			custRelAmtRef.updateCustAmt(paramInput);
			//发送微信通知
			WxGateSupport support = new WxGateSupport();
			String receivers = support.bConfig("groupcenter.jd_notice_receives_product");
			List<String> list = support.queryOpenId(receivers);
			for (String receiver : list) {
				support.sendWarnCountMsg("提现失败通知", "推广赚微信小程序提现失败", receiver, "失败提现申请信息：{申请编号:"+apply_code+"}");
			}
			log.error("小程序提现接口：TransferCreateOrder 调用异常！");
			
		}
		return new PaymentResult();
	}

	
	
	
	@Override
	public PaymentResult getResult() {
		return new PaymentResult();
	}
	
	/**
	 * 获取提现请求输入对象
	 */
	public static class PaymentInput extends  PayGatePreparePayProcess.PaymentInput {
		//账单号
		public String c_transfer_order;
		//转账原因
		public String c_transfer_reason;
		//收款人姓名
		public String receiveName;
		//转账金额
		public String transfer_amount;
		//小程序openId
		public String  wxOpenId;
		//申请编号
		public String  apply_code;
		//收款人编号
		public String  cust_id;
		
	}
	
	/**
	 * 获取提现参数的输出对象
	 */
	public static class PaymentResult extends PayGatePreparePayProcess.PaymentResult {}






	

}
