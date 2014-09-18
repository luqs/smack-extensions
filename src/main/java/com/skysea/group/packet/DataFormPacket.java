package com.skysea.group.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public abstract class DataFormPacket extends IQ {
    protected final String elementName;
    protected String namespace;
    protected DataForm dataForm;

    protected DataFormPacket(String elementName, String namespace) {
        if(elementName == null){ throw new NullPointerException("elementName is null."); }
        if(namespace == null){ throw new NullPointerException("namespace is null."); }

        this.elementName = elementName;
        this.namespace = namespace;
    }

    public DataForm getDataForm() {
        if (dataForm == null) {
            dataForm = (DataForm) getExtension("x", "jabber:x:data");
        }
        return dataForm;
    }

    public void setDataForm(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getElementName() {
        return elementName;
    }
}
