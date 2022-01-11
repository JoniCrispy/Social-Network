package bgu.spl.net.impl.BGSServer.Messages;
import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.Serializable;

public class Login implements Command {
    private String Username;
    private String Password;
    private Byte Capcha;
    private short Opcode = 2;
    private User loggedUser;

    public Login(String username, String password, Byte capcha){
        Username = username;
        Password = password;
        Capcha = capcha;
    }


    @Override
    public Serializable execute(Object arg) {               //****Dont forget to handle the notification list****
        if( Username != null & Password != null){ //the client is not already logged in    (We delete are == null)
            User user = DataBase.getInstance().getUser(Username);
            if(user != null) {
                if(user.login(Password)){   //login its a user function that checks if the password correct.
                    loggedUser = user;
                    user.setLogged(true);             //change isLogged field in user.
                    ConnectionHandler<Command> handler = ConnectionImpl.getInstance().getConnectionMap().get(user.getConnectionId());
                    while (!user.getNotifications().isEmpty()){
                        handler.send(user.getNotifications().poll());
                    }
                    return new ACK((short)2);

                }
            }
        }
        return new EROR((short)2);
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}

