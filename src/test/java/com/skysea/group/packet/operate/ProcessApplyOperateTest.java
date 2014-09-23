package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class ProcessApplyOperateTest extends TestCase {

    public void testToXML() throws Exception{
        // Arrange
        ProcessApplyOperate operate = new ProcessApplyOperate("abd", true);
        operate.setFrom("user@skysea.com");
        operate.setReason("欢迎加入");

        // Act
        String xml = operate.toXML().toString();

        // Assert
        assertEquals("<apply id='abd' from='user@skysea.com'><agree/><reason>欢迎加入</reason></apply>", xml);
    }
}