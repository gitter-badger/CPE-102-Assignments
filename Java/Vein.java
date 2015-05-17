import java.util.List;

import processing.core.PImage;

public class Vein extends Actor {
	private static final int NAME = 1;
	private static final int COL = 2;
	private static final int ROW = 3;
	private static final int RATE = 4;
	private static final int REACH = 5;
	
	private int rate;
	private int resourceDistance;

	public Vein(Point position, String name, int rate, int resourceDistance,
      List<PImage> images) {
		super(position, name, images);
		this.rate = rate;
		this.resourceDistance = resourceDistance;
	}

	public String entityString() {
		return String.format("vein %1$s %2$s %3$s", super.getName(), super
				.getPosition().toString(), this.rate);
	}

	public static Positionable createFromProperties(String[] prop,
			ImageStore iStore) {
		Point pos = new Point(Integer.parseInt(prop[COL]), Integer.parseInt(prop[ROW]));
		int rate = Integer.parseInt(prop[RATE]);
		int reach = Integer.parseInt(prop[REACH]);
		return new Vein(pos, prop[NAME], rate, reach, iStore.getImages("vein"));
	}
}
