package com.skysea.group;

import com.skysea.group.packet.RSMPacket;
import com.skysea.group.provider.GroupPacketProvider;
import com.skysea.group.provider.RSMPacketProvider;
import com.skysea.group.provider.SearchProvider;
import org.jivesoftware.smack.provider.ExtensionProviderInfo;
import org.jivesoftware.smack.provider.IQProviderInfo;
import org.jivesoftware.smack.provider.ProviderLoader;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhangzhi on 2014/9/18.
 */
public class GroupProviderLoader implements ProviderLoader {
    @Override
    public Collection<IQProviderInfo> getIQProviderInfo() {
        ArrayList<IQProviderInfo> providers = new ArrayList<IQProviderInfo>();
        GroupPacketProvider groupPacketProvider = new GroupPacketProvider();

        //providers.add(new IQProviderInfo("x", GroupService.GROUP_MEMBER_NAMESPACE, groupPacketProvider));
        providers.add(new IQProviderInfo("x", GroupService.GROUP_NAMESPACE, groupPacketProvider));
        providers.add(new IQProviderInfo("query", GroupService.GROUP_NAMESPACE, groupPacketProvider));
        providers.add(new IQProviderInfo("query", GroupService.GROUP_MEMBER_NAMESPACE, groupPacketProvider));

        providers.add(new IQProviderInfo("query", "jabber:iq:search", new SearchProvider()));
        return providers;
    }

    @Override
    public Collection<ExtensionProviderInfo> getExtensionProviderInfo() {
        ArrayList<ExtensionProviderInfo> providers = new ArrayList<ExtensionProviderInfo>();
        providers.add(new ExtensionProviderInfo(RSMPacket.ELEMENT, RSMPacket.NAMESPACE, new RSMPacketProvider()));
        return providers;
    }
}
