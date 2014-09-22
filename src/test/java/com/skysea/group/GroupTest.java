package com.skysea.group;

import com.skysea.GroupTestBase;
import com.skysea.XmppTestConnection;
import junit.framework.TestCase;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.util.HashMap;
import java.util.Map;

public class GroupTest extends GroupTestBase {
    private DataForm createForm;
    private Group group;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createForm = GroupServiceTest.getCreateForm();
        group = groupService.create(createForm);
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
        String reason = "再见吧各位";

        // Act
        group.destroy(reason);

        // Assert
        try {
            group.getInfo();
        } catch (XMPPException.XMPPErrorException exp) {
            assertEquals("item-not-found", exp.getXMPPError().getCondition());
            return;
        }
        fail();
    }

    public void testApplyToJoin() throws Exception {
        // Arrange
        NewUserTestHelper helper = new NewUserTestHelper(testConnection);
        String otherUserName = helper.bindUser();

        // Act
        helper.applyToJoinGroup(group.getJid(), "我想加入啊");
        Thread.sleep(100);

        // Assert
        assertMember(group, otherUserName, true);
    }

    public void testKick() throws Exception {
        // Arrange
        NewUserTestHelper helper = new NewUserTestHelper(testConnection);
        String otherUserName = helper.bindUser();
        helper.applyToJoinGroup(group.getJid(), "我想加入啊");

        Thread.sleep(100);
        assertMember(group, otherUserName, true);

        // Act
        group.kick(otherUserName, "测试踢出");
        Thread.sleep(100);

        // Assert
        assertMember(group, otherUserName, false);
    }


    public void testExit() throws Exception {
        // Arrange
        NewUserTestHelper helper = new NewUserTestHelper(testConnection);
        String otherUserName = helper.bindUser();
        Group theGroup = helper.applyToJoinGroup(group.getJid(), "我想加入啊");

        Thread.sleep(100);
        assertMember(group, otherUserName, true);

        // Act
        theGroup.exit("测试退出");
        Thread.sleep(100);

        // Assert
        assertMember(group, otherUserName, false);
    }

    public void testChangeNickname() throws Exception {
        // Arrange
        String newNickname = "cooper";

        // Act
        group.changeNickname(newNickname);

        // Assert
        String actualNickname = null;
        for(DataForm.Item item : group.getMembers().getItems()) {

            HashMap<String, FormField> fields = new HashMap<String, FormField>();
            for(FormField field : item.getFields()) {
                fields.put(field.getVariable(), field);
            }

            FormField field = fields.get("username");
            if(field.getValues().get(0).equalsIgnoreCase(testUserName)){
                actualNickname = fields.get("nickname").getValues().get(0);
            }
        }

        assertEquals(newNickname, actualNickname);
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
        Message msg = new Message();
        msg.setType(Message.Type.groupchat);
        msg.setBody("hello buddy.");

        // Act
        group.send(msg);

        // Assert
    }

    public void testGroup() throws Exception {
        // Arrange
        String jid = "12@group.skysea.com";

        // Act
        Group group = groupService.getGroup(jid);

        // Assert
        assertEquals(jid, group.getJid());
    }

    protected static void assertMember( Group group, String user,boolean has) throws Exception{
        boolean found = false;
        for(DataForm.Item item : group.getMembers().getItems()) {
            for(FormField field : item.getFields()) {
                if("username".equals(field.getVariable()) && field.getValues().get(0).equalsIgnoreCase(user)) {
                    found = true;
                }
            }
        }
        assertEquals(has, found);
    }


    public static class GroupApplyTest extends GroupTestBase
    {
        private Group group;
        private String otherUserName;
        private String otherUserNameJid;

        @Override
        protected void setUp() throws Exception{
            super.setUp();

            Form form = new Form(GroupServiceTest.getCreateForm());
            // 设置开放程度为：需要圈子所有者确认
            FormField field = form.getField("openness");
            field.setType(FormField.TYPE_TEXT_SINGLE);
            form.setAnswer("openness", (String)"AFFIRM_REQUIRED");
            group = groupService.create(form.getDataFormToSend());


            // 创建一个临时用户并，申请加入圈子
            NewUserTestHelper helper = new NewUserTestHelper(testConnection);
            otherUserName = helper.bindUser();
            otherUserNameJid =  otherUserName+ "@" + testConnection.getHost();
            helper.applyToJoinGroup(group.getJid(), "我想加入啊");
        }


        public void testProcessApply_When_Owner_Agree() throws Exception {
            // Arrange


            // Act
            group.processApply("invalid", otherUserNameJid, true, "welcome");

            // Assert
            assertMember(group, otherUserName, true);
        }

        public void testProcessApply_When_Owner_Decline() throws Exception {
            // Arrange

            // Act
            group.processApply("invalid", otherUserNameJid, false, "sorry");

            // Assert
            assertMember(group, otherUserName, false);
        }

    }

    static class NewUserTestHelper{
        private final XmppTestConnection connection;
        private String userName;

        public NewUserTestHelper(XmppTestConnection baseConnection) {
            connection = new XmppTestConnection(baseConnection);
        }

        public String bindUser() throws Exception{
            connection.connect();
            return userName = connection.createTestUserAndLogin();
        }

        public Group applyToJoinGroup(String jid, String reason) throws Exception {
            GroupService groupService1 =
                    new GroupService(connection.getConnection(), StringUtils.parseServer(jid));
            Group group = groupService1.getGroup(jid);
            group.applyToJoin(reason);
            return group;
        }
    }
}