package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.Serializable;
import java.sql.Connection;

public class NOTIFICATION implements Command<User> {

    private String postingUser;
    private String Content;
    private Byte notificationType;
    private short Opcode = 9;

    public NOTIFICATION(String postinguser, String content, Byte notificationtype){
        postingUser = postinguser;
        Content = content;
        notificationType = notificationtype;
    }

    @Override
    public Serializable execute(User user) {
        if(!user.isLogged()){
            user.getNotifications().add(this);
        }
        else{
            int connectionId  = user.getConnectionId();
            ConnectionHandler connectionHandler = ConnectionImpl.getInstance().getConnectionMap().get(connectionId);
            connectionHandler.send(this);
        }
    return new ACK((short) 9);
    }
    public String toString(){
        return "NOTIFICATION Post " + postingUser +" "+ Content;
    }
}
