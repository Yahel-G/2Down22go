package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.bidi.BidiMessageEncoderDecoder;
import bgu.spl.net.srv.bidi.DataBase;

/**
 * Created by Yahel on 03/01/2019.
 */
public class TPCMain {

    public static void main (String [] args) {
        DataBase DB17 = DataBase.getInstance();
        Server.threadPerClient(
                /*Integer.parseInt(args[0])*/7777, //port
                () -> new BidiMessagingProtocolImpl(DB17), //protocol factory
                BidiMessageEncoderDecoder::new //message encoder decoder factory
        ).serve();
    }
}
