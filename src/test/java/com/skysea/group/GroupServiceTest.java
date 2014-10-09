package com.skysea.group;

import com.skysea.group.packet.GroupSearch;
import com.skysea.group.packet.RSMPacket;
import mockit.Mocked;
import mockit.Verifications;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class GroupServiceTest extends GroupTestBase {
    @Mocked
    GroupEventListener listener;

    public void testCreate() throws Exception {
        // Arrange
        final DataForm form = getCreateForm();
        groupService.addGroupEventListener(listener);

        // Act
        final Group group = groupService.create(form);

        // Assert
        assertNotNull(group.getJid());
        new Verifications() {
            {
                listener.created(group.getJid(), form);
                times = 1;
            }
        };
    }

    public void testSearch() throws Exception {
        // Arrange
        Form createForm = new Form(getCreateForm());
        Group group = groupService.create(createForm.getDataFormToSend());

        // 搜索条件
        DataForm searchForm = new DataForm("submit");

        FormField field = new FormField("id");
        field.addValue(StringUtils.parseName(group.getJid()));
        searchForm.addField(field);

        field = new FormField("name");
        field.addValue(createForm.getField("name").getValues().get(0));
        searchForm.addField(field);

        field = new FormField("category");
        field.addValue(createForm.getField("category").getValues().get(0));
        searchForm.addField(field);

        // 分页信息
        RSMPacket rsm = new RSMPacket();
        rsm.setIndex(0);
        rsm.setMax(10);

        // Act
        GroupSearch result = groupService.search(new GroupSearch(searchForm, rsm));

        // Assert
        assertEquals(1, result.getRsm().getCount());
        assertEquals(1, result.getDataForm().getItems().size());
    }

    public void testSearch_When_Group_Is_Private() throws Exception {
        // Arrange
        Form createForm = new Form(getCreateForm());
        createForm.setAnswer("openness", "PRIVATE");
        Group group = groupService.create(createForm.getDataFormToSend());

        DataForm searchForm = new DataForm("submit");
        FormField field = new FormField("id");
        field.addValue(StringUtils.parseName(group.getJid()));
        searchForm.addField(field);

        // Act
        GroupSearch result = groupService.search(new GroupSearch(searchForm, null));

        // Assert
        assertEquals(0, result.getRsm().getCount());
        assertEquals(0, result.getDataForm().getItems().size());

    }

    public void testGetJoinedGroups() throws Exception {
        // Arrange
        Group group = groupService.create(getCreateForm());

        // Act
        DataForm resultForm = groupService.getJoinedGroups();

        // Assert
        boolean found = false;
        for (DataForm.Item item : resultForm.getItems()) {
            for (FormField field : item.getFields()) {
                if (field.getVariable().equals("jid")) {
                    if (field.getValues().get(0).equals(group.getJid())) {
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
        field.setType(FormField.TYPE_TEXT_SINGLE);
        field.addValue("PUBLIC");
        form.addField(field);
        return form;
    }


}