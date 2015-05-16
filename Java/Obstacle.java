import java.util.List;
import processing.core.PImage;

public class Obstacle extends Positionable {

	public Obstacle(Point position, String name, List<PImage> images) {
		super(position, name, images);
	}

	public String entityString() {
		return String.format("obstacle %1$s %2$s", super.getName(), super
				.getPosition().toString());
	}
}
