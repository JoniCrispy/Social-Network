# Social-Network (Client-Server)
Server: Manage and store data.
Client: User Inerface for interacting with the server. 

HowTo:
Client Side:
	Open terminal in the client bin folder insert in terminal line - ./echoExample <IP> <PORT>.

Server side(2 Implementations Reactor\TCP):
	
Reactor design:
	Open terminal in server/spl-net dir:
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain"
	-Dexec.args="<port><Num of threads>" for Reactor server.
	TCP design:
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" 
	-Dexec.args="<port>" for TPC server.

Notice:
Forbiden words can be change in ConnectionImpl.class in the BGSServer.
Each command(message) should be written in capital latters and the birthday should be saparated by '.'.
