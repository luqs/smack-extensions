package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 修改圈子名片的操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class ChangeProfileOperate extends Operate {
    private String nickname;

    public ChangeProfileOperate() {
        super("profile");
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.element("nickname", nickname);
    }
}
