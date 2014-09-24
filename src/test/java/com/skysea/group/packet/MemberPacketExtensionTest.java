package com.skysea.group.packet;

import com.skysea.group.MemberInfo;
import junit.framework.TestCase;

public class MemberPacketExtensionTest extends TestCase {

    public void testToXML() throws Exception {

        // Arrange
        MemberInfo memberInfo = new MemberInfo("user", "碧眼狐狸");
        MemberPacketExtension packetExtension = new MemberPacketExtension(memberInfo);

        // Act
        String xml = packetExtension.toXML().toString();

        // Assert
        assertEquals("<x xmlns='http://skysea.com/protocol/group#member'>" +
                "<member username='user' nickname='碧眼狐狸'/>" +
                "</x>", xml);
    }
}