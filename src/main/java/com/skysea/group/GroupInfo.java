package com.skysea.group;

/**
 * Created by zhangzhi on 2014/8/25.
 */
import java.util.Date;

/**
 * Created by zhangzhi on 2014/8/27.
 */
public class GroupInfo {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 圈子的开放类型。
     */
    public enum OpennessType {

        /**
         * 完全开放的，无论是谁都可加入。
         */
        PUBLIC,

        /**
         * 需要验证的，加入前需要经过圈子所有者验证同意。
         */
        AFFIRM_REQUIRED
    }
    private String owner;
    private String name;
    private String description;
    private OpennessType opennessType;
    private int category;
    private String logo;
    private Date createTime;
    private String subject;
    private int numberOfMembers;
    private String jid;
    private String id;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCategory() {
        return category;
    }

    public OpennessType getOpennessType() {
        return opennessType;
    }


    public void setOpennessType(OpennessType opennessType) {
        this.opennessType = opennessType;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getJid(){
        return this.jid;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }
}
