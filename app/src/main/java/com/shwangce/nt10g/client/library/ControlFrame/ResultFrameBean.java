package com.shwangce.nt10g.client.library.ControlFrame;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class ResultFrameBean {
    private byte resultdHead = 0x00;
    private byte dataLength = 0x00;
    private byte resultByte = 0x00;
    private byte[] data = null;
    private byte resultEnd = 0x00;

    public byte getResultdHead() {
        return resultdHead;
    }

    public void setResultdHead(byte resultdHead) {
        this.resultdHead = resultdHead;
    }

    public byte getDataLength() {
        return dataLength;
    }

    public void setDataLength(byte dataLength) {
        this.dataLength = dataLength;
    }

    public byte getResultByte() {
        return resultByte;
    }

    public void setResultByte(byte resultByte) {
        this.resultByte = resultByte;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getResultEnd() {
        return resultEnd;
    }

    public void setResultEnd(byte resultEnd) {
        this.resultEnd = resultEnd;
    }
}
