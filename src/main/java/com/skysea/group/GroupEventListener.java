package com.skysea.group;

import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * 圈子事件监听器。
 * Created by zhangzhi on 2014/9/22.
 */
public interface GroupEventListener {

    /**
     * 当圈子已被创建。
     * @param groupJid 新创建的圈子jid。
     * @param createForm 圈子创建表单。
     */
    void created(String groupJid, DataForm createForm);

    /**
     * 当圈子已被销毁。
     * @param groupJid 被销毁的圈子jid。
     * @param from 操作人jid。
     * @param reason 销毁原因。
     */
    void destroy(String groupJid, String from, String reason);
}
