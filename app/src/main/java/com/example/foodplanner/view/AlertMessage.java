package com.example.foodplanner.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class AlertMessage {
    public static void showToastMessage(String message, Context context){
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }

    public static void showCustomAlertDialog(Context context,String message,String positiveText,DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("")
                .setMessage(message)
                .setPositiveButton(positiveText, positiveClickListener)
                .setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss())
                .show();
    }

}
