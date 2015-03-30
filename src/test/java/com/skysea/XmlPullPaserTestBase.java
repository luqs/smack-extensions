package com.skysea;  

import junit.framework.TestCase;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

/**
 * Created by apple on 14-9-24.
 */
public abstract class XmlPullPaserTestBase extends TestCase {
    protected XmlPullParser xmlParser(String s) throws Exception{
        XmlPullParser parser = PacketParserUtils.newXmppParser();
        parser.setInput(new StringReader(s));
        return parser;
    }
}
