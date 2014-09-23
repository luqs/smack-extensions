package com.skysea.group.packet.operate;

/**
 * 一般性操作。
 * Created by zhangzhi on 2014/9/23.
 */
public final class GenericOperate extends HasReasonOperate {

    /**
     * 退出圈子的操作类型。
     */
    public final static String EXIT = "exit";

    /**
     * 销毁圈子的操作类型。
     */
    public final static String DESTROY = "destroy";

    /**
     * 申请加入圈子的操作类型。
     */
    public final static String APPLY = "apply";
    public GenericOperate(String type) {
        super(type);
    }
}
