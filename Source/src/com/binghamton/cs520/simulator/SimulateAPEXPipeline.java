package com.binghamton.cs520.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.binghamton.cs520.constants.InstructionEnum;
import com.binghamton.cs520.constants.Tokens;
import com.binghamton.cs520.entity.Instruction;
import com.binghamton.cs520.entity.Operand;
import com.binghamton.cs520.utilities.InitializeStructures;

public class SimulateAPEXPipeline {
	private Map<Integer, Instruction> instructions = new HashMap<>();
	private Map<String, Integer> architectureRegFile = new HashMap<>();
	private int[] memory = new int[1000];

	private int programCnt = 4000;
	private List<Instruction> fetchStageList = new ArrayList<>();
	private List<Instruction> decodeStageList = new ArrayList<>();
	private List<Instruction> arithExecuteStageOneList = new ArrayList<>();
	private List<Instruction> arithExecuteStageTwoList = new ArrayList<>();
	private List<Instruction> memoryStageList = new ArrayList<>();
	private List<Instruction> writeBackStageList = new ArrayList<>();
	//private HashMap<Integer, String> dependencyMap = new HashMap<>();

	private void fetchStageExecution() {
		System.out.println("Inside fetchStageExecution method");
		if (fetchStageList.size() > 0)
			System.out.println(fetchStageList.get(0).toString());
	}

