package com.hibikero.nutributler.enums;

/**
 * 食材单位枚举
 */
public enum IngredientUnit {
    
    // 重量单位
    GRAM("克", "g", 1.0),
    KILOGRAM("千克", "kg", 1000.0),
    POUND("磅", "lb", 453.592),
    OUNCE("盎司", "oz", 28.3495),
    
    // 体积单位
    MILLILITER("毫升", "ml", 1.0),
    LITER("升", "l", 1000.0),
    CUP("杯", "cup", 240.0),
    TABLESPOON("汤匙", "tbsp", 15.0),
    TEASPOON("茶匙", "tsp", 5.0),
    
    // 数量单位
    PIECE("个", "pcs", 0.0), // 需要根据具体食材计算
    BUNCH("把", "bunch", 0.0),
    HEAD("颗", "head", 0.0),
    SLICE("片", "slice", 0.0),
    CLOVE("瓣", "clove", 0.0),
    
    // 中国常用单位
    JIN("斤", "jin", 500.0),
    LIANG("两", "liang", 50.0),
    TAI("台", "tai", 0.0),
    BAG("袋", "bag", 0.0),
    BOX("盒", "box", 0.0),
    BOTTLE("瓶", "bottle", 0.0),
    CAN("罐", "can", 0.0),
    PACK("包", "pack", 0.0);

    private final String chineseName;
    private final String englishName;
    private final double gramEquivalent; // 相对于克的换算比例

    IngredientUnit(String chineseName, String englishName, double gramEquivalent) {
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.gramEquivalent = gramEquivalent;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public double getGramEquivalent() {
        return gramEquivalent;
    }

    /**
     * 根据中文名称获取单位
     */
    public static IngredientUnit fromChineseName(String chineseName) {
        for (IngredientUnit unit : values()) {
            if (unit.chineseName.equals(chineseName)) {
                return unit;
            }
        }
        return PIECE; // 默认返回"个"
    }

    /**
     * 根据英文名称获取单位
     */
    public static IngredientUnit fromEnglishName(String englishName) {
        for (IngredientUnit unit : values()) {
            if (unit.englishName.equals(englishName)) {
                return unit;
            }
        }
        return PIECE; // 默认返回"个"
    }

    /**
     * 获取所有中文单位名称
     */
    public static String[] getAllChineseNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].chineseName;
        }
        return names;
    }

    /**
     * 获取所有英文单位名称
     */
    public static String[] getAllEnglishNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].englishName;
        }
        return names;
    }
}
