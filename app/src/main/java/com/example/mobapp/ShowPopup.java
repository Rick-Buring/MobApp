package com.example.mobapp;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mobapp.databinding.PopupWindowBinding;

public class ShowPopup {

    public interface PopupAction {
        void performAction();
    }

    private PopupWindow popupWindow;

    private final String message;
    private final String text1;
    private final String text2;

    private final LayoutInflater inflater;
    private final PopupAction popupAction;
    private final View view;



    public ShowPopup(String message, String text1, String text2, View view, LayoutInflater inflater, PopupAction popupAction) {
        this.message = message;
        this.text1 = text1;
        this.text2 = text2;
        this.view = view;
        this.inflater = inflater;
        this.popupAction = popupAction;
    }

    public void show() {
        // inflate the layout of the popup window
        View popupView = this.inflater.inflate(R.layout.popup_window, null);

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

    public void action() {
        this.popupAction.performAction();
        dismiss();
    }

    public void dismiss() {
        System.out.println("am i here?");
        popupWindow.dismiss();
    }

    public String getMessage() {
        return message;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}
