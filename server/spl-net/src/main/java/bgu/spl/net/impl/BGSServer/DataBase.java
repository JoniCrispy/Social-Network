package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.*;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

    private ConcurrentHashMap<String, User> usersMap;
//   private ConcurrentHashMap<Integer , ConnectionHandler<Command>> connectionMap;  *Yoni moved it to connections*

    public DataBase(){
        usersMap = new ConcurrentHashMap<String,User>();
//        connectionMap = new ConcurrentHashMap<Integer,ConnectionHandler<Command>>(); *Yoni moved it to connections*
    }

    private static class DatabaseHolder{
        private static volatile DataBase instance = new DataBase();
    }

    public static DataBase getInstance() {
        return DataBase.DatabaseHolder.instance;}

    public ConcurrentHashMap<String, User> getUsersMap() {
        return usersMap;
    }

    public User getUser(String username){
        return usersMap.get(username);
    }

    public boolean register(String username, String password, String birthday){
        if(username == null || password == null || username == "" || password == "" || usersMap.get(username)!=null)
            return false;
        usersMap.put(username, new User(username, password,birthday));
        return true;
    }


}
