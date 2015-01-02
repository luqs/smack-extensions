package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class InviteOperateTest extends TestCase {

    public void testChildrenElements() throws Exception {

        // Arrange
        InviteOperate ope = new InviteOperate();
        ope.addMember("user100", "独孤求败");
        ope.addMember("user101", "圆月弯刀");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals(
                "<invite>" +
                "<member username='user100' nickname='独孤求败'/>" +
                "<member username='user101' nickname='圆月弯刀'/>" +
                "</invite>", xml);
    }
}