import java.util.List;
import processing.core.*;

public class WorldView {
	private Point topLeft;
	private Point bottomRight;
	private Point dimensions;
	private float tileWidth;
	private float tileHeight;

	public WorldView(String string, int viewGridWidth, 
			int viewGridHeight, int windowWidth, int windowHeight) {
		topLeft = new Point(0,0);
		dimensions = new Point(windowWidth, windowHeight);
		bottomRight = new Point(viewGridWidth, viewGridHeight);
		tileWidth =  windowWidth/viewGridWidth;
		tileHeight = windowHeight/viewGridHeight;
	}

	public void draw(Main main, WorldModel world) {
		drawBackground(main, world);
		drawEntities(main, world);		
	}
	
	private void drawEntities(Main main, WorldModel world) {
	 List<Positionable> entities = world.getEntities();
	 
	 for (Positionable a : entities){
		 if (inView(a)){
			 main.image(a.getImage(), 
					 (topLeft.getX() - a.getPosition().getX()) * tileWidth,
					 (topLeft.getY() - a.getPosition().getY()) * tileHeight, 
					 tileWidth, tileHeight);
		 }
	 }
		
	}

	private void drawBackground(PApplet main, WorldModel world){
		for (int i = topLeft.getY(); i < bottomRight.getY(); i++){
			for (int j = topLeft.getX(); j < bottomRight.getX(); j++){
				main.image(world.getBackground(new Point(j, i)).getImage(),
						j * tileWidth, i * tileHeight,
						tileWidth, tileHeight);
			}
		}
	}

	private boolean inView(Positionable entity){
		Point pos = entity.getPosition();
		return pos.getX() >= topLeft.getX() && 
				pos.getX() < bottomRight.getX() &&
				pos.getY() >= topLeft.getY() && 
				pos.getY() < bottomRight.getY();
	}
}
