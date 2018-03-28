package Scoreboard;

import java.util.*;

public class Issuer {
    private Queue<Instruction> instruction_set;
    ArrayList<Instruction> visibleinstructions;

    Issuer(Instruction[] instructions){
        visibleinstructions = new ArrayList<Instruction>();
        instruction_set = new LinkedList<>();
        for(Instruction instruction: instructions){
            visibleinstructions.add(instruction);
            instruction_set.add(instruction);
        }

    }

    public Instruction popTop(){
        visibleinstructions.remove(instruction_set.peek());
        return instruction_set.poll();

    }

    public Instruction peekTop(){
        return instruction_set.peek();
    }

    public String toString(){
        String toString = "Queue:\n";
        for(Instruction instruction:visibleinstructions){
            if(instruction!=null)
                toString+=instruction.toString()+"\n";
        }
        toString+="\n";
        return toString;
    }
}
