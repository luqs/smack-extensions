package com.skysea.group;

import com.skysea.GroupTestBase;
import junit.framework.TestCase;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

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
}