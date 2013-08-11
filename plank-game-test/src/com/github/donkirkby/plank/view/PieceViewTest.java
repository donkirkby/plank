package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PieceViewTest {

	@Test
	public void dragToNothing() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float radius = 10;
		Vector2 plankCentre = new Vector2(300, 100);
		float width = 25;
		Vector2 target = new Vector2(210, 120);

		Piece piece = new Piece(0, PieceColour.BLUE);
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		GameComponentView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.setDestinations(Arrays.asList(plankView));
		Piece originalPiece = plank.get(1);

		boolean isSnapped = pieceView.dragTo(target);
		
		Vector2 newCentre = pieceView.getCentre();
		Piece addedPiece = plank.get(1);
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(false));
		assertThat("centre", newCentre, is(target));
		assertThat("original piece", originalPiece, nullValue());
		assertThat("added piece", addedPiece, nullValue());
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
		GameComponentView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.setDestinations(Arrays.asList(plankView));
		boolean isSnapped = pieceView.dragTo(target);
		Vector2 newCentre = pieceView.getCentre();
        Piece addedPiece = plank.get(1);
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(true));
		assertThat("centre", newCentre, is(plankCentre));
        assertThat("added piece", addedPiece, is(piece));
	}

	@Test
	public void snapToMultiplePlanks() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float radius = 10;
		Vector2 plankCentre1 = new Vector2(300, 100);
		Vector2 plankCentre2 = new Vector2(400, 100);
		float width = 25;
		Vector2 expectedCentre2 = plankCentre2.cpy().add(0, width);
		Vector2 target1 = plankCentre1.cpy().add(radius / 2, 0);
		Vector2 target2 = expectedCentre2.cpy().add(radius / 2, 0);

		Piece piece = new Piece(0, PieceColour.BLUE);
		Plank plank1 = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		Plank plank2 = 
				new Plank(PieceColour.BLUE, PieceColour.RED, PieceColour.GREEN);
		GameComponentView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView1 = 
				new PlankView(plank1, plankCentre1, width);
		PlankView plankView2 = 
				new PlankView(plank2, plankCentre2, width);
		
		// EXEC
		pieceView.setDestinations(Arrays.asList(plankView1, plankView2));
		pieceView.dragTo(target1);
		Vector2 newCentre1 = pieceView.getCentre().cpy();
		pieceView.dragTo(target2);
		Vector2 newCentre2 = pieceView.getCentre();
        Piece removedPiece = plank1.get(1);
        Piece addedPiece = plank2.get(0);
		
		// VERIFY
		assertThat("centre 1", newCentre1, is(plankCentre1));
		assertThat("centre 2", newCentre2, is(expectedCentre2));
        assertThat("removed piece", removedPiece, nullValue());
		assertThat("added piece", addedPiece, is(piece));
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
		GameComponentView pieceView = 
				new PieceView(piece, oldCentre, radius);
		PlankView plankView = 
				new PlankView(plank, plankCentre, width);
		
		// EXEC
		pieceView.setDestinations(Arrays.asList(plankView));
		pieceView.dragTo(target);
		Vector2 newCentre = pieceView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(expectedCentre));
	}

}
