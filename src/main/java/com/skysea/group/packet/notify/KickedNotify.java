package com.skysea.group.packet.notify;

/**
 * 成员被踢出的通知。
 * Created by zhangzhi on 2014/9/23.
 */
public class KickedNotify extends MemberEventNotify implements HasOperator {
    private String from;
    public KickedNotify() {
        super(Type.MEMBER_KICKED);
    }

    @Override
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
