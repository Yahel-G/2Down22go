package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Register extends Message {
    String username;
    String password;
    boolean doneDecoding = false;
    public Register(){
        super();
    }

    public void decodeByte(Byte nextByte){
        if (username==""){

        }
    }
    public void process(int connectionId, Connections<Message> connections){
    }
}
