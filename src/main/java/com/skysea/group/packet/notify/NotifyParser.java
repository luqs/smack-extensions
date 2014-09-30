package com.skysea.group.packet.notify;

import com.skysea.group.GroupService;
import com.skysea.group.MemberInfo;
import com.skysea.group.packet.HasReason;
import org.xmlpull.v1.XmlPullParser;

/**
 * 通知分析器。
 * Created by apple on 14-9-23.
 */
public final class NotifyParser {
    private final XmlPullParser parser;
    private final String namespace;
    public NotifyParser(XmlPullParser parser, String namespace) throws Exception {
        assert parser != null;
        assert parser.getEventType() == XmlPullParser.START_TAG;

        this.parser = parser;
        this.namespace = namespace;
    }


    /**
     * 分析器是否接受指定类型的元素分析。
     * @param name
     * @param namespace
     * @return
     */
    public static boolean isAccept(String name, String namespace) {
        assert name != null;
        return
                isNotifyElement(Notify.Type.MEMBER_JOINED, name) ||
                isNotifyElement(Notify.Type.MEMBER_EXITED, name) ||
                isNotifyElement(Notify.Type.MEMBER_KICKED, name) ||

                isNotifyElement(Notify.Type.MEMBER_APPLY_TO_JOIN, name) &&
                        GroupService.GROUP_OWNER_NAMESPACE.equals(namespace) ||

                isNotifyElement(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, name) &&
                        GroupService.GROUP_USER_NAMESPACE.equals(namespace) ||

                isNotifyElement(Notify.Type.MEMBER_PROFILE_CHANGED, name) ||
                isNotifyElement(Notify.Type.GROUP_DESTROY, name) ;
    }


    /**
     * 执行分析。
     * @return
     * @throws Exception
     */
    public Notify parse() throws Exception {
        if (isNotifyElement(Notify.Type.MEMBER_JOINED)) {

            return parse(new MemberEventNotify(Notify.Type.MEMBER_JOINED));
        } else if (isNotifyElement(Notify.Type.MEMBER_EXITED)) {

            return parse(new MemberEventNotify(Notify.Type.MEMBER_EXITED));
        } else if (isNotifyElement(Notify.Type.MEMBER_KICKED)) {

            return parse((Notify) parseOperator(new KickedNotify()));
        } else if (isNotifyElement(Notify.Type.MEMBER_PROFILE_CHANGED)) {

            return parse(new ProfileChangedNotify());
        } else if (GroupService.GROUP_OWNER_NAMESPACE.equals(namespace) &&
                isNotifyElement(Notify.Type.MEMBER_APPLY_TO_JOIN)) {

            ApplyNotify applyNotify = new ApplyNotify();
            applyNotify.setId(parser.getAttributeValue(null, "id"));
            return parse((Notify)applyNotify);

        } else if (GroupService.GROUP_USER_NAMESPACE.equals(namespace) &&
                isNotifyElement(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT)) {

            ApplyResultNotify applyResultNotify = new ApplyResultNotify();
            return parse(applyResultNotify);

        } else if (isNotifyElement(Notify.Type.GROUP_DESTROY)) {
            GroupDestroyNotify groupDestroyNotify = new GroupDestroyNotify();
            parseOperator(groupDestroyNotify);
            return parse(groupDestroyNotify);

        } else {
            return null;
        }
    }

    private boolean isNotifyElement(Notify.Type type) {
        return isNotifyElement(type, parser.getName());
    }

    private static boolean isNotifyElement(Notify.Type type, String elementName) {
        return type.getName().equals(elementName);
    }

    private Notify parse(Notify notify) throws Exception {
        boolean done = false;
        while (!done) {
            int type = parser.next();
            if (type == XmlPullParser.START_TAG) {

                /* 尝试分析成员信息 */
                if (notify instanceof MemberEventNotify) {
                    tryParseMember((MemberEventNotify) notify);
                }

                /* 尝试分析reason */
                if (notify instanceof HasReason) {
                    tryParseReason((HasReason) notify);
                }

                /* 分析新的昵称 */
                if (notify instanceof ProfileChangedNotify) {
                    tryParseNickname((ProfileChangedNotify) notify);
                }

                /* 分析申请结果 */
                if (notify instanceof ApplyResultNotify) {
                    tryParseApplyResult((ApplyResultNotify) notify);
                }

            } else if (type == XmlPullParser.END_TAG) {
                done = notify.getType().getName().equals(parser.getName());
            }
        }
        return notify;
    }

    private boolean tryParseApplyResult(ApplyResultNotify notify) throws Exception {
        if ("agree".equals(parser.getName())) {
            notify.setResult(true);
        } else if ("decline".equals(parser.getName())) {
            notify.setResult(false);
        } else {
            return false;
        }

        parseOperator(notify);
        return true;
    }

    private void tryParseNickname(ProfileChangedNotify notify) throws Exception {
        if ("nickname".equals(parser.getName())) {
            notify.setNewNickname(parser.nextText());
        }
    }

    private boolean tryParseReason(HasReason notify) throws Exception {
        if ("reason".equals(parser.getName())) {
            notify.setReason(parser.nextText());
            return true;
        }

        return false;
    }

    private boolean tryParseMember(MemberEventNotify notify) {
        if ("member".equals(parser.getName())) {
            MemberInfo member = new MemberInfo(
                    parser.getAttributeValue(null, "username"),
                    parser.getAttributeValue(null, "nickname"));

            notify.setMemberInfo(member);
            return true;
        }

        return false;
    }

    private HasOperator parseOperator(HasOperator operator) {
        operator.setFrom(parser.getAttributeValue(null, "from"));
        return operator;
    }

}
