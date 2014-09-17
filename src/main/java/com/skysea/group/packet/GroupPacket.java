package com.skysea.group.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * Created by zhangzhi on 2014/9/17.
 */
public class GroupPacket extends IQ {
    public final static String NAMESPACE = "http://skysea.com/protocol/group";
    private ExtensionType type;
    private DataForm                dataForm;
    private String                  node;
    public GroupPacket(ExtensionType type) {
        assert type != null;
        this.type = type;
    }

    @Override
    public CharSequence getChildElementXML() {
        XmlStringBuilder builder = new XmlStringBuilder()
                .halfOpenElement(type.toString())
                .xmlnsAttribute(NAMESPACE)
                .optAttribute("node", node)
                .rightAngelBracket();

        if(dataForm!= null) {
            builder.append(dataForm.toXML());
        }
        return builder.closeElement(type.toString()).toString();
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public DataForm getDataForm() {
        if(dataForm == null) {
            dataForm = (DataForm)getExtension("x", "jabber:x:data");
            if(dataForm == null) {
                dataForm = new DataForm("submit");
            }
        }
        return dataForm;
    }

    public void setDataForm(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    public ExtensionType getExtensionPacketType(){
        return this.type;
    }
}
