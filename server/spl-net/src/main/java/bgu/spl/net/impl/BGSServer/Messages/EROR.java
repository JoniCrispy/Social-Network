package bgu.spl.net.impl.BGSServer.Messages;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class EROR implements Command<User> {

    private short messageOpcode;
    private short Opcode = 11;

    public EROR(short messageopcode){
        messageOpcode = messageopcode;
    }

    public short getOpcode() {
        return messageOpcode;
    }

    @Override
    public Serializable execute(User user) {
        return null;
    }
    public String toString(){
        return "Erorr " + String.valueOf(messageOpcode);
    }
}
