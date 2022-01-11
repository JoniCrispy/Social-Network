package bgu.spl.net.impl.BGSServer;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.rci.Command;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGSEncoderDecoder implements MessageEncoderDecoder {

    private byte[] bytes;
    private int len;
    private short opcode;


    public BGSEncoderDecoder(){
        clear();
    }

    public void clear(){
        bytes = new byte[256];
        len = 0;
        opcode = -1;
    }

    @Override
public char[] decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            return popString();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(Object message) {
        return (message.toString() + ";").getBytes();
    }

    private char[] popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        char[] resultChar = new char[result.length()];
        for (int i = 0;i<resultChar.length;i++){
            resultChar[i] = result.charAt(i);
        }
        len = 0;
        return resultChar;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

//    @Override
//    public byte[] encode(Command<User> message) {
//        return new byte[0];
//    }


}
