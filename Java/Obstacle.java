import java.util.List;

import processing.core.PImage;

public class Obstacle extends Positionable {
	private static final int NAME = 1;
	private static final int COL = 2;
	private static final int ROW = 3;

	public Obstacle(Point position, String name, List<PImage> images) {
		super(position, name, images);
	}

	public String entityString() {
		return String.format("obstacle %1$s %2$s", super.getName(), super
				.getPosition().toString());
	}

	public static Positionable createFromProperties(String[] prop,
			ImageStore iStore) {
		Point pos = new Point(Integer.parseInt(prop[COL]), Integer.parseInt(prop[ROW]));
		return new Obstacle(pos, prop[NAME], iStore.getImages("obstacle"));

	}
}