	private void decodeStageExecution() {
		System.out.println("Inside decodeStageExecution method");
		if (decodeStageList.size() > 0) {
			System.out.println("Instuction in decode stage(Before decode) -->" + decodeStageList.get(0).toString());
			String instruction = decodeStageList.get(0).getInstruction();
			String instructionFields[] = instruction.split(Tokens.SPACE.getToken());
			
			
			
			/*
			 * Check for the type of instructions ADD dest src1 src2 SUB dest
			 * src1 src2 MUL dest src1 src2 DIV dest src1 src2
			 */
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
			} else if (InstructionEnum.LOAD.getInstructionType().equals(instructionFields[0])) {
				// LOAD dest src1 literal
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
			} else if (InstructionEnum.STORE.getInstructionType().equals(instructionFields[0])) {
				// STORE src1 src2 literal
				decodeStageList.get(0).setInstructionType(instructionFields[0]);

				Operand source1 = new Operand();
				source1.setOperandName(instructionFields[1]);
				source1.setOperandValue(architectureRegFile.get(instructionFields[1]));
				decodeStageList.get(0).setSource1(source1);

				Operand source2 = new Operand();
				source2.setOperandName(instructionFields[2]);
				source2.setOperandValue(architectureRegFile.get(instructionFields[2]));
				decodeStageList.get(0).setSource2(source2);

				// assumed source 3 is literal
				Operand source3 = new Operand();
				source3.setOperandName(Tokens.LITERAL.getToken());
				source3.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
				decodeStageList.get(0).setSource3(source3);
			}
			decodeStageList.get(0).setDecoded(true);
			System.out.println("Instuction in decode stage(After decode) -->" + decodeStageList.get(0).toString());
		}
	}

	private void arithStage1() {
		System.out.println("Inside arithStage 1 method");
		if (arithExecuteStageOneList.size() > 0) {
			System.out.println("Arith stage 1 --> " + arithExecuteStageOneList.get(0).toString());
			arithExecuteStageOneList.get(0).setALU1Executed(true);
		}
	}

	private void arithmeticUnitStageExecution() {
		System.out.println("arithmeticUnitStageExecution");
		if (arithExecuteStageTwoList.size() > 0) {
			System.out.println("Inside execute stage 2 --> " + arithExecuteStageTwoList.get(0).toString());
			// check for instruction type

			if (InstructionEnum.ADD.getInstructionType().equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				// logic for ADD instruction
				int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
				int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int dest = src1 + src2;
				arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.SUB.getInstructionType()
					.equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				// logic for SUB instruction
				int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
				int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int dest = src1 - src2;
				arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.MUL.getInstructionType()
					.equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				// logic for MUL instruction
				int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
				int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int dest = src1 * src2;
				arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.DIV.getInstructionType()
					.equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
				int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int dest = src1 / src2;
				arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.LOAD.getInstructionType()
					.equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				// LOAD dest src1 literal
				int src1 = arithExecuteStageTwoList.get(0).getSource1().getOperandValue();
				int literal = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int dest = src1 + literal;
				arithExecuteStageTwoList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.STORE.getInstructionType()
					.equals(arithExecuteStageTwoList.get(0).getInstructionType())) {
				// STORE src1 src2 literal

				int src2 = arithExecuteStageTwoList.get(0).getSource2().getOperandValue();
				int literal = arithExecuteStageTwoList.get(0).getSource3().getOperandValue();
				int src1 = src2 + literal;

				Operand destination = new Operand();
				destination.setOperandValue(src1);
				arithExecuteStageTwoList.get(0).setDestination(destination);

			}
			System.out.println("After Execution:: " + arithExecuteStageTwoList.get(0).toString());
			arithExecuteStageTwoList.get(0).setALU2Executed(true);
		}
	}

	private void memoryStageExecution() {
		System.out.println("memoryStageExecution");
		if (memoryStageList.size() > 0) {
			System.out.println("Inside memory stage--> " + memoryStageList.get(0).toString());
			if (InstructionEnum.LOAD.getInstructionType().equals(memoryStageList.get(0).getInstructionType())) {
				memoryStageList.get(0).getDestination()
						.setOperandValue(memory[memoryStageList.get(0).getDestination().getOperandValue()]);
			}
			if (InstructionEnum.STORE.getInstructionType().equals(memoryStageList.get(0).getInstructionType())) {
				memory[memoryStageList.get(0).getDestination().getOperandValue()] = memoryStageList.get(0).getSource1()
						.getOperandValue();

			}
			memoryStageList.get(0).setMemoryExecuted(true);
		}
	}

	private void writeBackStageExecution() {
		System.out.println("writeBackStageExecution");
		if (writeBackStageList.size() > 0)
			System.out.println("Inside writeBack stage--> " + writeBackStageList.get(0).toString());

		if (InstructionEnum.ADD.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.SUB.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.MUL.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.DIV.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.LOAD.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())) {
			
			architectureRegFile.put(writeBackStageList.get(0).getDestination().getOperandName(),
					writeBackStageList.get(0).getDestination().getOperandValue());
		}
		if(InstructionEnum.MOVC.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())){
			architectureRegFile.put(writeBackStageList.get(0).getDestination().getOperandName(),
					writeBackStageList.get(0).getSource1().getOperandValue());
		}
	}

	public void simulateProcessingCycles(int totalSimulateCycles) {
		/*populate instructions in map from file, initalize architectural register file and memory*/ 
		InitializeStructures structures = new InitializeStructures();
		instructions = structures.populateInstructionsInMap();
		architectureRegFile = structures.initializeArchitectureRegisterFile();
		memory = structures.initializeMemory();
		Map<Integer, Instruction> allInstructions = structures.populateInstructions(instructions,architectureRegFile);
		
		for (int i = 0; i < totalSimulateCycles; i++) {
			System.out.println("\nCYCLE --" + (i + 1));

			// Fetch Stage
			if (fetchStageList.size() == 0) {
				if (instructions.get(programCnt) != null) {
					fetchStageList.add(instructions.get(programCnt));
					programCnt = programCnt + 1;
					fetchStageExecution();
					continue;
				}
			} else {
				if (instructions.get(programCnt) != null) {
					fetchStageList.add(instructions.get(programCnt));
					programCnt = programCnt + 1;
				}
				if (fetchStageList.size() > 0) {
					decodeStageList.add(fetchStageList.get(0));
					fetchStageList.remove(0);
					fetchStageExecution();
				}
			}

			// Decode Stage
			//Check for dependency
			//boolean isDependent = checkForDependency(allInstructions,decodeStageList.get(0));
			
			if (decodeStageList.size() == 1 && !decodeStageList.get(0).isDecoded()) {
				decodeStageExecution();
				continue;
			} else if (decodeStageList.size() > 0) {
				arithExecuteStageOneList.add(decodeStageList.get(0));
				decodeStageList.remove(0);
				decodeStageExecution();
			}

			// ALU stage 1
			if (arithExecuteStageOneList.size() == 1 && !arithExecuteStageOneList.get(0).isALU1Executed()) {
				arithStage1();
				continue;
			} else if (arithExecuteStageOneList.size() > 0) {
				arithExecuteStageTwoList.add(arithExecuteStageOneList.get(0));
				arithExecuteStageOneList.remove(0);
				arithStage1();
			}

			// ALU stage 2
			if (arithExecuteStageTwoList.size() == 1 && !arithExecuteStageTwoList.get(0).isALU2Executed()) {
				arithmeticUnitStageExecution();
				continue;
			} else if (arithExecuteStageTwoList.size() > 0) {
				memoryStageList.add(arithExecuteStageTwoList.get(0));
				arithExecuteStageTwoList.remove(0);
				arithmeticUnitStageExecution();
			}

			// MemoryStage
			if (memoryStageList.size() == 1 && !memoryStageList.get(0).isMemoryExecuted()) {
				memoryStageExecution();
				continue;
			} else if (memoryStageList.size() > 0) {
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
		structures.displayStructuresContent(architectureRegFile, memory);
	}
	

	private boolean checkForDependency(Map<Integer, Instruction> instructionMap,Instruction inputInstruction){

		if(instructionMap.size() > 0){
			//loop to check for next 4 instructions for dependency
			//if()
			System.out.println("Instructions in checkfordepndency : "+ instructionMap);
			return true;
		}
		return false;
	}
}
