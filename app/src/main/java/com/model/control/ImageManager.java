package com.model.control;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Image;


/**
 * 图片统一管理：
 *  根据该图片名判断在hashtable中是否存在，若不存在则创建并保存到hashtable中，
 *  若该图片存在则获得该图片，并判断内存是否溢出
 * @author Administrator
 *
 */

public class ImageManager {
	//用来保存图片
	private Hashtable imgTable;
	//用来保存图片名
	private Vector nameVector;
	//实例
	private static ImageManager instance;
//	//本地图片名
//	// 首页图片
//	private String[] HPImgName = {ImgName.HPBg,ImgName.HPBut};
//	//关卡选择图片名
//	private String[] lvlSelName = {};
//	//公告图片
//	private String[] postImg = {};
//	//宠物小屋图片
//	private String[] petHomeImg = {};
//	//游戏图片
//	private String[] gameImg= {};
//	//工坊图片
//	private String[] fabImg = {};
	
	public ImageManager(){
		this.imgTable = new Hashtable();
		nameVector = new Vector();
	}
	
	public static ImageManager getInstance(){
		if(instance == null){
			instance = new ImageManager();
		}
		return instance;
	}
	//将图片存于hashtable中
	public void addImg(String name,Image img){
		imgTable.put(name, img);
		nameVector.addElement(name);
	}
	
	//将图片于hashtable中删除
	public void removeImg(String name){
		imgTable.remove(name);
		//System.out.println("长度为 "+imgTable.size());
		nameVector.removeElement(name);
		//System.out.println("名字的长度为 "+nameVector.size());
	}
	
	//从hashtable中读取图片
	public Image getImg(String name){
		return (Image) imgTable.get(name);
	}
	//将hashtable中的图片清空
	public void clearImgs(){
		String name = "";
		for(int i = 0;i<nameVector.size();i++){
			name = (String) nameVector.elementAt(i);
			imgTable.remove(name);
		}
		nameVector.removeAllElements();
	}
	
	/**
	 * 获得剩余图片数量
	 */
	public int getImgNum(){
		return imgTable.size();
	}
	
	
}
