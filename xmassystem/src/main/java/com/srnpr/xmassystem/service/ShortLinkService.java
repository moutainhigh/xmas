package com.srnpr.xmassystem.service;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/** 
* @author Angel Joy
* @Time 2020-8-5 15:31:54 
* @Version 1.0
* <p>Description:</p>
* 短连接服务类
*/
public class ShortLinkService extends BaseClass{
	
	
	private static final char[] BASE_62_CHARS = "abcdefghzjklmnopqrstuvwxyz0123456789".toCharArray();
	private static final int BASE_62_CHARS_LENGTH = BASE_62_CHARS.length;
	private static final String key = "123456";
	
	private static Logger log = Logger.getLogger(ShortLinkService.class);
	
	/**
	 * 
	 * @param longLink
	 * @return 短连接
	 * 2020-8-6
	 * Angel Joy
	 * String
	 */
	public String turnToShort(String longLink) {
		long shortLinkStart = XmasKv.upFactory(EKvSchema.ShortLinkStart).incrBy(key, 1);
		String shortLink = formBase10(shortLinkStart);
		return shortLink;
	}
	
	public static void main(String[] args) {
//	    long startIndex= 1000000000000000l;
//	    System.out.println(formBase10(startIndex));
	    new ShortLinkService().turnToShort("");
	}
	public static String formBase10(long i){
	    StringBuffer sb = new StringBuffer();
	    if(i == 0){
	        return "a";
	    }
	    while (i > 0){
	        i = formBase10(i, sb);
	    }
	    return sb.toString();
	}
	public static long formBase10(long i, StringBuffer sb){
	    int rem = (int)(i % BASE_62_CHARS_LENGTH);
	    sb.append(BASE_62_CHARS[rem]);
	    long r = (i / BASE_62_CHARS_LENGTH);
	    return r;
	}
	
	/**
	 * 
	 * @param longLink 长连接
	 * @param operater 操作人员
	 * @param expireTime 失效时间
	 * @return
	 * 2020-8-6
	 * Angel Joy
	 * boolean
	 */
	public boolean insertIntoDb(String longLink,String operater,String expireTime) {
		boolean flag = true;
		String shortLinkKey = turnToShort(longLink);
		String shortLink = bConfig("familyhas.short_hjy")+shortLinkKey;
		MDataMap insert = new MDataMap();
		insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		insert.put("long_link", longLink);
		insert.put("short_link", shortLink);
		insert.put("short_link_key", shortLinkKey);
		insert.put("create_time", DateUtil.getSysDateTimeString());
		insert.put("expire_time", expireTime);
		insert.put("operater", operater);
		try {
			DbUp.upTable("sc_shortlink").dataInsert(insert);
		}catch(Exception e) {
			log.error("插入数据库失败："+e.getStackTrace());
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 
	 * @param longLink 长连接
	 * @param operater 操作人员
	 * @param expireTime 失效时间
	 * @return
	 * 2020-9-15
	 * zb
	 * boolean
	 */
	public String createShortLink(String longLink,String operater,String expireTime) {
		String shortLinkKey = turnToShort(longLink);
		String shortLink = bConfig("familyhas.short_hjy")+shortLinkKey;
		MDataMap insert = new MDataMap();
		insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		insert.put("long_link", longLink);
		insert.put("short_link", shortLink);
		insert.put("short_link_key", shortLinkKey);
		insert.put("create_time", DateUtil.getSysDateTimeString());
		insert.put("expire_time", expireTime);
		insert.put("operater", operater);
		insert.put("is_show", "0");
		try {
			DbUp.upTable("sc_shortlink").dataInsert(insert);
		}catch(Exception e) {
			log.error("插入数据库失败："+e.getStackTrace());
		}
		return shortLink;
	}
}
