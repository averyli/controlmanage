package com.aofeng.controlmanage.socket;

import com.alibaba.fastjson.JSON;
import com.aofeng.controlmanage.entity.Command;
import com.aofeng.controlmanage.entity.ResultVTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.Socket;

@SpringBootTest
public class SocketTest {

    public static Logger log = LoggerFactory.getLogger(SocketTest.class);

    public static void main(String[] args) throws IOException {
        clientConnect();
    }

    @Test
    public static void clientConnect() throws IOException {
        String host = "127.0.0.1";
        int port = 9000;

        //与服务端建立连接
        Socket socket = new Socket(host, port);
        socket.setOOBInline(true);

        //建立连接后获取输出流
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        register(outputStream, inputStream);
        int i=0;

        while (true) {

            Command command = readCommand(inputStream);
//            log.info("command:{}", command);
            command.setResult("c"+i++);
            writeCommandResult(command,outputStream);
        }
    }

    public static void register(DataOutputStream outputStream, DataInputStream inputStream) throws IOException {

        String buffer = null;
        while (!isRegisterSuccess(buffer)) {
            String key = "testMachine";
//            log.info("uuid: {}", key);
            System.out.println("向服务器注册本客户端,key为"+key);
            outputStream.write(key.getBytes());

            byte[] buff = new byte[1024];
            inputStream.read(buff);
            buffer = new String(buff, "utf-8");

        }

    }

    public static boolean isRegisterSuccess(String message) {
        if (message == null) {
            return false;
        }
        ResultVTO resultVTO = JSON.parseObject(message, ResultVTO.class);
        if (resultVTO.code.equals("000000")) {
            System.out.println("注册成功，响应报文:"+JSON.toJSONString(resultVTO));
            return true;
        } else {
            System.out.println("注册失败，响应报文:"+JSON.toJSONString(resultVTO));
            return false;
        }
    }

    public static Command readCommand(DataInputStream inputStream) throws IOException {
        System.out.println("等待读取命令...");
        byte[] buff = new byte[1024];
        inputStream.read(buff);
        String buffer = new String(buff, "utf-8");
        System.out.println("接收到命令:"+buffer);
//        log.info("readCommand:,{}", buffer);
        return JSON.parseObject(buffer, Command.class);
    }


    public static void writeCommandResult(Command command,DataOutputStream outputStream) throws IOException {
        System.out.println("返回执行命令结果:"+JSON.toJSONString(command));
        outputStream.write(JSON.toJSONString(command).getBytes());
        outputStream.flush();
    }

}
