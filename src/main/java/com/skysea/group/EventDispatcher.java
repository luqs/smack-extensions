package com.skysea.group;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangzhi on 2014/9/22.
 */
final class EventDispatcher implements PacketListener {
    private final ConcurrentLinkedQueue<UserEventListener> userListeners =
            new ConcurrentLinkedQueue<UserEventListener>();

    public EventDispatcher(XMPPConnection connection, String domain) {
        assert connection != null;
        assert domain != null;

        /* 监听所有从圈子服务发送过来的Message */
        connection.addPacketListener(this, new AndFilter(
                new PacketTypeFilter(Message.class),
                new DomainFilter(domain)));
    }

    public void addEventListener(Object listener) {

    }

    public void removeEventListener(Object listener) {

    }

    public void addEventListener(UserEventListener listener) {
        assert listener != null;

        if(!userListeners.contains(listener)) {
            userListeners.add(listener);
        }
    }

    public void removeEventListener(UserEventListener listener) {
        assert listener != null;

        userListeners.remove(listener);
    }



    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {

    }


    /**
     * Packet的域名过滤器。
     */
    static class DomainFilter implements PacketFilter {

        private final String domain;
        public DomainFilter(String domain) {
            assert domain != null;
            this.domain =  domain;
        }
        @Override
        public boolean accept(Packet packet) {
            String from = packet.getFrom();
            assert from != null;

            return domain.equals(from) ||
            // 注意大小写问题，已经Resource问题。
             from.endsWith(domain) &&
                     (from.length() > domain.length() &&
                             from.charAt(from.length() - domain.length() - 1) == '@') ;
            //return domain.equalsIgnoreCase(StringUtils.parseServer(packet.getFrom()));
        }
    }
}
