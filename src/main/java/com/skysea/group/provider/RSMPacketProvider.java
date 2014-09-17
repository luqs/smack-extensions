package com.skysea.group.provider;


import com.skysea.group.packet.RSMPacket;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by zhangzhi on 2014/9/17.
 */
public class RSMPacketProvider implements PacketExtensionProvider {
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        RSMPacket rsmPacket = new RSMPacket();
        while (true) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){
                if("first".equals(parser.getName())){
                    rsmPacket.setFirst(parser.nextText());
                }else if ("last".equals(parser.getName())){
                    rsmPacket.setLast(parser.nextText());
                }else if ("count".equals(parser.getName())){
                    rsmPacket.setCount(Integer.valueOf(parser.nextText()));
                }
            }else if (type == XmlPullParser.END_TAG) {
                if(parser.getName().equals(RSMPacket.ELEMENT)){
                    break;
                }
            }
        }

        return rsmPacket;
    }
}
