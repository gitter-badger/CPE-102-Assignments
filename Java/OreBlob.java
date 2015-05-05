public class OreBlob extends Mover {

	public OreBlob(Point position, String name, int animationRate, int rate) {
		super(position, name, animationRate, rate);
	}

	public boolean toVein(WorldModel world, Vein vein) {
		boolean found = this.toTarget(world, vein);

		// TODO check and remove any ores in the surrounding area

		return found;
	}

	public boolean canMove(WorldModel world, Point pt) {
		return (!world.isOccupied(pt) || world.getTileOccupant(pt) instanceof Ore);
	}

}
