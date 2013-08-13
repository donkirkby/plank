package com.github.donkirkby.plank.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTest {

    @Test
    public void string() {
        // SETUP
        Piece piece = new Piece(2, PieceColour.BLUE);
        
        // EXEC
        String string = piece.toString();
        
        // VERIFY
        assertThat("string", string, is("Player 2 blue piece"));
    }

    @Test
    public void nullPiece() {
        // EXEC
        String string = Piece.NULL_PIECE.toString();
        
        // VERIFY
        assertThat("string", string, is("Null piece"));
    }
}
