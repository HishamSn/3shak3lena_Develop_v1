package a3lena.a3shak.com.a3shak3lena.Dealers.Category;


import java.util.ArrayList;

import a3lena.a3shak.com.a3shak3lena.Dealers.Items.ItemsModel;


/**
 * Created by aldaboubi on 8/16/2017.
 */

public class CategoryModel {

    private String CategoryID;
    private String CategoryName;
    private String CategoryIcon;
    private ArrayList<ItemsModel> items;
    private String CategoryType;


    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryIcon() {
        return CategoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        CategoryIcon = categoryIcon;
    }

    public ArrayList<ItemsModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsModel> items) {
        this.items = items;
    }


    public String getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(String categoryType) {
        CategoryType = categoryType;
    }
}
