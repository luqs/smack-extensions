package com.skysea.group.provider;

import com.skysea.XmlPullPaserTestBase;
import com.skysea.group.packet.MemberPacketExtension;
import com.skysea.group.packet.NotifyPacketExtension;
import com.skysea.group.packet.notify.MemberExitedNotify;
import org.xmlpull.v1.XmlPullParser;

public class NotifyPacketExtensionProviderTest extends XmlPullPaserTestBase {

    private NotifyPacketExtensionProvider provider;

    @Override
    protected void setUp() throws Exception {
        provider = new NotifyPacketExtensionProvider();
    }

    public void testParseNotifyExtension() throws Exception {
        // Arrange
        XmlPullParser parser = xmlParser("<x xmlns='http://skysea.com/protocol/group#member'>\n" +
                "  \t<exit>\n" +
                "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                "  \t\t<reason>大家太吵了，不好意思，我退了先！</reason>\n" +
                "  \t</exit>\n" +
                "  </x>\n");
        parser.next();

        // Act
        NotifyPacketExtension packet = (NotifyPacketExtension)provider.parseExtension(parser);

        // Assert
        assertEquals("x", packet.getElementName());
        assertEquals("http://skysea.com/protocol/group#member", packet.getNamespace());

        MemberExitedNotify notify = (MemberExitedNotify)packet.getNotify();
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertNotNull("大家太吵了，不好意思，我退了先！", notify.getReason());
    }

    public void testParseMemberExtension() throws Exception {
        // Arrange
        XmlPullParser parser = xmlParser("<x xmlns='http://skysea.com/protocol/group#member'>\n" +
                "    <member nickname='碧眼狐狸' username='user' />\n" +
                "  </x>");
        parser.next();

        // Act
        MemberPacketExtension packet = (MemberPacketExtension)provider.parseExtension(parser);

        // Assert
        assertEquals("x", packet.getElementName());
        assertEquals("http://skysea.com/protocol/group#member", packet.getNamespace());
        assertEquals("碧眼狐狸", packet.getMemberInfo().getNickname());
        assertEquals("user", packet.getMemberInfo().getUserName());
    }
}