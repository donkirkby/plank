package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PieceViewTest {

	@Test
	public void dragTo() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float radius = 10;
		Vector2 plankCentre = new Vector2(300, 100);
		float width = 25;
		Vector2 target = new Vector2(210, 120);

		Piece piece = new Piece(0, PieceColour.BLUE);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PieceView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.addPossibleDestination(plankView);
		pieceView.dragTo(target);
		Vector2 newCentre = pieceView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(target));
	}

	@Test
	public void snapTo() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float radius = 10;
		Vector2 plankCentre = new Vector2(300, 100);
		float width = 25;
		Vector2 target = plankCentre.cpy().add(radius / 2, 0);

		Piece piece = new Piece(0, PieceColour.BLUE);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PieceView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.addPossibleDestination(plankView);
		pieceView.dragTo(target);
		Vector2 newCentre = pieceView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(plankCentre));
	}

	@Test
	public void snapToNonCentre() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float radius = 10;
		Vector2 plankCentre = new Vector2(300, 100);
		float width = 25;
		Vector2 expectedCentre = plankCentre.cpy().add(0, width);
		Vector2 target = expectedCentre.cpy().add(0, -radius/2);

		Piece piece = new Piece(0, PieceColour.RED);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PieceView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.addPossibleDestination(plankView);
		pieceView.dragTo(target);
		Vector2 newCentre = pieceView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(expectedCentre));
	}

}
