package com.skysea.group.provider;

import com.skysea.group.packet.NotifyPacketExtension;
import com.skysea.group.packet.notify.Notify;
import com.skysea.group.packet.notify.NotifyParser;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * 通知扩展包提供程序。
 * Created by apple on 14-9-24.
 */
public class NotifyPacketExtensionProvider implements PacketExtensionProvider {
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        NotifyPacketExtension packet = new NotifyPacketExtension(
                parser.getName(),
                parser.getNamespace());

        while (true) {
            int type = parser.next();
            if(type == XmlPullParser.START_TAG){
                if(NotifyParser.isAccept(parser.getName(), packet.getNamespace())) {
                    Notify notify = new NotifyParser(
                            parser, packet.getNamespace()).parse();
                    packet.setNotify(notify);
                    System.out.println(notify);
                }
            }else if (type == XmlPullParser.END_TAG) {
                if(parser.getName().equals(packet.getElementName())){
                    break;
                }
            }
        }

        return packet;
    }
}
