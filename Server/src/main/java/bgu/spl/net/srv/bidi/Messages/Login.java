package bgu.spl.net.srv.bidi.Messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.Message;
import bgu.spl.net.srv.bidi.OpcodeType;

import java.util.Vector;

/**
 * Created by Yahel on 23/12/2018.
 */
public class Login extends Message {

    private String username;
    private String password;
    private Vector<Byte> inputTemp;

    public Login(){
        super();
        inputTemp = new Vector<>();
        opcodeType = OpcodeType.LOGIN;


    }
    public void process(int connectionId, Connections<Message> connections){

    }

    public void decodeByte(Byte nextByte){
        if (username==""){
            if(nextByte == 0){
                username = new String(toByteArray(inputTemp)/*, "UTF-8"*/); // is this UTF-8?
                inputTemp.clear();
            }else{
                inputTemp.add(nextByte);
            }
        }else{
            if(nextByte == 0){
                password = new String(toByteArray(inputTemp)/*, "UTF-8"*/); // is this UTF-8?
                inputTemp.clear();
                doneDecoding = true;
            }else{
                inputTemp.add(nextByte);
            }

        }
    }

}
