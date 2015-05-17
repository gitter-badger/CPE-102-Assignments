import java.util.List;

import processing.core.PImage;

public class Blacksmith extends Actor {
	private static final int NAME = 1;
	private static final int COL = 2;
	private static final int ROW = 3;
	private static final int LIMIT = 4;
	private static final int RATE = 5;
	private static final int REACH = 6;

	private int rate;
	private int resourceLimit;
	private int resourceCount;
	private int resourceDistance;

	public Blacksmith(Point position, String name, int rate, int resourceLimit,
		int resourceDistance, List<PImage> images) {
		super(position, name, images);
		this.rate = rate;
		this.resourceLimit = resourceLimit;
		this.resourceCount = 0;
		this.resourceDistance = resourceDistance;
	}

	public void setResourceCount(int resourceCount) {
		this.resourceCount = resourceCount;
	}

	public int getResourceLimit() {
		return resourceLimit;
	}

	public int getResourceCount() {
		return resourceCount;
	}

	public int getResourceDistance() {
		return resourceDistance;
	}

	public String entityString() {
		return String.format("blacksmith %1$s %2$s %3$s %4$s %5$s",
				super.getName(), super.getPosition().toString(),
				this.resourceLimit, this.rate, this.resourceDistance);
	}

	public static Positionable createFromProperties(String[] prop,
			ImageStore iStore) {
		Point pos = new Point(Integer.parseInt(prop[COL]), Integer.parseInt(prop[ROW]));
		int reach = Integer.parseInt(prop[REACH]);
		int rate = Integer.parseInt(prop[RATE]);
		int limit = Integer.parseInt(prop[LIMIT]);
		return new Blacksmith(pos, prop[NAME], rate, limit, reach, iStore.getImages("blacksmith"));

	}
}
