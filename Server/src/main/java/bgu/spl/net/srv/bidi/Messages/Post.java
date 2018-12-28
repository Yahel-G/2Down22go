package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Post extends Message {

    private String content;
    private Vector<Byte> tempContentVector;


    public Post(){
        super();
        tempContentVector = new Vector<>();
    }
    public void process(int connectionId, Connections<Message> connections){    }

    @Override
    public void decodeByte(Byte nextByte) {

        if(nextByte != 0){
            tempContentVector.add(nextByte);
        }else{
            content = new String(toByteArray(tempContentVector));
            doneDecoding = true;
        }
    }
}
