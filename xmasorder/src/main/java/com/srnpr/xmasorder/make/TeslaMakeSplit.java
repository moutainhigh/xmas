package com.srnpr.xmasorder.make;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmasorder.enumer.ETeslaExec;
import com.srnpr.xmasorder.model.TeslaModelJdOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelOrderDetail;
import com.srnpr.xmasorder.model.TeslaModelOrderInfo;
import com.srnpr.xmasorder.model.TeslaModelShowGoods;
import com.srnpr.xmasorder.model.TeslaModelSubRole;
import com.srnpr.xmasorder.model.kaola.KaolaGoods;
import com.srnpr.xmasorder.model.kaola.Packages;
import com.srnpr.xmasorder.top.TeslaTopOrderMake;
import com.srnpr.xmasorder.x.TeslaXOrder;
import com.srnpr.xmasorder.x.TeslaXResult;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.PlusServiceSeller;
import com.srnpr.xmassystem.support.PlusSupportJdAddress;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportJdAddress.JdAddress;
import com.srnpr.xmassystem.util.MD5Code;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;

/**
 * 拆单(按规则把同一个订单的sku进行归类)
 * small_seller_code-是否虚拟商品-入库类型-供应商-入库地-贸易类型
 * @author xiegj
 *
 */
public class TeslaMakeSplit extends TeslaTopOrderMake {

	private final static String KAOLACODE = "SF03WYKLPT"; //网易考拉订单的商户编号
	
