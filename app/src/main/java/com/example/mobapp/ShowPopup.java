package com.example.mobapp;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mobapp.databinding.PopupWindowBinding;

/**
 * Shows a popup on the screen. The users can interact with the popup
 */
public class ShowPopup {

    /**
     * Inner interface to make the actions happen
     */
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

    /**
     * Constructor for the ShowPopup
     * @param message  The message to display on screen
     * @param text1  The text on the first button
     * @param text2  The text on the second button
     * @param view  The view where the popups needs to show up on
     * @param inflater  The inflater to create the popup
     * @param popupAction  The acton performed by the popup
     */
    public ShowPopup(String message, String text1, String text2, View view, LayoutInflater inflater, PopupAction popupAction) {
        this.message = message;
        this.text1 = text1;
        this.text2 = text2;
        this.view = view;
        this.inflater = inflater;
        this.popupAction = popupAction;
    }

    /**
     * Starts the popup
     */
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

    /**
     * Perform the action of the popup
     */
    public void action() {
        this.popupAction.performAction();
        dismiss();
    }

    /**
     * Closes the popup
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    /**
     * Getter for this.message
     * @return  this.message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for this.text1
     * @return  this.
     */
    public String getText1() {
        return text1;
    }

    /**
     * Getter for this.text2
     * @return  this.text2
     */
    public String getText2() {
        return text2;
    }
}
