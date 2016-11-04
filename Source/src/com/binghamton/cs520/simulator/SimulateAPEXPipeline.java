package com.binghamton.cs520.simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.binghamton.cs520.constants.Tokens;

public class SimulateAPEXPipeline {
	private static Map<Integer, String> instructions = new HashMap<>();
	private static int programCnt = 4000;
	
	
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
					instructions.put(Integer.parseInt(data[0]), data[1]);
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
	
	private void fetchInstruction(){
			String instruction = instructions.get(programCnt);
			programCnt = programCnt + 1;
			decodeInstruction(instruction);
		
	}
	
	private void decodeInstruction(String instruction){
		
		String data[] = instruction.split(Tokens.SPACE.getToken());
		
	}
	
	private void functionalUnitALU(){
		System.out.println("functionalUnitALU");
	}
	
	private void functionalUnitBranch(){
		System.out.println("functionalUnitBranch");
	}
	
	public void simulateProcessingCycles(){
		populateInstructionsInMap();
		fetchInstruction();
	}
	
}
