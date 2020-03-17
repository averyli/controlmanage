package com.aofeng.controlmanage.entity;

import java.util.Date;

public class Command {

    public String[] param;

    public String result;
    //time为用户在界面输入的时间
    public long time;

    public Command() {
    }

    public Command(String[] param) {
        this.param = param;
        this.time=new Date().getTime();
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
