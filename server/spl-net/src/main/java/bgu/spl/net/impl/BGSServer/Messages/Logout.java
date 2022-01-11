package bgu.spl.net.impl.BGSServer.Messages;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class Logout implements Command<User> {


    private short Opcode = 3;

    public Logout(){};

    @Override
    public Serializable execute(User user) {
        if(user != null){
            if(user.logout())
                return new ACK((short)3);
        }
        return new EROR((short)3);
    }
    }



