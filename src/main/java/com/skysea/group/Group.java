package com.skysea.group;

import com.skysea.group.packet.QueryPacket;
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
