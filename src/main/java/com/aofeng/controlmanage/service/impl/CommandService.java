package com.aofeng.controlmanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.aofeng.controlmanage.entity.CodeConstants;
import com.aofeng.controlmanage.entity.Command;
import com.aofeng.controlmanage.entity.ResultVTO;
import com.aofeng.controlmanage.service.ICommandService;
import com.aofeng.controlmanage.socket.SocketHandler;
import com.aofeng.controlmanage.socket.SocketPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class CommandService implements ICommandService {

     private Queue<Command> commandQueue=new ConcurrentLinkedQueue();

    private  Queue<Command> commandResultQueue=new ConcurrentLinkedQueue();

     @Value("${maxSize}")
     public int maxSize;

     public Logger log= LoggerFactory.getLogger(CommandService.class);

    @Override
    public ResultVTO saveComamnd(String message,String key) {
        ResultVTO resultVTO;
        Command command=new Command(message.trim().split("\\s+"));
        try {
            log.info("发送命令:{}",JSON.toJSONString(command));
            String result=SocketHandler.sendMessage(key,JSON.toJSONString(command));
            log.info("接收到命令执行结果:{}",result);
            resultVTO=new ResultVTO<Command>(CodeConstants.SUCCESS,"发送命令成功");
            Command commandResult=JSON.parseObject(result,Command.class);
            resultVTO.setData(commandResult);
        }catch (SocketException e){
            resultVTO=new ResultVTO();
            resultVTO.setCode(CodeConstants.CONNECT_FAIL);
            resultVTO.setMessage(e.getMessage());
        }
        return resultVTO;
    }

    @Override
    public ResultVTO listCommand() {
        ResultVTO resultVTO=null;
        if(commandQueue.isEmpty()){
            resultVTO=new ResultVTO();
            resultVTO.setCode(CodeConstants.QUERY_FAIL);
            resultVTO.setMessage("暂无命令数据");
        }else {
             resultVTO=new ResultVTO<Queue>();
             resultVTO.setCode(CodeConstants.SUCCESS);
             resultVTO.setMessage("查询命令成功");
        }
        return resultVTO;
    }

    @Override
    public ResultVTO saveCommandResult(Command command) {
        ResultVTO resultVTO=new ResultVTO();
        if(commandQueue.size()>maxSize){
            resultVTO.setMessage(CodeConstants.ADD_FAIL);
            resultVTO.setMessage("超出命令队列存储长度");
        }else {
            commandQueue.add(command);
            resultVTO.setMessage(CodeConstants.SUCCESS);
            resultVTO.setMessage("保存成功");
        }
        return resultVTO;
    }

    @Override
    public ResultVTO listCommandResult() {
        return null;
    }

    @Override
    public ResultVTO getAllKeys() {
        ResultVTO<List<String>> resultVTO=new ResultVTO<List<String>>(CodeConstants.SUCCESS,"获取keys成功");
        resultVTO.setData(SocketPool.getAllKeys());
        return resultVTO;
    }
}
