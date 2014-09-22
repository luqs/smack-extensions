package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public class KickOperate extends GenericOperate {
    private final String user;

    public KickOperate(String user, String reason) {
        super(Operate.KICK);
        this.user = user;
        setReason(reason);
    }

    @Override
    protected void startElement(XmlStringBuilder builder)
    {
        super.startElement(builder);
        builder.attribute("username", user);
    }
}
