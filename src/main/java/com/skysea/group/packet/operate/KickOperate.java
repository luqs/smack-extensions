package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 踢人操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class KickOperate extends HasReasonOperate {
    private final String userName;
    public KickOperate(String userName) {
        super("kick");
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.attribute("username", userName);
    }
}
