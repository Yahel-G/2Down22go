package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.bidi.Connections;

/**
 * Created by Yahel on 23/12/2018.
 */
public abstract class Message {
    private String[] theMsg;

    public Message(){

    }

    public String[] getTheMsg() {
        return theMsg;
    }

    public abstract void process(int connectionId, Connections<Message> connections);

    public abstract void decodeByte(byte nextByte);


    protected String bytesToString() {
/*        byte[] bytes = new byte[_byteVector.size()];
        int i = 0;
        for (Byte currByte : _byteVector) {
            bytes[i] = currByte.byteValue();
            i++;
        }
        _byteVector.clear();
        String str = null;
        try {
            str = new String(bytes, 0, bytes.length , ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;*/
    }
}
