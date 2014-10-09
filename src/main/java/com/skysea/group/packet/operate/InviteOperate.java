package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 邀请操作。
 * Created by zhangzhi on 2014/10/9.
 */
public class InviteOperate extends Operate {
    private final String userName;
    private final String nickname;

    public InviteOperate(String userName, String nickname) {
        super("invite");
        this.userName = userName;
        this.nickname = nickname;
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.halfOpenElement("member")
                .attribute("username", userName)
                .optAttribute("nickname", nickname)
                .closeEmptyElement();
    }


    /**
     * 被邀请的用户。
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 被邀请用户的昵称。
     * @return
     */
    public String getNickname() {
        return nickname;
    }

}
