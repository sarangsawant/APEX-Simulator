package com.binghamton.cs520.simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.binghamton.cs520.constants.InstructionEnum;
import com.binghamton.cs520.constants.Tokens;

public class SimulateAPEXPipeline {
	private static Map<Integer, Instruction> instructions = new HashMap<>();
	private static int programCnt = 4000;
	private static List<Instruction> fetchStageList = new ArrayList<>();
	private static List<Instruction> decodeStageList = new ArrayList<>();
	private static List<Instruction> executeStageList = new ArrayList<>();
	private static List<Instruction> memoryStageList = new ArrayList<>();
	private static List<Instruction> writeBackStageList = new ArrayList<>();
	
	private void populateInstructionsInMap(){
		String fileName = "src/Input.txt";

		File file = new File(fileName);
	    FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			try {
				while((line = br.readLine()) != null){
					String data[] = line.split(Tokens.COLON.getToken());
					Instruction instruction = new Instruction();
					instruction.setInstruction(data[1]);
					instructions.put(Integer.parseInt(data[0]),instruction);
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void fetchStageExecution(){
		System.out.println("Inside fetchStageExecution method");
		System.out.println(fetchStageList.get(0).getInstruction());
	}
	
	private void decodeStageExecution(){
		System.out.println("Inside decodeStageExecution method");
		System.out.println("Instuction in decode stage -->" + decodeStageList.get(0).getInstruction());
		String instruction = decodeStageList.get(0).getInstruction();
		String data[] = instruction.split(Tokens.SPACE.getToken());
		Instruction inputInstruction = new Instruction();
		
		//Check for the type of instructions
		if(InstructionEnum.ADD.equals(data[0])){
			if(data[2].indexOf("#") < 0){
				//ADD dest src1 src2
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
				inputInstruction.setSource2(data[3]);
			}else{
				//ADD dest src1 #literal
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
			}
		}
		else if(InstructionEnum.SUB.equals(data[0])){
			if(data[2].indexOf("#") < 0){
				//SUB dest src1 src2
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
				inputInstruction.setSource2(data[3]);
			}else{
				//SUB dest src1 #literal
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
			}
		}
		else if(InstructionEnum.MUL.equals(data[0])){
			if(data[2].indexOf("#") < 0){
				//MUL dest src1 src2
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
				inputInstruction.setSource2(data[3]);
			}else{
				//MUL dest src1 #literal
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
			}
		}
		else if(InstructionEnum.DIV.equals(data[0])){
			if(data[2].indexOf("#") < 0){
				//DIV dest src1 src2
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
				inputInstruction.setSource2(data[3]);
			}else{
				//DIV dest src1 #literal
				inputInstruction.setDestination(data[1]);
				inputInstruction.setSource1(data[2]);
			}
		}
		else if(InstructionEnum.MOVC.equals(data[0])){
			//MOVC R4 2
			inputInstruction.setDestination(data[1]);
			inputInstruction.setSource1(data[2]);
		}
		else{
			//throw exception or error message
		}

	}
	
	private void functionalUnitALU(){
		System.out.println("functionalUnitALU");
		System.out.println("Inside execute stage--> "+ executeStageList.get(0).getInstruction());
	}
	
	private void memoryStageExecution(){
		System.out.println("memoryStageExecution");
		System.out.println("Inside memory stage--> "+ memoryStageList.get(0).getInstruction());
	}
	
	private void writeBackStageExecution(){
		System.out.println("writeBackStageExecution");
		System.out.println("Inside writeBack stage--> "+ writeBackStageList.get(0).getInstruction());
	}
	
	public void simulateProcessingCycles(int totalSimulateCycles){
		populateInstructionsInMap();
		
		for(int i=0; i < totalSimulateCycles ; i++){
			System.out.println("\nCYCLE --" + i);
			if(fetchStageList.size() == 0){
				fetchStageList.add(instructions.get(programCnt));
				programCnt = programCnt + 1;
				fetchStageExecution();
				continue;
			}else{
				fetchStageList.add(instructions.get(programCnt));
				programCnt = programCnt + 1;
				decodeStageList.add(fetchStageList.get(0));
				fetchStageList.remove(0);
				fetchStageExecution();
			}
			
			if(decodeStageList.size() == 1){
				decodeStageExecution();
				continue;
			}else{
				executeStageList.add(decodeStageList.get(0));
				decodeStageList.remove(0);
				decodeStageExecution();
			}
			
			if(executeStageList.size() == 1){
				functionalUnitALU();
				continue;
			}else{
				memoryStageList.add(executeStageList.get(0));
				executeStageList.remove(0);
				functionalUnitALU();
			}
			
			if(memoryStageList.size() == 1){
				memoryStageExecution();
				continue;
			}else{
				writeBackStageList.add(memoryStageList.get(0));
				memoryStageList.remove(0);
				memoryStageExecution();
			}
			
			if(writeBackStageList.size() == 1){
				writeBackStageExecution();
				writeBackStageList.remove(0);
				continue;
			}
		}
	}
	
}
