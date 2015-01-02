package com.skysea.group;

import com.skysea.group.packet.MemberPacketExtension;
import mockit.Delegate;
import mockit.Mocked;
import mockit.Verifications;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.HashMap;

public class GroupTest extends GroupTestBase {
    private DataForm createForm;
    private Group group;
    @Mocked GroupEventListener ownerListener;
    @Mocked GroupEventListener userListener;
    @Mocked PacketListener packetListener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        groupService.addGroupEventListener(ownerListener);

        /* 每个TestCase运行之前，都创建一个新的圈子 */
        createForm = GroupServiceTest.getCreateForm();
        group = groupService.create(createForm);
    }

    public void testCreateGroup() throws Exception {
        // Arrange
        final DataForm form = GroupServiceTest.getCreateForm();

        // Act
        final Group retGroup = groupService.create(form);

        // Assert
        new Verifications() {
            {
                ownerListener.created(retGroup.getJid(), form);
                times = 1;
            }
        };
    }

    public void testGetInfo() throws Exception {
        // Arrange
        Form expectForm = new Form(createForm);

        // Act
        Form actualForm = new Form(group.getInfo());

        // Assert
        assertFormEquals(expectForm.getDataFormToSend(), actualForm.getDataFormToSend());
        assertNotNull(actualForm.getField("createTime").getValues().get(0));
        assertEquals(group.getJid(), actualForm.getField("jid").getValues().get(0));

    }

    public void testUpdateInfo() throws Exception {
        // Arrange
        DataForm updateForm = new DataForm("submit");

        FormField field = new FormField("name");
        updateForm.addField(field);
        field.addValue("new name");

        field = new FormField("subject");
        updateForm.addField(field);
        field.addValue("new subject");

        field = new FormField("description");
        updateForm.addField(field);
        field.addValue("new description");

        field = new FormField("category");
        updateForm.addField(field);
        field.addValue("2");

        field = new FormField("openness");
        updateForm.addField(field);
        field.addValue("AFFIRM_REQUIRED");

        // Act
        group.updateInfo(updateForm);

        // Assert
        assertFormEquals(updateForm, group.getInfo());
    }

    private void assertFormEquals(DataForm a, DataForm b) {
        Form actualForm = new Form(b);
        for (FormField field : a.getFields()) {
            assertEquals(
                    field.getValues().get(0),
                    actualForm.getField(field.getVariable()).getValues().get(0));
        }
    }

    public void testDestroy() throws Exception {
        // Arrange
        final String reason = "再见吧各位";

        // Act
        group.destroy(reason);

        // Assert
        new Verifications() {
            {
                ownerListener.destroyed(group.getJid(), testContext.getUserJid(), reason);
                times = 1;
            }
        };
        try {
            group.getInfo();
        } catch (XMPPException.XMPPErrorException exp) {
            assertEquals("item-not-found", exp.getXMPPError().getCondition());
            return;
        }
        fail();
    }


    public void testKick() throws Exception {
        // Arrange
        final String kickReason = "测试踢出";
        ApplyHelper applyHelper = new ApplyHelper(testContext);
        applyHelper.getUserTestContext().getGroupService().addGroupEventListener(userListener);

        final MemberInfo joinedMember = applyHelper.applyToJoin(group, "我想加入");
        assertMember(group, joinedMember, true);

        // Act
        group.kick(joinedMember.getUserName(), kickReason);

        // Assert
        assertMember(group, joinedMember, false);
        new Verifications() {
            {
                ownerListener.memberKicked(group.getJid(), joinedMember, withPrefix(testContext.getUserJid()), null);
                times = 1;

                /* 只有被踢出者才能收到reason */
                userListener.memberKicked(group.getJid(), joinedMember, withPrefix(testContext.getUserJid()), kickReason);
                times = 1;
            }
        };
    }

    public void testInviteToJoin() throws Exception {
        // Arrange
        GroupTestContext otherUser = new GroupTestContext(testContext);
        otherUser.initialize();
        otherUser.bindUser();
        MemberInfo member = new MemberInfo(otherUser.getUserName(), "独孤求败");

        // Act
        group.inviteToJoin(member.getUserName(), member.getNickname());

        // Assert
        assertMember(group, member, true);
    }


    public void testExit() throws Exception {
        // Arrange
        final String exitReason = "测试退出";
        ApplyHelper applyHelper = new ApplyHelper(testContext);
        final MemberInfo joinedMember = applyHelper.applyToJoin(group, "我想加入");
        assertMember(group, joinedMember, true);

        // Act
        applyHelper.exit(group, exitReason);

        // Assert
        assertMember(group, joinedMember, false);
        new Verifications() {
            {
                ownerListener.memberExited(group.getJid(), joinedMember, exitReason);
                times = 1;
            }
        };
    }

    public void testChangeNickname() throws Exception {
        // Arrange
        final String newNickname = "cooper";
        final MemberInfo changeBefore = new MemberInfo(testContext.getUserName(), testContext.getUserName());
        assertMember(group, changeBefore, true);

        // Act
        group.changeNickname(newNickname);

        // Assert
        assertMember(group, changeBefore, false);
        MemberInfo changeAfter = new MemberInfo(testContext.getUserName(), newNickname);
        assertMember(group, changeAfter, true);

        new Verifications() {
            {
                ownerListener.memberNicknameChanged(group.getJid(), changeBefore, newNickname);
                times = 1;
            }
        };
    }

    public void testGetMembers() throws Exception {
        // Arrange

        // Act
        DataForm resultForm = group.getMembers();

        // Assert
        assertMember(group, new MemberInfo(testContext.getUserName(), testContext.getUserName()), true);
    }


    public void testSend() throws Exception {
        // Arrange
        testContext.getConnection().addPacketListener(packetListener, new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                return packet instanceof Message && ((Message) packet).getType() == Message.Type.groupchat;
            }
        });

        final Message msg = new Message();
        msg.setType(Message.Type.groupchat);
        msg.setBody("hello buddy.");

        // Act
        group.send(msg);

        // Assert
        Thread.sleep(100);
        new Verifications() {
            {
                packetListener.processPacket(with(new Delegate<Packet>() {
                    public void validate(Packet packet) {
                        MemberPacketExtension packetExt = packet.getExtension(
                                MemberPacketExtension.ELEMENT_NAME,
                                MemberPacketExtension.NAMESPACE);
                        MemberInfo memberInfo = packetExt.getMemberInfo();

                        assertEquals(group.getJid() + "/" + testContext.getUserName(), packet.getFrom());
                        assertEquals(((Message) packet).getBody(), msg.getBody());
                        assertEquals(testContext.getUserName(), memberInfo.getNickname());
                    }
                }));
                times = 1;
            }
        };
    }

    public void testGetGroup() throws Exception {
        // Arrange
        String jid = "12@group.skysea.com";

        // Act
        Group group = groupService.getGroup(jid);

        // Assert
        assertEquals(jid, group.getJid());
    }

    protected static void assertMember(Group group, MemberInfo user, boolean has) throws Exception {
        boolean found = false;
        for (DataForm.Item item : group.getMembers().getItems()) {
            HashMap<String, String> rows = mapFields(item);
            if (rows.get("username").equalsIgnoreCase(user.getUserName()) &&
                    rows.get("nickname").equalsIgnoreCase(user.getNickname())) {
                found = true;
            }
        }
        assertEquals(has, found);
    }

    private static HashMap<String, String> mapFields(DataForm.Item item) {
        HashMap<String, String> maps = new HashMap<String, String>(item.getFields().size());
        for (FormField field : item.getFields()) {
            maps.put(field.getVariable(), field.getValues().get(0));
        }
        return maps;
    }


    public static class GroupApplyTest extends GroupTestBase {
        private String applyReason = "我希望加入";
        private Group group;
        @Mocked GroupEventListener ownerListener;
        @Mocked GroupEventListener userListener;
        private ApplyHelper applyHelper;

        @Override
        protected void setUp() throws Exception {
            super.setUp();

            applyHelper = new ApplyHelper(testContext);
            applyHelper.getUserTestContext().getGroupService().addGroupEventListener(userListener);
            testContext.getGroupService().addGroupEventListener(ownerListener);
        }

        public void testProcessApply_When_Owner_Agree() throws Exception {
            // Arrange
            createHalfOpenGroup();
            //groupService.addGroupEventListener(ownerListener);

            final MemberInfo applyMember = applyHelper.applyToJoin(group, applyReason);

            // Act
            group.processApply("testid", applyMember.getUserName(), applyMember.getNickname(), true, "welcome");

            // Assert
            assertMember(group, applyMember, true);
            new Verifications() {
                {
                    ownerListener.applyArrived(group.getJid(), anyString, applyMember,  applyReason);
                    times = 1;

                    userListener.applyProcessed(group.getJid(), true, testContext.getUserJid(), "welcome");
                    times = 1;

                    ownerListener.memberJoined(group.getJid(), applyMember);
                    times = 1;

                    userListener.memberJoined(group.getJid(), applyMember);
                    times = 1;
                }
            };
        }

        public void testProcessApply_When_Owner_Decline() throws Exception {
            // Arrange
            createHalfOpenGroup();
            final MemberInfo applyMember = applyHelper.applyToJoin(group, applyReason);


            // Act
            group.processApply("testid", applyMember.getUserName(), applyMember.getNickname(), false, "sorry");

            // Assert
            assertMember(group, applyMember, false);
            new Verifications() {
                {
                    ownerListener.applyArrived(group.getJid(), anyString, applyMember, applyReason);
                    times = 1;

                    userListener.applyProcessed(group.getJid(), false, testContext.getUserJid(), "sorry");
                    times = 1;
                }
            };
        }

        public void testApplyToJoin_When_Group_Is_Public() throws Exception {
            // Arrange
            createOpenGroup();
            //groupService.addGroupEventListener(ownerListener);


            // Act
            final MemberInfo joinedMember = applyHelper.applyToJoin(group, applyReason);

            // Assert
            assertMember(group, joinedMember, true);
            new Verifications() {
                {
                    ownerListener.memberJoined(group.getJid(), joinedMember);
                    times = 1;

                    userListener.memberJoined(group.getJid(), joinedMember);
                    times = 1;

                    userListener.applyProcessed(group.getJid(), true, group.getJid(), null);
                    times = 1;
                }
            };
        }

        private void createOpenGroup() throws Exception {
            group = groupService.create(GroupServiceTest.getCreateForm());
        }

        private void createHalfOpenGroup() throws Exception {
            Form form = new Form(GroupServiceTest.getCreateForm());
            // 设置开放程度为：需要圈子所有者确认
            FormField field = form.getField("openness");
            field.setType(FormField.TYPE_TEXT_SINGLE);
            form.setAnswer("openness", (String) "AFFIRM_REQUIRED");
            group = groupService.create(form.getDataFormToSend());
        }
    }

}