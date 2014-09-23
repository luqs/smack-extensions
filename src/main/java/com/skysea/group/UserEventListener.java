package com.skysea.group;

/**
 * 用户事件监听器。
 * Created by zhangzhi on 2014/9/22.
 */
public interface UserEventListener {

    /**
     * 新成员加入事件。
     * @param groupJid 圈子jid。
     * @param member 加入的成员。
     */
    void joined(String groupJid, MemberInfo member);

    /**
     * 圈子成员退出事件。
     * @param groupJid 圈子jid。
     * @param member 退出的成员。
     */
    void exited(String groupJid, MemberInfo member);

    /**
     * 圈子成员被踢出事件。
     * @param groupJid 圈子jid。
     * @param member 踢出的成员。
     * @param from 操作人jid。
     * @param reason 原因。
     */
    void kicked(String groupJid, MemberInfo member, String from, String reason);

    /**
     * 昵称修改事件。
     * @param groupJid 圈子jid。
     * @param member 修改的成员。
     * @param newNickname 新的昵称。
     */
    void nicknameChanged(String groupJid, MemberInfo member, String newNickname);
}
