package com.github.donkirkby.plank.view;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class PlankView extends GameComponentView {
	private Plank plank;
	private float lineHeight;

	public PlankView(final Plank plank, final Actor actor) {
	    super(actor);
		this.plank = plank;
	}

	public Vector2 getPosition(Piece piece) {
		int index = plank.indexOf(piece);
		return getCentre().add(0, (1-index)*getActor().getHeight()/3);
	}
	
	public Plank getPlank() {
	    return plank;
	}
	
	public float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}

	@Override
	public void panBy(float deltaX, float deltaY) {
	    if (isPlaced()) {
            return;
        }
        List<PlankView> destinations = getDestinations();
	    super.panBy(deltaX, deltaY);
        Vector2 target = getCentre();
		float width = getActor().getWidth();
		if (destinations != null && Math.abs(target.y - lineHeight) < width/2) {
			if (destinations.size() == 0) {
				setCentre(target.x, lineHeight);
				destinations.add(this);
				return;
			}
			float leftEdge = destinations.get(0).getCentre().x - width;
			if (Math.abs(leftEdge - target.x) < width/2) {
				setCentre(leftEdge, lineHeight);
				destinations.add(0, this);
				return;
			}
			PlankView lastPlankView = destinations.get(destinations.size()-1);
			float rightEdge = lastPlankView.getCentre().x + width;
			if (Math.abs(rightEdge - target.x) < width/2) {
				setCentre(rightEdge, lineHeight);
				destinations.add(this);
				return;
			}
		}
    }

    private boolean isPlaced() {
        List<PlankView> destinations = getDestinations();
        return destinations != null && destinations.contains(this);
    }
	
	@Override
	public void tap() {
	    if ( ! isPlaced()) {
    	    plank.flip();
    	    getActor().addAction(Actions.rotateBy(180f, 0.5f));
	    }
	}
	
	@Override
	public void reset() {
	    super.reset();
	    plank.reset();
        getActor().addAction(Actions.rotateTo(0, 0.5f));
	}
}
