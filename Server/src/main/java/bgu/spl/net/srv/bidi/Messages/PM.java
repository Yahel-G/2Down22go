package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class PM extends Message {

    private String content = "";
    private String username = "";
    private Vector<Byte> temp;


    public PM(){
        super();
        opcodeType = OpcodeType.PM;

    }
    public void process(int connectionId, Connections<Message> connections){

    }

    @Override
    public void decodeByte(Byte nextByte) {
        if (username == ""){
            if(nextByte != 0){
                temp.add(nextByte);
            }else{
                username = new String(toByteArray(temp));
                temp.clear();
            }
        }else{
            if(nextByte != 0){
                temp.add(nextByte);
            }else{
                content = new String(toByteArray(temp));
                temp.clear();
                doneDecoding = true;
            }
        }
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }
}
