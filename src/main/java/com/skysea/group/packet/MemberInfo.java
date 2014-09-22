package com.skysea.group.packet;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public class MemberInfo{
    private final String nickname;
    private final String userName;

    public MemberInfo(String userName, String nickname){
        this.userName = userName;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserName() {
        return userName;
    }
}
