package com.shwangce.nt10g.client.library.bluetoothLe;

/**
 * Created by linhao on 2017/11/16.
 */

public class WriteBean {


    private Object writeObjcet = null;
    private byte[] writeValue = null;

    public WriteBean() {
    }

    public WriteBean(Object writeType, byte[] writeValue) {
        this.writeObjcet = writeType;
        this.writeValue = writeValue;
    }

    public Object getWriteObjcet() {
        return writeObjcet;
    }

    public void setWriteObjcet(Object writeObjcet) {
        this.writeObjcet = writeObjcet;
    }

    public byte[] getWriteValue() {
        return writeValue;
    }

    public void setWriteValue(byte[] writeValue) {
        this.writeValue = writeValue;
    }
}
