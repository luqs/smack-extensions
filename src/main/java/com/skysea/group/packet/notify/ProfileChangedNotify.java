package com.skysea.group.packet.notify;

/**
 * Created by zhangzhi on 2014/9/23.
 */
public class ProfileChangedNotify extends MemberEventNotify {
    private String newNickname;
    public ProfileChangedNotify() {
        super(Type.MEMBER_PROFILE_CHANGED);
    }

    public String getNewNickname() {
        return newNickname;
    }

    public void setNewNickname(String newNickname) {
        this.newNickname = newNickname;
    }
}
