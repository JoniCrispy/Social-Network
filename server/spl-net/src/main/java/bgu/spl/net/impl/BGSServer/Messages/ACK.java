package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.BGSServer.User;

import java.io.Serializable;

public class ACK implements Command<User> {
    private String optional;
    private short messageOpcode;
    private short Opcode = 10;

    public ACK(short opcode){
        this(opcode, null);
    }

    public ACK(short opcode, String info){
        messageOpcode = opcode;
        optional = info;
    }

    public short getOpcode() {
        return  messageOpcode;
    }

    public String getInfo(){
        return optional;
    }

    @Override
    public Serializable execute(User arg) {
        return null;
    }
    public String toString(){
        if (optional != null)
            return "ACK " + String.valueOf(messageOpcode) +" " + optional;
        else
            return "ACK " + String.valueOf(messageOpcode);
    }
}
