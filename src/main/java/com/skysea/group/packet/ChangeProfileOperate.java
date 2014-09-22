package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public class ChangeProfileOperate  extends GenericOperate {
    private String nickname;

    public ChangeProfileOperate() {
        super(Operate.PROFILE);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.element("nickname", nickname);
    }
}
