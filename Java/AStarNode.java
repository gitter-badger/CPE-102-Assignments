public class AStarNode {
	private AStarNode cameFrom;
	private Point loc;
	private int gScore;
	private int fScore;

	public AStarNode(Point location, int gScore, Point goal, AStarNode cameFrom) {
		this.loc = location;
		this.gScore = gScore;
		this.fScore = gScore + calculateHeuristic(goal);
		this.cameFrom = cameFrom;
	}
	public AStarNode(Point location){
		this.loc = location;
		gScore = 0;
		fScore = 0;
		cameFrom = null;
	}

	public Point getLoc() {
		return loc;
	}

	public int getGScore() {
		return gScore;
	}

	public int getFScore() {
		return fScore;
	}

	private int calculateHeuristic(Point goal) {
		return AStarNode.calculateHeuristic(loc, goal);
	}

	public static int calculateHeuristic(Point from, Point to) {
		return Math.abs(from.getX() - to.getX())
				+ Math.abs(from.getY() - to.getY());
	}

	public AStarNode getCameFrom() {
		return cameFrom;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AStarNode other = (AStarNode) obj;
		return this.loc.equals(other.loc);
	}
}
