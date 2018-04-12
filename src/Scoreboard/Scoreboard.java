


package Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {

	//	//TESTER METHOD
	//	public static void main(final String[] args) {
	//
	//		final Instruction[] instructions = { new Instruction("LD", "R6", "36", "0", 1), new Instruction("LD", "R2", "45", "0", 2), new Instruction("LD", "R4", "45", "0", 3), new Instruction("MULT", "R0", "R2", "R4", 4), new Instruction("SUB", "R8", "R6", "R2", 5), new Instruction("DIV", "R10", "R0", "R6", 6), new Instruction("ADD", "R6", "R8", "R2", 7) };
	//
	//		final Scoreboard scoreboard = new Scoreboard(instructions, true);
	//
	//		while (scoreboard.writeback.size() != instructions.length) {
	//			System.out.println(scoreboard.queueToString());
	//			scoreboard.printRegisters();
	//			System.out.println(scoreboard.toString());
	//			scoreboard.oneClock();
	//		}
	//		System.out.println(scoreboard.queueToString());
	//		scoreboard.printRegisters();
	//		System.out.println(scoreboard.toString());
	//		scoreboard.oneClock();
	//	}

	private final Unit INT, M, FL, DIV;

	private final Issuer issuer;

	private int cycle;

	private final ArrayList<Instruction> writeback;

	private final Register storeRegister;

	private final CurrentRegister currentRegister;

	//PRIVATE METHODS

	public Scoreboard(final List<Instruction> instructionSet, final boolean isPipelined) {
		this.cycle = 0;
		this.writeback = new ArrayList<>();
		this.INT = new Unit(1, isPipelined);
		this.M = new Unit(10, isPipelined);
		this.FL = new Unit(2, isPipelined);
		this.DIV = new Unit(40, isPipelined);
		this.issuer = new Issuer(instructionSet);
		this.storeRegister = new Register();
		this.currentRegister = new CurrentRegister();
	}

	public ArrayList<String> getCurrentRegister() {

		return this.currentRegister.registers;
	}

	//PUBLIC METHODS

	/**
	 * Get the current cycle
	 *
	 * @return int
	 */
	public int getCycle() {

		return this.cycle;
	}

	public Unit getDIV() {

		return this.DIV;
	}

	public Unit getFL() {

		return this.FL;
	}

	public Unit getINT() {

		return this.INT;
	}

	//GETTERS
	public Issuer getIssuer() {

		return this.issuer;
	}

	public Unit getM() {

		return this.M;
	}

	public ArrayList<String> getStoreRegister() {

		return this.storeRegister.registers;
	}

	public ArrayList<Instruction> getWriteback() {

		return this.writeback;
	}

	/**
	 * Runs Scoreboard for one clock cycle
	 *
	 */
	public void oneClock() {

		//First writeback
		this.writeBackOutOfOrder();
		//this.writeBackInOrderOneClock();

		//Then Units
		this.FL.oneClock(this.currentRegister, this.storeRegister);
		this.INT.oneClock(this.currentRegister, this.storeRegister);
		this.M.oneClock(this.currentRegister, this.storeRegister);
		this.DIV.oneClock(this.currentRegister, this.storeRegister);

		//Then issuer
		this.oneClockIssuer();
		this.cycle++;

	}

	/**
	 * This method takes instructions from the queue and puts it in the correct readOP
	 *
	 */
	private void oneClockIssuer() {

		//First get instruction from Queue and put in proper set
		final Instruction queuedInstruction = this.issuer.peekTop();
		if (queuedInstruction == null) {
			return;
		}

		//Add instruction to proper readOP
		switch (queuedInstruction.getInstructionType()) {
			case FL:
				if (this.FL.readOPIsOpen()) {
					queuedInstruction.setReadOpCycle(this.cycle);
					this.currentRegister.addRegister(queuedInstruction.getFirstR());
					this.FL.readOp = queuedInstruction;
					this.issuer.popTop();
				}
				break;
			case MULT:
				if (this.M.readOPIsOpen()) {
					queuedInstruction.setReadOpCycle(this.cycle);
					this.currentRegister.addRegister(queuedInstruction.getFirstR());
					this.M.readOp = queuedInstruction;
					this.issuer.popTop();
				}
				break;
			case INT:
				if (this.INT.readOPIsOpen()) {
					queuedInstruction.setReadOpCycle(this.cycle);
					this.currentRegister.addRegister(queuedInstruction.getFirstR());
					this.INT.readOp = queuedInstruction;
					this.issuer.popTop();
				}
				break;
			case DIV:
				if (this.DIV.readOPIsOpen()) {
					queuedInstruction.setReadOpCycle(this.cycle);
					this.currentRegister.addRegister(queuedInstruction.getFirstR());
					this.DIV.readOp = queuedInstruction;
					this.issuer.popTop();
				}
				break;
		}
	}

	/**
	 * Print array of the current and store reigster
	 *
	 */
	public String printRegisters() {

		return "currentRegister: " + this.currentRegister.registers.toString() + "\n" + "storeRegister: " + this.storeRegister.registers.toString();

	}

	/**
	 * Prints all instructions in the queue
	 *
	 * @return String
	 */
	public String queueToString() {

		return this.issuer.toString();
	}

	/**
	 * Prints the scoreboard
	 *
	 * @return String
	 */
	@Override
	public String toString() {

		final String scoreboardString = "\nCycle: " + this.cycle + "\n INT " + this.INT.toString() + "\n FL " + this.FL.toString() + "\n MULT " + this.M.toString() + "\n DIV" + this.DIV.toString();
		return scoreboardString;
	}

	/**
	 * This method takes the instruction with the lowest order and puts it into the writeback Array
	 *
	 */
	private void writeBackInOrderOneClock() {

		int currentOrder;
		if (this.writeback.size() == 0) {
			currentOrder = 0;
		} else {
			currentOrder = this.writeback.get(this.writeback.size() - 1).getOrder();
		}

		if (this.FL.WARC.getOrder() - 1 == currentOrder) {
			this.FL.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.FL.WARC);
			this.storeRegister.addRegister(this.FL.WARC.getFirstR());
			this.currentRegister.removeRegister(this.FL.WARC.getFirstR());
			this.FL.WARC = new Instruction();
		} else if (this.M.WARC.getOrder() - 1 == currentOrder) {
			this.M.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.M.WARC);
			this.storeRegister.addRegister(this.M.WARC.getFirstR());
			this.currentRegister.removeRegister(this.M.WARC.getFirstR());
			this.M.WARC = new Instruction();
		} else if (this.DIV.WARC.getOrder() - 1 == currentOrder) {
			this.DIV.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.DIV.WARC);
			this.storeRegister.addRegister(this.DIV.WARC.getFirstR());
			this.currentRegister.removeRegister(this.DIV.WARC.getFirstR());
			this.DIV.WARC = new Instruction();
		} else if (this.INT.WARC.getOrder() - 1 == currentOrder) {
			this.INT.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.INT.WARC);
			this.storeRegister.addRegister(this.INT.WARC.getFirstR());
			this.currentRegister.removeRegister(this.INT.WARC.getFirstR());
			this.INT.WARC = new Instruction();
		}
	}
	
	//Write back out of order method used
	private void writeBackOutOfOrder() {
		if(this.FL.WARC.getOrder()!= -1 && currentRegister.instructionCanBeExecuted(this.FL.WARC)) {
			this.FL.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.FL.WARC);
			this.storeRegister.addRegister(this.FL.WARC.getFirstR());
			this.currentRegister.removeRegister(this.FL.WARC.getFirstR());
			this.FL.WARC = new Instruction();
		}else if(this.M.WARC.getOrder()!= -1 && currentRegister.instructionCanBeExecuted(this.M.WARC)) {
			this.M.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.M.WARC);
			this.storeRegister.addRegister(this.M.WARC.getFirstR());
			this.currentRegister.removeRegister(this.M.WARC.getFirstR());
			this.M.WARC = new Instruction();
		}else if(this.DIV.WARC.getOrder()!= -1 && currentRegister.instructionCanBeExecuted(this.DIV.WARC)) {
			this.DIV.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.DIV.WARC);
			this.storeRegister.addRegister(this.DIV.WARC.getFirstR());
			this.currentRegister.removeRegister(this.DIV.WARC.getFirstR());
			this.DIV.WARC = new Instruction();
		}else if(this.INT.WARC.getOrder()!= -1 && currentRegister.instructionCanBeExecuted(this.INT.WARC)) {
			this.INT.WARC.setWriteCycle(this.cycle);
			this.writeback.add(this.INT.WARC);
			this.storeRegister.addRegister(this.INT.WARC.getFirstR());
			this.currentRegister.removeRegister(this.INT.WARC.getFirstR());
			this.INT.WARC = new Instruction();
		}
	}
	
	public void printWriteback() {
		
	}
}
