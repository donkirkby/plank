package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.donkirkby.plank.model.PieceColour;

public class ViewStateTest {
    private Random random;
    
    @Before
    public void setUp() {
        random = new Random(1);
    }
    
    @Test
    public void reset() {
        // SETUP
        int startHeight = 50;
        Vector2 oldPieceCentre = new Vector2(100, startHeight);
        Vector2 oldPlankCentre = new Vector2(200, startHeight);
        float lineHeight = 300;
        float deltaX = 10;
        float deltaY = lineHeight - startHeight;
        
        PlankView plankView = PlankViewTest.createPlankView(oldPlankCentre);
        plankView.setLineHeight(lineHeight);
        
        PieceView pieceView = 
                PieceViewTest.createPieceView(random, oldPieceCentre);
        
        ViewState viewState = new ViewState();
        viewState.addView(pieceView);
        viewState.addView(plankView);

        // EXEC
        pieceView.panBy(deltaX, deltaY);
        plankView.panBy(deltaX, deltaY);
        int placedBeforeReset = viewState.getPlacedPlankViews().size();
        viewState.reset();
        int placedAfterReset = viewState.getPlacedPlankViews().size();
        PlankViewTest.completeActions(pieceView);
        PlankViewTest.completeActions(plankView);
        
        // VERIFY
        assertThat(
                "piece centre",
                pieceView.getCentre(),
                is(oldPieceCentre));
        assertThat(
                "plank centre",
                plankView.getCentre(),
                is(oldPlankCentre));
        assertThat(
                "placed planks before reset", 
                placedBeforeReset, 
                is(1));
        assertThat(
                "placed planks after reset", 
                placedAfterReset, 
                is(0));
    }

    @Test
    public void findWin() {
        // SETUP
        float lineHeight = 50;
        Vector2 oldRedCentre = new Vector2(100, 100);
        Vector2 oldBlueCentre = new Vector2(100, 50);
        Vector2 oldGreenCentre = new Vector2(100, 0);
        Vector2 plankTop = new Vector2(200, 100);
        Vector2 oldPlankCentre = new Vector2(200, 0);
        Vector2 newPlankCentre = new Vector2(200, lineHeight);
        Vector2 plankBottom = new Vector2(200, 0);

        PlankView plankView = PlankViewTest.createPlankView(
                oldPlankCentre,  // right position 
                PieceColour.RED, 
                PieceColour.BLUE, 
                PieceColour.GREEN);
        plankView.setLineHeight(lineHeight);

        int player = 0;
        int radius = 10;
        PieceView redPieceView = 
                PieceViewTest.createPieceView(
                        player, 
                        oldRedCentre, // left position 
                        radius, 
                        PieceColour.RED);
        PieceView bluePieceView = 
                PieceViewTest.createPieceView(
                        player, 
                        oldBlueCentre, // left position 
                        radius, 
                        PieceColour.BLUE);
        PieceView greenPieceView = 
                PieceViewTest.createPieceView(
                        player, 
                        oldGreenCentre, // left position 
                        radius, 
                        PieceColour.GREEN);
        
        float highlightSize = greenPieceView.getActor().getHeight();
        Actor highlight1 = createSquareActor(highlightSize);
        Actor highlight2 = createSquareActor(highlightSize);
        Actor highlight3 = createSquareActor(highlightSize);
        
        ViewState viewState = new ViewState();
        viewState.addView(redPieceView);
        viewState.addView(bluePieceView);
        viewState.addView(greenPieceView);
        viewState.addView(plankView);
        viewState.addHighlight(highlight1);
        viewState.addHighlight(highlight2);
        viewState.addHighlight(highlight3);
        
        // EXEC
        // place plank
        plankView.panBy(
                newPlankCentre.x - oldPlankCentre.x, 
                newPlankCentre.y - oldPlankCentre.y);
        
        // place three pieces in winning line.
        redPieceView.panBy(
                plankTop.x - oldRedCentre.x, 
                plankTop.y - oldRedCentre.y);
        bluePieceView.panBy(
                newPlankCentre.x - oldBlueCentre.x, 
                newPlankCentre.y - oldBlueCentre.y);
        viewState.displayWinners();
        boolean isVisibleBefore = highlight1.isVisible();
        greenPieceView.panBy(
                plankBottom.x - oldGreenCentre.x, 
                plankBottom.y - oldGreenCentre.y);
        viewState.displayWinners();
        boolean isVisibleAfter = highlight1.isVisible();
        Vector2 highlight1Position = new Vector2(
                highlight1.getX(),
                highlight1.getY());
        Vector2 redPiecePosition = new Vector2(
                redPieceView.getActor().getX(),
                redPieceView.getActor().getY());
        
        viewState.reset();
        viewState.displayWinners();
        boolean isVisibleAfterReset = highlight1.isVisible();
        
        // VERIFY
        assertThat("visible before", isVisibleBefore, is(false));
        assertThat("visible after", isVisibleAfter, is(true));
        assertThat("visible after reset", isVisibleAfterReset, is(false));
        assertThat("highlight position", highlight1Position, is(redPiecePosition));
    }
    
    private Actor createSquareActor(float size) {
        Actor actor = new Actor();
        actor.setWidth(size);
        actor.setHeight(size);
        return actor;
    }
}
