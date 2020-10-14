package com.srnpr.xmassystem.homehas;

public class GetCustLevel extends XmasRsyncHomeHas<GetCustLevel.IRsyncRequest, GetCustLevel.IRsyncResponse>{

	private IRsyncRequest req = new IRsyncRequest();
	
	@Override
	public String upRequestUrl() {
		//return bConfig("xmassystem.rsync_homehas_url_cust_level") + getRsyncTarget();
		return bConfig("groupcenter.rsync_homehas_url") + getRsyncTarget();
	}

	@Override
	public String getRsyncTarget() {
		return "getCustLvlByMobile";
	}

	@Override
	public IRsyncRequest upRsyncRequest() {
		return req;
	}

	@Override
	public IRsyncResponse upResponseObject() {
		return new IRsyncResponse();
	}

	public static class IRsyncRequest implements XmasRsyncHomeHas.IRsyncRequest{
		private String mobile;

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		
	}
	
	public static class IRsyncResponse implements XmasRsyncHomeHas.IRsyncResponse{
		private String cust_id;
		private String custlvl;
		private boolean success;
		
		public String getCust_id() {
			return cust_id;
		}
		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}
		public String getCustlvl() {
			return custlvl;
		}
		public void setCustlvl(String custlvl) {
			this.custlvl = custlvl;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		
	}
	
	public static void main(String[] args) {
		GetCustLevel custLevel = new GetCustLevel();
		custLevel.upRsyncRequest().setMobile("15111122288");
		custLevel.doRsync();
	}
}
