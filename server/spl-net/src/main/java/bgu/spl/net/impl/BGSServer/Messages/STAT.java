package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.Serializable;
import java.util.List;

public class STAT implements Command<User> {

    private short Opcode = 8;
    private String usernameList;

    public STAT(String usernamelist){
        usernameList = usernamelist;
    }

    @Override
    public Serializable execute(User user) {  //Yoni might change it to return 1 ACK of all the users togther.
        if(!user.isLogged())         //If user isn't logged in.
            return new EROR(Opcode);

        User currentUserName;
        ConnectionHandler<Command> ch = ConnectionImpl.getInstance().getConnectionMap().get(user.getConnectionId());
        int indexOf = usernameList.indexOf("|");
        while (indexOf != -1){
            currentUserName = DataBase.getInstance().getUser(usernameList.substring(0, indexOf));
            if(!currentUserName.getBlockedList().contains(user)) {  //Checks if the user is not on the asked for logstat user block list.
                ch.send(new ACK(Opcode, currentUserName.getLogStat()));
            }
            else {
                ch.send(new ACK(Opcode, "BLOCKED by that user"));
            }
            usernameList = usernameList.substring(indexOf + 1);
            indexOf = usernameList.indexOf("|");
        }
        currentUserName = DataBase.getInstance().getUser(usernameList);    //taking care of the last username in the list.
        if(!currentUserName.getBlockedList().contains(user)) {
            return new ACK(Opcode, currentUserName.getLogStat());
        }
        else{
            return new ACK(Opcode, "You are BLOCKED by that user");
        }

    }
}
