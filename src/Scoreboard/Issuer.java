
package Scoreboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Issuer {

	private final Queue<Instruction> instruction_set;

	ArrayList<Instruction> visibleinstructions;

	Issuer(final List<Instruction> instructions) {
		this.visibleinstructions = new ArrayList<>();
		this.instruction_set = new LinkedList<>();
		for (final Instruction instruction : instructions) {
			this.visibleinstructions.add(instruction);
			this.instruction_set.add(instruction);
		}

	}

	public Instruction peekTop() {

		return this.instruction_set.peek();
	}

	public Instruction popTop() {

		this.visibleinstructions.remove(this.instruction_set.peek());
		return this.instruction_set.poll();

	}

	@Override
	public String toString() {

		String toString = "Queue:\n";
		for (final Instruction instruction : this.visibleinstructions) {
			if (instruction != null) {
				toString += instruction.toString() + "\n";
			}
		}
		toString += "\n";
		return toString;
	}
}