	public TeslaXResult doRefresh(TeslaXOrder teslaOrder) {
		TeslaXResult result = new TeslaXResult();
		Map<String, List<String>> map = new HashMap<String,List<String>>();
		LoadProductInfo load = new LoadProductInfo();
		for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {//根据规则就行拆单
			TeslaModelShowGoods good = teslaOrder.getShowGoods().get(i);
			// 拆单规则增加贸易类型 增加配送仓库类别
			String role = good.getSmallSellerCode()+good.getValidateFlag()+good.getPrchType()+good.getDlrId()+good.getSiteNo()+good.getLowGood()+good.getProductTradeType()+good.getDeliveryStoreType()+WebConst.CONST_JJG_Flag+good.getIfJJGFlag();;
			
			PlusModelProductInfo productInfo = load.upInfoByCode(new PlusModelProductQuery(good.getProductCode()));
			// 一件代发的需要按商品拆单
			if("Y".equalsIgnoreCase(productInfo.getVlOrs())){
				role = good.getProductCode()+WebConst.CONST_JJG_Flag+good.getIfJJGFlag();
			}
			//京东商品根据sku拆单
			if(Constants.SMALL_SELLER_CODE_JD.equals(good.getSmallSellerCode())) {
				role += good.getSkuCode();
			}
			
			//考拉商品订单 如果没有地址编号 则不会调用考拉订单确认页 走惠家有拆单规则
			if(good.getIsKaolaGood() == 1 && teslaOrder.getOrderForm() != null && teslaOrder.getOrderForm().getPackageList().size() > 0) {
				
				continue; //考拉订单单独处理拆单
			}
			
			if(map.containsKey(role)){
				map.get(role).add(good.getSkuCode());
			}else {
				List<String> list = new ArrayList<String>();
				list.add(good.getSkuCode());
				map.put(role, list);
			}
//			if(("SF03KJT".equals(good.getSmallSellerCode())||"SF03MLG".equals(good.getSmallSellerCode())||"SF03100443".equals(good.getSmallSellerCode())||"SF03100393".equals(good.getSmallSellerCode())
//					||"SF03100294".equals(good.getSmallSellerCode())
//					||"SF03100327".equals(good.getSmallSellerCode())
//					||"SF03100329".equals(good.getSmallSellerCode()))//跨境通、麦乐购、全球淘
//					&&"0".equals(teslaOrder.getOrderOther().getIsVerifyIdNumber())){
				
			if(new PlusServiceSeller().isKJSeller(good.getSmallSellerCode())//跨境通、麦乐购、全球淘
					&&"0".equals(teslaOrder.getOrderOther().getIsVerifyIdNumber())){
			
				teslaOrder.getOrderOther().setIsVerifyIdNumber("1");
			}
		}
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {//对商品信息中的订单号进行赋值
			TeslaModelSubRole subRole = new TeslaModelSubRole();
			String key = iterator.next().toString();
			String jjgFlag = StringUtils.substring(key,key.indexOf(WebConst.CONST_JJG_Flag)+4 , key.indexOf(WebConst.CONST_JJG_Flag)+5);
			subRole.setOrderCode(KvHelper.upCode("DD"));
			subRole.setSubRole(key);
			subRole.setSkus(map.get(key));
			subRole.setJjgFlag(jjgFlag);
			teslaOrder.getRoles().add(subRole);
			String productName = "";
			String small_seller_code="";
			for (int i = 0; i < teslaOrder.getOrderDetails().size(); i++) {
				if((map.get(key).contains(teslaOrder.getOrderDetails().get(i).getSkuCode())
						||map.get(key).contains(teslaOrder.getOrderDetails().get(i).getProductCode()))&&jjgFlag.equals(teslaOrder.getOrderDetails().get(i).getIfJJGFlag())){
					teslaOrder.getOrderDetails().get(i).setOrderCode(subRole.getOrderCode());
					productName=productName+"|"+teslaOrder.getOrderDetails().get(i).getSkuName();
				}
			}
			TeslaModelOrderInfo modelOrderInfo = new TeslaModelOrderInfo();//初始化小订单数据
			modelOrderInfo.setOrderType(teslaOrder.getUorderInfo().getOrderType());
			modelOrderInfo.setOrderSource(teslaOrder.getUorderInfo().getOrderSource());
			Map<String, Integer> skuNum = new HashMap<String, Integer>();
			for (int i = 0; i < teslaOrder.getShowGoods().size(); i++) {
				if(map.get(key).contains(teslaOrder.getShowGoods().get(i).getSkuCode())&&jjgFlag.equals(teslaOrder.getShowGoods().get(i).getIfJJGFlag())){
					// 购买橙意会员卡商品，默认订单列表是不可见状态
					if(teslaOrder.getShowGoods().get(i).getProductCode().equals(bConfig("xmassystem.plus_product_code"))) {
						modelOrderInfo.setDeleteFlag("1");
						teslaOrder.getUorderInfo().setDeleteFlag("1");
					}
					modelOrderInfo.setDeliveryStoreType(teslaOrder.getShowGoods().get(i).getDeliveryStoreType());
					teslaOrder.getShowGoods().get(i).setOrderCode(subRole.getOrderCode());
					small_seller_code=teslaOrder.getShowGoods().get(i).getSmallSellerCode();
					if(Constants.SMALL_SELLER_CODE_JD.equals(small_seller_code)) {
						skuNum.put(teslaOrder.getShowGoods().get(i).getSkuCode(), teslaOrder.getShowGoods().get(i).getSkuNum());
					}
					if(StringUtils.isNotBlank(teslaOrder.getShowGoods().get(i).getEventType())){
						String sync_yyg_key = bConfig("familyhas.sync_yyg_key");
						try {
							String encode = MD5Code.encode(modelOrderInfo.getOrderSource() + modelOrderInfo.getOrderType()+sync_yyg_key);
							//判断是否是指定来源的扫码购订单
							if(StringUtils.isNotBlank(modelOrderInfo.getOrderType()) && encode.equals(teslaOrder.getYyg().getYygMac())) { 
								//通过审核
							} else {
								String orderTypeStr = WebHelper.getImportOrderSource();
								if(StringUtils.isBlank(modelOrderInfo.getOrderType())  || !orderTypeStr.contains(modelOrderInfo.getOrderType())) {
									checkOrderType(teslaOrder.getShowGoods().get(i).getEventType(), modelOrderInfo);
									checkOrderSource(teslaOrder.getShowGoods().get(i).getEventType(), modelOrderInfo);
								}
								/*if(!StringUtils.startsWithAny(modelOrderInfo.getOrderType(), "449715200013","449715200014","449715200015")){
									checkOrderType(teslaOrder.getShowGoods().get(i).getEventType(), modelOrderInfo);
									checkOrderSource(teslaOrder.getShowGoods().get(i).getEventType(), modelOrderInfo);
								}*/
							}
						} catch (UnsupportedEncodingException e) {
							result.setResultCode(0);
							result.setResultMessage("订单加密出错");
							e.printStackTrace();
						}
					}
				}
			}
			modelOrderInfo.setOrderCode(subRole.getOrderCode());
			modelOrderInfo.setAppVersion(teslaOrder.getUorderInfo().getAppVersion());
			modelOrderInfo.setBuyerCode(teslaOrder.getUorderInfo().getBuyerCode());
			modelOrderInfo.setCreateTime(DateHelper.upNow());
			modelOrderInfo.setOrderChannel(teslaOrder.getChannelId());
			if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getPayType())){
				modelOrderInfo.setPayType("449716200002".equals(teslaOrder.getUorderInfo().getPayType())?"449716200002":"449716200001");
			}
			if(StringUtils.isNotBlank(teslaOrder.getBlockSign())){
				modelOrderInfo.setBlockSign(teslaOrder.getBlockSign());
			}
			
