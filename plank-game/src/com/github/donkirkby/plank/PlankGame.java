package com.github.donkirkby.plank;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.donkirkby.plank.model.Piece;
import com.github.donkirkby.plank.model.PieceColour;
import com.github.donkirkby.plank.model.Plank;
import com.github.donkirkby.plank.view.PieceView;
import com.github.donkirkby.plank.view.PlankView;

public class PlankGame implements ApplicationListener {
	private OrthographicCamera camera;
	Plank plank;
	PlankView plankView;
	Piece piece;
	PieceView pieceView;
	private SpriteBatch batch;
	TextureAtlas atlas;
	TextureRegion plankImage;
	TextureRegion pieceImage;

	@Override
	public void create() {
		atlas = new TextureAtlas(Gdx.files.internal("plank.pack"));
		pieceImage = atlas.findRegion("circle-red");
		plankImage = atlas.findRegion("plank-rgb");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		
		piece = new Piece(PieceColour.RED);
		pieceView = new PieceView(piece, new Vector2(200/2 - 40/2, 480/2), 20);

		plank = new Plank(PieceColour.RED, PieceColour.GREEN, PieceColour.BLUE);
		plankView = new PlankView(plank, new Vector2(800 / 2 - 50 / 2, 100), 50);
		
		pieceView.addPossibleDestination(plankView);
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
		batch.draw(pieceImage, pieceView.getLeft(), pieceView.getBottom());
		batch.end();
		if (Gdx.input.isTouched()) {
			Vector3 touchPos3 = new Vector3();
			touchPos3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos3);
			Vector2 touchPos = new Vector2(touchPos3.x, touchPos3.y);
			pieceView.dragTo(touchPos);
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
