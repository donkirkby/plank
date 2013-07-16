package com.github.donkirkby.plank.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameComponentView {

	private Vector2 centre;
	private TextureRegion image;
	
	protected GameComponentView(Vector2 centre) {
		this.centre = centre;
	}

	/**
	 * Get the position of this component's centre. Do not modify!
	 */
	public Vector2 getCentre() {
		return centre;
	}
	
	public void setCentre(Vector2 centre) {
		this.centre.set(centre);
	}
	
	public void setCentre(float x, float y) {
		centre.set(x, y);
	}
	
	public abstract float getBottom();

	public abstract float getLeft();

	public TextureRegion getImage() {
		return image;
	}

	public void setImage(TextureRegion image) {
		this.image = image;
	}

	public abstract void dragTo(Vector2 target);

	/**
	 * Find the game component that is closest to a certain point.
	 * @param point the point to compare to
	 * @param components the list of components to search
	 * @return the closest member of components, ties are broken by earlier
	 * position in the list.
	 */
	public static GameComponentView findClosest(
			Vector2 point,
			Iterable<GameComponentView> components) {
		GameComponentView closestComponent = null;
		float shortestDistance2 = -1;
		for (GameComponentView component : components) {
			float distance2 = point.dst2(component.getCentre());
			if (shortestDistance2 < 0 || distance2 <= shortestDistance2) {
				shortestDistance2 = distance2;
				closestComponent = component;
			}
		}
		return closestComponent;
	}

}