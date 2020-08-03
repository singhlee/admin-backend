package org.singhlee.admin.common.enums;

/**
 * 工艺类型，1:炫彩,2:镭雕3:黑金
 */
public enum CraftsType {
    DAZZLING("炫彩", 1),
    LASER_CARVING("镭雕", 2),
    BLACK_GOLD("黑金",3);
    private String name;
    private int index;

    // 构造方法
    private CraftsType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (CraftsType c : CraftsType.values()) {
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
        for (CraftsType c : CraftsType.values()) {
            if (c.getIndex() == index) {
                return c.name();
            }
        }
        return null;
    }

    public static CraftsType getByIndex(int index) {
        for (CraftsType c : CraftsType.values()) {
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
