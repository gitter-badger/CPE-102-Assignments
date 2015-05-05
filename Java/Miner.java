
public class Miner extends Mover {

	private int resourceLimit;
	private int resource;
	
	public Miner(Point position, String name, int animationRate, int rate,
			int resourceLimit, int resource) {
		super(position, name, animationRate, rate);
		this.resourceLimit = resourceLimit;
		this.resource = resource;
	}

	public boolean canMove(int world, Point pt) {
		
		return !world.isOccupied(pt);
	}

}
