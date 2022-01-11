package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.*;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.ConnectionHandler;

import java.rmi.ConnectException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionImpl implements Connections<Command<User>> {

    //Field of all the active connections.
    private ConcurrentHashMap<Integer , ConnectionHandler<Command>> connectionMap;
    private LinkedList<String> forbiddenWords;
    private String[] forbiddenWordsArr = {"love","hello","what"};


    private ConnectionImpl(){
        connectionMap = new ConcurrentHashMap<Integer,ConnectionHandler<Command>>();
        forbiddenWords = new LinkedList<String>();
        for(String forbiddenWord : forbiddenWordsArr){
          forbiddenWords.add(forbiddenWord);
        }
    }

    private static class ConnectionHolder{
        private static volatile ConnectionImpl instance = new ConnectionImpl();
    }

    public static ConnectionImpl getInstance() {
        return ConnectionImpl.ConnectionHolder.instance;
    }


    @Override
    public boolean send(int connectionId, Command<User> msg) {
        if(connectionMap.contains(connectionId)){
            ConnectionHandler<Command> handler = connectionMap.get(connectionId);
            handler.send(msg);
            return true;
        }
        return false;   //At this moment we will return false if we didnt find the connection depends on the connectionId.
    }
    public boolean addConnection (Integer connectionId, ConnectionHandler<Command> ch){ //return true or false depends on success.
        if (connectionMap.get(connectionId) == null) {
            connectionMap.putIfAbsent(connectionId, ch);
            return true;
        }
        else
            return false;
    }

    @Override
    public void broadcast(Command<User> msg) {
        for (ConnectionHandler<Command> handler : connectionMap.values())
            handler.send(msg);
    }

    @Override
    public void disconnect(int connectionId) {
        connectionMap.remove(connectionId);
    }

    public ConcurrentHashMap<Integer, ConnectionHandler<Command>> getConnectionMap() {
        return connectionMap;
    }

    public String replaceForbiddenWord(String content,String forbiddenWord){
        return content.replaceAll(forbiddenWord,"<filtered>");
    }

    public Command<User> decodeToCommand(char[] commandToDecode){
        commandToDecode = Arrays.copyOf(commandToDecode , commandToDecode.length + 1);
        commandToDecode[commandToDecode.length - 1] = ';';
        String Op =""+ commandToDecode[0];
        Op = Op + commandToDecode[1];
        int OpCode = Integer.valueOf(Op);
        int startIndex = 1;                         //the start of the first word after OP
        int argument = 0;
        switch (OpCode) {                          //The concept here is that in every case the index proceed 1 cell in the array and then start to process the message.
            case 1:
                String userName = "", password = "", birthday = "";
                while (commandToDecode[startIndex] != ';') {
                    startIndex++;
                    String currentArgument = "";
                    while ((commandToDecode[startIndex] != '}') && ((commandToDecode[startIndex] != ';'))) {
                        currentArgument += commandToDecode[startIndex];
                        startIndex++;
                    }
                    switch (argument) {
                        case 0:
                            userName = currentArgument;
                        case 1:
                            password = currentArgument;
                        case 2:
                            birthday = currentArgument;
                    }
                    argument++;
                }
                return new Register(userName, password, birthday);
            case 2:
                String loginUserName = "", loginPassword = "";
                Byte captcha = 0;
                while (commandToDecode[startIndex] != ';') {
                    startIndex++;
                    String currentArgument = "";
                    while ((commandToDecode[startIndex] != '}') && (commandToDecode[startIndex] != ';')) {
                        currentArgument += commandToDecode[startIndex];
                        startIndex++;
                    }
                    switch (argument) {
                        case 0:
                            loginUserName = currentArgument;
                        case 1:
                            loginPassword = currentArgument;
                        case 2:
                            captcha = currentArgument.getBytes()[0];
                    }
                    argument++;
                }
                return new Login(loginUserName, loginPassword, captcha);
            case 3:
                return new Logout();
            case 4:{
                startIndex++;
                Byte followOrUnfollow = 0;
                String followUserName = "";
                followOrUnfollow = Character.toString(commandToDecode[startIndex]).getBytes()[0];
//                while (commandToDecode[startIndex] != ';'){
                startIndex++;
                String currentArgument = "";
                while ((commandToDecode[startIndex] != '}') && (commandToDecode[startIndex] != ';')) {
                    currentArgument += commandToDecode[startIndex];
                    startIndex++;
                }
                followUserName = currentArgument;
//                }
                return new Follow(followUserName, followOrUnfollow);
             }
            case 5: {
                startIndex++;
                String currentArgument = "";
                while ((commandToDecode[startIndex] != '}')&&(commandToDecode[startIndex] != ';')) {
                    currentArgument += commandToDecode[startIndex];
                    startIndex++;
                }
                return new Post(currentArgument);
            }
            case 6: {
                String PMuserName = "", PMcontent = "", PMdate = "";
                while (commandToDecode[startIndex] != ';') {
                    startIndex++;
                    String currentArgument = "";
                    while ((commandToDecode[startIndex] != '}')&&(commandToDecode[startIndex] != ';')) {
                        currentArgument += commandToDecode[startIndex];
                        startIndex++;
                    }
                    switch (argument) {
                        case 0:
                            PMuserName = currentArgument;
                        case 1:
                            PMcontent = currentArgument;
                        case 2:
                            PMdate = currentArgument;
                    }
                    argument++;
                }
                for(int i=0;i<forbiddenWords.size();i++){
                    PMcontent = replaceForbiddenWord(PMcontent,forbiddenWords.get(i));
                }
                return new PM(PMuserName, PMcontent, PMdate);
            }
            case 7:
                return new LOGSTAT();
            case 8: {
                startIndex++;
                String currentArgument = "";
                while ((commandToDecode[startIndex] != '}')&&(commandToDecode[startIndex] != ';')) {
                    currentArgument += commandToDecode[startIndex];
                    startIndex++;
                }
                return new STAT(currentArgument);
            }
            case 9: {
                startIndex++;
                Byte noficationT = 0;
                String postingUser = "", content = "";
                noficationT = Character.toString(commandToDecode[startIndex]).getBytes()[0];
                startIndex++;
                while (commandToDecode[startIndex] != ';') {
                    startIndex++;
                    String currentArgument = "";
                    while ((commandToDecode[startIndex] != '}')&&(commandToDecode[startIndex] != ';')) {
                        currentArgument += commandToDecode[startIndex];
                        startIndex++;
                    }
                    switch (argument){
                        case 0:
                            postingUser = currentArgument;
                        case 1:
                            content = currentArgument;
                    }
                    argument++;
                }
                return new NOTIFICATION(postingUser, content, noficationT);
            }

            case 10:
                //we do not need to create ack here because ack is created immediately.
            case 11:{}
                //same as ack
            case 12: {
                String currentArgument = "";
                startIndex++;
                while (commandToDecode[startIndex] != '}') {
                    currentArgument += commandToDecode[startIndex];
                    startIndex++;
                }
                return new BLOCK(currentArgument);
            }
        }
        return new EROR((short) 0);
    }


}
