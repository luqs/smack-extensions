package com.skysea.group.packet.operate;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 申请加入圈子
 * Created by apple on 14-9-30.
 */
public class ApplyOperate extends HasReasonOperate {
    private String nickName;

    public ApplyOperate() {
        super("apply");
    }

    public String getNickname() {
        return nickName;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        if (nickName != null) {
            builder.halfOpenElement("member")
                    .attribute("nickname", nickName)
                    .closeEmptyElement();
        }

        super.childrenElements(builder);
    }
}
