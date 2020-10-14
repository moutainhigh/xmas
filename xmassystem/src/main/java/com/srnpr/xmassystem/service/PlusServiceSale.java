package com.srnpr.xmassystem.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventFull;
import com.srnpr.xmassystem.modelevent.PlusModelEventSale;
import com.srnpr.xmassystem.modelevent.PlusModelFullCutMessage;
import com.srnpr.xmassystem.modelevent.PlusModelFullMoney;
import com.srnpr.xmassystem.modelevent.PlusModelFullRule;
import com.srnpr.xmassystem.modelevent.PlusModelProObject;
import com.srnpr.xmassystem.modelevent.PlusModelSaleProObject;
import com.srnpr.xmassystem.modelevent.PlusModelSku;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportMember;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.xmassystem.top.XmasSystemConst;
import com.srnpr.zapcom.topdo.TopConfig;
import com.srnpr.zapcom.topdo.TopUp;

/**
 * 满减过滤方法
 * 
 * @author zhouguohui
 *
 */
public class PlusServiceSale {

	/***
	 * 过滤活动
	 * 
	 * @param sale
	 *            购物车调用对象
	 * @param sellerCode
	 *            系统编号
	 * @return
	 */
	public PlusModelSaleProObject getEventSale(PlusModelSaleProObject sale, String sellerCode,String channelId) {
		if (sale == null) {
			return sale;
		}
		PlusModelEventSale eventSale = new PlusSupportEvent().upEventSalueByMangeCode(sellerCode,channelId);

		if (eventSale.getEventFulls() != null && eventSale.getEventFulls().size() > 0) {
			getEventFull(sale, eventSale);
			getEventPrice(sale);
		}
		return sale;
	}

