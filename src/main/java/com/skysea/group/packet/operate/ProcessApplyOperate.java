package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 处理圈子申请的操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class ProcessApplyOperate extends HasReasonOperate {
    private final String id;
    private final boolean result;
    private String from;

    public ProcessApplyOperate(String id, boolean result) {
        super("apply");
        this.id = id;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean getResult() {
        return result;
    }


    @Override
    protected void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.attribute("id", id);
        builder.attribute("from", from);
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.halfOpenElement(result ? "agree" : "decline");
        builder.closeEmptyElement();
        super.childrenElements(builder);
    }
}
