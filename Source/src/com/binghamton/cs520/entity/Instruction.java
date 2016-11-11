package com.binghamton.cs520.simulator;

public class Instruction {
	private String instruction;
	private String instructionType;
	private String source1;
	private String source2;
	private String source3;
	private String destination;
	private String literal;
	
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

	public String getSource1() {
		return source1;
	}

	public void setSource1(String source1) {
		this.source1 = source1;
	}

	public String getSource2() {
		return source2;
	}

	public void setSource2(String source2) {
		this.source2 = source2;
	}

	public String getSource3() {
		return source3;
	}

	public void setSource3(String source3) {
		this.source3 = source3;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getLiteral() {
		return literal;
	}

	public void setLiteral(String literal) {
		this.literal = literal;
	}
	
}
