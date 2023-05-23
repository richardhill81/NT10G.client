package com.shwangce.nt10g.client.library.ControlFrame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public class FrameHelper {

    public enum FrameKind {
        COMMAND,
        RESULT
    }

    public static final byte INFO_BLUETOOTH_CONNECTED = (byte)0xAA;
    public static final byte INFO_BLUETOOTH_DISCONNECTED = (byte)0xBB;
    public static final byte INFO_BLUETOOTH_LOWMODE = (byte)0xCC;
    public static final byte INFO_BLUETOOTH_HIGHMODE = (byte)0xDD;
    public static final byte INFO_BLUETOOTH_MAC = (byte)0xEE;

    public static final byte[] COMMANDHEAD_BLUETOOTH_MAC = new byte[] {(byte)0x02,(byte)0xEE};

    public static final byte COMMANDHEAD_FIRST = (byte)0xF0;
    public static final byte COMMANDHEAD_CONTINUE = (byte)0xF1;
    public static final byte RESULTHEAD_FIRST =  (byte)0xFA;
    public static final byte RESULTHEAD_CONTINUE =  (byte)0xFB;
    public static final byte FRAMEEND_CONTINUE = (byte)0xFE;
    public static final byte FRAMEEND_COMPLETE = (byte)0xFF;
    public static final byte FRAME_ADD = (byte)0x00;

/*

    public static byte[] getFrameBytes(FrameKind frameKind,@NonNull byte[] data, int maxframelength) {
        int detailLength;               //所有内容的长度
        int simpledetailLength;         //单个帧中内容部分的长度
        int simpleframeLength;          //单个帧的长度，不足需要后面补0x00
        int sentdetailLength = 0;       //已发送的内容长度
        int framecount;                 //所需发送的总帧数
        int t = 0;
        byte[] sendTotalbytes = null;
        byte[] detail;
        byte commandflag = data[0];
        if(data.length > 1) {
            detail = new byte[data.length - 1];
            for(int i=1;i<data.length ;i++)
            {
                detail[i-1] = data[i];
            }
            detailLength = detail.length;
        } else  {
            detail = null;
            detailLength = 0;
        }
        if(detailLength  == 0)
            framecount = 1;
        else
            framecount = (int)Math.ceil((double)detailLength /(double) (maxframelength - 4));//去掉包头、长度、指令、包尾 ,需要拆分发送
        sendTotalbytes = new byte[maxframelength * framecount];
        for(int i=0;i<framecount;i++) {
            byte[] s = new byte[maxframelength]; ;
            switch (frameKind) {
                case COMMAND:
                    t = 0;
                    if(i == 0 )
                        s[0] = COMMANDHEAD_FIRST;
                    else
                        s[0] = COMMANDHEAD_CONTINUE;
                    break;

                case RESULT:
                    t = 1;
                    s[0] = (byte)(maxframelength + 1);
                    if(i == 0 )
                        s[1] = RESULTHEAD_FIRST;
                    else
                        s[1] = RESULTHEAD_CONTINUE;
                    break;
            }

            if(i == framecount -1) {
                simpledetailLength = detailLength - sentdetailLength;
                s[simpledetailLength + 3] = FRAMEEND_COMPLETE;
            } else {
                simpledetailLength = maxframelength - 4;    //数据
                s[simpledetailLength + 3] = FRAMEEND_CONTINUE;
            }
            simpleframeLength = simpledetailLength + 4;
            s[t+1] = (byte)(simpledetailLength + 1);
            if(i == 0)
                s[t+2] = commandflag;
            else
                s[t+2] = COMMAND_CONTINUE_DETAIL;
            for(int j=0;j<simpledetailLength;j++) {
                s[t+ 3 + j ] = detail[sentdetailLength + j];
            }
            sentdetailLength += simpledetailLength;
            for(int j = simpleframeLength; j <maxframelength;j++) {
                s[t+j] = FRAME_ADD;
            }
            for(int k=0;k<maxframelength;k++) {
                sendTotalbytes[maxframelength * i + k ] = s[k];
            }
        }
        return sendTotalbytes;
    }

*/


    public static byte[] getFrameBytes(FrameKind frameKind,@NonNull byte[] data, int maxframelength) {
        int simpledataLength;         //单个帧中内容部分的长度
        int sentLength = 0;       //已发送的长度
        int totalDataLength = data.length;
        int framestart = 0;
        int simpleMaxFrameLength = maxframelength;
        int simpleMaxdatalength = maxframelength - 3;
        int framecount = (int)Math.ceil((double)data.length /(double) (simpleMaxdatalength));      //所需发送的总帧数
        int sendbytescount = 0;
        byte[] sendTotalbytes = null;
        byte[] s;
        switch (frameKind) {
            case COMMAND:
                simpleMaxFrameLength = maxframelength;
                break;

            case RESULT:
                simpleMaxFrameLength = maxframelength + 1;
                break;
        }
        sendTotalbytes = new byte[simpleMaxFrameLength * framecount];
        for(int i=0;i<framecount;i++) {
            switch (frameKind) {
                case RESULT:
                    s = new byte[maxframelength + 1];
                    framestart = 1;
                    s[0] = (byte)(maxframelength + 1);
                    if(i == 0 )
                        s[1] = RESULTHEAD_FIRST;
                    else
                        s[1] = RESULTHEAD_CONTINUE;
                    break;

                default:            //case COMMAND:
                    s = new byte[maxframelength];
                    framestart = 0;
                    if(i == 0 )
                        s[0] = COMMANDHEAD_FIRST;
                    else
                        s[0] = COMMANDHEAD_CONTINUE;
                    break;
            }
            if(sentLength + simpleMaxdatalength >= totalDataLength) {       //可发完
                simpledataLength = totalDataLength - sentLength;
            } else
                simpledataLength = simpleMaxdatalength;
            s[framestart + 1] = (byte) simpledataLength;
            for(int j=0;j<simpledataLength;j++) {
                s[framestart + 2 + j] = data[sentLength + j];
            }
            sentLength += simpledataLength;
            if(i == framecount -1) {
                s[framestart + 2 + simpledataLength] = FRAMEEND_COMPLETE;
            } else {
                s[framestart + 2 + simpledataLength] = FRAMEEND_CONTINUE;
            }

            for(int j=simpledataLength;j<simpleMaxdatalength;j++) {
                s[framestart+ 3 + j ] = FRAME_ADD;
            }
            for(int k=0;k<simpleMaxFrameLength;k++) {
                //sendTotalbytes[maxframelength * i + k ] = s[k];
                sendTotalbytes[sendbytescount] = s[k];
                sendbytescount ++;
            }
        }
        return sendTotalbytes;
    }

    public static byte[] getResultSerialBytes(@NonNull byte[] data,int maxserialdatalength,int maxframelength) {
        byte[] framedata = getFrameBytes(FrameKind.RESULT,data,maxframelength);
        int datalength  = framedata.length;
        boolean transFinished = false;
        int transframelength = 0;
        int simplelength = 0;
        ArrayList<Byte> sendBytes = new ArrayList<>();
        byte[] resultbytes;
        while(!transFinished) {
            if(transframelength >= datalength) {
                transFinished = true;
            } else {
                if(datalength - transframelength > maxserialdatalength) {
                    simplelength = maxserialdatalength;
                } else {
                    simplelength = datalength - transframelength;
                }
                sendBytes.add((byte)(simplelength + 1));
                for(int i=0;i<simplelength;i++) {
                    sendBytes.add(framedata[transframelength + i]);
                }
                transframelength += simplelength;
            }
        }
        resultbytes = new byte[sendBytes.size()];
        for(int i=0;i<sendBytes.size();i++) {
            resultbytes[i] = sendBytes.get(i);
        }
        return resultbytes;
    }

    public static CommandFrameBean getCommandFrame(final ArrayList<Byte> dataList) {
        CommandFrameBean commandFrameBean = null;
        byte frametype = 0x00;
        byte[] dataInfo = null;
        boolean bStart = false;
        int datalength = 0;
        ArrayList<Byte> data = (ArrayList<Byte>)dataList.clone();
        while(data.size() >0) {
            if(bStart) {
                datalength = data.get(1);
                if(data.size() > datalength + 1) {
                    if(data.get(datalength+2) == FRAMEEND_COMPLETE || data.get(datalength+2) == FRAMEEND_CONTINUE) {
                        commandFrameBean = new CommandFrameBean();
                        commandFrameBean.setCommandHead(data.get(0));
                        commandFrameBean.setDataLength(data.get(1));
                        if(frametype == COMMANDHEAD_FIRST) {
                            commandFrameBean.setCommandByte(data.get(2));
                            if(datalength >1) {
                                dataInfo = new byte[datalength - 1];
                                for (int i = 0; i < datalength - 1; i++) {
                                    dataInfo[i] = data.get(3 + i);
                                }
                                commandFrameBean.setData(dataInfo);
                            }
                            commandFrameBean.setCommandEnd(data.get(2 + datalength));
                        }
                        else {
                            commandFrameBean.setCommandByte(FRAME_ADD);
                            if(datalength >0) {
                                dataInfo = new byte[datalength];
                                for (int i = 0; i < datalength; i++) {
                                    dataInfo[i] = data.get(2 + i);
                                }
                                commandFrameBean.setData(dataInfo);
                            }
                            commandFrameBean.setCommandEnd(data.get(2 + datalength));
                        }
                        return commandFrameBean;
                    } else {        //帧尾错误，重新寻找帧头
                        data.remove(0);
                        bStart = false;
                    }
                } else {    //未接收完数据，返回空
                    return null;
                }
            } else {
                if(data.get(0) == COMMANDHEAD_FIRST || data.get(0) == COMMANDHEAD_CONTINUE ) {
                    frametype = data.get(0);
                    bStart = true;
                } else {
                    data.remove(0);
                }
            }
        }
        return null;
    }

    @Nullable
    public static ResultFrameBean getResultFrame(final ArrayList<Byte> dataList) {
        ResultFrameBean resultFrameBean = null;
        byte frametype = 0x00;
        byte[] dataInfo = null;
        boolean bStart = false;
        int datalength = 0;
        ArrayList<Byte> data = (ArrayList<Byte>)dataList.clone();
        while(data.size() >0) {
            if(bStart) {
                datalength = data.get(1);
                if(data.size() > datalength + 1) {
                    if(data.get(datalength+2) == FRAMEEND_COMPLETE || data.get(datalength+2) == FRAMEEND_CONTINUE) {
                        resultFrameBean = new ResultFrameBean();
                        resultFrameBean.setResultdHead(data.get(0));
                        resultFrameBean.setDataLength(data.get(1));
                        if(frametype == RESULTHEAD_FIRST) {
                            resultFrameBean.setResultByte(data.get(2));
                            if (datalength > 1) {
                                dataInfo = new byte[datalength - 1];
                                for (int i = 0; i < datalength - 1; i++) {
                                    dataInfo[i] = data.get(3 + i);
                                }
                                resultFrameBean.setData(dataInfo);
                            }
                            resultFrameBean.setResultEnd(data.get(2 + datalength));
                        } else  {
                            resultFrameBean.setResultByte(FRAME_ADD);
                            if (datalength > 0) {
                                dataInfo = new byte[datalength];
                                for (int i = 0; i < datalength; i++) {
                                    dataInfo[i] = data.get(2 + i);
                                }
                                resultFrameBean.setData(dataInfo);
                            }
                            resultFrameBean.setResultEnd(data.get(2 + datalength));
                        }
                        return resultFrameBean;
                    } else {        //帧尾错误，重新寻找帧头
                        data.remove(0);
                        bStart = false;
                    }
                } else {    //未接收完数据，返回空
                    return null;
                }
            } else {
                if(data.get(0) == RESULTHEAD_FIRST || data.get(0) == RESULTHEAD_CONTINUE ) {
                    frametype = data.get(0);
                    bStart = true;
                } else {
                    data.remove(0);
                }
            }
        }
        return null;
    }
}
