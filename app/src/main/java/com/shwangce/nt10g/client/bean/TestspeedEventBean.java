package com.shwangce.nt10g.client.bean;


import com.shwangce.nt10g.client.util.TestspeedEventType;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class TestspeedEventBean {
    private TestspeedEventType eventType;
    private Object msg;

    public TestspeedEventBean() {};

    public TestspeedEventBean(TestspeedEventType eventType, Object msg) {
        this.eventType = eventType;
        this.msg = msg;
    }

    public TestspeedEventType getEventType() {
        return eventType;
    }

    public void setEventType(TestspeedEventType eventType) {
        this.eventType = eventType;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
