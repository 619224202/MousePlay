package com.model.control;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.studio.motionwelder.MSpriteData;
import com.studio.motionwelder.MSpriteLoader;

public class MSDateManager {
	/**
	 * 用来保存以.anu文件为文件名的对应图片----key 如hero---hero1,hero2,hero3,其中
	 */
	public Hashtable anuProTable = new Hashtable();

	/**
	 * hashTable用来保存MSpriteData信息，场景切换时清空所有MSpriteData信息key hero----heroData
	 */
	private Hashtable dataTable = new Hashtable();
	/**
	 * 用来表示名字对应的资源文件 hero----hero.anu hero1---hero1.png hero2---hero2.png
	 */
	private Hashtable fileNameTable = new Hashtable();
	/**
	 * 
	 */

	/**
	 * Vector用来保存文件名
	 */
	private Vector nameVector = new Vector();
	/**
	 * 图片切割的信息
	 */
	private Hashtable pngSplitTable = new Hashtable();

	/**
	 * 具体实例
	 */
	private static MSDateManager instance;

	/**
	 * 获得信息
	 */
	public static MSDateManager getInstance() {
		if (instance == null) {
			instance = new MSDateManager();
		}
		return instance;
	}

	/**
	 * 将MSpriteData保存在hashTable中，以命名为来存取
	 * 
	 * @param name：anu文件名
	 * @param data:MSpriteData对象
	 */
	public void addData(String anuKey, MSpriteData data) {
		nameVector.addElement(anuKey);
		dataTable.put(anuKey, data);
	}

	/**
	 * 根据anu文件名获得dataTable中的MSpriteData值
	 * 
	 * @param name
	 * @return
	 */
	public MSpriteData getMSData(String anuKey) {
		MSpriteData data = (MSpriteData) dataTable.get(anuKey);
		// Enumeration k = dataTable.keys();
		// for(int i = 0;i<k.)
		return data;
	}

	/**
	 * 根据anu文件名删除dataTable中的MSpriteData值
	 * 
	 * @param name
	 */
	public void removeAnuKey(String anuKey, boolean flag) {
		if (flag) {
			String[] imgKeys = (String[]) anuProTable.get(anuKey);
			if (imgKeys != null) {
				for (int i = imgKeys.length - 1; i >= 0; i--) {
					ImageManager.getInstance().removeImg(imgKeys[i]);
					// System.out.println("ddddddddddddddddddddddddddddddd");
				}
			}
		}
		anuProTable.remove(anuKey);
		dataTable.remove(anuKey);
		nameVector.removeElement(anuKey);
	}

	/**
	 * 删除指定的anu
	 * 
	 * @param anuKey
	 */
	public void removeAnu(String anuKey) {
		String[] imgKeys = (String[]) anuProTable.get(anuKey);
		if (imgKeys != null) {
			for (int i = imgKeys.length - 1; i >= 0; i--) {
				ImageManager.getInstance().removeImg(imgKeys[i]);
				// System.out.println("ddddddddddddddddddddddddddddddd");
			}
		}
		dataTable.remove(anuKey);
		nameVector.removeElement(anuKey);
	}

	/**
	 * 清空所有数据信息及name信息
	 */
	public void clearAllAnuKey() {
		String anuName = "";
		for (int i = nameVector.size() - 1; i >= 0; i--) {
			anuName = (String) nameVector.elementAt(i);
			removeAnuKey(anuName, true);
		}
		nameVector.removeAllElements();

		// System.out.println("data的长度为 = "+dataTable.size());
	}

	private String[] anuAndImgPath;

