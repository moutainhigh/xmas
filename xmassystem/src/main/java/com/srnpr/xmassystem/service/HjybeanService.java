package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.enumer.HjyBeanExecType;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrInput;
import com.srnpr.xmassystem.modelbean.HjyBeanCtrResult;
import com.srnpr.xmassystem.modelbean.HjyBeanQueryResult;
import com.srnpr.xmassystem.modelbean.HjybeanConsumeSetModel;
import com.srnpr.xmassystem.modelbean.HjybeanProduceSetModel;
import com.srnpr.xmassystem.support.PlusSupportHomehasBean;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webmodel.MOauthInfo;
import com.srnpr.zapweb.websupport.ApiCallSupport;
import com.srnpr.zapweb.websupport.OauthSupport;

/**
 * 惠家有积分(惠豆) service
 * 
 * @author fq
 *
 */
public class HjybeanService extends BaseClass {

	/**
	 * 加减惠豆
	 */
	public static final String hjyBeanTarget1 = "1001";

	/**
	 * 查询余额
	 */
	public static final String hjyBeanTarget2 = "1002";

	/**
	 * 1003是获取列表
	 */
	public static final String hjyBeanTarget3 = "1003";
	
	/**
	 * 惠豆缓存相关方法类
	 */
	private static PlusSupportHomehasBean supportBean = new PlusSupportHomehasBean();

