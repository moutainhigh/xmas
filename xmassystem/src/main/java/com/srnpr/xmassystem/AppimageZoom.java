package com.srnpr.xmassystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;

public class AppimageZoom extends BaseClass {
	public int ImageZoom(String[] Picturepath) {
		// 获取图片屏宽
		String screenWidth = bConfig("xmassystem.screen_width");
		String[] screenValue = screenWidth.split(",");
		int Number =0 ;
		try {
			for (int i = 0; i < screenValue.length; i++) {
				for (int j = 0; j < Picturepath.length; j++) {
					long Reads = XmasKv.upFactory(EKvSchema.ImageZoom).hdel(screenValue[i], Picturepath[j]);
					if (Reads == 1) {
						Number++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Number;
	}
}
