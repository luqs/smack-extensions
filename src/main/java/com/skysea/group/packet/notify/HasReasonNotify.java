package com.skysea.group.packet.notify;

import com.skysea.group.packet.HasReason;

/**
 * 包含reason信息的通知。
 * Created by zhangzhi on 2014/9/23.
 */
public abstract class HasReasonNotify extends Notify implements HasReason {
    private String reason;
    protected HasReasonNotify(Type type) {
        super(type);
    }

    @Override
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
