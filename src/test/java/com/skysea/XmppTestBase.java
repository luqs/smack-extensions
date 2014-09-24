package com.skysea;


import junit.framework.TestCase;

/**
 * Created by zhangzhi on 2014/9/16.
 */
public abstract class XmppTestBase extends TestCase {
    protected XmppTestConnection testConnection;
    protected String testUserName;
    protected String testUserJid;

    @Override
    protected void setUp() throws Exception {
        testConnection = new XmppTestConnection("192.168.1.104", 5222, "skysea.com");
        testConnection.connect();
        testUserName = testConnection.createTestUserAndLogin();
        testUserJid = testUserName + "@" + testConnection.getXmppDomain();
    }

}
