package com.game.lib9;

public abstract class L9Screen implements L9IState {

	public void Init(int loadCound) {
		// TODO Auto-generated method stub

	}

	public void fristInit() {
		// TODO Auto-generated method stub

	}

	public abstract void Paint();

	public abstract void Update();

	public void Release() {
		// TODO Auto-generated method stub

	}

	public void dialogLogic(int selectOtion) {
		// TODO Auto-generated method stub

	}

	public boolean isDrawJalousieStateBegin() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDrawJalousieStateEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return true;
	}

	public int loadIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean threadLoad() {
		// TODO Auto-generated method stub
		return true;
	}

}
