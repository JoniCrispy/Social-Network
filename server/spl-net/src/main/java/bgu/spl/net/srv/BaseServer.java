package bgu.spl.net.srv;

import java.io.IOException;

import bgu.spl.net.impl.BGSServer.BGSEncoderDecoder;
import bgu.spl.net.impl.BGSServer.BGSprotocol;
import bgu.spl.net.impl.BGSServer.ConnectionImpl;
import bgu.spl.net.impl.rci.Command;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private Integer currentClientId;               //For giving every client a unique ID.
    private final Supplier<BGSprotocol> protocolFactory;
    private final Supplier<BGSEncoderDecoder> encdecFactory;
    private ServerSocket sock;


    public BaseServer(
            int port,
            Supplier<BGSprotocol> protocolFactory,
            Supplier<BGSEncoderDecoder> encdecFactory) {
        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
        currentClientId = 0;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) { //just to be able to close
            this.sock = serverSock;
            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();
                BGSprotocol newProtocol = protocolFactory.get();
                BlockingConnectionHandler<Command> handler = new BlockingConnectionHandler<>(
                        clientSock,
                        encdecFactory.get(),
                        newProtocol);
                ConnectionImpl.getInstance().addConnection(currentClientId, handler);
                newProtocol.start(currentClientId);
                currentClientId++;
                execute(handler);
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<Command> handler);

}
