package com.skysea.group;

import junit.framework.TestCase;
import org.jivesoftware.smack.packet.Message;

public class DomainFilterTest extends TestCase {

    public void testAccept_When_Packet_From_Service() throws Exception {
        // Arrange
        EventDispatcher.DomainFilter filter = new EventDispatcher.DomainFilter("group.skysea.com");
        Message msg = new Message();
        msg.setFrom("group.skysea.com");

        // Act
        boolean result = filter.accept(msg);

        // Assert
        assertTrue(result);
    }

    public void testAccept_When_Packet_From_Group() throws Exception {
        // Arrange
        EventDispatcher.DomainFilter filter = new EventDispatcher.DomainFilter("group.skysea.com");
        Message msg = new Message();
        msg.setFrom("123@group.skysea.com");

        // Act
        boolean result = filter.accept(msg);

        // Assert
        assertTrue(result);
    }

    public void testAccept_When_Packet_Invalid() throws Exception {
        // Arrange
        EventDispatcher.DomainFilter filter = new EventDispatcher.DomainFilter("group.skysea.com");

        // Act & Assert
        Message msg = new Message();
        msg.setFrom("123@conversation.skysea.com");
        assertFalse(filter.accept(msg));

        msg = new Message();
        msg.setFrom("123@mygroup.skysea.com");
        assertFalse(filter.accept(msg));
    }
}