package com.skysea.group;

import com.skysea.GroupTestBase;
import com.skysea.XmppTestBase;
import com.skysea.group.packet.GroupSearch;
import com.skysea.group.packet.RSMPacket;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class GroupServiceTest extends GroupTestBase {

    public void testCreate() throws Exception {
        // Arrange
        final DataForm form = getCreateForm();

        // Act
        Group group = groupService.create(form);

        // Assert
        assertNotNull(group.getJid());
    }

    public void testSearch() throws Exception{
        // Arrange
        DataForm searchForm = new DataForm("submit");
        RSMPacket rsm = new RSMPacket();
        rsm.setIndex(0);
        rsm.setMax(10);
        GroupSearch search = new GroupSearch();
        search.setDataForm(searchForm);
        search.setRsm(rsm);

        // Act
        GroupSearch result = groupService.search(search);

        // Assert
        assertTrue(result.getRsm().getCount() > 0);
        assertTrue(result.getDataForm().getItems().size() > 0);
    }

    public void testGetJoinedGroups() throws Exception{
        // Arrange
        Group group = groupService.create(getCreateForm());

        // Act
        DataForm resultForm = groupService.getJoinedGroups();

        // Assert
        boolean found = false;
        for(DataForm.Item item : resultForm.getItems()){
            for(FormField field : item.getFields()) {
                if(field.getVariable().equals("jid")){
                    if(field.getValues().get(0).equals(group.getJid())){
                        found = true;
                    }
                }
            }
        }

        assertTrue(found);
    }


    public static DataForm getCreateForm() {
        final DataForm form = new DataForm("submit");
        FormField field = new FormField("name");
        field.addValue("testgroup-" + System.currentTimeMillis());
        form.addField(field);

        field = new FormField("subject");
        field.addValue("this is test group create by GroupServiceTest.");
        form.addField(field);

        field = new FormField("description");
        field.addValue("i like programming, diligent programming");
        form.addField(field);

        field = new FormField("category");
        field.addValue("1");
        form.addField(field);

        field = new FormField("openness");
        field.addValue("PUBLIC");
        form.addField(field);
        return form;
    }


}