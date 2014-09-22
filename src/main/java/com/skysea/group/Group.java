package com.skysea.group;

import com.skysea.group.packet.GenericOperate;
import com.skysea.group.packet.Operate;
import com.skysea.group.packet.QueryPacket;
import com.skysea.group.packet.XPacket;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * 圈子对象类，代表某具体的圈子。
 * Created by zhangzhi on 2014/9/18.
 */
public final class Group {
    private final XMPPConnection connection;
    private String jid;

    Group(XMPPConnection connection, String jid) {
        assert connection != null;
        assert jid != null;

        this.connection = connection;
        this.jid = jid;
    }

    /**
     * 获得圈子详情。
     * @return 圈子详情表单对象。
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public DataForm getInfo() throws
            SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {

        QueryPacket packet = new QueryPacket(GroupService.GROUP_NAMESPACE, "info");

        packet = (QueryPacket)request(packet);
        return packet.getDataForm();
    }

    /**
     * 更新圈子信息。
     * @param form 圈子更新表单对象。
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void updateInfo(DataForm form) throws
            SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {

        if(form == null){ throw new NullPointerException("form is null."); }

        XPacket packet = new XPacket(GroupService.GROUP_NAMESPACE, form);
        request(packet);
    }

    /**
     * 销毁圈子。
     * @param reason 销毁的原因。
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public void destroy(String reason) throws
            SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {

        Operate destroyOpe = new GenericOperate(Operate.DESTROY);
        destroyOpe.setReason(reason);
        XPacket packet = new XPacket(GroupService.GROUP_OWNER, destroyOpe);
        request(packet);
    }

    /**
     * 获得圈子成员列表。
     * @return
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public DataForm getMembers() throws
            SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {

        QueryPacket packet = new QueryPacket(GroupService.GROUP_NAMESPACE, "members");

        packet = (QueryPacket)request(packet);
        return packet.getDataForm();
    }


    private Packet request(IQ packet) throws
    SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {
        packet.setTo(jid);
       return connection.createPacketCollectorAndSend(packet).nextResultOrThrow();
    }

    public String getJid() {
        return jid;
    }

}
