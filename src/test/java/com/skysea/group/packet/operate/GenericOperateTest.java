package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class GenericOperateTest extends TestCase {

    public void testToXML() throws Exception {
        // Arrange
        GenericOperate ope = new GenericOperate(GenericOperate.APPLY);
        ope.setReason("我也是80后啊！");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals("<apply><reason>我也是80后啊！</reason></apply>", xml);

    }
}