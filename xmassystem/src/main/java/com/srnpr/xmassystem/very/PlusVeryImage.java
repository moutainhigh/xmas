package com.srnpr.xmassystem.very;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.xmassystem.enumer.EImageWidthSuffix;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.EncodeHelper;
import com.srnpr.zapcom.basehelper.GsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapweb.extendapi.ImageZoomResult;
import com.srnpr.zapweb.webmodel.MFileItem;
import com.srnpr.zapweb.websupport.ImageMagicSupport;

public class PlusVeryImage extends BaseClass {

	/**
	 * @see PlusVeryImage#upImageZoomList(List, int)
	 */
	@Deprecated
	public Map<String, MFileItem> upImageZoom(String sImage, int iWidth) {
		return upImageZoom(sImage, iWidth, "");
	}
	
	/**
	 * @see PlusVeryImage#upImageZoomList(List, int, String)
	 */
	@Deprecated
	public Map<String, MFileItem> upImageZoom(String sImage, int iWidth,
			String sFormat) {
		return upImageZoom("", sImage, iWidth, sFormat);
	}
	
	public Map<String,Integer> getImagesKg(String url){
		Map<String,Integer> retMap = new HashMap<String,Integer>();
		int width = 0;
		int height = 0;
		//查看缓存中是否有图片对应的宽高
		String wh = XmasKv.upFactory(EKvSchema.ImageWidthHeight).get(url);
		
		if(StringUtils.isNotBlank(wh)) {
			String[] whArr = wh.split("-");
			width = Integer.valueOf(whArr[0]);
			height = Integer.valueOf(whArr[1]);
		} else {
			int[] is = new ImageMagicSupport().getImageInfoByUrl(url);
			width = is[0];
			height = is[1];
			XmasKv.upFactory(EKvSchema.ImageWidthHeight).set(url, is[0]+"-"+is[1]);			
			//设置图片过期时间
			String imageWidthHeight = bConfig("xmassystem.imageWidthHeight");
			// 图片宽高获取失败的情况下只缓存30秒
			if(width == 10 && height == 10){
				imageWidthHeight = "30";
			}
			XmasKv.upFactory(EKvSchema.ImageWidthHeight).expire(url,Integer.valueOf(imageWidthHeight));
		}
		retMap.put("width", width);
		retMap.put("height", height);
		return retMap;
	}

