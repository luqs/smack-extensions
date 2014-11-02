package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class HasReasonOperateTest extends TestCase {

    public void testNewInstanceForExitGroup() throws Exception {
        // Arrange
        HasReasonOperate ope = HasReasonOperate.newInstanceForExitGroup("我退了先！");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals("<exit><reason>我退了先！</reason></exit>", xml);
    }

    public void testNewInstanceForDestroyGroup() throws Exception {
        // Arrange
        HasReasonOperate ope = HasReasonOperate.newInstanceForDestroyGroup("再见了各位！");

        // Act
        String xml = ope.toXML().toString();

        // Assert
        assertEquals("<destroy><reason>再见了各位！</reason></destroy>", xml);
    }
}