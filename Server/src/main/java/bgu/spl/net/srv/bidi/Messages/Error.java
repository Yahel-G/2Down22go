package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Error extends Message {
    private Byte tempByte;
    private int opCode;

    public Error(){
        super();
        tempByte = -1;
        opCode = -1; // for debugging
    }
    public void process(int connectionId, Connections<Message> connections){
    }

    @Override
    public void decodeByte(Byte nextByte) {
        if (tempByte == -1){
            tempByte = nextByte;
        }else{
            opCode = (short)((short)((tempByte & 0xff) << 8) + (short)(nextByte & 0xff)); // todo test in debug if can do without the inner castings
            tempByte = -1;
            doneDecoding = true;
        }
    }


}
