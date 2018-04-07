
package Scoreboard;

import java.util.ArrayList;

//USED FOR STORE ARRAY
//STORES THE CURRENT REGISTERS
public class Register {

	ArrayList<String> registers;

	public Register() {
		this.registers = new ArrayList<>();
	}

	public void addRegister(final String register) {

		this.registers.add(register);
	}

	public boolean instructionCanBeExecuted(final Instruction instruction) {

		if ((instruction.getSecondR().charAt(0) == 'R' && this.registers.contains(instruction.getSecondR()) || instruction.getSecondR().charAt(0) != 'R') && (instruction.getThirdR().charAt(0) == 'R' && this.registers.contains(instruction.getThirdR()) || instruction.getThirdR().charAt(0) != 'R')) {
			return true;
		}
		return false;
	}

}
