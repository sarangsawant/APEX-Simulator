package com.binghamton.cs520.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.binghamton.cs520.constants.InstructionEnum;
import com.binghamton.cs520.constants.Tokens;
import com.binghamton.cs520.entity.Instruction;
import com.binghamton.cs520.entity.LatchSource;
import com.binghamton.cs520.entity.Operand;

public class InitializeStructures {

	public static Map<Integer, Instruction> populateInstructionsInMap() {
		Map<Integer, Instruction> instructions = new HashMap<>();
		String fileName = "src/Input.txt";

		File file = new File(fileName);
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			int pCounter = 4000;
			String line;
			try {
				while ((line = br.readLine()) != null) {
					Instruction instruction = new Instruction();
					instruction.setInstruction(line);
					instruction.setAddress(pCounter);
					instructions.put(pCounter, instruction);
					pCounter++;
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return instructions;
	}

	public static Map<String, Integer> initializeArchitectureRegisterFile() {
		//System.out.println("Inside initializeRegisterFile method.");
		Map<String, Integer> architectureRegFile = new LinkedHashMap<>();
		for (int i = 0; i < 15; i++) {
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			architectureRegFile.put(Tokens.REGISTER_PREFIX.getToken() + i, -99999);
		}
		return architectureRegFile;
	}

	public static int[] initializeMemory() {
		//System.out.println("inside initializeMemory method");
		int[] memory = new int[1000];
		for (int i = 0; i < 999; i++) {
			memory[i] = 99999;
		}
		return memory;
	}

	public static Map<Integer, Instruction> initAllInstructionsFields(Map<Integer, Instruction> instructions2,
			Map<String, Integer> architectureRegFile) {
		//System.out.println(instructions2.size());
		// do same as decode stage function
		int counter = 4000;
		Map<Integer, Instruction> instructionMap = new HashMap<>();
		for (int i = 0; i < instructions2.size(); i++) {
			String instruction = instructions2.get(counter).getInstruction();
			String instructionFields[] = instruction.split(Tokens.SPACE.getToken());
			Instruction inputInstruction = new Instruction();
			if (InstructionEnum.ADD.getInstructionType().equals(instructionFields[0])
					|| InstructionEnum.SUB.getInstructionType().equals(instructionFields[0])
					|| InstructionEnum.MUL.getInstructionType().equals(instructionFields[0])
					|| InstructionEnum.DIV.getInstructionType().equals(instructionFields[0])) {

				inputInstruction.setInstructionType(instructionFields[0]);

				Operand destination = new Operand();
				destination.setOperandName(instructionFields[1]);
				inputInstruction.setDestination(destination);

				if (instructionFields[2].indexOf("#") < 0) {
					Operand source1 = new Operand();
					source1.setOperandName(instructionFields[2]);
					source1.setOperandValue(architectureRegFile.get(instructionFields[2]));
					inputInstruction.setSource1(source1);
				} else {
					Operand source1 = new Operand();
					source1.setOperandName(Tokens.LITERAL.getToken());
					source1.setOperandValue(Integer.parseInt(instructionFields[2].substring(1)));
					inputInstruction.setSource1(source1);
				}

				if (instructionFields[3].indexOf("#") < 0) {
					Operand source2 = new Operand();
					source2.setOperandName(instructionFields[3]);
					source2.setOperandValue(architectureRegFile.get(instructionFields[3]));
					inputInstruction.setSource2(source2);
				} else {
					Operand source2 = new Operand();
					source2.setOperandName(Tokens.LITERAL.getToken());
					source2.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
					inputInstruction.setSource2(source2);
				}
			} else if (InstructionEnum.MOVC.getInstructionType().equals(instructionFields[0])) {
				// MOVC dest literal
				inputInstruction.setInstructionType(instructionFields[0]);

				Operand destination = new Operand();
				destination.setOperandName(instructionFields[1]);
				inputInstruction.setDestination(destination);

				Operand source1 = new Operand();
				source1.setOperandName(Tokens.LITERAL.getToken());
				source1.setOperandValue(Integer.parseInt(instructionFields[2].substring(1)));
				inputInstruction.setSource1(source1);
			} else if (InstructionEnum.LOAD.getInstructionType().equals(instructionFields[0])) {
				// LOAD dest src1 literal
				inputInstruction.setInstructionType(instructionFields[0]);

				Operand destination = new Operand();
				destination.setOperandName(instructionFields[1]);
				inputInstruction.setDestination(destination);

				Operand source1 = new Operand();
				source1.setOperandName(instructionFields[2]);
				source1.setOperandValue(architectureRegFile.get(instructionFields[2]));
				inputInstruction.setSource1(source1);

				Operand source2 = new Operand();
				source2.setOperandName(Tokens.LITERAL.getToken());
				source2.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
				inputInstruction.setSource2(source2);
			} else if (InstructionEnum.STORE.getInstructionType().equals(instructionFields[0])) {
				// STORE src1 src2 literal
				inputInstruction.setInstructionType(instructionFields[0]);

				Operand source1 = new Operand();
				source1.setOperandName(instructionFields[1]);
				source1.setOperandValue(architectureRegFile.get(instructionFields[1]));
				inputInstruction.setSource1(source1);

				Operand source2 = new Operand();
				source2.setOperandName(instructionFields[2]);
				source2.setOperandValue(architectureRegFile.get(instructionFields[2]));
				inputInstruction.setSource2(source2);

				// assumed source 3 is literal
				Operand source3 = new Operand();
				source3.setOperandName(Tokens.LITERAL.getToken());
				source3.setOperandValue(Integer.parseInt(instructionFields[3].substring(1)));
				inputInstruction.setSource3(source3);
			}else if(InstructionEnum.BNZ.getInstructionType().equals(instructionFields[0])){
				//BNZ #-16
				inputInstruction.setInstructionType(instructionFields[0]);
				
				Operand source1 = new Operand();
				source1.setOperandName(Tokens.LITERAL.getToken());
				source1.setOperandValue(Integer.parseInt(instructionFields[1].substring(1)));
				inputInstruction.setSource1(source1);
			}
			instructionMap.put(counter, inputInstruction);
			//System.out.println("instruction obj--> " + counter + "  " + instructionMap.get(counter));
			counter++;
		}
		return instructionMap;
	}

	/**
	 * If there is dependency between instructions from I->J, this method sets
	 * dependent field of instruction 'I' to true so that it knows that it has
	 * to forward result to forwarding latch.
	 * 
	 * @param instructionMap
	 * @param inputInstruction
	 */
	public static void checkInstructionDependency(Map<Integer, Instruction> instructionMap,
			Instruction inputInstruction) {
		if (instructionMap.size() > 0) {
			int startOfCount = inputInstruction.getAddress();

			/* loop to check for next 4 instructions for dependency */
			for (int i = (startOfCount + 1); i <= (startOfCount + 4); i++) {
				if (instructionMap.get(i) != null) {

					if (inputInstruction.getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.SUB.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.MUL.getInstructionType())
							|| inputInstruction.getInstructionType()
									.equals(InstructionEnum.MOVC.getInstructionType())) {
						/*
						 * I1: ADD R1 R2 R3 (inputInstr) I2: ADD R4 R1 R5
						 * (instrMap)
						 */
						if (instructionMap.get(i).getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.SUB.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MUL.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.STORE.getInstructionType())) {

							if (inputInstruction.getDestination().getOperandName()
									.equals(instructionMap.get(i).getSource1().getOperandName())) {
								inputInstruction.setDependent(true);
								break;
							}

							if (inputInstruction.getDestination().getOperandName()
									.equals(instructionMap.get(i).getSource2().getOperandName())) {
								inputInstruction.setDependent(true);
								break;
							}

						}

						/*
						 * Add/Sub/Mul/Mov -> LOAD i.e if load is dependent on
						 * any of the 4 instructions
						 */
						if (instructionMap.get(i).getInstructionType()
								.equals(InstructionEnum.LOAD.getInstructionType())) {
							if (inputInstruction.getDestination().getOperandName()
									.equals(instructionMap.get(i).getSource1().getOperandName())) {
								inputInstruction.setDependent(true);
								break;
							}
						}

					}

					/*Only STORE depends on LOAD*/
					if (inputInstruction.getInstructionType().equals(InstructionEnum.LOAD.getInstructionType())) {
						if (instructionMap.get(i).getInstructionType()
								.equals(InstructionEnum.STORE.getInstructionType())) {
							inputInstruction.setDependent(true);
							break;
						}
					}
				}
			}
		}
	}

