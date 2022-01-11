package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGSServer.Messages.*;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.ConnectionHandler;


public class BGSprotocol implements BidiMessagingProtocol<Command<User>>{

    private User user;
    private int userConnectionId;
    private boolean shouldTerminate;
    private ConnectionHandler<Command> ch; //Connection handler for sending back the ACK or EROR.

    public BGSprotocol(){
        user = null;   //this field will get the user object when he will log in.
        shouldTerminate = false;
    }

    @Override
    public void start(int connectionId) {   //deleted "Connections connections" from fields because I think we dont need it here.
        userConnectionId = connectionId;
        ch = ConnectionImpl.getInstance().getConnectionMap().get(connectionId);
    }

    @Override
    public void process(Command<User> message){
        Command<User> response = (Command<User>)message.execute(user);
        if(response instanceof ACK) {
            shouldTerminate = message instanceof Logout;
//            if(message instanceof Login) {
//                user = ((Login) message).getLoggedUser();
//            }
            if(message instanceof Register) {
                user = ((Register) message).getRegisterUser();
                user.setConnectionId(userConnectionId);
            }
        }
        ch.send(response);   //Sends the response(ACK or EROR).
    }

    @Override
    public boolean shouldTerminate(){
        return shouldTerminate;
    }


}
