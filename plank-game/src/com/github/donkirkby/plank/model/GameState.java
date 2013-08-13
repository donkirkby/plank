package com.github.donkirkby.plank.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Plank> placedPlanks = new ArrayList<Plank>();

    public List<Plank> getPlacedPlanks() {
        return placedPlanks;
    }

    /**
     * Check to see if there is a winning line, and return the three pieces
     * in that winning line if found. 
     * @param winner an array of 3 Piece objects that will hold the three pieces
     * in the winning line, or 3 Piece.NULL_PIECE references if there is no
     * winner.
     */
    public void findWin(Piece[] pieces) {
        for (int plankIndex = 0; plankIndex < placedPlanks.size(); plankIndex++) {
            findPlankWin(placedPlanks.get(plankIndex), pieces);
            if (pieces[0] != Piece.NULL_PIECE) {
                return;
            }
            if (plankIndex < placedPlanks.size() - 2) {
                for (int spaceIndex = 0; spaceIndex < 3; spaceIndex++) {
                    findNeighboringPlanksWin(plankIndex, spaceIndex, pieces);
                    if (pieces[0] != Piece.NULL_PIECE) {
                        return;
                    }
                }
            }
        }
    }

    private void findNeighboringPlanksWin(
            int plankIndex, 
            int spaceIndex, 
            Piece[] pieces) {
        for (int plankOffset = 0; plankOffset < 3; plankOffset++) {
            Plank plank = placedPlanks.get(plankIndex + plankOffset);
            Piece piece = plank.get(spaceIndex);
            if (plankOffset == 0) {
                pieces[0] = piece;
            }
            else if (piece.getPlayer() != pieces[0].getPlayer()) {
                clearPieces(pieces);
                return;
            }
            else {
                PieceColour colour = piece.getColour();
                for (
                        int plankOffset2 = 0; 
                        plankOffset2 < plankOffset; 
                        plankOffset2++) {
                    Plank plank2 = placedPlanks.get(plankIndex + plankOffset2);
                    PieceColour colour2 = plank2.get(spaceIndex).getColour();
                    if (colour2 == colour) {
                        // duplicate colours don't win.
                        clearPieces(pieces);
                        return;
                    }
                }
                pieces[plankOffset] = piece;
            }
        }
    }

    private void findPlankWin(Plank plank, Piece[] pieces) {
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                pieces[0] = plank.get(i);
            }
            else if (plank.get(i).getPlayer() != pieces[0].getPlayer()) {
                clearPieces(pieces);
                return;
            }
            else {
                pieces[i] = plank.get(i);
            }
        }
    }
    
    private void clearPieces(Piece[] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            pieces[i] = Piece.NULL_PIECE;
        }
    }
}
