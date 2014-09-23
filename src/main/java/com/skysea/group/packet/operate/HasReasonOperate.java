package com.skysea.group.packet.operate;

import com.skysea.group.packet.HasReason;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 包含原因的操作。
 * Created by zhangzhi on 2014/9/23.
 */
public abstract class HasReasonOperate extends Operate implements HasReason {
    private String reason;
    protected HasReasonOperate(String type) {
        super(type);
    }

    @Override
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.optElement("reason", getReason());
    }
}
