package com.srnpr.xmassystem.modelevent;

import com.srnpr.xmassystem.face.IPlusModel;
import com.srnpr.xmassystem.face.IPlusModelRefresh;


public class PlusModelwxMusicAlbumToken implements IPlusModel, IPlusModelRefresh{
	
	@Override
	public boolean isRefresh() {
		return false;
	}
	
	private String access_token;
    private String expires_time;
    private String create_time;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
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
