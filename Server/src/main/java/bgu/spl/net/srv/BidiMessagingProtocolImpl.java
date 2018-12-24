package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

/**
 * Created by Yahel on 23/12/2018.
 */
public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate;
    private int connectionId;
    private static Connections<Message> connections; // static so all threads can access it

    public void start(int connectionId, Connections<Message> connections){
        this.connectionId = connectionId;
        this.connections = connections;
        shouldTerminate = false;
    }


    public void process(Message message){
        message.process();
    }


    public boolean shouldTerminate(){
        return shouldTerminate;
    }
}
