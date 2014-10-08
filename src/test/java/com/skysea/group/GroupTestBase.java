package com.skysea.group;

import junit.framework.TestCase;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public abstract class GroupTestBase extends TestCase {
    protected GroupTestContext testContext;
    protected GroupService groupService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testContext = new GroupTestContext(
                System.getProperty("xmpp.host", "localhost"),
                Integer.parseInt(System.getProperty("xmpp.port", "5222")),
                System.getProperty("xmpp.domain", "skysea.com"));
        testContext.initialize();
        testContext.bindUser();

        groupService = testContext.getGroupService();
    }
}
