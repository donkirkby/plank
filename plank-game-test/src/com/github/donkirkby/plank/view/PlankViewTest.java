package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PlankViewTest {

	@Test
	public void dragTo() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float width = 50;
		Vector2 target = new Vector2(210, 120);

		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView plankView = 
				new PlankView(plank, oldCentre, width);
		
		// EXEC
		boolean isSnapped = plankView.dragTo(target);
		Vector2 newCentre = plankView.getCentre();
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(false));
		assertThat("centre", newCentre, is(target));
	}

	@Test
	public void dragToEndOfLine() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float width = 50;
		float lineHeight = 300;
		Vector2 firstPlankCentre = new Vector2(100, lineHeight);
		Vector2 target = new Vector2(270, 320);
		Vector2 expectedCentre = new Vector2(250, lineHeight);

		List<PlankView> destinations = 
				createDestinations(firstPlankCentre, width);
		Plank draggingPlank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView draggingPlankView = 
				new PlankView(draggingPlank, oldCentre, width);
		draggingPlankView.setLineHeight(lineHeight);
		draggingPlankView.setDestinations(destinations);
		
		// EXEC
		boolean isSnapped = draggingPlankView.dragTo(target);
		Vector2 newCentre = draggingPlankView.getCentre();
		PlankView lastDestination = destinations.get(3);
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(true));
		assertThat("centre", newCentre, is(expectedCentre));
		assertThat("last destination", lastDestination, is(draggingPlankView));
	}

	private List<PlankView> createDestinations(Vector2 firstPlankCentre,
			float width) {
		List<PlankView> destinations = new ArrayList<PlankView>();
		for (int i = 0; i < 3; i++) {
			Plank placedPlank =
					new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
			PlankView placedPlankView = new PlankView(
					placedPlank, 
					firstPlankCentre.cpy().add(i*width, 0), 
					width);
			destinations.add(placedPlankView);
		}
		return destinations;
	}

	@Test
	public void dragToStartOfLine() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float width = 50;
		float lineHeight = 300;
		Vector2 firstPlankCentre = new Vector2(100, lineHeight);
		Vector2 target = new Vector2(45, 320);
		Vector2 expectedCentre = new Vector2(50, lineHeight);

		List<PlankView> destinations = createDestinations(firstPlankCentre, width);
		Plank draggingPlank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView draggingPlankView = 
				new PlankView(draggingPlank, oldCentre, width);
		draggingPlankView.setLineHeight(lineHeight);
		draggingPlankView.setDestinations(destinations);
		
		// EXEC
		boolean isSnapped = draggingPlankView.dragTo(target);
		Vector2 newCentre = draggingPlankView.getCentre();
		PlankView firstDestination = destinations.get(0);
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(true));
		assertThat("centre", newCentre, is(expectedCentre));
		assertThat("first destination", firstDestination, is(draggingPlankView));
	}

	@Test
	public void dragToEmptyLine() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float width = 50;
		float lineHeight = 300;
		Vector2 target = new Vector2(45, 320);
		Vector2 expectedCentre = new Vector2(45, lineHeight);

		List<PlankView> destinations = new ArrayList<PlankView>();
		Plank draggingPlank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView draggingPlankView = 
				new PlankView(draggingPlank, oldCentre, width);
		draggingPlankView.setLineHeight(lineHeight);
		draggingPlankView.setDestinations(destinations);
		
		// EXEC
		boolean isSnapped = draggingPlankView.dragTo(target);
		Vector2 newCentre = draggingPlankView.getCentre();
		PlankView destination = destinations.get(0);
		
		// VERIFY
		assertThat("is snapped", isSnapped, is(true));
		assertThat("centre", newCentre, is(expectedCentre));
		assertThat("destination", destination, is(draggingPlankView));
	}
	
	@Test
	public void tap() {
		// SETUP
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		DummyImage image = new DummyImage();
		PlankView view = new PlankView(plank, new Vector2(), 50);
        view.setImage(image);
		
		// EXEC
		boolean defaultPlankFlip = plank.isFlipped();
		view.tap();
		boolean isPlankFlipped = plank.isFlipped();
		boolean isImageFlipped = image.isFlipped();
		
		// VERIFY
		assertThat("plank flipped by default", defaultPlankFlip, is(false));
        assertThat("plank flipped after", isPlankFlipped, is(true));
        assertThat("image flipped after", isImageFlipped, is(true));
	}

    @Test
    public void reset() {
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
        pieceView.dragTo(target);
        plankView.reset();
        Piece addedPiece = plank.get(1);
        
        // VERIFY
        assertThat("added piece", addedPiece, is(Piece.NULL_PIECE));
    }

}
