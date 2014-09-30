package com.skysea.group;

import com.skysea.group.packet.notify.*;
import junit.framework.TestCase;
import mockit.Mocked;
import mockit.Verifications;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class EventDispatcherTest extends TestCase {

    public static final String GROUP_JID = "100@group.skysea.com";
    public static final String OWNER_JID = "owner@skysea.com";
    public static final MemberInfo MEMBER_INFO = new MemberInfo("user", "碧眼狐狸");

    @Mocked GroupEventListener listener;
    @Mocked XMPPConnection connection;
    private EventDispatcher dispatcher;

    @Override
    protected void setUp() {

        assert connection != null;
        dispatcher = new EventDispatcher(connection, "group.skysea.com");
        dispatcher.addEventListener(listener);
    }


    public void testAddEventListeners() throws Exception {
        assertEquals(1, dispatcher.getEventListeners().size());
        assertTrue(dispatcher.getEventListeners().contains(listener));
        dispatcher.removeEventListener(listener);
        assertEquals(0, dispatcher.getEventListeners().size());
    }


    public void testDispatch_When_Member_Apply_Arrived() throws Exception {
        // Arrange
        final ApplyNotify notify = new ApplyNotify();
        notify.setMemberInfo(MEMBER_INFO);
        notify.setId("123");
        notify.setReason("我想加入");

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.applyArrived(
                        GROUP_JID,
                        notify.getId(),
                        MEMBER_INFO,
                        notify.getReason());
                times = 1;
            }
        };
    }

    public void testDispatch_When_Member_Apply_Result_Arrived() throws Exception {
        // Arrange
        final ApplyResultNotify notify = new ApplyResultNotify();
        notify.setFrom(OWNER_JID);
        notify.setResult(true);
        notify.setReason("欢迎加入");

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.applyProcessed(
                        GROUP_JID,
                        notify.getResult(),
                        notify.getFrom(),
                        notify.getReason());
                times = 1;
            }
        };
    }

    public void testDispatch_Group_Created() throws Exception {
        // Arrange
        final DataForm form = new DataForm(Form.TYPE_SUBMIT);

        // Act
        dispatcher.dispatchCreate(GROUP_JID, form);

        // Assert
        new Verifications() {
            {
                listener.created(
                        GROUP_JID,
                        form);
                times = 1;
            }
        };
    }

    public void testDispatch_When_Group_Destroyed() throws Exception {
        // Arrange
        final GroupDestroyNotify notify = new GroupDestroyNotify();
        notify.setFrom(OWNER_JID);
        notify.setReason("没意思了");

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.destroyed(
                        GROUP_JID,
                        notify.getFrom(),
                        notify.getReason());
                times = 1;
            }
        };
    }

    public void testDispatch_When_Member_Profile_Changed() throws Exception {
        // Arrange
        final ProfileChangedNotify notify = new ProfileChangedNotify();
        notify.setMemberInfo(MEMBER_INFO);
        notify.setNewNickname("金轮法王");

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.memberNicknameChanged(
                        GROUP_JID,
                        notify.getMemberInfo(),
                        notify.getNewNickname());
                times = 1;
            }
        };
    }

    public void testDispatch_When_Member_Exited() throws Exception {
        // Arrange
        final MemberEventNotify notify = new MemberEventNotify(Notify.Type.MEMBER_EXITED);
        notify.setMemberInfo(MEMBER_INFO);
        notify.setReason("再见吧");

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.memberExited(
                        GROUP_JID,
                        notify.getMemberInfo(),
                        notify.getReason());
                times = 1;
            }
        };
    }

    public void testDispatch_When_Member_Joined() throws Exception {
        // Arrange
        final MemberEventNotify notify = new MemberEventNotify(Notify.Type.MEMBER_JOINED);
        notify.setMemberInfo(MEMBER_INFO);

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.memberJoined(
                        GROUP_JID,
                        notify.getMemberInfo());
                times = 1;
            }
        };
    }

    public void testDispatch_When_Member_Kicked() throws Exception {
        // Arrange
        final KickedNotify notify = new KickedNotify();
        notify.setFrom(OWNER_JID);
        notify.setMemberInfo(MEMBER_INFO);

        // Act
        dispatcher.dispatch(GROUP_JID, notify);

        // Assert
        new Verifications() {
            {
                listener.memberKicked(
                        GROUP_JID,
                        notify.getMemberInfo(),
                        notify.getFrom(),
                        notify.getReason());
                times = 1;
            }
        };
    }


}