	/**
	 * 价格过滤
	 * 
	 * @param sale
	 * @param eventSale
	 */
	public void getEventPrice(PlusModelSaleProObject sale) {
		/** 取商品数据 **/
		Map<String, List<PlusModelSku>> map = new ConcurrentHashMap<String, List<PlusModelSku>>();
		Map<String, PlusModelFullMoney> fullMoneyMap = new ConcurrentHashMap<String, PlusModelFullMoney>();
		List<PlusModelProObject> proObject = sale.getSaleObject();
		for (int i = 0; i < proObject.size(); i++) {
			PlusModelProObject pro = proObject.get(i);
			if (map.get(pro.getEventCode()) != null && !pro.getEventCode().isEmpty()) {
				PlusModelSku sku = new PlusModelSku();
				sku.setProductCode(pro.getProductCode());
				sku.setSkuNum(pro.getSkuNum());
				sku.setSkuPrice(pro.getOrig_sku_price());
				sku.setChoose_flag(pro.getChoose_flag());
				sku.setSkuCode(pro.getSkuCode());
				map.get(pro.getEventCode()).add(sku);
			} else if (map.get(pro.getEventCode()) == null && !pro.getEventCode().isEmpty()) {
				List<PlusModelSku> lsitValue = new ArrayList<PlusModelSku>();
				PlusModelSku sku = new PlusModelSku();
				sku.setProductCode(pro.getProductCode());
				sku.setSkuNum(pro.getSkuNum());
				sku.setSkuPrice(pro.getOrig_sku_price());
				sku.setChoose_flag(pro.getChoose_flag());
				sku.setSkuCode(pro.getSkuCode());
				lsitValue.add(sku);
				map.put(pro.getEventCode(), lsitValue);
			}

		}
		

		/** 过滤价格 **/
		for (int i = 0; i < proObject.size(); i++) {
			BigDecimal money = BigDecimal.ZERO;
			int count = 0;
			PlusModelProObject pro = proObject.get(i);
			List<PlusModelFullMoney> listMoney = pro.getFullMoneysList();
			if (map.get(pro.getEventCode()) != null) {
				List<PlusModelSku> skuMap = map.get(pro.getEventCode());
				for (int j = 0; j < skuMap.size(); j++) {
					PlusModelSku skuMoney = skuMap.get(j);
					if (skuMoney.getChoose_flag().trim().equals("1")) {
						if (pro.getFullType().equals("449747630004")) {//满X件减Y件
							// 满X件，计算总件数
							count += skuMoney.getSkuNum();
							
						} else if (pro.getFullType().equals("449747630005")) {//满X元任选Y件
							// 满X元，计算总钱数
							money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
							count += skuMoney.getSkuNum();
							
						} else if (pro.getFullType().equals("449747630006")) { //满折-仅第X件打折
							// 第X件打Y折按商品编号计算件数
							if(pro.getProductCode().equals(skuMoney.getProductCode())){
								money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
								count += skuMoney.getSkuNum();
							}
						}else if (pro.getFullType().equals("449747630007")) {//满折-总价打折
							// 满X件打Y折
							money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
							count += skuMoney.getSkuNum();
							
						} else if(pro.getFullType().equals("449747630008")) {// 每满X减Y-LD多重促销活动

							//money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
							if("44974734000300010001".equals(pro.getFullCutCalType())) {//活动价之上进行满减
								money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
							} else if("44974734000300010002".equals(pro.getFullCutCalType())) {//原价的基础上进行满减
								// 查询sku原价
								PlusModelSkuQuery plusModelQuery = new PlusModelSkuQuery();
								plusModelQuery.setCode(skuMoney.getSkuCode());
								PlusModelSkuInfo upInfoByCode = new LoadSkuInfo().upInfoByCode(plusModelQuery);
								money = money.add( MoneyHelper.roundHalfUp(upInfoByCode.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
							}
							
							
						} else {
							// 满X元，计算总钱数
							money = money.add( MoneyHelper.roundHalfUp(skuMoney.getSkuPrice()).multiply(BigDecimal.valueOf(skuMoney.getSkuNum())) ) ;
						}
					}
				}

			}

			boolean isTrue = true;
			if (pro.getFullType().equals("449747630002")) {// 每满X减Y

				if (money.compareTo(listMoney.get(0).getFullMoney()) >= 0) {
					if (fullMoneyMap.get(pro.getEventCode()) == null) {
						int cutPrice = (int) Math.floor ( money.divide(listMoney.get(0).getFullMoney() ,2, BigDecimal.ROUND_DOWN).doubleValue() );
						BigDecimal FullLastPrice = listMoney.get(0).getFullMoney().multiply(new BigDecimal(cutPrice));
						BigDecimal cutLastPrice = listMoney.get(0).getCutMoney().multiply(new BigDecimal(cutPrice));
						listMoney.get(0).setCutMoney(cutLastPrice);
						listMoney.get(0).setAdvTitle("满" + FullLastPrice + "元，已减" + cutLastPrice);
						fullMoneyMap.put(pro.getEventCode(), listMoney.get(0));

					}

					if (fullMoneyMap.get(pro.getEventCode()) != null) {
						pro.setFullMoneys(fullMoneyMap.get(pro.getEventCode()));
					}

					isTrue = false;

				}

				/** 当商品虽然满足了满减活动 但价格没有满足的情况下 返回不满足活动 所有参数情空 **/
				if (isTrue) {
					pro.setEventTrue(false);
					pro.setEventCode("");
					//pro.setFullType("");
					// pro.setEventType("");
					// pro.setEventTypeTame("");
				}

			} else if(pro.getFullType().equals("449747630008")) {//每满X减Y-LD多重促销活动
				
				//判断最大购买量 
				if (money.compareTo(listMoney.get(0).getFullMoney()) >= 0 && pro.getFullCutMaxPrice().compareTo(BigDecimal.ZERO ) > 0) {
					
					if (fullMoneyMap.get(pro.getEventCode()) == null) {
						int cutPrice = (int) Math.floor ( money.divide(listMoney.get(0).getFullMoney() ,2, BigDecimal.ROUND_DOWN).doubleValue() );
						BigDecimal FullLastPrice = listMoney.get(0).getFullMoney().multiply(new BigDecimal(cutPrice));
						BigDecimal cutLastPrice = listMoney.get(0).getCutMoney().multiply(new BigDecimal(cutPrice));
						
						if(cutLastPrice.compareTo(pro.getFullCutMaxPrice()) > 0) {//判断最大购买量
							cutLastPrice = pro.getFullCutMaxPrice();
						}
						listMoney.get(0).setCutMoney(cutLastPrice);
						listMoney.get(0).setAdvTitle("满" + FullLastPrice + "元，已减" + cutLastPrice);
						fullMoneyMap.put(pro.getEventCode(), listMoney.get(0));

					}

					if (fullMoneyMap.get(pro.getEventCode()) != null) {
						pro.setFullMoneys(fullMoneyMap.get(pro.getEventCode()));
					}

					isTrue = false;

				}

				/** 当商品虽然满足了满减活动 但价格没有满足的情况下 返回不满足活动 所有参数情空 **/
				if (isTrue) {
					pro.setEventTrue(false);
					pro.setEventCode("");
				}

			} else {
				// 价格倒排序
				Collections.sort(listMoney, new Comparator<Object>() {
					public int compare(Object moenyOne, Object moneyTwo) {
						BigDecimal one = ((PlusModelFullMoney) moenyOne).getFullMoney();
						BigDecimal two = ((PlusModelFullMoney) moneyTwo).getFullMoney();
						return two.compareTo(one);
					}
				});

				for (int m = 0; m < listMoney.size(); m++) {
					PlusModelFullMoney plusModelFullMoney = new PlusModelFullMoney();
					
					plusModelFullMoney.setAdvTitle(listMoney.get(m).getAdvTitle());
					plusModelFullMoney.setCutMoney(listMoney.get(m).getCutMoney());
					plusModelFullMoney.setFullMoney(listMoney.get(m).getFullMoney());
					plusModelFullMoney.setFullCutType(listMoney.get(m).getFullCutType());
					
					if (pro.getFullType().equals("449747630004")) {// 满X件减Y件

						if (count >= plusModelFullMoney.getFullMoney().intValue()) {//fullMoney为后台配置的满X件减y件的   X

							if (fullMoneyMap.get(pro.getEventCode()) == null) {

								plusModelFullMoney.setAdvTitle("满" + plusModelFullMoney.getFullMoney().setScale(0, BigDecimal.ROUND_HALF_UP) + "件，已减"
										+ plusModelFullMoney.getCutMoney().setScale(0, BigDecimal.ROUND_HALF_UP) + "件");

								// 按sku价格升序排列，从价格最低的开始取
								if (map.get(pro.getEventCode()) != null) {
									List<PlusModelSku> skuMap = map.get(pro.getEventCode());
									Collections.sort(skuMap, new Comparator<Object>() {
										public int compare(Object moenyOne, Object moneyTwo) {
											BigDecimal one = ((PlusModelSku) moenyOne).getSkuPrice();
											BigDecimal two = ((PlusModelSku) moneyTwo).getSkuPrice();
											return one.compareTo(two);
										}
									});
									BigDecimal cutAllMoney = new BigDecimal(0);
									BigDecimal fullAllMoney = new BigDecimal(0);

									int curntFullCount = 0;// 当前累计满足的件数
									int curntCutCount = 0;// 当前累计需要减的件数
									// 循环计算排序之后需要满减的金额
									for (int j = 0; j < skuMap.size(); j++) {
										PlusModelSku skuMoney = skuMap.get(j);

										if (skuMoney.getChoose_flag().trim().equals("1")) {
											
											if ((curntFullCount + skuMoney.getSkuNum()) > plusModelFullMoney.getFullMoney()
													.intValue()) {//当最后件数大于总的满的件数时	
	
													// 累计总共需要满减的商品件数的总金额
													fullAllMoney = fullAllMoney
															.add(skuMoney.getSkuPrice().multiply(plusModelFullMoney
																	.getFullMoney().subtract(new BigDecimal(curntFullCount))));
													curntFullCount += plusModelFullMoney.getCutMoney()
															.subtract(new BigDecimal(curntFullCount)).intValue();
													
													//累计减的件数（cutMoney为满X件减Y件的 Y ）
													if ((curntCutCount + skuMoney.getSkuNum()) > plusModelFullMoney.getCutMoney()
															.intValue()) {	
			
														cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
																.multiply(plusModelFullMoney.getCutMoney()
																		.subtract(new BigDecimal(curntCutCount))));
														
														curntCutCount += plusModelFullMoney.getCutMoney()
																.subtract(new BigDecimal(curntCutCount)).intValue();
			
													} else {
															cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
																	.multiply(new BigDecimal(skuMoney.getSkuNum())));
															curntCutCount += skuMoney.getSkuNum();
													}
	
													break;
	
											} else {//当总件数没有减少时
												
											   // 累计总共需要满减的商品件数的总金额
												fullAllMoney = fullAllMoney.add(skuMoney.getSkuPrice()
														.multiply(new BigDecimal(skuMoney.getSkuNum())));// 累计总共需要满减的商品的价格
												curntFullCount += skuMoney.getSkuNum();
												
												//累计减的金额
												if ((curntCutCount + skuMoney.getSkuNum()) > plusModelFullMoney.getCutMoney()
														.intValue()) {	
		
													cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
															.multiply(plusModelFullMoney.getCutMoney()
																	.subtract(new BigDecimal(curntCutCount))));
													
													curntCutCount += plusModelFullMoney.getCutMoney()
															.subtract(new BigDecimal(curntCutCount)).intValue();
		
												} else {
														cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
																.multiply(new BigDecimal(skuMoney.getSkuNum())));
														curntCutCount += skuMoney.getSkuNum();
												}
	
											}
	
										}
									}

									// 将满X件减Y件转换成 满X元件Y元， 以备后续均摊sku售价
									plusModelFullMoney.setFullMoney(fullAllMoney);
									plusModelFullMoney.setCutMoney(cutAllMoney);
								}

								fullMoneyMap.put(pro.getEventCode(), plusModelFullMoney);

							}

							if (fullMoneyMap.get(pro.getEventCode()) != null) {
								pro.setFullMoneys(fullMoneyMap.get(pro.getEventCode()));
							}

							// pro.setFullMoneys(listMoney.get(m));
							isTrue = false;
							break;

						}

					} else if(ArrayUtils.contains(XmasSystemConst.MJ_MANZHE, pro.getFullType())){
						// 满折活动计算减的金额
						if(count >= plusModelFullMoney.getFullMoney().intValue()){
							// 按sku价格升序排列，从价格最低的开始取
							if (map.get(pro.getEventCode()) != null) {
								List<PlusModelSku> skuMap = map.get(pro.getEventCode());
								Collections.sort(skuMap, new Comparator<Object>() {
									public int compare(Object moenyOne, Object moneyTwo) {
										BigDecimal one = ((PlusModelSku) moenyOne).getSkuPrice();
										BigDecimal two = ((PlusModelSku) moneyTwo).getSkuPrice();
										return one.compareTo(two);
									}
								});
								
								List<PlusModelSku> skuList = new ArrayList<PlusModelSku>();
								for(PlusModelSku sku : skuMap){
									if(sku.getChoose_flag().trim().equals("1")){
										if(pro.getProductCode().equals(sku.getProductCode())){
											skuList.add(sku);
										}
									}
								}
								
								if(!skuList.isEmpty()){
									if("449747630006".equals(pro.getFullType())){
										// 满折-仅第X件打折
										// 对第一件商品计算折扣后得出优惠的金额
										if(StringUtils.isBlank(plusModelFullMoney.getAdvTitle())){
											plusModelFullMoney.setAdvTitle("第"+plusModelFullMoney.getFullMoney()+"件"+plusModelFullMoney.getCutMoney().intValue()+"折");
										}
										
										BigDecimal cutMoney = skuList.get(0).getSkuPrice().multiply(plusModelFullMoney.getCutMoney().multiply(new BigDecimal("0.1")));
										plusModelFullMoney.setFullMoney(money.setScale(2, BigDecimal.ROUND_HALF_UP));
										plusModelFullMoney.setCutMoney(skuList.get(0).getSkuPrice().subtract(cutMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
										pro.setFullMoneys(plusModelFullMoney);
										
										isTrue = false;
									} else if("449747630007".equals(pro.getFullType())){
										// 满折-总价打折
										// 对总金额计算折扣后得出优惠的金额
										if(StringUtils.isBlank(plusModelFullMoney.getAdvTitle())){
											plusModelFullMoney.setAdvTitle("满"+plusModelFullMoney.getFullMoney()+"件打"+plusModelFullMoney.getCutMoney().intValue()+"折");
										}
										
										BigDecimal cutMoney = money.multiply(plusModelFullMoney.getCutMoney().multiply(new BigDecimal("0.1")));
										plusModelFullMoney.setFullMoney(money.setScale(2, BigDecimal.ROUND_HALF_UP));
										plusModelFullMoney.setCutMoney(money.subtract(cutMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
										pro.setFullMoneys(plusModelFullMoney);
										
										isTrue = false;
									}
								}
							}		
						}
					} else {

						if (money.compareTo(plusModelFullMoney.getFullMoney()) >= 0) {

							if (fullMoneyMap.get(pro.getEventCode()) == null) {
								/*
								 * 满x元任选y件的规则：
								 *    1、参与活动的商品金额必须 大于 “x/y”
								 *    2、参与活动的商品总金额 大于 “x”
								 *    3、参与活动的商品数量  大于   “y”
								 */
								if (pro.getFullType().equals("449747630005")) {// 满X元任选Y件   （必须同时满足  满减的金额及任选的件数）

									if(count < plusModelFullMoney.getCutMoney().intValue() ) {
										//如果总商品件数小于任选的件数，则不参与活动
										continue;
									}
									// 按sku价格升序排列，从价格最低的开始取
									if (map.get(pro.getEventCode()) != null) {
										List<PlusModelSku> skuMap = map.get(pro.getEventCode());
										Collections.sort(skuMap, new Comparator<Object>() {
											public int compare(Object moenyOne, Object moneyTwo) {
												BigDecimal one = ((PlusModelSku) moenyOne).getSkuPrice();
												BigDecimal two = ((PlusModelSku) moneyTwo).getSkuPrice();
												return one.compareTo(two);
											}
										});
										BigDecimal cutAllMoney = new BigDecimal(0);
										
										//满x元任选Y件，   则 fullMoney为满的金额，cutMoney为任选的件数
										BigDecimal averageMoney = plusModelFullMoney.getFullMoney().divide(plusModelFullMoney.getCutMoney(),2,BigDecimal.ROUND_UP);//满减的平均值
										
										int curntCount = 0;// 当前累计符合满选的件数
										
										for (int j = 0; j < skuMap.size(); j++) {
											PlusModelSku skuMoney = skuMap.get(j);
											
											//如果商品的单价小于满X元任选Y件的平均值，则不参与计算
											if(skuMoney.getSkuPrice().compareTo(averageMoney) < 0 ) {
												continue;
											}
											
											if (skuMoney.getChoose_flag().trim().equals("1")) {
												// 判断总件数是否已经超过满减的件数，超过则取缺少的sku的价格
												if ((curntCount + skuMoney.getSkuNum()) >= plusModelFullMoney
														.getCutMoney().intValue()) {

													cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
															.multiply(plusModelFullMoney.getCutMoney()
																	.subtract(new BigDecimal(curntCount))));

													// 最后一次添加数量
													curntCount += plusModelFullMoney.getCutMoney()
															.subtract(new BigDecimal(curntCount)).intValue();
													break;

												} else {

													cutAllMoney = cutAllMoney.add(skuMoney.getSkuPrice()
															.multiply(new BigDecimal(skuMoney.getSkuNum())));
													curntCount += skuMoney.getSkuNum();

												}
											}

										}
										if(curntCount < plusModelFullMoney.getCutMoney().intValue()) {
											//如何符合条件的商品个数小于任选的件数，则不参与活动
											continue;
										}

										plusModelFullMoney.setAdvTitle(
												"满" + plusModelFullMoney.getFullMoney() + "元，已选" + curntCount + "件");

										// 由于满多少元任选多少件 是 共用 满多少元减多少元的字段，因此在这修改
										if (cutAllMoney.compareTo(plusModelFullMoney.getFullMoney()) > 0) {
											plusModelFullMoney
													.setCutMoney(cutAllMoney.subtract(plusModelFullMoney.getFullMoney()));
											//满减的总金额更改为，优惠的商品的金额
											plusModelFullMoney.setFullMoney(cutAllMoney);
										} else {
											continue;
										}

									}

								} else {
									plusModelFullMoney.setAdvTitle("满" + plusModelFullMoney.getFullMoney() + "元，已减"
											+ plusModelFullMoney.getCutMoney());
								}

								fullMoneyMap.put(pro.getEventCode(), plusModelFullMoney);
							}

							if (fullMoneyMap.get(pro.getEventCode()) != null) {
								pro.setFullMoneys(fullMoneyMap.get(pro.getEventCode()));
							}

							// pro.setFullMoneys(listMoney.get(m));
							isTrue = false;
							break;
						}

					}

				}

				/** 当商品虽然满足了满减活动 但价格没有满足的情况下 返回不满足活动 所有参数情空 **/
				if (isTrue) {
					pro.setEventTrue(false);
					pro.setEventCode("");
					//pro.setFullType("");
					// pro.setEventType("");
					// pro.setEventTypeTame("");
				}

			}

		}

		/** 过滤活动名称 **/
		for (int i = 0; i < proObject.size(); i++) {
			PlusModelProObject pro = proObject.get(i);
			if (pro.getFullMoneys().getAdvTitle().isEmpty() && MoneyHelper.roundHalfUp(pro.getFullMoneys().getFullMoney()).compareTo(BigDecimal.ZERO) == 0
					&& MoneyHelper.roundHalfUp(pro.getFullMoneys().getCutMoney()).compareTo(BigDecimal.ZERO) == 0) {
				if (fullMoneyMap.get(pro.getItem_code()) == null) {
					if (pro.getFullMoneysList() != null && pro.getFullMoneysList().size() > 0) {
						List<PlusModelFullMoney> fullMoney = pro.getFullMoneysList();
						String saleMessage = "";
						for (int m = fullMoney.size() - 1; m >= 0; m--) {
							saleMessage += fullMoney.get(m).getAdvTitle();
							if (m > 0) {
								saleMessage += ",";
							}
						}
						pro.getFullMoneysList().get(0).setAdvTitle(saleMessage);
						pro.setFullMoneys(pro.getFullMoneysList().get(0));
						fullMoneyMap.put(pro.getItem_code(), pro.getFullMoneysList().get(0));
					}
				} else {
					pro.setFullMoneys(fullMoneyMap.get(pro.getItem_code()));
				}
			}
		}
	}

	/***
	 * 数据处理类方法
	 * 
	 * @param sale
	 *            购物车对象
	 * @param sellerCode
	 *            系统编号
	 */
	public void getEventFull(PlusModelSaleProObject sale, PlusModelEventSale eventSale) {

		// 那购物车勾选进行倒叙排序 为了解决购物车全部不勾选时fullMoeny对象为空的bug
		Collections.sort(sale.getSaleObject(), new Comparator<Object>() {
			public int compare(Object fullMoneyOne, Object fullMoneyTwo) {
				String one = ((PlusModelProObject) fullMoneyOne).getChoose_flag();
				String two = ((PlusModelProObject) fullMoneyTwo).getChoose_flag();
				return two.compareTo(one);
			}
		});

		/** 取活动数据 **/
		List<PlusModelEventFull> listFull = eventSale.getEventFulls();
		boolean firstOrder = new PlusSupportMember().upFlagFirstOrder(sale.getMemberCode());

		List<PlusModelProObject> list = sale.getSaleObject();
		/** 先过滤首单活动 **/
		if (firstOrder) {
			for (int j = 0; j < listFull.size(); j++) {
				PlusModelEventFull eventFull = listFull.get(j);
				if (eventFull.getFirstOrder().equals("449747710001")) {
					continue;
				}
				setSalePro(firstOrder, list, eventFull);
			}
		}

		/** 不过滤首单活动 **/
		for (int j = 0; j < listFull.size(); j++) {
			PlusModelEventFull eventFull = listFull.get(j);
			if (eventFull.getFirstOrder().equals("449747710002")) {
				continue;
			}
			setSalePro(firstOrder, list, eventFull);
		}

	}

	/**
	 * 
	 * @param firstOrder
	 *            是否首单
	 * @param lsit
	 *            商品数据
	 * @param eventFull
	 *            活动数据
	 */
	public void setSalePro(boolean firstOrder, List<PlusModelProObject> list, PlusModelEventFull eventFull) {
		/** 循环遍历商品 **/
		for (int i = 0; i < list.size(); i++) {
			PlusModelProObject proObject = list.get(i);
			// 标示是否满足该活动
			boolean is_true = true;

			/** 判断商品是否已经参加活动 并且活动编号不为空该商品直接跳过 **/
			if (proObject.isEventTrue() == true && !proObject.getEventCode().isEmpty()) {
				continue;
			}

			/** 当不是首单时 **/
			if (is_true && !firstOrder && eventFull.getFirstOrder().equals("449747710002")) {
				is_true = false;
				continue;
			}

			/** 当分类 品牌 sku编号都为空的时间 证明该活动为无限制 **/
			/** 当分类 品牌 sku编号都为空的时间 证明该活动为无限制 **/
			if (is_true && null == eventFull.getRuleBrand().getLimitCode()
					&& null == eventFull.getRuleCategory().getLimitCode()
					&& null == eventFull.getRuleSku().getLimitCode()) {
				proObject.setEventCode(eventFull.getEventCode());
				proObject.setItem_code(eventFull.getEventCode());
				proObject.setEventTrue(true);
				proObject.setEventType("4497472600010008");
				proObject.setEventTypeTame("满减");
				proObject.setBeginTime(eventFull.getBeginTime());
				proObject.setEndTime(eventFull.getEndTime());
				proObject.setFullType(eventFull.getFullType());
				proObject.setFullMoneysList(eventFull.getFullMoneys());
				proObject.setActivityUrl(eventFull.getActivityUrl());
				
				proObject.setFullCutCalType(eventFull.getFullCutCalType());
				proObject.setFullCutMaxPrice(eventFull.getFullCutMaxPrice());
				continue;
			}

			/** 分类不空时 **/
			if (is_true && eventFull.getRuleCategory() != null) {
				PlusModelFullRule category = eventFull.getRuleCategory();
				List<String> listCategory = category.getLimitCode();
				List<String> listSkuCategory = proObject.getCategoryCodes();
				/**
				 * 4497476400020001 不限 4497476400020002 仅包含 4497476400020003
				 * 以下除外
				 **/
				if (category.getLimitType().equals("4497476400020001")) {

				} else if (category.getLimitType().equals("4497476400020002")) {
					if (!regList(listCategory, listSkuCategory)) {
						is_true = false;
						continue;
					}
				} else if (category.getLimitType().equals("4497476400020003")) {
					if (regList(listCategory, listSkuCategory)) {
						is_true = false;
						continue;
					}
				}
			}

			/** 品牌不空时 **/
			if (is_true && eventFull.getRuleBrand() != null) {
				PlusModelFullRule brand = eventFull.getRuleBrand();
				List<String> listBrand = brand.getLimitCode();
				String listSkuBrand = proObject.getBrandCode();
				/**
				 * 4497476400020001 不限 4497476400020002 仅包含 4497476400020003
				 * 以下除外
				 **/
				if (brand.getLimitType().equals("4497476400020001")) {

				} else if (brand.getLimitType().equals("4497476400020002")) {
					/** 当品牌包含 不跳出循环 **/
					if (regValue(listBrand, listSkuBrand)) {

					} else {
						is_true = false;
						continue;
					}
				} else if (brand.getLimitType().equals("4497476400020003")) {
					/** 当品牌包含 跳出循环 **/
					if (regValue(listBrand, listSkuBrand)) {
						is_true = false;
						continue;
					}
				}
			}

			/** 商品不空时 **/
			if (is_true && eventFull.getRuleSku() != null) {
				PlusModelFullRule sku = eventFull.getRuleSku();
				List<String> listSku = sku.getLimitCode();
				String listProCode = proObject.getProductCode();
				/**
				 * 4497476400020001 不限 4497476400020002 仅包含 4497476400020003
				 * 以下除外
				 **/
				if (sku.getLimitType().equals("4497476400020001")) {

				} else if (sku.getLimitType().equals("4497476400020002")) {
					/** 当商品包含 不跳出循环 **/
					if (regValue(listSku, listProCode)) {

					} else {
						is_true = false;
						continue;
					}
				} else if (sku.getLimitType().equals("4497476400020003")) {
					/** 当商品包含 跳出循环 **/
					if (regValue(listSku, listProCode)) {
						is_true = false;
						continue;
					}
				}
			}

			if (is_true) {
				proObject.setItem_code(eventFull.getEventCode());
				proObject.setEventCode(eventFull.getEventCode());
				proObject.setEventTrue(true);
				proObject.setBeginTime(eventFull.getBeginTime());
				proObject.setEndTime(eventFull.getEndTime());
				proObject.setEventType("4497472600010008");
				if("449747630005".equals(eventFull.getFullType())) {
					proObject.setEventTypeTame("N元任选");
				} else if(ArrayUtils.contains(XmasSystemConst.MJ_MANZHE, eventFull.getFullType())){
					proObject.setEventTypeTame("多买优惠");
				} else {
					proObject.setEventTypeTame("满减");
				}
				proObject.setFullType(eventFull.getFullType());
				proObject.setFullMoneysList(eventFull.getFullMoneys());
				proObject.setActivityUrl(eventFull.getActivityUrl());
				
				proObject.setFullCutCalType(eventFull.getFullCutCalType());
				proObject.setFullCutMaxPrice(eventFull.getFullCutMaxPrice());

			}

		}

	}

	/**
	 * 单个商品过滤 满足某个活动信息
	 * 
	 * @param productCode
	 * @param sellerCode
	 * @return
	 */
	public List<PlusModelFullCutMessage> getEventMessage(String productCode, PlusModelEventSale eventSale,
			String memberCode) {
		List<PlusModelFullCutMessage> eventMessage = new ArrayList<PlusModelFullCutMessage>();
		if (null != productCode && StringUtils.isEmpty(productCode)) {
			return eventMessage;
		}
		
		// 橙意会员卡商品不参与任何活动
		if(productCode.equals(TopConfig.Instance.bConfig("xmassystem.plus_product_code"))) {
			return eventMessage;
		}
		
		/** 取活动数据 **/
		List<PlusModelEventFull> listFull = eventSale.getEventFulls();

		/** 封装商品数据 **/
		PlusModelProductInfo plusModelProductinfo = new LoadProductInfo()
				.upInfoByCode(new PlusModelProductQuery(productCode));
		String brand = plusModelProductinfo.getBrandCode();
		List<String> category = plusModelProductinfo.getCategorys();

		List<PlusModelProObject> list = new ArrayList<PlusModelProObject>();
		PlusModelProObject pro = new PlusModelProObject();
		pro.setProductCode(productCode);
		pro.setCategoryCodes(category);
		pro.setBrandCode(brand);
		list.add(pro);

		boolean firstOrder = new PlusSupportMember().upFlagFirstOrder(memberCode);

		if (listFull != null && listFull.size() > 0) {

			/** 先过滤首单活动 **/
			if (firstOrder) {
				for (int j = 0; j < listFull.size(); j++) {
					PlusModelEventFull eventFull = listFull.get(j);
					if (eventFull.getFirstOrder().equals("449747710001")) {
						continue;
					}
					setSalePro(firstOrder, list, eventFull);
				}
			}

			/** 不过滤首单活动 **/
			for (int j = 0; j < listFull.size(); j++) {
				PlusModelEventFull eventFull = listFull.get(j);
				if (eventFull.getFirstOrder().equals("449747710002")) {
					continue;
				}
				setSalePro(firstOrder, list, eventFull);
			}

			if (list != null && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {

					if (j > 0) {
						continue;
					}

					PlusModelProObject proObject = list.get(j);
					if (proObject.getItem_code() == null || proObject.getItem_code().equals("")) {
						continue;
					}
					PlusModelFullCutMessage fullCutMessage = new PlusModelFullCutMessage();
					fullCutMessage.setEventCode(proObject.getEventCode());
					fullCutMessage.setEventType("4497472600010008");
					fullCutMessage.setBeginTime(proObject.getBeginTime());
					fullCutMessage.setEndTime(proObject.getEndTime());
					String saleMessage = "";
					List<PlusModelFullMoney> fullMoney = proObject.getFullMoneysList();
					for (int m = 0; m < fullMoney.size(); m++) {
						saleMessage += fullMoney.get(m).getAdvTitle();
						if (m < fullMoney.size() - 1) {
							saleMessage += ",";
						}
					}
					
					fullCutMessage.setSaleMessage(saleMessage);

					// //添加活动跳转类型 fq 活动跳转
					if (StringUtils.isNotBlank(proObject.getActivityUrl())) {
						// 跳转活动专题页
						fullCutMessage.setForwardType("100002");
						fullCutMessage.setForwardVal(proObject.getActivityUrl());
					} else {
						// 跳转原生活动页(与4.0.6版本之前的跳转方式一致)
						fullCutMessage.setForwardType("100001");
						fullCutMessage.setForwardVal(proObject.getEventCode());
					}
					
					//如果满减类型为  满x元任选 时 更换颜色为#b1a5f5
					if("449747630005".equals(proObject.getFullType())) {
						fullCutMessage.setEventName("N元任选");
						fullCutMessage.setEventNameColor(TopUp.upConfig("xmassystem.enentNameNColor"));
					} else if(ArrayUtils.contains(XmasSystemConst.MJ_MANZHE, pro.getFullType())){
						fullCutMessage.setEventName("多买优惠");
					} else {
						fullCutMessage.setEventName("满减");
					}
					
					eventMessage.add(fullCutMessage);

				}
			}

		}

		return eventMessage;

	}

	/**
	 * 判断两个list是否有交集
	 * 
	 * @param listSource
	 * @param listNew
	 * @return
	 */
	public boolean regList(List<String> listSource, List<String> listNew) {
		boolean is_true = false;
		for (int i = 0; i < listSource.size(); i++) {
			String source = listSource.get(i);
			for (int j = 0; j < listNew.size(); j++) {
				String lnew = listNew.get(j);
				if (source.contains(lnew) || lnew.contains(source)) {
					is_true = true;
					break;
				}
			}
		}
		return is_true;
	}

	/**
	 * 判断list是否包含
	 * 
	 * @param listSource
	 * @param value
	 * @return
	 */
	public boolean regValue(List<String> listSource, String value) {
		boolean is_true = false;
		is_true = listSource.contains(value);
		return is_true;
	}
}
