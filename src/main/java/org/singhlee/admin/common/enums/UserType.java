package org.singhlee.admin.common.enums;

/**
 * 1:普通用户,2:大客户3:设计师，4:游客,5:业务员
 */
public enum UserType {
    GENERAL("普通", 1),
    BIGCUSTOMER("大客户", 2),
    DESIGNER("设计师",3),
    VISITOR("游客",4),
    SALESMAN("业务员",5);

    private String name;
    private int index;

    // 构造方法
    private UserType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (UserType c : UserType.values()) {
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
