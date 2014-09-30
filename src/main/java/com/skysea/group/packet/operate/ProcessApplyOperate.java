package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 处理圈子申请的操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class ProcessApplyOperate extends HasReasonOperate {
    private final String id;
    private final String nickname;
    private final boolean result;
    private final String username;

    public ProcessApplyOperate(String id, String username, String nickname, boolean result) {
        super("apply");
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean getResult() {
        return result;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    protected void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.attribute("id", id);
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.halfOpenElement(result ? "agree" : "decline")
                .closeEmptyElement()
                .halfOpenElement("member")
                .attribute("username", username)
                .optAttribute("nickname", nickname)
                .closeEmptyElement();
        super.childrenElements(builder);
    }


}
