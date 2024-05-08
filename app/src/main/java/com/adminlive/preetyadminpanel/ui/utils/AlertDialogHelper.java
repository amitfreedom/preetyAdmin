package com.adminlive.preetyadminpanel.ui.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogHelper {

    public interface OnOkButtonClickListener {
        void onOkButtonClicked();
    }

    public static void showAgencyCreatedDialog(Context context,String message,OnOkButtonClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle("Success")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Notify listener when OK button is clicked
                    if (listener != null) {
                        listener.onOkButtonClicked();
                    }
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

