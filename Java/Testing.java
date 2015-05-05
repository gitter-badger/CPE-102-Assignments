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
		
		assertTrue(b.entitiyString().equals("Background"));
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
		fail("Not yet implemented");
	}

	@Test
	public void testObstacle() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testOre() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testOreBlob() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testPoint() {
		fail("Not yet implemented");
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
