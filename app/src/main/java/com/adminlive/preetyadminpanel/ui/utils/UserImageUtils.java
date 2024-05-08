package com.adminlive.preetyadminpanel.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class UserImageUtils {

    public static Bitmap createUserImage(String userName, int imageSize) {
        // Get the first character of the user's name
        char firstChar = userName.charAt(0);

        // Create a Bitmap with the desired size
        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);

        // Create a Canvas to draw on the Bitmap
        Canvas canvas = new Canvas(bitmap);

        // Create a Paint object for styling
        Paint paint = new Paint();
        paint.setColor(Color.BLUE); // Set the text color
        paint.setTextSize(imageSize / 2); // Set the text size
        paint.setTextAlign(Paint.Align.CENTER);

        // Draw the first character onto the Bitmap
        Rect bounds = new Rect();
        paint.getTextBounds(String.valueOf(firstChar), 0, 1, bounds);
        float x = canvas.getWidth() / 2f;
        float y = (canvas.getHeight() / 2f) - ((paint.descent() + paint.ascent()) / 2f);
        canvas.drawText(String.valueOf(firstChar), x, y, paint);

        return bitmap;
    }
}
