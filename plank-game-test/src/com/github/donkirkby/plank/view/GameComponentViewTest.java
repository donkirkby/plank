package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

public class GameComponentViewTest {

	@Test
	public void findClosest() {
		// SETUP
		GameComponentView pieceView = 
				new PieceView(null, new Vector2(100, 0), 10);
		GameComponentView plankView = 
				new PlankView(null, new Vector2(100, 50), 60);
		List<GameComponentView> components = Arrays.asList(
				pieceView,
				plankView);
		Vector2 highPoint = new Vector2(100, 45);
		Vector2 lowPoint = new Vector2(90, 0);
		Vector2 midPoint = new Vector2(100, 25);
		
		// EXEC
		GameComponentView closestToHighPoint = 
				GameComponentView.findClosest(highPoint, components);
		GameComponentView closestToLowPoint = 
				GameComponentView.findClosest(lowPoint, components);
		GameComponentView closestToMidPoint = 
				GameComponentView.findClosest(midPoint, components);
		
		// VERIFY
		assertThat("high", closestToHighPoint, is(plankView));
		assertThat("low", closestToLowPoint, is(pieceView));
		// Tie goes to earliest in list.
		assertThat("mid", closestToMidPoint, is(pieceView));
	}

}
