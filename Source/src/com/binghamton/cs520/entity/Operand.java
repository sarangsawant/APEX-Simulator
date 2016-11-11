package com.binghamton.cs520.entity;

public class Operand {
	private String operandName;
	private int operandValue;
	
	public Operand() {
		// TODO Auto-generated constructor stub
	}

	public String getOperandName() {
		return operandName;
	}

	public void setOperandName(String operandName) {
		this.operandName = operandName;
	}

	public int getOperandValue() {
		return operandValue;
	}

	public void setOperandValue(int operandValue) {
		this.operandValue = operandValue;
	}

	@Override
	public String toString() {
		return "Operand [operandName=" + operandName + ", operandValue=" + operandValue + "]";
	}
}
