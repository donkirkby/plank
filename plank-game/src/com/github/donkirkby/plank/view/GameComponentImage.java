package com.github.donkirkby.plank.view;

public interface GameComponentImage {
    /**
     * Flip the image vertically. 
     */
    public void flip();
    
    /**
     * Draw the image at the given coordinates.
     * @param left the coordinate of the left side of the image
     * @param bottom the coordinate of the bottom of the image
     */
    public void draw(float left, float bottom);
}
