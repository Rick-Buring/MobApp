package com.example.mobapp;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mobapp.databinding.PopupWindowBinding;

public class ShowPopup {
    private PopupWindow popupWindow;
    private Activity activity;

    public ShowPopup(View view, LayoutInflater inflater, Activity activity) {
        // inflate the layout of the popup window
        this.activity = activity;
        View popupView = inflater.inflate(R.layout.popup_window, null);

        PopupWindowBinding binding = PopupWindowBinding.bind(popupView);
        binding.setPopup(this);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        this.popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            v.performClick();
            return true;
        });
    }

    public void finish(){
        if(activity!= null)
            activity.finish();
        dismiss();
    }

    public void dismiss(){
        System.out.println("am i here?");
        if(popupWindow != null)
        {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
