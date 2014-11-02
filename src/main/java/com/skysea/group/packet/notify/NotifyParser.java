package com.skysea.group.packet.notify;

import com.skysea.group.GroupService;
import com.skysea.group.MemberInfo;
import com.skysea.group.packet.HasMember;
import com.skysea.group.packet.HasReason;
import org.xmlpull.v1.XmlPullParser;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 通知分析器。
 * Created by apple on 14-9-23.
 */
public final class NotifyParser {
    /**
     * 分析器缓存。
     */
    private final static AtomicReference<NotifyParser> parserCache = new AtomicReference<NotifyParser>();
    private XmlPullParser parser;
    private String namespace;
    private Notify result;

    private NotifyParser(){}
    private void reset(XmlPullParser parser, String namespace) {
        this.parser = parser;
        this.namespace = namespace;
        this.result = null;
    }

    /**
     * 分析器是否接受指定类型的元素分析。
     */
    public static boolean isAccept(String name) {
        assert name != null;
        return isNotify(Notify.Type.MEMBER_JOINED, name) ||
                isNotify(Notify.Type.MEMBER_EXITED, name) ||
                isNotify(Notify.Type.MEMBER_KICKED, name) ||
                isNotify(Notify.Type.MEMBER_APPLY_TO_JOIN, name) ||
                isNotify(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT, name) ||
                isNotify(Notify.Type.MEMBER_PROFILE_CHANGED, name) ||
                isNotify(Notify.Type.GROUP_DESTROY, name);
    }

    /**
     * 尝试从XmlPullParser中分析Notify对象。
     * @param parser
     * @param namespace
     * @return
     * @throws Exception
     */
    public static Notify parse(XmlPullParser parser, String namespace) throws Exception {
        NotifyParser notifyParser = acquireNotifyParser();
        notifyParser.reset(parser, namespace);

        /* 执行分析 */
        notifyParser.parse();

        Notify result = notifyParser.result;
        returnNotifyParser(notifyParser);
        return result;
    }

    private static NotifyParser acquireNotifyParser(){
        NotifyParser notifyParser = parserCache.get();
        if(notifyParser != null && parserCache.compareAndSet(notifyParser, null)) {
            return notifyParser;
        }
        return new NotifyParser();
    }

    private static void returnNotifyParser(NotifyParser notifyParser) {
        notifyParser.reset(null, null);
        parserCache.compareAndSet(null, notifyParser);
    }


    /**
     * 执行分析。
     *
     * @return
     * @throws Exception
     */
    public void parse() throws Exception {
        if (is(Notify.Type.MEMBER_JOINED)) {

            parse(new MemberJoinedNotify());
        } else if (is(Notify.Type.MEMBER_EXITED)) {

            parse(new MemberExitedNotify());
        } else if (is(Notify.Type.MEMBER_KICKED)) {

            parse((Notify) parseOperator(new MemberKickedNotify()));
        } else if (is(Notify.Type.MEMBER_PROFILE_CHANGED)) {

            parse(new MemberProfileChangedNotify());
        } else if (isMemberApplyToJoin()) {

            MemberApplyToJoinNotify applyNotify = new MemberApplyToJoinNotify();
            applyNotify.setId(parser.getAttributeValue(null, "id"));
            parse(applyNotify);
        } else if (isMemberApplyToJoinResult()) {

            parse(new MemberApplyResultNotify());
        } else if (is(Notify.Type.GROUP_DESTROY)) {

            parse((Notify) parseOperator(new GroupDestroyNotify()));
        }
    }

    private boolean isMemberApplyToJoinResult() {
        return GroupService.GROUP_USER_NAMESPACE.equals(namespace) &&
                is(Notify.Type.MEMBER_APPLY_TO_JOIN_RESULT);
    }

    private boolean isMemberApplyToJoin() {
        return GroupService.GROUP_OWNER_NAMESPACE.equals(namespace) &&
                is(Notify.Type.MEMBER_APPLY_TO_JOIN);
    }


    private void parse(Notify notify) throws Exception {
        boolean done = false;
        while (!done) {
            int type = parser.next();
            if (type == XmlPullParser.START_TAG) {

                /* 尝试分析成员信息 */
                if (notify instanceof HasMember) {
                    tryParseMember((HasMember) notify);
                }

                /* 尝试分析reason */
                if (notify instanceof HasReason) {
                    tryParseReason((HasReason) notify);
                }

                /* 分析新的昵称 */
                if (notify instanceof MemberProfileChangedNotify) {
                    tryParseNickname((MemberProfileChangedNotify) notify);
                }

                /* 分析申请结果 */
                if (notify instanceof MemberApplyResultNotify) {
                    tryParseApplyResult((MemberApplyResultNotify) notify);
                }

            } else if (type == XmlPullParser.END_TAG) {
                done = notify.getType().getName().equals(parser.getName());
            }
        }
        this.result = notify;
    }

    private boolean is(Notify.Type type) {
        return isNotify(type, parser.getName());
    }

    private static boolean isNotify(Notify.Type type, String elementName) {
        return type.getName().equals(elementName);
    }

    private void tryParseApplyResult(MemberApplyResultNotify notify) throws Exception {
        if ("agree".equals(parser.getName())) {
            notify.setResult(true);
            parseOperator(notify);
        } else if ("decline".equals(parser.getName())) {
            notify.setResult(false);
            parseOperator(notify);
        }
    }

    private void tryParseNickname(MemberProfileChangedNotify notify) throws Exception {
        if ("nickname".equals(parser.getName())) {
            notify.setNewNickname(parser.nextText());
        }
    }

    private void tryParseReason(HasReason notify) throws Exception {
        if ("reason".equals(parser.getName())) {
            notify.setReason(parser.nextText());
        }
    }

    private void tryParseMember(HasMember notify) {
        if ("member".equals(parser.getName())) {
            MemberInfo member = new MemberInfo(
                    parser.getAttributeValue(null, "username"),
                    parser.getAttributeValue(null, "nickname"));
            notify.setMemberInfo(member);
        }
    }

    private HasOperator parseOperator(HasOperator operator) {
        operator.setFrom(parser.getAttributeValue(null, "from"));
        return operator;
    }
}
