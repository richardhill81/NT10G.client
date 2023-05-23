package com.shwangce.nt10g.client.model;

import com.shwangce.nt10g.client.util.TestspeedEventType;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class TestspeedException extends Exception {
    private TestspeedEventType eventType ;
    private String message;

    public TestspeedException(TestspeedEventType eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }

    public TestspeedEventType getEventType() {
        return eventType;
    }

    public void setEventType(TestspeedEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
