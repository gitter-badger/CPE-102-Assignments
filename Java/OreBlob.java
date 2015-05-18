import java.util.List;

import processing.core.PImage;

public class OreBlob extends Mover {

	public OreBlob(Point position, String name, int animationRate, int rate,
			List<PImage> images) {
		super(position, name, animationRate, rate, images);
	}


	public boolean toTarget(WorldModel world, Positionable destination) {
		if (destination == null) {
			return false;
		} else if (adjacent(this.getPosition(), destination.getPosition())) {
			return true;
		} else {
         Point nextpt = this.nextPosition(world, destination.getPosition());
         world.removeEntityAt(nextpt);
			world.moveEntity(this, nextpt);
			return false;
		}
	}

	public boolean canMove(WorldModel world, Point pt) {
		return (!world.isOccupied(pt) || world.getTileOccupant(pt) instanceof Ore);
	}

	protected Action createAction(WorldModel world, ImageStore iStore) {
		Action[] actions = { null };
		actions[0] = (long ticks)-> {
			removePendingAction(actions[0]);

			Vein vein = world.findNearestVein(getPosition());
			boolean atVein = toTarget(world, vein);

			long delay = this.rate;
			if (atVein){
				world.removeEntity(vein);
				Quake quake = Quake.createQuake(world,
						vein.getPosition(), ticks, iStore);
				world.addEntity(quake);
				delay *= 2;
			}

			this.scheduleAction(world, ticks, iStore, delay);

			return this.getPosition();
		};
		return actions[0];
	}

	public static OreBlob createBlob(WorldModel world, String name, Point pt, int rate, long ticks, ImageStore iStore) {
		// TODO: change to random rate generation

		OreBlob blob = new OreBlob(pt, name, 100, rate, iStore.getImages("blob"));
		blob.schedule(world, ticks, iStore);

		return blob;
	}
}
