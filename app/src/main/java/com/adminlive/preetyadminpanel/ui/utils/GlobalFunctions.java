package com.adminlive.preetyadminpanel.ui.utils;

public class GlobalFunctions {
    public static String capitalizeText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

}
