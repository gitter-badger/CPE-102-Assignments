import java.util.List;
import processing.core.PImage;

public abstract class Positionable extends Entity {

	Point position;

	public Positionable(Point position, String name, List<PImage> images) {
		super(name, images);
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}
