package com.srnpr.xmasorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmasorder.model.AverageMoneyModel;

/**
 * 优惠平摊金额
 * 平摊金额的规则：
 * 		1、计算每个商品占总商品价格的比例分摊优惠总金额（全部向上取整）
 * 		2、由第一步向上取整，则将多余优惠的金额再平摊到每一个商品上，做递归平摊
 * 
 * @author fq
 *
 */
public class AverageMoneyService {
	
	public static final String LDSELLERCODE = "SI2003";//LD商户编号
	
	
	/**
	  * 平摊商品金额
	 * 规则：
	 * 		优先处理LD商品（LD商品只能是整数），
	 * 		然后平摊自营商品（支持小数）
	 * @param averageModel  需要平摊金额的商品信息
	 * @param initCutMoney  需要平摊的优惠金额
	 * @return   返回值map（key：sku编号，value：优惠的金额）
	 */
	public static Map<String , BigDecimal> toAverageMoney(List<AverageMoneyModel> averageModel ,BigDecimal initCutMoney) {
		
		/*
		 * 初始化开始  satart
		 */
		//按商品数量排序
		sortList(averageModel);
		
		List<AverageMoneyModel> pcList = new ArrayList<AverageMoneyModel>();//商品信息备份
		
		Map<String , BigDecimal> resultMap = new HashMap<String , BigDecimal>();//最终计算优惠金额（key：sku编号，value：优惠的金额）
		
		//计算商品总金额(用户计算每个商品所占总金额的比例去平摊优惠金额)
		BigDecimal allSkuMoney = BigDecimal.ZERO;
		for (AverageMoneyModel model : averageModel) {
			allSkuMoney = allSkuMoney.add(BigDecimal.valueOf(model.getSkuPrice() * model.getSkuNum()));
			//初始化返回金额
			resultMap.put(model.getSkuCode(), BigDecimal.valueOf(model.getSkuPrice()));
			
			//做商品数据的备份
			AverageMoneyModel tModel = new AverageMoneyModel();
			tModel.setSkuCode(model.getSkuCode());
			tModel.setSkuNum(model.getSkuNum());
			tModel.setSkuPrice(model.getSkuPrice());
			tModel.setSmallSellerCode(model.getSmallSellerCode());
			pcList.add(tModel);
		}
		if(initCutMoney.compareTo(BigDecimal.ZERO) <= 0) {
			//清空初始值，将所有商品对应的优惠金额致为0
			for (String skuCode : resultMap.keySet()) {
				resultMap.put(skuCode, BigDecimal.ZERO.setScale(2));
			}
			return resultMap;
		}
		
		if(allSkuMoney.compareTo(BigDecimal.ZERO) <= 0 || allSkuMoney.compareTo(initCutMoney) <= 0) {
			//如果计算的商品总金额不大于0，则不需要平摊金额(应为上文有初始化金额，所以直接返回即可)
			return resultMap;
		}
		
		/*
		 * 初始化结束  end
		 */
		resultMap = averageMoney(averageModel , initCutMoney);//返回金额为优惠金额（key：商品编号；value:优惠金额）
		BigDecimal overcouponMoney = new BigDecimal(0.00);//已使用优惠金额
		
		for (AverageMoneyModel model : pcList) {//pcList为备份
			BigDecimal t_money = resultMap.get(model.getSkuCode());//小数精确
			if(LDSELLERCODE.equals(model.getSmallSellerCode())) {
				t_money = t_money.setScale(0,BigDecimal.ROUND_DOWN);//优惠金额向下取整
			} else {
				t_money = t_money.setScale(2,BigDecimal.ROUND_DOWN);//优惠金额向下取整
			}
			//计算次商品的优惠金额(商品原售价-优惠金额)
			overcouponMoney = overcouponMoney.add(t_money.multiply(BigDecimal.valueOf(model.getSkuNum())));
			resultMap.put(model.getSkuCode(), t_money);
		}
		
		//判断 初始要优惠的总金额 是否大于 已使用的优惠金额 
		if(initCutMoney.compareTo(overcouponMoney) > 0) {
			BigDecimal subtract = initCutMoney.subtract(overcouponMoney);
			/*
			 * 最后一轮金额平摊（按商品数量）
			 */
			for (AverageMoneyModel model : pcList) {
				if(subtract.compareTo(BigDecimal.ZERO) <= 0) {
					break;
				}
				if(model.getSkuNum() <= 0 || model.getSkuPrice() <= 0.0) {
					continue;
				}
				//最有平摊一次没有优惠的金额，向上取整，保证数量最小的，让人品奖励出来的金额保证为最小的
				BigDecimal skuCutMoney = subtract.divide(BigDecimal.valueOf(model.getSkuNum()),2,BigDecimal.ROUND_FLOOR);
				if(LDSELLERCODE.equals(model.getSmallSellerCode())) {
					skuCutMoney = skuCutMoney.setScale(0,BigDecimal.ROUND_FLOOR);
				}
				
				BigDecimal subtract2 = resultMap.get(model.getSkuCode()).add(skuCutMoney);//此商品优惠的总金额之和
				if(subtract2.compareTo(BigDecimal.valueOf(model.getSkuPrice())) > 0) {//优惠金额最多只能是商品金额
					resultMap.put(model.getSkuCode(), BigDecimal.valueOf(model.getSkuPrice()));
					/*
					 * 这时   此商品优惠的总金额之和(subtract2) - 商品原价(model.getSkuPrice()) = 还没有进行优惠的金额 （subtract）
					 */
					subtract = subtract2.subtract(BigDecimal.valueOf(model.getSkuPrice()));
					
				} else {
					resultMap.put(model.getSkuCode(), subtract2);
					subtract = subtract.subtract(skuCutMoney.multiply(BigDecimal.valueOf(model.getSkuNum())));
				}
			}
			
			if(subtract.compareTo(BigDecimal.ZERO)>0) {//如果优惠金额还没有优惠完
				
				for (AverageMoneyModel model : pcList) {//此循环只取一个商品，且商品金额不为0的商品

					if(subtract.compareTo(BigDecimal.ZERO) <= 0) {
						break;
					}
					if(model.getSkuNum() <= 0) {
						continue;
					}
					//最有平摊一次没有优惠的金额，向上取整，保证数量最小的，让人品奖励出来的金额保证为最小的
					BigDecimal skuCutMoney = subtract.divide(BigDecimal.valueOf(model.getSkuNum()),2,BigDecimal.ROUND_UP);
					if(LDSELLERCODE.equals(model.getSmallSellerCode())) {
						skuCutMoney = skuCutMoney.setScale(0,BigDecimal.ROUND_UP);
					}
					
					BigDecimal subtract2 = resultMap.get(model.getSkuCode()).add(skuCutMoney);//此商品优惠的总金额之和
					if(subtract2.compareTo(BigDecimal.valueOf(model.getSkuPrice())) > 0) {//优惠金额最多只能是商品金额
						resultMap.put(model.getSkuCode(), BigDecimal.valueOf(model.getSkuPrice()));
						/*
						 * 这时   此商品优惠的总金额之和(subtract2) - 商品原价(model.getSkuPrice()) = 还没有进行优惠的金额 （subtract）
						 */
						subtract = subtract2.subtract(BigDecimal.valueOf(model.getSkuPrice()));
						
					} else {
						resultMap.put(model.getSkuCode(), subtract2);
						subtract = subtract.subtract(skuCutMoney.multiply(BigDecimal.valueOf(model.getSkuNum())));
					}
					
				
				}
			}
			
		}
		
		return resultMap;
		
	}
	/**
	 * 递归平摊多优惠的金额
	 * @param resultMap
	 * @param averageModel
	 * @param allSkuMoney
	 * @param initCutMoney
	 * @return
	 */
	private static Map<String , BigDecimal> averageMoney(List<AverageMoneyModel> averageModel ,BigDecimal initCutMoney) {
		Map<String , BigDecimal> resultMap = new HashMap<String , BigDecimal>();
		BigDecimal overcouponMoney = BigDecimal.ZERO;
		
		BigDecimal allSkuMoney = BigDecimal.ZERO;
		for (AverageMoneyModel model : averageModel) {
			allSkuMoney = allSkuMoney.add( BigDecimal.valueOf(model.getSkuPrice() * model.getSkuNum()));
			//初始化返回金额
			resultMap.put(model.getSkuCode(), BigDecimal.valueOf(model.getSkuPrice()));
		}
		for (AverageMoneyModel model : averageModel) {
			if(model.getSkuPrice() <=0) {
				//商品价格为0的不进行平摊金额
				resultMap.put(model.getSkuCode(), BigDecimal.ZERO);
				continue;
			}

			//计算商品所占优惠的金额( （单个商品金额/总商品金额）*总优惠金额   )
			BigDecimal averageMoney = initCutMoney.multiply(BigDecimal.valueOf(model.getSkuPrice())).divide(allSkuMoney, 10, BigDecimal.ROUND_FLOOR);
			
			//计算商品的所占优惠金额向上取整是否大于原商品金额(优惠的金额不能大于商品的原价,如果大于，则取商品价格，也就是最多优惠的金额为商品的金额)
			if(averageMoney.compareTo(BigDecimal.valueOf(model.getSkuPrice()))<0) {
				resultMap.put(model.getSkuCode(), averageMoney);
				overcouponMoney = overcouponMoney.add(averageMoney.multiply(BigDecimal.valueOf(model.getSkuNum())));
			} else {
				resultMap.put(model.getSkuCode(), BigDecimal.valueOf(model.getSkuPrice()));
				overcouponMoney = overcouponMoney.add(BigDecimal.valueOf(model.getSkuPrice() * model.getSkuNum()));
			}
		}
		if(initCutMoney.compareTo(overcouponMoney) > 0) {//向下取整，必须小于
			//由于最后金额基本是小数，则进行向上取整
			BigDecimal subtract = initCutMoney.subtract(overcouponMoney).setScale(10, BigDecimal.ROUND_FLOOR);
			//判断  进行下一轮扣减的金额要小于此次扣减的金额（越来越小才正确）,如果下一轮扣减的金额要==此次扣减的金额（相当于此次流程没有平摊金额），则退出循环
			if(subtract.compareTo( initCutMoney ) < 0) {
				
				for (AverageMoneyModel model : averageModel) {
					//计算将商品已经优惠的金额减掉
					model.setSkuPrice(BigDecimal.valueOf(model.getSkuPrice()).subtract(resultMap.get(model.getSkuCode())).setScale(10, BigDecimal.ROUND_FLOOR).doubleValue());
				}
				Map<String, BigDecimal> averageMoney = averageMoney ( averageModel, subtract );
				for (String key : resultMap.keySet()) {
					resultMap.put(key, resultMap.get(key).add(averageMoney.get(key)));
				}
				
			}
			
		}
		return resultMap;
	}
	
