package com.adminlive.preetyadminpanel.ui.home.models;

public class TopOptionsModel {

    String count="0";
    String title="";
    int icon;
    int color;

    public TopOptionsModel() {
    }

    public TopOptionsModel(String count, String title, int icon,int color) {
        this.count = count;
        this.title = title;
        this.icon = icon;
        this.color = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
