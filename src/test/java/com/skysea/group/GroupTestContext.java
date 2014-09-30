package com.skysea.group;

import com.skysea.group.GroupService;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.io.IOException;

/**
 * Created by zhangzhi on 2014/9/30.
 */
public class GroupTestContext {
    private XMPPTCPConnection connection;
    private final String host;
    private final int port;
    private final String xmppDomain;
    private GroupService groupService;
    private String userName;
    private String password;

    public GroupTestContext(String host, int port, String xmppDomain){
        this.host = host;
        this.port = port;
        this.xmppDomain = xmppDomain;
    }

    public GroupTestContext(GroupTestContext testContext) {
        this(testContext.host, testContext.port, testContext.xmppDomain);
    }

    public void initialize() throws Exception  {
        ConnectionConfiguration configuration = new ConnectionConfiguration(host, port, xmppDomain);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPTCPConnection(configuration);
        connection.connect();
    }

    public GroupService getGroupService() {
        if(groupService == null) {
            groupService = new GroupService(connection, "group.skysea.com");
        }
        return groupService;
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserJid() {
        return userName + "@" + xmppDomain;
    }

    public void bindUser() throws Exception {
        if(userName != null) {throw new IllegalStateException();}
        String uname = "testuser" + System.currentTimeMillis();
        String upass = "testpassword";
        register(uname, upass);
        connection.login(uname, upass, null);
        this.userName = uname;
        this.password = upass;
    }

    private void register(String uname, String upassword) throws Exception {
        AccountManager acc = AccountManager.getInstance(connection);
        acc.createAccount(uname, upassword);
    }

}
