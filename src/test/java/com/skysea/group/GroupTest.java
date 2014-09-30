package com.skysea.group;

import com.skysea.GroupTestBase;
import com.skysea.XmppTestConnection;
import com.skysea.group.packet.MemberPacketExtension;
import mockit.Delegate;
import mockit.Mocked;
import mockit.Verifications;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.HashMap;

public class GroupTest extends GroupTestBase {
    private DataForm createForm;
    private Group group;
    @Mocked GroupEventListener listener;
    @Mocked PacketListener packetListener;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createForm = GroupServiceTest.getCreateForm();
        group = groupService.create(createForm);
        groupService.addGroupEventListener(listener);
    }

    public void testGetInfo() throws Exception {
        // Arrange
        Form expectForm = new Form(createForm);

        // Act
        Form actualForm = new Form(group.getInfo());

        // Assert
        for (FormField field : expectForm.getFields()) {
            assertEquals(
                    field.getValues().get(0),
                    actualForm.getField(field.getVariable()).getValues().get(0));
        }
        assertNotNull(actualForm.getField("id").getValues().get(0));
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
        new Verifications(){
            {
                listener.destroyed(group.getJid(), anyString, reason);
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
        NewUserTestHelper helper = new NewUserTestHelper(testConnection, groupService.getServiceDomain());
        final String otherUserName = helper.bindUser();
        helper.applyToJoinGroup(group.getJid(), null, "我想加入啊");

        Thread.sleep(100);
        assertMember(group, otherUserName, true);

        // Act
        group.kick(otherUserName, "测试踢出");
        Thread.sleep(100);

        // Assert
        assertMember(group, otherUserName, false);
        new Verifications(){
            {
                listener.memberKicked(group.getJid(), with(new Delegate<MemberInfo>() {
                    public void validate(MemberInfo memberInfo) {
                        assertEquals(otherUserName, memberInfo.getUserName());
                    }
                }), anyString, null);
                times = 1;
            }
        };
    }


    public void testExit() throws Exception {
        // Arrange
        NewUserTestHelper helper = new NewUserTestHelper(testConnection, groupService.getServiceDomain());
        final String otherUserName = helper.bindUser();
        Group theGroup = helper.applyToJoinGroup(group.getJid(), null, "我想加入啊");

        Thread.sleep(100);
        assertMember(group, otherUserName, true);

        // Act
        theGroup.exit("测试退出");
        Thread.sleep(100);

        // Assert
        assertMember(group, otherUserName, false);
        new Verifications(){
            {
                listener.memberExited(group.getJid(), with(new Delegate<MemberInfo>() {
                    public void validate(MemberInfo memberInfo) {
                        assertEquals(otherUserName, memberInfo.getUserName());
                    }
                }), "测试退出");
                times = 1;
            }
        };
    }

    public void testChangeNickname() throws Exception {
        // Arrange
        final String newNickname = "cooper";

        // Act
        group.changeNickname(newNickname);

        // Assert
        String actualNickname = null;
        for (DataForm.Item item : group.getMembers().getItems()) {

            HashMap<String, FormField> fields = new HashMap<String, FormField>();
            for (FormField field : item.getFields()) {
                fields.put(field.getVariable(), field);
            }

            FormField field = fields.get("username");
            if (field.getValues().get(0).equalsIgnoreCase(testUserName)) {
                actualNickname = fields.get("nickname").getValues().get(0);
            }
        }

        assertEquals(newNickname, actualNickname);
        new Verifications(){
            {
                listener.memberNicknameChanged(group.getJid(), with(new Delegate<MemberInfo> (){
                    public void validate(MemberInfo memberInfo) {
                        assertEquals(testUserName, memberInfo.getUserName());
                        assertEquals(testUserName, memberInfo.getNickname());
                    }
                }), newNickname);
                times = 1;
            }
        };
    }

    public void testGetMembers() throws Exception {
        // Arrange

        // Act
        DataForm resultForm = group.getMembers();

        // Assert
        assertMember(group, testUserName, true);
    }


    public void testSend() throws Exception {
        // Arrange
        testConnection.getConnection().addPacketListener(packetListener, new PacketFilter() {
            @Override
            public boolean accept(Packet packet) {
                return packet instanceof Message && ((Message)packet).getType()== Message.Type.groupchat;
            }
        });

        final Message msg = new Message();
        msg.setType(Message.Type.groupchat);
        msg.setBody("hello buddy.");

        // Act
        group.send(msg);

        // Assert
        Thread.sleep(100);
        new Verifications(){
            {
                packetListener.processPacket(with(new Delegate<Packet>() {
                    public void validate(Packet packet) {
                        MemberPacketExtension packetExt = (MemberPacketExtension)packet.getExtensions().toArray()[0];

                        assertEquals(group.getJid() + "/" + testUserName, packet.getFrom());
                        assertEquals(((Message)packet).getBody(), msg.getBody());

                        //assertEquals(testUserName, packetExt.getMemberInfo().getUserName());
                        assertEquals(testUserName, packetExt.getMemberInfo().getNickname());
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

    protected static void assertMember(Group group, String user, boolean has) throws Exception {
        boolean found = false;
        for (DataForm.Item item : group.getMembers().getItems()) {
            for (FormField field : item.getFields()) {
                if ("username".equals(field.getVariable()) && field.getValues().get(0).equalsIgnoreCase(user)) {
                    found = true;
                }
            }
        }
        assertEquals(has, found);
    }


    public static class GroupApplyTest extends GroupTestBase {
        private Group group;
        private String otherNickName;
        private String applyReason;
        private String otherUserName;
        private String otherUserNameJid;
        @Mocked GroupEventListener ownerListener;
        @Mocked GroupEventListener userListener;
        private NewUserTestHelper userHelper;
        private String ownerJid;

        @Override
        protected void setUp() throws Exception {
            super.setUp();
            userHelper = new NewUserTestHelper(testConnection, groupService.getServiceDomain());
            userHelper.getGroupService().addGroupEventListener(userListener);

            otherUserName = userHelper.bindUser().toLowerCase();
            otherUserNameJid = otherUserName + "@" + testConnection.getXmppDomain();
            otherNickName = "@" + otherUserName + "@";
            applyReason = "我想加入啊";

            ownerJid = testUserName.toLowerCase() + "@" + testConnection.getXmppDomain();
        }

        public void testProcessApply_When_Owner_Agree() throws Exception {
            // Arrange
            createHalfOpenGroup();
            groupService.addGroupEventListener(ownerListener);

            autoApplyToJoin();

            // Act
            group.processApply("testid", otherUserName, otherNickName, true, "welcome");

            // Assert
            assertMember(group, otherUserName, true);
            new Verifications() {
                {
                    ownerListener.applyArrived(group.getJid(), anyString, with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo memberInfo) {
                            assertEquals(otherUserName, memberInfo.getUserName());
                            assertEquals(otherNickName, memberInfo.getNickname());
                        }
                    }), "我想加入啊");
                    times = 1;

                    userListener.applyProcessed(group.getJid(), true, with(new Delegate<String>() {
                        public void validate(String jid) {
                            assertEquals(ownerJid, StringUtils.parseBareAddress(jid));
                        }
                    }), "welcome");
                    times = 1;

                    ownerListener.memberJoined(group.getJid(), with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo info) {
                            assertEquals(otherUserName, info.getUserName());
                            assertEquals(otherNickName, info.getNickname());
                        }
                    }));
                    times = 1;

                    userListener.memberJoined(group.getJid(), with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo info) {
                            assertEquals(otherUserName, info.getUserName());
                            assertEquals(otherNickName, info.getNickname());
                        }
                    }));
                    times = 1;
                }
            };
        }

        public void testProcessApply_When_Owner_Decline() throws Exception {
            // Arrange
            createHalfOpenGroup();
            groupService.addGroupEventListener(ownerListener);

            autoApplyToJoin();

            // Act
            group.processApply("testid", otherUserName, otherNickName, false, "sorry");

            // Assert
            assertMember(group, otherUserName, false);
            new Verifications() {
                {
                    ownerListener.applyArrived(group.getJid(), anyString, with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo memberInfo) {
                            assertEquals(otherUserName, memberInfo.getUserName());
                            assertEquals(otherNickName, memberInfo.getNickname());
                        }
                    }), "我想加入啊");
                    times = 1;

                    userListener.applyProcessed(group.getJid(), false, with(new Delegate<String>() {
                    public void validate(String jid) {
                        assertEquals(ownerJid, StringUtils.parseBareAddress(jid));
                    }
                    }), "sorry");
                    times = 1;
                }
            };
        }

        public void testApplyToJoin_When_Group_Is_Public() throws Exception {
            // Arrange
            createOpenGroup();
            groupService.addGroupEventListener(ownerListener);


            // Act
            autoApplyToJoin();
            Thread.sleep(100);

            // Assert
            assertMember(group, otherUserName, true);
            new Verifications() {
                {
                    ownerListener.memberJoined(group.getJid(), with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo info) {
                            assertEquals(otherUserName, info.getUserName());
                            assertEquals(otherNickName, info.getNickname());
                        }
                    }));
                    times = 1;

                    userListener.memberJoined(group.getJid(), with(new Delegate<MemberInfo>() {
                        public void validate(MemberInfo info) {
                            assertEquals(otherUserName, info.getUserName());
                            assertEquals(otherNickName, info.getNickname());
                        }
                    }));
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

        private void autoApplyToJoin() throws Exception {
            userHelper.applyToJoinGroup(group.getJid(), otherNickName, "我想加入啊");
        }

    }

    static class NewUserTestHelper {
        private final XmppTestConnection connection;
        private String userName;
        private GroupService groupService1;

        public NewUserTestHelper(XmppTestConnection baseConnection, String groupServiceDomain) throws Exception {
            connection = new XmppTestConnection(baseConnection);
            connection.connect();
            groupService1 = new GroupService(connection.getConnection(), groupServiceDomain);
        }

        public String bindUser() throws Exception {
            if (userName != null) {
                throw new IllegalStateException("already bind.");
            }
            return userName = connection.createTestUserAndLogin();
        }

        public String getUserName() {
            return userName;
        }

        public GroupService getGroupService() {
            return groupService1;
        }

        public Group applyToJoinGroup(String jid, String nickname, String reason) throws Exception {
            Group group = groupService1.getGroup(jid);
            group.applyToJoin(nickname, reason);
            return group;
        }
    }
}