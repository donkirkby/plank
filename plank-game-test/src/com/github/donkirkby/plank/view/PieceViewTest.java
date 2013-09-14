package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;

public class PieceViewTest {
    private Random random;
    
    @Before
    public void setUp() {
        random = new Random(1);
    }

	@Test
	public void dragToNothing() {
        // SETUP
	    Vector2 oldCentre = new Vector2(200, 100);
	    float deltaX = 10;
	    float deltaY = 20;
	    Vector2 expectedCentre = new Vector2(210, 120);
        PieceView pieceView = createPieceView(oldCentre);
        Vector2 plankCentre = new Vector2(300, 100);
        PlankView plankView = PlankViewTest.createPlankView(plankCentre);

        // EXEC
        pieceView.setDestinations(Arrays.asList(plankView));
        Piece originalPiece = plankView.getPlank().get(1);
        pieceView.panBy(deltaX, deltaY);
        
        Vector2 newCentre = pieceView.getCentre();
        Piece addedPiece = plankView.getPlank().get(1);
        
        // VERIFY
        assertThat("centre", newCentre, is(expectedCentre));
        assertThat("original piece", originalPiece, is(Piece.NULL_PIECE));
        assertThat("added piece", addedPiece, is(Piece.NULL_PIECE));
    }
	
	public static PieceView createPieceView(Random random, Vector2 centre) {
	    float radius = 10 + 50*random.nextFloat();
	    PieceColour[] pieceColours = PieceColour.values();
	    PieceColour pieceColour = pieceColours[random.nextInt(pieceColours.length)];
	    return createPieceView(random, centre, radius, pieceColour);
	}

    public static PieceView createPieceView(
            Random random, 
            Vector2 centre, 
            float radius, 
            PieceColour pieceColour) {
        int player = random.nextInt(4);
        return createPieceView(player, centre, radius, pieceColour);
    }

    public static PieceView createPieceView(int player, Vector2 centre,
            float radius, PieceColour pieceColour) {
        Actor actor = new Actor();
        actor.setWidth(radius * 2);
        actor.setHeight(radius * 2);
        Piece piece = new Piece(player, pieceColour);
        PieceView pieceView = new PieceView(piece, actor);
        pieceView.setCentre(centre);
        return pieceView;
    }
    
    private PieceView createPieceView(Vector2 centre) {
        return createPieceView(random, centre);
    }

    private PieceView createPieceView(
            Vector2 centre, 
            float radius, 
            PieceColour pieceColour) {
        return createPieceView(random, centre, radius, pieceColour);
    }

    @Test
    public void snapTo() {
        // SETUP
        float radius = 10;
        Vector2 oldCentre = new Vector2(200, 100);
        float deltaX = 103;
        float deltaY = 0;
        Vector2 plankCentre = new Vector2(300, 100);
        PieceView pieceView = 
                createPieceView(oldCentre, radius, PieceColour.BLUE);
        PlankView plankView = PlankViewTest.createPlankView(
                plankCentre,
                PieceColour.RED,
                PieceColour.BLUE,
                PieceColour.GREEN);

        // EXEC
        pieceView.setDestinations(Arrays.asList(plankView));
        pieceView.panBy(deltaX, deltaY);
        
        Vector2 newCentre = pieceView.getCentre();
        Piece addedPiece = plankView.getPlank().get(1);
        
        // VERIFY
        assertThat("centre", newCentre, is(plankCentre));
        assertThat("added piece", addedPiece, is(pieceView.getPiece()));
    }

    @Test
    public void snapAndRelease() {
        // SETUP
        float radius = 10;
        Vector2 oldCentre = new Vector2(200, 100);
        float deltaX1 = 106;
        float deltaX2 = 6;
        float deltaY = 0;
        Vector2 plankCentre = new Vector2(300, 100);
        Vector2 expectedCentre2 = new Vector2(312, 100);
        PieceView pieceView = 
                createPieceView(oldCentre, radius, PieceColour.BLUE);
        PlankView plankView = PlankViewTest.createPlankView(
                plankCentre,
                PieceColour.RED,
                PieceColour.BLUE,
                PieceColour.GREEN);

        // EXEC
        pieceView.setDestinations(Arrays.asList(plankView));
        pieceView.panBy(deltaX1, deltaY);
        
        Vector2 newCentre1 = pieceView.getCentre().cpy();
        pieceView.panBy(deltaX2, deltaY);
        Vector2 newCentre2 = pieceView.getCentre().cpy();
        
        // VERIFY
        assertThat("centre 1", newCentre1, is(plankCentre));
        assertThat("centre 2", newCentre2, is(expectedCentre2));
    }

