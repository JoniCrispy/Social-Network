package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class PM implements Command<User> {
    private String Username;
    private String Content;
    private String date;
    private short Opcode = 6;

    public PM(String username, String content,String date){
        this.date = date;
        Username = username;
        Content = content;
    }

    @Override
    public Serializable execute(User user) {
        User userToSend = DataBase.getInstance().getUser(Username);
        if (!user.isLogged() || !user.getFollowers().contains(userToSend))
            return new EROR(Opcode);


        Byte Type = 0;
        NOTIFICATION sendCommand = new NOTIFICATION(user.getUsername(), Content,  Type);
        sendCommand.execute(userToSend);
        return new ACK(Opcode);
    }
}
