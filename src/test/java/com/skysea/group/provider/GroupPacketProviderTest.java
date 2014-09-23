package com.skysea.group.provider;

import com.skysea.XmlPullPaserTestBase;
import com.skysea.group.packet.QueryPacket;
import com.skysea.group.packet.XPacket;
import com.skysea.group.packet.notify.MemberEventNotify;
import org.jivesoftware.smack.SmackConfiguration;
import org.xmlpull.v1.XmlPullParser;

public class GroupPacketProviderTest extends XmlPullPaserTestBase {
    private GroupPacketProvider packetProvider;

    @Override
    protected void setUp() {
        packetProvider = new GroupPacketProvider();
        SmackConfiguration.getVersion();
    }

    public void testParse_Query_IQ_Packet() throws Exception {
        // Arrange
        XmlPullParser parser = xmlParser("<query xmlns='http://skysea.com/protocol/group' node='info'>\n" +
                "  \t<x xmlns='jabber:x:data' type='result'>\n" +
                "\t\t<field var='id'> <value>100</value> </field>\n" +
                "\t\t<field var='jid'> <value>100@group.skysea.com</value> </field>\n" +
                "\t\t<field var='owner'> <value>admin</value> </field>\n" +
                "\t\t<field var='name'> <value>一起狂欢</value> </field>\n" +
                "\t\t<field var='num_members'> <value>100</value> </field>\n" +
                "\t\t<field var='subject'> <value>今晚一醉方休！</value> </field>\n" +
                "\t\t<field var='description'> <value>欢迎80，90，00后的少年们的加入！</value> </field>\n" +
                "\t\t<field var='openness'> <value>PUBLIC</value> </field>\n" +
                "\t\t<field var='createTime'> <value>2001-07-04T12:08:56Z</value> </field>\n" +
                "\t</x>\n" +
                "  </query>");
        parser.next();

        // Act
        QueryPacket packet = (QueryPacket) packetProvider.parseIQ(parser);

        // Assert
        assertNotNull(packet.getDataForm());
    }




}