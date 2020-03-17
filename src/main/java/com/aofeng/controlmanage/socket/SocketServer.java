package com.aofeng.controlmanage.socket;



import com.alibaba.fastjson.JSON;
import com.aofeng.controlmanage.entity.CodeConstants;
import com.aofeng.controlmanage.entity.ResultVTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Component
@PropertySource("classpath:socket.properties")
public class SocketServer {
    @Value("${port}")
    private Integer port=8090;
    private boolean started;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public Logger log= LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) {
        new SocketServer().start(9000);
    }

    @PostConstruct
    public void init() {
        log.info("init.port:{}",this.port);
        new ServerThread(this.port).start();
    }

    public void start(Integer port) {
        try {
            serverSocket = new ServerSocket(port == null ? this.port : port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("端口冲突,异常信息：{}", e);
            System.exit(0);
        }
        while (started) {
            ClientSocket register=null;
            ResultVTO resultVTO=null;

            try {
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);
                try{
                    resultVTO=new ResultVTO();
                    register= SocketHandler.register(socket);
                    log.info("客户端已连接，其Key值为：{}", register.getKey());
                    resultVTO.setCode(CodeConstants.SUCCESS);
                    resultVTO.setMessage("注冊成功");
                    log.info("返回注册响应报文:{}",JSON.toJSONString(resultVTO));
                    register.getOutputStream().write(JSON.toJSONString(resultVTO).getBytes());
                    register.getOutputStream().flush();
                    if (register != null) {
                        executorService.submit(register);
                    }
                }catch (SocketException e){
                    log.error("注册失败");
                    resultVTO=new ResultVTO(CodeConstants.Register_FAIL,e.getMessage());
                    log.info("返回注册响应报文:{}",JSON.toJSONString(resultVTO));
                    register.getOutputStream().write(JSON.toJSONString(resultVTO).getBytes());
                    register.getOutputStream().flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ServerThread extends Thread{
        private int port;
        public ServerThread(int port){
            this.port=port;
        }
        @Override
        public void run() {
            new SocketServer().start(this.port);
        }
    }
}
