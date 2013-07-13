package com.github.donkirkby.plank.model;

public class Plank {
	private Piece[] pieces = new Piece[3];
	private PieceColour[] spaces = new PieceColour[3];

	public Plank(PieceColour space1, PieceColour space2, PieceColour space3) {
		spaces = new PieceColour[] {space1, space2, space3};
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

	public Piece get(int i) {
		return pieces[i];
	}

	public boolean isMatch(Piece piece, int i) {
		return piece.getColour() == spaces[i];
	}

}
