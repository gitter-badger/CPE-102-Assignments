import java.util.List;
import processing.core.*;

public class WorldView {
	private Point topLeft;
	private Point bottomRight;
	private Point worldSize;
	private float tileWidth;
	private float tileHeight;

	public WorldView(String string, Point viewGrid, Point window, Point worldSize) {
		topLeft = new Point(0,0);
		this.worldSize = new Point(worldSize.getX(), worldSize.getY());
		bottomRight = new Point(viewGrid.getX(), viewGrid.getY());
		tileWidth =  window.getX()/viewGrid.getX();
		tileHeight = window.getY()/viewGrid.getY();
	}

	public void draw(Main main, WorldModel world) {
		drawBackground(main, world);
		drawEntities(main, world);		
	}
	
	public void move(Point delta){
		if (topLeft.getX() + delta.getX() >= 0 && bottomRight.getX() + delta.getX() < worldSize.getX() &&
			topLeft.getY() + delta.getY() >= 0 && bottomRight.getY() + delta.getY() < worldSize.getY()) {	
			
			topLeft.setX(topLeft.getX() + delta.getX());
			bottomRight.setX(bottomRight.getX() + delta.getX());
			topLeft.setY(topLeft.getY() + delta.getY());
			bottomRight.setY(bottomRight.getY() + delta.getY());
		}
	}
	
	private void drawEntities(Main main, WorldModel world) {
	 List<Positionable> entities = world.getEntities();
	 
	 for (Positionable a : entities){
		 if (inView(a)){
			 main.image(a.getImage(), 
					 (a.getPosition().getX() - topLeft.getX()) * tileWidth,
					 (a.getPosition().getY() - topLeft.getY()) * tileHeight, 
					 tileWidth, tileHeight);
		 }
	 }
		
	}

	private void drawBackground(PApplet main, WorldModel world){
		for (int i = topLeft.getY(); i < bottomRight.getY(); i++){
			for (int j = topLeft.getX(); j < bottomRight.getX(); j++){
				main.image(world.getBackground(new Point(j, i)).getImage(),
						(j - topLeft.getX()) * tileWidth, (i - topLeft.getY()) * tileHeight,
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
