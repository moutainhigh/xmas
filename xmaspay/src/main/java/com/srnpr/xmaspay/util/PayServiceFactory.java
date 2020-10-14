package com.srnpr.xmaspay.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;

import com.srnpr.xmaspay.PaymentProcess;
import com.srnpr.xmaspay.common.PayProcessEnum;
import com.srnpr.xmaspay.process.IAlipayCustomsProcess;
import com.srnpr.xmaspay.process.IAlipayTradeCancelProcess;
import com.srnpr.xmaspay.process.IApplePayCallBackProcess;
import com.srnpr.xmaspay.process.IApplePayProcess;
import com.srnpr.xmaspay.process.IPayCustomsProcess;
import com.srnpr.xmaspay.process.IPayGateWayCallBackProcess;
import com.srnpr.xmaspay.process.IPayGateWayProcess;
import com.srnpr.xmaspay.process.IUnionPayCallBackProcess;
import com.srnpr.xmaspay.process.IUnionPayProcess;
import com.srnpr.xmaspay.process.IWechatCustomProcess;
import com.srnpr.xmaspay.process.IWechatTradeCancelProcess;
import com.srnpr.xmaspay.process.notify.ApplePayNotifyPayProcess;
import com.srnpr.xmaspay.process.notify.PayGateNotifyPayProcess;
import com.srnpr.xmaspay.process.prepare.AlipayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.ApplePayPreparePayProcess;
import com.srnpr.xmaspay.process.prepare.WechatPrepareCashProcess;
import com.srnpr.xmaspay.process.prepare.WechatPreparePayProcess;
import com.srnpr.xmaspay.process.refund.ApplePayRefundReqProcess;
import com.srnpr.xmaspay.process.refund.PayGateRefundReqProcess;
import com.srnpr.xmaspay.service.IOrderCustomsService;
import com.srnpr.xmaspay.service.IOrderPayService;
import com.srnpr.xmaspay.service.ITradeCancelLogService;
import com.srnpr.zapcom.baseface.IBaseInstance;
import com.srnpr.zapcom.basehelper.BeansHelper;

/**
 * 支付业务处理工厂
 * @author pang_jhui
 *
 */
public class PayServiceFactory implements IBaseInstance {
	
	/*支付相关业务处理实例*/
	private volatile static PayServiceFactory INSTANCE = new PayServiceFactory();
	
	private static Map<String,Object> beanMap = new ConcurrentHashMap<String, Object>();
	
	/**
	 * 获取支付相关业务处理实例
	 * @return
	 */
	public static PayServiceFactory getInstance(){
		return INSTANCE;		
	}	
	
	/**
	 * 获取spring bean 对象
	 * @param name
	 * 		对象名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String name){
			
		return (T)BeansHelper.upBean(name);
		
	}
	
	/*支付网关解析*/
	private IPayGateWayProcess payGateWayProcess = null;
	
	/*支付网关回调*/
	private IPayGateWayCallBackProcess payGateWayCallBackProcess = null;
	
	/*订单支付相关业务处理*/
	private IOrderPayService orderPayService = null;
	
	/*交易取消日志记录*/
	private ITradeCancelLogService tradeCancelLogService = null;
	
	/*微信交易取消处理*/
	private IWechatTradeCancelProcess wechatTradeCancelProcess = null;
	
	/*支付宝交易取消处理*/
	private IAlipayTradeCancelProcess alipayTradeCancelProcess = null;
	
	/*银联支付解析*/
	private IUnionPayProcess unionPayProcess = null;
	
	/*applePay支付解析*/
	private IApplePayProcess applePayProcess = null;
	
	/*银联支付回调*/
	private IUnionPayCallBackProcess unionPayCallBackProcess = null;
	
	/*applePay支付回调*/
	private IApplePayCallBackProcess applePayCallBackProcess = null;
	
	/*微信报关业务处理*/
	private IWechatCustomProcess wechatCustomProcess = null;
	
	/*支付宝报关业务处理*/
	private IAlipayCustomsProcess alipayCustomsProcess = null;
	
	/*报关成功信息处理*/
	private IOrderCustomsService orderCustomsService = null;
	
	/*报关成功信息处理*/
	private IPayCustomsProcess payCustomsProcess = null;

	/**
	 * 获取支付网关解析类
	 * @return
	 */
	public IPayGateWayProcess getPayGateWayProcess() {
		
		if(payGateWayProcess == null){
			
			payGateWayProcess = getBean(PayProcessEnum.payGateWayProcess.name());
			
		}
		
		return payGateWayProcess;
		
	}
	
	/**
	 * 支付网关回调
	 * @return
	 */
	public IPayGateWayCallBackProcess getPayGateWayCallBackProcess() {
		
		if(payGateWayCallBackProcess == null){
			
			payGateWayCallBackProcess = getBean(PayProcessEnum.payGateWayCallBackProcess.name());
			
		}
		
		return payGateWayCallBackProcess;
		
	}

	/**
	 * 订单支付相关
	 * @return
	 */
	public IOrderPayService getOrderPayService() {
		
		if(orderPayService == null){
			
			orderPayService = getBean(PayProcessEnum.orderPayService.name());
			
		}
		
		return orderPayService;
	}
	
	/**
	 * 获取交易取消日志记录
	 * @return 交易取消日志记录
	 */
	public ITradeCancelLogService getTradeCancelLogService(){
		
		if(tradeCancelLogService == null){
			
			tradeCancelLogService = getBean(PayProcessEnum.tradeCancelLogService.name());
			
		}
		
		return tradeCancelLogService;
		
	}

