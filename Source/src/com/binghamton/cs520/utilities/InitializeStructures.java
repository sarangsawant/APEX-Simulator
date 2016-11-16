package com.binghamton.cs520.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
			String line;
			try {
				while ((line = br.readLine()) != null) {
					String data[] = line.split(Tokens.COLON.getToken());
					Instruction instruction = new Instruction();
					instruction.setInstruction(data[1]);
					instruction.setAddress(Integer.parseInt(data[0]));
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
		return instructions;
	}

	public static Map<String, Integer> initializeArchitectureRegisterFile() {
		System.out.println("Inside initializeRegisterFile method.");
		Map<String, Integer> architectureRegFile = new HashMap<>();
		for (int i = 0; i < 15; i++) {
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			architectureRegFile.put(Tokens.REGISTER_PREFIX.getToken() + i, -99999);
		}
		return architectureRegFile;
	}

	public static int[] initializeMemory() {
		System.out.println("inside initializeMemory method");
		int[] memory = new int[1000];
		for (int i = 0; i < 999; i++) {
			memory[i] = 99999;
		}
		return memory;
	}

	public static void displayStructuresContent(Map<String, Integer> architectureRegFile, int[] memory,
			Map<String, LatchSource> forwardingLatch) {
		System.out.println("--------Architecture register file--------------");
		Iterator it = architectureRegFile.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove();
		}

		System.out.println("--------------------Memory content -------------------------");
		for (int i = 0; i < 50; i++) {
			System.out.println("Memory[" + i + "] -> " + memory[i]);
		}

		System.out.println("------------Forwarding latch--------------");
		Iterator iterator = forwardingLatch.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry pair = (Map.Entry) iterator.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			iterator.remove();
		}
	}

	public static Map<Integer, Instruction> initAllInstructionsFields(Map<Integer, Instruction> instructions2,
			Map<String, Integer> architectureRegFile) {
		System.out.println(instructions2.size());
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
			}
			instructionMap.put(counter, inputInstruction);
			System.out.println("instruction obj--> " + counter + "  " + instructionMap.get(counter));
			counter++;
		}
		return instructionMap;
	}

	/*
	 * This method sets dependent field of instruction object to true if its
	 * destination is same any source of the next consecutive 4 instructions
	 */
	public static void checkInstructionDependency(Map<Integer, Instruction> instructionMap,
			Instruction inputInstruction) {
		if (instructionMap.size() > 0) {
			int startOfCount = inputInstruction.getAddress(); // relative to
																// address of
																// the
																// instruction

			/* loop to check for next 4 instructions for dependency */
			for (int i = startOfCount; i < (startOfCount + 4); i++) {
				if (instructionMap.get(i) != null) {
					if (inputInstruction.getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.SUB.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.MUL.getInstructionType())) {
						/*
						 * I1: ADD R1 R2 R3 (inputInstr) I2: ADD R4 R1 R5
						 * (instrMap)
						 */
						if (instructionMap.get(i).getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.SUB.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MUL.getInstructionType())) {

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
						 * if(instructionMap.get(i).getInstructionType().equals(
						 * InstructionEnum.STORE.getInstructionType())){
						 * 
						 * if(inputInstruction.getDestination().getOperandName()
						 * .equals(instructionMap.get(i).getSource1().
						 * getOperandName())){ break; }
						 * 
						 * if(inputInstruction.getDestination().getOperandName()
						 * .equals(instructionMap.get(i).getSource2().
						 * getOperandName())){ break; }
						 * 
						 * inputInstruction.setLatchIndex(0); //0 for ALU
						 * 
						 * }
						 * 
						 * if(instructionMap.get(i).getInstructionType().equals(
						 * InstructionEnum.LOAD.getInstructionType())){
						 * if(inputInstruction.getDestination().getOperandName()
						 * .equals(instructionMap.get(i).getSource1().
						 * getOperandName())){ break; }
						 * 
						 * inputInstruction.setLatchIndex(0); //0 for ALU }
						 */
					}
				}
			}
		}
	}

	public static void initalizeLatches(Map<String, LatchSource> map) {
		System.out.println("Inside initialize latch method.");
		for (int i = 0; i < 15; i++) {
			LatchSource latchSource = new LatchSource();
			latchSource.setValidFlag(0);
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			map.put(Tokens.REGISTER_PREFIX.getToken() + i, latchSource);
		}

	}

	public static boolean checkForInstructionStall(Map<Integer, Instruction> instructionMap, Instruction inputInstruction) {
		boolean stallInstruction = false;
		if (instructionMap.size() > 0) {
			int startOfCount = inputInstruction.getAddress();

			// loop to check for previous 4 instructions for dependency
			for (int i = startOfCount-1; i >= startOfCount - 4; i--) {
				if (instructionMap.get(i) != null) {
					if (inputInstruction.getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.SUB.getInstructionType())
							|| inputInstruction.getInstructionType().equals(InstructionEnum.MUL.getInstructionType())) {
						// I1: ADD R1 R2 R3 (inputInstr)
						// I2: ADD R4 R1 R5 (instrMap)
						if (instructionMap.get(i).getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.SUB.getInstructionType())
								|| instructionMap.get(i).getInstructionType()
										.equals(InstructionEnum.MUL.getInstructionType())) {

							if (inputInstruction.getSource1().getOperandName()
									.equals(instructionMap.get(i).getDestination().getOperandName())) {
								/* ADD/SUB/MUL are consecutive instructions */
								if (startOfCount == (i+1))
									stallInstruction = true;
							}

							if (inputInstruction.getSource1().getOperandName()
									.equals(instructionMap.get(i).getDestination().getOperandName())) {
								/* ADD/SUB/MUL are consecutive instructions */
								if (startOfCount == (i+1))
									stallInstruction = true;
							}
						}
					}
				}
			}
		}
		System.out.println("Check stall -> " + stallInstruction);
		return stallInstruction;
	}

}