package com.skysea.group.packet;

/**
 * Created by zhangzhi on 2014/9/17.
 */
public enum ExtensionType {
    QUERY("query"),
    X("x");
    private String elementName;

    ExtensionType(String elementName){
        this.elementName = elementName;
    }

    @Override
    public String toString(){
        return this.elementName;
    }
}
