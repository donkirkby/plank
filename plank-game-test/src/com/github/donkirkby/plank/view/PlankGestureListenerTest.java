package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PlankGestureListenerTest {

    @Test
    public void pan() {
        // SETUP
        Vector2 oldPieceCentre = new Vector2(100, 50);
        Vector2 oldPlankCentre = new Vector2(200, 50);
        float width = 50;
        float lineHeight = 300;
        float deltaX = 10;
        float deltaY = 5;
        Vector2 touchLeft = new Vector2(80, 50);
        Vector2 touchRight = new Vector2(220,50);
        Vector2 targetLeft = touchLeft.cpy().add(deltaX, deltaY);
        Vector2 targetRight = touchRight.cpy().add(deltaX, deltaY);

        List<PlankView> destinations = new ArrayList<PlankView>();
        Plank plank = 
                new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
        PlankView plankView = 
                new PlankView(plank, oldPlankCentre, width); // right position
        plankView.setLineHeight(lineHeight);
        plankView.setDestinations(destinations);

        Piece piece = new Piece(0, PieceColour.RED);
        PieceView pieceView = 
                new PieceView(piece, oldPieceCentre, 10); // left position
        pieceView.setDestinations(destinations);
        
        PlankGestureListener listener = 
                new PlankGestureListener(new DummyCamera());
        listener.addView(pieceView);
        listener.addView(plankView);
        
        PieceView[] expectedWinners = new PieceView[] { null, null, null };
        
        // EXEC
        boolean isHandledLeftTouch = 
                listener.touchDown(touchLeft.x, touchLeft.y, 0, 0);
        boolean isHandledLeftPan =
                listener.pan(targetLeft.x, targetLeft.y, deltaX, deltaY);
        boolean isHandledRightTouch =
                listener.touchDown(touchRight.x, touchRight.y, 0, 0);
        boolean isHandledRightPan = 
                listener.pan(targetRight.x, targetRight.y, deltaX, deltaY);
        Vector2 newPieceCentre = pieceView.getCentre();
        Vector2 newPlankCentre = plankView.getCentre();
        PieceView[] winners = listener.getWinners();
        
        // VERIFY
        assertThat("is handled left touch", isHandledLeftTouch, is(true));
        assertThat("is handled left pan", isHandledLeftPan, is(true));
        assertThat("is handled right touch", isHandledRightTouch, is(true));
        assertThat("is handled right pan", isHandledRightPan, is(true));
        assertThat("new piece centre", newPieceCentre, is(targetLeft));
        assertThat("new plank centre", newPlankCentre, is(targetRight));
        assertThat("winners", winners, is(expectedWinners));
    }

    @Test
    public void findWin() {
        // SETUP
        Vector2 oldRedCentre = new Vector2(100, 100);
        Vector2 oldBlueCentre = new Vector2(100, 50);
        Vector2 oldGreenCentre = new Vector2(100, 0);
        Vector2 plankTop = new Vector2(200, 100);
        Vector2 plankCentre = new Vector2(200, 50);
        Vector2 plankBottom = new Vector2(200, 0);
        float width = 50;
        float lineHeight = 50;
        float deltaX = 100;
        float deltaY = 0;

        List<PlankView> destinations = new ArrayList<PlankView>();
        Plank plank = 
                new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
        PlankView plankView = 
                new PlankView(plank, plankCentre, width); // right position
        plankView.setLineHeight(lineHeight);
        plankView.setDestinations(destinations);
        destinations.add(plankView);

        Piece redPiece = new Piece(0, PieceColour.RED);
        PieceView redPieceView = 
                new PieceView(redPiece, oldRedCentre, 10); // left position
        redPieceView.setDestinations(destinations);
        Piece bluePiece = new Piece(0, PieceColour.BLUE);
        PieceView bluePieceView = 
                new PieceView(bluePiece, oldBlueCentre, 10); // left position
        bluePieceView.setDestinations(destinations);
        Piece greenPiece = new Piece(0, PieceColour.GREEN);
        PieceView greenPieceView = 
                new PieceView(greenPiece, oldGreenCentre, 10); // left position
        greenPieceView.setDestinations(destinations);
        
        PlankGestureListener listener = 
                new PlankGestureListener(new DummyCamera());
        listener.addView(redPieceView);
        listener.addView(bluePieceView);
        listener.addView(greenPieceView);
        listener.addView(plankView);
        
        PieceView[] oldWinners = listener.getWinners();
          
        // make sure the old winners get removed when there's no winner anymore
        oldWinners[1] = new PieceView(
                new Piece(2, PieceColour.RED), 
                new Vector2(20, 50), 
                20);
        
        PieceView[] expectedWinnersBefore = 
                new PieceView[] { null, null, null };
        PieceView[] expectedWinnersAfter = 
                new PieceView[] { redPieceView, bluePieceView, greenPieceView };
        
        // EXEC
        // place plank (doesn't actually move it)
        listener.touchDown(plankCentre.x, plankCentre.y, 0, 0);
        listener.pan(plankCentre.x, plankCentre.y, 0, 0);
        
        // place three pieces in winning line.
        listener.touchDown(oldRedCentre.x, oldRedCentre.y, 0, 0);
        listener.pan(plankTop.x, plankTop.y, deltaX, deltaY);
        listener.touchDown(oldBlueCentre.x, oldBlueCentre.y, 0, 0);
        listener.pan(plankCentre.x, plankCentre.y, deltaX, deltaY);
        PieceView[] winnersBefore = Arrays.copyOf(listener.getWinners(), 3);
        listener.touchDown(oldGreenCentre.x, oldGreenCentre.y, 0, 0);
        listener.pan(plankBottom.x, plankBottom.y, deltaX, deltaY);
        PieceView[] winnersAfter = listener.getWinners();
        
        // VERIFY
        assertThat("winners before", winnersBefore, is(expectedWinnersBefore));
        assertThat("winners after", winnersAfter, is(expectedWinnersAfter));
    }

	/** Drag piece two directly past piece one's position and make sure that
	 * we don't pick up piece one.
	 */
	@Test
	public void panPastAnother() {
		// SETUP
		Vector2 oldPiece1Centre = new Vector2(120, 50);
		Vector2 oldPiece2Centre = new Vector2(100, 50);
		float deltaX = 20;
		float deltaY = 0;
		Vector2 touch = new Vector2(100, 50);
		Vector2 target1 = touch.cpy().add(deltaX, deltaY);
		Vector2 target2 = target1.cpy().add(deltaX, deltaY);

		List<PlankView> destinations = new ArrayList<PlankView>();

		Piece piece1 = new Piece(0, PieceColour.RED);
		PieceView piece1View = 
				new PieceView(piece1, oldPiece1Centre, 10); // left position
		piece1View.setDestinations(destinations);
		
		Piece piece2 = new Piece(0, PieceColour.RED);
		PieceView piece2View = 
				new PieceView(piece2, oldPiece2Centre, 10); // left position
		piece2View.setDestinations(destinations);
		
		PlankGestureListener listener = 
				new PlankGestureListener(new DummyCamera());
		listener.addView(piece1View);
		listener.addView(piece2View);
		
		// EXEC
		listener.touchDown(touch.x, touch.y, 0, 0);
		listener.pan(target1.x, target1.y, deltaX, deltaY);
		listener.pan(target2.x, target2.y, deltaX, deltaY);
		Vector2 newPiece1Centre = piece1View.getCentre();
		Vector2 newPiece2Centre = piece2View.getCentre();
		
		// VERIFY
		assertThat("new piece 1 centre", newPiece1Centre, is(oldPiece1Centre));
		assertThat("new piece 2 centre", newPiece2Centre, is(target2));
	}

	@Test
	public void panWithProjection() {
		// SETUP
		Vector2 oldPieceCentre = new Vector2(100, 50);
		float deltaX = 10;
		float deltaY = 5;
		Vector2 touch = new Vector2(1080, -450);
		Vector2 target = touch.cpy().add(deltaX, deltaY);
		Vector2 expectedCentre = new Vector2(80 + deltaX, 50 + deltaY);

		List<PlankView> destinations = new ArrayList<PlankView>();
		Piece piece = new Piece(0, PieceColour.RED);
		PieceView pieceView = 
				new PieceView(piece, oldPieceCentre, 10);
		DummyCamera camera = new DummyCamera();
		camera.offset = new Vector3(1000, -500, 0);
		PlankGestureListener listener = 
				new PlankGestureListener(camera);
		pieceView.setDestinations(destinations);
		
		listener.addView(pieceView);
		
		// EXEC
		listener.touchDown(touch.x, touch.y, 0, 0);
		listener.pan(target.x, target.y, deltaX, deltaY);
		Vector2 newPieceCentre = pieceView.getCentre();
		
		// VERIFY
		assertThat("new centre", newPieceCentre, is(expectedCentre));
	}

	@Test
	public void panToLine() {
		// SETUP
		Vector2 oldPlankCentre = new Vector2(200, 50);
		float width = 50;
		float lineHeight = 300;
		float deltaX = 0;
		float deltaY = 100;
		Vector2 touch = new Vector2(220,lineHeight-deltaY);
		Vector2 target1 = touch.cpy().add(deltaX, deltaY);
		Vector2 target2 = target1.cpy().add(deltaX, deltaY);

		List<PlankView> destinations = new ArrayList<PlankView>();
		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView plankView = 
				new PlankView(plank, oldPlankCentre, width); // right position
		plankView.setLineHeight(lineHeight);
		plankView.setDestinations(destinations);

		PlankGestureListener listener = 
				new PlankGestureListener(new DummyCamera());
		listener.addView(plankView);
		
		// EXEC
		listener.touchDown(touch.x, touch.y, 0, 0);
		listener.pan(target1.x, target1.y, deltaX, deltaY);
		listener.pan(target2.x, target2.y, deltaX, deltaY);
		Vector2 newPlankCentre = plankView.getCentre().cpy();
		
		listener.touchDown(target1.x, target1.y, 0, 0);
		listener.pan(target2.x, target2.y, deltaX, deltaY);
		Vector2 plankCentreAfterSeparateMove = plankView.getCentre().cpy();
		
		// VERIFY
        assertThat("new plank centre", newPlankCentre, is(target1));
        assertThat(
                "plank centre after separate move", 
                plankCentreAfterSeparateMove, 
                is(target1));
	}

    @Test
    public void tapPiece() {
        // SETUP
        Vector2 oldPieceCentre = new Vector2(100, 50);
        Vector2 oldPlankCentre = new Vector2(200, 50);
        float width = 50;
        float lineHeight = 300;
        Vector2 touchLeft = new Vector2(80, 50);
        int tapCount = 1;
        int button = 0;

        List<PlankView> destinations = new ArrayList<PlankView>();
        Plank plank = 
                new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
        PlankView plankView = 
                new PlankView(plank, oldPlankCentre, width); // right position
        plankView.setLineHeight(lineHeight);
        plankView.setDestinations(destinations);

        Piece piece = new Piece(0, PieceColour.RED);
        PieceView pieceView = 
                new PieceView(piece, oldPieceCentre, 10); // left position
        pieceView.setDestinations(destinations);
        
        PlankGestureListener listener = 
                new PlankGestureListener(new DummyCamera());
        listener.addView(pieceView);
        listener.addView(plankView);
        
        // EXEC
        listener.tap(touchLeft.x, touchLeft.y, tapCount, button);
        boolean isPlankFlipped = plank.isFlipped();
        
        // VERIFY
        assertThat("is plank flipped", isPlankFlipped, is(false));
    }

    @Test
    public void tapPlank() {
        // SETUP
        Vector2 oldPieceCentre = new Vector2(100, 50);
        Vector2 oldPlankCentre = new Vector2(200, 50);
        float width = 50;
        float lineHeight = 300;
        Vector2 touchRight = new Vector2(220,50);
        int tapCount = 1;
        int button = 0;

        List<PlankView> destinations = new ArrayList<PlankView>();
        Plank plank = 
                new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
        PlankView plankView = 
                new PlankView(plank, oldPlankCentre, width); // right position
        plankView.setLineHeight(lineHeight);
        plankView.setDestinations(destinations);

        Piece piece = new Piece(0, PieceColour.RED);
        PieceView pieceView = 
                new PieceView(piece, oldPieceCentre, 10); // left position
        pieceView.setDestinations(destinations);
        
        PlankGestureListener listener = 
                new PlankGestureListener(new DummyCamera());
        listener.addView(pieceView);
        listener.addView(plankView);
        
        // EXEC
        listener.tap(touchRight.x, touchRight.y, tapCount, button);
        boolean isPlankFlipped = plank.isFlipped();
        
        // VERIFY
        assertThat("is plank flipped", isPlankFlipped, is(true));
    }
	
	/**
	 * Dummy camera that doesn't change a vector when it is projected or
	 * unprojected.
	 * If you want it to do something in project() and unproject(), set an 
	 * offset value.
	 * @author don
	 *
	 */
	private class DummyCamera extends Camera {
		// By default, do nothing
		public Vector3 offset = new Vector3(0, 0, 0);
		
		@Override
		public void project(Vector3 vec) {
			vec.add(offset);
		}
		
		@Override
		public void unproject(Vector3 vec) {
			vec.sub(offset);
		}

		@Override
		public void update() {
		}

		@Override
		public void update(boolean updateFrustum) {
		}
		
	}

}
