import java.util.List;

import processing.core.PImage;

public class Miner extends Mover {
	private static final int NAME = 1;
	private static final int COL = 2;
	private static final int ROW = 3;
	private static final int LIMIT = 4;
	private static final int RATE = 5;
	private static final int ANIMATION_RATE = 6;

	private int resourceLimit;
	private int resource;

	public Miner(Point position, String name, int animationRate, int rate,
			int resourceLimit, int resource, List<PImage> images) {
		super(position, name, animationRate, rate, images);
		this.resourceLimit = resourceLimit;
		this.resource = resource;
	}

	public boolean canMove(WorldModel world, Point pt) {

		return !world.isOccupied(pt);
	}

	public static Positionable createFromProperties(String[] prop,
			ImageStore iStore) {
		Point pos = new Point(Integer.parseInt(prop[COL]),
				Integer.parseInt(prop[ROW]));
		int aRate = Integer.parseInt(prop[ANIMATION_RATE]);
		int rate = Integer.parseInt(prop[RATE]);
		int limit = Integer.parseInt(prop[LIMIT]);
		return new Miner(pos, prop[NAME], aRate, rate, limit, 0,
				iStore.getImages("miner"));
	}

}
