package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/22.
 */

public class GenericOperate extends Operate implements OperateReply{
    private String id;
    private String from;
    private MemberInfo member;

    public GenericOperate(String type) {
        super(type);
    }

    public void setFrom(String from){
        this.from = from;
    }

    @Override
    public String getFrom() {
        return from;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setMember(MemberInfo member) {
        this.member = member;
    }

    @Override
    public MemberInfo getMember() {
        return member;
    }
}
