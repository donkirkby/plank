package com.github.donkirkby.plank.view;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public abstract class GameComponentView {

    private Actor actor;
	private Vector2 startPosition;
	private List<PlankView> destinations;
	
	protected GameComponentView(Actor actor) {
	    this.actor = actor;
        int halfTapSquareSize = 5;
        float tapCountInterval = 0.4f;
        float longPressDuration = 1.1f;
        float maxFlingDelay = 0.15f;
        actor.addListener(new ActorGestureListener(
                halfTapSquareSize, 
                tapCountInterval, 
                longPressDuration, 
                maxFlingDelay) {
            @Override
            public void pan(
                    InputEvent event, 
                    float x, 
                    float y, 
                    float deltaX,
                    float deltaY) {
                panBy(deltaX, deltaY);
            }
            
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                    int button) {
                GameComponentView.this.tap();
            }
        });
	}
	
    public void setCentre(float x, float y) {
        actor.setPosition(
                x - actor.getWidth()/2, 
                y - actor.getHeight()/2);
        if (startPosition == null) {
            startPosition = new Vector2(actor.getX(), actor.getY());
        }
    }
    
    public void setCentre(Vector2 centre) {
        setCentre(centre.x, centre.y);
    }
    
    public Vector2 getCentre() {
        return new Vector2(
                actor.getX() + actor.getWidth()/2, 
                actor.getY() + actor.getHeight()/2);
    }
    
    public void panBy(float deltaX, float deltaY) {
        actor.setPosition(actor.getX() + deltaX, actor.getY() + deltaY);
    }
    
    public Actor getActor() {
        return actor;
    }

    /**
	 * Set the list of plank views that this piece can be placed on. More
	 * plank views can be added to the collection after this call.
	 * @param destinations list of plank views that this piece can be placed on
	 */
	public void setDestinations(List<PlankView> destinations) {
		this.destinations = destinations;
	}

	public List<PlankView> getDestinations() {
		return destinations;
	}

    public void tap() {
        // By default, do nothing.
    }

    public void reset() {
        if (startPosition != null) {
            actor.addAction(Actions.moveBy(
                    startPosition.x - actor.getX(), 
                    startPosition.y - actor.getY(),
                    0.5f));
        }
    }

}