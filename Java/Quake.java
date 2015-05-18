import java.util.List;

import processing.core.PImage;

public class Quake extends AnimatedActor {
	public Quake(Point position, String name, long animationRate,
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

	public static Quake createQuake(WorldModel world, Point position,
			long ticks, ImageStore iStore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void schedule(WorldModel world, long ticks, ImageStore iStore) {
		scheduleAction(world, ticks, iStore, ticks);
		scheduleAnimation(world, ticks, /*repeatCount=*/ 4);
	}
}