	/**
	 * @see PlusVeryImage#upImageZoomList(String, List, int, String)
	 */
	@Deprecated
	public Map<String, MFileItem> upImageZoom(String sBaseKey, String sImage, int iWidth,
			String sFormat) {

		//iWidth为0时返回原图
		if (iWidth <= 0) {
			iWidth = 1080;
		}
		
		if(StringUtils.isEmpty(sBaseKey)) {
			sBaseKey = String.valueOf(iWidth) + sFormat;
		}

		Map<String, MFileItem> fileMap = new HashMap<String, MFileItem>();

		String sUrl = bConfig("xmassystem.image_zoom_url");
		if (StringUtils.isNotBlank(sUrl) && sImage.length() > 10) {

			ArrayList<String> aList = new ArrayList<String>();

			for (String s : sImage.split(",")) {

				String sValue = XmasKv.upFactory(EKvSchema.ImageZoom).hget(
						sBaseKey, s);

				if (StringUtils.isNotBlank(sValue)) {

					MFileItem mFileItem = new GsonHelper().fromJson(sValue,
							new MFileItem());
					if (mFileItem != null
							&& StringUtils.isNotBlank(mFileItem.getFileUrl())) {
						fileMap.put(s, mFileItem);
					} else {
						aList.add(s);
					}

				} else {
					//如果缓存中没有对应宽度的图片，比较一次图片实际的宽度和需要的宽度，如果需要的宽度比实际的宽度大，则直接取原图
					try {
						int width = 0;
						int height = 0;
						//查看缓存中是否有图片对应的宽高
						String wh = XmasKv.upFactory(EKvSchema.ImageWidthHeight).get(s);
						
						if(StringUtils.isNotBlank(wh)) {
							String[] whArr = wh.split("-");
							width = Integer.valueOf(whArr[0]);
							height = Integer.valueOf(whArr[1]);
						} else {
							int[] is = new ImageMagicSupport().getImageInfoByUrl(s);
							width = is[0];
							height = is[1];
							XmasKv.upFactory(EKvSchema.ImageWidthHeight).set(s, is[0]+"-"+is[1]);
							
							//设置图片过期时间
							String imageWidthHeight = bConfig("xmassystem.imageWidthHeight");
							// 图片宽高获取失败的情况下只缓存30秒
							if(width == 10 && height == 10){
								imageWidthHeight = "30";
							}
							XmasKv.upFactory(EKvSchema.ImageWidthHeight).expire(s,Integer.valueOf(imageWidthHeight));
						}
						
						/*
						 * 判断图片格式是否进行压缩
						 */
						boolean isZoom = true;
						String currentImageF = s.substring(s.lastIndexOf("."));//当前图片的格式
						//过滤不进行压缩的图片
						String notAllowZoomImage = bConfig("xmassystem.notAllowZoomImage");
						if(StringUtils.isNotBlank(notAllowZoomImage)) {
							String[] imgFArr = notAllowZoomImage.split(",");
							for (String imgf : imgFArr) {
								if(currentImageF.equals(imgf)) {//如果该图片不允许压缩，则返原图
									isZoom = false;
									break;
								}
							}
						}
						
						if( isZoom == false || iWidth >= width ) {//系统设置图片不进行压缩，或者 要压缩图片宽度 大于 原图的宽度，则不进行压缩图片，直接返回原图   fq++
							MFileItem tempItem = new MFileItem();
							tempItem.setFileUrl(s);
							tempItem.setOriginHeight(height);
							tempItem.setOriginWidth(width);
							//宽高要求为  前端(app)需要尺寸宽高 fq++   
							BigDecimal divide = BigDecimal.valueOf(iWidth).divide(BigDecimal.valueOf(width), 10, BigDecimal.ROUND_UP);
							BigDecimal newHeight = divide.multiply(BigDecimal.valueOf(height)).setScale(0, BigDecimal.ROUND_UP);
							tempItem.setHeight(newHeight.intValue());
							tempItem.setWidth(iWidth);
							
							// 宽高异常的图片不返回
							if(width != 10 || height != 10){
								fileMap.put(s, tempItem);
							}
						} else {
							aList.add(s);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}

			}

			if (aList != null && aList.size() > 0) {
				try {

					/*
					 * String sResponse = WebClientSupport.create().doGet(
					 * FormatHelper.formatString(sUrl, String .valueOf(iWidth),
					 * EncodeHelper .urlEncode(StringUtils.join(aList, ","))));
					 */
					//iWidth为0时返回原图
					String sResponse = WebClientSupport.upPost(
							sUrl,
							new MDataMap("zoomWidth", String.valueOf(iWidth),
									"imageFormat", sFormat, "imageUrl",
									EncodeHelper.urlEncode(StringUtils.join(
											aList, ","))));

					Map<String, MFileItem> resultMap = new GsonHelper()
							.fromJson(sResponse, new ImageZoomResult())
							.getMapImages();

					for (String sKey : resultMap.keySet()) {
						MFileItem mFileItem = resultMap.get(sKey);
						mFileItem.setOriginHeight(mFileItem.getHeight());
						mFileItem.setOriginWidth(mFileItem.getWidth());
						fileMap.put(sKey, resultMap.get(sKey));

						XmasKv.upFactory(EKvSchema.ImageZoom).hset(sBaseKey,
								sKey, GsonHelper.toJson(resultMap.get(sKey)));

					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		}

		return fileMap;

	}
	
	/**
	 * 
	 * 按系统规则压缩图（只能压缩到接近系统指定宽度的图片）
	 * 
	 * @param isWebPcPic 是否为商品列表图（商品列表图会将压缩的宽度*0.6  进行压缩）
	 * 【false:将压缩的宽度maxWidth进行压缩(redis 存储的key为  “宽+@param widthSuffix ”)】 
	 * 【true :将压缩的宽度maxWidth*0.6进行压缩(redis 存储的key为  “宽+@param widthSuffix ”)】
	 * 
	 * @param maxWidth 
	 * @param picUrlArr
	 * @param picType
	 * @param widthSuffix  redis 存储为  key+widthSuffix
	 * @return
	 */
	@Deprecated
	public Map<String, MFileItem> compressImage(boolean isWebPcPic, int maxWidth, String sUrls, String picType,EImageWidthSuffix widthSuffix) {
		
		Map<String, MFileItem> fileMap = new HashMap<String, MFileItem>();
		
		/**
		 * 压缩图片之前，需要取最接口该宽度的尺寸进行压缩。防止压缩的种类过多而无法在上传图片时先进行图片的压缩
		 */
		String sysWidths = bConfig("xmassystem.imageWidth");//取规定尺寸的宽度
		String[] sysWidthArr = sysWidths.split(",");
		List<Integer> widthList = new ArrayList<Integer>();
		for (String wid : sysWidthArr) {
			widthList.add(Integer.valueOf(wid));
		}
		if(widthList.size() > 0) {
			//从小到大进行排序
			Collections.sort(widthList, new Comparator<Integer>() {
				public int compare(Integer widthOne, Integer widthTwo) {
					return widthOne.compareTo(widthTwo);
				}
			});
		}
		
		
		for (int i = 0; i < widthList.size(); i++) {
			if(i == widthList.size() -1 ) {
				//如果为最后一位元素，则直接获取
				maxWidth = Integer.valueOf(widthList.get(i));
			}
			
			if(maxWidth <= Integer.valueOf(widthList.get(i))) {
				maxWidth = Integer.valueOf(widthList.get(i));
				break;
			}
		}
		if(isWebPcPic) {
			fileMap = upImageZoom(widthAppendSuffix(String.valueOf(maxWidth),widthSuffix) , StringUtils.join(StringUtils.split(sUrls, "|"), ",").replace(" ", ""), BigDecimal.valueOf(maxWidth*0.6).setScale(0,BigDecimal.ROUND_FLOOR).intValue(), picType);
		} else {
			fileMap = upImageZoom(widthAppendSuffix(String.valueOf(maxWidth),widthSuffix) , StringUtils.join(StringUtils.split(sUrls, "|"), ",").replace(" ", ""), maxWidth, picType);
		}
		
		return fileMap;
	}
	
	/**
	 * 获取图片信息
	 * @return
	 */
	public ImgInfo getImgInfo(String imageUrl) {
		ImgInfo  info = null;
		try {
			 URL url = new URL(imageUrl);
			
			InputStream imageStream = url.openStream();
			BufferedImage sourceImg =ImageIO.read(imageStream);   
			URLConnection uc = url.openConnection();
			
			info = new ImgInfo();
			info.setHeight(sourceImg.getHeight());
			info.setWidth(sourceImg.getWidth());
			info.setImageSize(uc.getContentLength());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}  
        System.out.println(JSON.toJSONString(info));
		return info;
	
	}
	
	/**
	 * 获取图片信息 返回值model
	 * @author fq
	 *
	 */
	public class ImgInfo {
		//图片大小
		private long imageSize = 0;
		//图片宽度
		private Integer width = 0;
		//图片高度
		private Integer height = 0;
		public long getImageSize() {
			return imageSize;
		}
		public void setImageSize(long imageSize) {
			this.imageSize = imageSize;
		}
		public Integer getWidth() {
			return width;
		}
		public void setWidth(Integer width) {
			this.width = width;
		}
		public Integer getHeight() {
			return height;
		}
		public void setHeight(Integer height) {
			this.height = height;
		}
		
		
	}
	
	public static void main(String[] args) {
		
		
		
		System.out.println(String.valueOf(EImageWidthSuffix.SALESIMAGES));
	}
	
	public String widthAppendSuffix (String width , EImageWidthSuffix fix) {
		return width + String.valueOf(fix);
	}
	
	public Map<String, MFileItem> upImageZoomList(List<String> sImage, int iWidth) {
		return upImageZoomList(sImage, iWidth, "");
	}
	
	public Map<String, MFileItem> upImageZoomList(List<String> sImage, int iWidth,
			String sFormat) {
		return upImageZoomList("", sImage, iWidth, sFormat);
	}

	public Map<String, MFileItem> upImageZoomList(String sBaseKey, List<String> sImage, int iWidth,
			String sFormat) {

		//iWidth为0时返回原图
		if (iWidth <= 0) {
			iWidth = 1080;
		}
		
		if(StringUtils.isEmpty(sBaseKey)) {
			sBaseKey = String.valueOf(iWidth) + sFormat;
		}

		Map<String, MFileItem> fileMap = new HashMap<String, MFileItem>();

		String sUrl = bConfig("xmassystem.image_zoom_url");
		ArrayList<String> aList = new ArrayList<String>();

		for (String s : sImage) {
			s = StringUtils.trimToEmpty(s);
			if(s.length() < 10) continue;
			
			String sValue = XmasKv.upFactory(EKvSchema.ImageZoom).hget(
					sBaseKey, s);

			if (StringUtils.isNotBlank(sValue)) {

				MFileItem mFileItem = new GsonHelper().fromJson(sValue,
						new MFileItem());
				if (mFileItem != null
						&& StringUtils.isNotBlank(mFileItem.getFileUrl())) {
					fileMap.put(s, mFileItem);
				} else {
					aList.add(s);
				}

			} else {
				//如果缓存中没有对应宽度的图片，比较一次图片实际的宽度和需要的宽度，如果需要的宽度比实际的宽度大，则直接取原图
				try {
					int width = 0;
					int height = 0;
					//查看缓存中是否有图片对应的宽高
					String wh = XmasKv.upFactory(EKvSchema.ImageWidthHeight).get(s);
					
					if(StringUtils.isNotBlank(wh)) {
						String[] whArr = wh.split("-");
						width = Integer.valueOf(whArr[0]);
						height = Integer.valueOf(whArr[1]);
					} else {
						int[] is = new ImageMagicSupport().getImageInfoByUrl(s);
						width = is[0];
						height = is[1];
						XmasKv.upFactory(EKvSchema.ImageWidthHeight).set(s, is[0]+"-"+is[1]);
						
						//设置图片过期时间
						String imageWidthHeight = bConfig("xmassystem.imageWidthHeight");
						// 图片宽高获取失败的情况下只缓存30秒
						if(width == 10 && height == 10){
							imageWidthHeight = "30";
						}
						XmasKv.upFactory(EKvSchema.ImageWidthHeight).expire(s,Integer.valueOf(imageWidthHeight));
					}
					
					/*
					 * 判断图片格式是否进行压缩
					 */
					boolean isZoom = true;
					String currentImageF = s.substring(s.lastIndexOf("."));//当前图片的格式
					//过滤不进行压缩的图片
					String notAllowZoomImage = bConfig("xmassystem.notAllowZoomImage");
					if(StringUtils.isNotBlank(notAllowZoomImage)) {
						String[] imgFArr = notAllowZoomImage.split(",");
						for (String imgf : imgFArr) {
							if(currentImageF.equals(imgf)) {//如果该图片不允许压缩，则返原图
								isZoom = false;
								break;
							}
						}
					}
					
					if( isZoom == false || iWidth >= width ) {//系统设置图片不进行压缩，或者 要压缩图片宽度 大于 原图的宽度，则不进行压缩图片，直接返回原图   fq++
						MFileItem tempItem = new MFileItem();
						tempItem.setFileUrl(s);
						tempItem.setOriginHeight(height);
						tempItem.setOriginWidth(width);
						//宽高要求为  前端(app)需要尺寸宽高 fq++   
						BigDecimal divide = BigDecimal.valueOf(iWidth).divide(BigDecimal.valueOf(width), 10, BigDecimal.ROUND_UP);
						BigDecimal newHeight = divide.multiply(BigDecimal.valueOf(height)).setScale(0, BigDecimal.ROUND_UP);
						tempItem.setHeight(newHeight.intValue());
						tempItem.setWidth(iWidth);
						
						// 宽高异常的图片不返回
						if(width != 10 || height != 10){
							fileMap.put(s, tempItem);
						}
					} else {
						aList.add(s);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		}

		if (aList != null && aList.size() > 0) {
			try {

				/*
				 * String sResponse = WebClientSupport.create().doGet(
				 * FormatHelper.formatString(sUrl, String .valueOf(iWidth),
				 * EncodeHelper .urlEncode(StringUtils.join(aList, ","))));
				 */
				List<String> urls = new ArrayList<String>();
				for(String v : aList) {
					// 每个url先进行一次url编码
					urls.add(EncodeHelper.urlEncode(v));
				}
				
				//iWidth为0时返回原图
				String sResponse = WebClientSupport.upPost(
						sUrl,
						new MDataMap("zoomWidth", String.valueOf(iWidth),
								"imageFormat", sFormat, "imageUrl",
								EncodeHelper.urlEncode(StringUtils.join(urls, ","))));

				Map<String, MFileItem> resultMap = new GsonHelper()
						.fromJson(sResponse, new ImageZoomResult())
						.getMapImages();

				for (String sKey : resultMap.keySet()) {
					MFileItem mFileItem = resultMap.get(sKey);
					mFileItem.setOriginHeight(mFileItem.getHeight());
					mFileItem.setOriginWidth(mFileItem.getWidth());
					fileMap.put(sKey, resultMap.get(sKey));

					XmasKv.upFactory(EKvSchema.ImageZoom).hset(sBaseKey,
							sKey, GsonHelper.toJson(resultMap.get(sKey)));

				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		return fileMap;

	}
	
}
