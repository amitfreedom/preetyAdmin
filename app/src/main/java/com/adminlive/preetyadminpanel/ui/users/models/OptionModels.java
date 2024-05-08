package com.adminlive.preetyadminpanel.ui.users.models;

public class OptionModels {
    String title="";
    int icon;
    int color;

    public OptionModels() {
    }

    public OptionModels(String title, int icon, int color) {
        this.title = title;
        this.icon = icon;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
