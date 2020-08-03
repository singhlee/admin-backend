package org.singhlee.admin.common.enums;

public enum ModelClass {
    LID("杯盖", 1),
    BODY("杯身", 2),
    LIDANDBODY("一体杯", 3);
    private String name;
    private int index;

    // 构造方法
    private ModelClass(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ModelClass c : ModelClass.values()) {
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
        for (ModelClass c : ModelClass.values()) {
            if (c.getIndex() == index) {
                return c.name();
            }
        }
        return null;
    }

    public static ModelClass getByIndex(int index) {
        for (ModelClass c : ModelClass.values()) {
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