	/**
	 * 获取微信交易取消解析
	 * @return 微信交易取消解析类
	 */
	public IWechatTradeCancelProcess getWechatTradeCancelProcess() {
		
		if(wechatTradeCancelProcess == null){
			
			wechatTradeCancelProcess = getBean(PayProcessEnum.wechatTradeCancelProcess.name());
			
		}
		
		return wechatTradeCancelProcess;
	}

	/**
	 * 获取支付宝交易取消解析
	 * @return 支付宝交易取消解析类
	 */
	public IAlipayTradeCancelProcess getAlipayTradeCancelProcess() {
		
		if(alipayTradeCancelProcess == null){
			
			alipayTradeCancelProcess = getBean(PayProcessEnum.alipayTradeCancelProcess.name());
			
		}
		
		return alipayTradeCancelProcess;
	}

	/**
	 * 获取银联支付解析类
	 * @return
	 */
	public IUnionPayProcess getUnionPayProcess() {
		
		if(unionPayProcess == null){
			
			unionPayProcess = getBean(PayProcessEnum.unionPayProcess.name());
			
		}
		
		return unionPayProcess;
	}
	
	/**
	 * 获取applePay支付解析
	 * @return
	 */
	public IApplePayProcess getApplePayProcess() {
		
		if(applePayProcess == null){
			
			applePayProcess = getBean(PayProcessEnum.applePayProcess.name());
			
		}
		
		return applePayProcess;
	}

	/**
	 * 获取银联支付回调解析
	 * @return
	 */
	public IUnionPayCallBackProcess getUnionPayCallBackProcess() {
		
		if(unionPayCallBackProcess == null){
			
			unionPayCallBackProcess = getBean(PayProcessEnum.unionPayCallBackProcess.name());
			
		}                                 
		
		return unionPayCallBackProcess;
	}

	/**
	 * 获取applePay回调解析
	 * @return
	 */
	public IApplePayCallBackProcess getApplePayCallBackProcess() {
		
		if(applePayCallBackProcess == null){
			
			applePayCallBackProcess = getBean(PayProcessEnum.applePayCallBackProcess.name());
			
		}                                 
		
		return applePayCallBackProcess;
	}

	/**
	 * 微信报关解析
	 * @return
	 */
	public IWechatCustomProcess getWechatCustomProcess() {
		
		if(wechatCustomProcess == null){
			
			wechatCustomProcess = getBean(PayProcessEnum.wechatCustomProcess.name());
			
		}
		
		return wechatCustomProcess;
	}	
	
	/**
	 * 支付宝报关解析
	 * @return
	 */
	public IAlipayCustomsProcess getAlipayCustomsProcess() {
		
		if(alipayCustomsProcess == null){
			
			alipayCustomsProcess = getBean(PayProcessEnum.alipayCustomsProcess.name());
			
		}
		
		return alipayCustomsProcess;
	}	

	/**
	 * 订单报关业务处理
	 * @return
	 */
	public IOrderCustomsService getOrderCustomsService() {
		
		if(orderCustomsService == null){
			
			orderCustomsService = getBean(PayProcessEnum.orderCustomsService.name());
			
		}
		
		return orderCustomsService;
	}	
	
	/**
	 * 订单报关业务统一入口实现
	 * @return
	 */
	public IPayCustomsProcess getPayCustomsProcess() {
		
		if(payCustomsProcess == null){
			
			payCustomsProcess = getBean(PayProcessEnum.payCustomsProcess.name());
			
		}
		
		return payCustomsProcess;
	}	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> cls){
		try {
			if(beanMap.get(cls.getName()) == null){
				beanMap.put(cls.getName(), BeansHelper.upBean(cls.getSimpleName()));
			}
		} catch (BeansException e) {
			//e.printStackTrace();
		}
		return (T)beanMap.get(cls.getName());
	}
	
	public PaymentProcess<AlipayPreparePayProcess.PaymentInput,AlipayPreparePayProcess.PaymentResult> getAlipayPreparePayProcess(){
		return getBean(AlipayPreparePayProcess.class);
	}
	
	public PaymentProcess<WechatPreparePayProcess.PaymentInput,WechatPreparePayProcess.PaymentResult> getWechatPreparePayProcess(){
		return getBean(WechatPreparePayProcess.class);
	}
	
	public PaymentProcess<ApplePayPreparePayProcess.PaymentInput,ApplePayPreparePayProcess.PaymentResult> getApplePayPreparePayProcess(){
		return getBean(ApplePayPreparePayProcess.class);
	}
	
	public PaymentProcess<PayGateNotifyPayProcess.PaymentInput,PayGateNotifyPayProcess.PaymentResult> getPayGateNotifyPayProcess(){
		return getBean(PayGateNotifyPayProcess.class);
	}
	
	public PaymentProcess<ApplePayNotifyPayProcess.PaymentInput,ApplePayNotifyPayProcess.PaymentResult> getApplePayNotifyPayProcess(){
		return getBean(ApplePayNotifyPayProcess.class);
	}
	
	public PaymentProcess<PayGateRefundReqProcess.PaymentInput,PayGateRefundReqProcess.PaymentResult> getPayGateRefundReqProcess(){
		return getBean(PayGateRefundReqProcess.class);
	}
	
	public PaymentProcess<ApplePayRefundReqProcess.PaymentInput,ApplePayRefundReqProcess.PaymentResult> getApplePayRefundReqProcess(){
		return getBean(ApplePayRefundReqProcess.class);
	}
	//微信小程序提现
	public PaymentProcess<WechatPrepareCashProcess.PaymentInput,WechatPrepareCashProcess.PaymentResult> getWechatPrepareCashProcess(){
		return getBean(WechatPrepareCashProcess.class);
	}
}
