package com.github.donkirkby.plank.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlankGestureListener implements GestureListener {
	private List<GameComponentView> allViews = 
			new ArrayList<GameComponentView>();
    private List<PlankView> placedPlankViews = 
            new ArrayList<PlankView>();
    private List<PlankView> unplacedPlankViews = 
            new ArrayList<PlankView>();
    private List<GameComponentView> draggableViews = 
            new ArrayList<GameComponentView>();
	private GameComponentView draggingView;
	private Camera camera;
	
	public PlankGestureListener(Camera camera) {
		this.camera = camera;
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
	    Vector2 touchPos = calculateTouchPos(x, y);
        GameComponentView tappedView = 
	            GameComponentView.findClosest(touchPos, draggableViews);
        tappedView.tap();
		return false;
	}
	
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (draggingView == null) {
			return false;
		}
		Vector2 touchPos = calculateTouchPos(x, y);
		boolean isSnapped = draggingView.dragTo(touchPos);
		if (isSnapped && unplacedPlankViews.contains(draggingView)) {
		    unplacedPlankViews.remove(draggingView);
		    draggableViews.remove(draggingView);
			draggingView = null;
		}
		return true;
	}

	private Vector2 calculateTouchPos(float x, float y) {
		Vector3 touchPos3 = new Vector3();
		touchPos3.set(x, y, 0);
		camera.unproject(touchPos3);
		Vector2 touchPos = new Vector2(touchPos3.x, touchPos3.y);
		return touchPos;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		draggingView = GameComponentView.findClosest(
		        calculateTouchPos(x, y), 
		        draggableViews);
		return true;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	public void addView(PieceView pieceView) {
	    pieceView.setDestinations(placedPlankViews);
	    allViews.add(pieceView);
	    draggableViews.add(pieceView);
	}

	public void addView(PlankView plankView) {
	    plankView.setDestinations(placedPlankViews);
		unplacedPlankViews.add(plankView);
		allViews.add(0, plankView);
		draggableViews.add(plankView);
	}

    public List<PlankView> getPlacedPlankViews() {
        return placedPlankViews;
    }

    public List<PlankView> getUnplacedPlankViews() {
        return unplacedPlankViews;
    }

    public List<GameComponentView> getDraggableViews() {
        return draggableViews;
    }

    public List<GameComponentView> getAllViews() {
        return allViews;
    }
}
