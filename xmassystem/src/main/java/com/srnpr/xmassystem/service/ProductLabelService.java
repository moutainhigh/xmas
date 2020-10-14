package com.srnpr.xmassystem.service;

import java.text.ParseException;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadProductLabelInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabelsQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

public class ProductLabelService {
	
	private PlusServiceSeller pss = new PlusServiceSeller();
	
	/**
	 * @descriptions 获取商品标签图片，如果获取到的图片为空，跨境通商品取默认的跨境图标
	 *  
	 * @param productCode
	 * @return
	 * 
	 * @refactor 根据3.9.6的需求，该标签只有在start_time和end_time之间才显示。
	 * 					如果不在有效时间内，则不显示
	 * @author Yangcl
	 * @date 2016-5-9-下午6:16:38
	 * @version 1.0.0.1
	 */
	public PlusModelProductLabel getLabelInfo(String productCode){
		// 缓存获取商品信息
//		PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
		PlusModelProductLabel labelInfo = new PlusModelProductLabel();
//		if (null != plusModelProductinfo) {
//			if (null!=plusModelProductinfo.getLabelsList() && plusModelProductinfo.getLabelsList().size()>0) {
//				labelInfo = new LoadProductLabelInfo().upInfoByCode(new PlusModelProductLabelsQuery(plusModelProductinfo.getLabelsList().get(0)));
//				if(null == labelInfo){
//					labelInfo = new PlusModelProductLabel();  // 如果不在有效时间内，则不显示
//				}
//				String currentTime_ = DateUtil.getSysDateTimeString();
//				String startTime_ = labelInfo.getStartTime(); 
//				String endTime_ = labelInfo.getEndTime();    
//				if(StringUtils.isBlank(startTime_) || StringUtils.isBlank(endTime_)){
//					labelInfo = new PlusModelProductLabel();  // 如果是脏数据，则不显示
//				}else if( this.compareDate(startTime_ , currentTime_) || this.compareDate(currentTime_ , endTime_)){ 
//					labelInfo = new PlusModelProductLabel();  // 如果不在有效时间内，则不显示
//				}
//			}
//			
//			//为跨境商品添加默认标签图片
//			if (StringUtils.isNotEmpty(plusModelProductinfo.getSmallSellerCode()) && pss.isKJSeller(plusModelProductinfo.getSmallSellerCode())) {
//				if(StringUtils.isEmpty(labelInfo.getInfoPic())){
//					labelInfo.setInfoPic(TopUp.upConfig("xmasproduct.infoLabelsPic"));
//				}
//				if(StringUtils.isEmpty(labelInfo.getListPic())){
//					// 屏蔽小飞机图标，防止和主图上面的标签冲突
//					String[] days = StringUtils.trimToEmpty(TopConfig.Instance.bConfig("xmassystem.flagthesea_disabled_days")).split(",");
//					boolean isHide = ArrayUtils.contains(days, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
//					if(!isHide){
//						labelInfo.setListPic(TopUp.upConfig("xmasproduct.listLabelsPic"));
//					}
//				}
//			}
//		}
//		
		List<PlusModelProductLabel> list = getLabelInfoList(productCode);
		return list.isEmpty() ? labelInfo : list.get(0);
	}
	