	@Test
	public void snapToMultiplePlanks() {
        // SETUP
        float radius = 10;
        Vector2 oldCentre = new Vector2(200, 100);
        Vector2 plankCentre1 = new Vector2(300, 100);
        Vector2 plankCentre2 = new Vector2(400, 100);
        PieceView pieceView = 
                createPieceView(oldCentre, radius, PieceColour.BLUE);
        PlankView plankView1 = PlankViewTest.createPlankView(
                plankCentre1,
                PieceColour.RED,
                PieceColour.BLUE,
                PieceColour.GREEN);
        PlankView plankView2 = PlankViewTest.createPlankView(
                plankCentre2,
                PieceColour.BLUE,
                PieceColour.RED,
                PieceColour.GREEN);
        Vector2 expectedCentre2 = 
                plankCentre2.cpy().add(0, plankView2.getActor().getHeight()/3);
        Vector2 target1 = plankCentre1.cpy().add(radius / 3, 0);
        Vector2 target2 = expectedCentre2.cpy().add(radius / 3, 0);

        // EXEC
        pieceView.setDestinations(Arrays.asList(plankView1, plankView2));
        pieceView.panBy(target1.x - oldCentre.x, target1.y - oldCentre.y);
        Vector2 newCentre1 = pieceView.getCentre().cpy();
        pieceView.panBy(target2.x - target1.x, target2.y - target1.y);
        
        Vector2 newCentre2 = pieceView.getCentre();
        Piece removedPiece = plankView1.getPlank().get(1);
        Piece addedPiece = plankView2.getPlank().get(0);
        
        // VERIFY
        assertThat("centre 1", newCentre1, is(plankCentre1));
        assertThat("centre 2", newCentre2, is(expectedCentre2));
        assertThat("removed piece", removedPiece, is(Piece.NULL_PIECE));
        assertThat("added piece", addedPiece, is(pieceView.getPiece()));
	}

	@Test
	public void snapToNonCentre() {
        // SETUP
        float radius = 10;
        Vector2 oldCentre = new Vector2(200, 100);
        Vector2 plankCentre = new Vector2(300, 100);
        PieceView pieceView = 
                createPieceView(oldCentre, radius, PieceColour.RED);
        PlankView plankView = PlankViewTest.createPlankView(
                plankCentre,
                PieceColour.RED,
                PieceColour.BLUE,
                PieceColour.GREEN);
        Vector2 expectedCentre = 
                plankCentre.cpy().add(0, plankView.getActor().getHeight()/3);
        Vector2 target = expectedCentre.cpy().add(0, -radius/3);

        // EXEC
        pieceView.setDestinations(Arrays.asList(plankView));
        pieceView.panBy(target.x - oldCentre.x, target.y - oldCentre.y);
        Vector2 newCentre = pieceView.getCentre();
        
        // VERIFY
        assertThat("centre", newCentre, is(expectedCentre));
	}

    @Test
    public void reset() {
        // SETUP
        Vector2 oldCentre = new Vector2(200, 100);
        float deltaX = 10;
        float deltaY = 20;
        PieceView pieceView = 
                createPieceView(oldCentre);

        // EXEC
        pieceView.setDestinations(new ArrayList<PlankView>());
        
        pieceView.panBy(deltaX, deltaY);
        pieceView.reset();
        PlankViewTest.completeActions(pieceView);
        
        // VERIFY
        assertThat(
                "centre",
                pieceView.getCentre(),
                is(oldCentre));
    }

    @Test
    public void panAfterReset() {
        // SETUP
        Vector2 oldCentre = new Vector2(200, 100);
        Vector2 target = new Vector2(210, 120);
        float deltaX = target.x - oldCentre.x;
        float deltaY = target.y - oldCentre.y;
        PieceView pieceView = 
                createPieceView(oldCentre);

        // EXEC
        pieceView.setDestinations(new ArrayList<PlankView>());
        
        pieceView.panBy(deltaX, deltaY);
        pieceView.reset();
        PlankViewTest.completeActions(pieceView);
        pieceView.panBy(deltaX, deltaY);
        
        // VERIFY
        assertThat(
                "centre",
                pieceView.getCentre(),
                is(target));
    }

    @Test
    public void resetInvalid() {
        // SETUP
        float oldX = 200;
        float oldY = 100;
        
        Piece piece = new Piece(1, PieceColour.BLUE);
        Actor actor = new Actor();
        actor.setPosition(oldX, oldY);
        PieceView pieceView = new PieceView(piece, actor); 

        // EXEC
        pieceView.reset();
        float newX = actor.getX();
        float newY = actor.getY();
        
        // VERIFY
        assertThat("new X", newX, is(oldX));
        assertThat("new Y", newY, is(oldY));
    }
    
    @Test
    public void string() {
        // SETUP
        PieceView pieceView = 
                createPieceView(0, new Vector2(100, 5), 20, PieceColour.BLUE);
        
        // EXEC
        String string = pieceView.toString();
        
        // VERIFY
        assertThat(string, is("blue piece at 100,5"));
    }
}
