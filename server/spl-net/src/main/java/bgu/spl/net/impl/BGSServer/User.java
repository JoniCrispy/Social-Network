package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Messages.*;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private String Username;
    private String Password;
    private String Birthday;
    private int connectionId;
    private boolean isLogged;
    private int postCounter;
    private LinkedList<User> followers;
    private int followingCounter;                  //Keep on a followers/following > 1
    private LinkedList<User> blockedList;
    private ConcurrentLinkedQueue<NOTIFICATION> notifications;

    public User(String username,String password,String birthday){
        postCounter = 0;
        Username = username;
        Password = password;
        Birthday = birthday;
        isLogged = false;
        followers = new LinkedList<User>();
        blockedList = new LinkedList<User>();
        notifications = new ConcurrentLinkedQueue<NOTIFICATION>();
    }

    public LinkedList<User> getFollowers() {
        return followers;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getUsername() {
        return Username;
    }

    public ConcurrentLinkedQueue<NOTIFICATION> getNotifications() {
        return notifications;
    }

    public String getPassword() {
        return Password;
    }
    public void followingCounterIncrease(){
        followingCounter++;
    }
    public void followingCounterDecrease(){
        followingCounter--;
    }

    public void postCounterIncrease() {
        this.postCounter++;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public synchronized boolean login(String password){
        if(isLogged | !(this.Password.equals(password)))
            return false;
        isLogged = true;
        return true;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getLogStat(){
        Integer ageInteger = 2021 - Integer.valueOf(Birthday.substring(6));
        return ("Age:" + String.valueOf(ageInteger)+ " Number of posts:"+ String.valueOf(postCounter) +" Followers:"+ String.valueOf(followers.size()) + " Following:"+ String.valueOf(followingCounter));
    }

    public boolean logout(){
        if(!isLogged)
            return false;
        isLogged = false;
        ConnectionImpl.getInstance().disconnect(connectionId);
        return true;
    }

    public LinkedList<User> getBlockedList() {
        return blockedList;
    }


}
