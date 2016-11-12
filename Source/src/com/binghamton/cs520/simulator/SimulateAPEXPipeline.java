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
import com.binghamton.cs520.entity.Instruction;
import com.binghamton.cs520.entity.Operand;


public class SimulateAPEXPipeline {
	private static Map<Integer, Instruction> instructions = new HashMap<>();
	private static Map<String, Integer> architectureRegFile = new HashMap<>();

	private static int programCnt = 4000;
	private static List<Instruction> fetchStageList = new ArrayList<>();
	private static List<Instruction> decodeStageList = new ArrayList<>();
	private static List<Instruction> arithExecuteStageOneList = new ArrayList<>();
	private static List<Instruction> arithExecuteStageTwoList = new ArrayList<>();
	private static List<Instruction> memoryStageList = new ArrayList<>();
	private static List<Instruction> writeBackStageList = new ArrayList<>();

	private void populateInstructionsInMap() {
		String fileName = "src/Input.txt";

		File file = new File(fileName);
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			try {
				while ((line = br.readLine()) != null) {
					String data[] = line.split(Tokens.COLON.getToken());
					Instruction instruction = new Instruction();
					instruction.setInstruction(data[1]);
					instructions.put(Integer.parseInt(data[0]), instruction);
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

	private void initializeArchitectureRegisterFile() {
		System.out.println("Inside initializeRegisterFile method.");
		for (Integer i = 0; i < 15; i++) {
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			architectureRegFile.put(Tokens.REGISTER_PREFIX.getToken() + i.toString(), -99999);
		}
	}

	private void fetchStageExecution() {
		System.out.println("Inside fetchStageExecution method");
		System.out.println(fetchStageList.get(0).toString());
	}

	private void decodeStageExecution() {
		System.out.println("Inside decodeStageExecution method");
		System.out.println("Instuction in decode stage(Before decode) -->" + decodeStageList.get(0).toString());
		String instruction = decodeStageList.get(0).getInstruction();
		String instructionFields[] = instruction.split(Tokens.SPACE.getToken());

		/*Check for the type of instructions
		ADD dest src1 src2
		SUB dest src1 src2
		MUL dest src1 src2
		DIV dest src1 src2*/
		if (InstructionEnum.ADD.getInstructionType().equals(instructionFields[0])
				|| InstructionEnum.SUB.getInstructionType().equals(instructionFields[0])
				|| InstructionEnum.MUL.getInstructionType().equals(instructionFields[0])
				|| InstructionEnum.DIV.getInstructionType().equals(instructionFields[0])) {
			
			decodeStageList.get(0).setInstructionType(instructionFields[0]);
			
			Operand destination = new Operand();
			destination.setOperandName(instructionFields[1]);
			decodeStageList.get(0).setDestination(destination);

			if (instructionFields[2].indexOf("#") < 0) {
				Operand source1 = new Operand();
				source1.setOperandName(instructionFields[2]);
				source1.setOperandValue(architectureRegFile.get(instructionFields[2]));
				decodeStageList.get(0).setSource1(source1);
			} else {
				Operand source1 = new Operand();
				source1.setOperandName(Tokens.LITERAL.getToken());
				source1.setOperandValue(Integer.parseInt(instructionFields[2].substring(1)));
				decodeStageList.get(0).setSource1(source1);
			}

			if (instructionFields[3].indexOf("#") < 0) {
				Operand source2 = new Operand();
				source2.setOperandName(instructionFields[3]);
				source2.setOperandValue(architectureRegFile.get(instructionFields[3]));
				decodeStageList.get(0).setSource2(source2);
			} else {
				Operand source2 = new Operand();
				source2.setOperandName(Tokens.LITERAL.getToken());
				source2.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
				decodeStageList.get(0).setSource2(source2);
			}
		} else if (InstructionEnum.MOVC.getInstructionType().equals(instructionFields[0])) {
			// MOVC dest literal
			decodeStageList.get(0).setInstructionType(instructionFields[0]);
			
			Operand destination = new Operand();
			destination.setOperandName(instructionFields[1]);
			decodeStageList.get(0).setDestination(destination);
			
			Operand source1 = new Operand();
			source1.setOperandName(Tokens.LITERAL.getToken());
			source1.setOperandValue(Integer.parseInt(instructionFields[2].substring(1)));
			decodeStageList.get(0).setSource1(source1);
		}else if(InstructionEnum.LOAD.getInstructionType().equals(instructionFields[0])){
			//LOAD dest src1 literal
			decodeStageList.get(0).setInstructionType(instructionFields[0]);
			
			Operand destination = new Operand();
			destination.setOperandName(instructionFields[1]);
			decodeStageList.get(0).setDestination(destination);
			
			Operand source1 = new Operand();
			source1.setOperandName(instructionFields[2]);
			source1.setOperandValue(architectureRegFile.get(instructionFields[2]));
			decodeStageList.get(0).setSource1(source1);
			
			Operand source2 = new Operand();
			source2.setOperandName(Tokens.LITERAL.getToken());
			source2.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
			decodeStageList.get(0).setSource2(source2);
		}else if(InstructionEnum.STORE.getInstructionType().equals(instructionFields[0])){
			//STORE src1 src2 literal
			decodeStageList.get(0).setInstructionType(instructionFields[0]);
			
			Operand source1 = new Operand();
			source1.setOperandName(instructionFields[1]);
			source1.setOperandValue(architectureRegFile.get(instructionFields[1]));
			decodeStageList.get(0).setSource1(source1);
			
			Operand source2 = new Operand();
			source2.setOperandName(instructionFields[2]);
			source2.setOperandValue(Integer.parseInt(instructionFields[2].substring(1)));
			decodeStageList.get(0).setSource2(source2);
			
			//assumed source 3 is literal
			Operand source3 = new Operand();
			source3.setOperandName(Tokens.LITERAL.getToken());
			source3.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
			decodeStageList.get(0).setSource3(source3);
		}
		System.out.println("Instuction in decode stage(After decode) -->" + decodeStageList.get(0).toString());
	}

	private void arithStage1() {
		System.out.println("Arith stage 1 --> " + arithExecuteStageOneList.get(0).toString());
	}

	private void arithmeticUnitStageExecution() {
		System.out.println("arithmeticUnitStageExecution");
		System.out.println("Inside execute stage 2 --> " + arithExecuteStageTwoList.get(0).toString());
		//check for instruction type
		
		if(InstructionEnum.ADD.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			//logic for ADD instruction
			int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int dest = src1 + src2;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
		}
		else if(InstructionEnum.SUB.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			//logic for SUB instruction
			int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int dest = src1 - src2;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
		}
		else if(InstructionEnum.MUL.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			//logic for MUL instruction
			int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int dest = src1 * src2;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
		}
		else if(InstructionEnum.DIV.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int dest = src1 / src2;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
		}
		else if(InstructionEnum.LOAD.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			//LOAD dest src1 literal
			int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int literal = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int dest = src1 + literal;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
		}
		else if(InstructionEnum.MOVC.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())){
			//STORE src1 src2 literal
			/*int src2 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
			int literal = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
			int src1 = src2 + literal;
			arithExecuteStageTwoList.get(0).getDestination().setOperandValue(src1);*/
		}
		System.out.println("After Execution:: " + arithExecuteStageTwoList.get(0).toString());
	}

	private void memoryStageExecution() {
		System.out.println("memoryStageExecution");
		System.out.println("Inside memory stage--> " + memoryStageList.get(0).toString());
	}

	private void writeBackStageExecution() {
		System.out.println("writeBackStageExecution");
		System.out.println("Inside writeBack stage--> " + writeBackStageList.get(0).toString());
	}

	public void simulateProcessingCycles(int totalSimulateCycles) {
		populateInstructionsInMap();
		initializeArchitectureRegisterFile();

		for (int i = 0; i < totalSimulateCycles; i++) {
			System.out.println("\nCYCLE --" + i);

			// Fetch Stage
			if (fetchStageList.size() == 0) {
				if(instructions.get(programCnt) != null){
					fetchStageList.add(instructions.get(programCnt));
					programCnt = programCnt + 1;
					fetchStageExecution();
					continue;
				}
			} else {
				if(instructions.get(programCnt) != null){
					fetchStageList.add(instructions.get(programCnt));
					programCnt = programCnt + 1;
					decodeStageList.add(fetchStageList.get(0));
					fetchStageList.remove(0);
					//if(!("HALT".equals(fetchStageList.get(0)))){
					fetchStageExecution();
					//}
				}
			}

			// Decode Stage
			if (decodeStageList.size() == 1) {
				decodeStageExecution();
				continue;
			} else {
				arithExecuteStageOneList.add(decodeStageList.get(0));
				decodeStageList.remove(0);
				decodeStageExecution();
			}

			// ALU stage 1
			if (arithExecuteStageOneList.size() == 1) {
				arithStage1();
				continue;
			} else {
				arithExecuteStageTwoList.add(arithExecuteStageOneList.get(0));
				arithExecuteStageOneList.remove(0);
				arithStage1();
			}

			// ALU stage 2
			if (arithExecuteStageTwoList.size() == 1) {
				arithmeticUnitStageExecution();
				continue;
			} else {
				memoryStageList.add(arithExecuteStageTwoList.get(0));
				arithExecuteStageTwoList.remove(0);
				arithmeticUnitStageExecution();
			}

			// MemoryStage
			if (memoryStageList.size() == 1) {
				memoryStageExecution();
				continue;
			} else {
				writeBackStageList.add(memoryStageList.get(0));
				memoryStageList.remove(0);
				memoryStageExecution();
			}

			// WriteBack
			if (writeBackStageList.size() == 1) {
				writeBackStageExecution();
				writeBackStageList.remove(0);
			}
		}
	}
}
