package com.skysea.group;

import com.skysea.XmppTestBase;
import com.skysea.group.GroupService;
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
        testContext = new GroupTestContext("localhost", 5222, "skysea.com");
        testContext.initialize();
        testContext.bindUser();

        groupService = testContext.getGroupService();
    }
}
