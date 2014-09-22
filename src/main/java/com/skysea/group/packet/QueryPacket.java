package com.skysea.group.packet;

import com.skysea.group.GroupService;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.xdata.packet.DataForm;

/**
 * 查询包。
 * Created by zhangzhi on 2014/9/17.
 */
public class QueryPacket extends DataFormPacket {
    private String node;

    public QueryPacket(String namespace, String node){
        this(namespace);
        this.node = node;
    }

    public QueryPacket(String namespace) {
        super("query", namespace);
    }

    @Override
    public void startElement(XmlStringBuilder builder) {
        super.startElement(builder);
        builder.optAttribute("node", node);
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }


}
