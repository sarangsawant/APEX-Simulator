package com.binghamton.cs520.simulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.binghamton.cs520.constants.InstructionEnum;
import com.binghamton.cs520.constants.Tokens;


public class SimulateAPEXPipeline {

	public static void main(String[] args) {
		String fileName = "src/Input.txt";
		List<String> instructions = new ArrayList<>();

		try {
			instructions = Files.readAllLines(Paths.get(fileName),
			        StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (String instruction : instructions) {
			String[] data = instruction.split(Tokens.SPACE.getToken());
			if(data[0].equals(InstructionEnum.MOVC.toString())){
				//TODO: Excecute MOVC function
			}
			if(data[0].equals(InstructionEnum.ADD.toString())){
				//TODO: Excecute ADD function
			}
			if(data[0].equals(InstructionEnum.SUB.toString())){
				//TODO: Excecute SUB function
			}
		}
	}
}
