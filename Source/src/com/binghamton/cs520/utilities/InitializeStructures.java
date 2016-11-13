package com.binghamton.cs520.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.binghamton.cs520.constants.Tokens;
import com.binghamton.cs520.entity.Instruction;

public class InitializeStructures {

	public Map<Integer, Instruction> populateInstructionsInMap() {
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
	
	public Map<String, Integer> initializeArchitectureRegisterFile() {
		System.out.println("Inside initializeRegisterFile method.");
		Map<String, Integer> architectureRegFile = new HashMap<>();
		for (int i = 0; i < 15; i++) {
			// Initializing 16 architectural registers(R0 to R15), -99999
			// indicates garbage value for each register
			architectureRegFile.put(Tokens.REGISTER_PREFIX.getToken() + i, -99999);
		}
		return architectureRegFile;
	}
	
	public int[] initializeMemory(){
		System.out.println("inside initializeMemory method");
		int[] memory = new int[1000];
		for(int i=0; i<999 ; i++){
			memory[i] = 99999;
		}
		return memory;
	}
	
	public void displayStructuresContent( Map<String, Integer> architectureRegFile, int[] memory) {
		System.out.println("--------Architecture register file--------------");
		Iterator it = architectureRegFile.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove();
		}
		
		System.out.println("--------------------Memory content -------------------------");
		for(int i=0 ; i< 50 ; i++){
			System.out.println("Memory[" + i + "] -> " + memory[i]);
		}
	}
}
