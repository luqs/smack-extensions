package com.skysea.group.packet.operate;

import com.skysea.group.MemberInfo;
import com.skysea.group.packet.HasMember;
import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * 邀请操作。
 *  <invite>
 *      <member username='user100' nickname='独孤求败' />
 *  </invite>
 * Created by zhangzhi on 2014/10/9.
 */
public final class InviteOperate extends Operate implements HasMember {
    private MemberInfo memberInfo;

    public InviteOperate(String userName, String nickname) {
        super("invite");
        memberInfo = new MemberInfo(userName, nickname);
    }

    @Override
    protected void childrenElements(XmlStringBuilder builder) {
        builder.halfOpenElement("member")
                .attribute("username", memberInfo.getUserName())
                .optAttribute("nickname", memberInfo.getNickname())
                .closeEmptyElement();
    }

    /**
     * 获得被邀请的成员信息。
     * @return
     */
    @Override
    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    @Override
    public void setMemberInfo(MemberInfo member) {
        this.memberInfo = member;
    }

}
