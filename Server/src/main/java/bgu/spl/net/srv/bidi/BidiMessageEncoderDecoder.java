package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;

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
                message = bgu.spl.net.srv.bidi.Messages.MessageFactory.create(OpcodeType.values()[OpcodeNumber]);
                opCode.clear();
                if (OpcodeNumber == 3 || OpcodeNumber == 7){

                }
            }
        }

    }

    private int getOpcodeValue() {
        byte[] toShort = opCode.array();
        short value = (short)((short)((toShort[0] & 0xff) << 8) + (short)(toShort[1] & 0xff)); // test in debug if can do without the inner castings
        OpcodeType type = OpcodeType.values()[value];
        switch (opcodeCommand){
            case REGISTER:      { _message = new bidiMessages.RegisterLogin((short)1); return true; }
            case LOGIN:         { _message = new bidiMessages.RegisterLogin((short)2); return true; }
            case LOGOUT:        { _message = new bidiMessages.Logout();                return false;}
            case FOLLOW:        { _message = new bidiMessages.Follow();                return true; }
            case POST:          { _message = new bidiMessages.Post();                  return true; }
            case PM:            { _message = new bidiMessages.PM();                    return true; }
            case USERLIST:      { _message = new bidiMessages.Userlist();              return false;}
            case STAT:          { _message = new bidiMessages.Stat();                  return true; }
            case NOTIFICATION:  { _message = new bidiMessages.Notification();          return true; }
            case ACK:           { _message = new bidiMessages.ACK();                   return true; }
            case ERROR:         { _message = new bidiMessages.Error();                 return true; }
            default:            return false;
        }
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private void cleanAll() {
        _message = null;
    }

    // --------------------- ENCODE SECTION --------------------- //

    @Override
    public byte[] encode(Message res) {
        String cmdAndMsg = res.getString();


        int indexOfSpace = cmdAndMsg.indexOf(" ");
        String cmdString = null;
        if (indexOfSpace != -1) {
            cmdString = cmdAndMsg.substring(0, indexOfSpace);
        }
        else
            cmdString = cmdAndMsg;
        parseCommand(cmdString);

        String msg = cmdAndMsg.substring(cmdString.length());

//        res = new bidiMessages.bidiMessage(_message,msg);

        byte[] ans = _message.encode(msg);
        cleanAll();
        return ans;

    }

    private void parseCommand(String command) {
        switch (command) {
            case "REGISTER":
            {_message = new bidiMessages.RegisterLogin((short)1);       break;}
            case "LOGIN":
            {    _message = new bidiMessages.RegisterLogin((short)2);   break;}
            case "LOGOUT":
            {    _message = new bidiMessages.Logout();                  break;}
            case "FOLLOW":
            {    _message = new bidiMessages.Follow();                  break;}
            case "POST":
            {    _message = new bidiMessages.Post();                    break;}
            case "PM":
            {   _message = new bidiMessages.PM();                       break;}
            case "USERLIST":
            {   _message = new bidiMessages.Userlist();                 break;}
            case "STAT":
            {    _message = new bidiMessages.Stat();                    break;}
            case "NOTIFICATION":
            {   _message = new bidiMessages.Notification();             break;}
            case "ACK":
            {   _message = new bidiMessages.ACK();                      break;}
            case "ERROR":
            {   _message = new bidiMessages.Error();                    break;}
        }
    }

}
