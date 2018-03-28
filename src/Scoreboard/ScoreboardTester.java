package Scoreboard;

public class ScoreboardTester {


    public static void main(String[] args){
        //INSTRUCTIONS TO USE SCOREBOARD PROGRAM
        //FIRST CREATE A LIST OF INSTRUCTIONS
        //FIRST ALWAYS USE R TO SIGNIFY REGISTER. NEVER F.
        //If you only need 2 registers, put a 0 in the 3rd. Never leave an register string empty.
        Instruction[] instructions = {new Instruction("LD","R6","36","0",1),
                new Instruction("LD","R2","45","0",2),
                new Instruction("LD","R4","45","0",3),
                new Instruction("MULT","R0","R2","R4",4),
                new Instruction("SUB","R8","R6","R2",5),
                new Instruction("DIV","R10","R0","R6",6),
                new Instruction("ADD","R6","R8","R2",7)};

        //Scoreboard
        //Main object for program
        Scoreboard scoreboard = new Scoreboard(instructions,true);

        //To execute 1 clock cylce
        scoreboard.oneClock();
        //Print scoreboard
        //System.out.println(scoreboard.toString());


        //Insturction path
        //Issuer                Unit                 Writeback
        //------   ------------------------------    ---------
        //Queue -> readOp -> pipe[0 to x] -> WARC -> writeback
        // (check registers before pipe)


        //Issuer
        //Contains all instructions that are going to be executed
        //Print all items in queue
        scoreboard.getIssuer().toString();
        //Get ArrayList of all items in queue
        //scoreboard.getIssuer().visibleinstructions;

        // Unit
        // This is where instructions are executed. Each scoreboard has 4 Units
        //A unit as a readOP,executable blocks, and a WARC

        // Example: Access INT Unit
        // Get Readop(Instruction)
        //  scoreboard.getINT().readOp;
        //Get execute pipe(Instruction[])
        //scoreboard.getINT().pipe;
        //Get WARC(Instruction)
        //scoreboard.getINT().WARC;

        //More Unit info
        // |DIV|-- Instruction is in readOP
        // ||DIV|| Instruction is in execution stage
        // --DIV   Instruction is the WARC


        //Writeback
        //This contains all the insturctions that have been written
        //Get writeback(Arraylist)
        //scoreboard.getWriteback();



        //Example run of program
        while(scoreboard.getWriteback().size()!=instructions.length){
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
