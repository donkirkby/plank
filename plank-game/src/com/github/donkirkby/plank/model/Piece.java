package com.github.donkirkby.plank.model;

public class Piece {
	private int player;
	private PieceColour colour;

	public Piece(int player, PieceColour colour) {
		this.player = player;
		this.colour = colour;
	}
	
	public int getPlayer() {
		return player;
	}

	public PieceColour getColour() {
		return colour;
	}

}
