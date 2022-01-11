# SPLA3
Server and client that are simulate a social network. 
For client: Open terminal in the client bin folder, and write ./echoExample <IP> <PORT>.

For server: Open terminal in server/spl-net dir:
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain"
	-Dexec.args="<port><Num of threads>" for Reactor server.
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" 
	-Dexec.args="<port>" for TPC server.

Forbiden words can be change in ConnectionImpl.class in the BGSServer.

Each command(message) should be written in capital latters and the birthday should be saparated by '.'.
