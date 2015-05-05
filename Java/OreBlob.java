
public class OreBlob 
	extends Mover {

	public OreBlob(Point position, String name, int animationRate, int rate) {
		super(position, name, animationRate, rate);
	}

	public boolean toVein(World world, Vein vein){
		boolean toReturn = this.toTarget(world, vein)
	}
	public boolean canMove(int world, Point pt) {
		return (!world.isOccupied(pt) || world.getTileOccupant(pt) instanceof Ore);
	}
	

}
