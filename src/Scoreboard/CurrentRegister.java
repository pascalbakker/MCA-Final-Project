package Scoreboard;

import java.util.ArrayList;

//USED THE CURRENTREIGSTER
public class CurrentRegister {

    ArrayList<String> registers;

    public CurrentRegister(){
        registers = new ArrayList<String>();
    }

    public void addRegister(String register){
        registers.add(register);
    }

    public boolean instructionCanBeExecuted(Instruction instruction){
        if((instruction.secondR.charAt(0)!='R'&&registers.contains(instruction.secondR))||(instruction.thirdR.charAt(0)!='R'&&registers.contains(instruction.thirdR))) return false;
        return true;
    }

    public void removeRegister(String register){
        registers.remove(register);
    }
}
