package com.tengteng.ui.recycleview;

/**
 * @author yejiasun
 * @date Create on 12/14/22
 */

class CustomItemBean {
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * ITEM的显示内容
     */
    private String content;

    public CustomItemBean(String groupName, String content) {
        this.groupName = groupName;
        this.content = content;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
