package com.example.monederoapp;

public class Category {
    private int cateogry_id;
    private String category_Name;
    private boolean isSelected;

    public int getCateogry_id() {
        return cateogry_id;
    }
    public void setCateogry_id(int cateogry_id) {
        this.cateogry_id = cateogry_id;
    }
    public String getCategory_Name() {
        return category_Name;
    }
    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
