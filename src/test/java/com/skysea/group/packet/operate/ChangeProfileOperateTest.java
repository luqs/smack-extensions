package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class ChangeProfileOperateTest extends TestCase {

    public void testChildrenElements() throws Exception {

        // Arrange
        ChangeProfileOperate ope = new ChangeProfileOperate();
        ope.setNickname("金轮法王");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals("<profile><nickname>金轮法王</nickname></profile>", xml);
    }
}