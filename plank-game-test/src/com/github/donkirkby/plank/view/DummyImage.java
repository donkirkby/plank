package com.github.donkirkby.plank.view;

public class DummyImage implements GameComponentImage {
    private boolean isFlipped;
    private int drawCount;
    private float lastLeft;
    private float lastBottom;
    
    @Override
    public void flip() {
        isFlipped = ! isFlipped;
    }

    @Override
    public void draw(float left, float bottom) {
        drawCount++;
        lastLeft = left;
        lastBottom = bottom;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public float getLastLeft() {
        return lastLeft;
    }

    public float getLastBottom() {
        return lastBottom;
    }

}
