package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;


/**
 * Created by Yahel on 24/12/2018.
 */
public class MessageFactory {
    public MessageFactory()

    public static Message create(OpcodeType type){
        if (type == OpcodeType.REGISTER){
            return new Register()

        }
    }
}
