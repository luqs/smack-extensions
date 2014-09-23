package com.skysea.group;

/**
 * 申请事件监听器。
 * Created by zhangzhi on 2014/9/22.
 */
public interface ApplyEventListener {

    /**
     * 当新的用户申请到达。
     * @param groupJid 申请加入的圈子jid。
     * @param id 申请事务id。
     * @param from 申请者jid。
     * @param reason 申请验证消息。
     */
    void applyArrived(String groupJid, String id, String from, String reason);

    /**
     * 当申请已经被处理。
     * @param groupJid 申请加入的圈子jid。
     * @param agree 处理人是否同意加入。
     * @param from 处理人jid。
     * @param reason 处理人附言。
     */
    void applyProcessed(String groupJid, boolean agree, String from, String reason);
}