	public List<PlusModelProductLabel> getLabelInfoList(String productCode){
		// 缓存获取商品信息
		PlusModelProductInfo plusModelProductinfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
		List<PlusModelProductLabel> list = new ArrayList<PlusModelProductLabel>();		
		if (null != plusModelProductinfo) {
			boolean hasLeftTop = false;
			if (null!=plusModelProductinfo.getLabelsList() && plusModelProductinfo.getLabelsList().size()>0) {
				for(String code:plusModelProductinfo.getLabelsList()){
					PlusModelProductLabel labelInfo = new PlusModelProductLabel();
					labelInfo = new LoadProductLabelInfo().upInfoByCode(new PlusModelProductLabelsQuery(code));
					if(null == labelInfo){
						continue;  // 如果不在有效时间内，则不显示
					}
					if(StringUtils.isBlank(labelInfo.getUpdateTime())||StringUtils.isBlank(labelInfo.getExcludeLive())){
						labelInfo = new LoadProductLabelInfo().upInfoByCode(new PlusModelProductLabelsQuery(code));
					}
					String currentTime_ = DateUtil.getSysDateTimeString();
					String startTime_ = labelInfo.getStartTime(); 
					String endTime_ = labelInfo.getEndTime();    
					if(StringUtils.isBlank(startTime_) || StringUtils.isBlank(endTime_)){
						continue;  // 如果是脏数据，则不显示
					}else if( this.compareDate(startTime_ , currentTime_) || this.compareDate(currentTime_ , endTime_)){ 
						continue;  // 如果不在有效时间内，则不显示
					}
					//566排除直播品
					if(labelInfo.getExcludeLive().equals("449747110002")){
						List<Map<String, Object>> list2 = DbUp.upTable("pc_tv").dataSqlList("SELECT * from pc_tv pt where pt.form_fr_date >= CURRENT_DATE() AND pt.form_end_date < DATE_FORMAT(CURRENT_DATE()+1,'%Y-%m-%d') AND pt.good_id = '"+productCode+"'", null);
						if(list2.size()>0){
							continue;
						}
					}
					
					try {
						PlusVeryImage p = new PlusVeryImage();
						Map<String,Integer> retMap = p.getImagesKg(labelInfo.getListPic());
						labelInfo.setWidth(retMap.get("width")*1L);
						labelInfo.setHeight(retMap.get("height")*1L);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					boolean flag = true;
					if(labelInfo.getLabelPosition().equals("449748430005")){
						Iterator<PlusModelProductLabel> iterator = list.iterator();
						while (iterator.hasNext()) {
							PlusModelProductLabel info = (PlusModelProductLabel) iterator.next();
							if(info.getLabelPosition().equals(labelInfo.getLabelPosition())&&this.compareDate(info.getStartTime(), labelInfo.getStartTime())){
								flag = false;
							}else{
								if(info.getLabelPosition().equals("449748430005")){
									iterator.remove();
								}
								
							}
						}
					}
					if(flag){
						list.add(labelInfo);
						
						// 包含左上标签
						if(labelInfo.getLabelPosition().equals("449748430001")){
							hasLeftTop = true;
						}
					}					
				}
			}
			
			// 没有设置左上标签时查询一下是否有默认左上标签
			if(!hasLeftTop) {
				PlusModelProductLabel labelInfo = new PlusModelProductLabel();
				//为跨境商品添加默认标签图片
				if (StringUtils.isNotEmpty(plusModelProductinfo.getSmallSellerCode()) && pss.isKJSeller(plusModelProductinfo.getSmallSellerCode())) {
					if(StringUtils.isEmpty(labelInfo.getInfoPic())){
						labelInfo.setInfoPic(TopUp.upConfig("xmasproduct.infoLabelsPic"));						
					}
					if(StringUtils.isEmpty(labelInfo.getListPic())){
						// 屏蔽小飞机图标，防止和主图上面的标签冲突
						String[] days = StringUtils.trimToEmpty(TopConfig.Instance.bConfig("xmassystem.flagthesea_disabled_days")).split(",");
						boolean isHide = ArrayUtils.contains(days, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
						if(!isHide){
							labelInfo.setListPic(TopUp.upConfig("xmasproduct.listLabelsPic"));
							labelInfo.setWidth(69);
							labelInfo.setHeight(69);
						}
					}
					labelInfo.setLabelPosition("449748430001");
					list.add(labelInfo);
				}
			}
		}
		
		Collections.sort(list, new Comparator<PlusModelProductLabel>() {

			@Override
			public int compare(PlusModelProductLabel o1, PlusModelProductLabel o2) {
				// TODO Auto-generated method stub
				try {
					long diff = FormatHelper.parseDate(StringUtils.isBlank(o2.getUpdateTime())?"1900-01-01 00:00:00":o2.getUpdateTime()).getTime()-FormatHelper.parseDate(StringUtils.isBlank(o1.getUpdateTime())?"1900-01-01 00:00:00":o1.getUpdateTime()).getTime();
					if(diff>0){
						return 1;
					}
					if(diff<0){
						return -1;
					}
				} catch (ParseException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
			
		});
		return list;
	}

	
	/**
	 * @descriptions 比较两个时间的大小
	 *  	如果两个时间相等则返回0
	 * @param a not null
	 * @param b not null 
	 * @return boolean 
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-5-下午2:52:13
	 * @version 1.0.0.1
	 */
	private boolean compareDate(String a, String b) {
	    return a.compareTo(b) > 0;
	}
}