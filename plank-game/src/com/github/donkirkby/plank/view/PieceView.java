package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;

public class PieceView {
	private Piece piece;
	private Vector2 centre;
	private float radius;
	private float radius2; // radius squared
	private PlankView destination;
	
	public PieceView(Piece piece, Vector2 centre, float radius) {
		this.piece = piece;
		this.centre = centre;
		this.radius = radius;
		this.radius2 = radius * radius;
	}

	public void addPossibleDestination(PlankView plankView) {
		destination = plankView;
	}

	public void dragTo(Vector2 target) {
		Vector2 destinationPosition = 
				destination.getPosition(piece.getColour());
		if (target.dst2(destinationPosition) < radius2) {
			centre.set(destinationPosition);
		}
		else {
			centre.set(target);
		}
	}

	/**
	 * Get the position of this piece's centre. Do not modify!
	 * @return
	 */
	public Vector2 getCentre() {
		return centre;
	}
	
	public float getLeft() {
		return centre.x - radius;
	}
	
	public float getBottom() {
		return centre.y - radius;
	}

}
