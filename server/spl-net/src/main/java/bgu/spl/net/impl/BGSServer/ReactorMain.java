package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main (String[] args) {
        int port = -1;
        int numOfThreads = 0;
        if (args.length == 0) {
            System.out.println("please specify port");
        } else {
            port = Integer.parseInt(args[0]);
            numOfThreads = Integer.parseInt(args[1]);
        }
        Server.reactor( numOfThreads , port , () -> new BGSprotocol(),
                () -> new BGSEncoderDecoder()   ).serve();    //() -> new BGSEncoderDecoder()
    }
}
