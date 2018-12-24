package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Error extends Message {

    public Error(String[] msg){
        super(msg);
    }
    public void process(int connectionId, Connections<Message> connections){

    }
}
