import java.util.List;
import processing.core.PImage;

// Background class

public class Background extends Entity {

	public Background(String name, List<PImage> images) {
		super(name, images);
	}

	public String entityString() {
		return "Background";
	}

}
