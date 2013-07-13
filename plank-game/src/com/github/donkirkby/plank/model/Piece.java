package com.github.donkirkby.plank.model;

public class Piece {
	private PieceColour colour;

	public Piece(PieceColour colour) {
		this.colour = colour;
	}

	public PieceColour getColour() {
		return colour;
	}

	public void setColour(PieceColour colour) {
		this.colour = colour;
	}

}
