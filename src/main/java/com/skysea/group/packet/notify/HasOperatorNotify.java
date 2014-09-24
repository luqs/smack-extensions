package com.skysea.group.packet.notify;

/**
 * 包含操作员的通知基类。
 * Created by apple on 14-9-23.
 */
public abstract class HasOperatorNotify extends HasReasonNotify implements HasOperator {
    private String from;
    protected HasOperatorNotify(Type type) {
        super(type);
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }
}
