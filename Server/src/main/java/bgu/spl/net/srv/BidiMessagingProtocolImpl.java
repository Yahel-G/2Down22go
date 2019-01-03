package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.DataBase;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.Messages.*;
import bgu.spl.net.srv.bidi.Messages.Error;
import bgu.spl.net.srv.bidi.OpcodeType;
import bgu.spl.net.srv.bidi.User;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yahel on 23/12/2018.
 */
public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate;
    private int connectionId;
    private static ConnectionsImpl connections; // static so all threads can access it
    private DataBase DB;
    private User theUser = null;

    public BidiMessagingProtocolImpl(DataBase DB){
        this.DB = DB;
    }

    public void start(int connectionId, Connections<Message> connections){
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl)connections; // todo is this casting legit?
        shouldTerminate = false;
        DB = DataBase.getInstance();

    }


    @SuppressWarnings("unchecked") // todo remove maybe
    public void process(Message message){
        OpcodeType type = message.getOpcodeType();
        if(type == OpcodeType.LOGOUT || type == OpcodeType.FOLLOW || type == OpcodeType.POST ||
                type == OpcodeType.PM || type == OpcodeType.USERLIST || type == OpcodeType.STAT){
            if(theUser == null || !theUser.isLoggedIn()){
                connections.send(connectionId, new Error(message.getOpcodeType()));
            }
        }
        if (type == OpcodeType.REGISTER){
            String username = ((Register)message).getUsername();
            if (DB.getUserMap().containsKey(username)){
                connections.send(connectionId, new Error(type));
            }else{
                DB.getUserMap().put(username,new User(username, ((Register)message).getPassword()));
                connections.send(connectionId, new Ack(1));
            }

        }else if (type == OpcodeType.LOGIN){
            String username =((Login)message).getUsername();
            if(!DB.getUserMap().containsKey(username)|| !(DB.getUserMap().get(username).getPassword().equals(((Login)message).getPassword())) || DB.getUserMap().get(username).isLoggedIn()){
                connections.send(connectionId, new Error(type));
            }else{ // all ok todo: deal with pending messages!!!
                theUser = DB.getUserMap().get(username);
                theUser.logIn();
                DB.getOnlineUsersMap().put(username, connectionId);
                connections.send(connectionId, new Ack(2)); // todo: refactor these to "getOpcode" func from Message
                for(Notification notific: theUser.getPendingMessagesQueue()){// taking care of pending messages
                    connections.send(connectionId, notific);
                    theUser.getPendingMessagesQueue().remove(notific);
                }
            }

        }else if (type == OpcodeType.LOGOUT){
            if (theUser != null && theUser.isLoggedIn()){
                theUser.logOut();
                DB.getOnlineUsersMap().remove(theUser.getUsername(), connectionId);
                theUser = null;
            }else{
                connections.send(connectionId, new Error(type));
            }

        }else if (type == OpcodeType.FOLLOW){
            Vector<String> successfullyProcessedUsers = new Vector<>();
            Follow followMessage = (Follow)message;
            int numSuccessfullyProcessed = 0;
            if (followMessage.getUnfollow() == 0){
                for (String username: followMessage.getUserNameList()){
                    if(theUser.getFollowingNameList().add(username)){
                        DB.getUserMap().get(username).getFollowersNameList().add(theUser.getUsername()); // add this user to other user's followers list
                        successfullyProcessedUsers.add(username);
                        numSuccessfullyProcessed++;
                    }
                }

            }else{ // unfollow message
                for (String username: followMessage.getUserNameList()){
                    if(theUser.getFollowingNameList().remove(username)){
                        DB.getUserMap().get(username).getFollowersNameList().remove(theUser.getUsername()); // remove this user to other user's followers list
                        successfullyProcessedUsers.add(username);
                        numSuccessfullyProcessed++;
                    }
                }
            }
            if (numSuccessfullyProcessed == 0){
                connections.send(connectionId, new Error(type));
            }else{
                connections.send(connectionId, new Ack(4, numSuccessfullyProcessed, successfullyProcessedUsers));
            }
        }else if (type == OpcodeType.POST){

                Post postMessage = (Post)message;
                DB.getUserSentPostsMap().get(theUser.getUsername()).add(postMessage);
                Vector<String> taggedUsernames = extractUsernames(postMessage.getContent());
                Vector<String> usersToSendTo = theUser.getFollowersNameList();
                for (String taggedUser: taggedUsernames){   // making sure we won't send 2 notifications
                    if (!usersToSendTo.contains(taggedUser)){
                        usersToSendTo.add(taggedUser);
                    }
                }
                for (String user: usersToSendTo){
                    Integer userConnectionId = DB.getOnlineUsersMap().get(user);
                    Notification notific = new Notification(true, theUser.getUsername(), postMessage.getContent());
                    if(/*userConnectionId != null*/DB.getUserMap().get(user).isLoggedIn()) {
                        if(!connections.send(userConnectionId, notific)){
                            DB.getUserMap().get(user).getPendingMessagesQueue().add(notific);
                        }
                    }else{ // the user is offline
                        DB.getUserMap().get(user).getPendingMessagesQueue().add(notific);
                    }
                }
        }else if (type == OpcodeType.PM){
            PM PMMs = (PM)message;
            String targetUser = PMMs.getUsername();
            if(!DB.getUserMap().containsKey(targetUser)){
                connections.send(connectionId, new Error(type));
            }else{
                Integer userConnectionId = DB.getOnlineUsersMap().get(targetUser);
                Notification notific = new Notification(false, PMMs.getUsername(), PMMs.getContent());
                if(/*userConnectionId != null*/DB.getUserMap().get(targetUser).isLoggedIn()) {
                    if(!connections.send(DB.getOnlineUsersMap().get(targetUser), notific)){
                        DB.getUserMap().get(targetUser).getPendingMessagesQueue().add(notific);
                    }
                }else{
                    DB.getUserMap().get(targetUser).getPendingMessagesQueue().add(notific);
                }
                    DB.getUserSentPMsMap().get(PMMs.getUsername()).add(PMMs);
            }

        }else if (type == OpcodeType.USERLIST){
            Vector<String> userNameList = new Vector<>();
            int numOfUsers = 0;
            for(String username: DB.getUserMap().keySet()){
                userNameList.add(username);
                numOfUsers++;
            }
            connections.send(connectionId, new Ack(7, numOfUsers, userNameList)); // todo refactor this to send an OpcodeType

        }else if (type == OpcodeType.STAT){
            String username = ((Stat)message).getUsername();
            if (!DB.getUserMap().containsKey(username)){
                connections.send(connectionId, new Error(message.getOpcodeType()));
            }else{
                connections.send(connectionId, new Ack(8/*todo refactor me*/, DB.getUserSentPostsMap().get(username).size(), //NumPosts
                        DB.getUserMap().get(username).getFollowersNameList().size(), // NumFollowers
                        DB.getUserMap().get(username).getFollowingNameList().size())); //NumFollowing
            }
        }
    }

    private Vector<String> extractUsernames(String str){
        Vector<String> ret = new Vector<>();
        Pattern pattern = Pattern.compile("@\\w+");

        Matcher matcher = pattern.matcher(str);
        while (matcher.find())
        {
            ret.add(matcher.group());
        }
        return ret;
    }
    public boolean shouldTerminate(){
        return shouldTerminate;
    }
}
