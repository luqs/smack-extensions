package com.skysea;

import com.skysea.group.GroupService;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public abstract class GroupTestBase extends XmppTestBase {
    protected GroupService groupService;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        groupService = new GroupService(testConnection.getConnection(), "group.skysea.com");
    }
}
