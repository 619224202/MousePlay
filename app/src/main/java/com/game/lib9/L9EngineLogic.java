package com.game.lib9;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.model.base.JGRectangle;
import com.model.tool.PubToolKit;
import com.model.tool.SortedArray;

public class L9EngineLogic {

	private static L9EngineLogic instance;

	public static L9EngineLogic getInstance() {
		if (instance == null) {
			instance = new L9EngineLogic();
		}
		return instance;
	}

	public boolean in_parallel_upd = false;
	public SortedArray objects = new SortedArray(80); /* String->JGObject */
	SortedArray obj_to_remove = new SortedArray(40); /* String */
	Vector obj_spec_to_remove = new Vector(20, 40); /* (String,Int) */
	SortedArray obj_to_add = new SortedArray(40); /* JGObject */

	JGRectangle tmprect1 = new JGRectangle();
	JGRectangle tmprect2 = new JGRectangle();

	L9Object[] srcobj = new L9Object[50];
	L9Object[] dstobj = new L9Object[50];

	/**
	 * Add new object now. Old object with the same name is replaced
	 * immediately, and its remove() method called. Skips calling objects.put
	 * when skip_actual_add=true. This is useful if the caller optimises the
	 * objects.add by adding an entire array at once
	 */
	public void addObject(L9Object obj, boolean skip_actual_add) {
		int idx = objects.get(obj.getObjKey());
		if (idx >= 0) {
			L9Object old_obj = (L9Object) objects.values[idx];
			// disable object so it doesn't call engine on removal
			old_obj.removeDone();
			// ensure any dispose stuff in the object is called
			old_obj.remove();
		}
		if (!skip_actual_add)
			objects.put(obj.getObjKey(), obj);
	}

	/* ====== objects from canvas ====== */

	/**
	 * 增加需要添加的Object添加到obj_to_add中 若不在碰撞中则直接添加Object，保存在这边
	 */
	public void markAddObject(L9Object obj) {
		obj_to_add.put(obj.depth + "-" + obj.getObjType() + "-" + obj.id, obj);
	}

	/**
	 * 增加需要删除的object类保存到obj_to_remove中
	 * 
	 * @param obj
	 */
	public void markRemoveObject(L9Object obj) {
		obj_to_remove.put(obj.depth + "-" + obj.getObjType() + "-" + obj.id,
				obj);
	}

	/** Mark object for removal. */
	void markRemoveObject(String index) {
		int idx = objects.get(index);
		if (idx < 0)
			return;
		obj_to_remove.put(index, (L9Object) objects.values[idx]);
	}

	void markRemoveObjects(String prefix, int cidmask, boolean suspended_obj) {
		obj_spec_to_remove.addElement(prefix);
		obj_spec_to_remove.addElement(new Integer(cidmask));
		obj_spec_to_remove.addElement(new Boolean(suspended_obj));
	}

