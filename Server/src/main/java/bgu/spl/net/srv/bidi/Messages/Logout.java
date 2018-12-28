package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Logout extends Message {
    public Logout(){
        super();
        doneDecoding = true;
        opcodeType = OpcodeType.LOGOUT;

    }
    public void process(int connectionId, Connections<Message> connections){

    }

    @Override
    public void decodeByte(Byte nextByte) {
        // no data to decode
    }
}
