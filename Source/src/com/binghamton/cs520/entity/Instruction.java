package com.binghamton.cs520.entity;

public class Instruction {
	private String instruction;
	private String instructionType;
	private Operand source1;
	private Operand source2;
	private Operand source3;
	private Operand destination;
	private boolean isDecoded = false;
	private boolean isALU1Executed = false;
	private boolean isALU2Executed = false;
	private boolean isMemoryExecuted = false;
	private int address;
	private int latchIndex;
	
	public Instruction() {
		// TODO Auto-generated constructor stub
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}

	
	public Operand getSource1() {
		return source1;
	}

	public void setSource1(Operand source1) {
		this.source1 = source1;
	}

	public Operand getSource2() {
		return source2;
	}

	public void setSource2(Operand source2) {
		this.source2 = source2;
	}

	public Operand getSource3() {
		return source3;
	}

	public void setSource3(Operand source3) {
		this.source3 = source3;
	}

	public Operand getDestination() {
		return destination;
	}

	public void setDestination(Operand destination) {
		this.destination = destination;
	}
	
	public boolean isDecoded() {
		return isDecoded;
	}

	public void setDecoded(boolean isDecoded) {
		this.isDecoded = isDecoded;
	}

	public boolean isALU1Executed() {
		return isALU1Executed;
	}

	public void setALU1Executed(boolean isALU1Executed) {
		this.isALU1Executed = isALU1Executed;
	}

	public boolean isALU2Executed() {
		return isALU2Executed;
	}

	public void setALU2Executed(boolean isALU2Executed) {
		this.isALU2Executed = isALU2Executed;
	}

	public boolean isMemoryExecuted() {
		return isMemoryExecuted;
	}

	public void setMemoryExecuted(boolean isMemoryExecuted) {
		this.isMemoryExecuted = isMemoryExecuted;
	}

	@Override
	public String toString() {
		return "Instruction [instruction=" + instruction + ", instructionType=" + instructionType + ", source1="
				+ source1 + ", source2=" + source2 + ", source3=" + source3 + ", destination=" + destination
				+ ", isDecoded=" + isDecoded + ", isALU1Executed=" + isALU1Executed + ", isALU2Executed="
				+ isALU2Executed + ", isMemoryExecuted=" + isMemoryExecuted + "]";
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getLatchIndex() {
		return latchIndex;
	}

	public void setLatchIndex(int latchIndex) {
		this.latchIndex = latchIndex;
	}

	

}
