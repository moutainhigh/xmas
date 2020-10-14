package com.srnpr.xmaspay.util;

import org.apache.commons.lang.StringUtils;
import com.srnpr.xmaspay.common.Constants;
import com.srnpr.zapweb.webdo.WebTemp;

/**
 * 参数字典管理
 * @author pang_jhui
 *
 */
public class DictManager {
	
	/**
	 * zw_define定义信息
	 * @param parentId
	 * 		父类标识
	 * @param defineName
	 * 		定义编码
	 * @return MDataMap
	 */
	public static String getZWDefineNote(String parentId, String defineName){
		
		return  WebTemp.upTempDataOne("zw_define","define_note","define_name",defineName,"parent_did",parentId);
		
	}
	
	/**
	 * zw_define定义信息
	 * @param parentId
	 * 		父类标识
	 * @param defineNote
	 * 		定义编码
	 * @return MDataMap
	 */
	public static String getZWDefineName(String parentId, String defineNote){
		
		return  WebTemp.upTempDataOne("zw_define","define_name","define_note",defineNote,"parent_did",parentId);
		
	}
	
	/**
	 * zw_define定义did
	 * @param parentId
	 * 		父类标识
	 * @param defineNote
	 * 		定义名称
	 * @return 定义标识
	 */
	public static String getZWDefineDids(String parentId, String defineNote){
		
		return  WebTemp.upTempDataOne("zw_define","define_dids","define_note",defineNote,"parent_did",parentId);
		
	}
	
	/**
	 * 获取定义信息备注
	 * @param parentId
	 * 		父类标识
	 * @param defineName
	 * 		定义名称
	 * @return 定义备注
	 */
	public static String getZWDefineRemark(String parentId, String defineName){
		
		return  WebTemp.upTempDataOne("zw_define","define_remark","define_name",defineName,"parent_did",parentId);
		
	}
	
	/**
	 * 获取支付接口编码
	 * @param defineName
	 * 		定义名称
	 * @return 支付接口编码
	 */
	public static int getPayGateCode(String defineName){
		
		int code = 0;
		
		String defineNote = getZWDefineNote(Constants.ZW_DEFINE_CODE_PAYGATE, defineName);
		
		if(StringUtils.isNotBlank(defineNote)){
			
			code = Integer.parseInt(defineNote);
			
		}
		
		
		return code;		
		
	}
	
	/**
	 * 获取支付网关名称
	 * @param payGate
	 * 		支付网关编码
	 * @return 支付网关名称
	 */
	public static String getPayGateName(int payGate){
		
		String defineNote = getZWDefineName(Constants.ZW_DEFINE_CODE_PAYGATE, Integer.toString(payGate));		
		
		return defineNote;		
		
	}
	
	/**
	 * 获取支付网关子信息备注
	 * @param defineNote
	 * 		支付网关中文名称
	 * @param defineName
	 * 		支付网关子信息名称
	 * @return 支付网关自信备注
	 */
	public static String getPayGateChildInfo(String defineNote,String defineName){
		
		String defineDids = getZWDefineDids(Constants.ZW_DEFINE_CODE_PAYGATE, defineNote);
		
		String defineRemark = getZWDefineRemark(defineDids, defineName);
		
		return defineRemark;
		
	}
	
	
	

}
