package com.skysea.group.packet.notify;

/**
 * 申请加入圈子的通知。
 * Created by zhangzhi on 2014/9/23.
 */
public class ApplyNotify extends MemberEventNotify implements HasOperator {
    private String id;
    private String from;
    public ApplyNotify() {
        super(Type.MEMBER_APPLY_TO_JOIN);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }
}
