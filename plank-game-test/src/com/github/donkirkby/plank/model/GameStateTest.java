package com.github.donkirkby.plank.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class GameStateTest {
    private int player1 = 0;
    private int player2 = 1;
    private Piece player1Red = new Piece(player1, PieceColour.RED);
    private Piece player1Green = new Piece(player1, PieceColour.GREEN);
    private Piece player1Green2 = new Piece(player1, PieceColour.GREEN);
    private Piece player1Blue = new Piece(player1, PieceColour.BLUE);
    private Piece player2Red = new Piece(player2, PieceColour.RED);
    private Piece player2Green = new Piece(player2, PieceColour.GREEN);
    private Piece player2Blue = new Piece(player2, PieceColour.BLUE);
    private Plank plank1 = 
            new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
    private Plank plank2 =
            new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
    private Plank plank3 =
            new Plank(PieceColour.BLUE, PieceColour.RED, PieceColour.GREEN);
    @Test
    public void singlePiece() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = new Piece[] { 
                Piece.NULL_PIECE, 
                Piece.NULL_PIECE, 
                Piece.NULL_PIECE 
        };
        
        plank1.add(player1Green);
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void fullPlankPlayer1() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = 
                new Piece[] { player1Red, player1Green, player1Blue };
        
        plank1.add(player1Red);
        plank1.add(player1Green);
        plank1.add(player1Blue);
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void fullPlankPlayer2() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = 
                new Piece[] { player2Red, player2Green, player2Blue };
        
        plank1.add(player2Red);
        plank1.add(player2Green);
        plank1.add(player2Blue);
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void fullPlankMixed() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = new Piece[] { 
                Piece.NULL_PIECE, 
                Piece.NULL_PIECE, 
                Piece.NULL_PIECE 
        };
        
        plank1.add(player2Red);
        plank1.add(player1Green);
        plank1.add(player2Blue);
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void fullPlank2() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        game.getPlacedPlanks().add(plank2);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = 
                new Piece[] { player1Red, player1Blue, player1Green };
        
        plank2.add(player1Red);
        plank2.add(player1Blue);
        plank2.add(player1Green);
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void neighbouringPlanks() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        game.getPlacedPlanks().add(plank2);
        game.getPlacedPlanks().add(plank3);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = 
                new Piece[] { player1Green, player1Blue, player1Red };
        
        plank1.add(player1Green); // middle
        plank2.add(player1Blue); // middle
        plank3.add(player1Red); // middle
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void neighbouringPlanksMixed() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        game.getPlacedPlanks().add(plank2);
        game.getPlacedPlanks().add(plank3);
        Piece[] pieces = new Piece[3];
        pieces[1] = player1Green; // Make sure it gets removed.
        Piece[] expectedPieces = new Piece[] { 
                Piece.NULL_PIECE,
                Piece.NULL_PIECE,
                Piece.NULL_PIECE
        };
        
        plank1.add(player1Green); // middle
        plank2.add(player2Blue); // middle
        plank3.add(player1Red); // middle
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

    @Test
    public void neighbouringPlanksWrongColours() {
        // SETUP
        GameState game = new GameState();
        game.getPlacedPlanks().add(plank1);
        game.getPlacedPlanks().add(plank2);
        game.getPlacedPlanks().add(plank3);
        Piece[] pieces = new Piece[3];
        Piece[] expectedPieces = new Piece[] { 
                Piece.NULL_PIECE,
                Piece.NULL_PIECE,
                Piece.NULL_PIECE
        };
        
        plank1.add(player1Blue); // bottom
        plank2.add(player1Green); // bottom
        plank3.add(player1Green2); // bottom
        
        // EXEC
        game.findWin(pieces);
        
        // VERIFY
        assertThat("pieces", pieces, is(expectedPieces));
    }

}
