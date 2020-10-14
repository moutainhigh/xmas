package com.srnpr.xmassystem.load;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.face.IPlusConfig;
import com.srnpr.xmassystem.modelevent.PlusModelEventGoodsProduct;
import com.srnpr.xmassystem.modelevent.PlusModelGoodsProduct;
import com.srnpr.xmassystem.modelevent.PlusModelSaleQuery;
import com.srnpr.xmassystem.plusconfig.PlusConfigGoodsProduct;
import com.srnpr.xmassystem.top.LoadTopMain;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/***
 * 拼好货往缓存写入的方法封装
 * @author zhouguohui
 *
 */
public class LoadGoodsProduct extends LoadTopMain<PlusModelEventGoodsProduct, PlusModelSaleQuery>{

	public PlusModelEventGoodsProduct topInitInfo(PlusModelSaleQuery tQuery) {
		PlusModelEventGoodsProduct pmep = new  PlusModelEventGoodsProduct();
		if(tQuery==null){
			return pmep;
		}
		
		getGoodsProduct(pmep,tQuery.getCode());
		
		return pmep;
	}

	private final static PlusConfigGoodsProduct PLUS_CONFIG = new PlusConfigGoodsProduct();
	
	public IPlusConfig topConfig() {
		return PLUS_CONFIG;
	}
	
	
	/**
	 * 数据初始化方法
	 * @param pmep        实体类
	 * @param sellerCode  系统编号
	 */
	private void getGoodsProduct(PlusModelEventGoodsProduct pmep,String sellerCode){
		String sql = "SELECT sei.*,sec.product_code,sec.sku_code,sec.sku_name,sec.favorable_price,sec.selling_price,sec.purchase_num FROM "+
					 " sc_event_info sei , sc_event_conglobation sec WHERE sei.seller_code =:sellerCode AND (sei.event_status = '4497472700020002' OR sei.event_status = '4497472700020004')"+
					 " AND sei.event_type_code = '4497472600010016' AND  sei.end_time>now() AND sei.event_code = sec.event_code "+
					 " ORDER BY sei.begin_time ASC, sei.zid DESC";
		List<Map<String, Object>> listGoodsProduct = DbUp.upTable("sc_event_info").dataSqlList(sql, new MDataMap("sellerCode",sellerCode));
		List<PlusModelGoodsProduct> goodsProduct=new ArrayList<PlusModelGoodsProduct>();
		if(listGoodsProduct!=null && listGoodsProduct.size()>0){
			for(Map<String, Object> map : listGoodsProduct){
				PlusModelGoodsProduct pmgp = new PlusModelGoodsProduct();
				pmgp.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				pmgp.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				pmgp.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				pmgp.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				pmgp.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				pmgp.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				pmgp.setProductCode(map.get("product_code")==null?"":map.get("product_code").toString());
				pmgp.setSkuCode(map.get("sku_code")==null?"":map.get("sku_code").toString());
				pmgp.setSkuName(map.get("sku_name")==null?"":map.get("sku_name").toString());
				pmgp.setFavorablePrice(map.get("favorable_price")==null?"":map.get("favorable_price").toString());
				pmgp.setSellingPrice(map.get("selling_price")==null?"":map.get("selling_price").toString());
				pmgp.setPurchaseNum(Integer.parseInt(map.get("purchase_num")==null?"0":map.get("purchase_num").toString()));
				
				goodsProduct.add(pmgp);
			}
			
		}
		pmep.setGoodsProduct(goodsProduct);
		
	}


	@Override
	public PlusModelEventGoodsProduct topInitInfoMain(PlusModelSaleQuery tQuery) {
		PlusModelEventGoodsProduct pmep = new  PlusModelEventGoodsProduct();
		
		String sellerCode = tQuery.getCode();
		String sql = "SELECT sei.*,sec.product_code,sec.sku_code,sec.sku_name,sec.favorable_price,sec.selling_price,sec.purchase_num FROM "+
					 " sc_event_info sei , sc_event_conglobation sec WHERE sei.seller_code =:sellerCode AND (sei.event_status = '4497472700020002' OR sei.event_status = '4497472700020004')"+
					 " AND sei.event_type_code = '4497472600010016' AND  sei.end_time>now() AND sei.event_code = sec.event_code "+
					 " ORDER BY sei.begin_time ASC, sei.zid DESC";
		List<Map<String, Object>> listGoodsProduct = DbUp.upTable("sc_event_info").dataSqlPriLibList(sql, new MDataMap("sellerCode",sellerCode));
		List<PlusModelGoodsProduct> goodsProduct=new ArrayList<PlusModelGoodsProduct>();
		if(listGoodsProduct!=null && listGoodsProduct.size()>0){
			for(Map<String, Object> map : listGoodsProduct){
				PlusModelGoodsProduct pmgp = new PlusModelGoodsProduct();
				pmgp.setEventCode(map.get("event_code")==null?"":map.get("event_code").toString());
				pmgp.setEventName(map.get("event_name")==null?"":map.get("event_name").toString());
				pmgp.setBeginTime(map.get("begin_time")==null?"":map.get("begin_time").toString());
				pmgp.setEndTime(map.get("end_time")==null?"":map.get("end_time").toString());
				pmgp.setEventType(map.get("event_type_code")==null?"":map.get("event_type_code").toString());
				pmgp.setEventStatus(map.get("event_status")==null?"":map.get("event_status").toString());
				pmgp.setProductCode(map.get("product_code")==null?"":map.get("product_code").toString());
				pmgp.setSkuCode(map.get("sku_code")==null?"":map.get("sku_code").toString());
				pmgp.setSkuName(map.get("sku_name")==null?"":map.get("sku_name").toString());
				pmgp.setFavorablePrice(map.get("favorable_price")==null?"":map.get("favorable_price").toString());
				pmgp.setSellingPrice(map.get("selling_price")==null?"":map.get("selling_price").toString());
				pmgp.setPurchaseNum(Integer.parseInt(map.get("purchase_num")==null?"0":map.get("purchase_num").toString()));
				goodsProduct.add(pmgp);
			}
		}
		pmep.setGoodsProduct(goodsProduct);
		
		return pmep;
	}

}
