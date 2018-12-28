package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Ack extends Message {
    private short opCode = -1;
    private Byte tempByte;

    //for follow or userlist messages ack:
    private Byte[] numOfUsers;
    private int usersAmount;
    private int numRead = 0;
    private Vector<String> userNameList;
    private Vector<Byte> tempUserVector;

    //for STAT message ack:
    private int numPosts = -1;
    private int numFollowers = -1;
    private  int numFollowing = -1;


    public Ack(){
        super();

        //for follow or userlist message ack:
        numOfUsers = new Byte[2];
        userNameList = new Vector<>();
        tempUserVector = new Vector<>();
        usersAmount = -1;
    }
    public void process(int connectionId, Connections<Message> connections){
    }

    @Override
    public void decodeByte(Byte nextByte) {
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
            if (numPosts == -1){
                if (tempByte == -1){
                    tempByte = nextByte;
                }else{
                    numPosts = (short)((short)((tempByte & 0xff) << 8) + (short)(nextByte & 0xff)); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                }
            }else if (numFollowers == -1) {
                if (tempByte == -1) {
                    tempByte = nextByte;
                } else {
                    numFollowers = (short) ((short) ((tempByte & 0xff) << 8) + (short) (nextByte & 0xff)); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                }
            }else if (numFollowing == -1) {
                if (tempByte == -1) {
                    tempByte = nextByte;
                } else {
                    numFollowing = (short) ((short) ((tempByte & 0xff) << 8) + (short) (nextByte & 0xff)); // todo test in debug if can do without the inner castings
                    tempByte = -1;
                    doneDecoding = true;
                }
            }

        }
    }


}
