package com.github.donkirkby.plank.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureImage implements GameComponentImage {
    private TextureRegion region;
    private TextureRegion flippedRegion;
    private SpriteBatch batch;
    
    public TextureImage(TextureRegion region, SpriteBatch batch) {
        this.region = region;
        this.batch = batch;
    }

    @Override
    public void flip() {
        if (flippedRegion == null) {
            boolean flipX = false;
            boolean flipY = true;
            flippedRegion = new TextureRegion(region);
            flippedRegion.flip(flipX, flipY);
        }
        TextureRegion swap = region;
        region = flippedRegion;
        flippedRegion = swap;
    }

    @Override
    public void draw(float left, float bottom) {
        batch.draw(region, left, bottom);
    }

}
