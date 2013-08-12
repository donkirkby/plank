package com.github.donkirkby.plank.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Plank> placedPlanks = new ArrayList<Plank>();

    public List<Plank> getPlacedPlanks() {
        return placedPlanks;
    }

    /**
     * Check to see if there is a winner.
     * @return the player number of the winner, or a negative number if there
     * is no winner.
     */
    public int getWinner() {
        for (int plankIndex = 0; plankIndex < placedPlanks.size(); plankIndex++) {
            int winner = getPlankWinner(placedPlanks.get(plankIndex));
            if (winner >= 0) {
                return winner;
            }
            if (plankIndex < placedPlanks.size() - 2) {
                for (int spaceIndex = 0; spaceIndex < 3; spaceIndex++) {
                    winner = getNeighboringPlanksWinner(plankIndex, spaceIndex);
                    if (winner >= 0) {
                        return winner;
                    }
                }
            }
        }

        return -1;
    }

    private int getNeighboringPlanksWinner(int plankIndex, int spaceIndex) {
        int player = -1;
        for (int plankOffset = 0; plankOffset < 3; plankOffset++) {
            Plank plank = placedPlanks.get(plankIndex + plankOffset);
            if (plankOffset == 0) {
                player = plank.get(spaceIndex).getPlayer();
            }
            else if (plank.get(spaceIndex).getPlayer() != player) {
                return -1;
            }
            else {
                PieceColour colour = plank.get(spaceIndex).getColour();
                for (
                        int plankOffset2 = 0; 
                        plankOffset2 < plankOffset; 
                        plankOffset2++) {
                    Plank plank2 = placedPlanks.get(plankIndex + plankOffset2);
                    PieceColour colour2 = plank2.get(spaceIndex).getColour();
                    if (colour2 == colour) {
                        // duplicate colours don't win.
                        return -1;
                    }
                }
            }
        }
        return player;
    }

    private int getPlankWinner(Plank plank) {
        int player = -1;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                player = plank.get(i).getPlayer();
            }
            else if (plank.get(i).getPlayer() != player) {
                return -1;
            }
        }
        return player;
    }
}
