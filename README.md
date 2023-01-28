# Social-Network (Client-Server)
Server: Manage and store data.
Client: User Inerface for interacting with the server. 

HowTo:<br>
Client Side:<br>
	Open terminal in the client bin folder insert in terminal line - ./echoExample <IP> <PORT>.

Server side(choose 1 of the 2 designs Reactor\TCP):<br>
	Reactor design:<br>
	Open terminal in server/spl-net dir:<br>
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain"<br>
	- Dexec.args="<port><Num of threads>" for Reactor server.<br>
	TCP design:<br>
	- write mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain"<br> 
	- Dexec.args="<port>" for TPC server.<br>

Notice:<br>
Forbiden words can be change in ConnectionImpl.class in the BGSServer.<br>
Each command(message) should be written in capital latters and the birthday should be saparated by '.'.
