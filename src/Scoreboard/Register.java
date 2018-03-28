package Scoreboard;

import java.util.ArrayList;

//USED FOR STORE ARRAY
//STORES THE CURRENT REGISTERS
public class Register {

    ArrayList<String> registers;

    public Register(){
        registers = new ArrayList<String>();
    }

    public void addRegister(String register){
        registers.add(register);
    }

    public boolean instructionCanBeExecuted(Instruction instruction){
        if(((instruction.secondR.charAt(0)=='R'&&registers.contains(instruction.secondR))||instruction.secondR.charAt(0)!='R')&&
                ((instruction.thirdR.charAt(0)=='R'&&registers.contains(instruction.thirdR))||instruction.thirdR.charAt(0)!='R')){
            return true;
        }
        return false;
    }


}