	/**
	 * 按商品数量，金额排序(升序)
	 * 优先数量升序排序。如果数量相等，则按金额降序排列,金额相等，则随机
	 * @param averageModel 需要排序字段
	 */
	private static void sortList(List<AverageMoneyModel> averageModel) {
		Collections.sort(averageModel, new Comparator<Object>() {
			public int compare(Object moenyOne, Object moneyTwo) {
				Integer one = ((AverageMoneyModel) moenyOne).getSkuNum();
				Integer two = ((AverageMoneyModel) moneyTwo).getSkuNum();
				if(one.compareTo(two) == 0) {
					Double one1 = ((AverageMoneyModel) moenyOne).getSkuPrice();
					Double two1 = ((AverageMoneyModel) moneyTwo).getSkuPrice();
					return two1.compareTo(one1);
				}
				return one.compareTo(two);
			}
		});
	}
	
	
	
	
	/*public static void main(String[] args) {
		List<AverageMoneyModel> averageModel = new ArrayList<AverageMoneyModel>();
		AverageMoneyModel model1 = new AverageMoneyModel();
		AverageMoneyModel model2 = new AverageMoneyModel();
		AverageMoneyModel model3 = new AverageMoneyModel();
		AverageMoneyModel model4 = new AverageMoneyModel();
		AverageMoneyModel model5 = new AverageMoneyModel();
	
		model1.setSkuCode("1");
		model1.setSkuNum(4);
		model1.setSkuPrice(15.54);
		//model1.setSmallSellerCode("SI2003");
		
		model2.setSkuCode("2");
		model2.setSkuNum(2);
		model2.setSkuPrice(13.42);
		//model2.setSmallSellerCode("SI2003");
		
		model3.setSkuCode("3");
		model3.setSkuNum(2);
		model3.setSkuPrice(151.0);
		model3.setSmallSellerCode("SI2003");
		
//		model4.setSkuCode("4");
//		model4.setSkuNum(2);
//		model4.setSkuPrice(290.00);
//		model4.setSmallSellerCode("SI2003");
		
		model5.setSkuCode("5");
		model5.setSkuNum(8);
		model5.setSkuPrice(34.25);
//		model5.setSmallSellerCode("SI2003");
		
		BigDecimal initCutMoney = new BigDecimal(20.00);//617.5+20+77
		
		averageModel.add(model1);
		averageModel.add(model2);
		averageModel.add(model3);
//		averageModel.add(model4);
		averageModel.add(model5);
		
		Map<String, BigDecimal> averageMoney = AverageMoneyService.toAverageMoney(averageModel, initCutMoney.setScale(2,BigDecimal.ROUND_HALF_UP));
		for (AverageMoneyModel m : averageModel) {
			System.out.println("商品编号："+m.getSkuCode()+";商品数量:"+m.getSkuNum()+";最终优惠的金额:"+averageMoney.get(m.getSkuCode()));
		}
		System.out.println(JSON.toJSONString(averageMoney));
	}*/
	
	
	
}