	public void checkCollision(int srccid, int dstcid) {

		if (in_parallel_upd) {
			System.out.println("");
			return;
		}
		in_parallel_upd = true;
		if (objects.size > srcobj.length) {
			// grow arrays to make objects fit
			srcobj = new L9Object[objects.size + 50];
			dstobj = new L9Object[objects.size + 50];
		}
		int srcsize = 0;
		int dstsize = 0;
		/* get all matching objects */
		JGRectangle sr = tmprect1;
		JGRectangle dr = tmprect2;
		for (int i = 0; i < objects.size; i++) {
			L9Object o = (L9Object) objects.values[i];
			if (o.isSuspend())
				continue;
			if ((o.getObjType() & srccid) != 0) {
				srcobj[srcsize++] = o;
			}
			if ((o.getObjType() & dstcid) != 0) {
				dstobj[dstsize++] = o;
			}
		}
		/* check collision */
		for (int si = 0; si < srcsize; si++) {
			L9Object srco = srcobj[si];
			if (!srco.isAlive())
				continue;
			if (!srco.setColBox(sr))
				continue;
			for (int di = 0; di < dstsize; di++) {
				L9Object dsto = dstobj[di];
				if (!dsto.isAlive())
					continue;
				if (dsto == srco)
					continue;
				if (!dsto.setAtkBox(dr))
					continue;
				if (sr.intersects(dr) && !dsto.isOver) {
					try {
						dsto.hit(srco);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		flushRemoveList();
		in_parallel_upd = false;
	}

	public void flushRemoveList() {
		// for (Enumeration e=obj_to_remove.elements(); e.hasMoreElements();) {
		// String name = (String)e.nextElement();
		// JGObject o = (JGObject)objects.get(name);
		// if (o!=null) { // object might have been removed already
		// doRemoveObject(o);
		// }
		// }
		// add all query results from object specs to obj_to_remove
		// don't enumerate when no elements (which is about 90% of the time)
		if (obj_spec_to_remove.size() != 0) {
			for (Enumeration e = obj_spec_to_remove.elements(); e
					.hasMoreElements();) {
				String prefix = (String) e.nextElement();
				int cid = ((Integer) e.nextElement()).intValue();
				boolean suspended_obj = ((Boolean) e.nextElement())
						.booleanValue();
				doRemoveObjects(prefix, cid, suspended_obj, false);
			}
			obj_spec_to_remove.removeAllElements();
		}
		// remove everything in one go
		doRemoveList();
	}

	/**
	 * Add objects marked for addition. Protected.
	 */
	public void flushAddList() {
		// XXX we have to add one by one because we have to call the dispose
		// method of the objects that are replaced
		for (int i = 0; i < obj_to_add.size; i++) {
			addObject((L9Object) obj_to_add.values[i], true);
		}
		// actually add objects to array in one go for faster performance
		objects.put(obj_to_add);
		obj_to_add.clear();
	}

	/**
	 * 本帧的对象移动结束：本帧结束时L9Object的一些逻辑
	 */
	public void frameFinished() {
		for (int i = 0; i < objects.size; i++) {
			((L9Object) objects.values[i]).frameFinished();
		}
	}

	/** Actually remove objects in obj_to_remove. */
	void doRemoveList() {
		for (int i = 0; i < obj_to_remove.size; i++) {
			((L9Object) obj_to_remove.values[i]).removeDone();
		}
		objects.remove(obj_to_remove);
		obj_to_remove.clear();
	}

	/** Actually remove object now */
	void doRemoveObject(L9Object obj) {
		obj.removeDone();
		objects.remove(obj.getObjKey());
	}

	void doRemoveObjects(String prefix, int cidmask, boolean suspended_obj,
			boolean do_remove_list) {
		int firstidx = getFirstObjectIndex(prefix);
		int lastidx = getLastObjectIndex(prefix);
		for (int i = firstidx; i < lastidx; i++) {
			L9Object o = (L9Object) objects.values[i];
			if (cidmask == 0 || (o.getObjType() & cidmask) != 0) {
				if (suspended_obj || !o.isSuspend()) {
					obj_to_remove.put(objects.keys[i], o);
				}
			}
		}
		if (do_remove_list)
			doRemoveList();
		// if we enumerate backwards, we can remove elements inline without
		// consistency problems
		for (int i = obj_to_add.size - 1; i >= 0; i--) {
			L9Object o = (L9Object) obj_to_add.values[i];
			if (prefix == null || obj_to_add.keys[i].startsWith(prefix)) {
				if (cidmask == 0 || (o.getObjType() & cidmask) != 0) {
					if (suspended_obj || !o.isSuspend()) {
						// Note: remove element inside element enumeration
						obj_to_add.remove(obj_to_add.keys[i]);
						o.removeDone();
					}
				}
			}
		}
	}

	public void removeObject(L9Object obj) {
		if (in_parallel_upd) { // queue remove
			markRemoveObject(obj);
		} else { // do remove immediately
			doRemoveObject(obj);
		}
	}

	public void removeObjects(String prefix, int cidmask) {
		removeObjects(prefix, cidmask, true);
	}

	public void removeObjects(String prefix, int cidmask, boolean suspended_obj) {
		if (in_parallel_upd) {
			markRemoveObjects(prefix, cidmask, suspended_obj);
		} else {
			doRemoveObjects(prefix, cidmask, suspended_obj, true);
		}
	}

	int getFirstObjectIndex(String prefix) {
		if (prefix == null)
			return 0;
		int firstidx = objects.get(prefix);
		if (firstidx < 0)
			firstidx = -1 - firstidx;
		return firstidx;
	}

	int getLastObjectIndex(String prefix) {
		if (prefix == null)
			return objects.size;
		// XXX theoretically there may be strings with prefix
		// lexicographically below this one
		return -1 - objects.get(prefix + '\uffff');
	}

	public int countObjects(String prefix, int cidmask) {
		return countObjects(prefix, cidmask, true);
	}

	public int countObjects(String prefix, int cidmask, boolean suspended_obj) {
		int nr_obj = 0;
		int firstidx = getFirstObjectIndex(prefix);
		int lastidx = getLastObjectIndex(prefix);
		for (int i = firstidx; i < lastidx; i++) {
			L9Object obj = (L9Object) objects.values[i];
			if (cidmask == 0 || (obj.getObjType() & cidmask) != 0) {
				if (suspended_obj || !obj.isSuspend()) {
					nr_obj++;
				}
			}
		}
		return nr_obj;
	}

	public void doRemoveAllObjects() {
		objects.clearL9Obj();
		obj_to_remove.clearL9Obj();
		obj_spec_to_remove.removeAllElements();
		obj_to_add.clearL9Obj();
		for (int i = 0; i < srcobj.length; i++) {
			srcobj[i] = null;
		}
		for (int i = 0; i < dstobj.length; i++) {
			dstobj[i] = null;
		}
	}

	public L9Object getObject(String index) {
		int idx = objects.get(index);
		if (idx < 0)
			return null;
		return (L9Object) objects.values[idx];
	}

	private int m_Cnt_Time;
	private int ID_State = 3;
	public boolean bDrawOver;

	public void setDrawJalousie(boolean flag) {
		bDrawOver = false;
		if (flag) {
			ID_State = 1 + PubToolKit.getRandomInt(5);
		} else {
			ID_State = 6 + PubToolKit.getRandomInt(3);
		}
		m_Cnt_Time = 0;
	}

	public boolean isDrawOver() {
		return bDrawOver;
	}

	public int getID_State() {
		return ID_State;
	}

	/*
	 * 一百叶窗的形式画图
	 */
	public final boolean drawJalousie(Graphics g, int width, int height,
			int rowNum, int colNum, int RGB, int changeTime, boolean isOpen) {
		g.setColor(RGB);
		int cellMaxW = width / colNum;
		int cellMaxH = height / rowNum;
		int perHalfCellWidthchange = cellMaxW / (2 * changeTime);
		int perHalfCellHeightchange = cellMaxH / (2 * changeTime);
		if (perHalfCellWidthchange < 1) {
			perHalfCellWidthchange = 1;
		}
		if (perHalfCellHeightchange < 1) {
			perHalfCellHeightchange = 1;
		}
		int cellX = 0;
		int cellY = 0;
		int cellW = 0;
		int cellH = 0;
		if (isOpen) {
			cellX = cellMaxW / 2 - m_Cnt_Time * perHalfCellWidthchange;
			cellY = cellMaxH / 2 - m_Cnt_Time * perHalfCellHeightchange;
			cellW = 2 * m_Cnt_Time * perHalfCellWidthchange;
			cellH = 2 * m_Cnt_Time * perHalfCellHeightchange;
			if (cellX <= 0 && cellY <= 0) {
				return true;
			}
		} else {
			cellX = m_Cnt_Time * perHalfCellWidthchange;
			cellY = m_Cnt_Time * perHalfCellHeightchange;
			cellW = cellMaxW - 2 * m_Cnt_Time * perHalfCellWidthchange;
			cellH = cellMaxH - 2 * m_Cnt_Time * perHalfCellHeightchange;
			if (cellW <= 0 && cellH <= 0) {
				return true;
			}
		}
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				g.fillRect(cellX + j * cellMaxW, cellY + i * cellMaxH, cellW,
						cellH);
			}
		}
		return false;
	}

	/**
	 * 闭幕
	 * 
	 * @param g:
	 *            画笔
	 * @param width：屏幕的宽度
	 * @param height：屏幕的高度
	 * @param colour：画图的颜色
	 * @param changeTime：需要画图的时间
	 * @param flag：是否是横向
	 * @return
	 */
	public boolean drawConclude(Graphics g, int width, int height, int colour,
			int changeTime, boolean flag) {
		g.setColor(colour);
		int tileW = 0;
		int tileH = 0;
		if (flag) {
			tileW = width;
			tileH = height / (changeTime * 2);
			g.fillRect(0, 0, tileW, tileH * m_Cnt_Time);
			g.fillRect(0, height - tileH * m_Cnt_Time, tileW, tileH
					* m_Cnt_Time);
			if (height - tileH * m_Cnt_Time * 2 <= 0) {
				return true;
			}
		} else {
			tileW = width / (changeTime * 2);
			tileH = height;
			g.fillRect(0, 0, tileW * m_Cnt_Time, tileH);
			g
					.fillRect(width - tileW * m_Cnt_Time, 0,
							tileW * m_Cnt_Time, tileH);
			if (width - tileW * m_Cnt_Time * 2 <= 0) {
				return true;
			}
		}
		return false;
	}

	public boolean drawOpen(Graphics g, int width, int height, int colour,
			int changeTime, boolean flag) {
		g.setColor(colour);
		int tileW = 0;
		int tileH = 0;
		if (flag) {
			tileW = width;
			tileH = height / (changeTime * 2);
			g.fillRect(0, 0, tileW, tileH * (changeTime - m_Cnt_Time));
			g.fillRect(0, height - tileH * (changeTime - m_Cnt_Time), tileW,
					tileH * (changeTime - m_Cnt_Time));
			if (m_Cnt_Time >= changeTime) {
				return true;
			}
		} else {
			tileW = width / (changeTime * 2);
			tileH = height;
			g.fillRect(0, 0, tileW * (changeTime - m_Cnt_Time), tileH);
			g.fillRect(width - tileW * (changeTime - m_Cnt_Time), 0, tileW
					* (changeTime - m_Cnt_Time), tileH);
			if (m_Cnt_Time >= changeTime) {
				return true;
			}
		}
		return false;
	}

	public void paintJalousie(Graphics g) {
		m_Cnt_Time++;
		if (bDrawOver) {
			return;
		}
		switch (ID_State) {
		case 0:
			m_Cnt_Time = 0;
			break;
		case 1:
			if (drawJalousie(g, L9Config.SCR_W, L9Config.SCR_H, 12, 9,
					0x000000, 15, true)) {
				ID_State = 100;
				bDrawOver = true;
				// m_Cnt_Time = 0;
				g.setColor(0xff0000);
				g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			}
			break;
		case 2:
			if (drawConclude(g, L9Config.SCR_W, L9Config.SCR_H, 0x000000, 15,
					false)) {
				ID_State = 100;
				bDrawOver = true;
				g.setColor(0xff0000);
				g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			}

			break;
		// 闭幕
		case 3:
			if (drawJalousie(g, L9Config.SCR_W, L9Config.SCR_H, 1, 9, 0x000000,
					15, true)) {
				ID_State = 100;
				bDrawOver = true;
				g.setColor(0xff0000);
				g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			}
			break;
		// 闭幕
		case 4:
			if (drawJalousie(g, L9Config.SCR_W, L9Config.SCR_H, 9, 1, 0x000000,
					15, true)) {
				ID_State = 100;
				bDrawOver = true;
				g.setColor(0xff0000);
				g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			}
			break;
		case 5:
			if (drawConclude(g, L9Config.SCR_W, L9Config.SCR_H, 0x000000, 15,
					true)) {
				ID_State = 100;
				bDrawOver = true;
				g.setColor(0xff0000);
				g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			}
			break;
		case 6:
			if (drawOpen(g, L9Config.SCR_W, L9Config.SCR_H, 0x000000, 15, true)) {
				ID_State = 100;
				bDrawOver = true;
			}
			break;
		case 7:
			if (drawJalousie(g, L9Config.SCR_W, L9Config.SCR_H, 12, 9,
					0x000000, 15, false)) {
				ID_State = 100;
				bDrawOver = true;
			}
			break;
		case 8:
			if (drawOpen(g, L9Config.SCR_W, L9Config.SCR_H, 0x000000, 15, false)) {
				ID_State = 100;
				bDrawOver = true;
			}
			break;
		}
	}
}
