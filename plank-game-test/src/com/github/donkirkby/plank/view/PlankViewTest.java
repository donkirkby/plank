package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PlankViewTest {
    private Random random;
    
    @Before
    public void setUp() {
        random = new Random(1);
    }

    // Test that the pan event is wired correctly and centre calculations work.
    @Test
    public void pan() {
        // SETUP
        Vector2 oldCentre = new Vector2(200, 225);
        PlankView plankView = createPlankView(oldCentre);

        Actor actor = plankView.getActor();
        Array<EventListener> listeners = actor.getListeners();
        float x = random.nextFloat();
        float y = random.nextFloat();
        float deltaX = 5;
        float deltaY = -3;
        
        // EXEC
        plankView.setCentre(oldCentre);
        float oldActorX = actor.getX();
        float oldActorY = actor.getY();
        ((ActorGestureListener)listeners.get(0)).pan(null, x, y, deltaX, deltaY);
        Vector2 newCentre = plankView.getCentre();
        
        // VERIFY
        assertThat("old actor X", oldActorX, is(175f)); // 200 - 50/2
        assertThat("old actor Y", oldActorY, is(150f)); // 225 - 150/2
        assertThat("actor X", actor.getX(), is(180f));
        assertThat("actor Y", actor.getY(), is(147f));
        assertThat("centre", newCentre, is(new Vector2(205, 222)));
    }

    @Test
	public void panBy() {
		// SETUP
        Vector2 oldCentre = new Vector2(200, 100);
        PlankView plankView = createPlankView(oldCentre);

        float deltaX = 10;
        float deltaY = 6;
        Vector2 expectedCentre = new Vector2(210, 106);

		// EXEC
		plankView.panBy(deltaX, deltaY);
		Vector2 newCentre = plankView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(expectedCentre));
	}

    public static PlankView createPlankView(Vector2 centre) {
        PieceColour space1 = PieceColour.RED;
        PieceColour space2 = PieceColour.BLUE;
        PieceColour space3 = PieceColour.GREEN;
        return createPlankView(centre, space1, space2, space3);
    }

    public static PlankView createPlankView(
            Vector2 centre,
            PieceColour space1, 
            PieceColour space2, 
            PieceColour space3) {
        Actor actor = new Actor();
        actor.setWidth(50);
        actor.setHeight(150);
        Plank plank = 
                new Plank(space1, space2, space3);
        PlankView plankView = 
                new PlankView(plank, actor);
        plankView.setCentre(centre);
        return plankView;
    }

	@Test
	public void panToEndOfLine() {
		// SETUP
		float width = 50;
		float lineHeight = 300;
		Vector2 firstPlankCentre = new Vector2(100, lineHeight);
		Vector2 oldCentre = new Vector2(200, 100);
		float deltaX = 70;
		float deltaY = 220;
		Vector2 expectedCentre = new Vector2(250, lineHeight);

		List<PlankView> destinations = 
				createThreeDestinations(firstPlankCentre, width);
		PlankView draggingPlankView = createPlankView(oldCentre);
		draggingPlankView.setLineHeight(lineHeight);
		draggingPlankView.setDestinations(destinations);
		
		// EXEC
		draggingPlankView.panBy(deltaX, deltaY);
		Vector2 newCentre = draggingPlankView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(expectedCentre));
		assertThat("last destination", destinations.get(3), is(draggingPlankView));
	}

	private List<PlankView> createThreeDestinations(
	        Vector2 firstPlankCentre,
			float width) {
		List<PlankView> destinations = new ArrayList<PlankView>();
		for (int i = 0; i < 3; i++) {
		    destinations.add(createPlankView(
		            firstPlankCentre.cpy().add(i*width, 0)));
		}
		return destinations;
	}

	@Test
	public void panToStartOfLine() {
	    // SETUP
        float width = 50;
        float lineHeight = 300;
        Vector2 firstPlankCentre = new Vector2(100, lineHeight);
        Vector2 oldCentre = new Vector2(200, 100);
        float deltaX = -155;
        float deltaY = 220;
        Vector2 expectedCentre = new Vector2(50, lineHeight);

        List<PlankView> destinations = 
                createThreeDestinations(firstPlankCentre, width);
        PlankView draggingPlankView = createPlankView(oldCentre);
        draggingPlankView.setLineHeight(lineHeight);
        draggingPlankView.setDestinations(destinations);
        
        // EXEC
        draggingPlankView.panBy(deltaX, deltaY);
        Vector2 newCentre = draggingPlankView.getCentre();
        
        // VERIFY
        assertThat("centre", newCentre, is(expectedCentre));
        assertThat("first destination", destinations.get(0), is(draggingPlankView));
	}

	@Test
	public void panToEmptyLine() {
        // SETUP
        float lineHeight = 300;
        Vector2 oldCentre = new Vector2(200, 100);
        float deltaX = -155;
        float deltaY = 220;
        Vector2 expectedCentre = new Vector2(45, lineHeight);

        List<PlankView> destinations = new ArrayList<PlankView>();
        PlankView draggingPlankView = createPlankView(oldCentre);
        draggingPlankView.setLineHeight(lineHeight);
        draggingPlankView.setDestinations(destinations);
        
        // EXEC
        draggingPlankView.panBy(deltaX, deltaY);
        Vector2 newCentre = draggingPlankView.getCentre();
        draggingPlankView.panBy(deltaX, deltaY); // ignored after snapping
        Vector2 newCentre2 = draggingPlankView.getCentre();
        draggingPlankView.tap(); // ignored after snapping
        
        // VERIFY
        assertThat("centre", newCentre, is(expectedCentre));
        assertThat(
                "first destination", 
                destinations.get(0), 
                is(draggingPlankView));
        assertThat("centre 2", newCentre2, is(expectedCentre));
        assertThat(
                "is flipped", 
                draggingPlankView.getPlank().isFlipped(), 
                is(false));
	}
	
	@Test
	public void tap() {
        // SETUP
        Vector2 oldCentre = 
                new Vector2(random.nextInt()%1000, random.nextInt()%1000);
        PlankView plankView = createPlankView(oldCentre);

        Actor actor = plankView.getActor();
        Array<EventListener> listeners = actor.getListeners();
        float x = random.nextInt();
        float y = random.nextInt();
        int count = random.nextInt();
        int button = random.nextInt();
        InputEvent event = null;
        Plank plank = plankView.getPlank();
        
        // EXEC
        boolean defaultPlankFlip = plank.isFlipped();
        ((ActorGestureListener)listeners.get(0)).tap(event, x, y, count, button);
        boolean isPlankFlipped = plank.isFlipped();
        Vector2 newCentre = plankView.getCentre();
        completeActions(plankView);
        
        // VERIFY
        assertThat("centre", newCentre, is(oldCentre));
        assertThat("plank flipped by default", defaultPlankFlip, is(false));
        assertThat("plank flipped after", isPlankFlipped, is(true));
        assertThat("rotation", plankView.getActor().getRotation(), is(180f));
	}

    @Test
    public void reset() {
        // SETUP
        Vector2 oldCentre = new Vector2(200, 100);
        float radius = 10;
        Vector2 oldPlankCentre = new Vector2(250, 50);
        Vector2 newPlankCentre = new Vector2(300, 100);
        Vector2 target = newPlankCentre.cpy().add(radius / 2, 0);

        Actor actor = new Actor();
        actor.setWidth(radius * 2);
        actor.setHeight(radius * 2);
        int player = 1;
        Piece piece = new Piece(player, PieceColour.BLUE);
        PieceView pieceView = new PieceView(piece, actor);
        pieceView.setCentre(oldCentre);

        PlankView plankView = createPlankView(
                oldPlankCentre, 
                PieceColour.RED, 
                PieceColour.BLUE, 
                PieceColour.GREEN);
        
        // EXEC
        plankView.panBy(
                newPlankCentre.x - oldPlankCentre.x, 
                newPlankCentre.y - oldPlankCentre.y);
        pieceView.setDestinations(Arrays.asList(plankView));
        pieceView.panBy(target.x - oldCentre.x, target.y - oldCentre.y);
        plankView.reset();
        Piece addedPiece = plankView.getPlank().get(1);
        completeActions(plankView);
        
        // VERIFY
        assertThat("added piece", addedPiece, is(Piece.NULL_PIECE));
        assertThat("centre", plankView.getCentre(), is(oldPlankCentre));
    }

    @Test
    public void resetRotated() {
        // SETUP
        Vector2 centre = new Vector2(100, 50);
        PlankView plankView = createPlankView(centre);
        
        // EXEC
        plankView.tap();
        plankView.reset();
        boolean isFlipped = plankView.getPlank().isFlipped();
        completeActions(plankView);
        // VERIFY
        assertThat("is flipped", isFlipped, is(false));
        assertThat("rotation", plankView.getActor().getRotation(), is(0f));
    }
    
    public static void completeActions(GameComponentView view) {
        Actor actor = view.getActor();
        Array<Action> actions = actor.getActions();
        for (Action action : actions) {
            while ( ! action.act(1000)) {
                // wait for action to complete
            }
        }
        actor.clearActions();
        // Avoid rounding errors by assuming all positions are integers.
        Vector2 centre = view.getCentre();
        centre.set(Math.round(centre.x), Math.round(centre.y));
        view.setCentre(centre);
    }
}
