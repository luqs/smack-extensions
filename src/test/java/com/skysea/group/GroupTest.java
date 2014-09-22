package com.skysea.group;

import com.skysea.GroupTestBase;
import junit.framework.TestCase;
import org.jivesoftware.smack.XMPPException;
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

    public void testDestroy() throws Exception{
        // Arrange
        String reason = "再见吧各位";

        // Act
        group.destroy(reason);

        // Assert
        try {
            group.getInfo();
        }catch (XMPPException.XMPPErrorException exp){
            assertEquals("item-not-found", exp.getXMPPError().getCondition());
            return;
        }
        fail();
    }


    public void testGetMembers() throws Exception{
        // Arrange

        // Act
        DataForm resultForm = group.getMembers();

        // Assert
        boolean found = false;


        for (DataForm.Item item: resultForm.getItems()){
            for (FormField field : item.getFields()) {
                if("username".equals(field.getVariable())){
                    if(StringUtils.parseName(connection.getUser()).equals(field.getValues().get(0))){
                        found = true;
                    }
                }
            }
        }
        assertTrue(found);
    }

    public void testGroup() throws Exception{
        // Arrange
        String jid = "12@group.skysea.com";

        // Act
        Group group = groupService.getGroup(jid);

        // Assert
        assertEquals(jid, group.getJid());
    }

    private void assertFormEquals(DataForm a, DataForm b) {

        Form actualForm = new Form(b);
        for (FormField field : a.getFields()) {

            assertEquals(
                    field.getValues().get(0),
                    actualForm.getField(field.getVariable()).getValues().get(0));
        }
    }
}