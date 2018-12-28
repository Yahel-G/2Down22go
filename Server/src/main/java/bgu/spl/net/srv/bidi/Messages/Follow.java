package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Follow extends Message {
    private int unfollow; // 0 indicates this is a follow message, 1 indicates this is an unfollow message, -1 indicates that this bit has not been processed yet.
    private boolean alreadyReadFollowByte;
    private Byte[] numOfUsers;
    private int usersAmount;
    private int numRead = 0;
    private Vector<String> userNameList;
    private Vector<Byte> tempUserVector;



    public Follow( ){
        super();
        unfollow = -1;
        numOfUsers = new Byte[2];
        userNameList = new Vector<>();
        tempUserVector = new Vector<>();
        usersAmount = -1;
 //       alreadyReadFollowByte = false;
    }
    public void process(int connectionId, Connections<Message> connections){

    }

    @Override
    public void decodeByte(Byte nextByte) {
        if (usersAmount > -1 && userNameList.size() == usersAmount){// if they sent the command with an empty list - this will fail - todo make sure they won't do that
            doneDecoding = true;
            return; // assumes nextByte is a zero-byte
        }
        if (unfollow != -1){
            if (numRead == 2){
                if(nextByte == 0){ // todo still need to check this works
                    userNameList.add(new String(toByteArray(tempUserVector)));
                    tempUserVector.clear();
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
        }
        else{
            unfollow = nextByte; // todo check if this works
        }
    }
}
