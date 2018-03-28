package Scoreboard;

import javax.swing.plaf.synth.SynthMenuBarUI;

public class Unit {

    Instruction[] pipe;
    Instruction readOp;
    Instruction WARC;
    private boolean isPipelined;

    Unit(int pipeSize, boolean isPipelined){
        pipe = new Instruction[pipeSize];
        for(int i=0;i<pipeSize;i++){
            pipe[i] = new Instruction();
        }
        this.isPipelined = isPipelined;
        readOp = new Instruction();
        WARC = new Instruction();
    }

    Unit(Instruction[] pipe, boolean isPipelined){
        this.pipe=pipe;
        this.isPipelined=isPipelined;
    }

    //Obselete Method
    public void oneClock(){
        //Put Instruction in WARC if possible
        if(WARCIsOpen()){
            WARC = pipe[pipe.length-1];
            pipe[pipe.length-1]=new Instruction();
        }

        //Execute units
        for(int i=pipe.length-1;i>0;i--){
            if(pipe[i].getOrder()==-1){
                pipe[i]=pipe[i-1];
                pipe[i-1]=new Instruction();
            }
        }

        //Put item from readOP to pipe[0] if possible
        if(pipe[0].getOrder()==-1&&readOp.getOrder()!=-1){
            pipe[0]=readOp;
            readOp=new Instruction();
        }
    }


    public void oneClock(CurrentRegister cReg, Register sReg){
        //Put Instruction in WARC if possible
        if(WARCIsOpen()){
            WARC = pipe[pipe.length-1];
            pipe[pipe.length-1]=new Instruction();
        }
        //Execute units
        for(int i=pipe.length-1;i>0;i--){
            if(pipe[i].getOrder()==-1){
                pipe[i]=pipe[i-1];
                pipe[i-1]=new Instruction();
            }
        }

        //Put item from readOP to pipe[0] if possible
        if(pipe[0].getOrder()==-1&&readOp.getOrder()!=-1&&cReg.instructionCanBeExecuted(readOp)&&sReg.instructionCanBeExecuted(readOp)){
            pipe[0]=readOp;
            readOp=new Instruction();
        }

    }

    public String toString(){
        String blockString = "|"+readOp.toString()+"|---|";
        for(Instruction instruction: pipe){
            if(instruction==null) blockString+=" ";
            else blockString+=instruction.toString()+"|";
        }

        blockString+="---"+WARC.toString();

        return blockString;
    }

    public boolean WARCIsOpen(){
        boolean warcboolean = WARC.getInstructionType()==null||WARC.getOrder()==-1;
        return warcboolean;
    }

    public boolean readOPIsOpen(){
        boolean readOPboolean = readOp.getInstructionType()==null||readOp.getOrder()==-1;
        return readOPboolean;
    }
}
