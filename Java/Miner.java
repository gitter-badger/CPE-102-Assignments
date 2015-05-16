import java.util.List;
import processing.core.PImage;

public class Miner extends Mover {

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

}
