package Scoreboard;

enum InstructionType{
    INT,MULT,FL,DIV,None;
}

public class Instruction implements Comparable<Instruction>{

    //Example Instruction: ADD R0 R1,45 (First instruction)

    InstructionType instructionType;
    String firstR;  //R0
    String secondR; //R1
    String thirdR; //45
    int order; //1
    int readOpCycle; //Cycle it is put into the readOP
    int WARCCycle; //Cycle it is put into the WARC
    int writeCycle; //Cycle it is put in the writeback

    public Instruction(InstructionType instructType, int order){
        instructionType=instructType;
        this.order=order;
    }

    public Instruction(){
        instructionType=InstructionType.None;
        order=-1;
    }

    public Instruction(String command, String r0, String r1, String r2, int order){
        instructionType = determineInstructionType(command);
        firstR = r0;
        secondR = r1;
        thirdR = r2;
        this.order=order;
    }

    /** Determines what Unit to put it into
     *
     * @param command
     * @return InstructionType
     */
    private static InstructionType determineInstructionType(String command){
        if(command.equalsIgnoreCase("LD")||command.equalsIgnoreCase("STR")) return InstructionType.INT;
        else if(command.equalsIgnoreCase("MULT")) return InstructionType.MULT;
        else if(command.equalsIgnoreCase("DIV")) return InstructionType.DIV;
        else return InstructionType.FL;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public int getOrder(){
        return order;
    }

    public boolean equals(Instruction o){
        return instructionType==o.getInstructionType();
    }

    public String toString(){
        if(instructionType==InstructionType.None) return " ";
        return instructionType+"";
    }

    @Override
    public int compareTo(Instruction o) {
        if(order<o.order) return 1;
        else if(order>o.order) return -1;
        else return 0;
    }
}
