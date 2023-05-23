package com.shwangce.nt10g.client.library.ControlFrame;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class ResultBean {
    private ResultKind resultType = ResultKind.Unknown;
    private ArrayList<Byte> byteArrayList = new ArrayList<>();
    private String resultParams = "";


    public ResultBean() {
    }

    public ResultBean(ResultKind resultType, String resultParams) {
        this.resultType = resultType;
        this.resultParams = resultParams;
    }

    public ResultKind getResultType() {
        return resultType;
    }

    public void setResultType(ResultKind resultType) {
        this.resultType = resultType;
    }

    public String getResultParams() {
        if(byteArrayList.size() >0) {
            byte[] params = new byte[byteArrayList.size()];
            for (int i = 0; i < params.length; i++) {
                params[i] = byteArrayList.get(i);
            }
            resultParams = new String(params);
        } else  {
            resultParams = "";
        }
        return resultParams;
    }

    public void addResultParams(byte[] resultParams) {
        ArrayList<Byte> list = new ArrayList<>();
        for(int i=0;i<resultParams.length;i++) {
            list.add(resultParams[i]);
        }
        byteArrayList.addAll(list);
    }
}
