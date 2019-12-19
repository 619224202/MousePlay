package com.model.control;

/*
 * COPYRIGHT - MOTIONWELDER
 */

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.studio.motionwelder.MSprite;
import com.studio.motionwelder.MSpriteImageLoader;

/**
 * Resource Loader: Class to load Images
 * 
 * @author Nitin Pokar (pokar.nitin@gmail.com)
 * 
 */
public class ResourceLoader implements MSpriteImageLoader {

	/** Making Class Singleton */
	/**
	 * 首页图片
	 */

	private int trans;

	static private ResourceLoader resourceLoader;

	private ResourceLoader() {
	}

	static public ResourceLoader getInstance() {
		if (resourceLoader == null) {
			resourceLoader = new ResourceLoader();
		}
		return resourceLoader;
	}

	public void setTrans(int trans) {
		this.trans = trans;
	}

	/**
	 * Function : LoadImage will be called while loading .anu. This version of
	 * Load Image will be called when .anu is loaded without chopping images In
	 * this example we have not loaded any .anu where we have passed false to
	 * MSpriteLoader, hence this function will never be called
	 */
	public Image[] loadImage(String spriteName, int imageId,
			int orientationUsedInStudio) {
		// returning null, as we are clipping the image in this example, this
		// function will never be called
		if (this.trans != 0)
			orientationUsedInStudio = this.trans;
		boolean doYouNeedHFlippedSpriteInYourgame = false;
		boolean doYouNeedVFlippedSpriteInYourgame = false;

		MyImg baseImage = null;
		// Image baseImage=null;

		String sName = spriteName.substring(1);
		// System.out.println(sName);
		String[] anuAndImgPath = MSDateManager.getInstance().getAnuAndImgPath();
		String imgName = anuAndImgPath[imageId + 1];
		// System.out.println(imgKeys);
		baseImage = loadImage(imgName);
		// if(imgKeys.startsWith("pngtank")){
		// doYouNeedHFlippedSpriteInYourgame = true;
		// System.out.println("加载镜像图片>>>>>>>>>");
		// }
		// if(imgName.equals("hero/skill4Guang")){
		// doYouNeedHFlippedSpriteInYourgame=true;
		// doYouNeedVFlippedSpriteInYourgame=true;
		// }
		Image[] image = new Image[3];

		// image[0] =
		// Image.createImage(baseImage.getImg(),0,0,baseImage.getWidth(),baseImage.getHeight(),Sprite.TRANS_NONE);
		image[0] = baseImage.getImg();
		/** Please don't load this if using Nokia Direct Graphics */
		if (orientationUsedInStudio == MSprite.ORIENTATION_FLIP_H
				|| orientationUsedInStudio == MSprite.ORIENTATION_FLIP_BOTH_H_V
				|| doYouNeedHFlippedSpriteInYourgame) {
			// image[1] = Image.createImage(baseImage.getImg(), 0, 0, baseImage
			// .getWidth(), baseImage.getHeight(), Sprite.TRANS_MIRROR);
			String imgNameF = anuAndImgPath[(anuAndImgPath.length - 1) / 2 + 1+imageId];
			image[1] = loadImage(imgNameF).getImg();
		}

		/** Please don't load this if using Nokia Direct Graphics */
		if (orientationUsedInStudio == MSprite.ORIENTATION_FLIP_V
				|| orientationUsedInStudio == MSprite.ORIENTATION_FLIP_BOTH_H_V
				|| doYouNeedVFlippedSpriteInYourgame)
			image[2] = Image.createImage(baseImage.getImg(), 0, 0, baseImage
					.getWidth(), baseImage.getHeight(),
					Sprite.TRANS_MIRROR_ROT180);
		// ImageManager.getInstance().clearImgs();
		// System.out.println(ImageManager.getInstance().getImgNum());
		return image;
	}

	/**
	 * If you are using Nokia DirectGraphics, please don't load flipped image,
	 * Instead modify MPlayer to flip it at runtime
	 * 
	 * Function : LoadImageClip will be called while loading .anu. This version
	 * of Load Image will be called when .anu is loaded with chopped images In
	 * this example we have loaded .anu with passing true in MSpriteLoader,
	 * hence this function will be called
	 */
	public Image[] loadImageClip(String spriteName, int imageId, int x, int y,
			int w, int h, int orientationUsedInStudio) {
		// determine whether i need flipped version in my game
		boolean doYouNeedHFlippedSpriteInYourgame = false;
		boolean doYouNeedVFlippedSpriteInYourgame = false;

		MyImg baseImage = null;
		// if(spriteName.equals("/anu/sy.anu")){
		// //baseImage = loadImage("/mongo/banana.png");
		// }
		System.out.println("/" + spriteName + ".anu" + "    imageId = "
				+ imageId);
		String imgKeys = MSDateManager.getInstance().getImgKeysByAnuKey(
				spriteName)[imageId];
		baseImage = loadImage(imgKeys);

		Image[] image = new Image[3];
		image[0] = Image.createImage(baseImage.getImg(), x, y, w, h,
				Sprite.TRANS_NONE);

		/** Please don't load this if using Nokia Direct Graphics */
		if (orientationUsedInStudio == MSprite.ORIENTATION_FLIP_H
				|| orientationUsedInStudio == MSprite.ORIENTATION_FLIP_BOTH_H_V
				|| doYouNeedHFlippedSpriteInYourgame)
			image[1] = Image.createImage(baseImage.getImg(), x, y, w, h,
					Sprite.TRANS_MIRROR);

		/** Please don't load this if using Nokia Direct Graphics */
		if (orientationUsedInStudio == MSprite.ORIENTATION_FLIP_V
				|| orientationUsedInStudio == MSprite.ORIENTATION_FLIP_BOTH_H_V
				|| doYouNeedVFlippedSpriteInYourgame)
			image[2] = Image.createImage(baseImage.getImg(), x, y, w, h,
					Sprite.TRANS_MIRROR_ROT180);

		return image;
	}

	public static MyImg loadImage(String imgName) {
		MyImg img = new MyImg(imgName);
		// img.release();
		try {
			return img;
			// return Image.createImage("/"+str+".png");
		} catch (Exception e) {
			System.out.println("Error loading Image " + imgName);
		}
		return null;
	}
}