	/**
	 * 创建新对象
	 */
	public MSpriteData getMSpriteData(String anuKey, int trans) {
		anuAndImgPath = MSDateManager.getInstance().getImgKeysByAnuKey(anuKey);
		MSpriteData data = MSDateManager.getInstance().getMSData(anuKey);
		if (data == null || trans != 0) {
			String name1 = "/" + anuAndImgPath[0];
			try {
				ResourceLoader.getInstance().setTrans(trans);
				data = MSpriteLoader.loadMSprite(name1, false, ResourceLoader
						.getInstance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MSDateManager.getInstance().addData(anuKey, data);
		}
		return data;
	}

	public String[] getAnuAndImgPath() {
		return anuAndImgPath;
	}

	public Hashtable getAnuProTable() {
		return anuProTable;
	}

	public void setAnuProTable(Hashtable anuProTable) {
		this.anuProTable = anuProTable;
	}

	public Hashtable getDataTable() {
		return dataTable;
	}

	public void setDataTable(Hashtable dataTable) {
		this.dataTable = dataTable;
	}

	public Hashtable getFileNameTable() {
		return fileNameTable;
	}

	public void setFileNameTable(Hashtable fileNameTable) {
		this.fileNameTable = fileNameTable;
	}

	public Vector getNameVector() {
		return nameVector;
	}

	public void setNameVector(Vector nameVector) {
		this.nameVector = nameVector;
	}

	public Hashtable getPngSplitTable() {
		return pngSplitTable;
	}

	public void setPngSplitTable(Hashtable pngSplitTable) {
		this.pngSplitTable = pngSplitTable;
	}

	public static void setInstance(MSDateManager instance) {
		MSDateManager.instance = instance;
	}

	/**
	 * 加入文件名及图片名
	 * 
	 * @param anuName:anu文件名
	 * @param imgName:anu文件要加载的文件名
	 */
	public void addAnuKey(String anuKey, String[] imgKey) {
		if (anuProTable.containsKey(anuKey)) {
			return;
		}
		anuProTable.put(anuKey, imgKey);
	}

	/**
	 * removeName
	 */
	public void removeAnuKey(String anuKey) {
		if (anuProTable.containsKey(anuKey)) {
			// System.out.println("==================remove"+anuBuf.toString()+"==================");
			anuProTable.remove(anuKey);
		}
	}

	public void removeAllAnuKey() {
		Enumeration e = anuProTable.keys();
		while (e.hasMoreElements()) {
			String key = e.nextElement().toString();
			removeAnuKey(key);
		}
	}

	/**
	 * 根据anu文件名获得anuProTable中的图片名
	 * 
	 * @param anuName:anu文件名
	 */
	public String[] getImgKeysByAnuKey(String anuKey) {
		String[] s = (String[]) anuProTable.get(anuKey);
		return s;
	}

	public String getFileNameByFileKey(String fileKey) {
		return (String) fileNameTable.get(fileKey);
	}

	public boolean addFileNameAndKey(String fileKey, String fileName) {

		if (!fileNameTable.containsKey(fileKey)) {
			fileNameTable.put(fileKey, fileName);
			return true;
		} else {
			System.out
					.println(fileKey
							+ "====================该名称的对象已经在HashTable中保存了，请检查=============="
							+ fileNameTable.get(fileKey));
		}
		return false;
	}

	public Vector getTblNameVector() {
		Enumeration e = fileNameTable.keys();
		Vector v = new Vector();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = (String) fileNameTable.get(key);
			if (value.endsWith("png")) {
				v.addElement(key);
			}
		}
		return v;
	}

	public boolean removeFileKey(String fileKey) {
		if (!fileNameTable.containsKey(fileKey)) {
			String fileName = (String) fileNameTable.get(fileKey);
			fileNameTable.remove(fileKey);
			return true;
		}
		return false;
	}

	public void removeAllFileKey() {
		Enumeration keys = fileNameTable.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			fileNameTable.remove(key);
		}
	}

	public void addPngSplitTable(String key, int[] value) {
		pngSplitTable.put(key, value);
	}

	public int[] getSplitTable(String key) {
		int[] splitInt = new int[2];
		if (pngSplitTable.containsKey(key)) {
			splitInt = (int[]) pngSplitTable.get(key);
		}
		return splitInt;
	}

	public void removePngSplitTable(String key) {
		if (pngSplitTable.containsKey(key)) {
			pngSplitTable.remove(key);
		}
	}

	public void removeAllPngSplit() {
		Enumeration keys = pngSplitTable.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement().toString();
			pngSplitTable.remove(key);
		}
	}

	/**
	 * 清空所有信息
	 */
	public void clearAll() {
		removeAllFileKey();
		clearAllAnuKey();
		removeAllPngSplit();
		removeAllAnuKey();
	}
}
