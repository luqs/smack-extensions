package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class ProcessApplyOperateTest extends TestCase {

    public void testToXML() throws Exception{
        // Arrange
        ProcessApplyOperate operate = new ProcessApplyOperate("abd","user", "碧眼狐狸", true);
        operate.setReason("欢迎加入");

        // Act
        String xml = operate.toXML().toString();

        // Assert
        assertEquals(
                "<apply id='abd'>" +
                "<agree/>" +
                "<member username='user' nickname='碧眼狐狸'/>" +
                "<reason>欢迎加入</reason>" +
                "</apply>", xml);
    }
}