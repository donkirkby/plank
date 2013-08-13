package com.github.donkirkby.plank.model;

public class Piece {
    public static final Piece NULL_PIECE = new Piece(-1, PieceColour.NONE);
    
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

    @Override
    public String toString() {
        if (player < 0) {
            return "Null piece";
        }
        return "Player " + player + " " + colour.name().toLowerCase() + " piece";
    }

}
