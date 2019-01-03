package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.srv.bidi.Message;


/**
 * Created by Yahel on 24/12/2018.
 */
public class MessageFactory {
    public MessageFactory(){

    }

    public static Message create(String type){
        if (type == "REGISTER") {
            return new Register();
        }else if (type == "LOGIN"){
            return new Login();
        }else if (type == "LOGOUT"){
            return new Logout();
        }else if (type == "FOLLOW"){
            return new Follow();
        }else if (type == "POST"){
            return new Post();
        }else if (type == "PM"){
            return new PM();
        }else if (type == "USERLIST"){
            return new Userlist();
        }else if (type == "STAT"){
            return new Stat();
        }else if (type == "NOTIFICATION"){
            return new Notification();
        }else if (type == "ACK"){
            return new Ack();
        }else if (type == "ERROR"){
            return new Error();
        }else return null; // some error
    }
}

