package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PlankView {
	private Vector2 centre;
	private float width;
	
	public PlankView(Plank plank, Vector2 centre, float width) {
		this.centre = centre;
		this.width = width;
	}

	public Vector2 getPosition(PieceColour colour) {
		return centre;
	}
	
	public float getLeft() {
		return centre.x - width/2;
	}
	
	public float getBottom() {
		return centre.y - width*1.5f;
	}
}
