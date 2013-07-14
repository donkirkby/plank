package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class PlankView {
	private Plank plank;
	private Vector2 centre;
	private float width;
	
	public PlankView(Plank plank, Vector2 centre, float width) {
		this.plank = plank;
		this.centre = centre;
		this.width = width;
	}

	public Vector2 getPosition(Piece piece) {
		int index = plank.indexOf(piece);
		return centre.cpy().add(0, (1-index)*width);
	}
	
	public float getLeft() {
		return centre.x - width/2;
	}
	
	public float getBottom() {
		return centre.y - width*1.5f;
	}
}
