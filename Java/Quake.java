import java.util.List;

import processing.core.PImage;

public class Quake extends AnimatedActor {
	public Quake(Point position, String name, double animationRate,
			List<PImage> images) {
		super(position, name, animationRate, images);
	}

	protected Action createAction(WorldModel world, ImageStore iStore) {
		// TODO: Add action generating code
		return null;
	}
}
