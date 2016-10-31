package com.binghamton.cs520.simulator;

public class AritmaticInstructionSimulator {

	public String fetchAddInstruction(String instruction){
		//System.out.println("instruction:: "+instruction.toString());
		String[] arr = instruction.split(" ");
		if(arr.length >= 3){
			int valueAtSrc1 = getValueOfRegister(arr[2]);
			int valueAtSrc2 = 0;
			if(arr[3].indexOf("#") > -1){
				valueAtSrc2 = getValueOfRegister(arr[3].substring(1, arr[3].length()));
			}else{
				valueAtSrc2 = getValueOfRegister(arr[3]);
			}
			int sum = valueAtSrc1 + valueAtSrc2;
			return "";
		}
		return "";
	}
	
	public String fetchSubInstruction(String instruction){
		
		return "";
	}
	
	public String fetchDivInstruction(String instruction){
		return "";
	}
	
	public String fetchMulInstruction(String instruction){
		return "";
	}
	
	public static int getValueOfRegister(String registerName){
		return 0;
	}
}
