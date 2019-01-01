package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Ack extends Message {


    private int opCode = -1; // todo: refactor this to be OpcodeType
    private Byte tempByte;

    //for follow or userlist messages ack:
    private Byte[] numOfUsers;
    private int usersAmount;
    private int numRead = 0;
    private Vector<String> userNameList;
    private Vector<Byte> tempUserVector;

    //for STAT message ack:
    private Byte[] numPosts = null;
    private Byte[] numFollowers = null;
    private  Byte[] numFollowing = null;

    //for userlist message ack:
    private OpcodeType secondOpcodeType;

    public Ack(){
        super();
        opcodeType = OpcodeType.ACK;

        //for follow or userlist message ack:
        numOfUsers = new Byte[2];
        userNameList = new Vector<>();
        tempUserVector = new Vector<>();
        usersAmount = -1;
    }

    public Ack(int code){ // for BidiMessageingProtocolImpl
        super();
        opcodeType = OpcodeType.ACK;

        //for follow or userlist message ack:
        numOfUsers = new Byte[2];
        userNameList = new Vector<>();
        tempUserVector = new Vector<>();
        usersAmount = -1;


        opCode = code;
        }

    // for BidiMessageingProtocolImpl (for un/follow messages  / userlist messages)
    public Ack (int code, int numOfUsers, Vector<String> userNameList){
        opCode = code;
        usersAmount = numOfUsers;
        this.userNameList = userNameList;
    }

    // for STAT ack
    public Ack (int code, int numPosts, int numFollowers, int numFollowing){
        opCode = code;
        this.numPosts = shortToByteArray((short)numPosts);
        this.numFollowers = shortToByteArray((short)numFollowers);
        this.numFollowing = shortToByteArray((short)numFollowing);
    }




    public void process(int connectionId, Connections<Message> connections){
    }

    @Override
    public void decodeByte(Byte nextByte) {// todo: delete all this maybe? because we won't receive from client
        if (opCode == -1){
            tempByte = nextByte;
            opCode = -2;
        }else if (opCode == -2){
            opCode = (short)((short)((tempByte & 0xff) << 8) + (short)(nextByte & 0xff)); // todo test in debug if can do without the inner castings
            tempByte = -1;
            if (opCode != 4 && opCode != 7 && opCode != 8){
                doneDecoding = true;
            }
        }else if (opCode == 4 || opCode == 7){
            if (numRead == 2){
                if(nextByte == 0){ // todo still need to check this works
                    userNameList.add(new String(toByteArray(tempUserVector)));
                    tempUserVector.clear();
                    doneDecoding = true;
                }else{
                    tempUserVector.add(nextByte);
                }
            }
            if (numRead == 1){
                numOfUsers[1] = nextByte;
                numRead = 2;
                usersAmount = twoBytesToInt(numOfUsers);
            }
            if (numRead == 0){
                numOfUsers[0] = nextByte;
                numRead = 1;
            }
        }else if (opCode == 8){
            if (numPosts == null){
                if (tempByte == -1){
                    tempByte = nextByte;
                }else{
                    numPosts = shortToByteArray((short)((short)((tempByte & 0xff) << 8) + (short)(nextByte & 0xff))); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                }
            }else if (numFollowers == null) {
                if (tempByte == -1) {
                    tempByte = nextByte;
                } else {
                    numFollowers = shortToByteArray((short) ((short) ((tempByte & 0xff) << 8) + (short) (nextByte & 0xff))); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                }
            }else if (numFollowing == null) {
                if (tempByte == -1) {
                    tempByte = nextByte;
                } else {
                    numFollowing = shortToByteArray((short) ((short) ((tempByte & 0xff) << 8) + (short) (nextByte & 0xff))); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                    doneDecoding = true;
                }
            }

        }
    }

    public OpcodeType getSecondOpcodeType() {
        return secondOpcodeType;
    }

    public Byte[] getNumOfUsers() {
        return numOfUsers;
    }

    public Byte[] getNumFollowers() {
        return numFollowers;
    }

    public Byte[] getNumFollowing() {
        return numFollowing;
    }

    public Vector<String> getUserNameList() {
        return userNameList;
    }

    public Byte[] getNumPosts() {
        return numPosts;
    }
}
