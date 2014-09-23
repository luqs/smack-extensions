package com.skysea.group.packet;

import com.skysea.group.packet.operate.Operate;
import org.xmlpull.v1.XmlPullParser;

public interface OperateParser {
    boolean isOperate(XmlPullParser parser);
    Operate parse(XmlPullParser parser, String namespace) throws Exception;
}
