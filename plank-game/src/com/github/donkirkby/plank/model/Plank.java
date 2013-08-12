package com.github.donkirkby.plank.model;

public class Plank {
    public static final Piece NULL_PIECE = new Piece(-1, PieceColour.NONE);
    
	private Piece[] pieces;
	private PieceColour[] spaces = new PieceColour[3];
	private boolean isFlipped;

	public Plank(PieceColour space1, PieceColour space2, PieceColour space3) {
		spaces = new PieceColour[] {space1, space2, space3};
		pieces = new Piece[] {NULL_PIECE, NULL_PIECE, NULL_PIECE};
	}

    public void add(Piece piece) {
        for (int i = 0; i < pieces.length; i++) {
            PieceColour space = spaces[i];
            if (piece.getColour() == space) {
                pieces[i] = piece;
                return;
            }
        }
    }

    public void remove(Piece piece) {
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == piece) {
                pieces[i] = NULL_PIECE;
            }
        }
    }

	public Piece get(int i) {
		return pieces[i];
	}

	public boolean isMatch(Piece piece, int i) {
		return piece.getColour() == spaces[i];
	}

	public int indexOf(Piece piece) {
		for (int i = 0; i < spaces.length; i++) {
			if (spaces[i] == piece.getColour()) {
				return i;
			}
		}
		throw new IllegalArgumentException(
				"Colour not found: " + piece.getColour());
	}

	public boolean isFlipped() {
		return isFlipped;
	}

	public void flip() {
		isFlipped = ! isFlipped;
		PieceColour swap = spaces[0];
		spaces[0] = spaces[2];
		spaces[2] = swap;
	}

}
