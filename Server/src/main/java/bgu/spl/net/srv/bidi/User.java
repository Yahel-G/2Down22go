package bgu.spl.net.srv.bidi;

import bgu.spl.net.srv.bidi.Messages.Notification;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Yahel on 28/12/2018.
 */
public class User {
    private String username;
    private String password;
    private boolean loggedIn;
    private Vector<String> followingNameList;
    private Vector<String> followersNameList;
    private ConcurrentLinkedQueue<Notification> pendingMessagesQueue;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        followingNameList = new Vector<>();
        followersNameList = new Vector<>();
        pendingMessagesQueue = new ConcurrentLinkedQueue<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void logIn() {
        loggedIn = true;
    }

    public void logOut(){
        loggedIn = false;
        // todo: let the server(baseServer?) know it needs to DC the client. something with shouldTerminate
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Vector<String> getFollowingNameList() {
        return followingNameList;
    }

    public Vector<String> getFollowersNameList() {
        return followersNameList;
    }

    public ConcurrentLinkedQueue<Notification> getPendingMessagesQueue() {
        return pendingMessagesQueue;
    }
}
