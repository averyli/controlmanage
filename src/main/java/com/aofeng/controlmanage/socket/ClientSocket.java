package com.aofeng.controlmanage.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static com.aofeng.controlmanage.socket.SocketHandler.close;
import static com.aofeng.controlmanage.socket.SocketHandler.isSocketClosed;

public class ClientSocket implements Runnable {
    public Logger log= LoggerFactory.getLogger(ClientSocket.class);

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String key;
    private String message;

    @Override
    public void run() {
        //每5秒进行一次客户端连接，判断是否需要释放资源
        while (true) {
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if (isSocketClosed(this)) {
//               log.info("客户端已关闭,其Key值为：{}", this.getKey());
//                //关闭对应的服务端资源
//                close(this);
//                break;
//            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

