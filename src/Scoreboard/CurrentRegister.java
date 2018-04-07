
package Scoreboard;

import java.util.ArrayList;

//USED THE CURRENTREIGSTER
public class CurrentRegister {

	ArrayList<String> registers;

	public CurrentRegister() {
		this.registers = new ArrayList<>();
	}

	public void addRegister(final String register) {

		this.registers.add(register);
	}

	public boolean instructionCanBeExecuted(final Instruction instruction) {

		if (instruction.getSecondR().charAt(0) != 'R' && this.registers.contains(instruction.getSecondR()) || instruction.getThirdR().charAt(0) != 'R' && this.registers.contains(instruction.getThirdR())) {
			return false;
		}
		return true;
	}

	public void removeRegister(final String register) {

		this.registers.remove(register);
	}
}
