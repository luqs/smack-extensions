package com.skysea.group;

import com.skysea.group.packet.ExtensionType;
import com.skysea.group.packet.GroupPacket;
import com.skysea.group.packet.RSMPacket;
import com.skysea.group.provider.GroupPacketProvider;
import com.skysea.group.provider.RSMPacketProvider;
import com.skysea.group.provider.SearchProvider;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 圈子协议的基础设施。
 * Created by zhangzhi on 2014/9/17.
 */
public final class GroupInfrastructure {
    private static final String LOG_TAG = "grou_xmmpp";
    private XMPPConnection connection;

    static {
        GroupPacketProvider provider = new GroupPacketProvider();
        ProviderManager.addIQProvider("x", GroupPacket.NAMESPACE, provider);
        ProviderManager.addIQProvider("query", GroupPacket.NAMESPACE, provider);
        ProviderManager.addExtensionProvider(RSMPacket.ELEMENT, RSMPacket.NAMESPACE, new RSMPacketProvider());
        ProviderManager.addIQProvider("query", "jabber:iq:search", new SearchProvider());
    }

    public GroupInfrastructure(XMPPConnection connection) {

        if(connection == null) {
            throw new NullPointerException("connection is null.");
        }
        this.connection = connection;
    }

    /**
     * 创建一个圈子。
     * @param groupInfo
     * @return 返回成功或失败。
     */
    public boolean createGroup(GroupInfo groupInfo) {
        if(groupInfo == null) {
            throw new NullPointerException("groupInfo is null");
        }
        final DataForm form = new DataForm("submit");
        FormField field = new FormField("name");
        field.addValue(groupInfo.getName());
        form.addField(field);

        field = new FormField("subject");
        field.addValue(groupInfo.getSubject());
        form.addField(field);

        field = new FormField("description");
        field.addValue(groupInfo.getDescription());
        form.addField(field);

        field = new FormField("category");
        field.addValue(String.valueOf(groupInfo.getCategory()));
        form.addField(field);

        field = new FormField("openness");
        field.addValue(groupInfo.getOpennessType().toString());
        form.addField(field);

        GroupPacket packet = new GroupPacket(ExtensionType.X);
        packet.setDataForm(form);
        packet.setType(IQ.Type.SET);

        try {
            packet = (GroupPacket) sendToService(packet);
            Form from = Form.getFormFrom(packet);
            groupInfo.setCreateTime(new Date());
            groupInfo.setJid(from.getField("jid").getValues().get(0));
            groupInfo.setOwner(StringUtils.parseName(connection.getUser()));
            groupInfo.setId(StringUtils.parseName(groupInfo.getJid()));
            groupInfo.setNumberOfMembers(1);
            return true;
        } catch (Exception e) {
            //Log.e(LOG_TAG, "创建圈子失败", e);
            return false;
        }

    }

    /**
     * 搜索圈子列表。
     */
    public Paging<GroupInfo> search(GroupQueryObject query, int offset, int limit) {
        UserSearch search = new UserSearch();
        search.setType(IQ.Type.SET);

        /* 构建搜索表单 */
        DataForm searchForm = new DataForm("submit");
        FormField field;
        if(query.getName() != null) {
            field = new FormField("name");
            field.addValue(query.getName());
            searchForm.addField(field);
        }

        if(query.getCategory() > 0) {
            field = new FormField("category");
            field.addValue(String.valueOf(query.getCategory()));
            searchForm.addField(field);
        }

        if(query.getGroupId() != null) {
            field = new FormField("id");
            field.addValue(query.getGroupId());
            searchForm.addField(field);
        }
        search.addExtension(searchForm);

        /* 构建分页参数 */
        RSMPacket rsm = new RSMPacket();
        rsm.setIndex(offset);
        rsm.setMax(limit == 0 ? 10 : limit);
        search.addExtension(rsm);

        try {
            search                  = (UserSearch)sendToService(search);
            DataForm resultFrom     = search.getExtension(DataForm.ELEMENT, DataForm.NAMESPACE);
            RSMPacket resultRsm     = RSMPacket.getRSMFrom(search);

            Paging<GroupInfo> paging = new Paging<GroupInfo>();
            paging.setOffset(offset);
            paging.setLimit(limit);
            if(resultRsm != null) {
                paging.setCount(resultRsm.getCount());
            }

            ArrayList<GroupInfo> groupInfos = new ArrayList<GroupInfo>(resultFrom.getItems().size());
            for (DataForm.Item item : resultFrom.getItems()){
                groupInfos.add(GroupInfoConverter.convert(item.getFields()));
            }

            paging.setItems(groupInfos);
            return paging;
        } catch (Exception e) {
            //Log.e(LOG_TAG, "搜索圈子列表失败", e);
            return null;
        }
    }

    private static class GroupInfoConverter{
        public static GroupInfo convert(Iterable<FormField> fieldList) {
            if(fieldList == null) {return null;}
            HashMap<String,FormField> fields = map(fieldList);
            GroupInfo groupInfo = new GroupInfo();

            FormField field = fields.get("id") ;
            if(field != null){
                groupInfo.setId(field.getValues().get(0));
            }

            field = fields.get("jid");
            if(field!= null) {
                groupInfo.setJid(field.getValues().get(0));
            }

            field = fields.get("owner");
            if(field!= null) {
                groupInfo.setOwner(field.getValues().get(0));
            }

            field = fields.get("name");
            if(field!= null) {
                groupInfo.setName(field.getValues().get(0));
            }

            field = fields.get("num_members");
            if(field!= null) {
                groupInfo.setNumberOfMembers(Integer.valueOf(field.getValues().get(0)));
            }

            field = fields.get("subject");
            if(field!= null) {
                groupInfo.setSubject(field.getValues().get(0));
            }

            field = fields.get("description");
            if(field!= null) {
                groupInfo.setDescription(field.getValues().get(0));
            }

            field = fields.get("openness");
            if(field != null) {
                groupInfo.setOpennessType(GroupInfo.OpennessType.valueOf(field.getValues().get(0)));
            }

            field = fields.get("createTime");
            if(field != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                format.setTimeZone(TimeZone.getTimeZone("utc"));
                try {
                    groupInfo.setCreateTime(format.parse(field.getValues().get(0)));
                } catch (ParseException e) {
                    //Log.e(LOG_TAG, "转换日志失败", e);
                }
            }
            return groupInfo;

        }

        private static HashMap<String, FormField> map(Iterable<FormField> fields) {

            int capacity = fields instanceof Collection ? ((Collection)fields).size() : 9;
            HashMap<String,FormField> hashMap = new HashMap<String, FormField>(capacity);

            for (FormField field:fields){
                hashMap.put(field.getVariable(), field);
            }
            return hashMap;
        }
    }

    private Packet sendToService(IQ packet) throws Exception {
        packet.setTo("group." + connection.getServiceName());
        return connection.createPacketCollectorAndSend(packet).nextResultOrThrow();
    }

}
