package com.skysea;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by zhangzhi on 2014/9/22.
 */
public class XmppTestConnection {
    private final String host;
    private final int port;
    private final String xmppDomain;
    private XMPPTCPConnection connection;

    public XmppTestConnection(String host, int port, String xmppDomain) {
        this.host = host;
        this.port = port;
        this.xmppDomain = xmppDomain;
    }

    public XmppTestConnection(XmppTestConnection connection) {
        this.host = connection.host;
        this.port = connection.port;
        this.xmppDomain = connection.xmppDomain;
    }

    public String getHost() {
        return host;
    }

    public void connect() throws IOException, XMPPException, SmackException {
        ConnectionConfiguration configuration = new ConnectionConfiguration(host, port, xmppDomain);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPTCPConnection(configuration);
        connection.connect();
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public String createTestUser(String password) throws
            SmackException.NotConnectedException,
            XMPPException.XMPPErrorException,
            SmackException.NoResponseException {
        String testUser = "testUser" + System.currentTimeMillis();
        AccountManager acc = AccountManager.getInstance(connection);
        acc.createAccount(testUser, password);
        return testUser;
    }

    public String createTestUserAndLogin() throws
            SmackException,
            XMPPException,
            IOException {
        String password = "test";
        String userName = createTestUser(password);
        connection.login(userName, password);
        return userName;
    }
}
