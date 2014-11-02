package com.skysea.group.packet.notify;

import com.skysea.XmlPullPaserTestBase;
import org.xmlpull.v1.XmlPullParser;

/**
* Created by apple on 14-9-24.
*/
public class NotifyParserTest extends XmlPullPaserTestBase {

    public void testIsAccept() throws Exception {
        assertTrue(NotifyParser.isAccept("join"));
        assertTrue(NotifyParser.isAccept("exit"));
        assertTrue(NotifyParser.isAccept("kick"));
        assertTrue(NotifyParser.isAccept("profile"));
        assertTrue(NotifyParser.isAccept("apply"));
        assertTrue(NotifyParser.isAccept("apply"));
        assertTrue(NotifyParser.isAccept("destroy"));
        assertFalse(NotifyParser.isAccept("history"));
    }
    public void testParse_When_Member_Joined() throws Exception {
        // Arrange && Act
        MemberJoinedNotify notify = (MemberJoinedNotify)parser(
                "http://skysea.com/protocol/group#member",
                "<join><member username='user' nickname='碧眼狐狸' /></join>");


        // Assert
        assertEquals(Notify.Type.MEMBER_JOINED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
    }

    public void testParse_When_Member_Exited() throws Exception {
        // Arrange && Act
        MemberExitedNotify notify =  (MemberExitedNotify)parser(
                "http://skysea.com/protocol/group#member",
                "<exit>\n" +
                "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                "  \t\t<reason>大家太吵了，不好意思，我退了先！</reason>\n" +
                "  \t</exit>");

        // Assert
        assertEquals(Notify.Type.MEMBER_EXITED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("大家太吵了，不好意思，我退了先！", notify.getReason());
    }

    public void testParse_When_Member_Kicked() throws Exception {
        // Arrange && Act
        MemberKickedNotify notify = (MemberKickedNotify)parser(
                "http://skysea.com/protocol/group#member",
                "<kick from='owner@skysea.com'>\n" +
                        "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                        "  \t\t<reason>抱歉！你总是发送广告信息。</reason>\n" +
                        "  \t</kick>");

        // Assert
        assertEquals(Notify.Type.MEMBER_KICKED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("抱歉！你总是发送广告信息。", notify.getReason());
        assertEquals("owner@skysea.com", notify.getFrom());
    }


    public void testParse_When_Member_Profile_Changed() throws Exception {
        // Arrange && Act
        MemberProfileChangedNotify notify = (MemberProfileChangedNotify)parser(
                "http://skysea.com/protocol/group#member",
                "  \t<profile>\n" +
                        "  \t\t<member username='user' nickname='碧眼狐狸' />\n" +
                        "  \t\t<nickname>金轮法王</nickname>\n" +
                        "  \t</profile>");

        // Assert
        assertEquals(Notify.Type.MEMBER_PROFILE_CHANGED, notify.getType());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
        assertEquals("金轮法王", notify.getNewNickname());
    }

    public void testParse_When_Member_Apply_To_Join() throws Exception {
        // Arrange && Act
        MemberApplyToJoinNotify notify = (MemberApplyToJoinNotify)parser(
                "http://skysea.com/protocol/group#owner",
                "<apply id='s2fd1'>\n" +
                        "<member username='user' nickname='碧眼狐狸' />"+
                        "  \t\t<reason>我也是80后，请让我加入吧！</reason>\n" +
                        "  \t</apply>");
        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN, notify.getType());
        assertEquals("s2fd1", notify.getId());
        assertEquals("我也是80后，请让我加入吧！", notify.getReason());
        assertEquals("user", notify.getMemberInfo().getUserName());
        assertEquals("碧眼狐狸", notify.getMemberInfo().getNickname());
    }

    public void testParse_When_Member_Apply_To_Join_Pass() throws Exception {
        // Arrange && Act
        MemberApplyResultNotify notify = (MemberApplyResultNotify)parser(
                "http://skysea.com/protocol/group#user",
                "<apply>\n" +
                        "\t\t<agree from='owner@skysea.com' />\n" +
                        "    \t<reason>欢迎加入！</reason>\n" +
                        "    </apply>");

        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertTrue(notify.getResult());
        assertEquals("欢迎加入！", notify.getReason());
    }

    public void testParse_When_Member_Apply_To_Join_NoPass() throws Exception {
        // Arrange && Act
        MemberApplyResultNotify notify = (MemberApplyResultNotify)parser(
                "http://skysea.com/protocol/group#user",
                "<apply>\n" +
                        "\t\t<decline from='owner@skysea.com' />\n" +
                        "    \t<reason>不好意思，人满为患！</reason>\n" +
                        "    </apply>");

        // Assert
        assertEquals(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertFalse(notify.getResult());
        assertEquals("不好意思，人满为患！", notify.getReason());
    }

    public void testParse_When_Group_Destroy() throws Exception {
        // Arrange && Act
        GroupDestroyNotify notify = (GroupDestroyNotify)parser(
                "http://skysea.com/protocol/group",
                "<destroy from='owner@skysea.com'>\n" +
                        "  \t\t<reason>再见了各位！</reason>\n" +
                        "  \t</destroy>");
        // Assert
        assertEquals(Notify.Type.GROUP_DESTROY, notify.getType());
        assertEquals("owner@skysea.com", notify.getFrom());
        assertEquals("再见了各位！", notify.getReason());
    }



    private Notify parser(String namespace, String xml) throws Exception {
        XmlPullParser xmlPullParser = xmlParser(xml);
        xmlPullParser.next();
        return  NotifyParser.parse(xmlPullParser, namespace);
    }


}
