import java.util.List;

import processing.core.PImage;

public class OreBlob extends Mover {

	public OreBlob(Point position, String name, int animationRate, int rate,
			List<PImage> images) {
		super(position, name, animationRate, rate, images);
	}

	public boolean toVein(WorldModel world, Vein vein) {
		boolean found = this.toTarget(world, vein);

		// TODO check and remove any ores in the surrounding area

		return found;
	}

	public boolean canMove(WorldModel world, Point pt) {
		return (!world.isOccupied(pt) || world.getTileOccupant(pt) instanceof Ore);
	}

	protected Action createAction(WorldModel world, ImageStore iStore) {
		// TODO: Add action generating code
		return null;
	}
}
