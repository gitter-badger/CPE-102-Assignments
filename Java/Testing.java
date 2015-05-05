import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


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
		
		//TODO canMove(WorldModel world, Pointpt)
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
	public void testPositionable() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testQuake() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testVein() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testWorldModel() {
		fail("Not yet implemented");
	}

}
