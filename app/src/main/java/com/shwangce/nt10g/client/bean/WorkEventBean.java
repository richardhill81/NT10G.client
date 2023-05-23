package com.shwangce.nt10g.client.bean;


import com.shwangce.nt10g.client.util.WorkEventType;

/**
 * Created by Administrator on 2016/9/14 0014.
 */

public class WorkEventBean {
    private WorkEventType eventType;
    private Object msg;

    public WorkEventBean() {};

    public WorkEventBean(WorkEventType eventType, Object msg) {
        this.eventType = eventType;
        this.msg = msg;
    }

    public WorkEventType getEventType() {
        return eventType;
    }

    public void setEventType(WorkEventType eventType) {
        this.eventType = eventType;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
