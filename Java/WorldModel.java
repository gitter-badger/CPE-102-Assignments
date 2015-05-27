import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;
import java.util.Iterator;
import java.util.PriorityQueue;

public class WorldModel {
	public static final int typeKey = 0;
	public static final int nameKey = 1;
	public static final int columnKey = 2;
	public static final int rowKey = 3;

	private int rows;
	private int columns;
	private Grid background;
	private Grid occupancy;
	private List<Positionable> entities;

	private PriorityQueue<ScheduledAction> actionQueue;

	public WorldModel(int rows, int columns, Background background) {
		this.rows = rows;
		this.columns = columns;
		this.background = new Grid(columns, rows, background);
		this.occupancy = new Grid(columns, rows, null);
		this.entities = new ArrayList<Positionable>();
		actionQueue = new PriorityQueue<ScheduledAction>();
	}

	public List<Point> updateOnTime(Long ticks) {
		List<Point> tiles = new ArrayList<Point>();

		ScheduledAction next = actionQueue.peek();
		if (next != null) {
			while (next.getTime() < ticks) {
				actionQueue.poll();
				tiles.add(next.getAction().AnAction(System.currentTimeMillis()));
				next = actionQueue.peek();
			}
		}

		return tiles;
	}

	public void clearPendingAtions(Actor entity) {
		for (Action a : entity.getPendingActions()) {
			unscheduleAction(a);
		}
		entity.clearPendingActions();
	}

	public void scheduleAction(Actor entity, Action action, Long time) {
		entity.addPendingAction(action);
		actionQueue.add(new ScheduledAction(action, time));
	}

	public void unscheduleAction(Action action) {
		// just needed to wrap the action in a scheduled action
		actionQueue.remove(new ScheduledAction(action, 0));
	}

	public Background getBackground(Point pt) {
		if (withinBounds(pt)) {
			return (Background) background.getCell(pt);
		} else
			return null;
	}

	public void setBackground(Point pt, Background background) {
		if (withinBounds(pt)) {
			this.background.setCell(pt, background);
		}
	}

	public Entity getTileOccupant(Point pt) {
		if (withinBounds(pt)) {
			return occupancy.getCell(pt);
		}
		return null;
	}

	public List<Positionable> getEntities() {
		return entities;
	}

	public boolean isOccupied(Point pt) {
		return withinBounds(pt) && getTileOccupant(pt) != null;
	}

	public Point findOpenNear(Point pt, int distance) {
		for (int dy = 0 - distance; dy <= distance; dy++) {
			for (int dx = 0 - distance; dx <= distance; dx++) {
				Point newPt = new Point(pt.getX() + dx, pt.getY() + dy);

				if (withinBounds(newPt) && !(isOccupied(newPt))) {
					return newPt;
				}
			}
		}
		return null;
	}

	public void addEntity(Positionable entity) {
		Point pt = entity.getPosition();

		if (withinBounds(pt)) {
			occupancy.setCell(pt, entity);
			entities.add(entity);
		}
	}

	public void removeEntity(Positionable entity) {
		if (entity instanceof Actor) {
			clearPendingAtions((Actor) entity);

		}
		removeEntityAt(entity.getPosition());
	}

	private void removeEntityAt(Point pt) {
		if (isOccupied(pt)) {
			Positionable entity = (Positionable) getTileOccupant(pt);
			entities.remove(entity);
			occupancy.setCell(pt, null);
		}
	}

	public List<Point> moveEntity(Positionable entity, Point pt) {
		List<Point> tiles = new ArrayList<Point>();

		if (withinBounds(pt)) {
			Point oldPt = entity.getPosition();

			if (withinBounds(oldPt)) {
				occupancy.setCell(oldPt, null);
			}

			occupancy.setCell(pt, entity);
			entity.setPosition(pt);

			tiles.add(oldPt);
			tiles.add(pt);
		}

		return tiles;
	}

	public Vein findNearestVein(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		for (Positionable e : entities) {
			if (e instanceof Vein) {
				candidates.add(e);
			}
		}

		return (Vein) nearest(pt, candidates);
	}

	public Ore findNearestOre(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		;
		for (Positionable e : entities) {
			if (e instanceof Ore) {
				candidates.add(e);
			}
		}

		return (Ore) nearest(pt, candidates);
	}

	public Blacksmith findNearestBlacksmith(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		;
		for (Positionable e : entities) {
			if (e instanceof Blacksmith) {
				candidates.add(e);
			}
		}

		return (Blacksmith) nearest(pt, candidates);
	}

	public void loadFromSave(ImageStore iStore, String filename) {
		try {
			Scanner in = new Scanner(new FileInputStream(filename));
			while (in.hasNextLine()) {
				String[] properties = in.nextLine().split("\\s+");
				if (properties.length > 0) {
					if (properties[typeKey].equals("background")) {
						addBackground(properties, iStore);
					} else {
						createAddEntity(properties, iStore);
					}
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void addBackground(String[] properties, ImageStore iStore) {
		String name = properties[nameKey];
		Point pt = new Point(Integer.parseInt(properties[columnKey]),
				Integer.parseInt(properties[rowKey]));
		setBackground(pt, new Background(name, iStore.getImages(name)));
	}

	public void createAddEntity(String[] properties, ImageStore iStore) {
		Positionable newEntity = createFromProperties(properties, iStore);
		addEntity(newEntity);
		if (newEntity instanceof Actor) {
			Actor actingEntity = (Actor) newEntity;
			actingEntity.schedule(this, (long) 0, iStore);
		}
	}

	public Positionable createFromProperties(String[] properties,
			ImageStore iStore) {

		String key = properties[typeKey];
		if (key.equals("miner")) {
			return Miner.createFromProperties(properties, iStore);
		} else if (key.equals("vein")) {
			return Vein.createFromProperties(properties, iStore);
		} else if (key.equals("ore")) {
			return Ore.createFromProperties(properties, iStore);
		} else if (key.equals("blacksmith")) {
			return Blacksmith.createFromProperties(properties, iStore);
		} else if (key.equals("obstacle")) {
			return Obstacle.createFromProperties(properties, iStore);
		}
		return null;
	}

	private boolean withinBounds(Point pt) {
		return (pt.getX() >= 0 && pt.getX() < columns)
				&& (pt.getY() >= 0 && pt.getY() < rows);
	}

	private static Positionable nearest(Point pt, List<Positionable> candidates) {
		if (candidates.size() == 0) {
			return null;
		}
		Positionable minEntity = candidates.get(0);
		int minDistance = distanceSq(pt, minEntity.getPosition());

		for (Positionable e : candidates) {
			int newDistance = distanceSq(pt, e.getPosition());
			if (newDistance < minDistance) {
				minDistance = newDistance;
				minEntity = e;
			}
		}
		return minEntity;
	}

	private static int distanceSq(Point pt1, Point pt2) {
		int deltaX = pt1.getX() - pt2.getX();
		int deltaY = pt1.getY() - pt2.getY();

		return deltaX * deltaX + deltaY * deltaY;
	}
}
