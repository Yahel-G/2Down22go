package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;

/**
 * Created by Yahel on 23/12/2018.
 */
public class BidiMessageEncoderDecoder<Message> implements MessageEncoderDecoder<Message> {

    private bgu.spl.net.srv.bidi.Message message = null;
    private bgu.spl.net.srv.bidi.Message result = null;
    private final ByteBuffer opCode = ByteBuffer.allocate(2);

    // --------------------- DECODER --------------------- //

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (message == null){   // not done decoding the opcode
            opCode.put(nextByte);
            if(!opCode.hasRemaining()){ // we'll pass here once opCode is full - once we've finsihed loading both bytes
                byte[] toShort = opCode.array();
                short OpcodeNumber = (short)((short)((toShort[0] & 0xff) << 8) + (short)(toShort[1] & 0xff)); // test in debug if can do without the inner castings
                message = bgu.spl.net.srv.bidi.Messages.MessageFactory.create(OpcodeType.values()[OpcodeNumber]);
                opCode.clear();
                if (OpcodeNumber == 3 || OpcodeNumber == 7){
                    result = message;
                    message = null;
                }
            }
        }else{
            message.decodeByte(nextByte);
            result = message;
        }
        return result;
    }



    // encode function should be called by the protocol in the process() function right before calling send() function
    @Override
    public byte[] encode(Message message) {

        //todo implement
    return new byte[17];
    }

}
