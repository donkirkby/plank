package com.github.donkirkby.plank.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.donkirkby.plank.model.GameState;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.Plank;

public class ViewState {
    private List<GameComponentView> allViews = 
            new ArrayList<GameComponentView>();
    private List<PieceView> pieceViews = 
            new ArrayList<PieceView>();
    private List<PlankView> placedPlankViews = 
            new ArrayList<PlankView>();
	private GameState gameState = new GameState();
	private PieceView[] winners = new PieceView[3];
	private Piece[] winningPieces = new Piece[3];
	private ArrayList<Actor> highlights = new ArrayList<Actor>();

	public void addView(PieceView pieceView) {
	    pieceView.setDestinations(placedPlankViews);
	    allViews.add(pieceView);
	    pieceViews.add(pieceView);
	}

	public void addView(PlankView plankView) {
	    plankView.setDestinations(placedPlankViews);
		allViews.add(0, plankView);
	}

    public List<PlankView> getPlacedPlankViews() {
        return placedPlankViews;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void displayWinners() {
        List<Plank> placedPlanks = gameState.getPlacedPlanks();
        if (placedPlankViews.size() != placedPlanks.size()) {
            placedPlanks.clear();
            for (PlankView view : placedPlankViews) {
                placedPlanks.add(view.getPlank());
            }
        }
        gameState.findWin(winningPieces);
        for (int i = 0; i < winningPieces.length; i++) {
            boolean isWinner = winningPieces[i] != Piece.NULL_PIECE;
            Actor highlight = highlights.get(i);
            highlight.setVisible(isWinner);
            if (isWinner) {
                for (PieceView pieceView : pieceViews) {
                    if (pieceView.getPiece() == winningPieces[i]) {
                        highlight.setPosition(
                                pieceView.getActor().getX() + 
                                (pieceView.getActor().getWidth() - 
                                        highlight.getWidth())/2, 
                                pieceView.getActor().getY() +
                                (pieceView.getActor().getHeight() -
                                        highlight.getHeight())/2);
                        winners[i] = pieceView;
                        break;
                    }
                }
            }
        }
    }

    public void reset() {
        for (GameComponentView view : allViews) {
            view.reset();
        }
        for (int i = 0; i < winners.length; i++) {
            winners[i] = null;
        }
        placedPlankViews.clear();
    }

    public void addHighlight(Actor highlight) {
        highlights.add(highlight);
    }
}
