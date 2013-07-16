package com.github.donkirkby.plank.view;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class PlankView extends GameComponentView {
	private Plank plank;
	private float width;
	private float lineHeight;
	
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

	public float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public boolean dragTo(Vector2 target) {
		List<PlankView> destinations = getDestinations();
		if (destinations != null && Math.abs(target.y - lineHeight) < width/2) {
			if (destinations.size() == 0) {
				setCentre(target.x, lineHeight);
				destinations.add(this);
				return true;
			}
			float leftEdge = destinations.get(0).getCentre().x - width;
			if (Math.abs(leftEdge - target.x) < width/2) {
				setCentre(leftEdge, lineHeight);
				destinations.add(0, this);
				return true;
			}
			PlankView lastPlankView = destinations.get(destinations.size()-1);
			float rightEdge = lastPlankView.getCentre().x + width;
			if (Math.abs(rightEdge - target.x) < width/2) {
				setCentre(rightEdge, lineHeight);
				destinations.add(this);
				return true;
			}
		}
		setCentre(target);
		return false;
	}
}
