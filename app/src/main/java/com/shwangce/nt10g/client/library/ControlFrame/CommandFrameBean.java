package com.shwangce.nt10g.client.library.ControlFrame;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class CommandFrameBean {
    private byte commandHead = 0x00;
    private byte dataLength = 0x00;
    private byte commandByte = 0x00;
    private byte[] data = null;

    private byte commandEnd = 0x00;

    public byte getCommandHead() {
        return commandHead;
    }

    public void setCommandHead(byte commandHead) {
        this.commandHead = commandHead;
    }

    public byte getDataLength() {
        return dataLength;
    }

    public void setDataLength(byte dataLength) {
        this.dataLength = dataLength;
    }

    public byte getCommandByte() {
        return commandByte;
    }

    public void setCommandByte(byte commandByte) {
        this.commandByte = commandByte;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data =  data;
    }

    public byte getCommandEnd() {
        return commandEnd;
    }

    public void setCommandEnd(byte commandEnd) {
        this.commandEnd = commandEnd;
    }
}
