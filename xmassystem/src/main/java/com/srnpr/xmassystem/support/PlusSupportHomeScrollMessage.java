package com.srnpr.xmassystem.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.modelproduct.PlusModelHomeScrollMessage;
import com.srnpr.xmassystem.modelproduct.PlusModelScrollMessage;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MFileItem;

/**
 * 首页滚动消息
 * @remark 
 * @author 任宏斌
 * @date 2019年8月22日
 */
public class PlusSupportHomeScrollMessage {

	/**
	 * 加载首页滚动消息
	 * @return
	 */
	public PlusModelHomeScrollMessage initHomeScrollMessageFromDb() {

		PlusModelHomeScrollMessage result = new PlusModelHomeScrollMessage();

		//取当前时间所在区间
		String dateTimeSection = getDateTimeSection();
		result.setEachTime(Integer.parseInt(TopUp.upConfig("familyhas.home_each_time_"+dateTimeSection)));
		result.setIntervalTime(Integer.parseInt(TopUp.upConfig("familyhas.home_interval_time_"+dateTimeSection)));
		
		String sSql = "select i.order_code,ml.login_name,IFNULL(m.nickname,CONCAT(SUBSTRING(ml.login_name,1,3),'****',SUBSTRING(ml.login_name,8,4))) nick_name,"
				+ "a.area_code from ordercenter.oc_orderinfo i "
				+ "LEFT JOIN membercenter.mc_member_sync m ON i.buyer_code = m.member_code "
				+ "LEFT JOIN membercenter.mc_login_info ml ON i.buyer_code = ml.member_code "
				+ "LEFT JOIN ordercenter.oc_orderadress a on i.order_code=a.order_code "
				+ "WHERE i.order_status in ('4497153900010001','4497153900010002') order by i.zid desc limit "
				+ TopUp.upConfig("familyhas.home_scroll_message_count");
		List<Map<String, Object>> list = DbUp.upTable("oc_orderinfo").dataSqlList(sSql, new MDataMap());
		if(null != list && !list.isEmpty()) {
			for (Map<String, Object> orderInfo : list) {
				PlusModelScrollMessage scrollMessage = new PlusModelScrollMessage();
				
				String orderCode = orderInfo.get("order_code") + "";
				String loginName = orderInfo.get("login_name") + "";
				String nickName = orderInfo.get("nick_name") + "";
				String areaCode = orderInfo.get("area_code") + "";
				
				String sql = "select product_code,product_picurl from ordercenter.oc_orderdetail where order_code=:order_code limit 1";
				Map<String, Object> detail = DbUp.upTable("oc_orderdetail").dataSqlOne(sql, new MDataMap("order_code", orderCode));
				if(null != detail) {
					//商品图按规则压缩
      				String picurl = detail.get("product_picurl") + "";
      				if(StringUtils.isNotEmpty(picurl)) {
      					int width = Integer.parseInt(TopUp.upConfig("familyhas.home_scroll_image_width"));
      					Map<String, MFileItem> upImageZoom = new PlusVeryImage().upImageZoom(picurl, width);
      					MFileItem mFileItem = upImageZoom.get(picurl);
      					if(null!=mFileItem) {
      						String fileUrl = mFileItem.getFileUrl();
      						scrollMessage.setPic(fileUrl);
      					}
      				}
					scrollMessage.setProductCode(detail.get("product_code") + "");
				}
				
				//一级地址 优先查临时表 若没有则递归查询地址编号表
				MDataMap one = DbUp.upTable("sc_tmp_tmp").one("fourth_code", areaCode);
				if(null != one && one.containsKey("first_name")) {
					scrollMessage.setAddress(one.get("first_name"));
				}else {
					scrollMessage.setAddress(getFirstAddressName(areaCode));
				}
				
				//昵称
				if(StringUtils.isEmpty(nickName)) {
					scrollMessage.setNickName(loginName.substring(0,3) + "****" + loginName.substring(7));
				}else {
					scrollMessage.setNickName(nickName);
				}
				
				//消息
				int count = DbUp.upTable("oc_order_activity").count("order_code", orderCode, "activity_type", "4497472600010024");
				if(count>0) {
					MDataMap item = DbUp.upTable("sc_event_collage_item").one("collage_ord_code", orderCode);
					if("449748310001".equals(item.get("collage_member_type"))) {
						//发起拼团
						scrollMessage.setMessage(TopUp.upConfig("familyhas.home_collage_sponsor"));
					}else {
						//加入拼团
						scrollMessage.setMessage(TopUp.upConfig("familyhas.home_collage_join"));
					}
				}else {
					//订购成功
					scrollMessage.setMessage(TopUp.upConfig("familyhas.home_create_order_success"));
				}
				
				result.getMessageList().add(scrollMessage);
			}
		}
		
		return result;
	}
	
	/**
	 * 取时间段
	 * @return
	 */
	private String getDateTimeSection() {
		String result = "";
		
		int now = DateUtil.getCurrentHour();
		if(now >= 6 && now < 9) {
			result = "6to9";
		}else if(now >= 9 && now < 19) {
			result = "9to19";
		}else if(now >= 19 && now < 22) {
			result = "19to22";
		}else if(now >=22 || now < 6) {
			result = "22to6";
		}
		
		return result;
	}

	/**
	 * 根据惠家有areaCode查一级地址中文
	 * @param areaCode
	 * @return
	 */
	private String getFirstAddressName(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		
		List<MDataMap> list = new ArrayList<MDataMap>();
		// 递归获取本级以及父级地址
		getHjyAddressAndParent(list, areaCode);
		
		String hjyAddressName = "";
		for (int i = 0; i < list.size(); i ++) {
			if("Y".equals(list.get(i).get("show_yn")) && "1".equals(list.get(i).get("code_lvl"))){
				hjyAddressName =list.get(i).get("name");
				break;
			}
		}
		return hjyAddressName;
	}
	
	private void getHjyAddressAndParent(List<MDataMap> list, String areaCode) {
		// 如果地址编码已经获取过了则直接返回
		for(MDataMap map : list) {
			if(map.get("code").equals(areaCode)) {
				return;
			}
		}
		
		List<MDataMap> res = DbUp.upTable("sc_tmp").queryAll("code,name,p_code,code_lvl,show_yn", "", "", new MDataMap("code", areaCode));
		if(!res.isEmpty()) {
			list.add(res.get(0));
			
			// 如果有父级则递归获取
			if(StringUtils.isNotBlank(res.get(0).get("p_code"))) {
				getHjyAddressAndParent(list,res.get(0).get("p_code"));
			}
		}
	}
}
