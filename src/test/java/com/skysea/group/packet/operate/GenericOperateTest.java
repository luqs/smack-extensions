package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class GenericOperateTest extends TestCase {

    public void testToXML() throws Exception {
        // Arrange
        GenericOperate ope = new GenericOperate(GenericOperate.DESTROY);
        ope.setReason("再见各位");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals("<destroy><reason>再见各位</reason></destroy>", xml);

    }
}