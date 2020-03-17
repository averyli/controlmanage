package com.aofeng.controlmanage.service;

import com.aofeng.controlmanage.entity.Command;
import com.aofeng.controlmanage.entity.ResultVTO;

public interface ICommandService {

    public ResultVTO saveComamnd(String message,String key);

    public ResultVTO listCommand();

    public ResultVTO saveCommandResult(Command command);

    public ResultVTO listCommandResult();

    public ResultVTO getAllKeys();

}
