package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class BLOCK implements Command<User> {

    private String Username;
    private short Opcode = 11;

    public BLOCK(String username){
        Username = username;
    }

    @Override
    public Serializable execute(User user) {
        User toBlockUser = DataBase.getInstance().getUser(Username);
        if (user.getBlockedList().contains(toBlockUser) || toBlockUser == null) //Check if already blocked or doesn't exist.
            return new EROR((short) 12);

        user.getBlockedList().add(toBlockUser);
        ConnectionImpl.getInstance().getConnectionMap().get(toBlockUser.getConnectionId()).send(new ACK((short) 12 , "You have been blocked by: " + toBlockUser.getUsername()));
        user.getFollowers().remove(toBlockUser); //Delete each other from their followers list
        toBlockUser.getFollowers().remove(user);
        return new ACK((short) 12);
    }
}
