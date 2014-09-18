package com.skysea.group.provider;


import com.skysea.group.packet.DataFormPacket;
import com.skysea.group.packet.QueryPacket;
import com.skysea.group.packet.XPacket;
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
        DataFormPacket packet = null;
        if("query".equals(parser.getName())){
            packet = new QueryPacket(parser.getNamespace());
            ((QueryPacket)packet).setNode(parser.getAttributeValue(null, "node"));
        } else if ("x".equals(parser.getName())) {
            packet = new XPacket(parser.getNamespace());
        }

        boolean done = false;
        while (!done) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){
                packet.addExtension(PacketParserUtils.parsePacketExtension(
                        parser.getName(),
                        parser.getNamespace(),
                        parser));
            }else if(type == XmlPullParser.END_TAG){
                done = packet.getElementName().equals(parser.getName());
            }
        }
        return packet;
    }
}
