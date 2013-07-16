package com.github.donkirkby.plank.view;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;

public class PlankViewTest {

	@Test
	public void dragTo() {
		// SETUP
		Vector2 oldCentre = new Vector2(200, 100);
		float width = 25;
		Vector2 target = new Vector2(210, 120);

		Plank plank = 
				new Plank(PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN);
		PlankView plankView = 
				new PlankView(plank, oldCentre, width);
		
		// EXEC
		plankView.dragTo(target);
		Vector2 newCentre = plankView.getCentre();
		
		// VERIFY
		assertThat("centre", newCentre, is(target));
	}
}
