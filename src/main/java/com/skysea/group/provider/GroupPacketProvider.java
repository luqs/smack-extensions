package com.skysea.group.provider;


import com.skysea.group.packet.GroupPacket;
import com.skysea.group.packet.QueryPacket;
import com.skysea.group.packet.XPacket;
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
        GroupPacket packet = null;
        if("query".equals(parser.getName())){
            packet = new QueryPacket(parser.getNamespace());
            ((QueryPacket)packet).setNode(parser.getAttributeValue(null, "node"));
            parsePacket(packet, parser);
        } else if ("x".equals(parser.getName())) {
            packet = new XPacket(parser.getNamespace());
            parsePacket(packet, parser);
        }

        return packet;
    }

    private void parsePacket(GroupPacket packet, XmlPullParser parser) throws Exception {
        boolean done = false;
        while (!done) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){

                /* 数据表单 */
                if(isDataForm(parser)) {
                    packet.addExtension(PacketParserUtils.parsePacketExtension(
                            parser.getName(),
                            parser.getNamespace(),
                            parser));

                    /* X节点包含通知信息 */
                } /* else if(packet instanceof XPacket &&
                        NotifyParser.isAccept(parser.getName(), packet.getNamespace())) {

                    ((XPacket)packet).setNotify(
                            new NotifyParser(parser, packet.getNamespace()).parse());
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
