package com.binghamton.cs520.simulator;

import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		int noOfCyclesToSimulate = 0;
		SimulateAPEXPipeline simulate = new SimulateAPEXPipeline();
		Scanner scanner = new Scanner(System.in);
		try {
			/*
			 * if (args.length != 1) {
			 * System.err.println("Please enter valid file name.");
			 * System.exit(1); }
			 */
			while (true) {
				displayOptions();
				int option = scanner.nextInt();

				switch (option) {
				case 1:
					simulate.freeMemoryAndRegisters();
					System.out.println("\tInitialized Program Counter to Address 4000.");
					break;

				case 2:
					System.out.print("\tEnter Number of cycles to simulate:: ");
					noOfCyclesToSimulate = scanner.nextInt();
					simulate.simulateProcessingCycles(noOfCyclesToSimulate);
					break;

				case 3:
					simulate.displayStages();
					break;

				case 4:
					simulate.displayMemory();
					break;

				case 5:
					System.out.println("Exiting....");
					scanner.close();
					System.exit(0);

				default:
					System.out.println("Please Select correct option.");
					break;
				}
			}

		} catch (Exception e) {
			System.out.println("\n");
			//do nothing
		}
	}

	private static void displayOptions() {
		try {
			System.out.println("\n\t========== Simulator for In-Order version of APEX ==========\n");
			System.out.println("\tSimulator Commands:\n");
			System.out.println("\t\t1) Initialize ");
			System.out.println("\t\t2) Simulate <Number of Cycles>");
			System.out.println("\t\t3) Display APEX Stages");
			System.out.println("\t\t4) Display Architecture Register File and Memory Locations");
			System.out.println("\t\t5) Exit");
			System.out.print("\tPlease choose Options from 1 - 5 : ");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
