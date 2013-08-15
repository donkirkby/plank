package com.github.donkirkby.plank.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.donkirkby.plank.model.GameState;
import com.github.donkirkby.plank.model.Piece;

public class PlankGestureListener implements GestureListener {
    private List<GameComponentView> allViews = 
            new ArrayList<GameComponentView>();
    private List<PieceView> pieceViews = 
            new ArrayList<PieceView>();
    private List<PlankView> placedPlankViews = 
            new ArrayList<PlankView>();
    private List<PlankView> unplacedPlankViews = 
            new ArrayList<PlankView>();
    private List<GameComponentView> draggableViews = 
            new ArrayList<GameComponentView>();
	private GameComponentView draggingView;
	private Camera camera;
	private GameState state = new GameState();
	private PieceView[] winners = new PieceView[3];
	private Piece[] winningPieces = new Piece[3];
	
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
		if (isSnapped) {
		    if (unplacedPlankViews.contains(draggingView)) {
		        PlankView placedView = placedPlankViews.get(0);
		        if (placedView == draggingView) {
		            state.getPlacedPlanks().add(0, placedView.getPlank());
		        }
		        else {
                    placedView = placedPlankViews.get(placedPlankViews.size()-1);
                    state.getPlacedPlanks().add(placedView.getPlank());
                }
    		    unplacedPlankViews.remove(draggingView);
    		    draggableViews.remove(draggingView);
    			draggingView = null;
		    }
		    else {
		        state.findWin(winningPieces);
		        for (int i = 0; i < winningPieces.length; i++) {
		            if (winningPieces[i] == Piece.NULL_PIECE) {
		                winners[i] = null;
		            }
		            else {
    		            for (PieceView pieceView : pieceViews) {
    		                if (pieceView.getPiece() == winningPieces[i]) {
    		                    winners[i] = pieceView;
    		                    break;
    		                }
    		            }
    		        }
                }
		    }
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
	    pieceViews.add(pieceView);
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

    public PieceView[] getWinners() {
        return winners;
    }
}
