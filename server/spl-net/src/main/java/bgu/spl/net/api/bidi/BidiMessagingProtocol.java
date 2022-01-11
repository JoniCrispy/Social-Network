package bgu.spl.net.api.bidi;

public interface BidiMessagingProtocol<T>  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId);     //deleted "Connections connections" from fields because I think we dont need it here.
    
    void process(T message);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
