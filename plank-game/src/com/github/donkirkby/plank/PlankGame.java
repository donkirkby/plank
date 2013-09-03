package com.github.donkirkby.plank;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;
import com.github.donkirkby.plank.view.GameComponentView;
import com.github.donkirkby.plank.view.PieceView;
import com.github.donkirkby.plank.view.PlankGestureListener;
import com.github.donkirkby.plank.view.PlankView;
import com.github.donkirkby.plank.view.TextureImage;

public class PlankGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureImage highlight;
    private PlankGestureListener gestureListener;
    private Stage stage;
    private Button refreshButton;

	@Override
	public void create() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        atlas = new TextureAtlas(Gdx.files.internal("atlas/plank.pack"));
        
        stage = new Stage();

        createRefreshButton(skin);
        stage.addActor(refreshButton);
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		gestureListener = new PlankGestureListener(camera);
		stage.addListener(gestureListener);
        
        batch = new SpriteBatch();
		
		highlight = findRegion("images/highlight");
		List<String> shapeNames = 
				Arrays.asList("circle", "square", "triangle", "octagon");
		
		PieceColour[] colours = PieceColour.values();
		for (int i = 0; i < colours.length; i++) {
			PieceColour colour = colours[i];
			if (colour == PieceColour.NONE) {
                continue;
            }
			for (int player = 0; player < shapeNames.size(); player++) {
				String shapeName = shapeNames.get(player);
				String colourName = colour.name().toLowerCase();
				String imageName = "images/" + shapeName + "-" + colourName;
				TextureImage image = findRegion(imageName);
				int playerSide = player % 2;
				int playerLevel = player / 2 % 2;
				int pieceX = 50 + i*50 + playerSide * 600;
				PieceView piece1 = new PieceView(
						new Piece(player, colour), 
						new Vector2(pieceX, 50 + playerLevel*350), 20);
				PieceView piece2 = new PieceView(
						new Piece(player, colour), 
						new Vector2(pieceX, 100 + playerLevel*350), 20);
				piece1.setImage(image);
				piece2.setImage(image);
				gestureListener.addView(piece1);
				gestureListener.addView(piece2);
			}
		}

		PieceColour[][] plankColourSets = {
				{PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE},
				{PieceColour.RED, PieceColour.BLUE, PieceColour.GREEN},
				{PieceColour.BLUE, PieceColour.RED, PieceColour.GREEN}
		};
		for (int i = 0; i < plankColourSets.length; i++) {
			PieceColour[] pieceColours = plankColourSets[i];
			StringBuilder builder = new StringBuilder("images/plank-");
			for (PieceColour pieceColour : pieceColours) {
				builder.append(pieceColour.name().toLowerCase().charAt(0));
			}
			String imageName = builder.toString();
			for (int player = 0; player < shapeNames.size(); player++) {
				for (int j = 0; j < 2; j++) {
					Plank plank = new Plank(
							pieceColours[0], 
							pieceColours[1], 
							pieceColours[2]);
					PlankView plankView = new PlankView(
							plank, 
							new Vector2(
									250 + i * 120 + j * 60, 
									80 + player*320), 
							50);
                    plankView.setImage(findRegion(imageName));
					plankView.setLineHeight(240);
					gestureListener.addView(plankView);
				}
			}
		}
	}

    private void createRefreshButton(Skin skin) {
        ButtonStyle refreshStyle = new ButtonStyle(skin.get(ButtonStyle.class));
        refreshStyle.up = 
                new TextureRegionDrawable(atlas.findRegion("images/refresh"));
        refreshStyle.down = refreshStyle.up;
        refreshButton = new Button(refreshStyle);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gestureListener.reset();
                event.cancel();
            }
        });
    }

	private TextureImage findRegion(String regionName) {
		AtlasRegion region = atlas.findRegion(regionName);
		if (region == null) {
			throw new IllegalArgumentException(
					"Region not found, '" + regionName + "'.");
		}
		return new TextureImage(region, batch);
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
        stage.dispose();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (GameComponentView view : gestureListener.getAllViews()) {
		    view.draw();
		}
		PieceView[] winners = gestureListener.getWinners();
		if (winners[0] != null) {
            for (int i = 0; i < winners.length; i++) {
                PieceView winner = winners[i];
                highlight.draw(winner.getLeft() + 10, winner.getBottom() + 10);
            }
        }
		batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
        refreshButton.setPosition(
                refreshButton.getWidth() * 0.25f, 
                (height-refreshButton.getHeight())/2);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
