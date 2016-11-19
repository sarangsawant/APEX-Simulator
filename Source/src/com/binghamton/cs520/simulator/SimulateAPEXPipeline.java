package com.binghamton.cs520.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.binghamton.cs520.constants.InstructionEnum;
import com.binghamton.cs520.constants.Tokens;
import com.binghamton.cs520.entity.Instruction;
import com.binghamton.cs520.entity.LatchSource;
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
	private List<Instruction> branchExecuteListOne = new ArrayList<>();
	private List<Instruction> branchExecuteListTwo = new ArrayList<>();
	private List<Instruction> memoryStageList = new ArrayList<>();
	private List<Instruction> writeBackStageList = new ArrayList<>();
	private Map<String, LatchSource> forwardingLatch = new HashMap<>();

	private Map<Integer, Instruction> allInstructions = new HashMap<>();
	private boolean isDecodeStageStalled = false;
	private int flag = 0;
	private String instructioninWBStage = "";

	private void fetchStageExecution() {
		//System.out.println("Inside fetchStageExecution method");
		if (fetchStageList.size() > 0)
			System.out.println("Fetch Stage 		:: "+ fetchStageList.get(0).toString());
	}

	private void decodeStageExecution() {
		//System.out.println("Inside decodeStageExecution method");
		if (decodeStageList.size() > 0) {
			//System.out.println("Instuction in decode stage(Before decode) -->" + decodeStageList.get(0).toString());

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
			} else if (InstructionEnum.BNZ.getInstructionType().equals(instructionFields[0])) {
				// BNZ #20
				decodeStageList.get(0).setInstructionType(instructionFields[0]);

				Operand literal = new Operand();
				literal.setOperandName(Tokens.LITERAL.getToken());
				literal.setOperandValue(Integer.parseInt(instructionFields[1].substring(1)));
				decodeStageList.get(0).setSource1(literal);

			}
			decodeStageList.get(0).setDecoded(true);
			InitializeStructures.checkInstructionDependency(allInstructions, decodeStageList.get(0));
			//System.out.println("Instuction in decode stage(After decode) -->" + decodeStageList.get(0).toString());
			System.out.println("Decode Stage 		:: "+ decodeStageList.get(0).toString());
		}
	}

	private void arithmeticUnitStageExecution1() {
		//System.out.println("Inside arithmeticUnitStageExecution 1");
		if (arithExecuteStageOneList.size() > 0) {
			//System.out.println("Arith stage 1 --> " + arithExecuteStageOneList.get(0).toString());
			Instruction arithStage1instruction = arithExecuteStageOneList.get(0);
			int source1 = 0, source2 = 0;

			/*
			 * Populate source1 and source2 for Add,Sub and Mul instruction
			 * correctly. i.e check if it has any valid forwarded result
			 */
			if (InstructionEnum.ADD.getInstructionType().equals(arithStage1instruction.getInstructionType())
					|| InstructionEnum.SUB.getInstructionType().equals(arithStage1instruction.getInstructionType())
					|| InstructionEnum.MUL.getInstructionType().equals(arithStage1instruction.getInstructionType())
					|| InstructionEnum.STORE.getInstructionType().equals(arithStage1instruction.getInstructionType())) {

				/*
				 * If valid flag is set for dependent register in forwarding
				 * latch, get latest forwarded value from latch and set flag to
				 * 0
				 */
				if (forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).getValidFlag() == 1) {
					source1 = forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).getValue();
					forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).setValidFlag(0);
				} else
					source1 = arithStage1instruction.getSource1().getOperandValue();

				/*
				 * If valid flag is set for dependent register in forwarding
				 * latch, get latest forwarded value from latch and set flag to
				 * 0
				 */
				if (forwardingLatch.get(arithStage1instruction.getSource2().getOperandName()).getValidFlag() == 1) {
					source2 = forwardingLatch.get(arithStage1instruction.getSource2().getOperandName()).getValue();
					forwardingLatch.get(arithStage1instruction.getSource2().getOperandName()).setValidFlag(0);
				} else
					source2 = arithStage1instruction.getSource2().getOperandValue();
			}
			// check for instruction type
			if (InstructionEnum.ADD.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				// logic for ADD instruction
				int dest = source1 + source2;
				arithExecuteStageOneList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.SUB.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				// logic for SUB instruction
				int dest = source1 - source2;
				arithExecuteStageOneList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.MUL.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				// logic for MUL instruction
				int dest = source1 * source2;
				arithExecuteStageOneList.get(0).getDestination().setOperandValue(dest);
			} else if (InstructionEnum.LOAD.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				// LOAD dest src1 literal
				int src1;
				if (forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).getValidFlag() == 1) {
					src1 = forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).getValue();
					forwardingLatch.get(arithStage1instruction.getSource1().getOperandName()).setValidFlag(0);
				} else
					src1 = arithStage1instruction.getSource1().getOperandValue();

				int literal = arithStage1instruction.getSource2().getOperandValue();
				int memoryAddr = src1 + literal;
				arithExecuteStageOneList.get(0).getDestination().setOperandValue(memoryAddr);
			} else if (InstructionEnum.STORE.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				// STORE src1 src2 literal

				/*
				 * get latest value of source 1 i.e get forwarded value if any
				 */
				arithExecuteStageOneList.get(0).getSource1().setOperandValue(source1);

				int literal = arithStage1instruction.getSource3().getOperandValue();
				int memoryAddr = source2 + literal;
				Operand destination = new Operand();
				destination.setOperandValue(memoryAddr);
				arithExecuteStageOneList.get(0).setDestination(destination);
			} else if (InstructionEnum.MOVC.getInstructionType().equals(arithStage1instruction.getInstructionType())) {
				int literal = arithStage1instruction.getSource1().getOperandValue();
				arithExecuteStageOneList.get(0).getDestination().setOperandValue(literal);
			}
			System.out.println("ALU 1 Stage 		:: "+ arithExecuteStageOneList.get(0).toString());
			arithExecuteStageOneList.get(0).setALU1Executed(true);
		}
	}

	private void arithmeticUnitStageExecution2() {
		//System.out.println("Inside arithmeticUnitStageExecution 2");
		if (arithExecuteStageTwoList.size() > 0) {
			System.out.println("ALU 2 Stage 		:: "+ arithExecuteStageTwoList.get(0).toString());
			if (arithExecuteStageTwoList.get(0).isDependent()) {
				forwardGeneratedResult(arithExecuteStageTwoList.get(0));
			}
			arithExecuteStageTwoList.get(0).setALU2Executed(true);
		}
	}

	/**
	 * Branch ALU
	 */
	private void branchExecutionStage1() {
		/*
		 * if(branchExecuteListOne.size() > 0){
		 * if(InstructionEnum.BAL.getInstructionType().equals(
		 * branchExecuteListOne.get(0).getInstructionType())){ //BAL R10, #19
		 * int source1address =
		 * branchExecuteListOne.get(0).getSource1().getOperandValue(); int
		 * literal = branchExecuteListOne.get(0).getSource2().getOperandValue();
		 * int destinationAddress = source1address + literal;
		 * branchExecuteListOne.get(0).getDestination().setOperandValue(
		 * destinationAddress); //execute branch from next stages programCnt =
		 * destinationAddress; }else
		 * if(InstructionEnum.BZ.getInstructionType().equals(
		 * branchExecuteListOne.get(0).getInstructionType())){ //BZ #20 //need
		 * to check the above instruction is in EX stage. } else
		 * if(InstructionEnum.BNZ.getInstructionType().equals(
		 * branchExecuteListOne.get(0).getInstructionType())){ //BNZ # -20 or
		 * BNZ #12 //need to check the above instruction is in EX stage. } else
		 * if(InstructionEnum.JUMP.getInstructionType().equals(
		 * branchExecuteListOne.get(0).getInstructionType())){ //JUMP X, #0
		 * where x is special register set to the next instruction of BAL }
		 * 
		 * }else{ //handle code here }
		 */
		System.out.println("Inside branchExecutionStage1");
	}

	private void branchExecutionDelay() {
		System.out.println("Inside branchExecutionDelay");
	}

	private void memoryStageExecution() {
		//System.out.println("memoryStageExecution");
		if (memoryStageList.size() > 0) {
			System.out.println("Memory Stage 		:: "+ memoryStageList.get(0).toString());
			if (InstructionEnum.LOAD.getInstructionType().equals(memoryStageList.get(0).getInstructionType())) {
				memoryStageList.get(0).getDestination()
						.setOperandValue(memory[memoryStageList.get(0).getDestination().getOperandValue()]);
				/**/
				forwardGeneratedResult(memoryStageList.get(0));
			}
			if (InstructionEnum.STORE.getInstructionType().equals(memoryStageList.get(0).getInstructionType())) {
				/*
				 * If valid flag is set for dependent register in forwarding
				 * latch, get latest forwarded value from latch and set flag to
				 * 0
				 */
				int source1;
				if (forwardingLatch.get(memoryStageList.get(0).getSource1().getOperandName()).getValidFlag() == 1) {
					source1 = forwardingLatch.get(memoryStageList.get(0).getSource1().getOperandName()).getValue();
					forwardingLatch.get(memoryStageList.get(0).getSource1().getOperandName()).setValidFlag(0);
				} else
					source1 = memoryStageList.get(0).getSource1().getOperandValue();

				memory[memoryStageList.get(0).getDestination().getOperandValue()] = source1;
			}
			memoryStageList.get(0).setMemoryExecuted(true);
		}
	}

	private void writeBackStageExecution() {
		//System.out.println("writeBackStageExecution");
		if (writeBackStageList.size() > 0)
			System.out.println("Writeback Stage 	:: "+ writeBackStageList.get(0).toString());
			instructioninWBStage =  writeBackStageList.get(0).toString();
			
		if (InstructionEnum.ADD.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.SUB.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.MUL.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.DIV.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())
				|| InstructionEnum.LOAD.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())) {

			architectureRegFile.put(writeBackStageList.get(0).getDestination().getOperandName(),
					writeBackStageList.get(0).getDestination().getOperandValue());
		}
		if (InstructionEnum.MOVC.getInstructionType().equals(writeBackStageList.get(0).getInstructionType())) {
			architectureRegFile.put(writeBackStageList.get(0).getDestination().getOperandName(),
					writeBackStageList.get(0).getSource1().getOperandValue());
		}
	}

	private void forwardGeneratedResult(Instruction instruction) {
		//System.out.println("Inside forwardGeneratedResult method.");
		if ((instruction.getInstructionType().equals(InstructionEnum.ADD.getInstructionType())
				|| instruction.getInstructionType().equals(InstructionEnum.SUB.getInstructionType())
				|| instruction.getInstructionType().equals(InstructionEnum.MUL.getInstructionType())
				|| instruction.getInstructionType().equals(InstructionEnum.MOVC.getInstructionType())
				|| instruction.getInstructionType().equals(InstructionEnum.LOAD.getInstructionType()))
				&& instruction.isDependent()) {
			forwardingLatch.get(instruction.getDestination().getOperandName())
					.setValue(instruction.getDestination().getOperandValue());
			forwardingLatch.get(instruction.getDestination().getOperandName()).setValidFlag(1);
		}
	}

	/*
	 * populate instructions in map from file, initalize architectural register
	 * file and memory
	 */
	public void simulateProcessingCycles(int totalSimulateCycles) {
		// InitializeStructures structures = new InitializeStructures();
		instructions = InitializeStructures.populateInstructionsInMap();
		architectureRegFile = InitializeStructures.initializeArchitectureRegisterFile();
		memory = InitializeStructures.initializeMemory();

		allInstructions = InitializeStructures.initAllInstructionsFields(instructions, architectureRegFile);
		InitializeStructures.initalizeLatches(forwardingLatch);

		for (int i = 0; i < totalSimulateCycles; i++) {
			System.out.println("\nCYCLE --" + (i + 1));

			if (!isDecodeStageStalled) {
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
			}

			// Decode Stage
			if (decodeStageList.size() == 1 && !decodeStageList.get(0).isDecoded()) {
				decodeStageExecution();
				continue;
			} else if (decodeStageList.size() > 0) {
				if (decodeStageList.get(0).getStallCycles() == -1)
					InitializeStructures.checkForInstructionStallCycles(allInstructions, decodeStageList.get(0));

				if (decodeStageList.get(0).getStallCycles() == 0) {
					arithExecuteStageOneList.add(decodeStageList.get(0));
					decodeStageList.remove(0);
					decodeStageExecution();
					isDecodeStageStalled = false;
				} else {
					int stallCycles = decodeStageList.get(0).getStallCycles();
					decodeStageList.get(0).setStallCycles(stallCycles - 1);
					isDecodeStageStalled = true;
				}
			}

			// ALU stage 1
			if (arithExecuteStageOneList.size() == 1 && !arithExecuteStageOneList.get(0).isALU1Executed()) {
				arithmeticUnitStageExecution1();

				/*
				 * Decode stage has recovered from stall, so instructions in
				 * next stages should continue execution
				 */
				if (arithExecuteStageTwoList.size() == 0)
					continue;
			} else if (arithExecuteStageOneList.size() > 0) {
				arithExecuteStageTwoList.add(arithExecuteStageOneList.get(0));
				arithExecuteStageOneList.remove(0);
				arithmeticUnitStageExecution1();
			}

			// ALU stage 2
			if (arithExecuteStageTwoList.size() == 1 && !arithExecuteStageTwoList.get(0).isALU2Executed()) {
				arithmeticUnitStageExecution2();

				/*
				 * Decode stage has recovered from stall, so instructions in
				 * next stages should continue execution
				 */
				if (memoryStageList.size() == 0)
					continue;
			} else if (arithExecuteStageTwoList.size() > 0) {
				memoryStageList.add(arithExecuteStageTwoList.get(0));
				arithExecuteStageTwoList.remove(0);
				arithmeticUnitStageExecution2();
			}

			// MemoryStage
			if (memoryStageList.size() == 1 && !memoryStageList.get(0).isMemoryExecuted()) {
				memoryStageExecution();

				/*
				 * Decode stage has recovered from stall, so instructions in
				 * next stages should continue execution
				 */
				if (writeBackStageList.size() == 0)
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
		//InitializeStructures.displayStructuresContent(architectureRegFile, memory, forwardingLatch);
	}

	public void freeMemoryAndRegisters() {
		architectureRegFile.clear();
		fetchStageList.clear();
		decodeStageList.clear();
		arithExecuteStageOneList.clear();
		arithExecuteStageTwoList.clear();
		memoryStageList.clear();
		writeBackStageList.clear();
		forwardingLatch.clear();
	}

	void displayStages() {
		System.out.println("At the end of last cycle Below are the contents of each stages: ");
		if(fetchStageList.get(0) != null){
			System.out.println("Fetch Stage 		:: "+fetchStageList.get(0).getInstruction());
		}
		
		
		if(decodeStageList.get(0) != null){
			System.out.println("Decode Stage 		:: "+decodeStageList.get(0).getInstruction());
		}
		
		if(arithExecuteStageOneList.get(0) != null){
			System.out.println("ALU 1 Stage 		:: "+arithExecuteStageOneList.get(0).getInstruction());
		}
		
		if(arithExecuteStageTwoList.get(0) != null){
			System.out.println("ALU 2 Stage 		:: "+arithExecuteStageTwoList.get(0).getInstruction());
		}
		
		if(memoryStageList.get(0) != null){
			System.out.println("Memory Stage 		:: "+memoryStageList.get(0).getInstruction());
			System.out.println("Writeback Stage 	:: "+instructioninWBStage);
		}
		
		
		
	}

	public void displayMemory() {
		System.out.println("---------- Architecture Register File ----------");
		Iterator it = architectureRegFile.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if(!(pair.getValue().toString().equals("-99999"))){
				System.out.println(pair.getKey() + " = " + pair.getValue());
				it.remove();
			}
		}
		System.out.println("\n");
		System.out.println(" ---------- Memory content ----------");
		for (int i = 0; i < 100; i++) {
			if(!(memory[i] == 99999)){
				System.out.println("Memory[" + i + "] -> " + memory[i]);
			}
		}

	}
}