			if(StringUtils.endsWith(key, "449747110002")){
				modelOrderInfo.setLowOrder("449747110002");
			}else{
				modelOrderInfo.setLowOrder("449747110001");
			}
			
			modelOrderInfo.setProductName(productName);
			modelOrderInfo.setSellerCode(teslaOrder.getUorderInfo().getSellerCode());
			modelOrderInfo.setSmallSellerCode(small_seller_code);
			modelOrderInfo.setAppVersion(teslaOrder.getExtras().getVision());
			
			modelOrderInfo.setAnchorId(teslaOrder.getAnchorId());
			modelOrderInfo.setRoomId(teslaOrder.getRoomId());
			
			if(StringUtils.isNotBlank(teslaOrder.getRoomId())&&StringUtils.isNotBlank(teslaOrder.getAnchorId())){
				modelOrderInfo.setOrderType("449715200016");//直播订单
			}
			
			teslaOrder.getSorderInfo().add(modelOrderInfo);
			
			//处理京东订单信息
			if(Constants.SMALL_SELLER_CODE_JD.equals(small_seller_code)) {
				TeslaModelJdOrderInfo teslaModelJdOrderInfo = new TeslaModelJdOrderInfo();
				teslaModelJdOrderInfo.setUid(UUID.randomUUID().toString().replace("-", ""));
				teslaModelJdOrderInfo.setOrderCode(subRole.getOrderCode());
				teslaModelJdOrderInfo.setSkuCode(subRole.getSkus().get(0));
				
				PlusModelSkuInfo plusModelSkuInfo = new PlusSupportProduct().upSkuInfoBySkuCode(
						subRole.getSkus().get(0), teslaOrder.getUorderInfo().getBuyerCode(),
						teslaOrder.getIsMemberCode(), teslaOrder.getIsPurchase(),
						teslaOrder.getUorderInfo().getOrderSource(), "1".equals(teslaOrder.getIsOriginal()),
						"1".equals(teslaOrder.getCollageFlag()));
				teslaModelJdOrderInfo.setSkuId(plusModelSkuInfo.getSellProductcode());
				
				teslaModelJdOrderInfo.setSkuNum(skuNum.get(subRole.getSkus().get(0)));
				
				String areaCode = teslaOrder.getAddress().getAreaCode();
				JdAddress jdAddress = null;
				PlusSupportJdAddress plusSupportJdAddress = new PlusSupportJdAddress();
				// 先查客服维护地址表
				jdAddress = plusSupportJdAddress.getJdAddressName(areaCode);
				if (null == jdAddress) {
					String jdTownId = plusSupportJdAddress.getJdAddressId(areaCode);
					if(StringUtils.isNotBlank(jdTownId)) {
						//查京东地址标准转换表
						jdAddress = plusSupportJdAddress.getJdAddress(jdTownId);
					}else {
						//逐级匹配
						jdAddress = plusSupportJdAddress.getJdAddressAntitone(areaCode);
					}
				}
				
				String provinceId = null == jdAddress ? "0" : StringUtils.isEmpty(jdAddress.getProvinceId()) ? "0" : jdAddress.getProvinceId();
				String cityId = null == jdAddress ? "0" : StringUtils.isEmpty(jdAddress.getCityId()) ? "0" : jdAddress.getCityId();
				String countyId = null == jdAddress ? "0" : StringUtils.isEmpty(jdAddress.getCountyId()) ? "0" : jdAddress.getCountyId();
				String villageId = null == jdAddress ? "0" : StringUtils.isEmpty(jdAddress.getVillageId()) ? "0" : jdAddress.getVillageId();
				
				teslaModelJdOrderInfo.setProvince(provinceId);
				teslaModelJdOrderInfo.setCity(cityId);
				teslaModelJdOrderInfo.setCounty(countyId);
				teslaModelJdOrderInfo.setTown(villageId);
				teslaModelJdOrderInfo.setCreateTime(DateHelper.upNow());
				teslaOrder.getJdOrderInfo().add(teslaModelJdOrderInfo);
			}
		}
		//处理网易考拉的订单
		//兼容加价购的活动配置
		if(teslaOrder.getIsKaolaOrder() == 1 && teslaOrder.getOrderForm() != null) {
			//沿用之前的拆分规则，做加价购的区分
			if(klOrderSplit(teslaOrder,"0",result).getResultCode()==0) {
				return result;
			}
			if(klOrderSplit(teslaOrder,"1",result).getResultCode()==0) {
				return result;
			}
		}		
		return result;
	}
	private TeslaXResult klOrderSplit(TeslaXOrder teslaOrder, String jjgFlag,TeslaXResult result) {
		// TODO Auto-generated method stub
		if("0".equals(jjgFlag)) {
			for(Packages pg : teslaOrder.getOrderForm().getPackageList()) {
				String orderCode = KvHelper.upCode("DD");
				boolean bigFlag = false;
				boolean flag = false;
				TeslaModelOrderInfo modelOrderInfo = new TeslaModelOrderInfo();//初始化小订单数据
				modelOrderInfo.setOrderType(teslaOrder.getUorderInfo().getOrderType());
				modelOrderInfo.setOrderSource(teslaOrder.getUorderInfo().getOrderSource());
				List<KaolaGoods> goods = pg.getGoodsList();
				String productName = "";
				String key = KAOLACODE + pg.getWarehouse().getWarehouseId() + modelOrderInfo.getOrderType()+WebConst.CONST_JJG_Flag+jjgFlag;
				List<String> skus = new ArrayList<String>();
				// 循环考拉返回的子单商品明细
				for(KaolaGoods good : goods) {
					TeslaModelOrderDetail detailItem = null;
					// 循环用户提交订单时的商品明细设置关联订单号
					for(TeslaModelOrderDetail orderDetail : teslaOrder.getOrderDetails()){
						// 如果商品在子单列表中则说明被拆分到同一单订单号
						if(good.getSkuId().equalsIgnoreCase(orderDetail.getSell_skucode())&&jjgFlag.equals(orderDetail.getIfJJGFlag())){
							if(StringUtils.isBlank(orderDetail.getOrderCode())){
								orderDetail.setOrderCode(orderCode); // 关联商品到订单号上面
								productName=productName+"|"+orderDetail.getSkuName();
								detailItem = orderDetail;
								skus.add(orderDetail.getSkuCode());
								flag = true;
							} else {
								// 暂不支持同一个sku购买多件时被拆分到多个订单上的情况，可能会造成后续满减金额计算错误
								result.setResultCode(0);
								result.setResultMessage("您购买的商品【"+orderDetail.getSkuName()+"】同一单数量超过限制，请尝试分开下单。");
								return result;
							}	
						}else if(good.getSkuId().equalsIgnoreCase(orderDetail.getSell_skucode())&&"1".equals(orderDetail.getIfJJGFlag())) {
							flag=true;
						}
					}
					
					// 未查询到考拉商品对应到的惠家有商品信息
					if(detailItem == null&&!flag) {
						result.setResultCode(0);
						result.setResultMessage("未查询到KL商品对应的商品编号!");
						return result;
					}
					if(detailItem!=null) {
						bigFlag = true;
						String orderTypeStr = WebHelper.getImportOrderSource();
						for(TeslaModelShowGoods showGoods : teslaOrder.getShowGoods()){
							if(jjgFlag.equals(showGoods.getIfJJGFlag())&& showGoods.getSkuCode().equalsIgnoreCase(detailItem.getSkuCode())) {
								showGoods.setOrderCode(detailItem.getOrderCode());
								if(StringUtils.isBlank(modelOrderInfo.getOrderType())  || !orderTypeStr.contains(modelOrderInfo.getOrderType())) {
									checkOrderType(showGoods.getEventType(), modelOrderInfo);
									checkOrderSource(showGoods.getEventType(), modelOrderInfo);
								}
							}
						}
					}else {
						flag=false;
					}
				
					if(flag&&teslaOrder.getStatus().getExecStep().equals(ETeslaExec.Create)||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {
						//保存网易考拉订单确认接口返回的订单详情
						String uid = UUID.randomUUID().toString().replace("-", "");
						MDataMap detailMap=new MDataMap();
						detailMap.put("uid", uid);
						detailMap.put("order_code", orderCode);
						detailMap.put("product_code", detailItem.getProductCode());
						detailMap.put("sku_code", detailItem.getSkuCode());
						detailMap.put("goods_id", detailItem.getSell_productcode());
						detailMap.put("sku_id", detailItem.getSell_skucode());
						detailMap.put("goods_unit_price", good.getGoodsUnitPriceWithoutTax().toString());
						detailMap.put("goods_tax_amount", good.getGoodsTaxAmount().toString());
						detailMap.put("goods_pay_amount", good.getGoodsPayAmount().toString());
						detailMap.put("goods_buy_number", String.valueOf(good.getGoodsBuyNumber()));
						detailMap.put("compose_tax_rate", good.getComposeTaxRate().toString());
						detailMap.put("actual_current_price", good.getSku().getActualCurrentPrice().toString());
						DbUp.upTable("oc_order_kaola_list_detail").dataInsert(detailMap);	
					}		
				}
				if(bigFlag) {
					if(teslaOrder.getStatus().getExecStep().equals(ETeslaExec.Create)||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {
						//保存网易考拉小订单
						String suid = UUID.randomUUID().toString().replace("-", "");
						MDataMap orderMap=new MDataMap();
						orderMap.put("uid", suid);
						orderMap.put("order_code", orderCode);
						orderMap.put("order_amount", teslaOrder.getOrderForm().getOrderAmount().toString());
						orderMap.put("pay_amount", pg.getPayAmount().toString());
						orderMap.put("tax_pay_amount", pg.getTaxPayAmount().toString());
						orderMap.put("logistics_pay_amount", pg.getLogisticsPayAmount().toString());
						orderMap.put("order_close_time", String.valueOf(teslaOrder.getOrderForm().getOrderCloseTime()));
						orderMap.put("warehouse_id", String.valueOf(pg.getWarehouse().getWarehouseId()));
						orderMap.put("warehouse_name",pg.getWarehouse().getWarehouseName());
						orderMap.put("create_time", DateHelper.upNow());
						orderMap.put("update_time", DateHelper.upNow());
						DbUp.upTable("oc_order_kaola_list").dataInsert(orderMap);
					}
					
					TeslaModelSubRole subRole = new TeslaModelSubRole();
					subRole.setOrderCode(orderCode);
					subRole.setSubRole(key);
					subRole.setSkus(skus);
					subRole.setJjgFlag(jjgFlag);
					teslaOrder.getRoles().add(subRole);
					
					modelOrderInfo.setOrderCode(orderCode);
					modelOrderInfo.setAppVersion(teslaOrder.getUorderInfo().getAppVersion());
					modelOrderInfo.setBuyerCode(teslaOrder.getUorderInfo().getBuyerCode());
					modelOrderInfo.setCreateTime(DateHelper.upNow());
					modelOrderInfo.setOrderChannel(teslaOrder.getChannelId());
					if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getPayType())){
						modelOrderInfo.setPayType("449716200002".equals(teslaOrder.getUorderInfo().getPayType())?"449716200002":"449716200001");
					}
					
					modelOrderInfo.setLowOrder("449747110001");
					//仓库id
					modelOrderInfo.setWarehouseId(pg.getWarehouse().getWarehouseId());
					modelOrderInfo.setIsKaolaOrder(1);
					
					modelOrderInfo.setProductName(productName);
					modelOrderInfo.setSellerCode(teslaOrder.getUorderInfo().getSellerCode());
					modelOrderInfo.setSmallSellerCode(KAOLACODE);
					modelOrderInfo.setAppVersion(teslaOrder.getExtras().getVision());
					
					modelOrderInfo.setAnchorId(teslaOrder.getAnchorId());
					modelOrderInfo.setRoomId(teslaOrder.getRoomId());
					
					if(StringUtils.isNotBlank(teslaOrder.getRoomId())&&StringUtils.isNotBlank(teslaOrder.getAnchorId())){
						modelOrderInfo.setOrderType("449715200016");//直播订单
					}
					teslaOrder.getSorderInfo().add(modelOrderInfo);
				}
			}
		}else {
			for(Packages pg : teslaOrder.getOrderForm().getPackageList()) {
				List<KaolaGoods> goods = pg.getGoodsList();
				LOOP:for(KaolaGoods good : goods) {
					String orderCode = KvHelper.upCode("DD");
					TeslaModelOrderInfo modelOrderInfo = new TeslaModelOrderInfo();//初始化小订单数据
					modelOrderInfo.setOrderType(teslaOrder.getUorderInfo().getOrderType());
					modelOrderInfo.setOrderSource(teslaOrder.getUorderInfo().getOrderSource());
					String productName = "";
					String key = KAOLACODE + pg.getWarehouse().getWarehouseId() + modelOrderInfo.getOrderType()+WebConst.CONST_JJG_Flag+jjgFlag;
					List<String> skus = new ArrayList<String>();
					// 循环考拉返回的子单商品明细
					TeslaModelOrderDetail detailItem = null;
					// 循环用户提交订单时的商品明细设置关联订单号
					for(TeslaModelOrderDetail orderDetail : teslaOrder.getOrderDetails()){
						// 如果商品在子单列表中则说明被拆分到同一单订单号
						if(good.getSkuId().equalsIgnoreCase(orderDetail.getSell_skucode())&&jjgFlag.equals(orderDetail.getIfJJGFlag())){
							if(StringUtils.isBlank(orderDetail.getOrderCode())){
								orderDetail.setOrderCode(orderCode); // 关联商品到订单号上面
								productName=productName+"|"+orderDetail.getSkuName();
								detailItem = orderDetail;
								skus.add(orderDetail.getSkuCode());
							} else {
								// 暂不支持同一个sku购买多件时被拆分到多个订单上的情况，可能会造成后续满减金额计算错误
								result.setResultCode(0);
								result.setResultMessage("您购买的商品【"+orderDetail.getSkuName()+"】同一单数量超过限制，请尝试分开下单。");
								return result;
							}
							
						}
					}
					
					// 未查询到考拉商品对应到的惠家有商品信息
					if(detailItem == null) {
						//前面已做校验
						continue LOOP;
					}
					
					String orderTypeStr = WebHelper.getImportOrderSource();
					for(TeslaModelShowGoods showGoods : teslaOrder.getShowGoods()){
						if(jjgFlag.equals(showGoods.getIfJJGFlag())&&showGoods.getSkuCode().equalsIgnoreCase(detailItem.getSkuCode())) {
							showGoods.setOrderCode(detailItem.getOrderCode());
							if(StringUtils.isBlank(modelOrderInfo.getOrderType())  || !orderTypeStr.contains(modelOrderInfo.getOrderType())) {
								checkOrderType(showGoods.getEventType(), modelOrderInfo);
								checkOrderSource(showGoods.getEventType(), modelOrderInfo);
							}
						}
					}
					
					if(teslaOrder.getStatus().getExecStep().equals(ETeslaExec.Create)||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {
						//保存网易考拉订单确认接口返回的订单详情
						String uid = UUID.randomUUID().toString().replace("-", "");
						MDataMap detailMap=new MDataMap();
						detailMap.put("uid", uid);
						detailMap.put("order_code", orderCode);
						detailMap.put("product_code", detailItem.getProductCode());
						detailMap.put("sku_code", detailItem.getSkuCode());
						detailMap.put("goods_id", detailItem.getSell_productcode());
						detailMap.put("sku_id", detailItem.getSell_skucode());
						detailMap.put("goods_unit_price", good.getGoodsUnitPriceWithoutTax().toString());
						detailMap.put("goods_tax_amount", good.getGoodsTaxAmount().toString());
						detailMap.put("goods_pay_amount", good.getGoodsPayAmount().toString());
						detailMap.put("goods_buy_number", String.valueOf(good.getGoodsBuyNumber()));
						detailMap.put("compose_tax_rate", good.getComposeTaxRate().toString());
						detailMap.put("actual_current_price", good.getSku().getActualCurrentPrice().toString());
						DbUp.upTable("oc_order_kaola_list_detail").dataInsert(detailMap);	
					}	
					
					if(teslaOrder.getStatus().getExecStep().equals(ETeslaExec.Create)||teslaOrder.getStatus().getExecStep() == ETeslaExec.PCCreate) {
						//保存网易考拉小订单
						String suid = UUID.randomUUID().toString().replace("-", "");
						MDataMap orderMap=new MDataMap();
						orderMap.put("uid", suid);
						orderMap.put("order_code", orderCode);
						orderMap.put("order_amount", teslaOrder.getOrderForm().getOrderAmount().toString());
						orderMap.put("pay_amount", pg.getPayAmount().toString());
						orderMap.put("tax_pay_amount", pg.getTaxPayAmount().toString());
						orderMap.put("logistics_pay_amount", pg.getLogisticsPayAmount().toString());
						orderMap.put("order_close_time", String.valueOf(teslaOrder.getOrderForm().getOrderCloseTime()));
						orderMap.put("warehouse_id", String.valueOf(pg.getWarehouse().getWarehouseId()));
						orderMap.put("warehouse_name",pg.getWarehouse().getWarehouseName());
						orderMap.put("create_time", DateHelper.upNow());
						orderMap.put("update_time", DateHelper.upNow());
						DbUp.upTable("oc_order_kaola_list").dataInsert(orderMap);
					}
					
					TeslaModelSubRole subRole = new TeslaModelSubRole();
					subRole.setOrderCode(orderCode);
					subRole.setSubRole(key);
					subRole.setSkus(skus);
					subRole.setJjgFlag(jjgFlag);
					teslaOrder.getRoles().add(subRole);
					
					modelOrderInfo.setOrderCode(orderCode);
					modelOrderInfo.setAppVersion(teslaOrder.getUorderInfo().getAppVersion());
					modelOrderInfo.setBuyerCode(teslaOrder.getUorderInfo().getBuyerCode());
					modelOrderInfo.setCreateTime(DateHelper.upNow());
					modelOrderInfo.setOrderChannel(teslaOrder.getChannelId());
					if(StringUtils.isNotBlank(teslaOrder.getUorderInfo().getPayType())){
						modelOrderInfo.setPayType("449716200002".equals(teslaOrder.getUorderInfo().getPayType())?"449716200002":"449716200001");
					}
					
					modelOrderInfo.setLowOrder("449747110001");
					//仓库id
					modelOrderInfo.setWarehouseId(pg.getWarehouse().getWarehouseId());
					modelOrderInfo.setIsKaolaOrder(1);
					
					modelOrderInfo.setProductName(productName);
					modelOrderInfo.setSellerCode(teslaOrder.getUorderInfo().getSellerCode());
					modelOrderInfo.setSmallSellerCode(KAOLACODE);
					modelOrderInfo.setAppVersion(teslaOrder.getExtras().getVision());
					
					modelOrderInfo.setAnchorId(teslaOrder.getAnchorId());
					modelOrderInfo.setRoomId(teslaOrder.getRoomId());
					
					if(StringUtils.isNotBlank(teslaOrder.getRoomId())&&StringUtils.isNotBlank(teslaOrder.getAnchorId())){
						modelOrderInfo.setOrderType("449715200016");//直播订单
					}
					
					teslaOrder.getSorderInfo().add(modelOrderInfo);

				}
			}
		}

		return result;
	}
	private void checkOrderType(String evevType,TeslaModelOrderInfo modelOrderInfo){
		String orderType = modelOrderInfo.getOrderType();
		if("4497472600010005".equals(evevType)){
			orderType="449715200004";
		}else if ("4497472600010004".equals(evevType)) {
			orderType="449715200010";
		}else if ("4497472600010006".equals(evevType)) {
			orderType="449715200007";
		}
		modelOrderInfo.setOrderType(orderType);
	}
	private void checkOrderSource(String evevType,TeslaModelOrderInfo modelOrderInfo){
		String orderSource = modelOrderInfo.getOrderSource();
		String orderType = modelOrderInfo.getOrderType();
		if ("4497472600010009".equals(evevType)) {//型录订单
			orderSource="449715190010";
			orderType="449715200010";
		}else if("4497472600010011".equals(evevType)){//生鲜订单
			orderSource="449715190011";
			orderType="449715200010";
		}else if ("4497472600010004".equals(evevType) || "4497472600010015".equals(evevType)) {//扫码购
			//orderSource="449715190007";
			orderType="449715200010";
		}else if ("4497472600010012".equals(evevType)) {//型录和生鲜合并
			orderType="449715200010";
		}
		modelOrderInfo.setOrderType(orderType);
		
		// 南京二台扫码购 和天鹅台扫码购不修改订单来源
		List<String> excludes = Arrays.asList("449715190020","449715190027","449715190033");
		if(!excludes.contains(modelOrderInfo.getOrderSource())) {
			modelOrderInfo.setOrderSource(orderSource);
		}

	}
}
