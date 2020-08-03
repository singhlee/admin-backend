package org.singhlee.admin.common.enums;

/**
 * @program: admin-backend
 * @description:  状态,0:初始, 1:待下单,2:已下单
 * @author: singhlee
 * @date: 2020-07-06 17:58
 **/
public enum CaseStatus {
    INITIAL("初始", 0),
    WAITORDER("待下单", 1),
    ORDERED("已下单", 2);
    private String name;
    private int index;

    // 构造方法
    private CaseStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (CaseStatus c : CaseStatus.values()) {
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

    public static CaseStatus getByIndex(int index) {
        for (CaseStatus c : CaseStatus.values()) {
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
