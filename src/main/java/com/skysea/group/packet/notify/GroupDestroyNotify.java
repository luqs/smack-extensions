package com.skysea.group.packet.notify;

/**
 * 圈子销毁的通知。
 * Created by zhangzhi on 2014/9/23.
 */
public class GroupDestroyNotify extends HasOperatorNotify {
    public GroupDestroyNotify() {
        super(Type.GROUP_DESTROY);
    }
}
