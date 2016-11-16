package com.binghamton.cs520.entity;

public class LatchSource {
	private int value;
	private int validFlag;
	
	public LatchSource() {
		// TODO Auto-generated constructor stub
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(int validFlag) {
		this.validFlag = validFlag;
	}

	@Override
	public String toString() {
		return "LatchSource [value=" + value + ", validFlag=" + validFlag + "]";
	}
	
}
