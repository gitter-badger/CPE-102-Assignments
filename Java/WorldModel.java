import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Scanner;

public class WorldModel {

	private int rows;
	private int columns;
	private Grid background;
	private Grid occupancy;
	private List<Positionable> entities;
   public static final int typeKey = 0; //Seriously Java why you no enum properly?
   public static final int nameKey = 1;
   public static final int columnKey = 2;
   public static final int rowKey = 3;

	public WorldModel(int rows, int columns, Background background) {
		this.rows = rows;
		this.columns = columns;
		this.background = new Grid(columns, rows, background);
		this.occupancy = new Grid(columns, rows, null);
		this.entities = new ArrayList<Positionable>();
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
		removeEntityAt(entity.getPosition());
	}

	public void removeEntityAt(Point pt) {
		if (isOccupied(pt)) {
			Positionable entity = (Positionable) getTileOccupant(pt);
			entity.setPosition(new Point(-1, -1)); // WAT Let's change this at
												         	// some point.
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

	public Positionable findNearestVein(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		for (Positionable e : entities) {
			if (e instanceof Vein) {
				candidates.add(e);
			}
		}

		return nearest(pt, candidates);
	}

	public Positionable findNearestOre(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		;
		for (Positionable e : entities) {
			if (e instanceof Ore) {
				candidates.add(e);
			}
		}

		return nearest(pt, candidates);
	}

	public Positionable findNearestBlacksmith(Point pt) {
		List<Positionable> candidates = new ArrayList<Positionable>();
		;
		for (Positionable e : entities) {
			if (e instanceof Blacksmith) {
				candidates.add(e);
			}
		}

		return nearest(pt, candidates);
	}

   public void loadFromSave(ImageStore iStore, String filename) {
      try {
         Scanner in = new Scanner(new FileInputStream(filename));
         while(in.hasNextLine()) {
            String[] properties = in.nextLine().split("\\s+");
            if(properties.length > 0) {
               if(properties[typeKey].equals("background")) {
                  addBackground(properties, iStore);
               }
               else {
                  createAddEntity(properties, iStore);
               }
            }
         }
         in.close();
      }
      catch(FileNotFoundException e) {
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
      if(newEntity instanceof Actor) {
         //TODO define schedule for all Actors then uncomment these lines

         //Actor actingEntity = (Actor)newEntity;
         //actingEntity.schedule();
      }
   }

   public Positionable createFromProperties(String[] properties, ImageStore iStore) {
      //TODO define createFromProperties for the following.

      String key = properties[typeKey];
      if(key.equals("miner")) {
         return Miner.createFromProperties(properties, iStore);
      }
      else if(key.equals("vein")) {
         return Vein.createFromProperties(properties, iStore);
      }
      else if(key.equals("ore")) {
         return Ore.createFromProperties(properties, iStore);
      }
      else if(key.equals("blacksmith")) {
         return Blacksmith.createFromProperties(properties, iStore);
      }
      else if(key.equals("obstacle")) {
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
