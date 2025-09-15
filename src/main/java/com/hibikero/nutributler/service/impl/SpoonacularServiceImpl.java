package com.hibikero.nutributler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibikero.nutributler.entity.Ingredient;
import com.hibikero.nutributler.repository.IngredientRepository;
import com.hibikero.nutributler.service.SpoonacularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Spoonacular API服务实现
 */
@Service
public class SpoonacularServiceImpl implements SpoonacularService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spoonacular.api.key}")
    private String apiKey;

    @Value("${spoonacular.api.base-url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Ingredient> searchIngredients(String query, int number) {
        try {
            String url = String.format("%s/food/ingredients/search?apiKey=%s&query=%s&number=%d", 
                baseUrl, apiKey, query, number);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.get("results");

            List<Ingredient> ingredients = new ArrayList<>();
            if (results != null && results.isArray()) {
                for (JsonNode result : results) {
                    Ingredient ingredient = parseIngredientFromSearch(result);
                    if (ingredient != null) {
                        ingredients.add(ingredient);
                    }
                }
            }
            return ingredients;
        } catch (Exception e) {
            System.err.println("搜索食材失败: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Ingredient getIngredientById(Integer id) {
        try {
            String url = String.format("%s/food/ingredients/%d/information?apiKey=%s&amount=100&unit=g", 
                baseUrl, id, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            return parseIngredientFromDetail(root);
        } catch (Exception e) {
            System.err.println("获取食材详情失败: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Ingredient> getIngredientsByIds(List<Integer> ids) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Integer id : ids) {
            Ingredient ingredient = getIngredientById(id);
            if (ingredient != null) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public void initializeCommonIngredients() {
        // 常用食材列表
        String[] commonIngredients = {
            "马铃薯", "土豆", "potato",
            "胡萝卜", "carrot",
            "洋葱", "onion",
            "西红柿", "番茄", "tomato",
            "鸡蛋", "egg",
            "鸡肉", "chicken",
            "牛肉", "beef",
            "猪肉", "pork",
            "大米", "rice",
            "面条", "noodles",
            "面包", "bread",
            "牛奶", "milk",
            "香蕉", "banana",
            "苹果", "apple",
            "橙子", "orange",
            "菠菜", "spinach",
            "西兰花", "broccoli",
            "蘑菇", "mushroom",
            "豆腐", "tofu"
        };

        System.out.println("开始初始化常用食材数据...");
        
        for (String ingredientName : commonIngredients) {
            try {
                // 检查是否已存在
                if (ingredientRepository.findByName(ingredientName).isPresent()) {
                    continue;
                }

                // 搜索食材
                List<Ingredient> searchResults = searchIngredients(ingredientName, 1);
                if (!searchResults.isEmpty()) {
                    Ingredient ingredient = searchResults.get(0);
                    
                    // 获取详细信息
                    if (ingredient.getSpoonacularId() != null) {
                        Ingredient detailedIngredient = getIngredientById(ingredient.getSpoonacularId());
                        if (detailedIngredient != null) {
                            ingredient = detailedIngredient;
                        }
                    }
                    
                    // 保存到数据库
                    ingredientRepository.save(ingredient);
                    System.out.println("已保存食材: " + ingredient.getName());
                }
                
                // 避免API限制
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("初始化食材失败: " + ingredientName + " - " + e.getMessage());
            }
        }
        
        System.out.println("食材数据初始化完成！");
    }

    private Ingredient parseIngredientFromSearch(JsonNode result) {
        try {
            Ingredient ingredient = new Ingredient();
            String englishName = result.get("name").asText();
            ingredient.setNameEn(englishName);
            
            // 尝试将英文名称转换为中文名称
            String chineseName = translateToChinese(englishName);
            ingredient.setName(chineseName);
            
            ingredient.setSpoonacularId(result.get("id").asInt());
            ingredient.setImageUrl("https://spoonacular.com/cdn/ingredients_100x100/" + result.get("image").asText());
            ingredient.setCategory("未知");
            return ingredient;
        } catch (Exception e) {
            System.err.println("解析搜索结果失败: " + e.getMessage());
            return null;
        }
    }

    private Ingredient parseIngredientFromDetail(JsonNode root) {
        try {
            Ingredient ingredient = new Ingredient();
            
            // 基本信息
            String englishName = root.get("name").asText();
            ingredient.setNameEn(englishName);
            
            // 尝试将英文名称转换为中文名称
            String chineseName = translateToChinese(englishName);
            ingredient.setName(chineseName);
            
            ingredient.setSpoonacularId(root.get("id").asInt());
            ingredient.setImageUrl("https://spoonacular.com/cdn/ingredients_100x100/" + root.get("image").asText());
            
            // 分类
            if (root.has("categoryPath") && root.get("categoryPath").isArray()) {
                JsonNode categoryPath = root.get("categoryPath");
                if (categoryPath.size() > 0) {
                    ingredient.setCategory(categoryPath.get(categoryPath.size() - 1).asText());
                }
            }
            
            // 营养成分
            JsonNode nutrition = root.get("nutrition");
            if (nutrition != null) {
                JsonNode nutrients = nutrition.get("nutrients");
                if (nutrients != null && nutrients.isArray()) {
                    for (JsonNode nutrient : nutrients) {
                        String name = nutrient.get("name").asText();
                        BigDecimal amount = BigDecimal.valueOf(nutrient.get("amount").asDouble());
                        
                        switch (name.toLowerCase()) {
                            case "calories":
                                ingredient.setCalories(amount);
                                break;
                            case "protein":
                                ingredient.setProtein(amount);
                                break;
                            case "carbohydrates":
                                ingredient.setCarbs(amount);
                                break;
                            case "fat":
                                ingredient.setFat(amount);
                                break;
                            case "fiber":
                                ingredient.setFiber(amount);
                                break;
                            case "sugar":
                                ingredient.setSugar(amount);
                                break;
                            case "sodium":
                                ingredient.setSodium(amount);
                                break;
                            case "cholesterol":
                                ingredient.setCholesterol(amount);
                                break;
                            case "vitamin c":
                                ingredient.setVitaminC(amount);
                                break;
                            case "calcium":
                                ingredient.setCalcium(amount);
                                break;
                            case "iron":
                                ingredient.setIron(amount);
                                break;
                        }
                    }
                }
            }
            
            return ingredient;
        } catch (Exception e) {
            System.err.println("解析食材详情失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 将英文食材名称翻译为中文
     */
    private String translateToChinese(String englishName) {
        // 常见食材英文到中文的映射
        java.util.Map<String, String> translationMap = new java.util.HashMap<>();
        translationMap.put("potato", "马铃薯");
        translationMap.put("carrot", "胡萝卜");
        translationMap.put("onion", "洋葱");
        translationMap.put("tomato", "西红柿");
        translationMap.put("egg", "鸡蛋");
        translationMap.put("chicken", "鸡肉");
        translationMap.put("beef", "牛肉");
        translationMap.put("pork", "猪肉");
        translationMap.put("rice", "大米");
        translationMap.put("noodles", "面条");
        translationMap.put("bread", "面包");
        translationMap.put("milk", "牛奶");
        translationMap.put("banana", "香蕉");
        translationMap.put("apple", "苹果");
        translationMap.put("orange", "橙子");
        translationMap.put("spinach", "菠菜");
        translationMap.put("broccoli", "西兰花");
        translationMap.put("mushroom", "蘑菇");
        translationMap.put("tofu", "豆腐");
        translationMap.put("cabbage", "卷心菜");
        translationMap.put("lettuce", "生菜");
        translationMap.put("cucumber", "黄瓜");
        translationMap.put("pepper", "辣椒");
        translationMap.put("garlic", "大蒜");
        translationMap.put("ginger", "生姜");
        translationMap.put("fish", "鱼肉");
        translationMap.put("salmon", "三文鱼");
        translationMap.put("shrimp", "虾");
        translationMap.put("lobster", "龙虾");
        translationMap.put("cheese", "奶酪");
        translationMap.put("butter", "黄油");
        translationMap.put("oil", "油");
        translationMap.put("salt", "盐");
        translationMap.put("sugar", "糖");
        translationMap.put("flour", "面粉");
        translationMap.put("corn", "玉米");
        translationMap.put("peas", "豌豆");
        translationMap.put("beans", "豆类");
        translationMap.put("nuts", "坚果");
        translationMap.put("almonds", "杏仁");
        translationMap.put("walnuts", "核桃");
        translationMap.put("strawberry", "草莓");
        translationMap.put("grape", "葡萄");
        translationMap.put("lemon", "柠檬");
        translationMap.put("lime", "青柠");
        translationMap.put("peach", "桃子");
        translationMap.put("pear", "梨");
        translationMap.put("cherry", "樱桃");
        translationMap.put("watermelon", "西瓜");
        translationMap.put("pineapple", "菠萝");
        translationMap.put("mango", "芒果");
        translationMap.put("kiwi", "猕猴桃");
        translationMap.put("avocado", "牛油果");
        translationMap.put("coconut", "椰子");
        translationMap.put("pomegranate", "石榴");
        translationMap.put("fig", "无花果");
        translationMap.put("date", "枣");
        translationMap.put("raisin", "葡萄干");
        translationMap.put("cranberry", "蔓越莓");
        translationMap.put("blueberry", "蓝莓");
        translationMap.put("raspberry", "覆盆子");
        translationMap.put("blackberry", "黑莓");
        translationMap.put("gooseberry", "醋栗");
        translationMap.put("elderberry", "接骨木莓");
        translationMap.put("currant", "醋栗");
        translationMap.put("huckleberry", "越橘");
        translationMap.put("boysenberry", "波森莓");
        translationMap.put("loganberry", "罗甘莓");
        translationMap.put("tayberry", "泰莓");
        translationMap.put("wineberry", "酒莓");
        translationMap.put("cloudberry", "云莓");
        translationMap.put("lingonberry", "越橘");
        translationMap.put("bilberry", "越橘");
        translationMap.put("mulberry", "桑葚");
        translationMap.put("elderflower", "接骨木花");
        translationMap.put("rosehip", "玫瑰果");
        translationMap.put("sea buckthorn", "沙棘");
        translationMap.put("goji berry", "枸杞");
        translationMap.put("acai berry", "巴西莓");
        translationMap.put("maqui berry", "马基莓");
        translationMap.put("camu camu", "卡姆果");
        translationMap.put("inca berry", "印加果");
        translationMap.put("golden berry", "金果");
        translationMap.put("physalis", "酸浆");
        translationMap.put("cape gooseberry", "开普醋栗");
        translationMap.put("ground cherry", "地樱桃");
        translationMap.put("tomatillo", "墨西哥酸浆");
        translationMap.put("japanese lantern", "日本灯笼果");
        translationMap.put("chinese lantern", "中国灯笼果");
        translationMap.put("winter cherry", "冬樱桃");
        translationMap.put("husk cherry", "壳樱桃");
        translationMap.put("strawberry tomato", "草莓番茄");
        translationMap.put("dwarf cape gooseberry", "矮开普醋栗");
        translationMap.put("peruvian ground cherry", "秘鲁地樱桃");
        translationMap.put("brazilian cherry", "巴西樱桃");
        translationMap.put("surinam cherry", "苏里南樱桃");
        translationMap.put("pitanga", "皮坦加");
        translationMap.put("eugenia uniflora", "尤金尼亚");
        translationMap.put("cayenne cherry", "卡宴樱桃");
        translationMap.put("arazá", "阿拉萨");
        translationMap.put("eugenia stipitata", "尤金尼亚");
        translationMap.put("amazon cherry", "亚马逊樱桃");
        translationMap.put("cerrado cherry", "塞拉多樱桃");
        translationMap.put("savanna cherry", "萨凡纳樱桃");
        translationMap.put("cerrado", "塞拉多");
        translationMap.put("savanna", "萨凡纳");
        translationMap.put("amazon", "亚马逊");
        translationMap.put("brazil", "巴西");
        translationMap.put("peru", "秘鲁");
        translationMap.put("colombia", "哥伦比亚");
        translationMap.put("venezuela", "委内瑞拉");
        translationMap.put("guyana", "圭亚那");
        translationMap.put("suriname", "苏里南");
        translationMap.put("french guiana", "法属圭亚那");
        translationMap.put("ecuador", "厄瓜多尔");
        translationMap.put("bolivia", "玻利维亚");
        translationMap.put("paraguay", "巴拉圭");
        translationMap.put("uruguay", "乌拉圭");
        translationMap.put("argentina", "阿根廷");
        translationMap.put("chile", "智利");
        translationMap.put("mexico", "墨西哥");
        translationMap.put("guatemala", "危地马拉");
        translationMap.put("belize", "伯利兹");
        translationMap.put("honduras", "洪都拉斯");
        translationMap.put("el salvador", "萨尔瓦多");
        translationMap.put("nicaragua", "尼加拉瓜");
        translationMap.put("costa rica", "哥斯达黎加");
        translationMap.put("panama", "巴拿马");
        translationMap.put("cuba", "古巴");
        translationMap.put("jamaica", "牙买加");
        translationMap.put("haiti", "海地");
        translationMap.put("dominican republic", "多米尼加共和国");
        translationMap.put("puerto rico", "波多黎各");
        translationMap.put("trinidad and tobago", "特立尼达和多巴哥");
        translationMap.put("barbados", "巴巴多斯");
        translationMap.put("saint lucia", "圣卢西亚");
        translationMap.put("saint vincent and the grenadines", "圣文森特和格林纳丁斯");
        translationMap.put("grenada", "格林纳达");
        translationMap.put("antigua and barbuda", "安提瓜和巴布达");
        translationMap.put("saint kitts and nevis", "圣基茨和尼维斯");
        translationMap.put("dominica", "多米尼克");
        translationMap.put("montserrat", "蒙特塞拉特");
        translationMap.put("anguilla", "安圭拉");
        translationMap.put("british virgin islands", "英属维尔京群岛");
        translationMap.put("u.s. virgin islands", "美属维尔京群岛");
        translationMap.put("turks and caicos islands", "特克斯和凯科斯群岛");
        translationMap.put("cayman islands", "开曼群岛");
        translationMap.put("bermuda", "百慕大");
        translationMap.put("bahamas", "巴哈马");
        translationMap.put("aruba", "阿鲁巴");
        translationMap.put("curaçao", "库拉索");
        translationMap.put("sint maarten", "圣马丁");
        translationMap.put("sint eustatius", "圣尤斯特歇斯");
        translationMap.put("saba", "萨巴");
        translationMap.put("bonaire", "博奈尔");
        translationMap.put("saint barthélemy", "圣巴泰勒米");
        translationMap.put("saint martin", "圣马丁");
        translationMap.put("guadeloupe", "瓜德罗普");
        translationMap.put("martinique", "马提尼克");
        translationMap.put("saint pierre and miquelon", "圣皮埃尔和密克隆");
        translationMap.put("greenland", "格陵兰");
        translationMap.put("canada", "加拿大");
        translationMap.put("united states", "美国");
        translationMap.put("mexico", "墨西哥");
        translationMap.put("guatemala", "危地马拉");
        translationMap.put("belize", "伯利兹");
        translationMap.put("honduras", "洪都拉斯");
        translationMap.put("el salvador", "萨尔瓦多");
        translationMap.put("nicaragua", "尼加拉瓜");
        translationMap.put("costa rica", "哥斯达黎加");
        translationMap.put("panama", "巴拿马");
        translationMap.put("cuba", "古巴");
        translationMap.put("jamaica", "牙买加");
        translationMap.put("haiti", "海地");
        translationMap.put("dominican republic", "多米尼加共和国");
        translationMap.put("puerto rico", "波多黎各");
        translationMap.put("trinidad and tobago", "特立尼达和多巴哥");
        translationMap.put("barbados", "巴巴多斯");
        translationMap.put("saint lucia", "圣卢西亚");
        translationMap.put("saint vincent and the grenadines", "圣文森特和格林纳丁斯");
        translationMap.put("grenada", "格林纳达");
        translationMap.put("antigua and barbuda", "安提瓜和巴布达");
        translationMap.put("saint kitts and nevis", "圣基茨和尼维斯");
        translationMap.put("dominica", "多米尼克");
        translationMap.put("montserrat", "蒙特塞拉特");
        translationMap.put("anguilla", "安圭拉");
        translationMap.put("british virgin islands", "英属维尔京群岛");
        translationMap.put("u.s. virgin islands", "美属维尔京群岛");
        translationMap.put("turks and caicos islands", "特克斯和凯科斯群岛");
        translationMap.put("cayman islands", "开曼群岛");
        translationMap.put("bermuda", "百慕大");
        translationMap.put("bahamas", "巴哈马");
        translationMap.put("aruba", "阿鲁巴");
        translationMap.put("curaçao", "库拉索");
        translationMap.put("sint maarten", "圣马丁");
        translationMap.put("sint eustatius", "圣尤斯特歇斯");
        translationMap.put("saba", "萨巴");
        translationMap.put("bonaire", "博奈尔");
        translationMap.put("saint barthélemy", "圣巴泰勒米");
        translationMap.put("saint martin", "圣马丁");
        translationMap.put("guadeloupe", "瓜德罗普");
        translationMap.put("martinique", "马提尼克");
        translationMap.put("saint pierre and miquelon", "圣皮埃尔和密克隆");
        translationMap.put("greenland", "格陵兰");
        translationMap.put("canada", "加拿大");
        translationMap.put("united states", "美国");
        
        // 转换为小写进行匹配
        String lowerCaseName = englishName.toLowerCase().trim();
        
        // 直接匹配
        if (translationMap.containsKey(lowerCaseName)) {
            return translationMap.get(lowerCaseName);
        }
        
        // 部分匹配（处理复数形式等）
        for (java.util.Map.Entry<String, String> entry : translationMap.entrySet()) {
            if (lowerCaseName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // 如果没有找到翻译，返回英文名称
        return englishName;
    }
}
