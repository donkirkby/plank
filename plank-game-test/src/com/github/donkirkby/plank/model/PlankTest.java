package com.github.donkirkby.plank.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PlankTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void addPiece() {
		// SETUP
		Piece piece = new Piece(0, PieceColour.RED);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		plank.add(piece);
		Piece pieceFromPlank = plank.get(0);
		
		// VERIFY
		assertThat("piece from plank", pieceFromPlank, sameInstance(piece));
	}

	@Test
	public void getEmptySpace() {
		// SETUP
		Piece piece = new Piece(0, PieceColour.RED);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		plank.add(piece);
		Piece pieceFromEmptySpace = plank.get(1);
		
		// VERIFY
		assertThat("piece from empty space", pieceFromEmptySpace, nullValue());
	}

	@Test
	public void addMiddlePiece() {
		// SETUP
		Piece piece = new Piece(0, PieceColour.GREEN);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		plank.add(piece);
		Piece pieceFromPlank = plank.get(1);
		
		// VERIFY
		assertThat("piece from plank", pieceFromPlank, sameInstance(piece));
	}

	@Test
	public void isMatch() {
		// SETUP
		Piece piece = new Piece(0, PieceColour.GREEN);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		boolean isSameColourMatch = plank.isMatch(piece, 1);
		boolean isDifferentColourMatch = plank.isMatch(piece, 2);
		
		// VERIFY
		assertThat("same colour match", isSameColourMatch, is(true));
		assertThat("different colour match", isDifferentColourMatch, is(false));
	}

	@Test
	public void indexOf() {
		// SETUP
		Piece redPiece = new Piece(0, PieceColour.RED);
		Piece greenPiece = new Piece(0, PieceColour.GREEN);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		int redIndex = plank.indexOf(redPiece);
		int greenIndex = plank.indexOf(greenPiece);
		
		// VERIFY
		assertThat("index", redIndex, is(0));
		assertThat("index", greenIndex, is(1));
	}

	@Test
	public void Flip() {
		// SETUP
		Piece redPiece = new Piece(0, PieceColour.RED);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		
		// EXEC
		plank.flip();
		int redIndex = plank.indexOf(redPiece);
		
		// VERIFY
		assertThat("index", redIndex, is(2));
	}

}
