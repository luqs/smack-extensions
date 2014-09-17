package com.skysea.group;

import com.skysea.XmppTestBase;
import junit.framework.TestCase;

public class GroupInfrastructureIntegrationTest extends XmppTestBase {
    private GroupInfrastructure infrastructure;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        infrastructure = new GroupInfrastructure(connection);
    }

    public void testCRUD() throws Exception {
        GroupInfo groupInfo = createGroup();
        search(groupInfo);
    }

    public GroupInfo createGroup() throws Exception{
        // Arrange
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setName("i want you.");
        groupInfo.setSubject("love is forever!");
        groupInfo.setDescription("comm on 80th.");
        groupInfo.setCategory(1);
        groupInfo.setOpennessType(GroupInfo.OpennessType.AFFIRM_REQUIRED);

        // Act
        infrastructure.createGroup(groupInfo);

        // Assert
        assertNotNull(groupInfo.getJid());
        assertNotNull(groupInfo.getId());
        return groupInfo;
    }

    public void search(GroupInfo target) throws Exception{
        // Arrange
        GroupQueryObject query = new GroupQueryObject();
        query.setName(target.getName());
        query.setCategory(target.getCategory());
        query.setGroupId(target.getId());

        // Act
        Paging<GroupInfo> groupInfos = infrastructure.search(query, 0, 10);

        // Assert
        assertEquals(1, groupInfos.getCount());
        assertEquals(0, groupInfos.getOffset());
        assertEquals(10, groupInfos.getLimit());
        assertEquals(1, groupInfos.getItems().size());

        GroupInfo actualGroup = groupInfos.getItems().get(0);
        assertEquals(target.getId(), actualGroup.getId());
        assertEquals(target.getJid(), actualGroup.getJid());
        assertEquals(target.getName(), actualGroup.getName());
        assertEquals(target.getOwner(), actualGroup.getOwner());
        assertEquals(target.getNumberOfMembers(), actualGroup.getNumberOfMembers());
        assertEquals(target.getSubject(), actualGroup.getSubject());
    }
}