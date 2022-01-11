package bgu.spl.net.impl.BGSServer.Messages;
import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;
import java.util.LinkedList;

public class Post implements Command<User> {

    private String Content;
    private short Opcode = 5;

    public Post(String content){
        Content = content;
    }
    public LinkedList<User> hashTags (String content){
        LinkedList<User> hashtags = new LinkedList<User>();
        for(int i = 0;i<content.length();i++){
            if(content.charAt(i)=='@'){
                String username = "";
                i++;
                while ((content.charAt(i)!=' ') && (i<content.length())){
                    username += content.charAt(i);
                    i++;
                }
                if(DataBase.getInstance().getUsersMap().get(username) != null) {
                    hashtags.add(DataBase.getInstance().getUsersMap().get(username));
                }
            }
        }
        return hashtags;
    }
    @Override
    public Serializable execute(User user) {
       if(user.isLogged()) {
           user.postCounterIncrease();
           for (User follower : user.getFollowers()){
                ConnectionImpl.getInstance().getConnectionMap().get(follower.getConnectionId()).send(new NOTIFICATION(user.getUsername(), Content, (byte) 1));
           }
           LinkedList<User> hashTags = this.hashTags(Content);
           for(User taggedUser: hashTags){
//               ConnectionImpl.getInstance().getConnectionMap().get(taggedUser.getConnectionId()).send(new NOTIFICATION(user.getUsername(), Content, (byte) 1));
               (new NOTIFICATION(user.getUsername(), Content, (byte) 1)).execute(taggedUser);
           }
       }
       else{
           return  new EROR((short) 5);
       }
       return new ACK((short) 5);
    }
}
