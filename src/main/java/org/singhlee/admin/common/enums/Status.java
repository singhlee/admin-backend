package org.singhlee.admin.common.enums;

public enum Status {
    ONLINE("上线", 0),
    OFFLINE("下线", 1);


    private String name;
    private int index;

    // 构造方法
    private Status(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (Status c : Status.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    /**
     * 根据index获取值
     */
    public static String getValue(int index) {
        for (Status c : Status.values()) {
            if (c.getIndex() == index) {
                return c.name();
            }
        }
        return null;
    }

    public static Status getByIndex(int index) {
        for (Status c : Status.values()) {
            if (c.getIndex() == index) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
