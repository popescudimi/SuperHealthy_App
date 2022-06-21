package com.example.superhealthyapp.adapters;


import com.example.superhealthyapp.R;

import java.util.List;

public class ApplicationViewModel {
    /**
     * This class holds the data parameters for Food
     */
    public String packageName;
    public String foodName;
    public String foodDescription;
    public String foodDownload;
    public String typeCalorie;
    public String foodGrams;
    public int caloriesNumber;

    ApplicationViewModel(String foodName, String foodDescription, String foodDownload, String typeCalorie, String foodGrams, int caloriesNumber) {
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodDownload = foodDownload;
        this.typeCalorie = typeCalorie;
        this.foodGrams = foodGrams;
        this.caloriesNumber = caloriesNumber;

    }


    public String getFoodName() {
        return foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public String getFoodDownload() {
        return foodDownload;
    }

    public String getTypeCalorie() {
        return typeCalorie;
    }

    public String getFoodGrams() {
        return foodGrams;
    }


    public int getCaloriesNumber() {
        return caloriesNumber;
    }



    public int[] LogoSet(int[] tabsSelected){
        int[] mTabIconsSelected= {
                    R.drawable.home,
                    R.drawable.food,
                    R.drawable.water,
                    R.drawable.stepcounter,
                    R.drawable.reminder };

        return tabsSelected;
        }


    public static List<ApplicationViewModel> foodItems(List<ApplicationViewModel> foodItemsList) {
        ApplicationViewModel Food1 = new ApplicationViewModel("Apple", "Apple - Fruit", "https://healthjade.com/wp-content/uploads/2017/10/apple-fruit.jpg", "Low Calorie", "100", 44);
        foodItemsList.add(Food1);
        ApplicationViewModel Food2 = new ApplicationViewModel("Banana", "Banana - Fruit", "https://s.iw.ro/gateway/g/ZmlsZVNvdXJjZT1odHRwJTNBJTJGJTJG/c3RvcmFnZWRpZ2l3b3JsZC5yY3MtcmRz/LnJvJTJGc3RvcmFnZSUyRjIwMjElMkYw/NSUyRjE3JTJGMTMyNzE2Ml8xMzI3MTYy/X2JhbmFuZS1lZmVjdGUtcGVudHJ1LXNh/bmF0YXRlLmpwZyZ3PTc4MCZoPTYwMCZ6/Yz0xJmhhc2g9MzUwYmYyMDkyMTRmMDM1MjZiM2JiMjRjODk3Y2Y4YTk=.thumb.jpg", "Low Calorie", "100", 65);
        foodItemsList.add(Food2);
        ApplicationViewModel Food3 = new ApplicationViewModel("Broccoli", "Broccoli - Leguma","https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/1adbb3b0-38f8-4cfa-ab94-1e1d36adfc7b/Derivates/c220c5c0-a313-48e1-a230-bf885c8be3b9.jpg","Very Low Calorie", "100", 32);
        foodItemsList.add(Food3);
        ApplicationViewModel Food4 = new ApplicationViewModel("Blackberries", "Blackberries - Fruit", "https://i0.wp.com/post.healthline.com/wp-content/uploads/2020/03/black-raspberries-1296x728-header.jpg?w=1575","Low calorie", "100", 25);
        foodItemsList.add(Food4);
        ApplicationViewModel Food5 = new ApplicationViewModel("Chocolate", "Chocolate - Sugar", "https://www.thespruceeats.com/thmb/h55qV5fnVTCne2i6vUxNyJRrbQc=/940x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/bittersweet-baking-chocolate-substitute-4153884-b14545739ef843e5be7af61af7e63606.jpg", "High", "100", 500);
        foodItemsList.add(Food5);
        ApplicationViewModel Food11 = new ApplicationViewModel("Chicken", "Chicken -Meat", "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/delish-190808-baked-drumsticks-0205-portrait-pf-1567089281.jpg?crop=1.00xw:0.667xh;0,0.150xh&resize=768:*", "Medium", "100", 200);
        foodItemsList.add(Food11);
        ApplicationViewModel Food6 = new ApplicationViewModel("Eggs fried", "Eggs fried - Normal food", "https://www.jessicagavin.com/wp-content/uploads/2020/09/how-to-fry-an-egg-3-600x900.jpg", "Med-High", "100", 180);
        foodItemsList.add(Food6);
        ApplicationViewModel Food12 = new ApplicationViewModel("Grapefruit", "Grapefruit - Fruit","https://mediacdn.libertatea.ro/unsafe/960x729/smart/filters:format(webp):contrast(8):quality(75)/https://static4.libertatea.ro/wp-content/uploads/2021/04/grapefruit-beneficii-si-proprietati.jpeg", "Low Calorie", "100", 32);
        foodItemsList.add(Food12);
        ApplicationViewModel Food13 = new ApplicationViewModel("Orange", "Orange - Fruit", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Oranges_-_whole-halved-segment.jpg/1280px-Oranges_-_whole-halved-segment.jpg", "Low Calorie", "100", 30);
        foodItemsList.add(Food13);
        ApplicationViewModel Food14 = new ApplicationViewModel("Mushrooms (fried)", "Mushrooms (fried) - Vegetable", "https://thecozyapron.com/wp-content/uploads/2020/05/sauteed-mushrooms_thecozyapron_1.jpg", "High", "100", 145);
        foodItemsList.add(Food14);
        ApplicationViewModel Food15 = new ApplicationViewModel("Pineapple", "Pineapple - Fruit", "https://cdn.shopify.com/s/files/1/0387/9079/1308/products/pineapple.jpg?v=1590948984", "Low Calorie", "100", 40);
        foodItemsList.add(Food15);
        ApplicationViewModel Food7 = new ApplicationViewModel("Honey", "Honey - Sugar", "https://sc04.alicdn.com/kf/U76c0bba5d6be44b89a7d4772bc72f545I.jpg","Medium","100",280);
        foodItemsList.add(Food7);
        ApplicationViewModel Food8 = new ApplicationViewModel("Pork", "Pork - Meat", "https://previews.123rf.com/images/kolesnikovserg/kolesnikovserg1906/kolesnikovserg190600212/124941512-sliced-raw-pork-meat-isolated-on-white-background.jpg?fj=1", "Medium-High", "100", 290);
        foodItemsList.add(Food8);
        ApplicationViewModel Food9 = new ApplicationViewModel("Sausage roll", "Sausage roll - Meat","https://img.taste.com.au/kv8NEC44/w720-h480-cfill-q80/taste/2016/11/easy-sausage-rolls-28532-1.jpeg", "High","100",480);
        foodItemsList.add(Food9);
        ApplicationViewModel Food10 = new ApplicationViewModel("Salmon fresh", "Salmon fresh - Fish", "https://st3.depositphotos.com/1031062/19027/i/1600/depositphotos_190272310-stock-photo-salmon-fresh-salmon-fish-raw.jpg", "Medium", "100", 180);
        foodItemsList.add(Food10);



        return foodItemsList;

    }
}


