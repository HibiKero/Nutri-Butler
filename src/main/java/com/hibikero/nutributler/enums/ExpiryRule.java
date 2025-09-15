package com.hibikero.nutributler.enums;

/**
 * 食材保质期规则枚举
 */
public enum ExpiryRule {
    
    // 蔬菜类
    LETTUCE("生菜", 3, "冷藏"),
    SPINACH("菠菜", 3, "冷藏"),
    CELERY("芹菜", 7, "冷藏"),
    CARROT("胡萝卜", 14, "冷藏"),
    POTATO("马铃薯", 30, "常温"),
    ONION("洋葱", 30, "常温"),
    GARLIC("大蒜", 60, "常温"),
    TOMATO("西红柿", 7, "冷藏"),
    CUCUMBER("黄瓜", 7, "冷藏"),
    PEPPER("辣椒", 7, "冷藏"),
    BROCCOLI("西兰花", 7, "冷藏"),
    CAULIFLOWER("花椰菜", 7, "冷藏"),
    CABBAGE("卷心菜", 14, "冷藏"),
    
    // 水果类
    BANANA("香蕉", 5, "常温"),
    APPLE("苹果", 14, "冷藏"),
    ORANGE("橙子", 14, "冷藏"),
    LEMON("柠檬", 14, "冷藏"),
    GRAPE("葡萄", 7, "冷藏"),
    STRAWBERRY("草莓", 3, "冷藏"),
    BLUEBERRY("蓝莓", 7, "冷藏"),
    CHERRY("樱桃", 7, "冷藏"),
    PEACH("桃子", 5, "冷藏"),
    PEAR("梨", 7, "冷藏"),
    WATERMELON("西瓜", 7, "冷藏"),
    PINEAPPLE("菠萝", 5, "冷藏"),
    MANGO("芒果", 5, "冷藏"),
    KIWI("猕猴桃", 7, "冷藏"),
    AVOCADO("牛油果", 5, "冷藏"),
    
    // 肉类
    CHICKEN("鸡肉", 2, "冷藏"),
    BEEF("牛肉", 3, "冷藏"),
    PORK("猪肉", 3, "冷藏"),
    LAMB("羊肉", 3, "冷藏"),
    TURKEY("火鸡肉", 2, "冷藏"),
    DUCK("鸭肉", 2, "冷藏"),
    
    // 海鲜类
    FISH("鱼肉", 1, "冷藏"),
    SALMON("三文鱼", 2, "冷藏"),
    SHRIMP("虾", 1, "冷藏"),
    LOBSTER("龙虾", 1, "冷藏"),
    CRAB("螃蟹", 1, "冷藏"),
    MUSSEL("贻贝", 1, "冷藏"),
    OYSTER("牡蛎", 1, "冷藏"),
    
    // 乳制品
    MILK("牛奶", 7, "冷藏"),
    CHEESE("奶酪", 14, "冷藏"),
    YOGURT("酸奶", 7, "冷藏"),
    BUTTER("黄油", 30, "冷藏"),
    CREAM("奶油", 7, "冷藏"),
    
    // 蛋类
    EGG("鸡蛋", 21, "冷藏"),
    DUCK_EGG("鸭蛋", 21, "冷藏"),
    QUAIL_EGG("鹌鹑蛋", 14, "冷藏"),
    
    // 豆制品
    TOFU("豆腐", 3, "冷藏"),
    SOY_MILK("豆浆", 3, "冷藏"),
    TEMPEH("天贝", 7, "冷藏"),
    
    // 谷物类
    RICE("大米", 365, "常温"),
    WHEAT("小麦", 365, "常温"),
    OATS("燕麦", 365, "常温"),
    QUINOA("藜麦", 365, "常温"),
    BARLEY("大麦", 365, "常温"),
    
    // 坚果类
    ALMOND("杏仁", 180, "常温"),
    WALNUT("核桃", 180, "常温"),
    PECAN("山核桃", 180, "常温"),
    CASHEW("腰果", 180, "常温"),
    PISTACHIO("开心果", 180, "常温"),
    HAZELNUT("榛子", 180, "常温"),
    MACADAMIA("夏威夷果", 180, "常温"),
    BRAZIL_NUT("巴西坚果", 180, "常温"),
    
    // 调料类
    SALT("盐", 3650, "常温"),
    SUGAR("糖", 1825, "常温"),
    BLACK_PEPPER("黑胡椒", 365, "常温"),
    CINNAMON("肉桂", 365, "常温"),
    GINGER("生姜", 14, "冷藏"),
    GARLIC_POWDER("蒜粉", 365, "常温"),
    ONION_POWDER("洋葱粉", 365, "常温"),
    OREGANO("牛至", 365, "常温"),
    BASIL("罗勒", 7, "冷藏"),
    PARSLEY("欧芹", 7, "冷藏"),
    CILANTRO("香菜", 5, "冷藏"),
    MINT("薄荷", 7, "冷藏"),
    THYME("百里香", 7, "冷藏"),
    ROSEMARY("迷迭香", 7, "冷藏"),
    SAGE("鼠尾草", 7, "冷藏"),
    BAY_LEAF("月桂叶", 365, "常温"),
    
    // 油类
    OLIVE_OIL("橄榄油", 730, "常温"),
    COCONUT_OIL("椰子油", 730, "常温"),
    AVOCADO_OIL("牛油果油", 730, "常温"),
    SUNFLOWER_OIL("葵花籽油", 730, "常温"),
    CANOLA_OIL("菜籽油", 730, "常温"),
    SESAME_OIL("芝麻油", 730, "常温"),
    
    // 冷冻食品
    FROZEN_VEGETABLES("冷冻蔬菜", 365, "冷冻"),
    FROZEN_FRUITS("冷冻水果", 365, "冷冻"),
    FROZEN_MEAT("冷冻肉类", 365, "冷冻"),
    FROZEN_FISH("冷冻鱼类", 180, "冷冻"),
    ICE_CREAM("冰淇淋", 90, "冷冻"),
    
    // 罐头食品
    CANNED_TOMATOES("罐头西红柿", 730, "常温"),
    CANNED_CORN("罐头玉米", 730, "常温"),
    CANNED_BEANS("罐头豆类", 730, "常温"),
    CANNED_FISH("罐头鱼类", 730, "常温"),
    CANNED_MEAT("罐头肉类", 730, "常温"),
    
    // 默认规则
    DEFAULT("默认", 7, "冷藏");

    private final String ingredientName;
    private final int expiryDays;
    private final String storageMethod;

    ExpiryRule(String ingredientName, int expiryDays, String storageMethod) {
        this.ingredientName = ingredientName;
        this.expiryDays = expiryDays;
        this.storageMethod = storageMethod;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getExpiryDays() {
        return expiryDays;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    /**
     * 根据食材名称获取保质期规则
     */
    public static ExpiryRule getRuleForIngredient(String ingredientName) {
        if (ingredientName == null) {
            return DEFAULT;
        }
        
        String lowerName = ingredientName.toLowerCase().trim();
        
        for (ExpiryRule rule : values()) {
            if (rule.ingredientName.equals(ingredientName) || 
                lowerName.contains(rule.ingredientName.toLowerCase())) {
                return rule;
            }
        }
        
        return DEFAULT;
    }

    /**
     * 根据食材名称获取保质期天数
     */
    public static int getExpiryDays(String ingredientName) {
        return getRuleForIngredient(ingredientName).getExpiryDays();
    }

    /**
     * 根据食材名称获取存储方法
     */
    public static String getStorageMethod(String ingredientName) {
        return getRuleForIngredient(ingredientName).getStorageMethod();
    }
}
