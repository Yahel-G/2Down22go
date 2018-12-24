package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.bidi.Connections;

/**
 * Created by Yahel on 23/12/2018.
 */
public abstract class Message {
    private String[] theMsg;

    public Message(String[] theMsg){
        this.theMsg = theMsg;
    }

    public String[] getTheMsg() {
        return theMsg;
    }

    public abstract void process(int connectionId, Connections<Message> connections);
}
