package bgu.spl.net.impl.BGSServer.Messages;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;
import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class Follow implements Command<User> {

    private String Username;
    private Byte Follow;
    private int Opcode = 4;

    public Follow(String username, Byte follow){
        Username = username;    //username to follow
        Follow = follow;
    }

    @Override
    public Serializable execute(User user) {
        User userToFollowOrUnfollow = DataBase.getInstance().getUsersMap().get(Username);
        if (userToFollowOrUnfollow == null || userToFollowOrUnfollow.getBlockedList().contains(user))
            return new EROR((short) 4);
        if(Follow == (byte) 48){              //48 as ascii value of '0'
            if(!userToFollowOrUnfollow.getFollowers().contains(user)){
                userToFollowOrUnfollow.getFollowers().add(user);
                user.followingCounterIncrease();
                return new ACK((short)4, Username);
            }
            else{
                return new EROR((short) 4);
            }

    }else{
            if(userToFollowOrUnfollow.getFollowers().contains(user)){
                userToFollowOrUnfollow.getFollowers().remove(user);
                user.followingCounterDecrease();
                return new ACK((short)4,Username);
            }
            else{
                return new EROR((short)4);
            }
        }
    }
}

