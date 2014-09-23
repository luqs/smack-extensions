package com.skysea.group.provider;


import com.skysea.group.MemberInfo;
import com.skysea.group.packet.*;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by zhangzhi on 2014/9/17.
 */
public class GroupPacketProvider implements IQProvider {
    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception {
        ExtensionPacket packet = null;
        if("query".equals(parser.getName())){
            packet = new QueryPacket(parser.getNamespace());
            ((QueryPacket)packet).setNode(parser.getAttributeValue(null, "node"));
            parseQueryPacket(((QueryPacket)packet), parser);
        } else if ("x".equals(parser.getName())) {
            packet = new XPacket(parser.getNamespace());
            parseXPacket((XPacket)packet, parser);
        }

        return packet;
    }

    private void parseQueryPacket(QueryPacket packet, XmlPullParser parser) throws Exception {
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
    }

    private void parseXPacket(XPacket packet, XmlPullParser parser) throws Exception {
        boolean done = false;
        while (!done) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){
                if(isDataForm(parser)) {
                    packet.addExtension(PacketParserUtils.parsePacketExtension(
                            parser.getName(),
                            parser.getNamespace(),
                            parser));
                }/*else if(ApplyOperate.PARSER.isOperate(parser)) {
                    packet.setOperate(ApplyOperate.PARSER.parse(parser, packet.getNamespace()));
                }*/
            }else if(type == XmlPullParser.END_TAG){
                done = packet.getElementName().equals(parser.getName());
            }
        }
    }

    private boolean isDataForm(XmlPullParser parser) {
        return DataForm.ELEMENT.equals(parser.getName()) &&
                DataForm.NAMESPACE.equals(parser.getNamespace());
    }

}
