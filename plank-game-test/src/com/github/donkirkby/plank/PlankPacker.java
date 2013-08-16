package com.github.donkirkby.plank;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class PlankPacker {
    public static void main (String[] args) throws Exception {
        TexturePacker2.process(
                "raw-assets", 
                "../plank-game-android/assets/atlas", 
                "plank.pack");
    }
}
