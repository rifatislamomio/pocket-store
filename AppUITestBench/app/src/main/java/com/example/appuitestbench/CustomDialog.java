package com.example.appuitestbench;

import android.app.Dialog;
import android.content.Context;

public class CustomDialog {
    Dialog dialog;
    public CustomDialog(Context context)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading_dialog);
    }
    public void showDialog()
    {
        dialog.show();
    }

    public void hideDialog()
    {
        dialog.dismiss();
    }

}
