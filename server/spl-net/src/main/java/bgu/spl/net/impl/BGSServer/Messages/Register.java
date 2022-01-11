package bgu.spl.net.impl.BGSServer.Messages;

import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class Register implements Command<User> {

    private String Username;
    private String Password;
    private String birthday;
    private User registerUser;
    private short Opcode = 1;


    public Register(String username, String password, String BD){
        Username = username;
        Password = password;
        birthday = BD;
    }

    @Override
    public Serializable execute(User user) {
        if(user == null) {
            boolean success = DataBase.getInstance().register(Username, Password,birthday);
            registerUser = DataBase.getInstance().getUsersMap().get(Username);
            if (success){
                return new ACK((short) 1);
            }
        }
        return new EROR((short)1);
    }
    public User getRegisterUser() {
        return registerUser;
    }
}

