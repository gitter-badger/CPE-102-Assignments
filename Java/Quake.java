import java.util.List;

import processing.core.PImage;

public class Quake extends AnimatedActor {
	public Quake(Point position, String name, double animationRate,
			List<PImage> images) {
		super(position, name, animationRate, images);
	}

	protected Action createAction(WorldModel world, ImageStore iStore) {
		Action[] actions = {null};
		
		actions[0] = (long ticks) -> {
			removePendingAction(actions[0]);
			Point pt = getPosition();
			world.removeEntity(this);
			return pt;
		};
		
		return actions[0];
	}
}
