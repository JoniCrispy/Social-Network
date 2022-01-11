#include <stdlib.h>
#include <connectionHandler.h>
#include <string>
#include <boost/algorithm/string.hpp>
#include <string.h>
#include <iomanip>
#include <cstddef>
#include <thread>
#include <mutex>


/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

std::string getCurrentTime() {
    auto t = std::time(nullptr);
    auto tm = *std::localtime(&t);
    std::ostringstream oss;
    oss << std::put_time(&tm, "%d-%m-%Y %H:%M");
    auto str = oss.str();
    return str;
}


std::string newLineProcess(std::string line) {

    std::vector<std::string> parameters;
    boost::split(parameters,line,boost::is_any_of(" "));
    std::string command = parameters.at(0);
    std::string newLine =  "";
    if(command == "REGISTER"){
        std::string username = parameters.at(1);
        std::string password = parameters.at(2);
        std::string birthday = parameters.at(3);
        newLine =  "01"+username+"}"+password+"}"+birthday;
    }
    else if (command == "LOGIN"){
        std::string username = parameters.at(1);
        std::string password = parameters.at(2);
        newLine =  "02"+username+"}"+password;
    }
    else if (command == "LOGOUT"){
        newLine =  "03";
    }
    else if (command == "FOLLOW"){
        std::string followOrUnfollow = parameters.at(1);
        std::string username = parameters.at(2);
        newLine =  "04"+followOrUnfollow + username+"}";
    }
    else if (command == "POST"){
        std::string content = "";
        int i = 1;
        while(parameters.size()>i){
            content += parameters.at(i)+" ";
            i++;
        }
        newLine =  "05"+content+"}";
    }
    else if (command == "PM"){
        std::string username = parameters.at(1);
        std::string content = "";
        int i = 2;
        while(parameters.size()>i){
            content += parameters.at(i)+" ";
            i++;
        }
        newLine =  "06"+username+"}"+content+"}"+getCurrentTime()+"}";
    }
    else if (command == "LOGSTAT"){
        newLine =  "07";
    }
    else if (command == "STAT"){
        std::string usernames = parameters.at(1);
        newLine =  "08"+usernames+"}";
    }
    else if (command == "BLOCK"){
        std::string usernames = parameters.at(1);
        newLine =  "12"+usernames+"}";
    }
    return newLine;
}

class additionnalThread{
private:
    ConnectionHandler & connectionHandler;
public:
    additionnalThread (ConnectionHandler &ch) :  connectionHandler(ch){}

    void run(){
        while (1) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            int len=line.length();
            line = newLineProcess(line);
            if (!connectionHandler.sendLine(line)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }}
    }
};

int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    additionnalThread task( connectionHandler);
    std::thread th(&additionnalThread::run, &task);
    while(1){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        const short bufsize = 1024;
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        
		int len=answer.length();
		// A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len-1);
        std::cout << answer << std::endl;
        if (answer == "ACK 3") {
            std::cout << "Exiting...\n" << std::endl;
            th.detach();
            break;
        }
    }
    return 0;}

