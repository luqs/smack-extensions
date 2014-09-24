package com.skysea.group.packet.notify;

import com.skysea.group.MemberInfo;

/**
 * 用户事件通知。
 * Created by zhangzhi on 2014/9/23.
 */
public class MemberEventNotify extends HasReasonNotify {
    private MemberInfo memberInfo;
    public MemberEventNotify(Type type) {
        super(type);
    }

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }
}
