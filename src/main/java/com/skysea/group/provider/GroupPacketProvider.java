package com.skysea.group.provider;


import com.skysea.group.packet.ExtensionType;
import com.skysea.group.packet.GroupPacket;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by zhangzhi on 2014/9/17.
 */
public class GroupPacketProvider implements IQProvider {
    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        GroupPacket packet = createPacket(parser);
        boolean done = false;
        while (!done) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){
                packet.addExtension(PacketParserUtils.parsePacketExtension(
                        parser.getName(),
                        parser.getNamespace(),
                        parser));
            }else if(type == XmlPullParser.END_TAG){
                done = packet.getExtensionPacketType().toString().equals(parser.getName());
            }
        }
        return packet;
    }

    private GroupPacket createPacket(XmlPullParser parser) {
        ExtensionType packetType =
                parser.getName().equals(ExtensionType.X.toString())
                        ? ExtensionType.X
                        : ExtensionType.QUERY;

        GroupPacket packet = new GroupPacket(packetType);
        if(packetType == ExtensionType.QUERY) {
            packet.setNode(parser.getAttributeValue("node", ""));
        }

        return packet;
    }

    private boolean isDataForm(XmlPullParser parser) {
        return parser.getName().equals("x") && "jabber:x:data".equals(parser.getNamespace());
    }
}
