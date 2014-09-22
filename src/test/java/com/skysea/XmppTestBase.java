package com.skysea;


import junit.framework.TestCase;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by zhangzhi on 2014/9/16.
 */
public abstract class XmppTestBase extends TestCase {
    protected XmppTestConnection testConnection;
    protected String testUserName;

    @Override
    protected void setUp() throws Exception {
        testConnection = new XmppTestConnection("192.168.1.104", 5222, "skysea.com");
        testConnection.connect();
        testUserName = testConnection.createTestUserAndLogin();
    }

}
