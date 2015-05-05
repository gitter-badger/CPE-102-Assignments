public abstract class Mover
    extends Quake{

    public int rate;

    public Mover(Point position, String name, int animationRate, int rate){
        super(position, name, animationRate);
        this.rate = rate;
    }

    public int getRate(){
        return rate;
    }

    public Point nextPosition(int world, Point destination){
        int horizontal = Mover.sign(destination.getX() - this.getPosition().getX());
        Point newPt = new Point(this.getPosition().getX() + horizontal, this.getPosition().getY());
        
        if (horizontal == 0 || !this.canMove(4, newPt)){
        	int vertical = Mover.sign(destination.getY() - this.getPosition().getY());
        	newPt = new Point(this.getPosition().getX(), this.getPosition().getY() + vertical);
        	
        	if (vertical == 0 || !this.canMove(4, newPt)){
        		newPt = new Point(this.getPosition().getX(), this.getPosition().getY());
        	}
        }
        return newPt;
    }
    public boolean toTarget(World world, Positionable destination){
    	if (destination == null){
    		return false;
    	}
    	else if (adjacent(this.getPosition(), destination.getPosition())){
    		return true;
    	}
    	else{
    		world.moveEntity(this, this.nextPosition(world, destination.getPosition()));
    		return false;
    	}
    }
    
    public abstract boolean canMove(int world, Point pt);

    private static int sign(int x){
        if (x < 0)
            return -1;
        else if (x > 0)
            return 1;
        else return 0;
    }
    private static boolean adjacent(Point one, Point two){
    	return (one.getX() == two.getX() && Math.abs(one.getY() - two.getY()) == 1) ||
    				(one.getY() == two.getY() && Math.abs(one.getX() - two.getX()) == 1);
    }    
}
