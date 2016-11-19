package com.binghamton.cs520.constants;

public enum InstructionEnum {
	MOVC("MOVC"), ADD("ADD"), SUB("SUB"), MUL("MUL"), DIV("DIV"), LOAD("LOAD"), STORE("STORE"), HALT("HALT"),
	BAL("BAL"), BZ("BZ"), BNZ("BNZ"), JUMP("JUMP");

	private String string;

	InstructionEnum(String string) {
		this.string = string;
	}

	public String getInstructionType() {
		return string;
	}
}
