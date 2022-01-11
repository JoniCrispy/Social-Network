package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;
import java.io.Serializable;

public class LOGSTAT implements Command<User> {

    private short Opcode = 7;
    public LOGSTAT(){};

    @Override
    public Serializable execute(User user) {
        return new ACK(Opcode , user.getLogStat());
    }
}
