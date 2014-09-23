package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class KickOperateTest extends TestCase {
    public void testToXML() throws Exception {
        // Arrange
        KickOperate operate = new KickOperate("user");
        operate.setReason("滚蛋吧");

        // Act
        String xml = operate.toXML().toString();

        // Assert
        assertEquals("<kick username='user'><reason>滚蛋吧</reason></kick>", xml);
    }

}