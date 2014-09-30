package com.skysea.group.packet.notify;

import com.skysea.XmlPullPaserTestBase;
import org.xmlpull.v1.XmlPullParser;

/**
* Created by apple on 14-9-24.
*/
public class NotifyParserTest extends XmlPullPaserTestBase {

    public void testIsAccept() throws Exception {
        assertTrue(NotifyParser.isAccept("join", "http://skysea.com/protocol/group#member"));
        assertTrue(NotifyParser.isAccept("exit", "http://skysea.com/protocol/group#member"));
        assertTrue(NotifyParser.isAccept("kick", "http://skysea.com/protocol/group#member"));
        assertTrue(NotifyParser.isAccept("profile", "http://skysea.com/protocol/group#member"));
        assertTrue(NotifyParser.isAccept("apply", "http://skysea.com/protocol/group#owner"));
        assertTrue(NotifyParser.isAccept("apply", "http://skysea.com/protocol/group#user"));
        assertTrue(NotifyParser.isAccept("destroy", "http://skysea.com/protocol/group"));
        assertFalse(NotifyParser.isAccept("history", "http://skysea.com/protocol/group"));
    }
    public void testParse_When_Member_Joined() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#member",
                "<join><member username='user' nickname='碧眼狐狸' /></join>");

        // Act
        MemberEventNotify notify = (MemberEventNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_JOINED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
    }

    public void testParse_When_Member_Exited() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#member",
                "<exit>\n" +
                "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                "  \t\t<reason>大家太吵了，不好意思，我退了先！</reason>\n" +
                "  \t</exit>");

        // Act
        MemberEventNotify notify = (MemberEventNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_EXITED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("大家太吵了，不好意思，我退了先！", notify.getReason());
    }

    public void testParse_When_Member_Kicked() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#member",
                "<kick from='owner@skysea.com'>\n" +
                        "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                        "  \t\t<reason>抱歉！你总是发送广告信息。</reason>\n" +
                        "  \t</kick>");

        // Act
        KickedNotify notify = (KickedNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_KICKED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("抱歉！你总是发送广告信息。", notify.getReason());
        assertEquals("owner@skysea.com", notify.getFrom());
    }


    public void testParse_When_Member_Profile_Changed() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#member",
                "  \t<profile>\n" +
                        "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                        "  \t\t<nickname>金轮法王</nickname>\n" +
                        "  \t</profile>");

        // Act
        ProfileChangedNotify notify = (ProfileChangedNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_PROFILE_CHANGED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("金轮法王", notify.getNewNickname());
    }

    public void testParse_When_Member_Apply_To_Join() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#owner",
                "<apply id='s2fd1'>\n" +
                        "<member username='user' nickname='碧眼狐狸' />"+
                        "  \t\t<reason>我也是80后，请让我加入吧！</reason>\n" +
                        "  \t</apply>");

        // Act
        ApplyNotify notify = (ApplyNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN, notify.getType());
        assertEquals("s2fd1", notify.getId());
        assertEquals("我也是80后，请让我加入吧！", notify.getReason());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
    }

    public void testParse_When_Member_Apply_To_Join_Pass() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#user",
                "<apply>\n" +
                        "\t\t<agree from='owner@skysea.com' />\n" +
                        "    \t<reason>欢迎加入！</reason>\n" +
                        "    </apply>");

        // Act
        ApplyResultNotify notify = (ApplyResultNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertTrue(notify.getResult());
        assertEquals("欢迎加入！", notify.getReason());
    }

    public void testParse_When_Member_Apply_To_Join_NoPass() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group#user",
                "<apply>\n" +
                        "\t\t<decline from='owner@skysea.com' />\n" +
                        "    \t<reason>不好意思，人满为患！</reason>\n" +
                        "    </apply>");

        // Act
        ApplyResultNotify notify = (ApplyResultNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertFalse(notify.getResult());
        assertEquals("不好意思，人满为患！", notify.getReason());
    }

    public void testParse_When_Group_Destroy() throws Exception {
        // Arrange
        NotifyParser parser = parser(
                "http://skysea.com/protocol/group",
                "<destroy from='owner@skysea.com'>\n" +
                        "  \t\t<reason>再见了各位！</reason>\n" +
                        "  \t</destroy>");

        // Act
        GroupDestroyNotify notify = (GroupDestroyNotify)parser.parse();

        // Assert
        assertEquals(Notify.Type.GROUP_DESTROY, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertEquals("再见了各位！", notify.getReason());
    }


    public static MemberEventNotify createMemberEventNotify(Notify.Type type) {
        return new MemberEventNotify(type);
    }

    private NotifyParser parser(String namespace, String xml) throws Exception {
        XmlPullParser xmlPullParser = xmlParser(xml);
        xmlPullParser.next();
        return new NotifyParser(xmlPullParser, namespace);
    }


}
