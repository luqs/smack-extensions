package com.skysea.group.packet.operate;

import junit.framework.TestCase;

public class OperateTest extends TestCase {

    public void testConstructor() throws Exception{
        // Arrange
        MockOperate ope = new MockOperate("hello");

        // Act & Assert
        assertEquals("hello", ope.getType());
    }
    public void testToXML() throws Exception {
        // Arrange
        MockOperate ope = new MockOperate("hello");

        // Act & Assert
        assertEquals("<hello></hello>", ope.toXML().toString());
    }

    private static class MockOperate extends Operate {
        public MockOperate(String type) {
            super(type);
        }
    }
}