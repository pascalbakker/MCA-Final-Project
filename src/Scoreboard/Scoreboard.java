package Scoreboard;

import java.util.ArrayList;

public class Scoreboard{

    private Unit INT,M,FL,DIV;
    private Issuer issuer;
    private int cycle;
    private ArrayList<Instruction> writeback;
    private Register storeRegister;
    private CurrentRegister currentRegister;


    public Scoreboard(Instruction[]instructionSet, boolean isPipelined){
        cycle = 0;
        writeback = new ArrayList<Instruction>();
        INT = new Unit(1,isPipelined);
        M = new Unit(10,isPipelined);
        FL = new Unit(2,isPipelined);
        DIV = new Unit(40,isPipelined);
        issuer =  new Issuer(instructionSet);
        storeRegister = new Register();
        currentRegister = new CurrentRegister();
    }

    //PRIVATE METHODS

    /** This method takes instructions from the queue and puts it in the correct readOP
     *
     */
    private void oneClockIssuer(){
        //First get instruction from Queue and put in proper set
        Instruction queuedInstruction = issuer.peekTop();
        if(queuedInstruction==null) return;

        //Add instruction to proper readOP
        switch (queuedInstruction.getInstructionType()){
            case FL:
                if(FL.readOPIsOpen()){
                    queuedInstruction.readOpCycle = cycle;
                    currentRegister.addRegister(queuedInstruction.firstR);
                    FL.readOp=queuedInstruction;
                    issuer.popTop();
                }
                break;
            case MULT:
                if(M.readOPIsOpen()){
                    queuedInstruction.readOpCycle = cycle;
                    currentRegister.addRegister(queuedInstruction.firstR);
                    M.readOp=queuedInstruction;
                    issuer.popTop();
                }
                break;
            case INT:
                if(INT.readOPIsOpen()){
                    queuedInstruction.readOpCycle = cycle;
                    currentRegister.addRegister(queuedInstruction.firstR);
                    INT.readOp=queuedInstruction;
                    issuer.popTop();
                }
                break;
            case DIV:
                if(DIV.readOPIsOpen()){
                    queuedInstruction.readOpCycle = cycle;
                    currentRegister.addRegister(queuedInstruction.firstR);
                    DIV.readOp=queuedInstruction;
                    issuer.popTop();
                }
                break;
        }
    }

    /** This method takes the instruction with the lowest order and puts it into the writeback Array
     *
     */
    private void writeBackInOrderOneClock(){
        int currentOrder;
        if(writeback.size()==0) currentOrder=0;
        else currentOrder = writeback.get(writeback.size()-1).getOrder();

        if(FL.WARC.getOrder()-1==currentOrder){
            FL.WARC.writeCycle = cycle;
            writeback.add(FL.WARC);
            storeRegister.addRegister(FL.WARC.firstR);
            currentRegister.removeRegister(FL.WARC.firstR);
            FL.WARC=new Instruction();
        }else if(M.WARC.getOrder()-1==currentOrder){
            M.WARC.writeCycle = cycle;
            writeback.add(M.WARC);
            storeRegister.addRegister(M.WARC.firstR);
            currentRegister.removeRegister(M.WARC.firstR);
            M.WARC=new Instruction();
        } else if(DIV.WARC.getOrder()-1==currentOrder){
            DIV.WARC.writeCycle = cycle;
            writeback.add(DIV.WARC);
            storeRegister.addRegister(DIV.WARC.firstR);
            currentRegister.removeRegister(DIV.WARC.firstR);
            DIV.WARC=new Instruction();
        }else if(INT.WARC.getOrder()-1==currentOrder) {
            INT.WARC.writeCycle = cycle;
            writeback.add(INT.WARC);
            storeRegister.addRegister(INT.WARC.firstR);
            currentRegister.removeRegister(INT.WARC.firstR);
            INT.WARC = new Instruction();
        }
    }

    //PUBLIC METHODS

    /** Runs Scoreboard for one clock cycle
     *
     */
    public void oneClock(){
        //First writeback
        writeBackInOrderOneClock();

        //Then Units
        FL.oneClock(currentRegister,storeRegister);
        INT.oneClock(currentRegister,storeRegister);
        M.oneClock(currentRegister,storeRegister);
        DIV.oneClock(currentRegister,storeRegister);

        //Then issuer
        oneClockIssuer();
        cycle++;

    }

    /** Prints the scoreboard
     *
     * @return String
     */
    public String toString(){
        String scoreboardString = "\nCycle: "+cycle+"\n INT "+INT.toString()+"\n FL "+FL.toString()+"\n MULT "+M.toString()+"\n DIV"+DIV.toString();
        return scoreboardString;
    }

    /** Prints all instructions in the queue
     *
     * @return String
     */
    public String queueToString(){
        return issuer.toString();
    }

    /** Get the current cycle
     *
     * @return int
     */
    public int getCycle(){
        return cycle;
    }

    /** Print array of the current and store reigster
     *
     */
    public void printRegisters(){
        System.out.println("currentRegister: "+currentRegister.registers.toString());
        System.out.println("storeRegister: "+storeRegister.registers.toString());
    }

    //GETTERS
    public Issuer getIssuer() {
        return issuer;
    }

    public ArrayList getCurrentRegister() {
        return currentRegister.registers;
    }

    public ArrayList getStoreRegister() {
        return storeRegister.registers;
    }

    public Unit getDIV() {
        return DIV;
    }

    public Unit getINT(){
        return INT;
    }

    public Unit getFL() {
        return FL;
    }

    public Unit getM() {
        return M;
    }

    public ArrayList<Instruction> getWriteback() {
        return writeback;
    }

    //TESTER METHOD
    public static void main(String[] args){
        Instruction[] instructions = {new Instruction("LD","R6","36","0",1),
                new Instruction("LD","R2","45","0",2),
                new Instruction("LD","R4","45","0",3),
                new Instruction("MULT","R0","R2","R4",4),
                new Instruction("SUB","R8","R6","R2",5),
                new Instruction("DIV","R10","R0","R6",6),
                new Instruction("ADD","R6","R8","R2",7)};


        Scoreboard scoreboard = new Scoreboard(instructions,true);

        while(scoreboard.writeback.size()!=instructions.length){
            System.out.println(scoreboard.queueToString());
            scoreboard.printRegisters();
            System.out.println(scoreboard.toString());
            scoreboard.oneClock();
        }
        System.out.println(scoreboard.queueToString());
        scoreboard.printRegisters();
        System.out.println(scoreboard.toString());
        scoreboard.oneClock();
    }




}
