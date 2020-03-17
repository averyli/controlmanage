package com.aofeng.controlmanage.socket;


import com.alibaba.fastjson.JSON;
import com.aofeng.controlmanage.entity.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


public class SocketHandler {

    public static Logger log = LoggerFactory.getLogger(SocketHandler.class);

    /**
     * 将连接的Socket注册到Socket池中
     */
    public static ClientSocket register(Socket socket) throws SocketException {
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.setSocket(socket);
        try {
            clientSocket.setInputStream(new DataInputStream(socket.getInputStream()));
            clientSocket.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            byte[] bytes = new byte[1024];
            clientSocket.getInputStream().read(bytes);
            String key=new String(bytes, "utf-8").trim();
            if(SocketPool.getCLientSocket(key)!=null){
                throw new SocketException("已存在相同key,不允许重复注册！");
            }
            clientSocket.setKey(key);
            SocketPool.add(clientSocket);
            return clientSocket;
        }catch (SocketException  e){
            throw e;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定客户端发送信息     * @param clientSocket     * @param message
     */
    public static String sendMessage(ClientSocket clientSocket, String message) {
        try {
            log.info("--->key={},message={}",clientSocket.getKey(),message);
            clientSocket.getOutputStream().write(message.getBytes("utf-8"));
            clientSocket.getOutputStream().flush();
            byte[] bytes = new byte[1024];
            clientSocket.getInputStream().read(bytes);
            String result=new String(bytes);
            return result;
        } catch (IOException e) {
            log.error("发送信息异常：{}", e);
            close(clientSocket);
        }
        return null;
    }

    /**
     *
     * @param key
     * @param message JSON格式字符串
     * @return
     */
    public static String sendMessage(String key, String message) throws SocketException {
        ClientSocket clientSocket = null;
        clientSocket = SocketPool.getCLientSocket(key);
        if(clientSocket==null){
            throw new SocketException("主机连接不上，请重新确认该机器已连接控制管理平台！");
        }
        return sendMessage(clientSocket, message);
    }

    /**
     * 获取指定客户端的上传信息
     */
    public static String onMessage(ClientSocket clientSocket) {
        byte[] bytes = new byte[1024];
        try {
            clientSocket.getInputStream().read(bytes);
            String msg = new String(bytes, "utf-8");
            return msg;
        } catch (IOException e) {
            e.printStackTrace();
            close(clientSocket);
        }
        return null;
    }

    /**
     * 指定Socket资源回收
     */
    public static void close(ClientSocket clientSocket) {
        log.info("进行资源回收");
        if (clientSocket != null) {
            log.info("开始回收socket相关资源，其Key为{}", clientSocket.getKey());
            SocketPool.remove(clientSocket.getKey());
            Socket socket = clientSocket.getSocket();
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
            } catch (IOException e) {
                log.error("关闭输入输出流异常，{}", e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("关闭socket异常{}", e);
                }
            }
        }
    }

    /**
     * 发送数据包，判断数据连接状态
     */
    public static boolean isSocketClosed(ClientSocket clientSocket) {
        try {
            clientSocket.getSocket().sendUrgentData(1);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
