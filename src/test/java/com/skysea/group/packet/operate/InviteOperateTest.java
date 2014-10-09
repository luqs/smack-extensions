package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class InviteOperateTest extends TestCase {

    public void testChildrenElements() throws Exception {

        // Arrange
        InviteOperate ope = new InviteOperate("user100", "独孤求败");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals(
                "<invite>" +
                "<member username='user100' nickname='独孤求败'/>" +
                "</invite>", xml);
    }
}