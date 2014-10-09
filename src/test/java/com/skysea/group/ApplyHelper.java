package com.skysea.group;

/**
* Created by zhangzhi on 2014/10/9.
*/
class ApplyHelper {
    private String nickname = "碧眼狐狸";
    private final GroupTestContext userTestContext;

    public ApplyHelper(GroupTestContext ownerTestContext) throws Exception {
        userTestContext = new GroupTestContext(ownerTestContext);
        userTestContext.initialize();
        userTestContext.bindUser();
    }

    public MemberInfo applyToJoin(Group group, String reason) throws Exception {
        userTestContext
                .getGroupService()
                .getGroup(group.getJid())
                .applyToJoin(nickname, reason);
        return new MemberInfo(userTestContext.getUserName(), nickname);
    }

    public void exit(Group group, String reason) throws Exception {
        userTestContext
                .getGroupService()
                .getGroup(group.getJid())
                .exit(reason);
    }

    public String getNickname() {
        return nickname;
    }

    public GroupTestContext getUserTestContext() {
        return userTestContext;
    }


}