	public static void initalizeLatches(Map<String, LatchSource> map) {
		//System.out.println("Inside initialize latch method.");
		for (int i = 0; i < 15; i++) {
			LatchSource latchSource = new LatchSource();
			latchSource.setValidFlag(0);
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			map.put(Tokens.REGISTER_PREFIX.getToken() + i, latchSource);
		}

	}

	/**
	 * If there is dependency between instructions from I->J, this function
	 * check for instruction J to see for how much cycles it has to wait in
	 * decode/RF stage. It sets the stallCycle variable accordingly
	 * 
	 * @param instructionMap
	 * @param inputInstruction
	 */
	public static void checkForInstructionStallCycles(Map<Integer, Instruction> instructionMap,
			Instruction inputInstruction) {
		//System.out.println("Inside checkForInstructionStallCycles method.");
		if (instructionMap.size() > 0) {
			int startOfCount = inputInstruction.getAddress();

			/* By default, no stall for every instruction */
			inputInstruction.setStallCycles(0);

			/* loop to check for previous 4 instructions for dependency */
			for (int i = startOfCount - 1; i >= startOfCount - 4; i--) {
				if (instructionMap.get(i) != null) {
					if (inputInstruction.getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.SUB.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.MUL.getInstructionType())
							|| inputInstruction.getInstructionType()
									.equals(InstructionEnum.STORE.getInstructionType())) {
						// I1: ADD R1 R2 R3 (inputInstr)
						// I2: ADD R4 R1 R5 (instrMap)
						if (instructionMap.get(i).getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.SUB.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MUL.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MOVC.getInstructionType())) {

							if (inputInstruction.getSource1().getOperandName()
									.equals(instructionMap.get(i).getDestination().getOperandName())) {
								/* ADD/SUB/MUL are consecutive instructions */
								if (startOfCount == (i + 1)) {
									inputInstruction.setStallCycles(1);
									break;
								}
							}

							if (inputInstruction.getSource2().getOperandName()
									.equals(instructionMap.get(i).getDestination().getOperandName())) {
								/* ADD/SUB/MUL are consecutive instructions */
								if (startOfCount == (i + 1)) {
									inputInstruction.setStallCycles(1);
									break;
								}
							}
						}
					}

					/*If Store depends on load*/
					if (inputInstruction.getInstructionType().equals(InstructionEnum.STORE.getInstructionType())) {
						if(instructionMap.get(i).getInstructionType().equals(InstructionEnum.LOAD.getInstructionType())){
							break;
						}
					}
					
					/*If ADD/SUB/MUL/MOVC depends on Load*/
					if (inputInstruction.getInstructionType().equals(InstructionEnum.LOAD.getInstructionType())) {
						if (instructionMap.get(i).getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.SUB.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MUL.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MOVC.getInstructionType())) {

							if (inputInstruction.getSource1().getOperandName()
									.equals(instructionMap.get(i).getDestination().getOperandName())) {
								/* ADD/SUB/MUL are consecutive instructions */
								if (startOfCount == (i + 1)) {
									inputInstruction.setStallCycles(1);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

}