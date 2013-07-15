package com.github.donkirkby.plank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;
import com.github.donkirkby.plank.view.GameComponentView;
import com.github.donkirkby.plank.view.PieceView;
import com.github.donkirkby.plank.view.PlankView;

public class PlankGame implements ApplicationListener {
	private OrthographicCamera camera;
	private PlankView plankView;
	private List<PieceView> pieceViews;
	private List<GameComponentView> allViews;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureRegion plankImage;
	private EnumMap<PieceColour, List<TextureRegion>> pieceImages;

	@Override
	public void create() {
		atlas = new TextureAtlas(Gdx.files.internal("plank.pack"));
		List<String> shapeNames = Arrays.asList("circle", "square");
		pieceImages = new EnumMap<PieceColour, List<TextureRegion>>(
				PieceColour.class);
		pieceViews = new ArrayList<PieceView>();
		
		PieceColour[] colours = PieceColour.values();
		for (int i = 0; i < colours.length; i++) {
			PieceColour colour = colours[i];
			List<TextureRegion> colourImages = new ArrayList<TextureRegion>();
			for (int player = 0; player < shapeNames.size(); player++) {
				String shapeName = shapeNames.get(player);
				colourImages.add(atlas.findRegion(
						shapeName + "-" + colour.name().toLowerCase()));
				pieceViews.add(new PieceView(
						new Piece(player, colour), 
						new Vector2(50 + i*50, 50 + player*400), 20));
				pieceViews.add(new PieceView(
						new Piece(player, colour), 
						new Vector2(50 + i*50, 100 + player*300), 20));
			}
			pieceImages.put(colour, colourImages);
		}
		plankImage = atlas.findRegion("plank-rgb");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		
		Plank plank = new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		plankView = new PlankView(plank, new Vector2(800 / 2 - 50 / 2, 100), 50);
		
		allViews = new ArrayList<GameComponentView>();
		allViews.addAll(pieceViews);
		allViews.add(plankView);
		
		for (PieceView pieceView : pieceViews) {
			pieceView.addPossibleDestination(plankView);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(plankImage, plankView.getLeft(), plankView.getBottom());
		for (PieceView pieceView : pieceViews) {
			Piece piece = pieceView.getPiece();
			TextureRegion pieceImage = 
					pieceImages.get(piece.getColour()).get(piece.getPlayer());
			batch.draw(pieceImage, pieceView.getLeft(), pieceView.getBottom());
		}
		batch.end();
		if (Gdx.input.isTouched()) {
			Vector3 touchPos3 = new Vector3();
			touchPos3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos3);
			Vector2 touchPos = new Vector2(touchPos3.x, touchPos3.y);
			GameComponentView touched = 
					GameComponentView.findClosest(touchPos, allViews);
			touched.dragTo(touchPos);
		}
		for (int i = 0; i < pieceViews.size(); i++) {
			GameComponentView pieceView = pieceViews.get(i);
		}
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
