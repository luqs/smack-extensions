package com.skysea;


import junit.framework.TestCase;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by zhangzhi on 2014/9/16.
 */
public abstract class XmppTestBase extends TestCase {
    protected XMPPConnection connection;

    @Override
    protected void setUp() throws Exception {
        ConnectionConfiguration configuration = new ConnectionConfiguration("192.168.1.104", 5222, "skysea.com");
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPTCPConnection(configuration);
        connection.connect();
        connection.login("admin", "admin", "android_test");
    }
}
