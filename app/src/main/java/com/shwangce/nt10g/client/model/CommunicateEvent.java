package com.shwangce.nt10g.client.model;

import java.util.EventObject;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class CommunicateEvent extends EventObject{

    public enum EventType {
        FindNewDevice,
        DiscoveryFinished,
        ConnectSuccess,
        ConnectError,
        ConnectLoss,
        ResultReceive,
        NULL
    }

    private EventType eventType = EventType.NULL;
    private Object message = null;

    public CommunicateEvent(Object source) {
        super(source);
    }

    public CommunicateEvent(Object source, EventType eventType, Object message) {
        super(source);
        this.eventType = eventType;
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
