
package Scoreboard;

public class Unit {

	Instruction[] pipe;

	Instruction readOp;

	Instruction WARC;

	private final boolean isPipelined;

	Unit(final Instruction[] pipe, final boolean isPipelined) {
		this.pipe = pipe;
		this.isPipelined = isPipelined;
	}

	Unit(final int pipeSize, final boolean isPipelined) {
		this.pipe = new Instruction[pipeSize];
		for (int i = 0; i < pipeSize; i++) {
			this.pipe[i] = new Instruction();
		}
		this.isPipelined = isPipelined;
		this.readOp = new Instruction();
		this.WARC = new Instruction();
	}

	//Obselete Method
	public void oneClock() {

		//Put Instruction in WARC if possible
		if (this.WARCIsOpen()) {
			this.WARC = this.pipe[this.pipe.length - 1];
			this.pipe[this.pipe.length - 1] = new Instruction();
		}

		//Execute units
		for (int i = this.pipe.length - 1; i > 0; i--) {
			if (this.pipe[i].getOrder() == -1) {
				this.pipe[i] = this.pipe[i - 1];
				this.pipe[i - 1] = new Instruction();
			}
		}

		//Put item from readOP to pipe[0] if possible
		if (this.pipe[0].getOrder() == -1 && this.readOp.getOrder() != -1) {
			this.pipe[0] = this.readOp;
			this.readOp = new Instruction();
		}
	}

	public void oneClock(final CurrentRegister cReg, final Register sReg) {

		//Put Instruction in WARC if possible
		if (this.WARCIsOpen()) {
			this.WARC = this.pipe[this.pipe.length - 1];
			this.pipe[this.pipe.length - 1] = new Instruction();
		}
		//Execute units
		for (int i = this.pipe.length - 1; i > 0; i--) {
			if (this.pipe[i].getOrder() == -1) {
				this.pipe[i] = this.pipe[i - 1];
				this.pipe[i - 1] = new Instruction();
			}
		}

		//Put item from readOP to pipe[0] if possible
		if (this.pipe[0].getOrder() == -1 && this.readOp.getOrder() != -1 && cReg.instructionCanBeExecuted(this.readOp) && sReg.instructionCanBeExecuted(this.readOp)) {
			this.pipe[0] = this.readOp;
			this.readOp = new Instruction();
		}

	}

	public boolean readOPIsOpen() {

		final boolean readOPboolean = this.readOp.getInstructionType() == null || this.readOp.getOrder() == -1;
		return readOPboolean;
	}

	@Override
	public String toString() {

		String blockString = "|" + this.readOp.toString() + "|---|";
		for (final Instruction instruction : this.pipe) {
			if (instruction == null) {
				blockString += " ";
			} else {
				blockString += instruction.toString() + "|";
			}
		}

		blockString += "---" + this.WARC.toString();

		return blockString;
	}

	public boolean WARCIsOpen() {

		final boolean warcboolean = this.WARC.getInstructionType() == null || this.WARC.getOrder() == -1;
		return warcboolean;
	}
}
