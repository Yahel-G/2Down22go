package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Notification extends Message {
    private int publicMessage = -1; // 1 indicates public message, 0 indicates PM
    private String postingUser = "";
    private String content;
    private Vector<Byte> temp;


    // for post messages
    private boolean publicPost;


    public Notification(){
        super();
        temp = new Vector<>();
        opcodeType = OpcodeType.NOTIFICATION;

    }

    //for bidimessagingprotocolimpl post message / PM
    public Notification(boolean publicPost,String postingUser, String content){
        super();
        opcodeType = OpcodeType.NOTIFICATION;
        this.publicPost = publicPost;
        this.postingUser = postingUser;
        this.content = content;
    }



    public void process(int connectionId, Connections<Message> connections){

    }

    @Override
    public void decodeByte(Byte nextByte) { // todo: delete all this maybe? because we won't receive from client
        if (publicMessage == 1 || publicMessage == 0){
            if (postingUser == ""){
                if (nextByte == 0){
                    postingUser = new String(toByteArray(temp));
                    temp.clear();
                }else{
                    temp.add(nextByte);
                }
            }else{
                if(nextByte == 0){
                    content = new String(toByteArray(temp));
                    temp.clear();
                    doneDecoding = true;
                }else{
                    temp.add(nextByte);
                }
            }
        }
        if (publicMessage == -1){
            publicMessage = nextByte;
        }
    }

    public String getContent() {
        return content;
    }

    public boolean isPublicPost() {
        return publicPost;
    }

    public String getPostingUser() {
        return postingUser;
    }
}
