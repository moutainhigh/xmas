package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;

/**
 * 京东token实体bean
 * @author 12154
 *
 */
public class PlusModelJDToken implements IPlusModel, IPlusModelRefresh{
	
	@Override
	public boolean isRefresh() {
		return false;
	}
	
	private String access_token;
    private String code;
    private int expires_in;
    private String refresh_token;
    private String time;
    private String token_type;
    private String jd_uid;
    private String user_nick;
    private String expires_time;
    private String create_time;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getJd_uid() {
		return jd_uid;
	}
	public void setJd_uid(String jd_uid) {
		this.jd_uid = jd_uid;
	}
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}
	public String getExpires_time() {
		return expires_time;
	}
	public void setExpires_time(String expires_time) {
		this.expires_time = expires_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

    

}
