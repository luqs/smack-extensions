package com.skysea.group.provider;

import com.skysea.group.MemberInfo;
import com.skysea.group.packet.MemberPacketExtension;
import com.skysea.group.packet.NotifyPacketExtension;
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
        String name = parser.getName();
        String namespace = parser.getNamespace();
        PacketExtension extension = null;

        while (true) {
            int type = parser.next();
            if (type == XmlPullParser.START_TAG && extension == null) {

                if (NotifyParser.isAccept(parser.getName(), namespace)) {

                    /* 解析通知扩展包 */
                    NotifyPacketExtension packet = new NotifyPacketExtension(name, namespace);
                    NotifyParser notifyparser = new NotifyParser(parser, packet.getNamespace());
                    packet.setNotify(notifyparser.parse());

                    extension = packet;
                } else {
                     /* 解析聊天消息中的程序信息扩展包 */
                    extension = MemberPacketExtension.tryParse(name, namespace, parser);
                }
            } else if (type == XmlPullParser.END_TAG) {
                if (parser.getName().equals(name)) {
                    break;
                }
            }
        }

        return extension;
    }

}
