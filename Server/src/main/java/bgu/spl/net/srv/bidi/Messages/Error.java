package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Error extends Message {
    private Byte tempByte;
    private OpcodeType opCode;

    public Error(){
        super();
        tempByte = -1;
        opCode = OpcodeType.NULL; // for debugging
        opcodeType = OpcodeType.ERROR;

    }

    public Error(OpcodeType code){ // for BidiMessagingProtocolImpl
        super();
        tempByte = -1;
        opcodeType = OpcodeType.ERROR;

        opCode = code;

    }

    public void process(int connectionId, Connections<Message> connections){
    }

    @Override
    public void decodeByte(Byte nextByte) {
        if (tempByte == -1){
            tempByte = nextByte;
        }else{
            opCode = OpcodeType.values ()[((short)((tempByte & 0xff) << 8) + (short)(nextByte & 0xff))];
            tempByte = -1;
            doneDecoding = true;
        }
    }



}
