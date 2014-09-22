package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public abstract class Operate {
    public final static String APPLY  = "apply";
    public final static String JOINED = "join";
    public final static String PROFILE = "profile";
    public final static String EXIT = "exit";
    public final static String KICK = "kick";
    public final static String DESTROY = "destroy";

    protected final String type;
    private String reason;


    protected Operate(String type){
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public XmlStringBuilder toXML() {
        return new XmlStringBuilder()
                .halfOpenElement(type)
                .rightAngelBracket()
                .optElement("reason", reason)
                .closeElement(type);
    }
}
