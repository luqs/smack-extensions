package com.skysea.group.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public class XPacket extends DataFormPacket {

    public XPacket(String namespace) {
        super("x", namespace);
    }

    @Override
    public CharSequence getChildElementXML() {
        XmlStringBuilder builder = new XmlStringBuilder()
                .halfOpenElement("x")
                .xmlnsAttribute(namespace)
                .rightAngelBracket();

        if(dataForm!= null) {
            builder.append(dataForm.toXML());
        }
        return builder.closeElement("x");
    }

}
