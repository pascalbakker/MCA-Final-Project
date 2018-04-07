
package Scoreboard;

public class Instruction implements Comparable<Instruction> {

	//Example Instruction: ADD R0 R1,45 (First instruction)

	public enum InstructionType {
		INT, MULT, FL, DIV, None;
	}

	/**
	 * Determines what Unit to put it into
	 *
	 * @param command
	 * @return InstructionType
	 */
	private static InstructionType determineInstructionType(final String command) {

		if (command.equalsIgnoreCase("LD") || command.equalsIgnoreCase("STR")) {
			return InstructionType.INT;
		} else if (command.equalsIgnoreCase("MULT")) {
			return InstructionType.MULT;
		} else if (command.equalsIgnoreCase("DIV")) {
			return InstructionType.DIV;
		} else {
			return InstructionType.FL;
		}
	}

	private final String command;

	private final InstructionType instructionType;

	private final String firstR;  //R0

	private final String secondR; //R1

	private final String thirdR; //45

	private final int order; //1

	private int readOpCycle; //Cycle it is put into the readOP

	private int WARCCycle; //Cycle it is put into the WARC

	private int writeCycle; //Cycle it is put in the writeback

	public Instruction() {
		this.instructionType = InstructionType.None;
		this.order = -1;
		this.firstR = null;
		this.secondR = null;
		this.thirdR = null;
		this.command = null;
	}

	public Instruction(final InstructionType instructType, final int order) {
		this.instructionType = instructType;
		this.order = order;
		this.firstR = null;
		this.secondR = null;
		this.thirdR = null;
		this.command = null;
	}

	public Instruction(final String command, final String r0, final String r1, final String r2, final int order) {
		this.instructionType = Instruction.determineInstructionType(command);
		this.firstR = r0;
		this.secondR = r1;
		this.thirdR = r2;
		this.order = order;
		this.command = command;
	}

	@Override
	public int compareTo(final Instruction o) {

		if (this.order < o.order) {
			return 1;
		} else if (this.order > o.order) {
			return -1;
		} else {
			return 0;
		}
	}

	public boolean equals(final Instruction o) {

		return this.instructionType == o.getInstructionType();
	}

	public String getCommand() {

		return this.command;
	}

	public String getFirstR() {

		return this.firstR;
	}

	public InstructionType getInstructionType() {

		return this.instructionType;
	}

	public int getOrder() {

		return this.order;
	}

	public int getReadOpCycle() {

		return this.readOpCycle;
	}

	public String getSecondR() {

		return this.secondR;
	}

	public String getThirdR() {

		return this.thirdR;
	}

	public int getWriteCycle() {

		return this.writeCycle;
	}

	public void setReadOpCycle(final int readOpCycle) {

		this.readOpCycle = readOpCycle;
	}

	public void setWriteCycle(final int writeCycle) {

		this.writeCycle = writeCycle;
	}

	@Override
	public String toString() {

		if (this.instructionType == InstructionType.None) {
			return " ";
		}
		return this.instructionType + "";
	}

}
