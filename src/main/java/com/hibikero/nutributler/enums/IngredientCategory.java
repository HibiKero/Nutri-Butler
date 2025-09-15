package com.hibikero.nutributler.enums;

/**
 * 食材分类枚举
 */
public enum IngredientCategory {
    
    VEGETABLES("蔬菜", "Vegetables"),
    FRUITS("水果", "Fruits"),
    MEAT_POULTRY("肉类禽类", "Meat & Poultry"),
    SEAFOOD("海鲜", "Seafood"),
    GRAINS_LEGUMES("谷物豆类", "Grains & Legumes"),
    DAIRY_EGGS("乳制品蛋类", "Dairy & Eggs"),
    NUTS_SEEDS("坚果种子", "Nuts & Seeds"),
    HERBS_SPICES("香草香料", "Herbs & Spices"),
    OILS_CONDIMENTS("油醋调味品", "Oils & Condiments"),
    SWEETENERS_BAKING("甜味剂烘焙", "Sweeteners & Baking"),
    OTHER("其他", "Other");

    private final String chineseName;
    private final String englishName;

    IngredientCategory(String chineseName, String englishName) {
        this.chineseName = chineseName;
        this.englishName = englishName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    /**
     * 根据中文名称获取分类
     */
    public static IngredientCategory fromChineseName(String chineseName) {
        for (IngredientCategory category : values()) {
            if (category.chineseName.equals(chineseName)) {
                return category;
            }
        }
        return OTHER;
    }

    /**
     * 根据英文名称获取分类
     */
    public static IngredientCategory fromEnglishName(String englishName) {
        for (IngredientCategory category : values()) {
            if (category.englishName.equals(englishName)) {
                return category;
            }
        }
        return OTHER;
    }

    /**
     * 获取所有分类的中文名称
     */
    public static String[] getAllChineseNames() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].chineseName;
        }
        return names;
    }
}
