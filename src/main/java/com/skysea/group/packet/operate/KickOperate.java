package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 踢人操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class KickOperate extends HasReasonOperate {
    private final String userName;

    /**
     *
     * @param userName 被踢用户名。
     */
    public KickOperate(String userName) {
        super("kick");
        this.userName = userName;
    }

    /**
     * 获得被踢的用户名。
     * @return
     */
    public String getUserName() {
        return userName;
    }

    @Override
    protected void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.attribute("username", userName);
    }
}
