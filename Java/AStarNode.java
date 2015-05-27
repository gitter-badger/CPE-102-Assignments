
public class AStarNode {
	private Point loc;
	private int gScore;
	private int fScore;
	
	public AStarNode(Point location, int gScore, Point goal){
		this.loc = location;
		this.gScore = gScore;
		this.fScore = gScore + calculateHeuristic(goal);
	}

	public Point getLoc() {
		return loc;
	}

	public int getgScore() {
		return gScore;
	}

	public int getfScore() {
		return fScore;
	}
	
	private int calculateHeuristic(Point goal){
		// TODO: compute estimate of distance to goal)
		return Math.abs(loc.getX() - goal.getX()) + Math.abs(loc.getY() - goal.getY());
	}
}
