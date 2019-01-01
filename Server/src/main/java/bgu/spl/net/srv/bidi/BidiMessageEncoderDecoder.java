package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.bidi.Messages.Ack;
import bgu.spl.net.srv.bidi.Messages.Error;
import bgu.spl.net.srv.bidi.Messages.MessageFactory;
import bgu.spl.net.srv.bidi.Messages.Notification;

import java.nio.ByteBuffer;
import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class BidiMessageEncoderDecoder implements MessageEncoderDecoder<Message> {

    private Message message = null;
    private Message result = null;
    private final ByteBuffer opCode = ByteBuffer.allocate(2);

    // --------------------- DECODER --------------------- //

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (message == null){   // not done decoding the opcode
            opCode.put(nextByte);
            if(!opCode.hasRemaining()){ // we'll pass here once opCode is full - once we've finsihed loading both bytes
                byte[] toShort = opCode.array();
                short OpcodeNumber = (short)((short)((toShort[0] & 0xff) << 8) + (short)(toShort[1] & 0xff)); // test in debug if can do without the inner castings
                message = MessageFactory.create(OpcodeType.values()[OpcodeNumber]);
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
        Vector<Byte> temp = new Vector<>();
        OpcodeType type = message.getOpcodeType();
        Byte[] opCodeByte = message.shortToByteArray((short)type.ordinal());
        temp.add(opCodeByte[0]);
        temp.add(opCodeByte[1]);
        switch (type){
            case NOTIFICATION:
                Notification notifiMessage = (Notification)message;
                if (notifiMessage.isPublicPost()){
                    temp.add("1".getBytes()[0]);// todo this legit?
                }else{
                    temp.add("0".getBytes()[0]);// todo this legit?
                }

                byte[] userBytes = notifiMessage.getPostingUser().getBytes();
                for(byte bait: userBytes){
                    temp.add(bait);
                }
                temp.add(("0".getBytes()[0]));
                byte[] contentBytes = notifiMessage.getContent().getBytes();

                for(byte bait: contentBytes){
                    temp.add(bait);
                }
                temp.add(("0".getBytes()[0]));

            case ACK:
                Ack ack = (Ack)message;
                OpcodeType ackType = ack.getOpcodeType();
                Byte[] ackOpCodeByte = message.shortToByteArray((short)ackType.ordinal());
                temp.add(ackOpCodeByte[0]);
                temp.add(ackOpCodeByte[1]);
                switch (ackType){
                    case FOLLOW: case USERLIST:
                        temp.add(ack.getNumOfUsers()[0]);
                        temp.add(ack.getNumOfUsers()[1]);
                        for(String username: ack.getUserNameList()){
                            byte[] usersBytes = username.getBytes();
                            for(byte bait: usersBytes){
                                temp.add(bait);
                            }
                            temp.add("0".getBytes()[0]);
                        }
                        temp.add("0".getBytes()[0]);
                    case STAT:
                        temp.add(ack.getNumPosts()[0]);
                        temp.add(ack.getNumPosts()[1]);
                        temp.add(ack.getNumFollowers()[0]);
                        temp.add(ack.getNumFollowers()[1]);
                        temp.add(ack.getNumFollowing()[0]);
                        temp.add(ack.getNumFollowing()[1]);
                }
            case ERROR:
                Error err = (Error)message;
                Byte[] errOpCodeByte = message.shortToByteArray((short)err.getOpcodeType().ordinal());
                temp.add(errOpCodeByte[0]);
                temp.add(errOpCodeByte[1]);
        }

    return message.toByteArray(temp);
    }


}
