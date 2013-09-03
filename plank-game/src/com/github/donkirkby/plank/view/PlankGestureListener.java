package com.github.donkirkby.plank.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.github.donkirkby.plank.model.GameState;
import com.github.donkirkby.plank.model.Piece;

public class PlankGestureListener extends ActorGestureListener {
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
	public void tap(InputEvent event, float x, float y, int count, int button) {
	    Vector2 touchPos = calculateTouchPos(x, y);
        GameComponentView tappedView = 
	            GameComponentView.findClosest(touchPos, draggableViews);
        tappedView.tap();
	}
	
	@Override
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		if (draggingView == null) {
			return;
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
	}

	private Vector2 calculateTouchPos(float x, float y) {
        Vector3 touchPos3 = new Vector3();
        touchPos3.set(x, y, 0);
        camera.unproject(touchPos3);
		Vector2 touchPos = new Vector2(touchPos3.x, camera.viewportHeight - touchPos3.y);
		return touchPos;
	}

	@Override
	public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
		draggingView = GameComponentView.findClosest(
		        calculateTouchPos(x, y), 
		        draggableViews);
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
    
    public GameState getGameState() {
        return state;
    }

    public PieceView[] getWinners() {
        return winners;
    }

    public void reset() {
        for (GameComponentView view : allViews) {
            view.reset();
        }
        for (int i = 0; i < winners.length; i++) {
            winners[i] = null;
        }
        draggableViews.addAll(placedPlankViews);
        unplacedPlankViews.addAll(placedPlankViews);
        placedPlankViews.clear();
        state.reset();
    }
}
