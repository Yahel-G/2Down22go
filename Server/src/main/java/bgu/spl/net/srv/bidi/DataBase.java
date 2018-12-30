package bgu.spl.net.srv.bidi;

import bgu.spl.net.srv.bidi.Messages.*;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Yahel on 28/12/2018.
 */
public class DataBase {

    private HashMap<String, User> userMap;
    private HashMap<String, Vector<PM>> userSentPMsMap;
    private HashMap<String, Vector<Post>> userSentPostsMap;
    private Vector<Post> allPosts;
    private HashMap<String, Integer> onlineUsersMap;



    private static class singletonHolder{
        private static DataBase DB = new DataBase();
    }

    public static DataBase getInstance() {

        return singletonHolder.DB;
    }

    private DataBase(){
        userMap = new HashMap<>();
        userSentPMsMap = new HashMap<>();
        userSentPostsMap = new HashMap<>();
        allPosts = new Vector<>();
        onlineUsersMap = new HashMap<>();
    }

    public HashMap<String, User> getUserMap() {
        return userMap;
    }

    public HashMap<String, Vector<PM>> getUserSentPMsMap() {
        return userSentPMsMap;
    }

    public HashMap<String, Vector<Post>> getUserSentPostsMap() {
        return userSentPostsMap;
    }

    public Vector<Post> getAllPosts() {
        return allPosts;
    }

    public HashMap<String, Integer> getOnlineUsersMap() {
        return onlineUsersMap;
    }
}
