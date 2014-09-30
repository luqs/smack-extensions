package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class ApplyOperateTest extends TestCase {
    public void testChildrenElements() throws Exception {

        // Arrange
        ApplyOperate ope = new ApplyOperate();
        ope.setNickname("金轮法王");
        ope.setReason("我想加入");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals(
                "<apply>" +
                "<member nickname='金轮法王'/>" +
                "<reason>我想加入</reason>" +
                "</apply>", xml);
    }
}