	/**
	 * 调用惠豆公用api
	 * @param sTarget 惠豆操作方式(请使用   IN(HjyhjyBeanService.hjyBeanTarget1,HjyhjyBeanService.hjyBeanTarget2,HjyhjyBeanService.hjyBeanTarget3))
	 * @param sApiToken 用户token
	 * @param input  输入参数
	 * @param tResult  返回参数
	 * @return
	 */
	public RootResult hjyBeanApiCall( String sTarget, String sApiToken, RootInput input,
			RootResult tResult) {
		
		RootResult refundResult = null;
		
		/*
		 * 获取惠豆可用开关
		 */
		String beanOpenFlag = XmasKv.upFactory(EKvSchema.HomehasBeanConfig).get("switch");
		if(StringUtils.isNotBlank(beanOpenFlag) &&  "1".equals(beanOpenFlag)) {
			
			//校验token
			MOauthInfo upOauthInfo = new OauthSupport().upOauthInfo(sApiToken);
			if (upOauthInfo != null) {
				
				ApiCallSupport<RootInput, RootResult> apiCallSupport = new ApiCallSupport<RootInput, RootResult>();
				try {
					refundResult = apiCallSupport.doCallUserCenterApiForToken(
							bConfig("xmassystem.hjyBeanUrl"), 
							sTarget, 
							bConfig("xmassystem.hjyBeanKey"), 
							bConfig("xmassystem.hjyBeanMD5Key"), 
							sApiToken, 
							input,
							tResult);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		

		return refundResult;
	}
	
	/**
	 * 查询用户拥有的惠豆数
	 * @param membercode
	 * @return
	 */
	public Integer uphjyBeanByMemberCode(String membercode) {
		
		Integer hjyBean = 0;
		
		if(StringUtils.isNotBlank(membercode)) {
			
			MDataMap param = new MDataMap();
			param.put("manage_code", "SI2003");
			param.put("user_code", membercode);
			String sSql = "SELECT access_token FROM zapdata.za_oauth WHERE manage_code = :manage_code AND user_code = :user_code AND scope_resources != '' AND flag_enable = '1' ORDER BY zid DESC LIMIT 1;";
			Map<String, Object> dataSqlOne = DbUp.upTable("za_oauth").dataSqlOne(sSql, param);
			if(null != dataSqlOne) {
				
				String sApiToken = dataSqlOne.get("access_token").toString();
				HjyBeanQueryResult result = new HjyBeanQueryResult();
				result = (HjyBeanQueryResult) this.hjyBeanApiCall( hjyBeanTarget2, sApiToken, new RootInput(), new HjyBeanQueryResult());
				if(null != result ) {
					hjyBean = result.getRebatbalance();
				}
				
			}
			
		}
		
		return hjyBean;
	}

	/**
	 * 操作用户惠豆(增删)
	 * @param membercode
	 * @return
	 */
	public HjyBeanCtrResult ctrhjyBeanByMemberCode(String membercode,HjyBeanCtrInput input) {
		
		HjyBeanCtrResult result = null;
		if(StringUtils.isNotBlank(membercode)) {
			
			MDataMap param = new MDataMap();
			param.put("manage_code", "SI2003");
			param.put("user_code", membercode);
			String sSql = "SELECT access_token FROM zapdata.za_oauth WHERE manage_code = :manage_code AND user_code = :user_code AND scope_resources != '' AND flag_enable = '1' ORDER BY zid DESC LIMIT 1;";
			Map<String, Object> dataSqlOne = DbUp.upTable("za_oauth").dataSqlOne(sSql, param);
			if(null != dataSqlOne) {
				
				String sApiToken = dataSqlOne.get("access_token").toString();
				result = (HjyBeanCtrResult) this.hjyBeanApiCall( hjyBeanTarget1, sApiToken, input, new HjyBeanCtrResult());
				
			} else {
				result = new HjyBeanCtrResult();
				result.setResultCode(964305170);
				result.setResultMessage(bInfo(964305170));
			}
			
		}
		
		return result;
	}
	
	/**
	 * 查询惠豆消费配置
	 * @return
	 */
	public HjybeanConsumeSetModel getHomehasBeanConsumeConfig() {
		return supportBean.getHomehasBeanConsumeConfig();
	}
	
	/**
	 * 查询惠豆赠送配置
	 * @return
	 */
	public HjybeanProduceSetModel getHomehasBeanProduceConfig() {
		return supportBean.getHomehasBeanProduceConfig();
	}
	
	/**
	 * 更新惠豆消费配置
	 * @param map
	 * @return
	 */
	public boolean updateHomehasBeanConsumeConfig(MDataMap consumeConfig) {
		return supportBean.updateHomehasBeanConsumeConfig(consumeConfig);
	}
	
	/**
	 * 更新惠豆赠送配置
	 * @param map
	 * @return
	 */
	public boolean updateHomehasBeanProduceConfig(MDataMap produceConfig) {
		return supportBean.updateHomehasBeanProduceConfig(produceConfig);
	}
	
	/**
	 * 将惠豆转换为人民币（RMB）
	 * @return
	 */
	public static BigDecimal reverseHjyBeanToRMB(BigDecimal hjy_bean) {
		HjybeanConsumeSetModel homehasBeanConsumeConfig = supportBean.getHomehasBeanConsumeConfig();
		return homehasBeanConsumeConfig.getRatio().multiply(hjy_bean);
	}
	
	/**
	 * 将人民币（RMB）转换为惠豆
	 * 四舍五入
	 * @return
	 */
	public static BigDecimal reverseRMBToHjyBean(BigDecimal money) {
		HjybeanConsumeSetModel homehasBeanConsumeConfig = supportBean.getHomehasBeanConsumeConfig();
		return money.divide(homehasBeanConsumeConfig.getRatio()).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 增加惠豆相关定时任务<br>
	 * 惠豆相关逻辑废弃 20180104
	 * @param money
	 */
	@Deprecated
	public static void addHjyBeanTimer(HjyBeanExecType type, String info, String orderCode) {
		/*
		if(DbUp.upTable("fh_hd_exectimer").count("exec_type", type.getType(),"exec_info", info) == 0){
			switch (type) {
			case CANCEL:
			case RETURN_MONEY:
			case RETURN_GOODS:
				if(StringUtils.isNotBlank(orderCode)){
					// 如果未使用惠豆支付则不用添加返还惠豆的任务
					if(DbUp.upTable("oc_order_pay").count("order_code",orderCode,"pay_type","449746280015") == 0){
						return;
					}
				}
				break;
			default:
				break;
			}
			
			MDataMap data = new MDataMap();
			data.put("exec_code",WebHelper.upCode("HET"));
			data.put("exec_type",type.getType());
			data.put("exec_info",info);
			data.put("create_time",FormatHelper.upDateTime());
			data.put("exec_time",data.get("create_time"));
			data.put("remark","");
			DbUp.upTable("fh_hd_exectimer").dataInsert(data);
		}
		*/
	}
}
