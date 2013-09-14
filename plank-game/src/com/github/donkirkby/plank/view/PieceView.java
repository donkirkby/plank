package com.github.donkirkby.plank.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class PieceView extends GameComponentView {
	private Piece piece;
	private Plank plank;
	private float radius;
	private float radius2; // radius squared
	private Vector2 target;
	public PieceView(Piece piece, Actor actor) {
	    super(actor);
		this.piece = piece;
		this.radius = actor.getWidth()/2;
		this.radius2 = radius * radius;
	}

	@Override
	public void panBy(float deltaX, float deltaY) {
	    if (target == null) {
            target = getCentre();
        }
	    target.add(deltaX, deltaY);
		for (PlankView destination : getDestinations()) {
			Vector2 destinationPosition = destination.getPosition(piece);
			if (target.dst2(destinationPosition) < radius2) {
			    if (plank != null) {
                    plank.remove(piece);
                }
				setCentre(destinationPosition);
				plank = destination.getPlank();
				plank.add(piece);
				return;
			}
		}
		setCentre(target);
	}

	public Piece getPiece() {
		return piece;
	}

	@Override
	public String toString() {
	    Vector2 centre = getCentre();
        return piece.getColour().name().toLowerCase() +
                " piece at " +  
                Integer.toString((int) centre.x) +
                "," +
                Integer.toString((int) centre.y);
	}
}
