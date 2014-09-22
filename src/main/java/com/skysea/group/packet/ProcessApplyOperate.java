package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public class ProcessApplyOperate extends GenericOperate {
    private final String id;
    private final String proposer;
    private boolean result;

    public ProcessApplyOperate(String id, String proposer) {
        super(Operate.APPLY);
        this.id = id;
        this.proposer = proposer;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    protected void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.attribute("id", id);
        builder.attribute("from", proposer);
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        if(result) {
            builder.halfOpenElement("agree");
        }else {
            builder.halfOpenElement("decline");
        }
        builder.closeEmptyElement();
        super.childrenElements(builder);
    }
}
