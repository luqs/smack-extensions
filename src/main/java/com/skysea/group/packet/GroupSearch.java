package com.skysea.group.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public class GroupSearch extends IQ {
    private DataForm dataForm;
    private RSMPacket rsm;

    public GroupSearch() { }
    public GroupSearch(UserSearch packet) {
        this.dataForm  = packet.getExtension(DataForm.ELEMENT, DataForm.NAMESPACE);
        this.rsm     = RSMPacket.getRSMFrom(packet);
    }

    @Override
    public CharSequence getChildElementXML() {
        XmlStringBuilder builder = new XmlStringBuilder()
                .halfOpenElement("query")
                .xmlnsAttribute("jabber:iq:search")
                .rightAngelBracket();

        if(dataForm != null) {
            builder.append(dataForm.toXML());
        }

        if(rsm != null) {
            builder.append(rsm.toXML());
        }
        return builder.closeElement("query");
    }

    public DataForm getDataForm() {
        return dataForm;
    }

    public void setDataForm(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    public RSMPacket getRsm() {
        return rsm;
    }

    public void setRsm(RSMPacket rsm) {
        this.rsm = rsm;
    }
}
