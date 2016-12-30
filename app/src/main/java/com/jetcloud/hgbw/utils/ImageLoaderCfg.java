package com.jetcloud.hgbw.utils;

import android.graphics.Bitmap;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderCfg {
	public static DisplayImageOptions options = new DisplayImageOptions.Builder()
	// .showImageOnLoading(R.drawable.ic_stub) // 设置图片在下载期间显示的图片
	// .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
	// .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			// .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//
			// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			// .delayBeforeLoading(int delayInMillis)//int
			// delayInMillis为你设置的下载前的延迟时间
			// 设置图片加入缓存前，对bitmap进行设置
			// .preProcessor(BitmapProcessor preProcessor)
			// .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
			.displayer(new FadeInBitmapDisplayer(1))// 是否图片加载好后渐入的动画时间
			.build();// 构建完成
	public static DisplayImageOptions options1 = new DisplayImageOptions.Builder()
	// .showImageOnLoading(R.drawable.ic_stub) // 设置图片在下载期间显示的图片
	// .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
	// .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			// .delayBeforeLoading(int delayInMillis)//int
			// delayInMillis为你设置的下载前的延迟时间
			// 设置图片加入缓存前，对bitmap进行设置
			// .preProcessor(BitmapProcessor preProcessor)
			// .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
			.displayer(new FadeInBitmapDisplayer(1))// 是否图片加载好后渐入的动画时间
			.build();// 构建完成

	// 加载头像时用
	public static DisplayImageOptions options2 = new DisplayImageOptions.Builder()
	// .showImageOnLoading(R.drawable.ic_launcher) // 设置图片在下载期间显示的图片
	// .showImageForEmptyUri(R.drawable.ic_launcher)// 设置图片Uri为空或是错误的时候显示的图片
	// .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
			.cacheOnDisk(false)// 设置下载的图片是否缓存在SD卡中
			.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			// .delayBeforeLoading(int delayInMillis)//int
			// delayInMillis为你设置的下载前的延迟时间
			// 设置图片加入缓存前，对bitmap进行设置
			// .preProcessor(BitmapProcessor preProcessor)
			// .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
			.displayer(new FadeInBitmapDisplayer(1))// 是否图片加载好后渐入的动画时间
			.build();// 构建完成

	private static final String HEX_STRING = "0123456789ABCDEF";

	/**
	 * 把中文字符转换为带百分号的浏览器编码
	 *
	 * @param word
	 * @return
	 */
	public static String toBrowserCode(String word) {
		byte[] bytes = word.getBytes();

		//不包含中文，不做处理
		if (bytes.length == word.length())
			return word;

		StringBuilder browserUrl = new StringBuilder();
		String tempStr = "";

		for (int i = 0; i < word.length(); i++) {
			char currentChar = word.charAt(i);

			//不需要处理
			if ((int) currentChar <= 256) {

				if (tempStr.length() > 0) {
					byte[] cBytes = tempStr.getBytes();

					for (int j = 0; j < cBytes.length; j++) {
						browserUrl.append('%');
						browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0xf0) >> 4));
						browserUrl.append(HEX_STRING.charAt((cBytes[j] & 0x0f) >> 0));
					}
					tempStr = "";
				}

				browserUrl.append(currentChar);
			} else {
				//把要处理的字符，添加到队列中
				tempStr += currentChar;
			}
		}
		return browserUrl.toString();
	}
}
