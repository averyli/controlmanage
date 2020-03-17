package com.aofeng.controlmanage.controller;

import com.alibaba.fastjson.JSON;
import com.aofeng.controlmanage.entity.CodeConstants;
import com.aofeng.controlmanage.entity.Command;
import com.aofeng.controlmanage.entity.ResultVTO;
import com.aofeng.controlmanage.service.ICommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/command")
public class ComandController {

    public Logger log= LoggerFactory.getLogger(ComandController.class);

    @Autowired
    ICommandService commandService;

    @PostMapping("/setCommands")
    @ResponseBody
    public ResultVTO setCommands(@RequestParam(value = "command") String command,@RequestParam(value = "key") String key){
        if(command!=null){
            log.info("输入命令为:{},key:{}",command,key);
            ResultVTO resultVTO=commandService.saveComamnd(command,key);
            log.info("返回报文:{}", JSON.toJSONString(resultVTO));
            return resultVTO;
        }else{
            log.error("传入参数错误,值为null");
            return new ResultVTO(CodeConstants.ADD_FAIL,"传入参数错误");
        }
    }


    @GetMapping("/fetchCommands")
    @ResponseBody
    public ResultVTO fetchCommand(){
        return commandService.listCommand();
    }

    @PostMapping("/uploadCommandResult")
    @ResponseBody
    public ResultVTO uploadCommandResult(@RequestBody Command command){

        log.debug("uploadCommandResult:"+command.toString());
        return new ResultVTO(CodeConstants.SUCCESS,"上传命令结果成功");
    }

    @GetMapping("/getAllKeys")
    @ResponseBody
    public ResultVTO getAllKeys(){
        return commandService.getAllKeys();
    }
}
