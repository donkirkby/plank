package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class PlankView extends GameComponentView {
	private Plank plank;
	private float width;
	
	public PlankView(Plank plank, Vector2 centre, float width) {
		super(centre);
		this.plank = plank;
		this.width = width;
	}

	public Vector2 getPosition(Piece piece) {
		int index = plank.indexOf(piece);
		return getCentre().cpy().add(0, (1-index)*width);
	}
	
	@Override
	public float getLeft() {
		return getCentre().x - width/2;
	}
	
	@Override
	public float getBottom() {
		return getCentre().y - width*1.5f;
	}

	public void dragTo(Vector2 target) {
		setCentre(target);
	}
}
