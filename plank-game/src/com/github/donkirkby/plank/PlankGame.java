package com.github.donkirkby.plank;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;
import com.github.donkirkby.plank.view.PieceView;
import com.github.donkirkby.plank.view.PlankView;
import com.github.donkirkby.plank.view.ViewState;

public class PlankGame implements ApplicationListener {
	private TextureAtlas atlas;
    private ViewState viewState;
    private Stage stage;

	@Override
	public void create() {
        atlas = new TextureAtlas(Gdx.files.internal("atlas/plank.pack"));
        
        boolean keepAspectRatio = true;
        stage = new Stage(800, 480, keepAspectRatio);

        createRefreshButton();
        Gdx.input.setInputProcessor(stage);

		viewState = new ViewState();
        
		List<String> shapeNames = 
				Arrays.asList("circle", "square", "triangle", "octagon");
		
		createPlanks(shapeNames.size());
		createPieces(shapeNames);
		createHighlights();
	}

    private void createPieces(List<String> shapeNames) {
        PieceColour[] colours = PieceColour.values();
        for (int i = 0; i < colours.length; i++) {
			PieceColour colour = colours[i];
			if (colour == PieceColour.NONE) {
                continue;
            }
			for (int player = 0; player < shapeNames.size(); player++) {
				String shapeName = shapeNames.get(player);
				String colourName = colour.name().toLowerCase();
				String imageName = shapeName + "-" + colourName;
				int playerSide = player % 2;
				int playerLevel = player / 2 % 2;
				int pieceX = 50 + i*50 + playerSide * 600;
				PieceView piece1 = new PieceView(
						new Piece(player, colour),
						createImage(imageName));
				piece1.setCentre(new Vector2(pieceX, 50 + playerLevel*350));
				PieceView piece2 = new PieceView(
                        new Piece(player, colour),
                        createImage(imageName));
                piece2.setCentre(new Vector2(pieceX, 100 + playerLevel*350));
                stage.addActor(piece1.getActor());
                stage.addActor(piece2.getActor());
				viewState.addView(piece1);
				viewState.addView(piece2);
			}
		}
    }

    private void createPlanks(int playerCount) {
        PieceColour[][] plankColourSets = {
				{PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE},
				{PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN},
				{PieceColour.BLUE, PieceColour.RED, PieceColour.GREEN}
		};
		for (int i = 0; i < plankColourSets.length; i++) {
			PieceColour[] pieceColours = plankColourSets[i];
			StringBuilder builder = new StringBuilder("plank-");
			for (PieceColour pieceColour : pieceColours) {
				builder.append(pieceColour.name().toLowerCase().charAt(0));
			}
			String imageName = builder.toString();
			for (int player = 0; player < playerCount; player++) {
				for (int j = 0; j < 2; j++) {
					Plank plank = new Plank(
							pieceColours[0], 
							pieceColours[1], 
							pieceColours[2]);
					Actor plankImage = createImage(imageName);
					PlankView plankView = new PlankView(plank, plankImage);
					plankView.setCentre(new Vector2(
							250 + i * 120 + j * 60, 
							80 + player*320)); 
					plankView.setLineHeight(240);
					stage.addActor(plankView.getActor());
					viewState.addView(plankView);
				}
			}
		}
    }
    
    private void createHighlights() {
        for (int i = 0; i < 3; i++) {
            Image image = createImage("highlight");
            image.setTouchable(Touchable.disabled);
            stage.addActor(image);
            viewState.addHighlight(image);
        }
    }

    private void createRefreshButton() {
        final Image refreshButton = createImage("refresh");
        stage.addActor(refreshButton);
        refreshButton.setPosition(
                refreshButton.getWidth() * 0.25f, 
                (stage.getCamera().viewportHeight-refreshButton.getHeight())/2);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                viewState.reset();
                refreshButton.addAction(Actions.rotateBy(-90f, 0.5f));
            }
        });
    }

    private Image createImage(String imageName) {
        Image image = new Image(atlas.findRegion("images/" + imageName));
        image.setOrigin(image.getWidth()/2, image.getHeight()/2);
        return image;
    }

	@Override
	public void dispose() {
		atlas.dispose();
        stage.dispose();
	}

	@Override
	public void render() {
	    viewState.displayWinners();

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
