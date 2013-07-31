package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;

public class PieceView extends GameComponentView {
	private Piece piece;
	private float radius;
	private float radius2; // radius squared
	public PieceView(Piece piece, Vector2 centre, float radius) {
		super(centre);
		this.piece = piece;
		this.radius = radius;
		this.radius2 = radius * radius;
	}

	@Override
	public boolean dragTo(Vector2 target) {
		for (PlankView destination : getDestinations()) {
			Vector2 destinationPosition = destination.getPosition(piece);
			if (target.dst2(destinationPosition) < radius2) {
				setCentre(destinationPosition);
				return true;
			}
		}
		setCentre(target);
		return false;
	}

	public float getLeft() {
		return getCentre().x - radius;
	}
	
	public float getBottom() {
		return getCentre().y - radius;
	}

	public Piece getPiece() {
		return piece;
	}

}