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

   public void schedule(WorldModel world, Long ticks, ImageStore iStore) {
      this.
   }
	protected Action createAction(WorldModel world, ImageStore iStore) {
		// TODO: Add action generating code
		return null;
	}

	public static OreBlob createBlob(WorldModel world, String name, Point pt, int rate, long ticks, ImageStore iStore) {
		// TODO: change to random rate generation

		OreBlob blob = new OreBlob(pt, name, 100, rate, iStore.getImages("blob"));
		blob.schedule(world, ticks, iStore);

		return blob;
	}
}
