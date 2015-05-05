import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;

public class Testing {

	@Test
	public void testBackground() {
		Background b = new Background("A name");
		
		assertTrue(b.entityString().equals("Background"));
		assertTrue(b.getName().equals("A name"));
	}
	
	@Test
	public void testBlacksmith() {
		Blacksmith bm = new Blacksmith(new Point(1,2), "name", 3, 4, 5);
		
		assertEquals(bm.entityString(), "blacksmith name Point{x=1, y=2} 4 3 5");
		assertEquals(bm.getResourceCount(), 0);
		assertEquals(bm.getResourceLimit(), 4);
		assertEquals(bm.getResourceDistance(), 5);
		
		bm.setResourceCount(6);
		assertEquals(bm.getResourceCount(), 6);
	}
	
	@Test
	public void testMiner() {
		Miner m = new Miner(new Point(1,2), "name", 3, 4, 5, 6);
		
		assertEquals(m.getRate(), 4);
		assertEquals(m.getAnimationRate(), 3, .001);
		
		//TODO canMove(WorldModel world, Point pt)		
	}

	@Test
	public void testObstacle() {
		Obstacle ob = new Obstacle(new Point(1,2), "name");
		
		assertEquals(ob.entityString(), "obstacle name Point{x=1, y=2}");
	}
	
	@Test
	public void testOre() {
		Ore ore = new Ore(new Point(1,2), "name", 3);
		
		assertEquals(ore.entityString(), "ore name Point{x=1, y=2} 3");
	}
	
	@Test
	public void testOreBlob() {
		OreBlob ob = new OreBlob(new Point(1, 2), "name", 1, 2);
		
		//TODO toVein(WorldModel world, Vein vein)
		
		//TODO canMove(WorldModel world, Point pt)
	}
	
	@Test
	public void testPoint() {
		Point pt = new Point(1,2);
		
		assertEquals(pt.getX(), 1);
		assertEquals(pt.getY(), 2);
		
		pt.setX(3);
		pt.setY(4);;
		
		assertEquals(pt.getX(), 3);
		assertEquals(pt.getY(), 4);
		
		assertTrue(pt.equals(new Point(3, 4)));
		assertFalse(pt.equals(new Point(1, 2)));
		
		assertEquals(pt.toString(), "Point{x=3, y=4}");
	}
	
	@Test
	public void testQuake() {
		Quake q = new Quake(new Point(1,2), "name", 3);
		
		assertEquals(q.getPosition(), new Point(1, 2));
		q.setPosition(new Point(3, 4));
		assertEquals(q.getPosition(), new Point(3, 4));
	}
	
	@Test
	public void testVein() {
		Vein v = new Vein(new Point(1,2), "name", 3, 4);
		
		assertEquals(v.entityString(), "vein name Point{x=1, y=2} 3");
	}
	
	@Test
	public void testWorldModel() {
      Background b = new Background("Default");
      Point testPoint = new Point(1, 1);
      Point testPoint2 = new Point(2, 5);
      Point testPoint3 = new Point(11, 10);
      Positionable testEntity = new Obstacle(testPoint, "Obsty");
      Positionable testEntity2 = new Ore(testPoint2, "Ore", 5000);
	   List<Positionable> entityList;

      WorldModel world = new WorldModel(10, 10, b);

      // Testing getBackground
      assertEquals(world.getBackground(testPoint).getName(), "Default");
      assertEquals(world.getBackground(testPoint3), null);

      // Testing setBackground
      world.setBackground(testPoint2, new Background("New"));
      assertEquals(world.getBackground(testPoint2).getName(), "New");

      // Testing addEntity, isOccupied, and getTileOccupant
      world.addEntity(testEntity);
      assertTrue(world.isOccupied(testPoint));
      assertEquals(world.getTileOccupant(testPoint).getName(),"Obsty");

      // Testing removeEntity, isOccupied, and getTileOccupant
      world.removeEntity(testEntity);
      assertFalse(world.isOccupied(testPoint));
      assertEquals(world.getTileOccupant(testPoint), null);

      // Testing getEntities
      testEntity.setPosition(testPoint); // removeEntity sets it to -1, -1 *facepalm*.
      world.addEntity(testEntity);
      world.addEntity(testEntity2);
      entityList = world.getEntities();

      assertEquals(entityList.get(entityList.indexOf(testEntity)).getName(),
                   "Obsty");
      assertEquals(entityList.get(entityList.indexOf(testEntity2)).getName(),
                  "Ore");

      // Testing removeEntityAt
      world.removeEntityAt(testPoint);
      assertFalse(world.isOccupied(testPoint));

      // Testing moveEntity
      testEntity.setPosition(testPoint);
      world.moveEntity(testEntity, new Point(6, 7));
      assertEquals(world.getTileOccupant(new Point(6, 7)).getName(),
                   "Obsty");
      assertFalse(world.isOccupied(testPoint));

      //Testing nearest Functions
      Positionable[] entityArray = {
         new Vein(new Point(2, 2), "NearVein", 1, 1),
         new Vein(new Point(9, 9), "FarVein", 1, 1),
         new Ore(new Point(3, 3), "NearOre", 1),
         new Ore(new Point(8, 8), "FarOre", 1),
         new Blacksmith(new Point(4, 4), "NearSmith", 1, 1, 1),
         new Blacksmith(new Point(7, 7), "FarSmith", 1, 1, 1)
      };

      for(Positionable e : entityArray) {
         world.addEntity(e);
      }

      Positionable nearestTarget = world.findNearestVein(testPoint);
      assertEquals(nearestTarget.getName(), "NearVein");

      nearestTarget = world.findNearestOre(testPoint);
      assertEquals(nearestTarget.getName(), "NearOre");

      nearestTarget = world.findNearestBlacksmith(testPoint);
      assertEquals(nearestTarget.getName(), "NearSmith");

      // Testing findOpenNear
      Point nearOpen = world.findOpenNear(new Point(3, 3), 1);
      assertEquals(nearOpen.getX(), 3);
      assertEquals(nearOpen.getY(), 2);

      world.addEntity(new Obstacle(new Point(3, 2), "Blocker"));

      nearOpen = world.findOpenNear(new Point(3, 3), 1);
      assertEquals(nearOpen.getX(), 4);
      assertEquals(nearOpen.getY(), 2);

      world.addEntity(new Obstacle(new Point(0, 0), "Blocker"));
      world.addEntity(new Obstacle(new Point(1, 0), "Blocker"));
      world.addEntity(new Obstacle(new Point(0, 1), "Blocker"));
      world.addEntity(new Obstacle(new Point(2, 0), "Blocker"));
      world.addEntity(new Obstacle(new Point(0, 2), "Blocker"));
      world.addEntity(new Obstacle(new Point(1, 1), "Blocker"));
      world.addEntity(new Obstacle(new Point(1, 2), "Blocker"));
      world.addEntity(new Obstacle(new Point(2, 1), "Blocker"));

      nearOpen = world.findOpenNear(new Point(1, 1), 1);
      assertEquals(nearOpen, null);
   }

}
