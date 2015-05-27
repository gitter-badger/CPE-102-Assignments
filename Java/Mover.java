import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import processing.core.PImage;

public abstract class Mover extends AnimatedActor {

	public int rate;

	public Mover(Point position, String name, int animationRate, int rate,
			List<PImage> images) {
		super(position, name, animationRate, images);
		this.rate = rate;
	}

	public int getRate() {
		return rate;
	}

	public void schedule(WorldModel world, long ticks, ImageStore iStore) {
		scheduleAction(world, ticks, iStore, rate);
		scheduleAnimation(world, ticks, /* repeatCount= */0);
	}

	public List<Point> aStar(WorldModel world, Point goal) {
		// Set up open set and closed set with the first open node as the start
		List<AStarNode> openSet = new ArrayList<AStarNode>();
		openSet.add(new AStarNode(this.getPosition(), 0, goal, null));
		List<AStarNode> closeSet = new ArrayList<AStarNode>();

		// define a comparator for sorting nodes by their f values
		Comparator<AStarNode> byFVal = (AStarNode one, AStarNode two) -> {
			return Integer.compare(one.getFScore(), two.getFScore());
		};

		while (openSet.size() > 0) {
			openSet.sort(byFVal);
			AStarNode cur = openSet.get(0);

			if (cur.getLoc().equals(goal)) { // goal reached
				return reconstructPath(cur);
			}

			// remove cur from openset and add to close set
			closeSet.add(cur);
			openSet.remove(cur);

			List<Point> neighbors = getNeighbors(cur.getLoc());

			for (Point neighbor : neighbors) {// node has already been
				if (closeSet.contains(neighbor)) // visited													
					continue; // and deemed not best path
				if (!canMove(world, neighbor))
					continue; // this point is occupied

				AStarNode neighborNode = new AStarNode(neighbor,
						cur.getGScore() + 1, goal, cur);

				int index = openSet.indexOf(neighborNode);
				if (index != -1
						&& openSet.get(index).getGScore() > neighborNode
								.getGScore()) {
					openSet.remove(index); // a better way to get to this point
											// has been discovered
				} // remove old way from openSet (new way will be added below)

				if (!openSet.contains(neighborNode)) {
					openSet.add(neighborNode);
				}
			}
		}

		// no path found
		return null;
	}

	private List<Point> reconstructPath(AStarNode current) {
		List<Point> path = new LinkedList<Point>();
		Point toAdd = current.getLoc();
		while (toAdd != this.getPosition()) {
			path.add(0, toAdd);
			current = current.getCameFrom();
			toAdd = current.getLoc();
		}
		return path;
	}

	private List<Point> getNeighbors(Point cur) {
		List<Point> toReturn = new ArrayList<Point>(4);
		toReturn.add(new Point(cur.getX() - 1, cur.getY()));
		toReturn.add(new Point(cur.getX() + 1, cur.getY()));
		toReturn.add(new Point(cur.getX(), cur.getY() - 1));
		toReturn.add(new Point(cur.getX(), cur.getY() + 1));
		return toReturn;
	}

	public Point nextPosition(WorldModel world, Point destination) {
		int horizontal = Mover.sign(destination.getX()
				- this.getPosition().getX());
		Point newPt = new Point(this.getPosition().getX() + horizontal, this
				.getPosition().getY());

		if (horizontal == 0 || !this.canMove(world, newPt)) {
			int vertical = Mover.sign(destination.getY()
					- this.getPosition().getY());
			newPt = new Point(this.getPosition().getX(), this.getPosition()
					.getY() + vertical);

			if (vertical == 0 || !this.canMove(world, newPt)) {
				newPt = new Point(this.getPosition().getX(), this.getPosition()
						.getY());
			}
		}
		return newPt;
	}

	public boolean toTarget(WorldModel world, Positionable destination) {
		if (destination == null) {
			return false;
		} else if (adjacent(this.getPosition(), destination.getPosition())) {
			return true;
		} else {
			world.moveEntity(this,
					this.nextPosition(world, destination.getPosition()));
			return false;
		}
	}

	public abstract boolean canMove(WorldModel world, Point pt);

	private static int sign(int x) {
		if (x < 0)
			return -1;
		else if (x > 0)
			return 1;
		else
			return 0;
	}

	protected static boolean adjacent(Point one, Point two) {
		return (one.getX() == two.getX() && Math.abs(one.getY() - two.getY()) == 1)
				|| (one.getY() == two.getY() && Math.abs(one.getX()
						- two.getX()) == 1);
	}
}
