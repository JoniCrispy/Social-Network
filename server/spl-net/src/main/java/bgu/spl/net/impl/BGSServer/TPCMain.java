package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.srv.Server;
import bgu.spl.net.impl.*;
public class TPCMain {
    public static void main (String[] args) {
        System.out.println("TPCMain");
        int port = -1;
        if (args.length == 0) {
            System.out.println("please specify port");
        } else {
            port = Integer.parseInt(args[0]);
        }
        Server.threadPerClient(port, () -> new BGSprotocol(),
                () -> new BGSEncoderDecoder()).serve();
    }

}

