package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.entity.Ingredient;
import com.hibikero.nutributler.enums.IngredientCategory;
import com.hibikero.nutributler.repository.IngredientRepository;
import com.hibikero.nutributler.service.IngredientDataService;
import com.hibikero.nutributler.service.SpoonacularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 食材数据服务实现
 */
@Service
public class IngredientDataServiceImpl implements IngredientDataService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SpoonacularService spoonacularService;

    @Override
    public void initializeCommonIngredients() {
        System.out.println("开始初始化常用食材数据...");

        // 蔬菜类
        initializeVegetables();
        
        // 水果类
        initializeFruits();
        
        // 肉类和禽类
        initializeMeatAndPoultry();
        
        // 海鲜类
        initializeSeafood();
        
        // 谷物豆类
        initializeGrainsAndLegumes();
        
        // 乳制品和蛋类
        initializeDairyAndEggs();
        
        // 坚果和种子
        initializeNutsAndSeeds();
        
        // 香草和香料
        initializeHerbsAndSpices();
        
        // 油醋调味品
        initializeOilsAndCondiments();
        
        // 甜味剂和烘焙用品
        initializeSweetenersAndBaking();

        System.out.println("食材数据初始化完成！");
    }

    @Override
    public void initializeIngredientsByCategory(String category, List<String[]> ingredients) {
        System.out.println("正在初始化 " + category + " 类食材...");
        
        for (String[] ingredient : ingredients) {
            if (ingredient.length >= 2) {
                String chineseName = ingredient[0];
                String englishName = ingredient[1];
                
                try {
                    searchAndSaveIngredient(chineseName, englishName, category);
                    Thread.sleep(1000); // 避免API限制
                } catch (Exception e) {
                    System.err.println("初始化食材失败: " + chineseName + " - " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Ingredient searchAndSaveIngredient(String chineseName, String englishName, String category) {
        try {
            // 检查是否已存在
            if (ingredientRepository.findByName(chineseName).isPresent()) {
                System.out.println("食材已存在: " + chineseName);
                return ingredientRepository.findByName(chineseName).get();
            }

            // 使用英文名称搜索API
            List<Ingredient> searchResults = spoonacularService.searchIngredients(englishName, 1);
            
            if (!searchResults.isEmpty()) {
                Ingredient ingredient = searchResults.get(0);
                
                // 获取详细信息
                if (ingredient.getSpoonacularId() != null) {
                    Ingredient detailedIngredient = spoonacularService.getIngredientById(ingredient.getSpoonacularId());
                    if (detailedIngredient != null) {
                        ingredient = detailedIngredient;
                    }
                }
                
                // 设置中文名称和分类
                ingredient.setName(chineseName);
                ingredient.setNameEn(englishName);
                ingredient.setCategory(category);
                
                // 保存到数据库
                Ingredient savedIngredient = ingredientRepository.save(ingredient);
                System.out.println("已保存食材: " + chineseName + " (" + englishName + ")");
                return savedIngredient;
            } else {
                // 如果英文搜索失败，尝试中文搜索
                List<Ingredient> chineseResults = spoonacularService.searchIngredients(chineseName, 1);
                if (!chineseResults.isEmpty()) {
                    Ingredient ingredient = chineseResults.get(0);
                    ingredient.setName(chineseName);
                    ingredient.setNameEn(englishName);
                    ingredient.setCategory(category);
                    
                    Ingredient savedIngredient = ingredientRepository.save(ingredient);
                    System.out.println("已保存食材: " + chineseName + " (" + englishName + ")");
                    return savedIngredient;
                } else {
                    System.err.println("未找到食材: " + chineseName + " (" + englishName + ")");
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("搜索食材失败: " + chineseName + " - " + e.getMessage());
            return null;
        }
    }

    private void initializeVegetables() {
        List<String[]> vegetables = Arrays.asList(
            // 绿叶蔬菜
            new String[]{"菠菜", "Spinach"},
            new String[]{"生菜", "Lettuce"},
            new String[]{"卷心菜", "Cabbage"},
            new String[]{"大白菜", "Napa Cabbage"},
            new String[]{"小白菜", "Bok Choy"},
            
            // 根茎类蔬菜
            new String[]{"胡萝卜", "Carrot"},
            new String[]{"土豆", "Potato"},
            new String[]{"红薯", "Sweet Potato"},
            new String[]{"白萝卜", "Radish"},
            new String[]{"甜菜根", "Beetroot"},
            new String[]{"芋头", "Taro"},
            new String[]{"山药", "Yam"},
            new String[]{"莲藕", "Lotus Root"},
            new String[]{"姜", "Ginger"},
            
            // 葱蒜类
            new String[]{"洋葱", "Onion"},
            new String[]{"大蒜", "Garlic"},
            new String[]{"小葱", "Scallion"},
            new String[]{"韭菜", "Chives"},
            
            // 十字花科蔬菜
            new String[]{"西兰花", "Broccoli"},
            new String[]{"菜花", "Cauliflower"},
            new String[]{"孢子甘蓝", "Brussels Sprouts"},
            
            // 其他蔬菜
            new String[]{"番茄", "Tomato"},
            new String[]{"黄瓜", "Cucumber"},
            new String[]{"彩椒", "Bell Pepper"},
            new String[]{"茄子", "Eggplant"},
            new String[]{"西葫芦", "Zucchini"},
            new String[]{"芹菜", "Celery"},
            new String[]{"芦笋", "Asparagus"},
            new String[]{"蘑菇", "Mushroom"},
            new String[]{"香菇", "Shiitake Mushroom"},
            new String[]{"玉米", "Corn"},
            new String[]{"四季豆", "Green Bean"},
            new String[]{"豌豆", "Pea"},
            new String[]{"荷兰豆", "Snow Pea"},
            new String[]{"南瓜", "Pumpkin"},
            new String[]{"竹笋", "Bamboo Shoot"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.VEGETABLES.getChineseName(), vegetables);
    }

    private void initializeFruits() {
        List<String[]> fruits = Arrays.asList(
            new String[]{"苹果", "Apple"},
            new String[]{"香蕉", "Banana"},
            new String[]{"橙子", "Orange"},
            new String[]{"柠檬", "Lemon"},
            new String[]{"青柠", "Lime"},
            new String[]{"葡萄", "Grape"},
            new String[]{"草莓", "Strawberry"},
            new String[]{"蓝莓", "Blueberry"},
            new String[]{"覆盆子", "Raspberry"},
            new String[]{"牛油果", "Avocado"},
            new String[]{"芒果", "Mango"},
            new String[]{"菠萝", "Pineapple"},
            new String[]{"西瓜", "Watermelon"},
            new String[]{"桃子", "Peach"},
            new String[]{"梨", "Pear"},
            new String[]{"樱桃", "Cherry"},
            new String[]{"猕猴桃", "Kiwi"},
            new String[]{"李子", "Plum"},
            new String[]{"西柚", "Grapefruit"},
            new String[]{"木瓜", "Papaya"},
            new String[]{"石榴", "Pomegranate"},
            new String[]{"无花果", "Fig"},
            new String[]{"椰子", "Coconut"},
            new String[]{"荔枝", "Lychee"},
            new String[]{"龙眼", "Longan"},
            new String[]{"火龙果", "Dragon Fruit"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.FRUITS.getChineseName(), fruits);
    }

    private void initializeMeatAndPoultry() {
        List<String[]> meatAndPoultry = Arrays.asList(
            // 肉类
            new String[]{"牛肉", "Beef"},
            new String[]{"牛肉末", "Ground Beef"},
            new String[]{"牛排", "Steak"},
            new String[]{"猪肉", "Pork"},
            new String[]{"五花肉", "Pork Belly"},
            new String[]{"猪排", "Pork Chop"},
            new String[]{"培根", "Bacon"},
            new String[]{"香肠", "Sausage"},
            new String[]{"羊肉", "Lamb"},
            new String[]{"小牛肉", "Veal"},
            
            // 禽类
            new String[]{"鸡肉", "Chicken"},
            new String[]{"鸡胸肉", "Chicken Breast"},
            new String[]{"鸡腿肉", "Chicken Thigh"},
            new String[]{"鸡翅", "Chicken Wing"},
            new String[]{"鸭肉", "Duck"},
            new String[]{"火鸡肉", "Turkey"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.MEAT_POULTRY.getChineseName(), meatAndPoultry);
    }

    private void initializeSeafood() {
        List<String[]> seafood = Arrays.asList(
            // 鱼类
            new String[]{"三文鱼", "Salmon"},
            new String[]{"金枪鱼", "Tuna"},
            new String[]{"鳕鱼", "Cod"},
            new String[]{"罗非鱼", "Tilapia"},
            new String[]{"鳟鱼", "Trout"},
            new String[]{"鲭鱼", "Mackerel"},
            new String[]{"沙丁鱼", "Sardine"},
            new String[]{"海鲈鱼", "Sea Bass"},
            
            // 甲壳类及其他
            new String[]{"虾", "Shrimp"},
            new String[]{"螃蟹", "Crab"},
            new String[]{"龙虾", "Lobster"},
            new String[]{"扇贝", "Scallop"},
            new String[]{"蛤蜊", "Clam"},
            new String[]{"青口", "Mussel"},
            new String[]{"生蚝", "Oyster"},
            new String[]{"鱿鱼", "Squid"},
            new String[]{"章鱼", "Octopus"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.SEAFOOD.getChineseName(), seafood);
    }

    private void initializeGrainsAndLegumes() {
        List<String[]> grainsAndLegumes = Arrays.asList(
            // 谷物
            new String[]{"米饭", "Rice"},
            new String[]{"糙米", "Brown Rice"},
            new String[]{"藜麦", "Quinoa"},
            new String[]{"燕麦", "Oats"},
            new String[]{"大麦", "Barley"},
            new String[]{"玉米面", "Cornmeal"},
            new String[]{"面包", "Bread"},
            new String[]{"面粉", "Flour"},
            new String[]{"古斯米", "Couscous"},
            
            // 豆类
            new String[]{"扁豆", "Lentils"},
            new String[]{"鹰嘴豆", "Chickpeas"},
            new String[]{"黑豆", "Black Beans"},
            new String[]{"芸豆", "Kidney Beans"},
            new String[]{"黄豆", "Soybean"},
            new String[]{"豆腐", "Tofu"},
            new String[]{"毛豆", "Edamame"},
            new String[]{"绿豆", "Mung Bean"},
            
            // 意面
            new String[]{"意大利面", "Spaghetti"},
            new String[]{"通心粉", "Penne"},
            new String[]{"意大利宽面", "Fettuccine"},
            new String[]{"千层面", "Lasagna"},
            new String[]{"弯管面", "Macaroni"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.GRAINS_LEGUMES.getChineseName(), grainsAndLegumes);
    }

    private void initializeDairyAndEggs() {
        List<String[]> dairyAndEggs = Arrays.asList(
            new String[]{"牛奶", "Milk"},
            new String[]{"奶酪", "Cheese"},
            new String[]{"切达奶酪", "Cheddar Cheese"},
            new String[]{"马苏里拉奶酪", "Mozzarella Cheese"},
            new String[]{"帕玛森奶酪", "Parmesan Cheese"},
            new String[]{"菲达奶酪", "Feta Cheese"},
            new String[]{"酸奶", "Yogurt"},
            new String[]{"希腊酸奶", "Greek Yogurt"},
            new String[]{"黄油", "Butter"},
            new String[]{"奶油", "Cream"},
            new String[]{"酸奶油", "Sour Cream"},
            new String[]{"鸡蛋", "Egg"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.DAIRY_EGGS.getChineseName(), dairyAndEggs);
    }

    private void initializeNutsAndSeeds() {
        List<String[]> nutsAndSeeds = Arrays.asList(
            new String[]{"杏仁", "Almond"},
            new String[]{"核桃", "Walnut"},
            new String[]{"腰果", "Cashew"},
            new String[]{"开心果", "Pistachio"},
            new String[]{"花生", "Peanut"},
            new String[]{"山核桃", "Pecan"},
            new String[]{"夏威夷果", "Macadamia Nut"},
            new String[]{"芝麻", "Sesame Seed"},
            new String[]{"奇亚籽", "Chia Seed"},
            new String[]{"亚麻籽", "Flaxseed"},
            new String[]{"葵花籽", "Sunflower Seed"},
            new String[]{"南瓜籽", "Pumpkin Seed"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.NUTS_SEEDS.getChineseName(), nutsAndSeeds);
    }

    private void initializeHerbsAndSpices() {
        List<String[]> herbsAndSpices = Arrays.asList(
            // 香草
            new String[]{"罗勒", "Basil"},
            new String[]{"欧芹", "Parsley"},
            new String[]{"香菜", "Cilantro"},
            new String[]{"薄荷", "Mint"},
            new String[]{"迷迭香", "Rosemary"},
            new String[]{"百里香", "Thyme"},
            new String[]{"莳萝", "Dill"},
            new String[]{"牛至", "Oregano"},
            new String[]{"鼠尾草", "Sage"},
            new String[]{"月桂叶", "Bay Leaf"},
            
            // 香料
            new String[]{"黑胡椒", "Black Pepper"},
            new String[]{"盐", "Salt"},
            new String[]{"孜然", "Cumin"},
            new String[]{"红甜椒粉", "Paprika"},
            new String[]{"辣椒粉", "Chili Powder"},
            new String[]{"卡宴辣椒粉", "Cayenne Pepper"},
            new String[]{"肉桂", "Cinnamon"},
            new String[]{"肉豆蔻", "Nutmeg"},
            new String[]{"丁香", "Cloves"},
            new String[]{"八角", "Star Anise"},
            new String[]{"花椒", "Sichuan Peppercorn"},
            new String[]{"茴香籽", "Fennel Seed"},
            new String[]{"豆蔻", "Cardamom"},
            new String[]{"芥菜籽", "Mustard Seed"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.HERBS_SPICES.getChineseName(), herbsAndSpices);
    }

    private void initializeOilsAndCondiments() {
        List<String[]> oilsAndCondiments = Arrays.asList(
            // 油和脂肪
            new String[]{"橄榄油", "Olive Oil"},
            new String[]{"植物油", "Vegetable Oil"},
            new String[]{"菜籽油", "Canola Oil"},
            new String[]{"香油", "Sesame Oil"},
            new String[]{"椰子油", "Coconut Oil"},
            new String[]{"牛油果油", "Avocado Oil"},
            
            // 醋
            new String[]{"意大利黑醋", "Balsamic Vinegar"},
            new String[]{"苹果醋", "Apple Cider Vinegar"},
            new String[]{"米醋", "Rice Vinegar"},
            new String[]{"红酒醋", "Red Wine Vinegar"},
            
            // 调味品和酱料
            new String[]{"酱油", "Soy Sauce"},
            new String[]{"蚝油", "Oyster Sauce"},
            new String[]{"海鲜酱", "Hoisin Sauce"},
            new String[]{"番茄酱", "Ketchup"},
            new String[]{"芥末酱", "Mustard"},
            new String[]{"蛋黄酱", "Mayonnaise"},
            new String[]{"辣酱", "Hot Sauce"},
            new String[]{"伍斯特酱", "Worcestershire Sauce"},
            new String[]{"鱼露", "Fish Sauce"},
            new String[]{"味增酱", "Miso Paste"},
            new String[]{"芝麻酱", "Tahini"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.OILS_CONDIMENTS.getChineseName(), oilsAndCondiments);
    }

    private void initializeSweetenersAndBaking() {
        List<String[]> sweetenersAndBaking = Arrays.asList(
            new String[]{"糖", "Sugar"},
            new String[]{"红糖", "Brown Sugar"},
            new String[]{"蜂蜜", "Honey"},
            new String[]{"枫糖浆", "Maple Syrup"},
            new String[]{"泡打粉", "Baking Powder"},
            new String[]{"小苏打", "Baking Soda"},
            new String[]{"酵母", "Yeast"},
            new String[]{"可可粉", "Cocoa Powder"},
            new String[]{"香草精", "Vanilla Extract"},
            new String[]{"巧克力", "Chocolate"}
        );
        
        initializeIngredientsByCategory(IngredientCategory.SWEETENERS_BAKING.getChineseName(), sweetenersAndBaking);
    }
}
