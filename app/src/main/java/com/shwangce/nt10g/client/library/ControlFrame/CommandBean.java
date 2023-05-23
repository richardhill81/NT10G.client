package com.shwangce.nt10g.client.library.ControlFrame;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/2/17 0017.
 */

public class CommandBean {
    private String commandType = "";
    private ArrayList<Byte> commandParamsBytes = new ArrayList<>();

    private String commandParams = "";

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getCommandParams() {
        if(commandParamsBytes.size() >0) {
            byte[] params = new byte[commandParamsBytes.size()];
            for(int i=0;i<params.length;i++) {
                params[i] = commandParamsBytes.get(i);
            }
            commandParams = new String(params);
        }
        else {
            commandParams = "";
        }
        return commandParams;
    }

    public byte[] getbyteCommandParams() {
        byte[] params = new byte[commandParamsBytes.size()];
        for(int i=0;i<params.length;i++) {
            params[i] = commandParamsBytes.get(i);
        }
        return params;
    }

    public void addCommandParams(byte[] commandParams) {
        ArrayList<Byte> blist = new ArrayList<>();
        for(int i=0;i<commandParams.length;i++) {
            blist.add(commandParams[i]);
        }
        this.commandParamsBytes.addAll(blist);
    }
}
