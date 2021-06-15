package com.example.mobapp;

/**
 * Callback interface to change the active image on the viewFlipper
 */
public interface viewFlipperCallback {

    /**
     * Makes the ViewFlipper go to the next image
     */
    void flipperNext();

    /**
     * Makes the ViewFlipper go to the previous image
     */
    void flipperPrevious();

    /**
     * Makes the ViewFlipper go to the next image, skipping one
     */
    void flipperSkipOne();
}
